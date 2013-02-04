/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.AnnotationValueUtil;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Action used to edit the type and annotation of an imaging file column by a Study Manager.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" }) // See selectDataElement()
public abstract class AbstractFileColumnAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;

    private static final String ANNOTATION_TYPE = "Annotation";
    private static final String IDENTIFIER_TYPE = "Identifier";
    private static final String TIMEPOINT_TYPE = "Timepoint";
    private static final String GRAY_BACKGROUND = "background-color:#e9e9e9;";
    private static final String GRAY_FOREGROUND = "color:#999999;";
    private static final String[] COLUMN_TYPES = new String[] {ANNOTATION_TYPE, IDENTIFIER_TYPE};
    
    private FileColumn fileColumn = new FileColumn();
    private boolean readOnly;
    private boolean cancelEnabled;
    private List<String> availableUpdateList = new ArrayList<String>();
    private List<String> permissibleUpdateList = new ArrayList<String>();
    private int dataElementIndex;
    private int definitionIndex;
    private String columnType;
    private String sourceId;

    private void clearCacheMemory() {
        columnType = null;
        getDisplayableWorkspace().getDataElementSearchObject().clear();
        availableUpdateList.clear();
        permissibleUpdateList.clear();
    }
    
    private Collection<AbstractAnnotationValue> getAnnotationValueCollection() {
        return fileColumn.getFieldDescriptor().getDefinition().getAnnotationValueCollection();
    }
    
    /**
     * Refreshes the current source configuration.
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getFileColumn().getId() != null) {
            setFileColumn(getStudyManagementService().getRefreshedStudyEntity(getFileColumn()));
            if (fileColumn.getFieldDescriptor() != null && fileColumn.getFieldDescriptor().getDefinition() != null) {
                fileColumn.getFieldDescriptor().setDefinition(getStudyManagementService().getRefreshedStudyEntity(
                                fileColumn.getFieldDescriptor().getDefinition()));
                HibernateUtil.loadCollections(fileColumn.getFieldDescriptor().getDefinition());
            }
        }
        setReadOnly(true);
        cancelEnabled = true;
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
    public String editFileColumn() {
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
    public String saveColumnType() {
        try {
            updateColumnType();
        } catch (ValidationException e) {
            addActionError(e.getMessage());
        }
        if (isColumnTypeAnnotation() && getFileColumn().getFieldDescriptor() == null) {
            getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
            getFileColumn().getFieldDescriptor().setName(getFileColumn().getName());
        }
        getStudyManagementService().save(getStudyConfiguration());
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
        FileColumn originalFileColumn = getFileColumn();
        AnnotationDefinition definitionToUse = getStudyManagementService().
                        getRefreshedStudyEntity(getDefinitions().get(getDefinitionIndex()));
        try {
            getStudyManagementService().setDefinition(getStudyConfiguration().getStudy(),
                                                  getFileColumn(), 
                                                  definitionToUse,
                                                  getEntityType());
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            setFileColumn(originalFileColumn);
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
        if (getFileColumn().getFieldDescriptor().getDefinition() != null) {
            annotationType = 
                    getFileColumn().getFieldDescriptor().getDefinition().getDataType();
        }
        getStudyManagementService().createDefinition(getFileColumn().getFieldDescriptor(), 
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
        return SUCCESS;
    }
    
    /**
     * Selects an existing CaDSR data element as the definition for a column.
     * 
     * @return the Struts result.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // Null Checks and Try/Catch  
    public String selectDataElement() {
        FileColumn originalFileColumn = getFileColumn();
        try {
            getStudyManagementService().setDataElement(getFileColumn(), 
                                                       retrieveSelectedDataElement(),
                                                       getStudyConfiguration().getStudy(),
                                                       getEntityType(),
                                                       getKeywordsForSearch());
        } catch (ConnectionException e) {
            addActionError(e.getMessage());
            setFileColumn(originalFileColumn);
            prepare();
            return ERROR;
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            setFileColumn(originalFileColumn);
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
            updateColumnType();
            if (isPermissibleOn() && !isFromCadsr()) {
                updatePermissible();
            }
            if (ANNOTATION_TYPE.equals(columnType) 
                 && getFileColumn().getFieldDescriptor().getDefinition() != null) {
                getStudyManagementService().save(getFileColumn().getFieldDescriptor().getDefinition());
            }
        } catch (ValidationException e) {
            addActionError(e.getMessage());
            clearCacheMemory();
            fileColumn.getFieldDescriptor().setDefinition(getStudyManagementService().getRefreshedStudyEntity(
                    fileColumn.getFieldDescriptor().getDefinition()));
            setReadOnly(false);
            return ERROR;
        } 
        return SUCCESS;
    }

    /**
     * @return the fileColumn
     */
    public FileColumn getFileColumn() {
        return fileColumn;
    }

    /**
     * @param fileColumn the fileColumn to set
     */
    public void setFileColumn(FileColumn fileColumn) {
        this.fileColumn = fileColumn;
    }

    /**
     * @return the columnType
     */
    public String getColumnType() {
        if (this.columnType == null) {
            if (getFileColumn().isIdentifierColumn()) {
                columnType = IDENTIFIER_TYPE;
            } else if (getFileColumn().isTimepointColumn()) {
                columnType = TIMEPOINT_TYPE;
            } else {
                columnType = ANNOTATION_TYPE;
            }
        }
        return columnType;
    }

    /**
     * @param columnType the columnType to set
     */
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    /**

     * 
     * @return if the ColumnType is ANNOTATION_TYPE.
     */
    public boolean isColumnTypeAnnotation() {
        if (getColumnType().equals(ANNOTATION_TYPE)) {
            return true;
        } 
        return false;
    }


    private void updateColumnType() throws ValidationException {
        if (IDENTIFIER_TYPE.equals(columnType)) {
            getFileColumn().checkValidIdentifierColumn();
            setAnnotationColumn(getFileColumn().getAnnotationFile().getIdentifierColumn());
            getFileColumn().getAnnotationFile().setIdentifierColumn(getFileColumn());
        } else if (TIMEPOINT_TYPE.equals(columnType)) {
            setAnnotationColumn(getFileColumn().getAnnotationFile().getTimepointColumn());
            getFileColumn().getAnnotationFile().setTimepointColumn(getFileColumn());
        } else if (ANNOTATION_TYPE.equals(columnType)) {
            getFileColumn().makeAnnotationColumn();
        }
    }
    
    private void setAnnotationColumn(FileColumn annotationColumn) {
        if (annotationColumn != null) {
            annotationColumn.makeAnnotationColumn();
        }
    }

    /**
     * @return the columnTypes
     */
    @SuppressWarnings("PMD")    // Prevent internal array exposure warning
    public String[] getColumnTypes() {
        return COLUMN_TYPES;
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
                && fileColumn.getFieldDescriptor() != null
                && fileColumn.getFieldDescriptor().getDefinition() != null) {
            return true;
        } 
        return false;
    }
    
    /**
     * @return the availableValue
     * @throws ValidationException Invalid data
    */
    @SuppressWarnings("PMD.EmptyCatchBlock") // See message inside catch block.
   public Set<String> getAvailableValues() throws ValidationException {
       List<String> fileDataValues = fileColumn.getAnnotationFile() != null
           ? fileColumn.getDataValues() : new ArrayList<String>();
       if (AnnotationTypeEnum.DATE.getValue().equalsIgnoreCase(getDefinitionType())) {
           try {
               fileDataValues = DateUtil.toString(fileDataValues);
           } catch (ParseException e) {
               // noop - if it doesn't fit the date format just let it keep going.  
               // This function is for JSP display so it can't fail.
           }
       }
       return AnnotationValueUtil.getAdditionalValue(getAnnotationValueCollection(),
           fileDataValues, getPermissibleValues());
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
        return fileColumn.getFieldDescriptor().getDefinition().getPermissibleValueCollection();
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
        if (getFileColumn() != null 
            && getFileColumn().getFieldDescriptor() != null 
            && getFileColumn().getFieldDescriptor().getDefinition() != null 
            && getFileColumn().getFieldDescriptor().getDefinition().getCommonDataElement().getPublicID() != null) {
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return the identifier name.
     */
    public String getIdentifier() {
        FileColumn identifier = getFileColumn().getAnnotationFile().getIdentifierColumn();
        return identifier == null ? "" : identifier.getName();
    }
    
    /**
     * 
     * @return the column definition type.
     */
    public String getDefinitionType() {
        return getFileColumn().getFieldDescriptor().getDefinition().getDataType().getValue();
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
    
    /**
     * Entity type for the action.
     * @return the entity type for the action. 
     */
    public abstract EntityTypeEnum getEntityType();
    
    /**
     * The string for the SaveColumnType action.
     * @return struts action string.
     */
    public abstract String getSaveColumnTypeAction();
    
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
        return isFromCadsr() 
             ? styleString + GRAY_BACKGROUND + GRAY_FOREGROUND : styleString; 
    }
}
