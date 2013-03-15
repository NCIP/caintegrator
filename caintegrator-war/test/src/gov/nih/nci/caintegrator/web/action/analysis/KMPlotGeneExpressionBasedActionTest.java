/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KMPlotGeneExpressionBasedActionTest extends AbstractSessionBasedTest {

    private KMPlotGeneExpressionBasedAction action;
    private KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();
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
        action = new KMPlotGeneExpressionBasedAction();
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
        action.getKmPlotForm().setSurvivalValueDefinitionId("1");
        setupActionVariables();
        action.prepare();
        verify(queryManagementService, atLeastOnce()).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
        assertTrue(!action.getKmPlotForm().getSurvivalValueDefinitions().isEmpty());
    }

    @Test
    public void testValidate() {
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        action.getKmPlotForm().getSurvivalValueDefinitions().put("1", new SurvivalValueDefinition());
        action.validate();
        assertTrue(action.getActionErrors().isEmpty());
        action.getCurrentStudy().getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        ActionContext.getContext().getValueStack().setValue("studySubscription", null);
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
    }

    @Test
    public void testInput() {
        assertEquals(ActionSupport.SUCCESS, action.input());
    }

    @Test
    public void testCreatePlot() throws Exception {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        action.setCreatePlotSelected(true);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getKmPlotParameters().setSurvivalValueDefinition(new SurvivalValueDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setId(Long.valueOf(1));
        action.getKmPlotParameters().getSurvivalValueDefinition().setSurvivalStartDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getSurvivalStartDate()
            .setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setDeathDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getDeathDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setLastFollowupDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getLastFollowupDate()
        .setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().setControlSampleSetName("controls");
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        verify(analysisService, atLeastOnce()).createKMPlot(any(StudySubscription.class),
                any(AbstractKMParameters.class));

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
        SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, plot);
        assertEquals("1.10", action.getAllStringPValues().get("group").get("group"));
    }

    @Test
    public void testReset() {
        setupActionVariables();
        assertNotNull(action.getKmPlotForm().getGeneExpressionBasedForm().getGeneSymbol());
        action.reset();
        assertNotNull(action.getKmPlotForm().getGeneExpressionBasedForm().getGeneSymbol());
        action.setResetSelected(true);
        action.reset();
        assertNull(action.getKmPlotForm().getGeneExpressionBasedForm().getGeneSymbol());
    }

    @Test
    public void testGetPlotUrl() {
        assertEquals("/caintegrator/retrieveGeneExpressionKMPlot.action?", action.getPlotUrl());
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
        action.getKmPlotParameters().setGeneSymbol("EGFR");
        action.getKmPlotParameters().setOverValue(2.0F);
        action.getKmPlotParameters().setUnderValue(2.0F);
        action.getKmPlotForm().getGeneExpressionBasedForm().setGeneSymbol("EGFR");
        action.getKmPlotForm().getGeneExpressionBasedForm().setOverexpressedNumber("2.0");
        action.getKmPlotForm().getGeneExpressionBasedForm().setUnderexpressedNumber("2.0");
    }


}
