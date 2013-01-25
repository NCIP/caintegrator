/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * Implementation based on JFreeChart.
 */
class KMPlotImpl implements KMPlot {
    
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 500;
    private static final int HEIGHT_INCREMENTER = 50;
    
    private static final Logger LOGGER = Logger.getLogger(KMPlotImpl.class);
    
    private JFreeChart plotChart;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    
    private final Map<SubjectGroup, Map<SubjectGroup, Double>> groupToGroupPValueMap = 
        new HashMap<SubjectGroup, Map<SubjectGroup, Double>>();
    
    private KMPlotConfiguration configuration;


    /**
     * {@inheritDoc}
     */
    public void writePlotImage(OutputStream out) {
        fixHeight();
        BufferedImage bufferedImage = getPlotChart().createBufferedImage(width, height);
        try {
            ChartUtilities.writeBufferedImageAsPNG(out, bufferedImage);
        } catch (IOException e) {
            LOGGER.warn("Couldn't write KMPlot image", e);
        }
    }
    
    private void fixHeight() {
        height = height
            + (int) (HEIGHT_INCREMENTER * (configuration.getGroups().size() / 3)); // For every 3 groups, increment
    }

    int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }
    
    JFreeChart getPlotChart() {
        return plotChart;
    }

    void setPlotChart(JFreeChart plotChart) {
        this.plotChart = plotChart;
    }
    
    void setPValue(SubjectGroup group1, SubjectGroup group2, Double pValue) {
        getGroupPValueMap(group1).put(group2, pValue);
    }

    private Map<SubjectGroup, Double> getGroupPValueMap(SubjectGroup group) {
        Map<SubjectGroup, Double> groupPValueMap = groupToGroupPValueMap.get(group); 
        if (groupPValueMap == null) {
            groupPValueMap = new HashMap<SubjectGroup, Double>();
            groupToGroupPValueMap.put(group, groupPValueMap);
        }
        return groupPValueMap;
    }
    
    /**
     * {@inheritDoc}
     */
    public Double getPValue(SubjectGroup group1, SubjectGroup group2) {
        if (group1.equals(group2)) {
            throw new IllegalArgumentException("Groups must be different");
        }
        return getGroupPValueMap(group1).get(group2);
    }

    /**
     * {@inheritDoc}
     */
    public KMPlotConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(KMPlotConfiguration configuration) {
        this.configuration = configuration;
    }



}
