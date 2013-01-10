/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * 
 */
public class GeneExpressionPlotImpl implements GeneExpressionPlot {
    
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 500;
    
    private static final Logger LOGGER = Logger.getLogger(GeneExpressionPlotImpl.class);
    
    private JFreeChart plotChart;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;


    /**
     * {@inheritDoc}
     */
    public void writePlotImage(OutputStream out) {
        BufferedImage bufferedImage = getPlotChart().createBufferedImage(width, height);
        try {
            ChartUtilities.writeBufferedImageAsPNG(out, bufferedImage);
        } catch (IOException e) {
            LOGGER.warn("Couldn't write GeneExpressionPlot image", e);
        }
    }

    /**
     * @return the plotChart
     */
    public JFreeChart getPlotChart() {
        return plotChart;
    }

    /**
     * @param plotChart the plotChart to set
     */
    public void setPlotChart(JFreeChart plotChart) {
        this.plotChart = plotChart;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
