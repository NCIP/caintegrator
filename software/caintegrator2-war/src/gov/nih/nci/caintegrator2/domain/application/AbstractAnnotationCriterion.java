/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;

/**
 *
 */
public abstract class AbstractAnnotationCriterion extends AbstractCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;

    private EntityTypeEnum entityType;
    private AnnotationFieldDescriptor annotationFieldDescriptor;


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
        if (annotationFieldDescriptor != null) {
            this.entityType = annotationFieldDescriptor.getAnnotationEntityType();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMaskedCriterion() {
        return annotationFieldDescriptor != null && !annotationFieldDescriptor.getAnnotationMasks().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // Default implementation is null
    public String getPlatformName(GenomicCriterionTypeEnum genomicCriterionType) {
        return null;
    }

}
