/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * Saves basic study information.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") //Validate the study name
public class SaveStudyAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    
    private static final int NAME_LENGTH = 50;
    private static final int DESC_LENGTH = 200;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (SessionHelper.getInstance().isAuthenticated()) {
            if (getStudyConfiguration().getId() == null) {
                return createStudy();
            } else {
                setLastModifiedByCurrentUser();
                getStudyManagementService().save(getStudyConfiguration());
            }
            return SUCCESS;
        } else {
            addActionError("User is unauthenticated");
            return ERROR;
        }
    }

    private String createStudy() {
        getStudyConfiguration().setUserWorkspace(getWorkspace());
        getStudyConfiguration().setLastModifiedBy(getWorkspace());
        getStudyConfiguration().setLastModifiedDate(new Date());
        getStudyManagementService().save(getStudyConfiguration());
        getDisplayableWorkspace().setCurrentStudyConfiguration(getStudyConfiguration());
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        try {
            getStudyManagementService().createProtectionElement(getStudyConfiguration());
        } catch (CSException e) {
            addActionError("Problem trying to create instance level security on Study " 
                    + getStudyConfiguration().getStudy().getShortTitleText());
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity") // Validate the study name
    public void validate() {
        String studyName = getStudyConfiguration().getStudy().getShortTitleText();
        if (StringUtils.isEmpty(studyName)) {
            addFieldError("study.shortTitleText", "Study Name is required");
        } else if (studyName.length() > NAME_LENGTH) {
            addFieldError("study.shortTitleText",
                    "Study name exceeds maximum length of 50 characters, please shorten it.");
        } else if (getStudyManagementService().isDuplicateStudyName(getStudyConfiguration().getStudy(),
                getWorkspace().getUsername())) {
            addFieldError("study.shortTitleText", "There is already a study named '" + studyName
                    + "', please use a different name.");
        }
        if (!StringUtils.isEmpty(getStudyConfiguration().getStudy().getLongTitleText())
                && getStudyConfiguration().getStudy().getLongTitleText().length() > DESC_LENGTH) {
            addFieldError("study.longTitleText",
                    "Study description exceeds maximum length of 200 characters, please shorten it.");
        }
    }

}
