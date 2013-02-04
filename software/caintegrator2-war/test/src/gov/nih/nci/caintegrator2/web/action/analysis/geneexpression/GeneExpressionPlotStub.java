/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import java.io.OutputStream;

import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlot;

public class GeneExpressionPlotStub implements GeneExpressionPlot {

    public boolean writePlotImageCalled;
    
    public void clear() {
        writePlotImageCalled = false;
    }
    
    public void writePlotImage(OutputStream output) {
        writePlotImageCalled = true;
    }

}
