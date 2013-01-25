/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * Implementation of the GeneExpressionPlotService interface.
 */
public class GeneExpressionPlotServiceImpl implements GeneExpressionPlotService {
    private static final String DOMAIN_AXIS_LABEL = "Groups";
    private static final String RANGE_AXIS_NOTE_TWO_COLOR = " (ratio of sample to common reference)";
    private static final double LOWER_MARGIN = .02;
    private static final double CATEGORY_MARGIN = .20; 
    private static final double UPPER_MARGIN = .02;
    private static final double ITEM_MARGIN = .01;
    private static final int WIDTH_MULTIPLIER = 33;
    private static final int MINIMUM_WIDTH = 300;
    private LegendItemCollection legendItems;
    private String rangeAxisNote = "";
    
    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup generatePlots(GeneExpressionPlotConfiguration configuration) {
        GeneExpressionPlotGroup plotGroup = new GeneExpressionPlotGroup();
        PlotGroupDatasets dataSets = PlotGroupDatasetsFactory.createDatasets(configuration);
        plotGroup.setTwoChannelType(configuration.isTwoChannelType());
        rangeAxisNote = (plotGroup.isTwoChannelType()) ? RANGE_AXIS_NOTE_TWO_COLOR : "";
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEAN, 
                createMeanTypePlot(dataSets.getMeanDataset(), configuration.getGenomicValueResultsType()));
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEDIAN, 
                createMedianTypePlot(dataSets.getMedianDataset(), configuration.getGenomicValueResultsType()));
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.LOG2_INTENSITY, 
                createLog2TypePlot(dataSets.getLog2Dataset(), configuration.getGenomicValueResultsType()));
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.BOX_WHISKER_LOG2_INTENSITY, 
                createBoxWhiskerTypePlot(dataSets.getBwDataset(), configuration.getGenomicValueResultsType()));
        addLegendItemsToPlot(plotGroup);
        addPlotGroupSubjectCounts(configuration, plotGroup);
        plotGroup.getGenesNotFound().addAll(configuration.getGenesNotFound());
        plotGroup.getSubjectsNotFound().addAll(configuration.getSubjectsNotFound());
        return plotGroup;
    }

    private GeneExpressionPlot createMeanTypePlot(DefaultCategoryDataset meanDataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(meanDataset, "Mean " + genomicValueResultsType.getValue() + rangeAxisNote);
        legendItems = chart.getLegend().getSources()[0].getLegendItems();
        cusomtizeBarChart(chart);
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(meanDataset));
        return plot;
    }
    
    private GeneExpressionPlot createMedianTypePlot(DefaultCategoryDataset medianDataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(medianDataset, "Median " + genomicValueResultsType.getValue() + rangeAxisNote);
        cusomtizeBarChart(chart);
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(medianDataset));
        return plot;
    }

    private GeneExpressionPlot createLog2TypePlot(DefaultStatisticalCategoryDataset log2Dataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(log2Dataset, "Log2 " + genomicValueResultsType.getValue() + rangeAxisNote);
        chart.getCategoryPlot().setRenderer(cusomtizeStatisticalBarChart(chart));
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(log2Dataset));
        return plot;
    }

    private GeneExpressionPlot createBoxWhiskerTypePlot(DefaultBoxAndWhiskerCategoryDataset bwDataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(bwDataset, "Log2 " + genomicValueResultsType.getValue() + rangeAxisNote);
        chart.getCategoryPlot().setRenderer(cusomtizeBoxWhiskerChart(chart));
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(bwDataset));
        return plot;
    }

    private JFreeChart createChart(CategoryDataset dataset, String rangeAxisLabel) {
        JFreeChart chart = ChartFactory.createBarChart(
                null, 
                DOMAIN_AXIS_LABEL, 
                rangeAxisLabel, 
                dataset,
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // URLs
                );
        return chart;
    }

    private void cusomtizeBarChart(JFreeChart chart) {
        customizeAxisMargin(chart);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(ITEM_MARGIN);
        renderer.setDrawBarOutline(true);
        chart.removeLegend();
    }

    
    private StatisticalBarRenderer cusomtizeStatisticalBarChart(JFreeChart chart) {
        customizeAxisMargin(chart);
        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
        renderer.setItemMargin(ITEM_MARGIN);
        renderer.setDrawBarOutline(true);
        chart.removeLegend();
        return renderer;
    }
    
    private BoxAndWhiskerCoinPlotRenderer cusomtizeBoxWhiskerChart(JFreeChart chart) {
        customizeAxisMargin(chart);
        BoxAndWhiskerCoinPlotRenderer renderer = new BoxAndWhiskerCoinPlotRenderer();
        renderer.setDisplayAllOutliers(true);
        renderer.setDisplayMean(false);
        renderer.setFillBox(false);
        renderer.setItemMargin(ITEM_MARGIN);
        chart.removeLegend();
        return renderer;
    }
    
    private void customizeAxisMargin(JFreeChart chart) {
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setLowerMargin(LOWER_MARGIN);
        axis.setCategoryMargin(CATEGORY_MARGIN);
        axis.setUpperMargin(UPPER_MARGIN);
    }


    private void addLegendItemsToPlot(GeneExpressionPlotGroup plotGroup) {
        if (legendItems != null) {
            for (int x = 0; x < legendItems.getItemCount(); x++) {
                plotGroup.getLegendItems().add(new LegendItemWrapper(legendItems.get(x)));
            }
        }
    }
    
    private void addPlotGroupSubjectCounts(GeneExpressionPlotConfiguration configuration,
            GeneExpressionPlotGroup plotGroup) {
        for (PlotSampleGroup sampleGroup : configuration.getPlotSampleGroups()) {
            plotGroup.getGroupNameToNumberSubjectsMap().put(sampleGroup.getName(), sampleGroup.getNumberSubjects());
        }
    }
    
    private int retrievePlotWidth(CategoryDataset dataset) {
        int width = dataset.getColumnCount() * dataset.getRowCount() * WIDTH_MULTIPLIER;
        return width > MINIMUM_WIDTH ? width : MINIMUM_WIDTH;
    }
}
