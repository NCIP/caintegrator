/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Form used to store input values for Query Based KM Plots. 
 */
public class KMPlotQueryBasedActionForm {
    
    private List<DisplayableQuery> displayableQueries = new ArrayList<DisplayableQuery>();
    private Map<String, DisplayableQuery> displayableQueryMap = new HashMap<String, DisplayableQuery>();
    private List<String> selectedQueryNames = new ArrayList<String>();
    private List<String> unselectedQueryNames = new ArrayList<String>();
    private boolean exclusiveGroups = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private boolean resetSelected = false;
    
    // JSP Select List Options
    private Map<String, DisplayableQuery> selectedQueries = new HashMap<String, DisplayableQuery>();
    private SortedMap<String, DisplayableQuery> unselectedQueries = new TreeMap<String, DisplayableQuery>();
    
    /**
     * Clears all the variables to null.
     */
    public void clear() {
        selectedQueryNames = new ArrayList<String>();
        unselectedQueryNames = new ArrayList<String>();
        exclusiveGroups = false;
        addPatientsNotInQueriesGroup = false;
        selectedQueries.clear();
        unselectedQueries.clear();
    }

    /**
     * @return the displayableQueries
     */
    public List<DisplayableQuery> getDisplayableQueries() {
        return displayableQueries;
    }

    /**
     * @param displayableQueries the displayableQueries to set
     */
    public void setDisplayableQueries(List<DisplayableQuery> displayableQueries) {
        this.displayableQueries = displayableQueries;
    }

    /**
     * @return the displayableQueryMap
     */
    public Map<String, DisplayableQuery> getDisplayableQueryMap() {
        return displayableQueryMap;
    }

    /**
     * @param displayableQueryMap the displayableQueryMap to set
     */
    public void setDisplayableQueryMap(Map<String, DisplayableQuery> displayableQueryMap) {
        this.displayableQueryMap = displayableQueryMap;
    }

    /**
     * @return the selectedQueryNames
     */
    public List<String> getSelectedQueryNames() {
        return selectedQueryNames;
    }

    /**
     * @param selectedQueryNames the selectedQueryNames to set
     */
    public void setSelectedQueryNames(List<String> selectedQueryNames) {
        this.selectedQueryNames = selectedQueryNames;
    }

    /**
     * @return the unselectedQueryNames
     */
    public List<String> getUnselectedQueryNames() {
        return unselectedQueryNames;
    }

    /**
     * @param unselectedQueryNames the unselectedQueryNames to set
     */
    public void setUnselectedQueryNames(List<String> unselectedQueryNames) {
        this.unselectedQueryNames = unselectedQueryNames;
    }

    /**
     * @return the exclusiveGroups
     */
    public boolean isExclusiveGroups() {
        return exclusiveGroups;
    }

    /**
     * @param exclusiveGroups the exclusiveGroups to set
     */
    public void setExclusiveGroups(boolean exclusiveGroups) {
        this.exclusiveGroups = exclusiveGroups;
    }

    /**
     * @return the addPatientsNotInQueriesGroup
     */
    public boolean isAddPatientsNotInQueriesGroup() {
        return addPatientsNotInQueriesGroup;
    }

    /**
     * @param addPatientsNotInQueriesGroup the addPatientsNotInQueriesGroup to set
     */
    public void setAddPatientsNotInQueriesGroup(boolean addPatientsNotInQueriesGroup) {
        this.addPatientsNotInQueriesGroup = addPatientsNotInQueriesGroup;
    }

    /**
     * @return the selectedQueries
     */
    public Map<String, DisplayableQuery> getSelectedQueries() {
        return selectedQueries;
    }

    /**
     * @param selectedQueries the selectedQueries to set
     */
    public void setSelectedQueries(Map<String, DisplayableQuery> selectedQueries) {
        this.selectedQueries = selectedQueries;
    }

    /**
     * @return the unselectedQueries
     */
    public SortedMap<String, DisplayableQuery> getUnselectedQueries() {
        return unselectedQueries;
    }

    /**
     * @param unselectedQueries the unselectedQueries to set
     */
    public void setUnselectedQueries(SortedMap<String, DisplayableQuery> unselectedQueries) {
        this.unselectedQueries = unselectedQueries;
    }

    /**
     * @return the resetSelected
     */
    public boolean isResetSelected() {
        return resetSelected;
    }

    /**
     * @param resetSelected the resetSelected to set
     */
    public void setResetSelected(boolean resetSelected) {
        this.resetSelected = resetSelected;
    }
}
