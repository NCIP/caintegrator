/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory utility class for generating a <code>PlotGroupDatasets</code> from a 
 * <code>GeneExpressionPlotConfiguration</code>.
 */
public final class PlotGroupDatasetsFactory {
    
    private PlotGroupDatasetsFactory() { }
    
    /**
     * Creates the datasets from a plot configuration.
     * @param configuration for the plot data sets.
     * @return object containing the JFreeChart datasets.
     */
    public static PlotGroupDatasets createDatasets(GeneExpressionPlotConfiguration configuration) {
        PlotGroupDatasets datasets = new PlotGroupDatasets();
        for (PlotSampleGroup sampleGroup : configuration.getPlotSampleGroups()) {
            String columnKey = sampleGroup.getName();
            for (PlotReporterGroup reporterGroup : sampleGroup.getReporterGroups()) {
                List <Double> rowValues = new ArrayList<Double>();
                List <Double> rowLog2Values = new ArrayList<Double>();
                Double totalValue = 0.0;
                Double totalLog2Value = 0.0;
                String rowKey = reporterGroup.getName();
                for (Double value : reporterGroup.getGeneExpressionValues()) {
                    Double log2Value = log2Intensity(value);
                    rowValues.add(value);
                    rowLog2Values.add(log2Value);
                    totalValue += value;
                    totalLog2Value += log2Value;
                }
                if (!rowValues.isEmpty()) {
                    updateDatasets(datasets, columnKey, rowKey, rowValues, totalValue);
                    updateLog2Datasets(datasets, columnKey, rowKey, rowLog2Values, totalLog2Value);
                }
            }
        }
        return datasets;
    }
    
    private static void updateDatasets(PlotGroupDatasets datasets, String columnKey, String rowKey, 
                                        List<Double> rowValues, Double totalValue) {
        datasets.getMeanDataset().addValue(mean(totalValue, rowValues.size()), rowKey, columnKey);
        datasets.getMedianDataset().addValue(median(rowValues), rowKey, columnKey);
    }
    
    private static void updateLog2Datasets(PlotGroupDatasets datasets, String columnKey, String rowKey, 
                                       List<Double> rowLog2Values, Double totalLog2Value) {
        Double mean = mean(totalLog2Value, rowLog2Values.size());
        datasets.getLog2Dataset().add(mean, standardDeviation(rowLog2Values, mean), rowKey, columnKey);
        datasets.getBwDataset().add(rowLog2Values, rowKey, columnKey);   
    }
    
    private static double log2Intensity(Double value) {
        return Math.log(Math.abs(value) / Math.log(2));
    }

    private static Double standardDeviation(List<Double> values, Double mean) {
        Double totalSquaredDeviations = 0.0;
        for (Double value : values) {
            totalSquaredDeviations += Math.pow(value - mean, 2);
        }
        return Math.sqrt(mean(totalSquaredDeviations, values.size()));
    }

    private static Double mean(Double totalValue, int numValues) {
        return numValues != 0 ? totalValue / numValues : 0;
    }

    private static Double median(List<Double> list) {
        Collections.sort(list);
        int middle = list.size() / 2;
        if (list.size() % 2 == 1) {
            return list.get(middle);
        } else {
            return (list.get(middle - 1) + list.get(middle)) / 2.0;
        }
    }
    
    
}
