/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Grouping for gene reporters in a Gene Expression Plot.
 */
public class PlotReporterGroup {

    private String name;
    private List<Double> geneExpressionValues = new ArrayList<Double>();
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the geneExpressionValues
     */
    public List<Double> getGeneExpressionValues() {
        return geneExpressionValues;
    }
    /**
     * @param geneExpressionValues the geneExpressionValues to set
     */
    public void setGeneExpressionValues(List<Double> geneExpressionValues) {
        this.geneExpressionValues = geneExpressionValues;
    }
    
}
