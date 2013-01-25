/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the array data service (backed by NetCDF).
 */
@Transactional
public class ArrayDataServiceImpl implements ArrayDataService {
    
    private static final Logger LOGGER = Logger.getLogger(ArrayDataServiceImpl.class);
    
    private CaIntegrator2Dao dao;
    private FileManager fileManager;

    /**
     * {@inheritDoc}
     */
    public ArrayDataValues getData(DataRetrievalRequest request) {
        return new NetCDFManager(getFileManager()).retrieveValues(request);
    }

    /**
     * {@inheritDoc}
     */
    public void save(ArrayDataValues values) {
        for (ArrayData arrayData : values.getArrayDatas()) {
            dao.save(arrayData);
        }
        new NetCDFManager(getFileManager()).storeValues(values);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.AvoidReassigningParameters") // preferable in this instance for error handling.
    public PlatformConfiguration loadArrayDesign(PlatformConfiguration platformConfiguration, 
            PlatformDeploymentListener listener) {
        AbstractPlatformSource platformSource = platformConfiguration.getPlatformSource();
        platformConfiguration = getRefreshedPlatformConfiguration(platformConfiguration.getId());
        try {
            LOGGER.info("Started loading design from " + platformSource.toString());
            getDao().setFlushMode(HibernateAccessor.FLUSH_COMMIT);
            String platformName = platformSource.getPlatformName();
            Platform platform = platformSource.getLoader().load(getDao());
            LOGGER.info("Completed loading design from " + platformSource.toString());
            platformConfiguration.setPlatform(platform);
            platformConfiguration.setStatus(Status.LOADED);
            platformConfiguration.setDeploymentFinishDate(new Date());
            saveAndUpdateDeploymentStatus(platformConfiguration, listener);
            LOGGER.info("Platform named " + platformName + " has been loaded.");
        } catch (Exception e) {
            handlePlatformException(platformConfiguration, listener, e);
        } catch (Error e) {
            handlePlatformException(platformConfiguration, listener, e);
        }
        return platformConfiguration;
        
    }

    private void handlePlatformException(PlatformConfiguration platformConfiguration,
            PlatformDeploymentListener listener, Throwable e) {
        LOGGER.error("Deployment of platform " + platformConfiguration.getName() + " failed.", e);
        platformConfiguration.setStatus(Status.ERROR);
        platformConfiguration.setStatusDescription(e.getMessage());
        saveAndUpdateDeploymentStatus(platformConfiguration, listener);
    }
    
    private void saveAndUpdateDeploymentStatus(PlatformConfiguration platformConfiguration, 
                        PlatformDeploymentListener listener) {
        dao.save(platformConfiguration);
        if (listener != null) {
            listener.statusUpdated(platformConfiguration);
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void savePlatformConfiguration(PlatformConfiguration platformConfiguration) {
        dao.save(platformConfiguration);
    }
    
    /**
     * {@inheritDoc}
     */
    public Platform getPlatform(String name) {
        return dao.getPlatform(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Platform> getPlatforms() {
        return dao.getPlatforms();
    }
    
    /**
     * {@inheritDoc}
     */
    public PlatformConfiguration getPlatformConfiguration(String name) {
        return dao.getPlatformConfiguration(name);
    }

    /**
     * {@inheritDoc}
     */
    public List<PlatformConfiguration> getPlatformConfigurations() {
        return dao.getPlatformConfigurations();
    }

    /**
     * {@inheritDoc}
     */
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
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayDataValues getFoldChangeValues(DataRetrievalRequest request, Collection<ArrayData> controlArrayDatas) {
        ArrayDataValues values = getData(request);
        DataRetrievalRequest controlDataRequest = new DataRetrievalRequest();
        controlDataRequest.addReporters(request.getReporters());
        controlDataRequest.addArrayDatas(controlArrayDatas);
        controlDataRequest.addTypes(request.getTypes());
        ArrayDataValues controlValues = getData(controlDataRequest);
        return new FoldChangeCalculator(values, controlValues).calculate();
    }
    
    /**
     * {@inheritDoc}
     */
    public PlatformConfiguration getRefreshedPlatformConfiguration(Long id) {
        return dao.get(id, PlatformConfiguration.class);
    }

}
