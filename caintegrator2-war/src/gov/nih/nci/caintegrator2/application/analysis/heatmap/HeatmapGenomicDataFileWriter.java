/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import gov.nih.nci.caintegrator2.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator2.domain.genomic.GeneChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.heatmap.GeneLocationWrapper;
import gov.nih.nci.caintegrator2.heatmap.HeatMapArgs;
import gov.nih.nci.caintegrator2.heatmap.SegmentDataWrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Calls cbs2heatmap algorithm to write the data file for the input segment datas.
 */
public class HeatmapGenomicDataFileWriter {
    private static final Character TAB = '\t';
    private static final Character NEW_LINE = '\n';
    private static final String HEADER = "BinName" + TAB + "c1" + TAB + "c2";
    private static final Integer NUMBER_CHROMOSOMES = 24;
    private final Collection<SegmentData> segmentDatas;
    private final GeneLocationConfiguration geneLocationConfiguration;
    private final HeatmapParameters parameters; 
    private final CBSToHeatmapFactory cbsToHeatmapFactory;
    private final Map<String, Integer> chromosomeGeneSize = new HashMap<String, Integer>();
    
    
    /**
     * @param segmentDatas the segment data to run cbs to heatmap algorithm on.
     * @param geneLocationConfiguration to look up gene chromosome locations.
     * @param parameters for heatmap.
     * @param cbsToHeatmapFactory factory that creates CBSToHeatmap object, which runs cbsToHeatmap algorithm.
     */
    public HeatmapGenomicDataFileWriter(Collection<SegmentData> segmentDatas,
            GeneLocationConfiguration geneLocationConfiguration, HeatmapParameters parameters, 
            CBSToHeatmapFactory cbsToHeatmapFactory) {
        this.segmentDatas = segmentDatas;
        this.geneLocationConfiguration = geneLocationConfiguration;
        this.parameters = parameters;
        this.cbsToHeatmapFactory = cbsToHeatmapFactory;
        chromosomeGeneSize.clear();
    }
    
    /**
     * Writes genomic data files to the given path.
     * @param genomoicDataFilePath path for genomic data file.
     * @param layoutFilePath path for layout file(chr2genesize).
     * @throws IOException if unable to write files.
     */
    public void writeGenomicDataFiles(String genomoicDataFilePath, String layoutFilePath) throws IOException {
        writeGenomicDataFile(genomoicDataFilePath);
        writeLayoutFile(layoutFilePath);
    }
    
    private void writeGenomicDataFile(String genomicDataFilePath) throws IOException {
        HeatMapArgs hma = new HeatMapArgs();
        hma.setBigBinFile(parameters.getLargeBinsFile());
        hma.setSmallBinFile(parameters.getSmallBinsFile());
        hma.setGeneOutFile(genomicDataFilePath); // If we want to do gene based.
        hma.getGeneLocations().addAll(convertGeneLocations(geneLocationConfiguration.getGeneLocations()));
        hma.getSegmentDatas().addAll(convertSegmentDatas());
        cbsToHeatmapFactory.getCbsToHeatmap().runCBSToHeatmap(hma);
    }
    
    private void writeLayoutFile(String layoutFilePath) throws IOException {
        FileWriter writer = new FileWriter(layoutFilePath);
        writer.write(HEADER + NEW_LINE);
        Integer prevEndValue = writeChromosomeLine(writer, "1", 1);
        for (Integer curChromNum = 2; curChromNum <= NUMBER_CHROMOSOMES - 2; curChromNum++) {
            prevEndValue = writeChromosomeLine(writer, String.valueOf(curChromNum), prevEndValue + 1);
        }
        prevEndValue = writeChromosomeLine(writer, "X", prevEndValue + 1);
        prevEndValue = writeChromosomeLine(writer, "Y", prevEndValue + 1);
        writer.flush();
        writer.close();
    }
    
    private Integer writeChromosomeLine(FileWriter writer, String chromosome, Integer startNumber) throws IOException {
        Integer numberGenes = chromosomeGeneSize.get(chromosome);
        if (numberGenes == null) {
            numberGenes = 0;
        }
        Integer endNumber = startNumber + numberGenes - 1;
        writer.write("chr" + chromosome + TAB + startNumber + TAB + endNumber + NEW_LINE);
        return endNumber;
    }
    
    private List<GeneLocationWrapper> convertGeneLocations(Collection<GeneChromosomalLocation> geneLocations) {
        List<GeneLocationWrapper> geneLocationWrappers = new ArrayList<GeneLocationWrapper>();
        for (GeneChromosomalLocation geneLocation : geneLocations) {
            GeneLocationWrapper geneLocationWrapper = new GeneLocationWrapper();
            geneLocationWrapper.setChromosome(geneLocation.getLocation().getChromosome());
            geneLocationWrapper.setEndPosition(geneLocation.getLocation().getEndPosition());
            geneLocationWrapper.setStartPosition(geneLocation.getLocation().getStartPosition());
            geneLocationWrapper.setGeneSymbol(geneLocation.getGeneSymbol());
            geneLocationWrappers.add(geneLocationWrapper);
            increaseChromosomeSize(geneLocation.getLocation().getChromosome());
        }
        return geneLocationWrappers;
    }
    
    private List<SegmentDataWrapper> convertSegmentDatas() {
        List<SegmentDataWrapper> segmentDataWrappers = new ArrayList<SegmentDataWrapper>();
        for (SegmentData segmentData : segmentDatas) {
            SegmentDataWrapper segmentDataWrapper = new SegmentDataWrapper();
            segmentDataWrapper.setChromosome(segmentData.getLocation().getChromosome());
            segmentDataWrapper.setNumberOfMarkers(segmentData.getNumberOfMarkers());
            segmentDataWrapper.setSampleIdentifier(segmentData.getArrayData().getSample().getName());
            if (parameters.isUseCGHCall()) {
                if (segmentData.getCallsValue() != null) {
                    segmentDataWrapper.setSegmentValue(Float.valueOf(segmentData.getCallsValue()));
                }
            } else {
                segmentDataWrapper.setSegmentValue(segmentData.getSegmentValue());
            }
            segmentDataWrapper.setStartPosition(segmentData.getLocation().getStartPosition());
            segmentDataWrapper.setStopPosition(segmentData.getLocation().getEndPosition());
            segmentDataWrappers.add(segmentDataWrapper);
        }
        return segmentDataWrappers;
    }
    
    private void increaseChromosomeSize(String chromosome) {
        if (!chromosomeGeneSize.containsKey(chromosome)) {
            chromosomeGeneSize.put(chromosome, 0);
        }
        chromosomeGeneSize.put(chromosome, chromosomeGeneSize.get(chromosome) + 1);
    }
}
