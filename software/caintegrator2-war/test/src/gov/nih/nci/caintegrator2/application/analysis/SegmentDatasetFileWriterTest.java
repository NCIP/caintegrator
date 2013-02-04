/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;


/**
 * 
 */
public class SegmentDatasetFileWriterTest {

    @Test
    public void testWriteAsSegFile() throws IOException {
        List<SegmentData> result = createTestResult();
        String testFilePath = System.getProperty("java.io.tmpdir") + File.separator + "segTest.seg";
        File segFile = SegmentDatasetFileWriter.writeAsSegFile(result, testFilePath, false);
        segFile.deleteOnExit();
        checkFile(segFile, result);
    }

    private void checkFile(File segFile, List<SegmentData> result) throws IOException {
        assertTrue(segFile.exists());
        CSVReader reader = new CSVReader(new FileReader(segFile), '\t');
        checkLine(reader.readNext(), "Track Name", "Chromosome", "Start Position", "End Position", "Segment Value");
        checkLine(reader.readNext(), "Sample 1", "1", "1", "5", "0.1");
        checkLine(reader.readNext(), "Sample 2", "2", "6", "9", "0.2");
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }

    private List<SegmentData> createTestResult() {
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

}
