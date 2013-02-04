/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

import java.io.File;
import java.io.IOException;

/**
 * Adds a new new clinical data source file to a study.
 */
public class AddClinicalFileAction extends AbstractClinicalSourceAction {

    private static final long serialVersionUID = 1L;
    private static final String[] ERROR_MESSAGE_ARGS = {"Subject Annotation"};
    private File clinicalFile;
    private String clinicalFileContentType;
    private String clinicalFileFileName;
    private boolean createNewAnnotationDefinition = false;

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
            DelimitedTextClinicalSourceConfiguration clinicalSource = 
                getStudyManagementService().addClinicalAnnotationFile(getStudyConfiguration(), getClinicalFile(), 
                        getClinicalFileFileName(), createNewAnnotationDefinition);
            setClinicalSource(clinicalSource);
            setStudyLastModifiedByCurrentUser(clinicalSource, 
                    LogEntry.getSystemLogAddSubjectAnnotationFile(getClinicalFileFileName()));
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
        if (clinicalFile == null) {
            addActionError(getText("struts.messages.error.file.required", ERROR_MESSAGE_ARGS));
        } else if (clinicalFile.length() == 0) {
            addActionError(getText("struts.messages.error.file.empty", ERROR_MESSAGE_ARGS));
        }
        prepareValueStack();
    }
    
    /**
     * @return the clinicalFile
     */
    public File getClinicalFile() {
        return clinicalFile;
    }

    /**
     * @param clinicalFile the clinicalFile to set
     */
    public void setClinicalFile(File clinicalFile) {
        this.clinicalFile = clinicalFile;
    }

    /**
     * @return the clinicalFileContentType
     */
    public String getClinicalFileContentType() {
        return clinicalFileContentType;
    }

    /**
     * @param clinicalFileContentType the clinicalFileContentType to set
     */
    public void setClinicalFileContentType(String clinicalFileContentType) {
        this.clinicalFileContentType = clinicalFileContentType;
    }

    /**
     * @return the clinicalFileFileName
     */
    public String getClinicalFileFileName() {
        return clinicalFileFileName;
    }

    /**
     * @param clinicalFileFileName the clinicalFileFileName to set
     */
    public void setClinicalFileFileName(String clinicalFileFileName) {
        this.clinicalFileFileName = clinicalFileFileName;
    }
   
    /**
     * @return the createNewAnnotationDefinition
     */
    public boolean isCreateNewAnnotationDefinition() {
        return createNewAnnotationDefinition;
    }

    /**
     * @param createNewAnnotationDefinition the createNewAnnotationDefinition to set
     */
    public void setCreateNewAnnotationDefinition(boolean createNewAnnotationDefinition) {
        this.createNewAnnotationDefinition = createNewAnnotationDefinition;
    }
    
}
