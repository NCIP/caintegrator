/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.ajax.ISubjectDataSourceAjaxUpdater;
import gov.nih.nci.caintegrator.web.ajax.SubjectDataSourceAjaxRunner;
import gov.nih.nci.security.exceptions.CSException;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Saves basic study information.
 */
public class CopyStudyAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private ISubjectDataSourceAjaxUpdater updater;
    private static final int NAME_LENGTH = 57;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (SessionHelper.getInstance().isAuthenticated()) {
            if (getStudyConfiguration().getId() == null) {
                addActionError(getText("struts.messages.error.study.copy.id"));
                return ERROR;
            } else {
                doCopy();
            }

            // cleanup
            getDisplayableWorkspace().refresh(getWorkspaceService(), false);

            return this.hasActionErrors() ? ERROR : SUCCESS;
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

    private void doCopy() {
        StudyConfiguration original = getStudyConfiguration();
        doValidateCopy(original);
        if (this.hasFieldErrors()) {
            addCopyErrors();
        } else {
            try {
                getStudyConfiguration().setUserWorkspace(getWorkspace());
                getStudyConfiguration().setLastModifiedBy(getWorkspace());
                setStudyConfiguration(getStudyManagementService().copy(original, getStudyConfiguration()));
                doLoadCopiedSubjects();
                createStudy();
                setStudyLastModifiedByCurrentUser(null, LogEntry.getSystemLogCopy(original.getStudy()));
            } catch (ValidationException vale) {
                addActionError(vale.getResult().getInvalidMessage());
            } catch (Exception e) {
                addActionError(getText("struts.messages.error.study.copy"));
                setStudyConfiguration(original);
            }
        }
    }

    private void addCopyErrors() {
        for (List<String> errorList : this.getFieldErrors().values()) {
            for (String error : errorList) {
                addActionError(error);
            }
        }
    }

    private void doValidateCopy(StudyConfiguration original) {
        setStudyConfiguration(new StudyConfiguration());
        String name = "Copy of ".concat(StringUtils.trimToEmpty(original.getStudy()
                .getShortTitleText()));
        getStudyConfiguration().getStudy().setShortTitleText(name);
        if (!StringUtils.trimToEmpty(
                original.getStudy().getLongTitleText()).isEmpty()) {
                    getStudyConfiguration().getStudy().setLongTitleText("Copy of ".concat(StringUtils.trimToEmpty(
                original.getStudy().getLongTitleText())));
        }
        getStudyConfiguration().getStudy().setPubliclyAccessible(original.getStudy().isPubliclyAccessible());
        validate();
    }

    private void doLoadCopiedSubjects() {
        for (AbstractClinicalSourceConfiguration clinicalSource : getStudyConfiguration()
                .getClinicalConfigurationCollection()) {
                DelimitedTextClinicalSourceConfiguration clinicalSourceToLoad =
                    (DelimitedTextClinicalSourceConfiguration) clinicalSource;
                if (clinicalSourceToLoad.getStatus().equals(Status.LOADED)) {
                    clinicalSourceToLoad.setStatus(Status.PROCESSING);
                    updater.runJob(getStudyConfiguration().getId(), clinicalSourceToLoad.getId(),
                            SubjectDataSourceAjaxRunner.JobType.LOAD);
                }
            }
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
                    getText("struts.messages.error.copy.study.length.excessive"));
        } else if (getStudyManagementService().isDuplicateStudyName(getStudyConfiguration().getStudy(),
                getWorkspace().getUsername())) {
            addFieldError("study.shortTitleText", getText("struts.messages.error.duplicate.name", getArgs("Study",
                    studyName)));
        }
    }

    /**
     * @return the updater
     */
    public ISubjectDataSourceAjaxUpdater getUpdater() {
        return updater;
    }

    /**
     * @param updater the updater to set
     */
    public void setUpdater(ISubjectDataSourceAjaxUpdater updater) {
        this.updater = updater;
    }

}
