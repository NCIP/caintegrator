/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;

/**
 * 
 */
public class FoldChangeCriterion extends AbstractGenomicCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    
    private Float foldsUp;
    private Float foldsDown;
    private RegulationTypeEnum regulationType;
    private String controlSampleSetName;
    private transient SampleSet compareToSampleSet = new SampleSet();
    

    /**
     * @return the regulationType
     */
    public RegulationTypeEnum getRegulationType() {
        return regulationType;
    }

    /**
     * @param regulationType the regulationType to set
     */
    public void setRegulationType(RegulationTypeEnum regulationType) {
        this.regulationType = regulationType;
    }

    /**
     * @return the controlSampleSetName
     */
    public String getControlSampleSetName() {
        return controlSampleSetName;
    }

    /**
     * @param controlSampleSetName the controlSampleSetName to set
     */
    public void setControlSampleSetName(String controlSampleSetName) {
        this.controlSampleSetName = controlSampleSetName;
    }

    /**
     * @return the foldsUp
     */
    public Float getFoldsUp() {
        return foldsUp;
    }

    /**
     * @param foldsUp the foldsUp to set
     */
    public void setFoldsUp(Float foldsUp) {
        this.foldsUp = foldsUp;
    }

    /**
     * @return the foldsDown
     */
    public Float getFoldsDown() {
        return foldsDown;
    }

    /**
     * @param foldsDown the foldsDown to set
     */
    public void setFoldsDown(Float foldsDown) {
        this.foldsDown = foldsDown;
    }

    /**
     * {@inheritDoc}
     */
    protected FoldChangeCriterion clone() throws CloneNotSupportedException {
        FoldChangeCriterion clone = (FoldChangeCriterion) super.clone();
        clone.setCompareToSampleSet(this.compareToSampleSet);
        return clone;
    }

    /**
     * @return the compareToSampleSet
     */
    public SampleSet getCompareToSampleSet() {
        return compareToSampleSet;
    }

    /**
     * @param compareToSampleSet the compareToSampleSet to set
     */
    public void setCompareToSampleSet(SampleSet compareToSampleSet) {
        this.compareToSampleSet = compareToSampleSet;
    }
}
