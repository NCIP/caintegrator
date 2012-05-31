package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;

/**
 *
 */
public class Sample extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private String name;
    private GenomicDataSourceConfiguration genomicDataSource;
    private SampleAcquisition sampleAcquisition;
    private Set<ArrayData> arrayDataCollection = new HashSet<ArrayData>();
    private Set<Array> arrayCollection = new HashSet<Array>();
    private Set<AbstractReporter> reportersHighVariance = new HashSet<AbstractReporter>();
    private Date creationDate = new Date();
    private SampleRefreshTypeEnum refreshType;


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
     * Returns all array datas of the given type for this sample.
     *
     * @param reporterType return array data of this type.
     * @param platform to retrieve Array Datas for (null if you want to use all platforms).
     * @return the array datas.
     */
    public Set<ArrayData> getArrayDatas(ReporterTypeEnum reporterType, Platform platform) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (ArrayData arrayData : getArrayDataCollection()) {
            if (reporterType.equals(arrayData.getReporterType())
                && (platform == null || platform.equals(arrayData.getArray().getPlatform()))) {
                arrayDatas.add(arrayData);
            }
        }
        return Collections.unmodifiableSet(arrayDatas);
    }

    /**
     * @return the arrayDataCollection
     */
    public Set<ArrayData> getArrayDataCollection() {
        return arrayDataCollection;
    }

    /**
     * @param arrayDataCollection the arrayDataCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setArrayDataCollection(Set<ArrayData> arrayDataCollection) {
        this.arrayDataCollection = arrayDataCollection;
    }

    /**
     * @return the arrayCollection
     */
    public Set<Array> getArrayCollection() {
        return arrayCollection;
    }

    /**
     * @param arrayCollection the arrayCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setArrayCollection(Set<Array> arrayCollection) {
        this.arrayCollection = arrayCollection;
    }

    /**
     * @return the reportersHighVariance
     */
    public Set<AbstractReporter> getReportersHighVariance() {
        return reportersHighVariance;
    }

    /**
     * @param reportersHighVariance the reportersHighVariance to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setReportersHighVariance(Set<AbstractReporter> reportersHighVariance) {
        this.reportersHighVariance = reportersHighVariance;
    }

    /**
     * @return the genomicDataSource
     */
    public GenomicDataSourceConfiguration getGenomicDataSource() {
        return genomicDataSource;
    }

    /**
     * @param genomicDataSource the genomicDataSource to set
     */
    public void setGenomicDataSource(GenomicDataSourceConfiguration genomicDataSource) {
        this.genomicDataSource = genomicDataSource;
    }

    /**
     * @return the study
     */
    public Study getStudy() {
        if (getSampleAcquisition() != null) {
            return getSampleAcquisition().getStudy();
        } else {
            return null;
        }
    }

    /**
     * Removes the associations from the Sample Acquisition.
     */
    public void removeSampleAcquisitionAssociations() {
        if (sampleAcquisition != null) {
            sampleAcquisition.getAssignment().getSampleAcquisitionCollection().remove(sampleAcquisition);
            sampleAcquisition.setAssignment(null);
            sampleAcquisition = null;
        }
    }

    /**
     * Remove all array data from this sample.
     */
    public void clearArrayData() {
        arrayDataCollection.clear();
        arrayCollection.clear();
        reportersHighVariance.clear();
    }

    /**
     * @param refreshType the refreshType to set
     */
    public void setRefreshType(SampleRefreshTypeEnum refreshType) {
        this.refreshType = refreshType;
    }

    /**
     * @return the refreshType
     */
    @Transient
    public SampleRefreshTypeEnum getRefreshType() {
        return refreshType;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}