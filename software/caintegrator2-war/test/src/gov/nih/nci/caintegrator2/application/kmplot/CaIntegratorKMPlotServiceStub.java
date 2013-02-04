/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import gov.nih.nci.caintegrator.plots.kaplanmeier.ImageTypes;
import gov.nih.nci.caintegrator.plots.kaplanmeier.KMException;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;
import gov.nih.nci.caintegrator.plots.services.KMPlotService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

public class CaIntegratorKMPlotServiceStub implements KMPlotService {

    public boolean computeLogRankPValueBetweenCalled;
    public boolean createBufferedImageCalled;
    public boolean createPlotAsImageFileCalled;
    public boolean getChartCalled;
    public boolean writePlotToOutputStreamCalled;
    public KMCriteriaDTO kmCriteriaDTO;
    
    public void clear() {
        computeLogRankPValueBetweenCalled = false;
        createBufferedImageCalled = false;
        createPlotAsImageFileCalled = false;
        getChartCalled = false;
        writePlotToOutputStreamCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    public Double computeLogRankPValueBetween(KMSampleGroupCriteriaDTO group1, KMSampleGroupCriteriaDTO group2) {
        computeLogRankPValueBetweenCalled = true;
        return 1.1;
    }

    /**
     * {@inheritDoc}
     */
    public BufferedImage createBufferedImage(KMCriteriaDTO kmCrit) {
        kmCriteriaDTO = kmCrit;
        createBufferedImageCalled = true;
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void createPlotAsImageFile(File outFile, KMCriteriaDTO kmCrit, ImageTypes imgType) throws KMException {
        createPlotAsImageFileCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public Object getChart(KMCriteriaDTO kmCrit) {
        kmCriteriaDTO = kmCrit;
        getChartCalled = true;
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void writePlotToOutputStream(OutputStream out, KMCriteriaDTO kmCrit, ImageTypes imgType) throws KMException {
        writePlotToOutputStreamCalled = true;
    }

}
