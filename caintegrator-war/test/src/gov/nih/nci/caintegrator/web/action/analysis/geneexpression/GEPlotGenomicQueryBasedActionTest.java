/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.geneexpression.GEPlotGenomicQueryBasedAction;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Before;

public class GEPlotGenomicQueryBasedActionTest extends AbstractSessionBasedTest {

    private GEPlotGenomicQueryBasedAction action;

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
        action = new GEPlotGenomicQueryBasedAction();
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
        SurvivalValueDefinition survivalValue = new SurvivalValueDefinition();
        survivalValue.setId(Long.valueOf(1));
        study.getSurvivalValueDefinitionCollection().add(survivalValue);
        return study;
    }

    @Test
    public void testPrepare() {
        setupActionVariables();
        action.prepare();
        verify(queryManagementService, atLeastOnce()).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
        assertNotNull(action.getGePlotForm().getGeneExpressionQueryBasedForm().getSelectedQueryId());
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
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        verify(analysisService, atLeastOnce()).createGeneExpressionPlot(any(StudySubscription.class), any(AbstractGEPlotParameters.class));
        assertTrue(action.isCreatable());
    }

    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(StringUtils.isBlank(action.getGePlotForm().getGeneExpressionQueryBasedForm().getSelectedQueryId()));
        action.reset();
        assertFalse(StringUtils.isBlank(action.getGePlotForm().getGeneExpressionQueryBasedForm().getSelectedQueryId()));
        action.setResetSelected(true);
        action.reset();
        assertTrue(StringUtils.isBlank(action.getGePlotForm().getGeneExpressionQueryBasedForm().getSelectedQueryId()));
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
        action.getGePlotForm().getGeneExpressionQueryBasedForm().setSelectedQueryId("1");
        action.getGePlotForm().getGeneExpressionQueryBasedForm().getQueries().put("1", new Query());
        action.getGePlotForm().getGeneExpressionQueryBasedForm().
                setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue());
    }


}
