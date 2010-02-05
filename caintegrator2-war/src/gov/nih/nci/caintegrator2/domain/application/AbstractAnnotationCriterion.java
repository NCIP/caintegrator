package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * 
 */
public class AbstractAnnotationCriterion extends AbstractCriterion implements Cloneable {
    
    private static final long serialVersionUID = 1L;
    
    private EntityTypeEnum entityType;
    // This will be @Deprecated.
    private AnnotationDefinition annotationDefinition;
    private AnnotationFieldDescriptor annotationFieldDescriptor;

    
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

    /**
     * @return the entityType
     */
    public EntityTypeEnum getEntityType() {
        return entityType;
    }

    /**
     * @param entityType the entityType to set
     */
    public void setEntityType(EntityTypeEnum entityType) {
        this.entityType = entityType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractAnnotationCriterion clone() throws CloneNotSupportedException {
        return (AbstractAnnotationCriterion) super.clone();
    }
    
    /**
     * @return the annotationFieldDescriptor
     */
    public AnnotationFieldDescriptor getAnnotationFieldDescriptor() {
        return annotationFieldDescriptor;
    }

    /**
     * @param annotationFieldDescriptor the annotationFieldDescriptor to set
     */
    public void setAnnotationFieldDescriptor(AnnotationFieldDescriptor annotationFieldDescriptor) {
        this.annotationFieldDescriptor = annotationFieldDescriptor;
    }

}