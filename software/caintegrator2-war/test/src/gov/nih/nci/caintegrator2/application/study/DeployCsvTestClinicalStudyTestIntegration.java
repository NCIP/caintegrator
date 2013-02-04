/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.TestDataFiles;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional(timeout = 2880)
public class DeployCsvTestClinicalStudyTestIntegration extends AbstractDeployStudyTestIntegration {

    private final static Logger LOGGER = Logger.getLogger(DeployCsvTestClinicalStudyTestIntegration.class);

    @Test
    public void testDeployStudy() throws Exception {
        deployStudy();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
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
    protected boolean getAuthorizeStudy() {
        return false;
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
