/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.CaIntegratorKMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KMPlotQueryBasedActionTest {
    
    private KMPlotQueryBasedAction action;
    private StudyManagementServiceStub studyManagementServiceStub = new StudyManagementServiceStub();
    private AnalysisServiceStub analysisServiceStub = new AnalysisServiceStub();
    private KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();
    
    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        StudySubscription subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        subscription.setQueryCollection(new HashSet<Query>());
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new KMPlotQueryBasedAction();
        action.setAnalysisService(analysisServiceStub);
        WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        
        action.setStudyManagementService(studyManagementServiceStub);
        studyManagementServiceStub.clear();
        analysisServiceStub.clear();
    }
    
    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        SurvivalValueDefinition survivalValue = new SurvivalValueDefinition();
        survivalValue.setId(Long.valueOf(1));
        study.getSurvivalValueDefinitionCollection().add(survivalValue);
        return study;
    }
    
    @Test
    public void testPrepare() {
        SurvivalValueDefinition svd = new SurvivalValueDefinition();
        svd.setId(Long.valueOf(1));
        action.getKmPlotForm().setSurvivalValueDefinitionId("1");
        action.getKmPlotParameters().setSurvivalValueDefinition(svd);
        setupActionVariables();
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        assertTrue(!action.getKmPlotForm().getSurvivalValueDefinitions().isEmpty());
    }
    
    @Test
    public void testValidate() {
        action.clearErrorsAndMessages();
        ActionContext.getContext().getValueStack().setValue("studySubscription", null);
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        action.getKmPlotForm().getSurvivalValueDefinitions().put("1", new SurvivalValueDefinition());
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        ActionContext.getContext().getValueStack().setValue("studySubscription", new StudySubscription());
        assertTrue(action.getActionErrors().isEmpty());
    }

    @Test
    public void testInput() {
        assertEquals(ActionSupport.SUCCESS, action.input());
    }

    @Test
    public void testCreatePlot() throws InterruptedException {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        action.setCreatePlotSelected(true);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getKmPlotParameters().setSurvivalValueDefinition(new SurvivalValueDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setId(Long.valueOf(1));
        action.getKmPlotParameters().getSurvivalValueDefinition().setSurvivalStartDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setDeathDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setLastFollowupDate(new AnnotationDefinition());
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        assertTrue(analysisServiceStub.createKMPlotCalled);
        
        assertTrue(action.isCreatable());
    }
    
    @Test
    public void testGetAllStringPValues() {
        plotService.setCaIntegratorPlotService(new CaIntegratorKMPlotServiceStub());
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        configuration.setTitle("title");
        configuration.setDurationLabel("duration");
        configuration.setProbabilityLabel("probability");
        SubjectGroup group1 = createGroup();
        configuration.getGroups().add(group1);
        SubjectGroup group2 = createGroup();
        configuration.getGroups().add(group2);
        KMPlot plot = plotService.generatePlot(configuration);
        SessionHelper.setKmPlot(PlotTypeEnum.QUERY_BASED, plot);
        assertEquals("1.10", action.getAllStringPValues().get("group").get("group"));
    }
    
    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty());
        action.reset();
        assertFalse(action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty());
        action.setResetSelected(true);
        action.reset();
        assertTrue(action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty());
    }
    
    @Test
    public void testGetPlotUrl() {
        assertEquals("/caintegrator2/retrieveQueryKMPlot.action?", action.getPlotUrl());
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
        action.getKmPlotParameters().setExclusiveGroups(true);
        action.getKmPlotParameters().setAddPatientsNotInQueriesGroup(true);
        action.getKmPlotParameters().getQueries().add(new Query());
        action.getKmPlotParameters().getQueries().add(new Query());
        action.getKmPlotForm().getQueryBasedForm().setAddPatientsNotInQueriesGroup(true);
        action.getKmPlotForm().getQueryBasedForm().setExclusiveGroups(true);
        action.getKmPlotForm().getQueryBasedForm().getSelectedQueryIDs().add("1");
        action.getKmPlotForm().getQueryBasedForm().getUnselectedQueryIDs().add("2");
        action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().put("1", new Query());
        action.getKmPlotForm().getQueryBasedForm().getUnselectedQueries().put("2", new Query());
    }
    

}
