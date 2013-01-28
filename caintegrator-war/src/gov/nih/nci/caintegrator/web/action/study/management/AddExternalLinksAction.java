/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.application.study.ValidationException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

/**
 * Adds a new new clinical data source file to a study.
 */
public class AddExternalLinksAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private static final String[] ERROR_MESSAGE_ARGS = {"External Links"};
    private File externalLinksFile;
    private String externalLinksFileFileName;
    private ExternalLinkList externalLinkList = new ExternalLinkList();

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute()  {
        try {
            externalLinkList.setFileName(externalLinksFileFileName);
            externalLinkList.setFile(externalLinksFile);
            getStudyManagementService().addExternalLinksToStudy(getStudyConfiguration(), externalLinkList);
            setStudyLastModifiedByCurrentUser(null, LogEntry.getSystemLogAdd(externalLinkList));
            return SUCCESS;
        } catch (ValidationException e) {
            addActionError(getText("struts.messages.exception.invalid.file",
                                        getArgs(e.getResult().getInvalidMessage())));
            return INPUT;
        } catch (IOException e) {
            addActionError(getText("struts.messages.exception.file.ioexception",
                                        getArgs(e.getMessage())));
            return INPUT;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (externalLinksFile == null) {
            addFieldError("externalLinksFile", getText("struts.messages.error.file.required", ERROR_MESSAGE_ARGS));
        } else if (externalLinksFile.length() == 0) {
            addFieldError("externalLinksFile", getText("struts.messages.error.file.empty", ERROR_MESSAGE_ARGS));
        }
        if (StringUtils.isBlank(externalLinkList.getName())) {
            addFieldError("externalLinkList.name", getText("struts.messages.error.name.required", ERROR_MESSAGE_ARGS));
        }
        prepareValueStack();
    }

    /**
     * @return the externalLinksFile
     */
    public File getExternalLinksFile() {
        return externalLinksFile;
    }

    /**
     * @param externalLinksFile the externalLinksFile to set
     */
    public void setExternalLinksFile(File externalLinksFile) {
        this.externalLinksFile = externalLinksFile;
    }

    /**
     * @return the externalLinksFileFileName
     */
    public String getExternalLinksFileFileName() {
        return externalLinksFileFileName;
    }

    /**
     * @param externalLinksFileFileName the externalLinksFileFileName to set
     */
    public void setExternalLinksFileFileName(String externalLinksFileFileName) {
        this.externalLinksFileFileName = externalLinksFileFileName;
    }

    /**
     * @return the externalLinkList
     */
    public ExternalLinkList getExternalLinkList() {
        return externalLinkList;
    }

    /**
     * @param externalLinkList the externalLinkList to set
     */
    public void setExternalLinkList(ExternalLinkList externalLinkList) {
        this.externalLinkList = externalLinkList;
    }

}
