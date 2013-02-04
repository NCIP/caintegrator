/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class ClassificationsToClsConverterTest {

    @Test
    public void testWriteAsGct() throws IOException {
        SampleClassificationParameterValue parameterValue = createTestParameterValue();
        String testFilePath = System.getProperty("java.io.tmpdir") + File.separator + "clsTest.cls";
        File clsFile = new File(testFilePath);
        ClassificationsToClsConverter.writeAsCls(parameterValue, testFilePath);
        clsFile.deleteOnExit();
        checkFile(clsFile);
    }

    private void checkFile(File clsFile) throws IOException {
        assertTrue(clsFile.exists());
        CSVReader reader = new CSVReader(new FileReader(clsFile), ' ');
        checkLine(reader.readNext(), "3", "2", "1");
        checkLine(reader.readNext(), "#", "class1", "class2");
        checkLine(reader.readNext(), "0", "1", "0");
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }

    private SampleClassificationParameterValue createTestParameterValue() {
        SampleClassificationParameterValue parameterValue = new SampleClassificationParameterValue();
        Sample sample1 = new Sample();
        sample1.setId(1L);
        Sample sample2 = new Sample();
        sample1.setId(2L);
        Sample sample3 = new Sample();
        sample1.setId(3L);
        parameterValue.classify(sample1, "class1");
        parameterValue.classify(sample2, "class2");
        parameterValue.classify(sample3, "class1");
        return parameterValue;
    }

}
