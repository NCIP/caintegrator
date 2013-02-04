/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationFileStub;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.ISubjectDataSourceAjaxUpdater;
import gov.nih.nci.caintegrator2.web.ajax.SubjectDataSourceAjaxRunner;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class LoadClinicalSourceActionTest extends AbstractSessionBasedTest {

    private LoadClinicalSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    private SubjectDataSourceAjaxUpdaterStub updater;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", LoadClinicalSourceActionTest.class); 
        action = (LoadClinicalSourceAction) context.getBean("loadClinicalSourceAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        action.getClinicalSource().setAnnotationFile(createAnnotationFile());
        updater = new SubjectDataSourceAjaxUpdaterStub();
        updater.clear();
        action.setUpdater(updater);
    }

    @Test
    public void testExecute() {
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(updater.runJobCalled);
    }

    @Test
    public void testReLoad() {
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.reLoad());
        assertTrue(updater.runJobCalled);
    }

    @Test
    public void testDelete() {
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.delete());
        assertTrue(updater.runJobCalled);
    }
    
    private AnnotationFile createAnnotationFile() {
        DelimitedTextClinicalSourceConfiguration clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        AnnotationFileStub annotationFile = new AnnotationFileStub();
        clinicalConf.setAnnotationFile(annotationFile);
        return annotationFile;
    }
    
    
    private static class SubjectDataSourceAjaxUpdaterStub implements ISubjectDataSourceAjaxUpdater {
        
        public boolean runJobCalled = false;
        
        public void clear() {
            runJobCalled = false;
        }
        
        public void initializeJsp() {
            
        }

        public void runJob(Long studyConfigurationId, Long subjectSourceId, SubjectDataSourceAjaxRunner.JobType jobType) {
            runJobCalled = true;
        }

    }

}
