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
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Action dealing with Kaplan-Meier Query Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class KMPlotQueryBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;
    private static final String QUERY_PLOT_URL = "/caintegrator2/retrieveQueryKMPlot.action?";
    private KMQueryBasedParameters kmPlotParameters = new KMQueryBasedParameters();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(QUERY_TAB);
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
    }
    
    
    private void retrieveFormValues() {
        kmPlotParameters.setExclusiveGroups(getForm().isExclusiveGroups());
        kmPlotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        if (!getForm().getSelectedQueryIDs().isEmpty()) {
            kmPlotParameters.getQueries().clear();
            for (String id : getForm().getSelectedQueryIDs()) {
                Query query = new Query();
                query.setId(Long.valueOf(id));
                kmPlotParameters.getQueries().add(query);
            }
        }
    }
    
    private void refreshObjectInstances() {
        if (!kmPlotParameters.getQueries().isEmpty()) {
            List <Query> newValues = new ArrayList<Query>();
            for (Query value : kmPlotParameters.getQueries()) {
                Query newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            kmPlotParameters.getQueries().clear();
            kmPlotParameters.getQueries().addAll(newValues);
        }
    }
    
    private void populateQueries() {
        initialize();
        loadSelectedQueries();
    }

    private void initialize() {
        if (getStudySubscription() != null 
            && getStudySubscription().getQueryCollection() != null
            && getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty() 
            && getKmPlotForm().getQueryBasedForm().getUnselectedQueries().isEmpty()) {
            getKmPlotForm().getQueryBasedForm().setUnselectedQueries(new HashMap<String, Query>());
            for (Query query 
                    : getStudySubscription().getQueryCollection()) {
                getKmPlotForm().getQueryBasedForm().getUnselectedQueries().
                                                    put(query.getId().toString(), query);
            }
        }
    }
    
    private void loadSelectedQueries() {
        if (!kmPlotParameters.getQueries().isEmpty()) {
            getKmPlotForm().getQueryBasedForm().getSelectedQueries().clear();
            Set<Query> usedQueries = new HashSet<Query>();
            for (Query query : kmPlotParameters.getQueries()) {
                getKmPlotForm().getQueryBasedForm().getSelectedQueries().put(query.getId().toString(), query);
                usedQueries.add(query);
            }
            loadAvailableQueries(usedQueries);
        }
    }

    private void loadAvailableQueries(Set<Query> usedQueries) {
        getKmPlotForm().getQueryBasedForm().getUnselectedQueries().clear();
        for (Query query 
                : getStudySubscription().getQueryCollection()) {
            if (!usedQueries.contains(query)) {
                getKmPlotForm().getQueryBasedForm().getUnselectedQueries().
                                                put(query.getId().toString(), query);
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
            if (kmPlotParameters.validate()) {
                try {
                    KMPlot plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
                    SessionHelper.setKmPlot(PlotTypeEnum.QUERY_BASED, plot);
                } catch (InvalidCriterionException e) {
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
    public Map<String, Map<String, String>> getAllStringPValues() {
        if (SessionHelper.getQueryBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getQueryBasedKmPlot());
        }
        return new HashMap<String, Map<String, String>>();
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

}
