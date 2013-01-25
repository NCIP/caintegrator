/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;

import java.io.OutputStream;

/**
 * 
 */
public class KMPlotStub implements KMPlot {

    public boolean getPValueCalled;
    public boolean writePlotImageCalled;

    
    public void clear() {
        getPValueCalled = false;
        writePlotImageCalled = false;
    }
    
    public Double getPValue(SubjectGroup group1, SubjectGroup group2) {
        getPValueCalled = true;
        return null;
    }

    
    public void writePlotImage(OutputStream out) {
        writePlotImageCalled = true;
    }


    public KMPlotConfiguration getConfiguration() {

        return null;
    }
}
