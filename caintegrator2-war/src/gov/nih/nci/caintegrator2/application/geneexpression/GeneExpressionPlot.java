/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import java.io.OutputStream;

/**
 * 
 */
public interface GeneExpressionPlot {

    /**
     * Writes the image to the output stream.
     * @param output - output stream to write image to.
     */
    void writePlotImage(OutputStream output);

}
