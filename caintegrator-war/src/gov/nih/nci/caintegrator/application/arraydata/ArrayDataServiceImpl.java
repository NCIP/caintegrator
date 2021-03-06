/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.application.query.FoldChangeCriterionHandler;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the array data service (backed by NetCDF).
 */
@Service("arrayDataService")
@Transactional (propagation = Propagation.REQUIRED)
public class ArrayDataServiceImpl implements ArrayDataService {

    private static final Logger LOGGER = Logger.getLogger(ArrayDataServiceImpl.class);

    private CaIntegrator2Dao dao;
    private FileManager fileManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayDataValues getData(DataRetrievalRequest request) {
        return new NetCDFManager(getFileManager()).retrieveValues(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(ArrayDataValues values) {
        for (ArrayData arrayData : values.getArrayDatas()) {
            dao.save(arrayData);
        }
        new NetCDFManager(getFileManager()).storeValues(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformConfiguration loadArrayDesign(PlatformConfiguration platformConfiguration) {
        AbstractPlatformSource platformSource = platformConfiguration.getPlatformSource();
        platformConfiguration = getRefreshedPlatformConfiguration(platformConfiguration.getId());
        try {
            LOGGER.info("Platform named " + platformConfiguration.getName() + " has started to load.");
            getDao().setFlushMode(HibernateAccessor.FLUSH_COMMIT);
            Platform platform = platformSource.getLoader().load(getDao());
            LOGGER.info("Completed loading design from " + platformSource.toString());
            platformConfiguration.setPlatform(platform);
            platformConfiguration.setStatus(Status.LOADED);
            platformConfiguration.setDeploymentFinishDate(new Date());
            if (platformConfiguration.getDeploymentStartDate() != null) {
                platformConfiguration.setStatusDescription("Load time: "
                    + DateUtil.compareDatesInMinutes(platformConfiguration.getDeploymentStartDate(),
                            platformConfiguration.getDeploymentFinishDate()) + " minute(s)");
            } else {
                platformConfiguration.setStatusDescription("Load time: Unknown");
            }
            dao.save(platformConfiguration);
            LOGGER.info("Platform named " + platformConfiguration.getName() + " has been deployed.");
        } catch (Exception e) {
            handlePlatformException(platformConfiguration, e);
        } catch (Error e) {
            handlePlatformException(platformConfiguration, e);
        }
        return platformConfiguration;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGisticAnalysisNetCDFFile(Study study, Long reporterListId) {
        new NetCDFManager(fileManager).deleteGisticAnalysisNetCDFFile(study, reporterListId);
    }

    private void handlePlatformException(PlatformConfiguration platformConfiguration,
            Throwable e) {
        LOGGER.error("Deployment of platform " + platformConfiguration.getName() + " failed.", e);
        platformConfiguration.setStatus(Status.ERROR);
        platformConfiguration.setStatusDescription(e.getMessage());
        dao.save(platformConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeneLocationConfiguration loadGeneLocationFile(File geneLocationFile,
            GenomeBuildVersionEnum genomeBuildVersion)
        throws ValidationException, IOException {
        GeneLocationConfiguration geneLocationConfiguration = new GeneLocationConfiguration();
        geneLocationConfiguration.setGenomeBuildVersion(genomeBuildVersion);
        GeneLocationFileLoader.loadFile(geneLocationConfiguration, geneLocationFile);
        dao.save(geneLocationConfiguration);
        return geneLocationConfiguration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void savePlatformConfiguration(PlatformConfiguration platformConfiguration) {
        dao.save(platformConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Platform getPlatform(String name) {
        return dao.getPlatform(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Platform> getPlatforms() {
        return dao.getPlatforms();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Platform> getPlatformsInStudy(Study study, PlatformDataTypeEnum sourceType) {
        List<Platform> allPlatforms = new ArrayList<Platform>();
        for (GenomicDataSourceConfiguration genomicSource : study.getStudyConfiguration().getGenomicDataSources()) {
            if (sourceType.equals(genomicSource.getDataType())) {
                allPlatforms.addAll(dao.retrievePlatformsForGenomicSource(genomicSource));
            }
        }
        if (allPlatforms.size() > 1) {
            Collections.sort(allPlatforms);
        }
        return allPlatforms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Platform> getPlatformsWithCghCallInStudy(Study study, PlatformDataTypeEnum sourceType) {
        List<Platform> allPlatforms = new ArrayList<Platform>();
        for (GenomicDataSourceConfiguration genomicSource : study.getStudyConfiguration().getGenomicDataSources()) {
            if (sourceType.equals(genomicSource.getDataType())
                    && genomicSource.isUseCghCall()) {
                allPlatforms.addAll(dao.retrievePlatformsForGenomicSource(genomicSource));
            }
        }
        if (allPlatforms.size() > 1) {
            Collections.sort(allPlatforms);
        }
        return allPlatforms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformConfiguration getPlatformConfiguration(String name) {
        return dao.getPlatformConfiguration(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlatformConfiguration> getPlatformConfigurations() {
        return dao.getPlatformConfigurations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePlatform(Long platformConfigurationId) {
        dao.delete(getRefreshedPlatformConfiguration(platformConfigurationId));
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    @Autowired
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    @Autowired
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayDataValues getFoldChangeValues(DataRetrievalRequest request,
            List<ArrayDataValues> controlArrayValuesList, PlatformChannelTypeEnum channelType) {
        ArrayDataValues values = getData(request);
        return new FoldChangeCalculator(values, controlArrayValuesList, channelType).calculate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayDataValues getFoldChangeValues(DataRetrievalRequest request, Query query) {
        ArrayDataValues values = getData(request);
        return new FoldChangeCalculator(values,
                getFoldChangeControlDataFromCompoundCriterion(query, query.getCompoundCriterion()),
                query.getGeneExpressionPlatform().getPlatformConfiguration().getPlatformChannelType()).calculate();
    }

    private List<ArrayDataValues> getFoldChangeControlDataFromCompoundCriterion(
            Query query, CompoundCriterion compoundCriterion) {
        List<ArrayDataValues> results = new ArrayList<ArrayDataValues>();
        for (AbstractCriterion criterion : compoundCriterion.getCriterionCollection()) {
            getFoldChangeControlValues(query, criterion, results);
        }
        return results;
    }

    private void getFoldChangeControlValues(Query query,
            AbstractCriterion criterion,  List<ArrayDataValues> results) {
        if (criterion instanceof FoldChangeCriterion) {
            results.add(getFoldChangeControlValues(query, (FoldChangeCriterion) criterion));
        } else if (criterion instanceof CompoundCriterion) {
            CompoundCriterion compoundCriterion = (CompoundCriterion) criterion;
            results.addAll(getFoldChangeControlDataFromCompoundCriterion(query, compoundCriterion));
        }
    }

    private ArrayDataValues getFoldChangeControlValues(Query query, FoldChangeCriterion criterion) {
        Set<ArrayData> controlArrayData = new HashSet<ArrayData>();
        for (Sample sample : criterion.getCompareToSampleSet().getSamples()) {
            controlArrayData.addAll(sample.getArrayDatas(query.getReporterType(), query.getGeneExpressionPlatform()));
        }
        DataRetrievalRequest controlDataRequest = new DataRetrievalRequest();
        controlDataRequest.addReporters(FoldChangeCriterionHandler.create(criterion)
                .getReporterMatches(dao, query.getSubscription().getStudy(),
                query.getReporterType(), query.getGeneExpressionPlatform()));
        controlDataRequest.addArrayDatas(controlArrayData);
        controlDataRequest.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        return getData(controlDataRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformConfiguration getRefreshedPlatformConfiguration(Long id) {
        return dao.get(id, PlatformConfiguration.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StudyConfiguration> getStudyConfigurationsWhichNeedThisPlatform(Platform platform) {
        return dao.getStudyConfigurationsWhichNeedThisPlatform(platform);
    }

}
