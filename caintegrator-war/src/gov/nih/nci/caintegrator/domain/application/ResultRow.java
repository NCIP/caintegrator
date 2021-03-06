/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.io.Serializable;
import java.util.Collection;

/**
 * Representation of a query result row.
 */
public class ResultRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer rowIndex;
    private StudySubjectAssignment subjectAssignment;
    private SampleAcquisition sampleAcquisition;
    private Collection<ResultValue> valueCollection;
    private ImageSeries imageSeries;

    /**
     * @return the rowIndex
     */
    public Integer getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * @return the subjectAssignment
     */
    public StudySubjectAssignment getSubjectAssignment() {
        return subjectAssignment;
    }

    /**
     * @param subjectAssignment the subjectAssignment to set
     */
    public void setSubjectAssignment(StudySubjectAssignment subjectAssignment) {
        this.subjectAssignment = subjectAssignment;
    }

    /**
     * @return the sampleAcquisition
     */
    public SampleAcquisition getSampleAcquisition() {
        return sampleAcquisition;
    }

    /**
     * @param sampleAcquisition the sampleAcquisition to set
     */
    public void setSampleAcquisition(SampleAcquisition sampleAcquisition) {
        this.sampleAcquisition = sampleAcquisition;
    }

    /**
     * @return the valueCollection
     */
    public Collection<ResultValue> getValueCollection() {
        return valueCollection;
    }

    /**
     * @param valueCollection the valueCollection to set
     */
    public void setValueCollection(Collection<ResultValue> valueCollection) {
        this.valueCollection = valueCollection;
    }

    /**
     * @return the imageSeries
     */
    public ImageSeries getImageSeries() {
        return imageSeries;
    }

    /**
     * @param imageSeries the imageSeries to set
     */
    public void setImageSeries(ImageSeries imageSeries) {
        this.imageSeries = imageSeries;
    }

}
