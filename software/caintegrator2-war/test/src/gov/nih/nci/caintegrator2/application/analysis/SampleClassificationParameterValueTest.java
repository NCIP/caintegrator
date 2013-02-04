/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import org.junit.Test;

public class SampleClassificationParameterValueTest {

    @Test
    public void testClassify() {
        Sample sample1 = new Sample();
        sample1.setId(1L);
        Sample sample2 = new Sample();
        sample1.setId(2L);
        Sample sample3 = new Sample();
        sample1.setId(3L);
        SampleClassificationParameterValue parameterValue = new SampleClassificationParameterValue();
        parameterValue.classify(sample1, "class1");
        parameterValue.classify(sample2, "class2");
        parameterValue.classify(sample3, "class1");
        assertEquals(2, parameterValue.getClassifications().size());
        assertEquals("class1", parameterValue.getClassification(sample1).getName());
        assertEquals("class2", parameterValue.getClassification(sample2).getName());
        assertEquals("class1", parameterValue.getClassification(sample3).getName());
        assertEquals(0, parameterValue.getClassification(sample1).getIndex());
        assertEquals(1, parameterValue.getClassification(sample2).getIndex());
        assertEquals(0, parameterValue.getClassification(sample3).getIndex());
    }

}
