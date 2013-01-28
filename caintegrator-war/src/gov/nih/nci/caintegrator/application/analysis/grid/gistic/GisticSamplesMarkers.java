/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid.gistic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.genepattern.gistic.Marker;

import edu.wustl.icr.asrv1.dnacopy.ChromosomalSegmentWithMeanAndMarker;
import edu.wustl.icr.asrv1.segment.ChromosomalSegment;
import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSetSegments;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;

/**
 * Wraps the parameters which get sent over to the Gistic GenePattern grid service.
 */
public class GisticSamplesMarkers {
    private final List<Marker> markers = new ArrayList<Marker>();
    private final List<SampleWithChromosomalSegmentSet> samples = new ArrayList<SampleWithChromosomalSegmentSet>();
    private final Set<DnaAnalysisReporter> usedReporters = new HashSet<DnaAnalysisReporter>();
    private final Set<Sample> usedSamples = new HashSet<Sample>();
    
    /**
     * @return the markers
     */
    public Marker[] getMarkers() {
        return markers.toArray(new Marker[markers.size()]);
    }
    /**
     * @return the samples
     */
    public SampleWithChromosomalSegmentSet[] getSamples() {
        return samples.toArray(new SampleWithChromosomalSegmentSet[samples.size()]);
    }

    /**
     * Adds the Cai2 ReporterList to the gistic markers. 
     * @param reporterList representing a list of Cai2 reporters.
     */
    public void addReporterListToGisticMarkers(ReporterList reporterList) {
        for (AbstractReporter reporter : reporterList.getReporters()) {
            if (reporter instanceof DnaAnalysisReporter) {
                addMarkerFromReporter(reporter);
            }
        }
    }

    private void addMarkerFromReporter(AbstractReporter reporter) {
        DnaAnalysisReporter dnaReporter = (DnaAnalysisReporter) reporter;
        if (!usedReporters.contains(dnaReporter) && dnaReporter.hasValidLocation()) {
            usedReporters.add(dnaReporter);
            Marker marker = new Marker();
            marker.setChromosome(dnaReporter.getChromosome());
            marker.setPosition(dnaReporter.getPosition());
            marker.setName(dnaReporter.getName());
            markers.add(marker);
        }
    }

    /**
     * Adds Cai2 segmentation data to the Gistic samples object.
     * @param segmentDatas the cai2 segment data to add to gistic chromosomal sample.
     * @param sample the cai2 sample to add to gistic chromosomal sample.
     */
    public void addSegmentDataToGisticSamples(Set<SegmentData> segmentDatas, Sample sample) {
        SampleWithChromosomalSegmentSet chromosomalSample = new SampleWithChromosomalSegmentSet();
        chromosomalSample.setName(sample.getName());
        chromosomalSample.setExternalSampleId(String.valueOf(sample.getId()));
        
        SampleWithChromosomalSegmentSetSegments chromosomalSegments = new SampleWithChromosomalSegmentSetSegments();
        
        chromosomalSegments.setChromosomalSegment(new ChromosomalSegment[segmentDatas.size()]);
        int i = 0;
        for (SegmentData segmentData : segmentDatas) {
            ChromosomalSegmentWithMeanAndMarker chromosomalSegment = new ChromosomalSegmentWithMeanAndMarker();
            chromosomalSegment.setChromosomeNumber(segmentData.getLocation().getChromosome());
            chromosomalSegment.setSegmentStart(BigInteger.valueOf(segmentData.getLocation().getStartPosition()));
            chromosomalSegment.setSegmentEnd(BigInteger.valueOf(segmentData.getLocation().getEndPosition()));
            chromosomalSegment.setSegmentMean(segmentData.getSegmentValue());
            chromosomalSegment.setNumberMarkers(BigInteger.valueOf(segmentData.getNumberOfMarkers()));
            chromosomalSegments.setChromosomalSegment(i, chromosomalSegment);
            i++;
        }
        chromosomalSample.setSegments(chromosomalSegments);
        samples.add(chromosomalSample);
        usedSamples.add(sample);
    }
    /**
     * @return the usedSamples
     */
    public Set<Sample> getUsedSamples() {
        return usedSamples;
    }
    
    
}
