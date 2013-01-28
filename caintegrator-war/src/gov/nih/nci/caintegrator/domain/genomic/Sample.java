/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

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
    private Set<SampleAcquisition> sampleAcquisitions = new HashSet<SampleAcquisition>();
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
     * @return the sampleAcquisitions
     */
    public Set<SampleAcquisition> getSampleAcquisitions() {
        return sampleAcquisitions;
    }

    /**
     * @param sampleAcquisitions the sampleAcquisitions to set
     */
    public void setSampleAcquisitions(Set<SampleAcquisition> sampleAcquisitions) {
        this.sampleAcquisitions = sampleAcquisitions;
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
