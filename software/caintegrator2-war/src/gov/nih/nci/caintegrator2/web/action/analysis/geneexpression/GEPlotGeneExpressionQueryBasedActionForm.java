/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Form used to store input values for Gene Expression Query Based GE Plots. 
 */
public class GEPlotGeneExpressionQueryBasedActionForm {
    
    private String selectedQueryId;
    private String reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
    private boolean resetSelected = false;
    
    // JSP Select List Options
    private final Map<String, Query> queries = new HashMap<String, Query>();
    
    /**
     * Clears all the variables to null.
     */
    public void clear() {
        selectedQueryId = null;
        reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
        queries.clear();        
    }

    /**
     * @return the selectedQueryId
     */
    public String getSelectedQueryId() {
        return selectedQueryId;
    }

    /**
     * @param selectedQueryId the selectedQueryId to set
     */
    public void setSelectedQueryId(String selectedQueryId) {
        this.selectedQueryId = selectedQueryId;
    }

    /**
     * @return the reporterType
     */
    public String getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(String reporterType) {
        this.reporterType = reporterType;
    }

    /**
     * @return the queries
     */
    public Map<String, Query> getQueries() {
        return queries;
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
