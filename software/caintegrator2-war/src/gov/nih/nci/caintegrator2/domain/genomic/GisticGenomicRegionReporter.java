/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

/**
 * Reporter retrieved by running Gistic analysis.
 */
public class GisticGenomicRegionReporter extends AbstractReporter {
    private static final long serialVersionUID = 1L;
    
    private String genomicDescriptor;
    private String broadOrFocal;
    private Double qvalue;
    private Double residualQValue;
    private String regionBoundaries;
    private String widePeakBoundaries;
    private AmplificationTypeEnum geneAmplificationType;

    /**
     * @return the genomicDescriptor
     */
    public String getGenomicDescriptor() {
        return genomicDescriptor;
    }
    /**
     * @param genomicDescriptor the genomicDescriptor to set
     */
    public void setGenomicDescriptor(String genomicDescriptor) {
        this.genomicDescriptor = genomicDescriptor;
    }
    /**
     * @return the broadOrFocal
     */
    public String getBroadOrFocal() {
        return broadOrFocal;
    }
    /**
     * @param broadOrFocal the broadOrFocal to set
     */
    public void setBroadOrFocal(String broadOrFocal) {
        this.broadOrFocal = broadOrFocal;
    }
    /**
     * @return the qValue
     */
    public Double getQvalue() {
        return qvalue;
    }
    /**
     * @param qvalue the qvalue to set
     */
    public void setQvalue(Double qvalue) {
        this.qvalue = qvalue;
    }
    /**
     * @return the residualQValue
     */
    public Double getResidualQValue() {
        return residualQValue;
    }
    /**
     * @param residualQValue the residualQValue to set
     */
    public void setResidualQValue(Double residualQValue) {
        this.residualQValue = residualQValue;
    }
    /**
     * @return the regionBoundaries
     */
    public String getRegionBoundaries() {
        return regionBoundaries;
    }
    /**
     * @param regionBoundaries the regionBoundaries to set
     */
    public void setRegionBoundaries(String regionBoundaries) {
        this.regionBoundaries = regionBoundaries;
    }
    /**
     * @return the widePeakBoundaries
     */
    public String getWidePeakBoundaries() {
        return widePeakBoundaries;
    }
    /**
     * @param widePeakBoundaries the widePeakBoundaries to set
     */
    public void setWidePeakBoundaries(String widePeakBoundaries) {
        this.widePeakBoundaries = widePeakBoundaries;
    }
    /**
     * @return the geneAmplificationType
     */
    public AmplificationTypeEnum getGeneAmplificationType() {
        return geneAmplificationType;
    }
    /**
     * @param geneAmplificationType the geneAmplificationType to set
     */
    public void setGeneAmplificationType(AmplificationTypeEnum geneAmplificationType) {
        this.geneAmplificationType = geneAmplificationType;
    }

}
