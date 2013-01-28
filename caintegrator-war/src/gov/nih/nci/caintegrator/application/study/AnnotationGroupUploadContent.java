/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;

import org.apache.commons.lang3.StringUtils;

/**
 * Object that hold the upload file context.
 */
public class AnnotationGroupUploadContent {
    private String columnName = null;
    private AnnotationFieldType annotationType;
    private Long cdeId = null;
    private Float version = null;
    private String definitionName;
    private AnnotationTypeEnum dataType = null;
    private EntityTypeEnum entityType = null;
    private boolean permissible = false;
    private boolean visible = false;

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }
    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    /**
     * @return the annotationType
     */
    public AnnotationFieldType getAnnotationType() {
        return annotationType;
    }
    /**
     * @param annotationType the annotationType to set
     */
    public void setAnnotationType(String annotationType) {
        this.annotationType = AnnotationFieldType.getByValue(annotationType);
    }
    /**
     * @return the cdeId
     */
    public Long getCdeId() {
        return cdeId;
    }
    /**
     * @param cdeId the cdeId to set
     */
    public void setCdeId(String cdeId) {
        if (!StringUtils.isBlank(cdeId)) {
            this.cdeId = Long.valueOf(cdeId);
        }
    }
    /**
     * @return the version
     */
    public Float getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        if (!StringUtils.isBlank(version)) {
            this.version = Float.valueOf(version);
        }
    }
    /**
     * @return the definitionName
     */
    public String getDefinitionName() {
        return definitionName;
    }
    /**
     * @param definitionName the definitionName to set
     */
    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }
    /**
     * @return the dataType
     */
    public AnnotationTypeEnum getDataType() {
        return dataType;
    }
    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = AnnotationTypeEnum.getByValue(dataType);
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
    public void setEntityType(String entityType) {
        this.entityType = EntityTypeEnum.getByValue(entityType);
    }
    /**
     * @return the permissible
     */
    public boolean isPermissible() {
        return permissible;
    }
    /**
     * @param permissible the permissible to set
     */
    public void setPermissible(String permissible) {
        this.permissible = "Yes".equalsIgnoreCase(permissible);
    }
    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * @param visible the visible to set
     */
    public void setVisible(String visible) {
        this.visible = "Yes".equalsIgnoreCase(visible);
    }

    /**
     * Create a new annotation field descriptor.
     * @return the annotationFieldDescriptor
     */
    public AnnotationFieldDescriptor createAnnotationFieldDescriptor() {
        AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
        annotationFieldDescriptor.setName(getColumnName());
        annotationFieldDescriptor.setAnnotationEntityType(getEntityType());
        annotationFieldDescriptor.setType(getAnnotationType());
        if (AnnotationFieldType.ANNOTATION.equals(annotationFieldDescriptor.getType())) {
            annotationFieldDescriptor.setUsePermissibleValues(isPermissible());
        }
        annotationFieldDescriptor.setShownInBrowse(isVisible());
        return annotationFieldDescriptor;
    }

    /**
     * Locate or create a new annotation definition.
     * @return an annotation definition
     * @throws ValidationException when cdeId is not null
     */
    public AnnotationDefinition createAnnotationDefinition() throws ValidationException {
        if (cdeId != null) {
            throw new ValidationException("Don't know how to create CaDSR annotation definition.");
        }
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setKeywords(getDefinitionName());
        annotationDefinition.getCommonDataElement().setLongName(getDefinitionName());
        annotationDefinition.getCommonDataElement().getValueDomain().setDataType(getDataType());
        return annotationDefinition;
    }

    /**
     * Validate that the input annotation definition is matching with this content.
     * @param definition to validate
     * @return boolean for matching
     */
    public boolean matching(AnnotationDefinition definition) {
        if (cdeId != null) {
            return matchingCdeId(definition);
        }
        return definition.getKeywords().equals(getDefinitionName())
            && definition.getDataType().equals(getDataType())
            && definition.getCommonDataElement().getLongName().equalsIgnoreCase(getDefinitionName());
    }

    private boolean matchingCdeId(AnnotationDefinition definition) {
        return (cdeId.equals(definition.getCommonDataElement().getPublicID())
                    && (version == null
                            || version.equals(Float.valueOf(definition.getCommonDataElement().getVersion()))));
    }
}
