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

/**
 * Integration test for deployment of the public DC Lung study.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Transactional
public class DeployPublicDCLungStudyTestIntegration extends AbstractDeployStudyTestIntegration {

    private static final Logger LOGGER = Logger.getLogger(DeployPublicDCLungStudyTestIntegration.class);

    @Test
    public void testDeployStudy() throws Exception {
        deployStudy();
    }

    @Override
    protected boolean getMapImages() {
        return false;
    }

    @Override
    protected boolean getLoadImages() {
        return false;
    }

    @Override
    protected boolean getLoadImageAnnotation() {
        return false;
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
        return "jacob-00182";
    }

    @Override
    protected int getExpectedSampleCount() {
        return 443;
    }

    @Override
    protected int getExpectedMappedSampleCount() {
        return 443;
    }

    @Override
    protected int getExpectedControlSampleCount() {
        return 4;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getNCIAServerUrl() {
        return null;
    }

    @Override
    protected String getNCIATrialId() {
        return null;
    }

    @Override
    protected String getStudyName() {
        return "DC Lung Study";
    }

    @Override
    protected String getDescription() {
        return "DC Lung Study";
    }

    @Override
    protected File getAnnotationGroupFile() {
        return TestDataFiles.DC_LUNG_PUBLIC_ANNOTATION_GROUP_FILE;
    }

    @Override
    protected File getImageAnnotationFile() {
        return null;
    }

    @Override
    protected File getImageMappingFile() {
        return null;
    }
    @Override
    protected File getSampleMappingFile() {
        return TestDataFiles.DC_LUNG_PUBLIC_SAMPLE_MAPPING_FILE;
    }

    @Override
    protected String getControlSampleSetName() {
        return TestDataFiles.DC_LUNG_PUBLIC_CONTROL_SAMPLE_SET_NAME;
    }

    @Override
    protected File getControlSamplesFile() {
        return TestDataFiles.DC_LUNG_PUBLIC_CONTROL_SAMPLE_MAPPING_FILE;
    }

    @Override
    protected String getControlSamplesFileName() {
        return TestDataFiles.DC_LUNG_PUBLIC_CONTROL_SAMPLE_MAPPING_FILE_PATH;
    }

    @Override
    protected File getSubjectAnnotationFile() {
        return TestDataFiles.DC_LUNG_PUBLIC_CLINICAL_FILE;
    }

    @Override
    protected AbstractPlatformSource getPlatformSource() {
        return new AffymetrixExpressionPlatformSource(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
    }

    @Override
    protected String getDeathDateName() {
        return "DEATH_DATE";
    }

    @Override
    protected String getLastFollowupDateName() {
        return "LAST_CONTACT_DATE";
    }

    @Override
    protected String getSurvivalStartDateName() {
        return "ENROLLMENT_DATE";
    }

    @Override
    protected int getExpectedNumberOfGeneReporters() {
        return 13796;
    }

    @Override
    protected int getExpectedNumberProbeSets() {
        return 22283;
    }

    @Override
    protected String getPlatformName() {
        return "HG-U133A";
    }

    @Override
    protected PlatformVendorEnum getPlatformVendor() {
        return PlatformVendorEnum.AFFYMETRIX;
    }

    @Override
    protected boolean getAuthorizeStudy() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryFieldDescriptorName() {
        return "GENDER";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryAnnotationValue() {
        return "Female";
    }
}
