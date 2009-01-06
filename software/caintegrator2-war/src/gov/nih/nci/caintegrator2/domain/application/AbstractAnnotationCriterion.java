package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * 
 */
public class AbstractAnnotationCriterion extends AbstractCriterion {
    
    private static final long serialVersionUID = 1L;
    
    private String entityType;
    private AnnotationDefinition annotationDefinition;
    
    /**
     * @return the entityType
     */
    public String getEntityType() {
        return entityType;
    }
    
    /**
     * @param entityType the entityType to set
     */
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    /**
     * @return the annotationDefinition
     */
    public AnnotationDefinition getAnnotationDefinition() {
        return annotationDefinition;
    }
    
    /**
     * @param annotationDefinition the annotationDefinition to set
     */
    public void setAnnotationDefinition(AnnotationDefinition annotationDefinition) {
        this.annotationDefinition = annotationDefinition;
    }

}