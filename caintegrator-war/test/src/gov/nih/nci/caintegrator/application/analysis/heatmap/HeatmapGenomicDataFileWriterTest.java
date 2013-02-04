/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.heatmap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapGenomicDataFileWriter;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator.domain.genomic.GeneChromosomalLocation;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.heatmap.CBSToHeatmap;
import gov.nih.nci.caintegrator.heatmap.HeatMapArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 *
 */
public class HeatmapGenomicDataFileWriterTest {

    @Test
    public void testWriteGenomicDataFiles() throws IOException {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "heatmapTmp");
        tempDirectory.mkdir();
        List<SegmentData> segmentDatas = createSegmentDatas();
        CBSToHeatmap cbsToHeatmap = mock(CBSToHeatmap.class);
        CBSToHeatmapFactory cbsToHeatmapFactory = mock(CBSToHeatmapFactory.class);
        when(cbsToHeatmapFactory.getCbsToHeatmap()).thenReturn(cbsToHeatmap);
        HeatmapGenomicDataFileWriter fileWriter =
            new HeatmapGenomicDataFileWriter(segmentDatas, createGeneLocationConfiguration(segmentDatas),
                    new HeatmapParameters(), cbsToHeatmapFactory);
        File genomicDataFile = new File(tempDirectory, "genomicDataFile.txt");
        File layoutFile = new File(tempDirectory, "layoutFile.txt");
        fileWriter.writeGenomicDataFiles(genomicDataFile.getAbsolutePath(), layoutFile.getAbsolutePath());
        verify(cbsToHeatmap, atLeastOnce()).runCBSToHeatmap(any(HeatMapArgs.class));
        assertTrue(layoutFile.exists());
        FileUtils.deleteDirectory(tempDirectory);
    }

    private List<SegmentData> createSegmentDatas() {
        List<SegmentData> dataSet = new ArrayList<SegmentData>();
        SegmentData result = new SegmentData();
        result.setArrayData(new ArrayData());
        result.getArrayData().setSample(new Sample());
        result.getArrayData().getSample().setName("Sample 1");
        result.setLocation(new ChromosomalLocation());
        result.getLocation().setChromosome("1");
        result.getLocation().setStartPosition(Integer.valueOf(1));
        result.getLocation().setEndPosition(Integer.valueOf(5));
        result.setSegmentValue(Float.valueOf("0.1"));
        dataSet.add(result);

        result = new SegmentData();
        result.setArrayData(new ArrayData());
        result.getArrayData().setSample(new Sample());
        result.getArrayData().getSample().setName("Sample 2");
        result.setLocation(new ChromosomalLocation());
        result.getLocation().setChromosome("2");
        result.getLocation().setStartPosition(Integer.valueOf(6));
        result.getLocation().setEndPosition(Integer.valueOf(9));
        result.setSegmentValue(Float.valueOf("0.2"));
        dataSet.add(result);

        return dataSet;
    }

    private GeneLocationConfiguration createGeneLocationConfiguration(List<SegmentData> segmentDatas) {
        GeneLocationConfiguration configuration = new GeneLocationConfiguration();
        for (SegmentData segmentData : segmentDatas) {
            ChromosomalLocation location = segmentData.getLocation();
            GeneChromosomalLocation geneLocation = new GeneChromosomalLocation();
            geneLocation.setLocation(location);
            configuration.getGeneLocations().add(geneLocation);
        }
        return configuration;
    }

}
