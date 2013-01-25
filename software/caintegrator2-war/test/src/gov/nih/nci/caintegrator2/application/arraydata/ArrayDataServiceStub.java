/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArrayDataServiceStub implements ArrayDataService {

    public int numberPlatformsInStudy = 1;
    public boolean loadArrayDesignCalled;
    public boolean getFoldChangeValuesCalled;
    public boolean deleteCalled;
    public boolean saveCalled;
    public boolean getPlatformConfigurationsCalled;
    public boolean getRefreshedPlatformConfigurationCalled;
    
    
    public void reset() {
        numberPlatformsInStudy = 1;
        loadArrayDesignCalled = false;
        getFoldChangeValuesCalled = false;
        deleteCalled = false;
        saveCalled = false;
        getPlatformConfigurationsCalled = false;
        getRefreshedPlatformConfigurationCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayDataValues getData(DataRetrievalRequest request) {
        ArrayDataValues values = new ArrayDataValues(request.getReporters());
        for (AbstractReporter reporter : request.getReporters()) {
            for (ArrayData arrayData : request.getArrayDatas()) {
                for (ArrayDataValueType type : request.getTypes()) {
                    values.setFloatValue(arrayData, reporter, type, (float) 1.23);
                }
            }
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    public PlatformConfiguration loadArrayDesign(PlatformConfiguration platformConfiguration, PlatformDeploymentListener listener) {
        loadArrayDesignCalled = true;
        return new PlatformConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    public void save(ArrayDataValues values) {
        saveCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayDataValues getFoldChangeValues(DataRetrievalRequest request, List<ArrayDataValues> controlArrayValuesList,
            PlatformChannelTypeEnum channelType) {
        getFoldChangeValuesCalled = true;
        return getData(request);
    }

    /**
     * {@inheritDoc}
     */
    public ArrayDataValues getFoldChangeValues(DataRetrievalRequest request, Query query) {
        getFoldChangeValuesCalled = true;
        return getData(request);
    }

    /**
     * {@inheritDoc}
     */
    public PlatformConfiguration getPlatformConfiguration(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Platform getPlatform(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<Platform> getPlatforms() {
        List<Platform> platforms = new ArrayList<Platform>();
        Platform platform = new Platform();
        platform.setVendor(PlatformVendorEnum.AFFYMETRIX);
        platforms.add(platform);
        platform = new Platform();
        platform.setVendor(PlatformVendorEnum.AGILENT);
        platforms.add(platform);
        return platforms;
    }

    public void clear() {
        loadArrayDesignCalled = false;
        deleteCalled = false;
        getPlatformConfigurationsCalled = false;
        getRefreshedPlatformConfigurationCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    public void deletePlatform(Long id) {
        deleteCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPlatformInUsed(String name) {
        return false;
    }

    public List<PlatformConfiguration> getPlatformConfigurations() {
        getPlatformConfigurationsCalled = true;
        List<PlatformConfiguration> platformConfigurations = new ArrayList<PlatformConfiguration>();
        PlatformConfiguration config1 = new PlatformConfiguration();
        config1.setId(1l);
        config1.setName("name");
        config1.setStatus(Status.PROCESSING);
        config1.setDeploymentStartDate(new Date());
        PlatformConfiguration config2 = new PlatformConfiguration();
        config2.setId(1l);
        config2.setName("name2");
        config2.setStatus(Status.LOADED);
        config2.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        config2.setDeploymentStartDate(new Date());
        Platform platform = new Platform();
        platform.setName("name2");
        platform.setVendor(PlatformVendorEnum.AFFYMETRIX);
        config2.setPlatform(platform);
        config2.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        platformConfigurations.add(config1);
        platformConfigurations.add(config2);
        return platformConfigurations;
    }


    public PlatformConfiguration getRefreshedPlatformConfiguration(Long id) {
        getRefreshedPlatformConfigurationCalled = true;
        PlatformConfiguration config = new PlatformConfiguration();
        config.setId(1l);
        config.setName("name");
        config.setStatus(Status.PROCESSING);
        config.setDeploymentStartDate(new Date());
        return config;
    }


    public void savePlatformConfiguration(PlatformConfiguration platformConfiguration) {
        
    }

    public List<Platform> getPlatformsInStudy(Study study, GenomicDataSourceDataTypeEnum sourceType) {
        List<Platform> platforms = new ArrayList<Platform>();
        for (int x = 0; x < numberPlatformsInStudy; x++) {
            Platform platform = new Platform();
            platform.setName(String.valueOf(x));
            platforms.add(platform);
            
        }
        return platforms;
    }

}
