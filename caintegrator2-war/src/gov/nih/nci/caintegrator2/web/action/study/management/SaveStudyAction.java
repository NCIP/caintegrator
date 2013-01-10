/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.security.exceptions.CSException;

import org.apache.commons.lang3.StringUtils;

/**
 * Saves basic study information.
 */
public class SaveStudyAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;

    private static final int NAME_LENGTH = 50;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (SessionHelper.getInstance().isAuthenticated()) {
            if (getStudyConfiguration().getId() == null) {
                return createStudy();
            } else {
                cleanStudyName();
                setStudyLastModifiedByCurrentUser(null,
                        LogEntry.getSystemLogSave(getStudyConfiguration().getStudy()));
                getStudyManagementService().save(getStudyConfiguration());
            }
            return SUCCESS;
        } else {
            addActionError(getText("struts.messages.error.unauthenticated.user"));
            return ERROR;
        }
    }

    private String createStudy() {
        getStudyConfiguration().setUserWorkspace(getWorkspace());
        getStudyConfiguration().setLastModifiedBy(getWorkspace());
        cleanStudyName();
        getStudyManagementService().save(getStudyConfiguration());
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogCreate(getStudyConfiguration().getStudy()));
        getDisplayableWorkspace().setCurrentStudyConfiguration(getStudyConfiguration());
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        try {
            getStudyManagementService().createProtectionElement(getStudyConfiguration());
        } catch (CSException e) {
            addActionError(getText("struts.messages.error.csm.study.instance.level",
                    getArgs(getStudyConfiguration().getStudy().getShortTitleText())));
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        String studyName = getStudyConfiguration().getStudy().getShortTitleText();
        if (StringUtils.isEmpty(studyName)) {
            addFieldError("study.shortTitleText", getText("struts.messages.error.name.required",
                    getArgs("Study")));
        } else if (studyName.length() > NAME_LENGTH) {
            addFieldError("study.shortTitleText",
                    getText("struts.messages.error.study.length.excessive"));
        } else if (getStudyManagementService().isDuplicateStudyName(getStudyConfiguration().getStudy(),
                getWorkspace().getUsername())) {
            addFieldError("study.shortTitleText", getText("struts.messages.error.duplicate.name", getArgs("Study",
                    studyName)));
        }
    }

}
