/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class SampleSet extends AbstractCaIntegrator2Object {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String fileName;
    private Set<Sample> samples = new HashSet<Sample>();
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the samples
     */
    public Set<Sample> getSamples() {
        return samples;
    }
    /**
     * @param samples the samples to set
     */
    @SuppressWarnings("unused") // required by Hibernate
    private void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @param sample the element to check.
     * @return true if contains the element.
     */
    public boolean contains(Sample sample) {
        return samples.contains(sample);
    }
}
