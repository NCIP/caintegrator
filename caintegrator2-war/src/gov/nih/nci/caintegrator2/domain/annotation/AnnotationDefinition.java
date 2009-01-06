package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class AnnotationDefinition extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String displayName;
    private String keywords;
    private String preferredDefinition;
    private String type;
    private String unitsOfMeasure;
    
    private CommonDataElement cde;
    private Collection<AbstractAnnotationValue> annotationValueCollection;
    private Collection<AbstractPermissibleValue> permissibleValueCollection;
    
    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * @return the keywords
     */
    public String getKeywords() {
        return keywords;
    }
    
    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    /**
     * @return the preferredDefinition
     */
    public String getPreferredDefinition() {
        return preferredDefinition;
    }
    
    /**
     * @param preferredDefinition the preferredDefinition to set
     */
    public void setPreferredDefinition(String preferredDefinition) {
        this.preferredDefinition = preferredDefinition;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return the unitsOfMeasure
     */
    public String getUnitsOfMeasure() {
        return unitsOfMeasure;
    }
    
    /**
     * @param unitsOfMeasure the unitsOfMeasure to set
     */
    public void setUnitsOfMeasure(String unitsOfMeasure) {
        this.unitsOfMeasure = unitsOfMeasure;
    }
    
    /**
     * @return the cde
     */
    public CommonDataElement getCde() {
        return cde;
    }
    
    /**
     * @param cde the cde to set
     */
    public void setCde(CommonDataElement cde) {
        this.cde = cde;
    }
    
    /**
     * @return the annotationValueCollection
     */
    public Collection<AbstractAnnotationValue> getAnnotationValueCollection() {
        return annotationValueCollection;
    }
    
    /**
     * @param annotationValueCollection the annotationValueCollection to set
     */
    public void setAnnotationValueCollection(Collection<AbstractAnnotationValue> annotationValueCollection) {
        this.annotationValueCollection = annotationValueCollection;
    }
    
    /**
     * @return the permissibleValueCollection
     */
    public Collection<AbstractPermissibleValue> getPermissibleValueCollection() {
        return permissibleValueCollection;
    }
    
    /**
     * @param permissibleValueCollection the permissibleValueCollection to set
     */
    public void setPermissibleValueCollection(Collection<AbstractPermissibleValue> permissibleValueCollection) {
        this.permissibleValueCollection = permissibleValueCollection;
    }
    
}