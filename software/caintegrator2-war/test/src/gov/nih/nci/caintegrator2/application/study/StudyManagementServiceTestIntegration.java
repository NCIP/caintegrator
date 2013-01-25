/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.io.IOException;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

@Transactional 
public class StudyManagementServiceTestIntegration extends AbstractTransactionalSpringContextTests{
    
    private StudyManagementService studyManagementService;
    
    public StudyManagementServiceTestIntegration() {
        // Set this to false if you want to see the change occur in the database and not be rolled back.
        this.setDefaultRollback(true);
    }
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/service-test-integration-config.xml"};
    }
    
    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }
    
    @Test
    public void testSetClinicalAnnotationAndLoadData() throws ValidationException, IOException {
        
        // Set Clinical Annotations
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration = 
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName());
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
