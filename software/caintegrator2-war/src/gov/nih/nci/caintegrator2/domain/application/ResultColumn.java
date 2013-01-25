/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * 
 */
public class ResultColumn extends AbstractCaIntegrator2Object implements Cloneable, Comparable<ResultColumn> {

    private static final long serialVersionUID = 1L;
    
    private Integer columnIndex;
    private EntityTypeEnum entityType;
    private Integer sortOrder;
    private SortTypeEnum sortType = SortTypeEnum.UNSORTED;
    private AnnotationFieldDescriptor annotationFieldDescriptor;
    private Query query;
    
    /**
     * @return the columnIndex
     */
    public Integer getColumnIndex() {
        return columnIndex;
    }
    
    /**
     * @param columnIndex the columnIndex to set
     */
    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }
    
    /**
     * @return the sortOrder
     */
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    /**
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    /**
     * @return the annotationDefinition
     */
    public AnnotationDefinition getAnnotationDefinition() {
        return annotationFieldDescriptor != null ? annotationFieldDescriptor.getDefinition() : null;
    }

    /**
     * @return the sortType
     */
    public SortTypeEnum getSortType() {
        return sortType;
    }

    /**
     * @param sortType the sortType to set
     */
    public void setSortType(SortTypeEnum sortType) {
        this.sortType = sortType;
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
    protected ResultColumn clone() throws CloneNotSupportedException {
        return (ResultColumn) super.clone();
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
        if (entityType == null && annotationFieldDescriptor != null) {
            setEntityType(annotationFieldDescriptor.getAnnotationEntityType());
        }
    }

    /**
     * @return the query
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(ResultColumn o) {
        return getColumnIndex() - o.getColumnIndex();
    }
    
    /**
     * Display name for the column.
     * @return display name.
     */
    public String getDisplayName() {
        return annotationFieldDescriptor.getDisplayName();
    }
}
