/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.DisplayableQuery;
import gov.nih.nci.caintegrator.web.action.analysis.geneexpression.GEPlotClinicalQueryBasedAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GEPlotClinicalQueryBasedActionTest extends AbstractSessionBasedTest {

    private GEPlotClinicalQueryBasedAction action;
    private StudySubscription subscription;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new GEPlotClinicalQueryBasedAction();
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
        return study;
    }

    @Test
    public void testPrepare() {
        setupActionVariables();
        action.getGePlotForm().getClinicalQueryBasedForm().setGeneSymbol("EGFR");
        action.prepare();
        verify(queryManagementService, atLeastOnce()).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
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
        gdsc.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
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
        action.getPlotParameters().setGeneSymbol("EGFR");
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        verify(analysisService, atLeastOnce()).createGeneExpressionPlot(any(StudySubscription.class), any(AbstractGEPlotParameters.class));
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

        Query query1 = new Query();
        query1.setName("1");
        Query query2 = new Query();
        query2.setName("2");
        subscription.getQueryCollection().add(query1);
        subscription.getQueryCollection().add(query2);
        action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueryNames().add(DisplayableQuery.getDisplayableQueryName(query1));
        action.getGePlotForm().getClinicalQueryBasedForm().getUnselectedQueryNames().add(DisplayableQuery.getDisplayableQueryName(query2));
        action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().put(DisplayableQuery.getDisplayableQueryName(query1), new DisplayableQuery(query1));
        action.getGePlotForm().getClinicalQueryBasedForm().getSelectedQueries().put(DisplayableQuery.getDisplayableQueryName(query2), new DisplayableQuery(query2));
        action.getGePlotForm().getClinicalQueryBasedForm().
                setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue());
        action.getGePlotForm().getClinicalQueryBasedForm().setAddPatientsNotInQueriesGroup(true);
        action.getGePlotForm().getClinicalQueryBasedForm().setExclusiveGroups(true);
    }


}
