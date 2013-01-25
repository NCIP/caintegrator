/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 */
public class ArrayData extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Set<ReporterList> reporterLists = new HashSet<ReporterList>();
    private Sample sample;
    private Study study;
    private Array array;
    private ArrayDataType type;
    private SortedSet<SegmentData> segmentDatas = new TreeSet<SegmentData>();
    
    /**
     * @return the sample
     */
    public Sample getSample() {
        return sample;
    }
    
    /**
     * @param sample the sample to set
     */
    public void setSample(Sample sample) {
        this.sample = sample;
    }
    
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    
    /**
     * @return the array
     */
    public Array getArray() {
        return array;
    }
    
    /**
     * @param array the array to set
     */
    public void setArray(Array array) {
        this.array = array;
    }
    
    /**
     * The reporter type for this data.
     *  
     * @return the reporter type.
     */
    public ReporterTypeEnum getReporterType() {
        if (!reporterLists.isEmpty()) {
            return getReporterLists().iterator().next().getReporterType();
        } 
        return null;
    }

    /**
     * Validity check to ensure that this <code>ArrayData</code> object is associated with a <code>Study</code>.
     */
    public void checkHasStudy() {
        if (getStudy() == null) {
            throw new IllegalArgumentException("Null Study in ArrayData with id: " + getId());
        }
    }

    /**
     * @return the type
     */
    public ArrayDataType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ArrayDataType type) {
        this.type = type;
    }

    /**
     * @return the segmentDatas
     */
    public SortedSet<SegmentData> getSegmentDatas() {
        return segmentDatas;
    }

    /**
     * @param segmentDatas the segmentDatas to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setSegmentDatas(SortedSet<SegmentData> segmentDatas) {
        this.segmentDatas = segmentDatas;
    }

    /**
     * @return the reporterLists
     */
    public Set<ReporterList> getReporterLists() {
        return reporterLists;
    }

    /**
     * @param reporterLists the reporterLists to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setReporterLists(Set<ReporterList> reporterLists) {
        this.reporterLists = reporterLists;
    }

    /**
     * Returns all reporters associated with this ArrayData.
     * @return all abstract reporters found.
     */
    public Collection<AbstractReporter> getReporters() {
        HashSet<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (ReporterList reporterList : getReporterLists()) {
            reporters.addAll(reporterList.getReporters());
        }
        return reporters;
    }

}
