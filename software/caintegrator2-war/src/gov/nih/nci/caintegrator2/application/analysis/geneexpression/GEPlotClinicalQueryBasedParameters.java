/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

/**
 * Parameters used for creating an Clinical Query based Gene Expression plot. 
 */
public class GEPlotClinicalQueryBasedParameters extends AbstractGEPlotParameters {

    private final List<Query> queries = new ArrayList<Query>();
    private boolean exclusiveGroups = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private ReporterTypeEnum reporterType;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        if (isMultiplePlatformsInStudy() && StringUtils.isBlank(getPlatformName())) {
            getErrorMessages().add("Must select a platform");
            isValid = false;
        }
        if (StringUtils.isBlank(getGeneSymbol())) {
            getErrorMessages().add("Must enter a gene symbol");
            isValid = false;
        }
        if (queries.isEmpty()) {
            getErrorMessages().add("Must select at least one query.");
            isValid = false;
        }
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        queries.clear();
        reporterType = null;
        exclusiveGroups = false;
        addPatientsNotInQueriesGroup = false;
    }

    /**
     * @return the reporterType
     */
    public ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(ReporterTypeEnum reporterType) {
        this.reporterType = reporterType;
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

}
