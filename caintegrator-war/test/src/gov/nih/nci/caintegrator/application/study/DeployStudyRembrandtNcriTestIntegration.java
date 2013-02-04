/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional(timeout = 2880)
public class DeployStudyRembrandtNcriTestIntegration extends AbstractDeployStudyTestIntegration {

    private final static Logger LOGGER = Logger.getLogger(DeployStudyRembrandtNcriTestIntegration.class);

    @Test
    public void testDeployStudy() throws Exception {
        deployStudy();
    }

    @Override
    protected boolean getMapImages() {
        return true;
    }

    @Override
    protected boolean getLoadImages() {
        return true;
    }

    @Override
    protected boolean getLoadImageAnnotation() {
        return true;
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
        return "jagla-00034";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCaArrayHostname() {
        return "ncias-d227-v.nci.nih.gov";
    }

    @Override
    protected int getCaArrayPort() {
        return 31099;
    }

    @Override
    protected int getExpectedSampleCount() {
        return 3;
    }

    @Override
    protected int getExpectedMappedSampleCount() {
        return 2;
    }

    @Override
    protected int getExpectedControlSampleCount() {
        return 1;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getNCIAServerUrl() {
        return "http://imaging.nci.nih.gov/wsrf/services/cagrid/NCIACoreService";
    }

    @Override
    protected String getNCIATrialId() {
        return "NCRI";
    }

    @Override
    protected String getStudyName() {
        return "Rembrandt with NCRI";
    }

    @Override
    protected File getAnnotationGroupFile() {
        return TestDataFiles.REMBRANDT_ANNOTATION_GROUP_FILE;
    }

    @Override
    protected File getImageAnnotationFile() {
        return TestDataFiles.REMBRANDT_NCRI_IMAGE_ANNOTATION_FILE;
    }

    @Override
    protected File getImageMappingFile() {
        return TestDataFiles.REMBRANDT_NCRI_IMAGE_SERIES_TO_SUBJECT_FILE;
    }
    @Override
    protected File getSampleMappingFile() {
        return TestDataFiles.REMBRANDT_NCRI_SAMPLE_MAPPING_FILE;
    }

    @Override
    protected String getControlSampleSetName() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_SET_NAME;
    }

    @Override
    protected File getControlSamplesFile() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_FILE;
    }

    @Override
    protected String getControlSamplesFileName() {
        return TestDataFiles.JAGLA_00034_CONTROL_SAMPLES_FILE_PATH;
    }

    @Override
    protected File getSubjectAnnotationFile() {
        return TestDataFiles.REMBRANDT_NCRI_CLINICAL_FILE;
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
