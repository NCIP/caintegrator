/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GEPlotClinicalQueryBasedActionTest {
    
    private GEPlotClinicalQueryBasedAction action;
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
        action = new GEPlotClinicalQueryBasedAction();
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
        return study;
    }
    
    @Test
    public void testPrepare() {
        setupActionVariables();
        action.getGePlotForm().getClinicalQueryBasedForm().setGeneSymbol("EGFR");
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        action.prepare();
        assertEquals("EGFR", action.getPlotParameters().getGeneSymbol());
    }
    
    @Test
    public void testValidate() {
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
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getPlotParameters().setGeneSymbol("EGFR");
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        assertTrue(analysisServiceStub.createGEPlotCalled);
        
        assertTrue(action.isCreatable());
    }
    
    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().isEmpty());
        action.reset();
        assertFalse(action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().isEmpty());
        action.setResetSelected(true);
        action.reset();
        assertTrue(action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().isEmpty());
    }
    
    @Test
    public void testGetPlotUrl() {
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.MEAN.getValue()).
                                        contains("retrieveClinicalQueryGEPlot_mean.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.MEDIAN.getValue()).
                                        contains("retrieveClinicalQueryGEPlot_median.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.LOG2_INTENSITY.getValue()).
                                        contains("retrieveClinicalQueryGEPlot_log2.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.BOX_WHISKER_LOG2_INTENSITY.getValue()).
                                        contains("retrieveClinicalQueryGEPlot_bw.action?"));
    }

    private void setupActionVariables() {
        action.getPlotParameters().getQueries().add(new Query());
        action.getPlotParameters().getQueries().add(new Query());
        action.getPlotParameters().setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        action.getPlotParameters().setAddPatientsNotInQueriesGroup(true);
        action.getPlotParameters().setExclusiveGroups(true);
        action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueryIDs().add("1");
        action.getGePlotForm().getClinicalQueryBasedForm().getUnselectedQueryIDs().add("2");
        action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().put("1", new Query());
        action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().put("2", new Query());
        action.getGePlotForm().getClinicalQueryBasedForm().
                setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue());
        action.getGePlotForm().getClinicalQueryBasedForm().setAddPatientsNotInQueriesGroup(true);
        action.getGePlotForm().getClinicalQueryBasedForm().setExclusiveGroups(true);
    }
    

}
