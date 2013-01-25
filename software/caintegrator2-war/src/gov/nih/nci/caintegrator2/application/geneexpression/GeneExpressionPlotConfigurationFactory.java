/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import gov.nih.nci.caintegrator2.application.query.GenomicDataResultRowComparator;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Factory for generating GeneExpressionPlotConfiguration's from GenomicDataQueryResults.
 */
public final class GeneExpressionPlotConfigurationFactory {

    private GeneExpressionPlotConfigurationFactory() { }
    
    /**
     * Creates a configuration object from genomic query results.
     * @param genomicResults - results used to create plot configuration.
     * @param genomicValueResultsType - to determine how to display the graph.
     * @return - plot configuration from given results.
     */
    public static GeneExpressionPlotConfiguration createPlotConfiguration(
                    List<GenomicDataQueryResult> genomicResults, GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotConfiguration configuration = new GeneExpressionPlotConfiguration();
        configuration.setGenomicValueResultsType(genomicValueResultsType);
        addSampleGroups(genomicResults, configuration);
        return configuration;
    }


    private static void addSampleGroups(List<GenomicDataQueryResult> genomicResults,
            GeneExpressionPlotConfiguration configuration) {
        PlotSampleGroup allSamplesGroup = new PlotSampleGroup();
        if (genomicResults.size() > 1) {
            configuration.getPlotSampleGroups().add(allSamplesGroup);    
        }
        int numberSubjectsTotal = 0;
        Map<String, PlotReporterGroup> reporterNameToGroupMap = new HashMap<String, PlotReporterGroup>();
        for (GenomicDataQueryResult genomicResult : genomicResults) {
            PlotSampleGroup sampleGroup = new PlotSampleGroup();
            sampleGroup.setName(genomicResult.getQuery().getName());
            configuration.getPlotSampleGroups().add(sampleGroup);
            int numberSubjects = numberSubjectsInGenomicResult(genomicResult);
            if (!sampleGroup.isControlSampleGroup()) { // Don't count control group for total.
                numberSubjectsTotal += numberSubjects;
            }
            sampleGroup.setNumberSubjects(numberSubjects);
            addReporterGroups(reporterNameToGroupMap, genomicResult, sampleGroup, configuration);
        }
        configureAllSamplesGroup(configuration, allSamplesGroup, reporterNameToGroupMap, numberSubjectsTotal);
    }

    private static void addReporterGroups(Map<String, PlotReporterGroup> reporterNameToGroupMap, 
            GenomicDataQueryResult genomicResult, PlotSampleGroup sampleGroup, 
            GeneExpressionPlotConfiguration configuration) {
        Collections.sort(genomicResult.getRowCollection(), new GenomicDataResultRowComparator());
        for (GenomicDataResultRow row : genomicResult.getRowCollection()) {
            addReporterGroups(reporterNameToGroupMap, sampleGroup, configuration, row);
        }
    }

    private static void addReporterGroups(Map<String, PlotReporterGroup> reporterNameToGroupMap,
            PlotSampleGroup sampleGroup, GeneExpressionPlotConfiguration configuration, GenomicDataResultRow row) {
        if (!(row.getReporter() instanceof GeneExpressionReporter)) {
            throw new NonGeneExpressionReporterException("Row reporter is not a GeneExpressionReporter.");
        }
        AbstractReporter rowReporter = row.getReporter();
        for (Gene gene : rowReporter.getGenes()) {
            configuration.getGeneNames().add(gene.getSymbol());
        }
        String name = retrieveRowName(row, rowReporter);
        if (!reporterNameToGroupMap.containsKey(name)) {
            configureNewReporterGroup(reporterNameToGroupMap, name);
        } 
        PlotReporterGroup reporterGroup = copyReporterGroup(reporterNameToGroupMap.get(name));
        sampleGroup.getReporterGroups().add(reporterGroup);
        addGeneExpressionValuesToGroup(reporterNameToGroupMap, sampleGroup, row, name, reporterGroup);
    }

    private static void addGeneExpressionValuesToGroup(Map<String, PlotReporterGroup> reporterNameToGroupMap,
            PlotSampleGroup sampleGroup, GenomicDataResultRow row, String name, PlotReporterGroup reporterGroup) {
        for (GenomicDataResultValue value : row.getValues()) {
            reporterGroup.getGeneExpressionValues().add(Double.valueOf(value.getValue()));
        }
        if (!sampleGroup.isControlSampleGroup()) { // Don't count control group for total.
            reporterNameToGroupMap.get(name).getGeneExpressionValues().addAll(reporterGroup.getGeneExpressionValues());
        }
    }
    
    private static int numberSubjectsInGenomicResult(GenomicDataQueryResult genomicResult) {
        Set <Long> uniqueSubjects = new HashSet<Long>();
        for (GenomicDataResultColumn column : genomicResult.getColumnCollection()) {
            uniqueSubjects.add(column.getSampleAcquisition().getAssignment().getId());
        }
        return uniqueSubjects.size();
    }

    private static String retrieveRowName(GenomicDataResultRow row, AbstractReporter rowReporter) {
        StringBuffer name = new StringBuffer();
        for (Gene gene : rowReporter.getGenes()) {
            if (name.length() > 0) {
                name.append(' ');
            }
            name.append(gene.getSymbol());
        }
        if (!name.toString().equals(row.getReporter().getName())) {
            name.append(" - ");
            name.append(row.getReporter().getName());
        }
        return name.toString();
    }
    
    private static void configureAllSamplesGroup(GeneExpressionPlotConfiguration configuration, 
            PlotSampleGroup allSamplesGroup, Map<String, PlotReporterGroup> reporterNameToGroupMap, 
            int numberSubjects) {
        if (configuration.getPlotSampleGroups().size() > 1) {
            allSamplesGroup.setName("All");
            allSamplesGroup.setNumberSubjects(numberSubjects);
            for (PlotReporterGroup reporterGroup : retrievePlotReporterGroups(configuration)) {
                allSamplesGroup.getReporterGroups().add(reporterNameToGroupMap.get(reporterGroup.getName()));
            }
        }
    }

    private static List<PlotReporterGroup> retrievePlotReporterGroups(GeneExpressionPlotConfiguration configuration) {
        for (PlotSampleGroup plotSampleGroup : configuration.getPlotSampleGroups()) {
            if (!plotSampleGroup.getReporterGroups().isEmpty()) {
                return plotSampleGroup.getReporterGroups();
            }
        }
        return new ArrayList<PlotReporterGroup>();
    }

    private static void configureNewReporterGroup(Map<String, PlotReporterGroup> reporterNameToGroupMap, String name) {
        PlotReporterGroup group = new PlotReporterGroup();
        reporterNameToGroupMap.put(name, group);
        group.setName(name);
    }
    
    private static PlotReporterGroup copyReporterGroup(PlotReporterGroup group) {
        PlotReporterGroup newGroup = new PlotReporterGroup();
        newGroup.setName(group.getName());
        return newGroup;
    }
    

}
