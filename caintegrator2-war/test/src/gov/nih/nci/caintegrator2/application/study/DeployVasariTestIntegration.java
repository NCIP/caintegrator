/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional(timeout = 14400)
public class DeployVasariTestIntegration extends AbstractDeployStudyTestIntegration {
    
    private final static Logger LOGGER = Logger.getLogger(DeployVasariTestIntegration.class);

    @Test
    public void testDeployStudy() throws Exception {
        deployStudy();
    }

    @Override
    protected boolean getLoadDesign() {
        return true;
    }
    
    @Override
    protected boolean getLoadSamples() {
        return true;
    }
    
    @Override
    protected String getCaArrayId() {
        return "rembr-00037";
    }

    @Override
    protected int getExpectedSampleCount() {
        return 564;
    }

    @Override
    protected int getExpectedMappedSampleCount() {
        return 68;
    }

    @Override
    protected int getExpectedControlSampleCount() {
        return 28;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getNCIAServerUrl() {
        return "http://imaging-dev.nci.nih.gov/wsrf/services/cagrid/NCIACoreService";
    }
    
    @Override
    protected String getNCIATrialId() {
        return "GBM Rembrandt";
    }

    @Override
    protected String getStudyName() {
        return "Rembrandt/VASARI";
    }

    @Override
    protected File getAnnotationGroupFile() {
        return TestDataFiles.REMBRANDT_ANNOTATION_GROUP_FILE;
    }

    protected File getImageAnnotationFile() {
        return TestDataFiles.VASARI_IMAGE_ANNOTATION_FILE;
    }

    @Override
    protected File getImageMappingFile() {
        return TestDataFiles.REMBRANDT_IMAGE_STUDIES_TO_SUBJECT_FILE;
    }

    @Override
    protected File getSampleMappingFile() {
        return TestDataFiles.REMBRANDT_SAMPLE_MAPPING_FILE;
    }

    @Override
    protected String getControlSampleSetName() {
        return TestDataFiles.REMBRANDT_CONTROL_SAMPLE_SET_NAME;
    }

    @Override
    protected File getControlSamplesFile() {
        return TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE;
    }

    @Override
    protected String getControlSamplesFileName() {
        return TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE_PATH;
    }
    
    @Override
    protected File getSubjectAnnotationFile() {
        return TestDataFiles.REMBRANDT_CLINICAL_FILE;
    }

    @Override
    protected AbstractPlatformSource getPlatformSource() {
        return new AffymetrixExpressionPlatformSource(TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
    }

    @Override
    protected String getDeathDateName() {
        return "Death Date";
    }

    @Override
    protected String getLastFollowupDateName() {
        return "Last Followup Date";
    }

    @Override
    protected String getSurvivalStartDateName() {
        return "Survival Start Date";
    }

    @Override
    protected int getExpectedNumberOfGeneReporters() {
        return 21432;
    }

    @Override
    protected int getExpectedNumberProbeSets() {
        return 54675;
    }

    @Override
    protected String getPlatformName() {
        return "HG-U133_Plus_2";
    }

    @Override
    protected PlatformVendorEnum getPlatformVendor() {
        return PlatformVendorEnum.AFFYMETRIX;
    }

    /**
     * {@inheritDoc}
     */
    @Override    
    protected boolean getAuthorizeStudy() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryFieldDescriptorName() {
        return "Gender";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryAnnotationValue() {
        return "F";
    }     

}
