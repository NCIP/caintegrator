package gov.nih.nci.caintegrator.plots.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

import org.apache.log4j.Logger;

import gov.nih.nci.caintegrator.plots.kaplanmeier.DefaultKMAlgorithmImpl;
import gov.nih.nci.caintegrator.plots.kaplanmeier.ImageTypes;
import gov.nih.nci.caintegrator.plots.kaplanmeier.JFreeChartIKMPlottermpl;
import gov.nih.nci.caintegrator.plots.kaplanmeier.KMAlgorithm;
import gov.nih.nci.caintegrator.plots.kaplanmeier.KMException;
import gov.nih.nci.caintegrator.plots.kaplanmeier.KMPlotter;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.model.XYCoordinate;
import gov.nih.nci.caintegrator.plots.kaplanmeier.model.GroupCoordinates;

/**
 @author caIntegrator Team
 */

public class KMPlotServiceImpl implements KMPlotService {
    private static Logger logger = Logger.getLogger(KMPlotServiceImpl.class);
    private KMPlotter plotter = new JFreeChartIKMPlottermpl();
    private KMAlgorithm kmAlgorithm = new DefaultKMAlgorithmImpl();
    public final static Color DEFAULT_COLOR = Color.CYAN;

    public KMPlotter getPlotter() {
        return plotter;
    }

    public void setPlotter(KMPlotter plotter) {
        this.plotter = plotter;
    }

    public KMAlgorithm getKmAlgorithm() {
        return kmAlgorithm;
    }

    public void setKmAlgorithm(KMAlgorithm kmAlgorithm) {
        this.kmAlgorithm = kmAlgorithm;
    }

    public Double computeLogRankPValueBetween(KMSampleGroupCriteriaDTO group1, KMSampleGroupCriteriaDTO group2) {
        Collection<KMSampleDTO> sampleGroup1 = group1.getKmSampleDTOCollection();
        Collection<KMSampleDTO> sampleGroup2 = group2.getKmSampleDTOCollection();
        return kmAlgorithm.getLogRankPValue(sampleGroup1, sampleGroup2);
    }

    public void writePlotToOutputStream(OutputStream out, KMCriteriaDTO kmCrit, ImageTypes imgType)
    throws KMException {
        java.awt.image.BufferedImage image = buildImage(kmCrit);
        plotter.writeBufferedImage(out, image, imgType);
    }

    public void createPlotAsImageFile(File outFile, KMCriteriaDTO kmCrit, ImageTypes imgType)
    throws KMException {
        assert kmCrit != null;
        if ( imgType == null) imgType = ImageTypes.PNG;  // default to png format
        try {
            FileOutputStream fileOutStream = new FileOutputStream(outFile);
            writePlotToOutputStream(fileOutStream, kmCrit, imgType);
            fileOutStream.close();
        } catch (IOException e) {
            logger.debug(e);
            throw new KMException(e);
        }
    }

    public BufferedImage createBufferedImage(KMCriteriaDTO kmCrit) {
        return buildImage(kmCrit);
    }

    private java.awt.image.BufferedImage  buildImage(KMCriteriaDTO kmCrit ) {
        Collection<KMSampleGroupCriteriaDTO> sampleGroups = kmCrit.getSampleGroupCriteriaDTOCollection();
        String plotName = kmCrit.getPlotTitle();
        int count=0;
        Collection<GroupCoordinates> groupsToBePlotted = new ArrayList<GroupCoordinates>();
        for (Iterator<KMSampleGroupCriteriaDTO> iterator = sampleGroups.iterator(); iterator.hasNext(); ++count) {
            KMSampleGroupCriteriaDTO groupCrit =  iterator.next();
            assert(groupCrit != null);
            String groupName = (groupCrit.getSampleGroupName() != null) ? groupCrit.getSampleGroupName():"GROUP "+count;
            Color color = (groupCrit.getColor() != null) ? groupCrit.getColor() : DEFAULT_COLOR;
            Collection<KMSampleDTO>  samples = groupCrit.getKmSampleDTOCollection();
            Collection<XYCoordinate> dataPoints = kmAlgorithm.getPlottingCoordinates(samples );
            GroupCoordinates groupToBePlotted = new GroupCoordinates(dataPoints, groupName, color, samples.size());
            groupsToBePlotted.add(groupToBePlotted);
        }

        java.awt.image.BufferedImage image = plotter.createImage(groupsToBePlotted, plotName, kmCrit.getDurationAxisLabel(),
                                                            kmCrit.getProbablityAxisLabel());
        return image;
    }
    
    public Object getChart(KMCriteriaDTO kmCrit ) {
        Collection<KMSampleGroupCriteriaDTO> sampleGroups = kmCrit.getSampleGroupCriteriaDTOCollection();
        String plotName = kmCrit.getPlotTitle();
        int count=0;
        Collection<GroupCoordinates> groupsToBePlotted = new ArrayList<GroupCoordinates>();
        for (Iterator<KMSampleGroupCriteriaDTO> iterator = sampleGroups.iterator(); iterator.hasNext(); ++count) {
            KMSampleGroupCriteriaDTO groupCrit =  iterator.next();
            assert(groupCrit != null);
            String groupName = (groupCrit.getSampleGroupName() != null) ? groupCrit.getSampleGroupName():"GROUP "+count;
            Color color = (groupCrit.getColor() != null) ? groupCrit.getColor() : DEFAULT_COLOR;
            Collection<KMSampleDTO>  samples = groupCrit.getKmSampleDTOCollection();
            Collection<XYCoordinate> dataPoints = kmAlgorithm.getPlottingCoordinates(samples );
            GroupCoordinates groupToBePlotted = new GroupCoordinates(dataPoints, groupName, color, samples.size());
            groupsToBePlotted.add(groupToBePlotted);
        }

        Object image = plotter.createImageOfKnownType(groupsToBePlotted, plotName, kmCrit.getDurationAxisLabel(),
                                                            kmCrit.getProbablityAxisLabel());
        return image;
    }
}
                                                  