/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.application.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Parameters used for creating a Query Based KaplanMeier plot. 
 */
public class KMQueryBasedParameters extends AbstractKMParameters {

    private final List<Query> queries = new ArrayList<Query>();
    private boolean exclusiveGroups = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private final List<String> subjectsNotFoundInStudy = new ArrayList<String>();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        Integer queriesSize = queries.size();
        queriesSize += isAddPatientsNotInQueriesGroup() ? 1 : 0;
        if (queriesSize < 2) {
            getErrorMessages().add("Must select at least two queries to create plot (or one query and checkbox #3)");
            isValid = false;
        }
        isValid = validateSurvivalValueDefinition(isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        queries.clear();
        exclusiveGroups = false;
        addPatientsNotInQueriesGroup = false;
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
     * @return the queries
     */
    public List<Query> getQueries() {
        return queries;
    }

    /**
     * @return the subjectsNotFoundInStudy
     */
    public List<String> getSubjectsNotFoundInStudy() {
        return subjectsNotFoundInStudy;
    }
    
    
}
