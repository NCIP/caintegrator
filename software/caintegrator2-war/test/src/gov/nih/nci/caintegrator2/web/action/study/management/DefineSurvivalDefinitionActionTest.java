/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfigurationFactory;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;


public class DefineSurvivalDefinitionActionTest extends AbstractSessionBasedTest {
    
    private DefineSurvivalDefinitionAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineSurvivalDefinitionActionTest.class); 
        action = (DefineSurvivalDefinitionAction) context.getBean("defineSurvivalDefinitionAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        StudyConfiguration studyConfiguration = StudyConfigurationFactory.createNewStudyConfiguration();
        action.setStudyConfiguration(studyConfiguration);
    }
    
    @Test
    public void testPrepare() {
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyConfigurationCalled);
    }
    
    @Test
    public void testEditSurvivalValueDefinitions() {
        assertEquals(Action.SUCCESS, action.editSurvivalValueDefinitions());
    }
    
    @Test
    public void testEditSurvivalValueDefinition() {
        SurvivalValueDefinition survivalValueDefinition = new SurvivalValueDefinition();
        survivalValueDefinition.setId(Long.valueOf(1));
        AnnotationDefinition startDate = new AnnotationDefinition();
        startDate.setId(Long.valueOf(1));
        AnnotationDefinition deathDate = new AnnotationDefinition();
        deathDate.setId(Long.valueOf(2));
        AnnotationDefinition lastFollowup = new AnnotationDefinition();
        lastFollowup.setId(Long.valueOf(3));
        survivalValueDefinition.setDeathDate(deathDate);
        survivalValueDefinition.setSurvivalStartDate(startDate);
        survivalValueDefinition.setLastFollowupDate(lastFollowup);
        action.setSurvivalValueDefinition(survivalValueDefinition);
        assertEquals(Action.SUCCESS, action.editSurvivalValueDefinition());
        assertEquals(startDate.getId().toString(), action.getSurvivalDefinitionFormValues().getSurvivalStartDateId());
        assertEquals(deathDate.getId().toString(), action.getSurvivalDefinitionFormValues().getSurvivalDeathDateId());
        assertEquals(lastFollowup.getId().toString(), action.getSurvivalDefinitionFormValues().getLastFollowupDateId());
    }
    
    @Test
    public void testNewSurvivalValueDefinition() {
        assertEquals(Action.SUCCESS, action.newSurvivalValueDefinition());
        assertTrue(studyManagementServiceStub.createNewSurvivalValueDefinitionCalled);
        
    }
    
    @Test
    public void testSaveSurvivalValueDefinition() {
        assertEquals(Action.SUCCESS, action.saveSurvivalValueDefinition());
        assertTrue(studyManagementServiceStub.saveCalled);
    }
    
    @Test
    public void testDeleteSurvivalValueDefinition() {
        assertEquals(Action.SUCCESS, action.deleteSurvivalValueDefinition());
        assertTrue(studyManagementServiceStub.removeSurvivalValueDefinitionCalled);
    }
    

}
