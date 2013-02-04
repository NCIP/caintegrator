/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.analysis.AbstractKMParameters;
import gov.nih.nci.caintegrator.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.KMPlotAnnotationBasedAction;
import gov.nih.nci.caintegrator.web.action.analysis.KMPlotAnnotationBasedActionForm;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KMPlotAnnotationBasedActionTest extends AbstractSessionBasedTest  {

    private KMPlotAnnotationBasedAction action;
    private final StudyManagementServiceStub studyManagementService = new StudyManagementServiceStub();
    private final KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();

    private PermissibleValue val1 = new PermissibleValue();
    private PermissibleValue val2 = new PermissibleValue();
    private SurvivalValueDefinition survivalValue;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        StudySubscription subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new KMPlotAnnotationBasedAction();
        action.setStudyManagementService(studyManagementService);
        action.setAnalysisService(analysisService);
        action.setWorkspaceService(workspaceService);
        action.setQueryManagementService(queryManagementService);

        setStudySubscription(subscription);
        SessionHelper.getInstance().getDisplayableUserWorkspace().refresh(workspaceService, true);
    }

    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        AnnotationGroup group = new AnnotationGroup();
        group.setName("subjectAnnotations");
        study.getAnnotationGroups().add(group);
        AnnotationFieldDescriptor afd1 = new AnnotationFieldDescriptor();
        afd1.setId(1l);
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setId(2l);
        group.getAnnotationFieldDescriptors().add(afd1);
        group.getAnnotationFieldDescriptors().add(afd2);
        AnnotationDefinition subjectDef1 = new AnnotationDefinition();
        subjectDef1.setId(Long.valueOf(1));
        val1.setId(Long.valueOf(1));
        val1.setValue("M");
        val2.setId(Long.valueOf(2));
        val2.setValue("F");
        subjectDef1.getPermissibleValueCollection().add(val1);
        subjectDef1.getPermissibleValueCollection().add(val2);
        subjectDef1.getPermissibleValueCollection().add(new PermissibleValue());
        afd1.setDefinition(subjectDef1);
        AnnotationDefinition subjectDef2 = new AnnotationDefinition();
        subjectDef2.setId(Long.valueOf(2));
        afd2.setDefinition(subjectDef2);
        AnnotationDefinition survivalStartDate = new AnnotationDefinition();
        survivalStartDate.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition deathDate = new AnnotationDefinition();
        deathDate.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition lastFollowupDate = new AnnotationDefinition();
        lastFollowupDate.setDataType(AnnotationTypeEnum.DATE);

        survivalValue = new SurvivalValueDefinition();
        survivalValue.setId(Long.valueOf(1));
        survivalValue.setSurvivalStartDate(survivalStartDate);
        survivalValue.setDeathDate(deathDate);
        survivalValue.setLastFollowupDate(lastFollowupDate);
        study.getSurvivalValueDefinitionCollection().add(survivalValue);

        return study;
    }

    @Test
    public void testPrepare() {
        action.getKmPlotParameters().setSurvivalValueDefinition(survivalValue);
        setupActionVariables();
        action.prepare();
        verify(queryManagementService, atLeastOnce()).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
        assertEquals(1, action.getKmPlotForm().getSurvivalValueDefinitions().size());
    }

    @Test
    public void testValidate() {
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
        action.clearErrorsAndMessages();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getKmPlotForm().getSurvivalValueDefinitions().put("1", new SurvivalValueDefinition());
        action.validate();
        assertTrue(action.getActionErrors().isEmpty());
    }

    @Test
    public void testInput() {
        assertEquals(ActionSupport.SUCCESS, action.input());
    }

    @Test
    public void testUpdateAnnotationDefinitions() {
        // Invalid because thre's not an Annotation EntityType selected.
        assertEquals(ActionSupport.INPUT, action.updateAnnotationDefinitions());
        KMPlotAnnotationBasedActionForm form = new KMPlotAnnotationBasedActionForm();
        form.setAnnotationGroupSelection("subjectAnnotations");
        action.getKmPlotForm().setAnnotationBasedForm(form);
        assertEquals(ActionSupport.SUCCESS, action.updateAnnotationDefinitions());
        assertEquals(1, action.getKmPlotForm().getAnnotationBasedForm().getAnnotationFieldDescriptors().size());
        assertTrue(action.getKmPlotForm().getAnnotationBasedForm().getAnnotationFieldDescriptors().containsKey("1"));
    }

    @Test
    public void testGetDisplayTab() {
        setupActionVariables();
        assertEquals("annotationTab", action.getDisplayTab());
        action.setDisplayTab("Testing");
        assertEquals("Testing", action.getDisplayTab());
    }

    @Test
    public void testUpdatePermissibleValues() {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.updatePermissibleValues());
    }

    @Test
    public void testCreatePlot() throws Exception {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        action.setCreatePlotSelected(true);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getKmPlotParameters().getSelectedValues().clear();
        action.getKmPlotParameters().getSelectedValues().add(val1);
        action.getKmPlotParameters().getSelectedValues().add(val2);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getKmPlotParameters().setSurvivalValueDefinition(new SurvivalValueDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setId(Long.valueOf(1));
        action.getKmPlotParameters().getSurvivalValueDefinition().setSurvivalStartDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getSurvivalStartDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setDeathDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getDeathDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setLastFollowupDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getLastFollowupDate().setDataType(AnnotationTypeEnum.DATE);
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        verify(analysisService, atLeastOnce()).createKMPlot(any(StudySubscription.class), any(AbstractKMParameters.class));
        assertFalse(action.isCreatable());

        action.getKmPlotForm().getAnnotationBasedForm().setSelectedAnnotationId("1");
        action.getKmPlotForm().getAnnotationBasedForm().setAnnotationGroupSelection("subjectAnnotations");
        action.getKmPlotForm().setSurvivalValueDefinitionId("1");
        assertTrue(action.isCreatable());
    }

    @Test
    public void testGetAllStringPValues() {
        plotService.setCaIntegratorPlotService(kmPlotService);
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        configuration.setTitle("title");
        configuration.setDurationLabel("duration");
        configuration.setProbabilityLabel("probability");
        SubjectGroup group1 = createGroup();
        configuration.getGroups().add(group1);
        SubjectGroup group2 = createGroup();
        configuration.getGroups().add(group2);
        KMPlot plot = plotService.generatePlot(configuration);
        SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, plot);
        assertEquals("1.10", action.getAllStringPValues().get("group").get("group"));
    }

    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(action.getKmPlotForm().getAnnotationBasedForm().getSelectedValuesIds().isEmpty());
        action.reset();
        assertTrue(action.getKmPlotForm().getAnnotationBasedForm().getSelectedValuesIds().isEmpty());
    }

    @Test
    public void testGetPlotUrl() {
        assertEquals("/caintegrator/retrieveAnnotationKMPlot.action?", action.getPlotUrl());
    }

    private SubjectGroup createGroup() {
        SubjectGroup group = new SubjectGroup();
        group.setColor(Color.BLACK);
        group.setName("group");
        SubjectSurvivalData survivalData = new SubjectSurvivalData(1, false);
        group.getSurvivalData().add(survivalData);
        return group;
    }

    private void setupActionVariables() {
        KMPlotAnnotationBasedActionForm form = new KMPlotAnnotationBasedActionForm();
        form.setAnnotationGroupSelection("subjectAnnotations");
        form.getSelectedValuesIds().add("1");
        form.getSelectedValuesIds().add("2");
        action.getKmPlotForm().setAnnotationBasedForm(form);
        AnnotationFieldDescriptor selectedAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        AnnotationDefinition selectedAnnotation = new AnnotationDefinition();
        selectedAnnotation.getPermissibleValueCollection().add(val1);
        selectedAnnotation.getPermissibleValueCollection().add(val2);
        selectedAnnotation.setDataType(AnnotationTypeEnum.STRING);
        selectedAnnotation.setId(Long.valueOf(1));
        selectedAnnotationFieldDescriptor.setDefinition(selectedAnnotation);
        selectedAnnotationFieldDescriptor.setId(1l);
        action.getKmPlotParameters().setSelectedAnnotation(selectedAnnotationFieldDescriptor);
    }


}
