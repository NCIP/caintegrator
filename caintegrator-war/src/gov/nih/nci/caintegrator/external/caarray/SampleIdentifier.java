/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

/**
 * Uniquely identifies a sample in caArray.
 */
public class SampleIdentifier extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    private String experimentIdentifier;
    private String sampleName;
    
    /**
     * @return the experimentIdentifier
     */
    public String getExperimentIdentifier() {
        return experimentIdentifier;
    }
    
    /**
     * @param experimentIdentifier the experimentIdentifier to set
     */
    public void setExperimentIdentifier(String experimentIdentifier) {
        this.experimentIdentifier = experimentIdentifier;
    }
    
    /**
     * @return the sampleName
     */
    public String getSampleName() {
        return sampleName;
    }
    
    /**
     * @param sampleName the sampleName to set
     */
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

}
