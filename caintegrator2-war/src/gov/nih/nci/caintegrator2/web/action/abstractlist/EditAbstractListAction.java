/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides functionality to list and add array designs.
 */
public abstract class EditAbstractListAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    private static final int LIST_NAME_LENGTH = 100;
    private AbstractList abstractList;
    private String listOldName = "";
    private String listName = "";
    private String selectedAction;
    private boolean creationSuccess = false;
    private boolean editOn = true;
    private boolean visibleToOther = false;;
    private boolean globalList = false;

    static final String EDIT_ACTION = "editList";
    static final String EDIT_GLOBAL_ACTION = "editGlobalList";
    static final String DELETE_ACTION = "deleteList";
    static final String CANCEL_ACTION = "cancel";
    static final String SAVE_ACTION = "saveList";
    static final String LIST_NAME = "listName";
    static final String HOME_PAGE = "homePage";
    static final String EDIT_PAGE = "editPage";
    static final String EDIT_GLOBAL_PAGE = "editGlobalPage";

    /**
     * @return the Struts result.
     */
    @Override
    public String execute() {
        if (EDIT_ACTION.equals(selectedAction)) {
            setGlobalList(false);
            abstractList = getAbstractList(getListName(), isGlobalList());
            setOpenList(listName);
            return editList();
        } else if (EDIT_GLOBAL_ACTION.equals(selectedAction)) {
            setGlobalList(true);
            abstractList = getAbstractList(getListName(), isGlobalList());
            setOpenGlobalList(listName);
            return editGlobalList();
        } else if (DELETE_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return deleteList();
        } else if (SAVE_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return saveList();
        } else if (CANCEL_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return HOME_PAGE;
        }
        return SUCCESS;
    }

    abstract AbstractList getAbstractList(String name, boolean isGlobal);
    abstract void setOpenList(String name);
    abstract void setOpenGlobalList(String name);

    /**
     * Go to the edit page.
     * @return the Struts result.
     */
    public String editList() {
        setListOldName(getListName());
        setVisibleToOther(Visibility.GLOBAL.equals(getAbstractList().getVisibility()));
        return SUCCESS;
    }

    /**
     * Go to the edit page.
     * @return the Struts result.
     */
    public String editGlobalList() {
        setListOldName(getListName());
        setVisibleToOther(Visibility.GLOBAL.equals(getAbstractList().getVisibility()));
        return SUCCESS;
    }

    /**
     * Rename the subject list.
     * @return the Struts result.
     */
    public String saveList() {
        abstractList = getAbstractList(listOldName, isGlobalList());
        if (abstractList != null) {
            abstractList.setName(listName);
            abstractList.setVisibility(isVisibleToOther() ? Visibility.GLOBAL : null);
            abstractList.setLastModifiedDate(new Date());
            saveAbstractList();
            listOldName = listName;
            return isVisibleToOther() ? EDIT_GLOBAL_PAGE : EDIT_PAGE;
        } else {
            listNoLongerAvailable();
            return SUCCESS;
        }
    }

    private void saveAbstractList() {
        if (isVisibleToOther() == isGlobalList()) {
            getWorkspaceService().saveUserWorkspace(getWorkspace());
        } else if (isVisibleToOther()) {
            abstractList.setStudyConfiguration(getSubscription().getStudy().getStudyConfiguration());
            getWorkspaceService().makeListGlobal(abstractList);
        } else {
            abstractList.setSubscription(getSubscription());
            getWorkspaceService().makeListPrivate(abstractList);
        }

    }

    /**
     * Delete the subject list.
     * @return the Struts result.
     */
    public String deleteList() {
        abstractList = getAbstractList(listName, isGlobalList());
        if (abstractList != null) {
            getWorkspaceService().deleteAbstractList(getSubscription(), abstractList);
            return HOME_PAGE;
        } else {
            listNoLongerAvailable();
            return SUCCESS;
        }
    }

    private void listNoLongerAvailable() {
        addActionError(getText("struts.messages.error.list.unavailable", getArgs(listOldName)));
        editOn = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if (creationSuccess) {
            addActionMessage(getCreationSuccessfulMessage());
        }
        if (EDIT_ACTION.equals(selectedAction)) {
            setGlobalList(false);
            validateList(isGlobalList());
        } else if (EDIT_GLOBAL_ACTION.equals(selectedAction)) {
            validateEditGlobalAction();
        } else if (SAVE_ACTION.equalsIgnoreCase(selectedAction)) {
            validateListName();
        }
    }

    private void validateEditGlobalAction() {
        if (!isStudyManager()) {
            setInvalidDataBeingAccessed(true);
        } else {
            setGlobalList(true);
            validateList(isGlobalList());
        }
    }

    /**
     * Creation message (overridden by subclasses).
     * @return message.
     */
    protected String getCreationSuccessfulMessage() {
        return getText("struts.messages.list.successfully.created");
    }

    private void validateList(boolean isGlobal) {
        abstractList = getAbstractList(getListName(), isGlobal);
        if (abstractList == null) {
            addActionError(getText("struts.messages.error.list.unavailable", getArgs(getListName())));
            editOn = false;
        }
    }

    private void validateListName() {
        if (StringUtils.isEmpty(getListName())) {
            addFieldError(LIST_NAME, getText("struts.messages.error.name.required", getArgs("List")));
        } else if (StringUtils.length(getListName()) > LIST_NAME_LENGTH) {
            addFieldError(LIST_NAME, getText("struts.message.error.list.name.length"));
        } else if ((!listOldName.equals(listName) && getAbstractList(listName, isVisibleToOther()) != null)
                || (isVisibleToOther() != isGlobalList() && getAbstractList(listName, isVisibleToOther()) != null)) {
            addFieldError(LIST_NAME, getText("struts.messages.error.duplicate.name", getArgs("List", getListName())));
            abstractList = getAbstractList(listOldName, isGlobalList());
            setListName(listOldName);
        }
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
     * @return the study subscription
     */
    public StudySubscription getSubscription() {
        return getStudySubscription();
    }

    /**
     * @return the listOldName
     */
    public String getListOldName() {
        return listOldName;
    }

    /**
     * @param listOldName the listOldName to set
     */
    public void setListOldName(String listOldName) {
        this.listOldName = listOldName;
    }

    /**
     * @return the editOn
     */
    public boolean isEditOn() {
        return editOn;
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
     * @return the globalList
     */
    public boolean isGlobalList() {
        return globalList;
    }

    /**
     * @param globalList the globalList to set
     */
    public void setGlobalList(boolean globalList) {
        this.globalList = globalList;
    }

    /**
     * @return the abstractList
     */
    public AbstractList getAbstractList() {
        return abstractList;
    }

    /**
     * @return the creationSuccess
     */
    public boolean isCreationSuccess() {
        return creationSuccess;
    }

    /**
     * @param creationSuccess the creationSuccess to set
     */
    public void setCreationSuccess(boolean creationSuccess) {
        this.creationSuccess = creationSuccess;
    }
}
