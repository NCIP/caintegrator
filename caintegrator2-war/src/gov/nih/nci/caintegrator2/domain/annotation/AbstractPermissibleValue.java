package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class AbstractPermissibleValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String conceptCode;
    private String description;
    private String valueMeaning;
    
    /**
     * @return the conceptCode
     */
    public String getConceptCode() {
        return conceptCode;
    }
    
    /**
     * @param conceptCode the conceptCode to set
     */
    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }
    
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
    
    /**
     * @return the valueMeaning
     */
    public String getValueMeaning() {
        return valueMeaning;
    }
    
    /**
     * @param valueMeaning the valueMeaning to set
     */
    public void setValueMeaning(String valueMeaning) {
        this.valueMeaning = valueMeaning;
    }

}