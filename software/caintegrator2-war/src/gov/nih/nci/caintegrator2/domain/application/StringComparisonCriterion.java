package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class StringComparisonCriterion extends AbstractAnnotationCriterion {

    private static final long serialVersionUID = 1L;
    
    private String stringValue;
    private WildCardTypeEnum wildCardType;
    
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
    public WildCardTypeEnum getWildCardType() {
        return wildCardType;
    }

    /**
     * @param wildCardType the wildCardType to set
     */
    public void setWildCardType(WildCardTypeEnum wildCardType) {
        this.wildCardType = wildCardType;
    }

}