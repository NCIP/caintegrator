/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class EditAnnotationGroupAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private FileManager fileManager;
    private File annotationGroupFile;
    private String annotationGroupFileContentType;
    private String annotationGrouptFileFileName;
    private AnnotationGroup annotationGroup = new AnnotationGroup();
    private String selectedAction;
    private String groupName;
    private List<DisplayableAnnotationFieldDescriptor> displayableFields =
        new ArrayList<DisplayableAnnotationFieldDescriptor>();
    private final List<AnnotationGroup> selectableAnnotationGroups = new ArrayList<AnnotationGroup>();
    private final Map<String, AnnotationGroup> annotationGroupNameToGroupMap = new HashMap<String, AnnotationGroup>();

    private static final String CANCEL_ACTION = "cancel";
    private static final String SAVE_ACTION = "save";
    private static final String ANNOTATION_GROUP_NAME = "groupName";

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (isExistingGroup()) {
            annotationGroup = getStudyManagementService().getRefreshedEntity(annotationGroup);
            setupAnnotationGroups();
            setupDisplayableFields();
        }
    }

    private void setupAnnotationGroups() {
        selectableAnnotationGroups.clear();
        List<AnnotationGroup> sortedAnnotationGroups = getStudy().getSortedAnnotationGroups();
        for (AnnotationGroup group : sortedAnnotationGroups) {
            group = getStudyManagementService().getRefreshedEntity(group);
            selectableAnnotationGroups.add(group);
            annotationGroupNameToGroupMap.put(group.getName(), group);
        }
    }

    private void setupDisplayableFields() {
        displayableFields.clear();
        for (AnnotationFieldDescriptor fieldDescriptor : annotationGroup.getAnnotationFieldDescriptors()) {
            displayableFields.add(new DisplayableAnnotationFieldDescriptor(fieldDescriptor));
        }
        Collections.sort(displayableFields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (!CANCEL_ACTION.equals(selectedAction)) {
            prepareValueStack();
        }
        if (SAVE_ACTION.equals(selectedAction)) {
            clearErrorsAndMessages();
            validateAnnotationGroupName();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (CANCEL_ACTION.equals(selectedAction)) {
            return SUCCESS;
        } else if (SAVE_ACTION.equals(selectedAction)) {
            return save();
        }
        setGroupName(getAnnotationGroup().getName());
        return SUCCESS;
    }

    private void validateAnnotationGroupName() {
        if (StringUtils.isBlank(getGroupName())) {
            addFieldError(ANNOTATION_GROUP_NAME, getText("struts.messages.error.name.required",
                    getArgs("Annotation Group")));
        } else if (!getGroupName().equals(getAnnotationGroup().getName())
                && getStudy().getAnnotationGroup(getGroupName()) != null) {
            addFieldError(ANNOTATION_GROUP_NAME,
                    getText("struts.messages.error.duplicate.name", getArgs("Annotation Group", getGroupName())));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }

    /**
     * @return String.
     */
    public String save() {
        try {
            annotationGroup.setName(getGroupName());
            getStudyManagementService().saveAnnotationGroup(
                    annotationGroup, getStudyConfiguration(), annotationGroupFile);
            setStudyLastModifiedByCurrentUser(null,
                    LogEntry.getSystemLogSave(annotationGroup));
        } catch (ConnectionException e) {
            return errorSavingAnnotationGroup("Error connecting to caDSR: ");
        } catch (ValidationException e) {
            return errorSavingAnnotationGroup("Unable to save annotation group: " + e.getMessage());
        } catch (IOException e) {
            return errorSavingAnnotationGroup("Error reading upload file: " + e.getMessage());
        }
        return SUCCESS;
    }

    private String errorSavingAnnotationGroup(String errorMsg) {
        addActionError(errorMsg);
        annotationGroup = new AnnotationGroup();
        return ERROR;
    }

    /**
     * Saves field descriptors.
     * @return struts return value.
     */
    public String saveFieldDescriptors() {
        for (DisplayableAnnotationFieldDescriptor displayableFieldDescriptor : displayableFields) {
            if (displayableFieldDescriptor.isGroupChanged()) {
                displayableFieldDescriptor.getFieldDescriptor().switchAnnotationGroup(
                        annotationGroupNameToGroupMap.get(displayableFieldDescriptor.getAnnotationGroupName()));
            }
        }
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogSave(annotationGroup));
        getStudyManagementService().save(getStudyConfiguration());
        return SUCCESS;
    }

    /**
     * Delete an annotation grouping.
     * @return string
     */
    public String delete() {
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogDelete(annotationGroup));
        getStudyManagementService().delete(getStudyConfiguration(), annotationGroup);
        return SUCCESS;
    }

    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the annotationGroup
     */
    public AnnotationGroup getAnnotationGroup() {
        return annotationGroup;
    }

    /**
     * @param annotationGroup the annotationGroup to set
     */
    public void setAnnotationGroup(AnnotationGroup annotationGroup) {
        this.annotationGroup = annotationGroup;
    }

    /**
     * If group already exists.
     * @return t/f value.
     */
    public boolean isExistingGroup() {
        if (annotationGroup.getId() != null) {
            return true;
        }
        return false;
    }

    /**
     * @return the displayableFields
     */
    public List<DisplayableAnnotationFieldDescriptor> getDisplayableFields() {
        return displayableFields;
    }

    /**
     * @return the annotationGroupFile
     */
    public File getAnnotationGroupFile() {
        return annotationGroupFile;
    }

    /**
     * @param annotationGroupFile the annotationGroupFile to set
     */
    public void setAnnotationGroupFile(File annotationGroupFile) {
        this.annotationGroupFile = annotationGroupFile;
    }

    /**
     * @return the annotationGroupFileContentType
     */
    public String getAnnotationGroupFileContentType() {
        return annotationGroupFileContentType;
    }

    /**
     * @param annotationGroupFileContentType the annotationGroupFileContentType to set
     */
    public void setAnnotationGroupFileContentType(String annotationGroupFileContentType) {
        this.annotationGroupFileContentType = annotationGroupFileContentType;
    }

    /**
     * @return the annotationGrouptFileFileName
     */
    public String getAnnotationGrouptFileFileName() {
        return annotationGrouptFileFileName;
    }

    /**
     * @param annotationGrouptFileFileName the annotationGrouptFileFileName to set
     */
    public void setAnnotationGrouptFileFileName(String annotationGrouptFileFileName) {
        this.annotationGrouptFileFileName = annotationGrouptFileFileName;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = (groupName != null) ? groupName.trim() : groupName;
    }

    /**
     * @param displayableFields the displayableFields to set
     */
    public void setDisplayableFields(List<DisplayableAnnotationFieldDescriptor> displayableFields) {
        this.displayableFields = displayableFields;
    }


    /**
     * @return the selectableAnnotationGroups
     */
    public List<AnnotationGroup> getSelectableAnnotationGroups() {
        return selectableAnnotationGroups;
    }
}
