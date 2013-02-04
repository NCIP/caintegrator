/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;

/**
 * 
 */
public class DisplayableCopyNumberSampleBasedRow extends AbstractDisplayableCopyNumberRow
implements Comparable<DisplayableCopyNumberSampleBasedRow> {
    
    private String subject;
    private String sample;
    private GenomicDataResultValue value;
    /**
     * @return the value
     */
    public GenomicDataResultValue getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(GenomicDataResultValue value) {
        this.value = value;
    }
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the sample
     */
    public String getSample() {
        return sample;
    }
    /**
     * @param sample the sample to set
     */
    public void setSample(String sample) {
        this.sample = sample;
    }
    /**
     * Compare based on the Sample.
     * {@inheritDoc}
     */
    public int compareTo(DisplayableCopyNumberSampleBasedRow o) {
        return getSample().compareTo(o.getSample());
    }
}
