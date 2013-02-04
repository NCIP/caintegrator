/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * 
 */
public class AbstractAnnotationCriterion extends AbstractCriterion implements Cloneable {
    
    private static final long serialVersionUID = 1L;
    
    private EntityTypeEnum entityType;
    private AnnotationDefinition annotationDefinition;
    
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

}
