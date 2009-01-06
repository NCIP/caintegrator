package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class StringComparisonCriterion extends AbstractAnnotationCriterion {

    private static final long serialVersionUID = 1L;
    
    private String stringValue;
    private String wildCardType;
    
    /**
     * @return the stringValue
     */
    public String getStringValue() {
        return stringValue;
    }
    
    /**
     * @param stringValue the stringValue to set
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
    
    /**
     * @return the wildCardType
     */
    public String getWildCardType() {
        return wildCardType;
    }
    
    /**
     * @param wildCardType the wildCardType to set
     */
    public void setWildCardType(String wildCardType) {
        this.wildCardType = wildCardType;
    }

}