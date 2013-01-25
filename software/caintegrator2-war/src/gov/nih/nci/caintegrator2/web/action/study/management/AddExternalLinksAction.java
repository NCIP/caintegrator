/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.logging.api.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Adds a new new clinical data source file to a study.
 */
public class AddExternalLinksAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private File externalLinksFile;
    private String externalLinksFileFileName;
    private ExternalLinkList externalLinkList = new ExternalLinkList();
   
    /**
     * {@inheritDoc}
     */
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
            setLastModifiedByCurrentUser();
            return SUCCESS;
        } catch (ValidationException e) {
            addActionError("Invalid file: " + e.getResult().getInvalidMessage());
            return INPUT;
        } catch (IOException e) {
            addActionError("Unexpected IO exception: " + e.getMessage());
            return INPUT;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (externalLinksFile == null) {
            addActionError("External Links File is required");
        } else if (externalLinksFile.length() == 0) {
            addActionError("External Links File is empty");
        }
        if (StringUtils.isBlank(externalLinkList.getName())) {
            addActionError("Must supply a name for the External Links List");
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
