/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample classification.
 */
public class SampleClassificationParameterValue extends AbstractParameterValue {

    private static final long serialVersionUID = 1L;
    private final List<SampleClassification> classifications = new ArrayList<SampleClassification>();
    private final List<Sample> classifiedSamples = new ArrayList<Sample>();
    private final Map<String, SampleClassification> classificationNameMap =
        new HashMap<String, SampleClassification>();
    private final Map<Sample, SampleClassification> sampleClassificationMap = 
        new HashMap<Sample, SampleClassification>();
    @SuppressWarnings("PMD.ImmutableField") // value is altered in inner class
    private int nextIndex = 0;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueAsString() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueFromString(String stringValue) {
        throw new IllegalStateException("Can't set value from String");
    }
    
    /**
     * Classify the given sample.
     * 
     * @param sample the sample to classify
     * @param classificationName the class name
     */
    public void classify(Sample sample, String classificationName) {
        classifiedSamples.add(sample);
        SampleClassification classification = getClassification(classificationName);
        sampleClassificationMap.put(sample, classification);
    }
    
    /**
     * @param classificationName
     * @return
     */
    private SampleClassification getClassification(String classificationName) {
        SampleClassification classification = classificationNameMap.get(classificationName);
        if (classification == null) {
            classification = new SampleClassification(classificationName);
            classifications.add(classification);
            classificationNameMap.put(classificationName, classification);
        }
        return classification;
    }

    List<Sample> getClassifiedSamples() {
        return classifiedSamples;
    }

    List<SampleClassification> getClassifications() {
        return classifications;
    }
    
    SampleClassification getClassification(Sample sample) {
        return sampleClassificationMap.get(sample);
    }

    /**
     * Clear all existing classifications, in case the parameter value is being
     * reused.
     */
    public void clear() {
        classifiedSamples.clear();
        classificationNameMap.clear();
        classifications.clear();
        sampleClassificationMap.clear();
        nextIndex = 0;
    }

    /**
     * A single category in the classifications.
     */
    final class SampleClassification {
        
        private final String name;
        private final int index;
        
        SampleClassification(String name) {
            this.name = name;
            index = nextIndex++;
        }

        String getName() {
            return name;
        }

        int getIndex() {
            return index;
        }
        
    }

}
