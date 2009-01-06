package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class AnalysisMethod extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String description;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
}