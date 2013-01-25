/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import java.io.OutputStream;

/**
 * Provides access to the plot image and other plot information.
 */
public interface KMPlot {
    
    /**
     * Writes the plot image to the given output stream.
     * 
     * @param out write image to this stream
     */
    void writePlotImage(OutputStream out);
    
    /**
     * Returns the log rank p-value between two groups.
     * 
     * @param group1 first group in the comparison
     * @param group2 second group in the comparison
     * @return the p-value.
     */
    Double getPValue(SubjectGroup group1, SubjectGroup group2);
    
    /**
     * 
     * @return configuration for kmPlot.
     */
    KMPlotConfiguration getConfiguration();

}
