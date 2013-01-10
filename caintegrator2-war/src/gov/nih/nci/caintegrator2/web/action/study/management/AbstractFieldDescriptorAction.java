/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.mask.AbstractAnnotationMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Action used to edit the type and annotation of a field descriptor by a Study Manager.
 */
public abstract class AbstractFieldDescriptorAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;

    /**
     * Identifier label.
     */
    protected static final String IDENTIFIER_TYPE = "Identifier";
    private static final String ANNOTATION_TYPE = "Annotation";
    private static final String TIMEPOINT_TYPE = "Timepoint";
    private static final String GRAY_BACKGROUND = "background-color:#e9e9e9;";
    private static final String GRAY_FOREGROUND = "color:#999999;";
    private static final String[] FIELD_DESCRIPTOR_TYPES = new String[] {ANNOTATION_TYPE, IDENTIFIER_TYPE};

    private AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
    private boolean readOnly;
    private boolean cancelEnabled;
    private List<String> availableUpdateList = new ArrayList<String>();
    private List<String> permissibleUpdateList = new ArrayList<String>();
    private int dataElementIndex;
    private int definitionIndex;
    private String fieldDescriptorType;
    private String sourceId;
    private String groupId;
    private AnnotationMaskForm maskForm = new AnnotationMaskForm();

    private void clearCacheMemory() {
        fieldDescriptorType = null;
        getDisplayableWorkspace().getDataElementSearchObject().clear();
        availableUpdateList.clear();
        permissibleUpdateList.clear();
    }

    /**
     * Refreshes the current source configuration.
     */
    @Override
    public void prepare() {
        super.prepare();
        if (fieldDescriptor.getId() != null) {
            fieldDescriptor = getStudyManagementService().getRefreshedEntity(fieldDescriptor);
            if (fieldDescriptor.getDefinition() != null) {
                fieldDescriptor.setDefinition(getStudyManagementService().getRefreshedEntity(
                        fieldDescriptor.getDefinition()));
                HibernateUtil.loadCollections(fieldDescriptor.getDefinition());
            }
        }
        setReadOnly(true);
        cancelEnabled = true;
        loadExistingMasks();
    }

    private void loadExistingMasks() {
        maskForm.clear();
        for (AbstractAnnotationMask mask : fieldDescriptor.getAnnotationMasks()) {
            if (mask instanceof NumericRangeMask) {
                maskForm.setNumericRangeMask((NumericRangeMask) mask);
                maskForm.setHasNumericRangeMask(true);
            } else if (mask instanceof MaxNumberMask) {
                maskForm.setMaxNumberMask((MaxNumberMask) mask);
                maskForm.setHasMaxNumberMask(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
    }

    /**
     * Edit a data source file column.
     *
     * @return the Struts result.
     */
    public String editFieldDescriptor() {
        clearCacheMemory();
        return SUCCESS;
    }

    /**
     * Cancel returns you to the edit source.
     * @return struts result.
     */
    public String cancel() {
        return SUCCESS;
    }

    /**
     * Saves the column type to the database.
     * @return the Struts result.
     */
    public String saveFieldDescriptorType() {
        try {
            updateColumnType();
        } catch (ValidationException e) {
            addActionError(e.getMessage());
            return ERROR;
        }
        getStudyManagementService().makeFieldDescriptorValid(fieldDescriptor);
        updateDataSourceStatus();
        clearCacheMemory();
        cancelEnabled = false;
        return SUCCESS;
    }

    /**
     * Selects an existing annotation definition for a column.
     *
     * @return the Struts result.
     */
    public String selectDefinition() {
        AnnotationFieldDescriptor originalFieldDescriptor = getFieldDescriptor();
        AnnotationDefinition definitionToUse = getStudyManagementService().
                        getRefreshedEntity(getDefinitions().get(getDefinitionIndex()));
        try {
            getStudyManagementService().setDefinition(getStudyConfiguration().getStudy(),
                                                  fieldDescriptor,
                                                  definitionToUse,
                                                  getEntityType());
            getStudyManagementService().makeFieldDescriptorValid(fieldDescriptor);
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            setFieldDescriptor(originalFieldDescriptor);
            prepare();
            return ERROR;
        }
        updateDataSourceStatus();
        return SUCCESS;
    }

    /**
     * Let's the user create a new AnnotationDefinition for a column.
     * @return the Struts result.
     * @throws ValidationException Invalid data
     * @throws ParseException Invalid data
     */
    public String createNewDefinition() throws ValidationException, ParseException {
        AnnotationTypeEnum annotationType = AnnotationTypeEnum.STRING;
        if (fieldDescriptor.getDefinition() != null) {
            annotationType =
                fieldDescriptor.getDefinition().getDataType();
        }
        getStudyManagementService().createDefinition(fieldDescriptor,
                                                     getStudyConfiguration().getStudy(),
                                                     getEntityType(),
                                                     annotationType);
        setReadOnly(false);
        cancelEnabled = false;
        // Default the available values to be permissible on any new definition.
        getPermissibleUpdateList().clear();
        getPermissibleUpdateList().addAll(getAvailableValues());
        updatePermissible();
        clearCacheMemory();
        getStudyManagementService().makeFieldDescriptorValid(fieldDescriptor);
        return SUCCESS;
    }

    /**
     * Selects an existing CaDSR data element as the definition for a column.
     *
     * @return the Struts result.
     */
    public String selectDataElement() {
        AnnotationFieldDescriptor originalFieldDescriptor = getFieldDescriptor();
        try {
            getStudyManagementService().setDataElement(fieldDescriptor,
                                                       retrieveSelectedDataElement(),
                                                       getStudyConfiguration().getStudy(),
                                                       getEntityType(),
                                                       getKeywordsForSearch());
            getStudyManagementService().makeFieldDescriptorValid(fieldDescriptor);
        } catch (ConnectionException e) {
            addActionError(e.getMessage());
            setFieldDescriptor(originalFieldDescriptor);
            prepare();
            return ERROR;
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            setFieldDescriptor(originalFieldDescriptor);
            prepare();
            return ERROR;
        }
        updateDataSourceStatus();
        return SUCCESS;
    }

    private CommonDataElement retrieveSelectedDataElement() {
        CommonDataElement selectedDataElement = getDataElements().get(getDataElementIndex());
        selectedDataElement.setId(null);
        selectedDataElement.setValueDomain(null);
        return selectedDataElement;
    }


    /**
     * Updates a clinical data source file column.
     *
     * @return the Struts result.
     */
    public String updateAnnotationDefinition() {
        try {
            if (isPermissibleOn() && !isFromCadsr()) {
                updatePermissible();
            }
            if (!validateAndSetMasks()) {
                clearCacheMemory();
                fieldDescriptor.setDefinition(getStudyManagementService().getRefreshedEntity(
                        fieldDescriptor.getDefinition()));
                return ERROR;
            }
            if (ANNOTATION_TYPE.equals(fieldDescriptorType)
                 && fieldDescriptor.getDefinition() != null) {
                getStudyManagementService().save(fieldDescriptor.getDefinition());
            }
            getStudyManagementService().makeFieldDescriptorValid(fieldDescriptor);
        } catch (ValidationException e) {
            addActionError(e.getMessage());
            clearCacheMemory();
            fieldDescriptor.setDefinition(getStudyManagementService().getRefreshedEntity(
                    fieldDescriptor.getDefinition()));
            setReadOnly(false);
            return ERROR;
        }
        return SUCCESS;
    }

    private boolean validateAndSetMasks() {
        if (fieldDescriptor.getDefinition() != null) {
            validateAndSetMaxNumberMask();
            validateAndSetNumericRangeMask();
        }
        return getActionErrors().isEmpty();
    }

    private void validateAndSetNumericRangeMask() {
        if (!AnnotationTypeEnum.NUMERIC.equals(fieldDescriptor.getDefinition().getDataType())
                || !maskForm.isHasNumericRangeMask()) {
            fieldDescriptor.clearMask(NumericRangeMask.class);
        } else {
            validateNoPermissibleValues();
            if (maskForm.getNumericRangeMask().getNumericRange() == null) {
                addActionError(getText("struts.messages.error.numeric.mask.invalid", getArgs("numeric range")));
            }
            if (getActionErrors().isEmpty()) {
                fieldDescriptor.setNumericRange(maskForm.getNumericRangeMask().getNumericRange());
            }
        }
    }

    private void validateAndSetMaxNumberMask() {
        if (!AnnotationTypeEnum.NUMERIC.equals(fieldDescriptor.getDefinition().getDataType())
                || !maskForm.isHasMaxNumberMask()) {
            fieldDescriptor.clearMask(MaxNumberMask.class);
        } else {
            validateNoPermissibleValues();
            if (maskForm.getMaxNumberMask().getMaxNumber() == null) {
                addActionError(getText("struts.messages.error.numeric.mask.invalid", getArgs("max number")));
            }
            if (getActionErrors().isEmpty()) {
                fieldDescriptor.setMaxNumber(maskForm.getMaxNumberMask().getMaxNumber());
            }
        }
    }

    private void validateNoPermissibleValues() {
        if (!permissibleUpdateList.isEmpty()) {
            addActionError(getText("struts.messages.error.mask.invalid.no.permissible"));
        }
    }

    /**
     * @return the fieldDescriptorType
     */
    public String getFieldDescriptorType() {
        if (this.fieldDescriptorType == null) {
            if (AnnotationFieldType.IDENTIFIER.equals(fieldDescriptor.getType())) {
                fieldDescriptorType = IDENTIFIER_TYPE;
            } else if (AnnotationFieldType.TIMEPOINT.equals(fieldDescriptor.getType())) {
                fieldDescriptorType = TIMEPOINT_TYPE;
            } else {
                fieldDescriptorType = ANNOTATION_TYPE;
            }
        }
        return fieldDescriptorType;
    }

    /**
     * @param fieldDescType the fieldDescriptorType to set
     */
    public void setFieldDescriptorType(String fieldDescType) {
        this.fieldDescriptorType = fieldDescType;
    }

    /**

     *
     * @return if the ColumnType is ANNOTATION_TYPE.
     */
    public boolean isColumnTypeAnnotation() {
        if (getFieldDescriptorType().equals(ANNOTATION_TYPE)) {
            return true;
        }
        return false;
    }


    private void updateColumnType() throws ValidationException {
        AnnotationFieldType type = AnnotationFieldType.ANNOTATION;
        if (IDENTIFIER_TYPE.equals(fieldDescriptorType)) {
            type = AnnotationFieldType.IDENTIFIER;
        } else if (TIMEPOINT_TYPE.equals(fieldDescriptorType)) {
            type = AnnotationFieldType.TIMEPOINT;
        } else if (ANNOTATION_TYPE.equals(fieldDescriptorType)) {
            type = AnnotationFieldType.ANNOTATION;
        }
        setFieldDescriptor(getStudyManagementService().updateFieldDescriptorType(fieldDescriptor, type));
    }

    /**
     * @return the columnTypes
     */
    public String[] getFieldDescriptorTypes() {
        return FIELD_DESCRIPTOR_TYPES;
    }

    /**
     * Converts the enum to the string list.
     * @return the annotationTypes
     */
    public String[] getAnnotationDataTypes() {
        List<String> types = new ArrayList<String>();
        for (AnnotationTypeEnum type : AnnotationTypeEnum.values()) {
            types.add(type.getValue());
        }
        return types.toArray(new String[types.size()]);
    }

    /**
     * @return the definitions
     */
    public List<AnnotationDefinition> getDefinitions() {
        return getDisplayableWorkspace().getDataElementSearchObject().getSearchDefinitions();
    }

    /**
     * @return the dataElements
     */
    public List<CommonDataElement> getDataElements() {
        return getDisplayableWorkspace().getDataElementSearchObject().getSearchCommonDataElements();
    }

    /**
     * @return the dataElementIndex
     */
    public int getDataElementIndex() {
        return dataElementIndex;
    }

    /**
     * @param dataElementIndex the dataElementIndex to set
     */
    public void setDataElementIndex(int dataElementIndex) {
        this.dataElementIndex = dataElementIndex;
    }

    /**
     * @return the definitionIndex
     */
    public int getDefinitionIndex() {
        return definitionIndex;
    }

    /**
     * @param definitionIndex the definitionIndex to set
     */
    public void setDefinitionIndex(int definitionIndex) {
        this.definitionIndex = definitionIndex;
    }

    /**
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Determines if we want to disable the numeric mask form.
     * @return T/F value.
     */
    public boolean isNumericMaskDisabled() {
        return !isReadOnly() ? false
                : !AnnotationTypeEnum.NUMERIC.equals(fieldDescriptor.getDefinition().getDataType());
    }

    /**
     * @return the keywordsForSearch
     */
    public String getKeywordsForSearch() {
        return getDisplayableWorkspace().getDataElementSearchObject().getKeywordsForSearch();
    }



    /**
     *
     * @return if the Permissible is available.
     */
    public boolean isPermissibleOn() {
        if (isColumnTypeAnnotation()
                && fieldDescriptor != null
                && fieldDescriptor.getDefinition() != null) {
            return true;
        }
        return false;
    }

    /**
     * @return the availableValue
     * @throws ValidationException Invalid data
     */
    public Set<String> getAvailableValues() throws ValidationException {
        return getStudyManagementService().getAvailableValuesForFieldDescriptor(fieldDescriptor);
    }

    /**
     * @return the permissibleValues
     */
    public Set<String> getPermissibleValues() {
        return PermissibleValueUtil.getDisplayPermissibleValue(getPermissibleCollection());
    }

    /**
     * @return the Struts result.
     */
    private void updatePermissible() {
        PermissibleValueUtil.update(getDefinitionType(),
                getPermissibleCollection(), getPermissibleUpdateList());
    }

    private Collection<PermissibleValue> getPermissibleCollection() {
        return fieldDescriptor.getDefinition().getPermissibleValueCollection();
    }

    /**
     * @return the permissibleReturnList
     */
    public List<String> getPermissibleUpdateList() {
        return permissibleUpdateList;
    }

    /**
     * @param permissibleUpdateList the new list of permissible display string
     */
    public void setPermissibleUpdateList(List<String> permissibleUpdateList) {
        this.permissibleUpdateList = permissibleUpdateList;
    }

    /**
     * @return the availableUpdateList
     */
    public List<String> getAvailableUpdateList() {
        return availableUpdateList;
    }

    /**
     * @param availableUpdateList the availableUpdateList to set
     */
    public void setAvailableUpdateList(List<String> availableUpdateList) {
        this.availableUpdateList = availableUpdateList;
    }

    /**
     * Determines if the AnnotationDefinition came from a CDE.
     *
     * @return T/F value.
     */
    public boolean isFromCadsr() {
        if (fieldDescriptor != null
            && fieldDescriptor.getDefinition() != null
            && fieldDescriptor.getDefinition().getCommonDataElement().getPublicID() != null) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return the column definition type.
     */
    public String getDefinitionType() {
        return fieldDescriptor.getDefinition().getDataType().getValue();
    }

    /**
     * @return the cancelEnabled
     */
    public boolean isCancelEnabled() {
        return cancelEnabled;
    }

    /**
     * @param cancelEnabled the cancelEnabled to set
     */
    public void setCancelEnabled(boolean cancelEnabled) {
        this.cancelEnabled = cancelEnabled;
    }

    private EntityTypeEnum getEntityType() {
        return fieldDescriptor.getAnnotationEntityType();
    }

    /**
     * The string for the SaveFieldType action.
     * @return struts action string.
     */
    public abstract String getSaveFieldDescriptorTypeAction();

    /**
     * The string for the NewDefinition action.
     * @return struts action string.
     */
    public abstract String getNewDefinitionAction();

    /**
     * The string for the SaveDefinition action.
     * @return struts action string.
     */
    public abstract String getSaveAnnotationDefinitionAction();

    /**
     * The string for the Cancel action.
     * @return struts action string.
     */
    public abstract String getCancelAction();

    /**
     * The string for the Select Definition action.
     * @return struts action string.
     */
    public abstract String getSelectDefinitionAction();

    /**
     * The string for the Select Data Element action.
     * @return struts action string.
     */
    public abstract String getSelectDataElementAction();

    /**
     * The string for the EntityType action.
     * @return struts action string.
     */
    public abstract String getEntityTypeForSearch();


    /**
     * Updates the source status.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // empty default implementation
    protected void updateDataSourceStatus() {
        // default implementation is no-op; override if appropriate
    }

    /**
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * Permissible value select box css style.
     * @return css style for the permissible value boxes.
     */
    public String getPermissibleCssStyle() {
        String styleString = "min-width:100px; vertical-align=middle;";
        return isReadOnly()
             ? styleString + GRAY_BACKGROUND + GRAY_FOREGROUND : styleString;
    }

    /**
     * @return the fieldDescriptor
     */
    public AnnotationFieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }

    /**
     * @param fieldDescriptor the fieldDescriptor to set
     */
    public void setFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the maskForm
     */
    public AnnotationMaskForm getMaskForm() {
        return maskForm;
    }

    /**
     * @param maskForm the maskForm to set
     */
    public void setMaskForm(AnnotationMaskForm maskForm) {
        this.maskForm = maskForm;
    }


}
