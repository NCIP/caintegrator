/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.kmplot;

/**
 * Subsystem interface for generation of Kaplan-Meier survival plots.
 */
public interface KMPlotService {
    
    /**
     * Generates a plot from the given configuration.
     * 
     * @param configuration contains the data to be plotted and configuration information for the output.
     * @return the plot.
     */
    KMPlot generatePlot(KMPlotConfiguration configuration);

}
