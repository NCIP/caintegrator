package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * 
 */
public class ResultColumn extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Integer columnIndex;
    private String entityType;
    private Integer sortOrder;
    private String sortType;
    private AnnotationDefinition annotationDefinition;

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
     * @return the sortType
     */
    public String getSortType() {
        return sortType;
    }
    
    /**
     * @param sortType the sortType to set
     */
    public void setSortType(String sortType) {
        this.sortType = sortType;
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