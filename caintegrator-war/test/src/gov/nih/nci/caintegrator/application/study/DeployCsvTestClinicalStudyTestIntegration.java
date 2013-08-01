/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.TestDataFiles;

import java.io.File;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests deployment of the csv clinical study.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Transactional
public class DeployCsvTestClinicalStudyTestIntegration extends AbstractDeployStudyTestIntegration {

    @Test
    public void testDeployStudy() throws Exception {
        deployStudy();
    }

    @Override
    protected String getStudyName() {
        return "Test Clinical";
    }

    @Override
    protected String getDescription() {
        return "Only clinical data";
    }

    @Override
    protected File getAnnotationGroupFile() {
        return TestDataFiles.VALID_ANNOTATION_GROUP_FILE;
    }

    @Override
    protected File getSubjectAnnotationFile() {
        return TestDataFiles.VALID_FILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryFieldDescriptorName() {
        return "Col3";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getQueryAnnotationValue() {
        return "Y";
    }
}
