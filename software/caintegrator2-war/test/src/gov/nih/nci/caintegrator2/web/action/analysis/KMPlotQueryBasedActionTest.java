/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.CaIntegratorKMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KMPlotQueryBasedActionTest extends AbstractSessionBasedTest {
    
    private KMPlotQueryBasedAction action;
    private QueryManagementServiceStub queryManagementServiceStub = new QueryManagementServiceStub();
    private AnalysisServiceStub analysisServiceStub = new AnalysisServiceStub();
    private KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();
    private StudySubscription subscription;
    private SurvivalValueDefinition survivalValue;
    
    @Before
    public void setUp() {
        super.setUp();
        subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new KMPlotQueryBasedAction();
        action.setAnalysisService(analysisServiceStub);
        WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        
        action.setQueryManagementService(queryManagementServiceStub);
        queryManagementServiceStub.clear();
        analysisServiceStub.clear();
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
        SurvivalValueDefinition svd = new SurvivalValueDefinition();
        svd.setId(Long.valueOf(1));
        action.getKmPlotForm().setSurvivalValueDefinitionId("1");
        action.getKmPlotParameters().setSurvivalValueDefinition(svd);
        setupActionVariables();
        action.prepare();
        assertTrue(queryManagementServiceStub.getRefreshedEntityCalled);
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
        action.getKmPlotParameters().getSurvivalValueDefinition().getSurvivalStartDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setDeathDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getDeathDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setLastFollowupDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getLastFollowupDate().setDataType(AnnotationTypeEnum.DATE);
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
        assertEquals("/caintegrator/retrieveQueryKMPlot.action?", action.getPlotUrl());
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
        Query query1 = new Query();
        query1.setName("1");
        Query query2 = new Query();
        query2.setName("2");
        subscription.getQueryCollection().add(query1);
        subscription.getQueryCollection().add(query2);
        action.getKmPlotForm().getQueryBasedForm().setAddPatientsNotInQueriesGroup(true);
        action.getKmPlotForm().getQueryBasedForm().setExclusiveGroups(true);
        action.getKmPlotForm().getQueryBasedForm().getSelectedQueryNames().add(DisplayableQuery.getDisplayableQueryName(query1));
        action.getKmPlotForm().getQueryBasedForm().getUnselectedQueryNames().add(DisplayableQuery.getDisplayableQueryName(query1));
        action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().put(DisplayableQuery.getDisplayableQueryName(query1), new DisplayableQuery(query1));
        action.getKmPlotForm().getQueryBasedForm().getUnselectedQueries().put(DisplayableQuery.getDisplayableQueryName(query2), new DisplayableQuery(query2));
    }
    

}
