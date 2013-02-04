/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

/**
 * Creates the <code>GeneExpressionPlot</code>'s for the 4 different plot types, and puts them in the
 * <code>GeneExpressionPlotGroup</code>.
 */
public interface GeneExpressionPlotService {
    
    /**
     * Creates a GeneExpressionPlotGroup for the given configuration.
     * @param configuration - configuration for the plots.
     * @return plots.
     */
    GeneExpressionPlotGroup generatePlots(GeneExpressionPlotConfiguration configuration);
}
