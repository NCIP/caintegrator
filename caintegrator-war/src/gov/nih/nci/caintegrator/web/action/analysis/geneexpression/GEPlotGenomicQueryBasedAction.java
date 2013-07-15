/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.GEPlotGenomicQueryBasedParameters;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.web.SessionHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action dealing with Gene Expression Genomic Query Based plotting.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class GEPlotGenomicQueryBasedAction extends AbstractGeneExpressionAction {

    private static final long serialVersionUID = 1L;
    private static final String GENOMIC_QUERY_MEAN_PLOT_URL = "retrieveGenomicQueryGEPlot_mean.action?";
    private static final String GENOMIC_QUERY_MEDIAN_PLOT_URL = "retrieveGenomicQueryGEPlot_median.action?";
    private static final String GENOMIC_QUERY_LOG2_PLOT_URL = "retrieveGenomicQueryGEPlot_log2.action?";
    private static final String GENOMIC_QUERY_BW_PLOT_URL = "retrieveGenomicQueryGEPlot_bw.action?";
    private final GEPlotGenomicQueryBasedParameters plotParameters = new GEPlotGenomicQueryBasedParameters();


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(GENOMIC_QUERY_TAB);
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
    }

    private void retrieveFormValues() {
        plotParameters.setReporterType(ReporterTypeEnum.
                getByValue(getGePlotForm().getGeneExpressionQueryBasedForm().getReporterType()));
        if (!StringUtils.isBlank(getForm().getSelectedQueryId())) {
            Query query = new Query();
            query.setId(Long.valueOf(getForm().getSelectedQueryId()));
            plotParameters.setQuery(query);
        }
    }

    private void refreshObjectInstances() {
        if (plotParameters.getQuery() != null) {
            plotParameters.setQuery(getQueryManagementService().getRefreshedEntity(plotParameters.getQuery()));
        }
    }

    private void populateQueries() {
        initialize();
    }

    private void initialize() {
        if (getStudySubscription() != null && getStudySubscription().getQueryCollection() != null) {
            getGePlotForm().getGeneExpressionQueryBasedForm().getQueries().clear();
            for (Query query : getStudySubscription().getQueryCollection()) {
                if (ResultTypeEnum.GENE_EXPRESSION.equals(query.getResultType())) {
                    getGePlotForm().getGeneExpressionQueryBasedForm().getQueries().put(query.getId().toString(), query);
                }
            }
        }
    }

    /**
     * Clears all input values and ge plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        if (isResetSelected()) {
            clearGenomicQueryBasedGePlot();
            getForm().clear();
            plotParameters.clear();
        }
        return SUCCESS;
    }

    private void clearGenomicQueryBasedGePlot() {
        SessionHelper.setGePlots(PlotTypeEnum.GENOMIC_QUERY_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    @Override
    public String input() {
        setDisplayTab(GENOMIC_QUERY_TAB);
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            clearGenomicQueryBasedGePlot();
            if (getPlotParameters().validate()) {
                try {
                    GeneExpressionPlotGroup plots = getAnalysisService().
                            createGeneExpressionPlot(getStudySubscription(), plotParameters);
                    SessionHelper.setGePlots(PlotTypeEnum.GENOMIC_QUERY_BASED, plots);
                } catch (ControlSamplesNotMappedException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.GENOMIC_QUERY_BASED, null);
                    addActionError(getText("struts.messages.error.geplot.controls.not.mapped.to.patients",
                            getArgs(e.getMessage())));
                } catch (Exception e) {
                    SessionHelper.setGePlots(PlotTypeEnum.GENOMIC_QUERY_BASED, null);
                    addActionError(e.getMessage());
                }
            }
            setCreatePlotRunning(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        return true;
    }


    /**
     * @return
     */
    private GEPlotGeneExpressionQueryBasedActionForm getForm() {
        return getGePlotForm().getGeneExpressionQueryBasedForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBoxWhiskerPlotUrl() {
        return GENOMIC_QUERY_BW_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLog2PlotUrl() {
        return GENOMIC_QUERY_LOG2_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMeanPlotUrl() {
        return GENOMIC_QUERY_MEAN_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMedianPlotUrl() {
        return GENOMIC_QUERY_MEDIAN_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public GEPlotGenomicQueryBasedParameters getPlotParameters() {
        return plotParameters;
    }

    /**
     * This is set only when the reset button on the JSP is pushed, so we know that a reset needs to occur.
     * @param resetSelected T/F value.
     */
    public void setResetSelected(boolean resetSelected) {
        getForm().setResetSelected(resetSelected);
    }

    private boolean isResetSelected() {
        return getForm().isResetSelected();
    }

}
