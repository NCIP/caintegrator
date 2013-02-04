/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;


import gov.nih.nci.caintegrator2.application.analysis.KMQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.web.Cai2WebUtil;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Action dealing with Kaplan-Meier Query Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class KMPlotQueryBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;
    private static final String QUERY_PLOT_URL = "/" + SessionHelper.WAR_CONTEXT_NAME + "/retrieveQueryKMPlot.action?";
    private KMQueryBasedParameters kmPlotParameters = new KMQueryBasedParameters();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(QUERY_TAB);
        getForm().setDisplayableQueries(
                Cai2WebUtil.retrieveDisplayableQueries(getStudySubscription(), getQueryManagementService(), true));
        getForm().getDisplayableQueryMap().clear();
        for (DisplayableQuery displayableQuery : getForm().getDisplayableQueries()) {
            getForm().getDisplayableQueryMap().put(displayableQuery.getDisplayName(), displayableQuery);
        }
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
    }
    
    
    private void retrieveFormValues() {
        kmPlotParameters.setExclusiveGroups(getForm().isExclusiveGroups());
        kmPlotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        if (!getForm().getSelectedQueryNames().isEmpty()) {
            kmPlotParameters.getQueries().clear();
            for (String name : getForm().getSelectedQueryNames()) {
                kmPlotParameters.getQueries().add(getForm().getDisplayableQueryMap().get(name).getQuery());
            }
        }
    }
    
    private void refreshObjectInstances() {
        if (!kmPlotParameters.getQueries().isEmpty()) {
            List <Query> newValues = new ArrayList<Query>();
            for (Query value : kmPlotParameters.getQueries()) {
                if (!value.isSubjectListQuery()) {
                    Query newValue = getQueryManagementService().getRefreshedEntity(value);
                    newValues.add(newValue);
                } else {
                    newValues.add(value);
                }
            }
            kmPlotParameters.getQueries().clear();
            kmPlotParameters.getQueries().addAll(newValues);
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
            for (DisplayableQuery query : displayableQueries) {
                getForm().getUnselectedQueries().put(query.getDisplayName(), query);
            }
        }
    }
    
    private void loadSelectedQueries(List<DisplayableQuery> displayableQueries) {
        if (!kmPlotParameters.getQueries().isEmpty()) {
            getForm().getSelectedQueries().clear();
            Set<Query> usedQueries = new HashSet<Query>();
            for (Query query : kmPlotParameters.getQueries()) {
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
     * Clears all input values and km plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        if (isResetSelected()) {
            clearQueryBasedKmPlot();
            getForm().clear();
            kmPlotParameters.clear();
        }
        return SUCCESS;
    }

    private void clearQueryBasedKmPlot() {
        SessionHelper.setKmPlot(PlotTypeEnum.QUERY_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    public String input() {
        setDisplayTab(QUERY_TAB);
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            clearQueryBasedKmPlot();
            if (kmPlotParameters.validate()) {
                try {
                    KMPlot plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
                    SessionHelper.setKmPlot(PlotTypeEnum.QUERY_BASED, plot);
                } catch (Exception e) {
                    SessionHelper.setKmPlot(PlotTypeEnum.QUERY_BASED, null);
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
    public SortedMap<String, SortedMap<String, String>> getAllStringPValues() {
        if (SessionHelper.getQueryBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getQueryBasedKmPlot());
        }
        return new TreeMap<String, SortedMap<String, String>>();
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
    private KMPlotQueryBasedActionForm getForm() {
        return getKmPlotForm().getQueryBasedForm();
    }

    /**
     * @return the kmPlotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public KMQueryBasedParameters getKmPlotParameters() {
        return kmPlotParameters;
    }

    /**
     * @param kmPlotParameters the kmPlotParameters to set
     */
    public void setKmPlotParameters(KMQueryBasedParameters kmPlotParameters) {
        this.kmPlotParameters = kmPlotParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlotUrl() {
        return QUERY_PLOT_URL;
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
     * 
     * @return list of genesNotFound.
     */
    public List<String> getSubjectsNotFound() {
        if (SessionHelper.getQueryBasedKmPlot() != null) {
            return SessionHelper.getQueryBasedKmPlot().getConfiguration().getSubjectsNotFound();
        }
        return new ArrayList<String>();
    }

}
