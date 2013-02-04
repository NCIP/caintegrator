/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import java.io.File;
import java.io.IOException;

/**
 * Adds a new new clinical data source file to a study.
 */
public class AddStudyLogoAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private File studyLogoFile;
    private String studyLogoFileContentType;
    private String studyLogoFileFileName;
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")     // PMD mistakenly flagging as empty method
    protected boolean isFileUpload() {
        return true;
    }     
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String execute()  {
        try {
            getStudyManagementService().addStudyLogo(getStudyConfiguration(), getStudyLogoFile(),
                    getStudyLogoFileFileName(), getStudyLogoFileContentType());
            return SUCCESS;
        } catch (IOException e) {
            return ERROR;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (!getFieldErrors().containsKey("studyLogoFile") && getStudyLogoFile() == null) {
            addFieldError("studyLogoFile", getText("struts.messages.error.file.required", getArgs("Logo")));
        }
        prepareValueStack();
    }

    /**
     * @return the studyLogoFile
     */
    public File getStudyLogoFile() {
        return studyLogoFile;
    }

    /**
     * @param studyLogoFile the studyLogoFile to set
     */
    public void setStudyLogoFile(File studyLogoFile) {
        this.studyLogoFile = studyLogoFile;
    }

    /**
     * @return the studyLogoFileContentType
     */
    public String getStudyLogoFileContentType() {
        return studyLogoFileContentType;
    }

    /**
     * @param studyLogoFileContentType the studyLogoFileContentType to set
     */
    public void setStudyLogoFileContentType(String studyLogoFileContentType) {
        this.studyLogoFileContentType = studyLogoFileContentType;
    }

    /**
     * @return the studyLogoFileFileName
     */
    public String getStudyLogoFileFileName() {
        return studyLogoFileFileName;
    }

    /**
     * @param studyLogoFileFileName the studyLogoFileFileName to set
     */
    public void setStudyLogoFileFileName(String studyLogoFileFileName) {
        this.studyLogoFileFileName = studyLogoFileFileName;
    }

   
}
