/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.plots.services;

import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.ImageTypes;
import gov.nih.nci.caintegrator.plots.kaplanmeier.KMException;

import java.io.File;


/**
 * @author caIntegrator Team
 */

public interface KMPlotService {

    /*  Currently JPEG & PNG formats are supported.  */
    public void createPlotAsImageFile(File outFile, KMCriteriaDTO kmCrit, ImageTypes imgType)
    throws KMException;

    public void writePlotToOutputStream(java.io.OutputStream out, KMCriteriaDTO kmCrit, ImageTypes imgType)
    throws KMException;

    public java.awt.image.BufferedImage createBufferedImage(KMCriteriaDTO kmCrit);

    /*  compute Log-rank p-value (which indicates the significance of the difference in survival between any two
        groups of samples) */
     public Double computeLogRankPValueBetween(KMSampleGroupCriteriaDTO group1, KMSampleGroupCriteriaDTO group2);
     
     public Object getChart(KMCriteriaDTO kmCrit);
}
