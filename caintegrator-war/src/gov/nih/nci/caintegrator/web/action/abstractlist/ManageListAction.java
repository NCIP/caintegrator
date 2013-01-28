/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.abstractlist;

import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.application.study.Visibility;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.domain.application.AbstractList;
import gov.nih.nci.caintegrator.domain.application.GeneList;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.file.FileManager;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Provides functionality to list and add array designs.
 */
public class ManageListAction extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;
    private static final int LIST_NAME_LENGTH = 100;
    private FileManager fileManager;
    private File listFile;
    private String listFileContentType;
    private String listFileFileName;
    private ListTypeEnum listType = ListTypeEnum.GENE;
    private String listName;
    private String description;
    private boolean visibleToOther = false;
    private String subjectInputElements;
    private String geneInputElements;
    private String selectedAction;
    private String listId;
    private final Set<String> elementList = new HashSet<String>();

    private static final String CREATE_LIST_ACTION = "createList";
    private static final String CANCEL_ACTION = "cancel";
    private static final String LIST_NAME = "listName";
    private static final String LIST_FILE = "listFile";
    private static final String LIST_DESCRIPTION = "description";
    private static final String EDIT_GENE_PAGE = "editGenePage";
    private static final String EDIT_GLOBAL_GENE_PAGE = "editGlobalGenePage";
    private static final String EDIT_SUBJECT_PAGE = "editSubjectPage";
    private static final String EDIT_GLOBAL_SUBJECT_PAGE = "editGlobalSubjectPage";
    private static final String HOME_PAGE = "homePage";

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setInvalidDataBeingAccessed(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }

    /**
     * @return the Struts result.
     */
    @Override
    public String execute() {
        if (CREATE_LIST_ACTION.equals(selectedAction)) {
            try {
                return createList();
            } catch (ValidationException e) {
                addActionError(e.getMessage());
                return INPUT;
            }
        } else if (CANCEL_ACTION.equals(selectedAction)) {
            return HOME_PAGE;
        }
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        prepareValueStack();
        if (getCurrentStudy() == null) {
            setInvalidDataBeingAccessed(true);
        }
        if (CREATE_LIST_ACTION.equalsIgnoreCase(selectedAction)) {
            validateForCreate();
        } else {
            super.validate();
        }
    }

    private void validateForCreate() {
        validateListName();
        validateDescription();
        validateFileType();
        if (!hasActionErrors() && !hasFieldErrors()) {
            validateListData();
        }
    }

    private void validateFileType() {
        if (StringUtils.isNotBlank(listFileContentType) && !listFileContentType.startsWith("text/")
                && !getListFileFileName().endsWith(".csv")) {
            addFieldError(LIST_FILE, getText("struts.messages.error.content.type.not.allowed",
                    getArgs("", "", "", listFileContentType)));
        }
    }

    private void validateListName() {
        if (StringUtils.isEmpty(getListName())) {
            addFieldError(LIST_NAME, getText("struts.messages.error.name.required", getArgs("List")));
        } else if (StringUtils.length(getListName()) > LIST_NAME_LENGTH) {
            addFieldError(LIST_NAME, getText("struts.message.error.list.name.length"));
        } else if (duplicateListName()) {
            addFieldError(LIST_NAME, getText("struts.messages.error.duplicate.name", getArgs("List", getListName())));
        }
    }

    private void validateDescription() {
        if (StringUtils.length(getDescription()) > Cai2Util.MAX_LIST_DESCRIPTION_LENGTH) {
            addFieldError(LIST_DESCRIPTION, getText("struts.message.error.list.description.length"));
        }
    }

    private boolean duplicateListName() {
        if (ListTypeEnum.GENE.equals(listType)) {
            if (isDuplicateGeneListName()) {
                return true;
            }
        } else if (isDuplicateSubjectListName()) {
            return true;
        }
        return false;
    }

    private boolean isDuplicateSubjectListName() {
        return (!isVisibleToOther() && getStudySubscription().getSubjectList(getListName()) != null)
            || (isVisibleToOther() && getStudy().getStudyConfiguration().getSubjectList(getListName()) != null);
    }

    private boolean isDuplicateGeneListName() {
        return (!isVisibleToOther() && getStudySubscription().getGeneList(getListName()) != null)
            || (isVisibleToOther() && getStudy().getStudyConfiguration().getGeneList(getListName()) != null);
    }

    private void validateListData() {
        elementList.clear();
        if (ListTypeEnum.GENE.equals(listType)) {
            extractInputElements(getGeneInputElements());
        } else {
            extractInputElements(getSubjectInputElements());
        }
        extractInputElements(getListFile());
        if (elementList.isEmpty()) {
            addActionError(getText("struts.messages.error.list.nothing.to.save"));
        }
    }

    private void extractInputElements(String inputElements) {
        if (!StringUtils.isEmpty(inputElements)) {
            for (String element : inputElements.split(",")) {
                elementList.add(element.trim());
            }
        }
    }

    private void extractInputElements(File uploadFile) {
        if (uploadFile != null) {
            CSVReader reader;
            try {
                reader = new CSVReader(new FileReader(uploadFile));
                String[] elements;
                while ((elements = reader.readNext()) != null) {
                    for (String element : elements) {
                        String trimmedElement = element.trim();
                        if (trimmedElement.contains("\t")) {
                            addFieldError(LIST_FILE, getText("struts.messages.error.file.tab.should.be.comma",
                                    getArgs("")));
                            return;
                        }
                        elementList.add(element.trim());
                    }
                }
                if (elementList.isEmpty()) {
                    addFieldError(LIST_FILE, getText("struts.messages.error.file.empty", getArgs("")));
                }
            } catch (IOException e) {
                addFieldError(LIST_FILE, getText("struts.messages.error.file.read", getArgs("list")));
            }
        }
    }

    private String createList() throws ValidationException {
        AbstractList newList = (ListTypeEnum.GENE.equals(listType)) ? new GeneList() : new SubjectList();
        newList.setName(getListName());
        newList.setDescription(getDescription());
        newList.setLastModifiedDate(new Date());
        setNewListVisibility(newList);
        if (ListTypeEnum.GENE.equals(listType)) {
            getWorkspaceService().createGeneList((GeneList) newList, elementList);
            return (isVisibleToOther()) ? EDIT_GLOBAL_GENE_PAGE : EDIT_GENE_PAGE;
        } else {
            getWorkspaceService().createSubjectList((SubjectList) newList, elementList);
            return (isVisibleToOther()) ? EDIT_GLOBAL_SUBJECT_PAGE : EDIT_SUBJECT_PAGE;
        }
    }

    private void setNewListVisibility(AbstractList newList) {
        if (isVisibleToOther()) {
            newList.setVisibility(Visibility.GLOBAL);
            newList.setStudyConfiguration(getSubscription().getStudy().getStudyConfiguration());
        } else {
            newList.setVisibility(Visibility.PRIVATE);
            newList.setSubscription(getSubscription());
        }
    }

    /**
     * @return the listFile
     */
    public File getListFile() {
        return listFile;
    }

    /**
     * @param listFile the geneListFile to set
     */
    public void setListFile(File listFile) {
        this.listFile = listFile;
    }

    /**
     * @return the listFileContentType
     */
    public String getListFileContentType() {
        return listFileContentType;
    }

    /**
     * @param listFileContentType the listFileContentType to set
     */
    public void setListFileContentType(String listFileContentType) {
        this.listFileContentType = listFileContentType;
    }

    /**
     * @return the listFileFileName
     */
    public String getListFileFileName() {
        return listFileFileName;
    }

    /**
     * @param listFileFileName the listFileFileName to set
     */
    public void setListFileFileName(String listFileFileName) {
        this.listFileFileName = listFileFileName;
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
     * @return the listName
     */
    public String getListName() {
        return listName;
    }

    /**
     * @param listName the listName to set
     */
    public void setListName(String listName) {
        this.listName = listName;
    }

    /**
     * @return the listId
     */
    public String getListId() {
        return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId(String listId) {
        this.listId = listId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the elementList
     */
    public Set<String> getElementList() {
        return elementList;
    }

    /**
     * @return the study subscription
     */
    public StudySubscription getSubscription() {
        return getStudySubscription();
    }

    /**
     * @return the visibleToOther
     */
    public boolean isVisibleToOther() {
        return visibleToOther;
    }

    /**
     * @param visibleToOther the visibleToOther to set
     */
    public void setVisibleToOther(boolean visibleToOther) {
        this.visibleToOther = visibleToOther;
    }

    /**
     * @return the listType
     */
    public String getListType() {
        return listType.getValue();
    }

    /**
     * @param listType the listType to set
     */
    public void setListType(String listType) {
        this.listType = ListTypeEnum.getByValue(listType);
    }

    /**
     * @return the subjectInputElements
     */
    public String getSubjectInputElements() {
        return subjectInputElements;
    }

    /**
     * @param subjectInputElements the subjectInputElements to set
     */
    public void setSubjectInputElements(String subjectInputElements) {
        this.subjectInputElements = subjectInputElements;
    }

    /**
     * @return the geneInputElements
     */
    public String getGeneInputElements() {
        return geneInputElements;
    }

    /**
     * @param geneInputElements the geneInputElements to set
     */
    public void setGeneInputElements(String geneInputElements) {
        this.geneInputElements = geneInputElements;
    }
}
