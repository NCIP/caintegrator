package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfigurationFactory;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;


public class DefineSurvivalDefinitionActionTest {
    
    private DefineSurvivalDefinitionAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineSurvivalDefinitionActionTest.class); 
        action = (DefineSurvivalDefinitionAction) context.getBean("defineSurvivalDefinitionAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        StudyConfiguration studyConfiguration = StudyConfigurationFactory.createNewStudyConfiguration();
        action.setStudyConfiguration(studyConfiguration);
    }
    
    @Test
    public void testPrepare() {
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
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
