/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotClinicalQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Action dealing with Gene Expression Clinical Query Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // see method runFirstCreatPlotThread
public class GEPlotClinicalQueryBasedAction extends AbstractGeneExpressionAction {

    private static final long serialVersionUID = 1L;
    private static final String CLINICAL_QUERY_MEAN_PLOT_URL = "retrieveClinicalQueryGEPlot_mean.action?";
    private static final String CLINICAL_QUERY_MEDIAN_PLOT_URL = "retrieveClinicalQueryGEPlot_median.action?";
    private static final String CLINICAL_QUERY_LOG2_PLOT_URL = "retrieveClinicalQueryGEPlot_log2.action?";
    private static final String CLINICAL_QUERY_BW_PLOT_URL = "retrieveClinicalQueryGEPlot_bw.action?";
    private final GEPlotClinicalQueryBasedParameters plotParameters = new GEPlotClinicalQueryBasedParameters();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(CLINICAL_QUERY_TAB);
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
    }
    
    
    private void retrieveFormValues() {
        plotParameters.setGeneSymbol(getForm().getGeneSymbol());
        plotParameters.setReporterType(ReporterTypeEnum.getByValue(getForm().getReporterType()));
        plotParameters.setExclusiveGroups(getForm().isExclusiveGroups());
        plotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        plotParameters.setAddControlSamplesGroup(getForm().isAddControlSamplesGroup());
        plotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
        if (!getForm().getSelectedQueryIDs().isEmpty()) {
            plotParameters.getQueries().clear();
            for (String id : getForm().getSelectedQueryIDs()) {
                Query query = new Query();
                query.setId(Long.valueOf(id));
                plotParameters.getQueries().add(query);
            }
        }
    }
    
    private void refreshObjectInstances() {
        if (!plotParameters.getQueries().isEmpty()) {
            List <Query> newValues = new ArrayList<Query>();
            for (Query value : plotParameters.getQueries()) {
                Query newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            plotParameters.getQueries().clear();
            plotParameters.getQueries().addAll(newValues);
        }
    }
    
    private void populateQueries() {
        initialize();
        loadSelectedQueries();
    }

    private void initialize() {
        if (getStudySubscription() != null 
            && getStudySubscription().getQueryCollection() != null
            && getForm().getSelectedQueries().isEmpty() 
            && getForm().getUnselectedQueries().isEmpty()) {
            getForm().setUnselectedQueries(new HashMap<String, Query>());
            addNonGenomicQueries();
        }
    }

    private void addNonGenomicQueries() {
        for (Query query 
                : getStudySubscription().getQueryCollection()) {
            if (!ResultTypeEnum.GENOMIC.equals(query.getResultType()) 
                && !Cai2Util.isCompoundCriterionGenomic(query.getCompoundCriterion())) {
                getForm().getUnselectedQueries().put(query.getId().toString(), query);
            }
        }
    }
    
    private void loadSelectedQueries() {
        if (!plotParameters.getQueries().isEmpty()) {
            getForm().getSelectedQueries().clear();
            Set<Query> usedQueries = new HashSet<Query>();
            for (Query query : plotParameters.getQueries()) {
                getForm().getSelectedQueries().put(query.getId().toString(), query);
                usedQueries.add(query);
            }
            loadAvailableQueries(usedQueries);
        }
    }

    private void loadAvailableQueries(Set<Query> usedQueries) {
        getForm().getUnselectedQueries().clear();
        for (Query query 
                : getStudySubscription().getQueryCollection()) {
            if (!usedQueries.contains(query)
                && !ResultTypeEnum.GENOMIC.equals(query.getResultType()) 
                && !Cai2Util.isCompoundCriterionGenomic(query.getCompoundCriterion())) {
                getForm().getUnselectedQueries().
                                                put(query.getId().toString(), query);
            }
        }
    }


    /**
     * Clears all input values and ge plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        if (isResetSelected()) {
            clearClinicalQueryBasedGePlot();
            getForm().clear();
            plotParameters.clear();
        }
        return SUCCESS;
    }

    private void clearClinicalQueryBasedGePlot() {
        SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    public String input() {
        setDisplayTab(CLINICAL_QUERY_TAB);
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity") // Multiple Catch Statements
    protected void runFirstCreatePlotThread() {
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            if (plotParameters.validate()) {
                try {
                    GeneExpressionPlotGroup plots = getAnalysisService().
                            createGeneExpressionPlot(getStudySubscription(), plotParameters);
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, plots);
                } catch (ControlSamplesNotMappedException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
                    addActionError("Group selected in step 6 invalid, control samples must all be mapped to patients: "
                            + e.getMessage());
                } catch (InvalidCriterionException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
                    addActionError(e.getMessage());
                } catch (GenesNotFoundInStudyException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
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
     * {@inheritDoc}
     */
    @Override
    public String getMeanPlotUrl() {
        return CLINICAL_QUERY_MEAN_PLOT_URL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMedianPlotUrl() {
        return CLINICAL_QUERY_MEDIAN_PLOT_URL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLog2PlotUrl() {
        return CLINICAL_QUERY_LOG2_PLOT_URL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBoxWhiskerPlotUrl() {
        return CLINICAL_QUERY_BW_PLOT_URL;
    }


    /**
     * @return
     */
    private GEPlotClinicalQueryBasedActionForm getForm() {
        return getGePlotForm().getClinicalQueryBasedForm();
    }

    /**
     * @return the plotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public GEPlotClinicalQueryBasedParameters getPlotParameters() {
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
