/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator.application.analysis.GenePatternClientFactory;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Performs study deployments and notifies clients.
 */
@Transactional (propagation = Propagation.REQUIRED)
public class DeploymentServiceImpl implements DeploymentService {
    private static final Logger LOGGER = Logger.getLogger(DeploymentServiceImpl.class);

    private CaArrayFacade caArrayFacade;
    private CaIntegrator2Dao dao;
    private ArrayDataService arrayDataService;
    private AnalysisService analysisService;
    private BioconductorService bioconductorService;
    private DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory = new DnaAnalysisHandlerFactoryImpl();
    private ExpressionHandlerFactory expressionHandlerFactory = new ExpressionHandlerFactoryImpl();
    private GenePatternClientFactory genePatternClientFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareForDeployment(StudyConfiguration studyConfiguration) {
        try {
            studyConfiguration = getDao().get(studyConfiguration.getId(), StudyConfiguration.class);
            startDeployment(studyConfiguration);
        } catch (Exception e) {
            handleDeploymentFailure(studyConfiguration, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudyConfiguration performDeployment(StudyConfiguration studyConfiguration,
            HeatmapParameters heatmapParameters) {
        try {
            studyConfiguration = getDao().get(studyConfiguration.getId(), StudyConfiguration.class);
            if (!Status.PROCESSING.equals(studyConfiguration.getStatus())) {
                startDeployment(studyConfiguration);
            }
            return doDeployment(studyConfiguration, heatmapParameters);
        } catch (Exception e) {
            handleDeploymentFailure(studyConfiguration, e);
        } catch (Error e) {
            handleDeploymentFailure(studyConfiguration, e);
        }
        return studyConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleDeploymentFailure(StudyConfiguration studyConfiguration, Throwable e) {
        LOGGER.error("Deployment of study " + studyConfiguration.getStudy().getShortTitleText()
                + " failed.", e);
        studyConfiguration.setStatusDescription((e.getMessage() != null)
                ? e.getMessage() : e.toString());
        studyConfiguration.setStatus(Status.ERROR);
        getDao().save(studyConfiguration);
    }

    private StudyConfiguration doDeployment(StudyConfiguration studyConfiguration, HeatmapParameters heatmapParameters)
    throws ConnectionException, DataRetrievalException, ValidationException, IOException, InvalidCriterionException {
        if (!studyConfiguration.getGenomicDataSources().isEmpty()) {
            GenomicDataHelper genomicDataHelper = new GenomicDataHelper(getCaArrayFacade(),
                    getArrayDataService(), getDao(), getBioconductorService(), getDnaAnalysisHandlerFactory());
            genomicDataHelper.setExpressionHandlerFactory(getExpressionHandlerFactory());
            genomicDataHelper.setGenePatternClientFactory(getGenePatternClientFactory());
            genomicDataHelper.loadData(studyConfiguration);
            dao.runSessionKeepAlive();
            generateViewerFiles(studyConfiguration, heatmapParameters);
        }
        studyConfiguration.setStatus(Status.DEPLOYED);
        studyConfiguration.setDeploymentFinishDate(new Date());
        studyConfiguration.setStatusDescription("Minutes for deployment (approx):  "
                + DateUtil.compareDatesInMinutes(studyConfiguration.getDeploymentStartDate(),
                                                 studyConfiguration.getDeploymentFinishDate()));
        getDao().save(studyConfiguration);
        LOGGER.info("Deployment of study " + studyConfiguration.getStudy().getShortTitleText()
                + " has been completed.");
        return studyConfiguration;
    }

    private void generateViewerFiles(StudyConfiguration studyConfiguration, HeatmapParameters heatmapParameters)
    throws InvalidCriterionException, IOException {
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(studyConfiguration.getStudy());
        studySubscription.setUserWorkspace(studyConfiguration.getUserWorkspace());
        dao.runSessionKeepAlive();
        getAnalysisService().deleteViewerDirectory(studyConfiguration.getStudy());
        heatmapParameters.setStudySubscription(studySubscription);
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration
                : studyConfiguration.getGenomicDataSources()) {
            getAnalysisService().createViewerFiles(studySubscription, heatmapParameters,
                    getArrayDataService().getPlatform(genomicDataSourceConfiguration.getPlatformName()));
        }
    }

    private void startDeployment(StudyConfiguration studyConfiguration) {
        LOGGER.info("Deployment of study " + studyConfiguration.getStudy().getShortTitleText()
                + " has started.");
        studyConfiguration.setDeploymentStartDate(new Date());
        studyConfiguration.setDeploymentFinishDate(null);
        studyConfiguration.setStatusDescription(null);
        studyConfiguration.setStatus(Status.PROCESSING);
        studyConfiguration.setDataRefreshed(false);
        getDao().save(studyConfiguration);
    }

    /**
     * @return the caArrayFacade
     */
    public CaArrayFacade getCaArrayFacade() {
        return caArrayFacade;
    }

    /**
     * @param caArrayFacade the caArrayFacade to set
     */
    public void setCaArrayFacade(CaArrayFacade caArrayFacade) {
        this.caArrayFacade = caArrayFacade;
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
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the bioconductorService
     */
    public BioconductorService getBioconductorService() {
        return bioconductorService;
    }

    /**
     * @param bioconductorService the bioconductorService to set
     */
    public void setBioconductorService(BioconductorService bioconductorService) {
        this.bioconductorService = bioconductorService;
    }

    /**
     * @return the dnaAnalysisHandlerFactory
     */
    public DnaAnalysisHandlerFactory getDnaAnalysisHandlerFactory() {
        return dnaAnalysisHandlerFactory;
    }

    /**
     * @param dnaAnalysisHandlerFactory the dnaAnalysisHandlerFactory to set
     */
    public void setDnaAnalysisHandlerFactory(DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory) {
        this.dnaAnalysisHandlerFactory = dnaAnalysisHandlerFactory;
    }

    /**
     * @return the genePatternClientFactory
     */
    public GenePatternClientFactory getGenePatternClientFactory() {
        return genePatternClientFactory;
    }

    /**
     * @param genePatternClientFactory the genePatternClientFactory to set
     */
    public void setGenePatternClientFactory(GenePatternClientFactory genePatternClientFactory) {
        this.genePatternClientFactory = genePatternClientFactory;
    }

    /**
     * @return the expressionHandlerFactory
     */
    public ExpressionHandlerFactory getExpressionHandlerFactory() {
        return expressionHandlerFactory;
    }

    /**
     * @param expressionHandlerFactory the expressionHandlerFactory to set
     */
    public void setExpressionHandlerFactory(ExpressionHandlerFactory expressionHandlerFactory) {
        this.expressionHandlerFactory = expressionHandlerFactory;
    }

    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

}
