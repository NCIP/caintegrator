package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfigurationFactory;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;


public class DefineSurvivalDefinitionActionTest extends AbstractSessionBasedTest {
    
    private DefineSurvivalDefinitionAction action;
    private LocalStudyManagementServiceStub studyManagementServiceStub;
    private AnnotationDefinition startDate;
    private AnnotationDefinition lastFollowup;
    private AnnotationDefinition deathDate;
    private Map<Long, AnnotationDefinition> annotationDefinitionMap = new HashMap<Long, AnnotationDefinition>();

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineSurvivalDefinitionActionTest.class); 
        action = (DefineSurvivalDefinitionAction) context.getBean("defineSurvivalDefinitionAction");
        studyManagementServiceStub = new LocalStudyManagementServiceStub();
        studyManagementServiceStub.clear();
        action.setStudyManagementService(studyManagementServiceStub);
        
        StudyConfiguration studyConfiguration = StudyConfigurationFactory.createNewStudyConfiguration();
        action.setStudyConfiguration(studyConfiguration);
        startDate = new AnnotationDefinition();
        startDate.setId(100l);
        startDate.setDataType(AnnotationTypeEnum.DATE);
        lastFollowup = new AnnotationDefinition();
        lastFollowup.setId(101l);
        lastFollowup.setDataType(AnnotationTypeEnum.DATE);
        deathDate = new AnnotationDefinition();
        deathDate.setId(102l);
        deathDate.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinitionMap.clear();
        annotationDefinitionMap.put(startDate.getId(), startDate);
        annotationDefinitionMap.put(lastFollowup.getId(), lastFollowup);
        annotationDefinitionMap.put(deathDate.getId(), deathDate);
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
        
    }
    
    @Test
    public void testSaveSurvivalValueDefinition() {
        action.prepare();
        assertEquals(Action.INPUT, action.saveSurvivalValueDefinition()); // New survival value definition
        assertTrue(action.isNewDefinition());
        
        action.getSurvivalDefinitionFormValues().setSurvivalStartDateId("100");
        action.getSurvivalDefinitionFormValues().setSurvivalDeathDateId("101");
        action.getSurvivalDefinitionFormValues().setLastFollowupDateId("102");
        action.getSurvivalDefinitionFormValues().setSurvivalValueDefinitionId("1");
        action.prepare();
        assertEquals(Action.INPUT, action.saveSurvivalValueDefinition()); // Existing survival value definition, no name
        assertFalse(action.isNewDefinition());
        
        action.getSurvivalDefinitionFormValues().setSurvivalValueDefinitionName("uniqueName");
        action.prepare();
        assertEquals(Action.SUCCESS, action.saveSurvivalValueDefinition()); // All valid.
        
        assertTrue(studyManagementServiceStub.saveCalled);
    }
    
    @Test
    public void testDeleteSurvivalValueDefinition() {
        assertEquals(Action.SUCCESS, action.deleteSurvivalValueDefinition());
        assertTrue(studyManagementServiceStub.removeSurvivalValueDefinitionCalled);
    }
    
    private class LocalStudyManagementServiceStub extends StudyManagementServiceStub {
        @SuppressWarnings("unchecked")
        @Override
        public <T> T getRefreshedEntity(T entity) {
            getRefreshedStudyEntityCalled = true;
            Long id;
            try {
                id = (Long) entity.getClass().getMethod("getId").invoke(entity);
            } catch (Exception e) {
                throw new IllegalArgumentException("Entity doesn't have a getId() method", e);
            }
            if (annotationDefinitionMap.containsKey(id)) {
                return (T) annotationDefinitionMap.get(id);
            }
            return entity;
        }
    }
    

}
