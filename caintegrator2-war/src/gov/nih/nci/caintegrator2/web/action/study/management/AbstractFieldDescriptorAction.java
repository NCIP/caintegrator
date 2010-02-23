/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" }) // See selectDataElement()
public abstract class AbstractFieldDescriptorAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;

    private static final String ANNOTATION_TYPE = "Annotation";
    private static final String IDENTIFIER_TYPE = "Identifier";
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
    @SuppressWarnings("PMD.CyclomaticComplexity") // Null Checks and Try/Catch  
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
            updateColumnType();
            if (isPermissibleOn() && !isFromCadsr()) {
                updatePermissible();
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
        getStudyManagementService().updateFieldDescriptorType(fieldDescriptor, type);
    }

    /**
     * @return the columnTypes
     */
    @SuppressWarnings("PMD")    // Prevent internal array exposure warning
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
    @SuppressWarnings("PMD.EmptyCatchBlock")
    // See message inside catch block.
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
    
    /**
     * Entity type for the action.
     * @return the entity type for the action. 
     */
    public abstract EntityTypeEnum getEntityType();
    
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
        return isFromCadsr() 
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
}
