/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotClinicalQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.web.Cai2WebUtil;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.analysis.DisplayableQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

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
        getForm().setDisplayableQueries(
                Cai2WebUtil.retrieveDisplayableQueries(getStudySubscription(), getQueryManagementService(), false));
        getForm().getDisplayableQueryMap().clear();
        for (DisplayableQuery displayableQuery : getForm().getDisplayableQueries()) {
            getForm().getDisplayableQueryMap().put(displayableQuery.getDisplayName(), displayableQuery);
        }
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
        if (isStudyHasMultiplePlatforms()) {
            plotParameters.setMultiplePlatformsInStudy(true);
        }
    }
    
    
    private void retrieveFormValues() {
        plotParameters.setGeneSymbol(getForm().getGeneSymbol());
        plotParameters.setReporterType(ReporterTypeEnum.getByValue(getForm().getReporterType()));
        plotParameters.setExclusiveGroups(getForm().isExclusiveGroups());
        plotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        plotParameters.setAddControlSamplesGroup(getForm().isAddControlSamplesGroup());
        plotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
        plotParameters.setPlatformName(getForm().getPlatformName());
        if (!getForm().getSelectedQueryNames().isEmpty()) {
            plotParameters.getQueries().clear();
            for (String name : getForm().getSelectedQueryNames()) {
                plotParameters.getQueries().add(getForm().getDisplayableQueryMap().get(name).getQuery());
            }
        }
    }
    
    private void refreshObjectInstances() {
        if (!plotParameters.getQueries().isEmpty()) {
            List <Query> newValues = new ArrayList<Query>();
            for (Query value : plotParameters.getQueries()) {
                if (!value.isSubjectListQuery()) {
                    Query newValue = getQueryManagementService().getRefreshedEntity(value);
                    newValues.add(newValue);
                } else {
                    newValues.add(value);
                }
            }
            plotParameters.getQueries().clear();
            plotParameters.getQueries().addAll(newValues);
        }
    }
    
    private void populateQueries() {
        initialize(getForm().getDisplayableQueries());
        loadSelectedQueries(getForm().getDisplayableQueries());
    }

    private void initialize(List<DisplayableQuery> displayableQueries) {
        if (!displayableQueries.isEmpty()
            && getForm().getSelectedQueries().isEmpty() 
            && getForm().getUnselectedQueries().isEmpty()) {
            getForm().setUnselectedQueries(new TreeMap<String, DisplayableQuery>());
            addQueriesToForm(displayableQueries);
        }
    }

    private void addQueriesToForm(List<DisplayableQuery> displayableQueries) {
        for (DisplayableQuery query : displayableQueries) {
            getForm().getUnselectedQueries().put(query.getDisplayName(), query);
        }
    }
    
    private void loadSelectedQueries(List<DisplayableQuery> displayableQueries) {
        if (!plotParameters.getQueries().isEmpty()) {
            getForm().getSelectedQueries().clear();
            Set<Query> usedQueries = new HashSet<Query>();
            for (Query query : plotParameters.getQueries()) {
                DisplayableQuery displayableQuery = getForm().getDisplayableQueryMap().get(DisplayableQuery
                        .getDisplayableQueryName(query));
                getForm().getSelectedQueries().put(displayableQuery.getDisplayName(), displayableQuery);
                usedQueries.add(query);
            }
            loadAvailableQueries(displayableQueries, usedQueries);
        }
    }

    private void loadAvailableQueries(List<DisplayableQuery> displayableQueries, Set<Query> usedQueries) {
        getForm().getUnselectedQueries().clear();
        for (DisplayableQuery displayableQuery : displayableQueries) {
            if (!usedQueries.contains(displayableQuery.getQuery())) {
                getForm().getUnselectedQueries().put(displayableQuery.getDisplayName(), displayableQuery);
            }
        }
    }

    /**
     * Updates the control sample sets based on the platform selected.
     * @return struts return value.
     */
    public String updateControlSampleSets() {
        getForm().getControlSampleSets().clear();
        if (StringUtils.isBlank(plotParameters.getPlatformName())) {
            addActionError(getText("struts.messages.error.select.valid.platform"));
            return INPUT;
        }
        getForm().setControlSampleSets(getStudy().getStudyConfiguration().getControlSampleSetNames(
                plotParameters.getPlatformName()));
        clearClinicalQueryBasedGePlot();
        return SUCCESS;
    }

    /**

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
            clearClinicalQueryBasedGePlot();
            if (plotParameters.validate()) {
                try {
                    GeneExpressionPlotGroup plots = getAnalysisService().
                            createGeneExpressionPlot(getStudySubscription(), plotParameters);
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, plots);
                } catch (ControlSamplesNotMappedException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
                    addActionError(getText("struts.messages.error.geplot.selected.controls.not.mapped.to.patients", 
                            getArgs("6", e.getMessage())));
                } catch (Exception e) {
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getControlSampleSets() {
        return isStudyHasMultiplePlatforms() ? getForm().getControlSampleSets() 
                : super.getControlSampleSets();
    }


}
