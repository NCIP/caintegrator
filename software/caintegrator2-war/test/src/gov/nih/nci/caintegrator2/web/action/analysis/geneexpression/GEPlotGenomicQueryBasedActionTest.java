/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GEPlotGenomicQueryBasedActionTest {
    
    private GEPlotGenomicQueryBasedAction action;
    private StudyManagementServiceStub studyManagementServiceStub = new StudyManagementServiceStub();
    private AnalysisServiceStub analysisServiceStub = new AnalysisServiceStub();
    
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
        action = new GEPlotGenomicQueryBasedAction();
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
        setupActionVariables();
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        assertNotNull(action.getGePlotForm().getGenomicQueryBasedForm().getSelectedQueryId());
    }
    
    @Test
    public void testValidate() {
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
        action.clearErrorsAndMessages();
        action.getCurrentStudy().setStudyConfiguration(new StudyConfiguration());
        GenomicDataSourceConfiguration gdsc =  new GenomicDataSourceConfiguration();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(gdsc);
        assertTrue(action.getActionErrors().isEmpty());
        gdsc.setDataType(GenomicDataSourceDataTypeEnum.COPY_NUMBER);
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
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
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        assertTrue(analysisServiceStub.createGEPlotCalled);
        
        assertTrue(action.isCreatable());
    }
    
    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(StringUtils.isBlank(action.getGePlotForm().getGenomicQueryBasedForm().getSelectedQueryId()));
        action.reset();
        assertFalse(StringUtils.isBlank(action.getGePlotForm().getGenomicQueryBasedForm().getSelectedQueryId()));
        action.setResetSelected(true);
        action.reset();
        assertTrue(StringUtils.isBlank(action.getGePlotForm().getGenomicQueryBasedForm().getSelectedQueryId()));
    }
    
    @Test
    public void testGetPlotUrl() {
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.MEAN.getValue()).
                                        contains("retrieveGenomicQueryGEPlot_mean.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.MEDIAN.getValue()).
                                        contains("retrieveGenomicQueryGEPlot_median.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.LOG2_INTENSITY.getValue()).
                                        contains("retrieveGenomicQueryGEPlot_log2.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.BOX_WHISKER_LOG2_INTENSITY.getValue()).
                                        contains("retrieveGenomicQueryGEPlot_bw.action?"));
    }

    private void setupActionVariables() {
        action.getPlotParameters().setQuery(new Query());
        action.getPlotParameters().setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        action.getGePlotForm().getGenomicQueryBasedForm().setSelectedQueryId("1");
        action.getGePlotForm().getGenomicQueryBasedForm().getQueries().put("1", new Query());
        action.getGePlotForm().getGenomicQueryBasedForm().
                setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue());
    }
    

}
