package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
     * @return the array datas.
     */
    public Set<ArrayData> getArrayDatas(ReporterTypeEnum reporterType) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (ArrayData arrayData : getArrayDataCollection()) {
            if (reporterType.equals(arrayData.getReporterType())) {
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

}