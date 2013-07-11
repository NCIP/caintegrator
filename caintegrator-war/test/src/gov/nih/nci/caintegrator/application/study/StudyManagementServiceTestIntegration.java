/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Study management integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class StudyManagementServiceTestIntegration {

    @Autowired
    private StudyManagementService studyManagementService;

    @Test
    public void testSetClinicalAnnotationAndLoadData() throws ValidationException, IOException {
        // Set Clinical Annotations
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration =
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE,
                    TestDataFiles.VALID_FILE.getName(), false);
        assertNotNull(sourceConfiguration);

        // Clean up the test file
        sourceConfiguration.getAnnotationFile().getFile().delete();
    }

    @Test
    public void testRetrieveStudyLogo() {
        String name = "StudyName";
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStudy(new Study());
        studyConfiguration.getStudy().setShortTitleText(name);
        studyConfiguration.setStudyLogo(new StudyLogo());

        studyManagementService.save(studyConfiguration);

        assertNotNull(studyManagementService.retrieveStudyLogo(studyConfiguration.getStudy().getId(), name));
    }
}
