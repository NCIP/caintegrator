/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfigurationFactory;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
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
    private AnnotationDefinition survivalLength;
    private Map<Long, AnnotationDefinition> annotationDefinitionMap = new HashMap<Long, AnnotationDefinition>();

    @Override
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
        survivalLength = new AnnotationDefinition();
        survivalLength.setId(103l);
        survivalLength.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinitionMap.clear();
        annotationDefinitionMap.put(startDate.getId(), startDate);
        annotationDefinitionMap.put(lastFollowup.getId(), lastFollowup);
        annotationDefinitionMap.put(deathDate.getId(), deathDate);
        annotationDefinitionMap.put(survivalLength.getId(), survivalLength);
    }

    @Test
    public void testPrepare() {
        SurvivalValueDefinition defaultDefinition = new SurvivalValueDefinition();
        defaultDefinition.setId(1l);
        action.getStudyConfiguration().setId(null);
        Study study = action.getStudyConfiguration().getStudy();
        study.getSurvivalValueDefinitionCollection().add(defaultDefinition);
        AnnotationGroup group = new AnnotationGroup();

        AnnotationFieldDescriptor dateAfd1 = new AnnotationFieldDescriptor();
        dateAfd1.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        AnnotationDefinition dateAd1 = new AnnotationDefinition();
        dateAd1.setId(2l);
        dateAd1.setDataType(AnnotationTypeEnum.DATE);
        dateAfd1.setDefinition(dateAd1);
        dateAfd1.setShownInBrowse(true);

        AnnotationFieldDescriptor numericAfd1 = new AnnotationFieldDescriptor();
        numericAfd1.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        AnnotationDefinition numericAd1 = new AnnotationDefinition();
        numericAd1.setId(3l);
        numericAd1.setDataType(AnnotationTypeEnum.NUMERIC);
        numericAfd1.setDefinition(numericAd1);
        numericAfd1.setShownInBrowse(true);

        AnnotationFieldDescriptor statusAfd1 = new AnnotationFieldDescriptor();
        statusAfd1.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        AnnotationDefinition statusAd1 = new AnnotationDefinition();
        statusAd1.setId(4l);
        statusAd1.setDataType(AnnotationTypeEnum.STRING);
        PermissibleValue pv1 = new PermissibleValue();
        statusAd1.getPermissibleValueCollection().add(pv1);
        statusAfd1.setDefinition(statusAd1);
        statusAfd1.setShownInBrowse(true);


        group.getAnnotationFieldDescriptors().add(dateAfd1);
        group.getAnnotationFieldDescriptors().add(numericAfd1);
        group.getAnnotationFieldDescriptors().add(statusAfd1);
        study.getAnnotationGroups().add(group);
        group.setName("annotationGroup");
        action.prepare();
        assertEquals(defaultDefinition, action.getSurvivalValueDefinitions().get("1"));
        assertEquals(dateAd1, action.getDateAnnotationDefinitions().get("2"));
        assertEquals(numericAd1, action.getNumericAnnotationDefinitions().get("3"));
        assertEquals(statusAd1, action.getSurvivalStatusAnnotationDefinitions().get("4"));
    }

    @Test
    public void testUpdateSurvivalStatusValues() {
        SurvivalValueDefinition survivalValueDefinition = new SurvivalValueDefinition();
        AnnotationDefinition survivalStatus = new AnnotationDefinition();
        PermissibleValue pv1 = new PermissibleValue();
        pv1.setValue("ALIVE");
        PermissibleValue pv2 = new PermissibleValue();
        pv2.setValue("DEAD");
        survivalStatus.getPermissibleValueCollection().add(pv1);
        survivalStatus.getPermissibleValueCollection().add(pv2);
        survivalValueDefinition.setSurvivalStatus(survivalStatus);
        action.setSurvivalValueDefinition(survivalValueDefinition);
        action.getSurvivalDefinitionFormValues().setSurvivalStatusId("1");
        action.getSurvivalDefinitionFormValues().setSurvivalLengthId("2");
        assertEquals(Action.SUCCESS, action.updateSurvivalStatusValues());
        assertEquals("ALIVE", action.getSurvivalStatusValues().get(0));
        assertEquals("DEAD", action.getSurvivalStatusValues().get(1));
        action.getSurvivalDefinitionFormValues().clear();
        assertEquals(Action.SUCCESS, action.updateSurvivalStatusValues());
        assertEquals(null, survivalValueDefinition.getSurvivalStatus());
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
        action.getSurvivalDefinitionFormValues().setSurvivalValueType(SurvivalValueTypeEnum.DATE.getValue());
        assertEquals(Action.SUCCESS, action.saveSurvivalValueDefinition()); // All valid.
        assertTrue(studyManagementServiceStub.saveCalled);
        action.prepare();
        action.getSurvivalDefinitionFormValues().setSurvivalValueType(SurvivalValueTypeEnum.LENGTH_OF_TIME.getValue());
        assertEquals(Action.INPUT, action.saveSurvivalValueDefinition()); // All valid.
        assertTrue(studyManagementServiceStub.saveCalled);

        action.getSurvivalDefinitionFormValues().setSurvivalLengthId("103");
        action.getSurvivalDefinitionFormValues().setSurvivalStatusId("104");
        action.prepare();
        assertEquals(Action.INPUT, action.saveSurvivalValueDefinition()); // All valid.

        action.getSurvivalDefinitionFormValues().setValueForCensored("alive");
        assertEquals(Action.SUCCESS, action.saveSurvivalValueDefinition()); // All valid.

    }

    @Test
    public void testDeleteSurvivalValueDefinition() {
        assertEquals(Action.SUCCESS, action.deleteSurvivalValueDefinition());
        assertTrue(studyManagementServiceStub.removeSurvivalValueDefinitionCalled);
    }

    private class LocalStudyManagementServiceStub extends StudyManagementServiceStub {
        @SuppressWarnings("unchecked")
        @Override
        public <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity) {
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
