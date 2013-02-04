/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.ValidationException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;


/**
 * Action called to create or edit a <code>GenomicDataSourceConfiguration</code>.
 */
public class SaveSampleMappingAction extends AbstractGenomicSourceAction {

    private static final long serialVersionUID = 1L;
    
    /////////
    // Sample Mapping
    /////////
    private File sampleMappingFile;
    private String sampleMappingFileContentType;
    private String sampleMappingFileFileName;
    /////////
    // Control Mapping
    /////////
    private String controlSampleSetName;
    private File controlSampleFile;
    private String controlSampleFileContentType;
    private String controlSampleFileFileName;

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
    public String execute() {
        String returnString = SUCCESS;
        returnString = executeSampleMapping();
        if (SUCCESS.equals(returnString)) {
            returnString = executeControlSampleMapping();
        }
        return returnString;
        
    }

    private String executeControlSampleMapping() {
        if (getControlSampleFile() != null) {
            try {
                getStudyManagementService().addControlSampleSet(getGenomicSource(),
                        getControlSampleSetName(), getControlSampleFile(), getControlSampleFileFileName());
                setLastModifiedByCurrentUser();
                return SUCCESS;
            } catch (ValidationException e) {
                setControlMappingFieldError("Invalid file: " + e.getResult().getInvalidMessage());
                return INPUT;
            } catch (IOException e) {
                setControlMappingFieldError("Unexpected IO exception: " + e.getMessage());
                return INPUT;
            } 
        }
        return SUCCESS;
    }


    private String executeSampleMapping() {
        if (getSampleMappingFile() != null) {
            try {
                getStudyManagementService().mapSamples(getStudyConfiguration(), getSampleMappingFile(), 
                        getGenomicSource());
                persistFileName();
                setLastModifiedByCurrentUser();
                return SUCCESS;
            } catch (ValidationException e) {
                setSampleMappingFieldError("Invalid file: " + e.getResult().getInvalidMessage());
                return INPUT;
            } catch (IOException e) {
                setSampleMappingFieldError("Unexpected IO exception - " + e.getMessage());
                return INPUT;
            } catch (Exception e) {
                setSampleMappingFieldError("Something is very wrong in the code, please report this problem - " 
                        + e.getMessage());
                return INPUT;
            } finally {
                super.prepare(); // Reloads the genomic source if there's an exception.
            }
        }
        return SUCCESS;
    }

    private void persistFileName() {
        getGenomicSource().setSampleMappingFileName(getSampleMappingFileFileName());
        getStudyManagementService().save(getStudyConfiguration());
    }
    
    private void setSampleMappingFieldError(String errorMessage) {
        addFieldError("sampleMappingFile", errorMessage);
    }
    
    private void setControlMappingFieldError(String errorMessage) {
        addFieldError("sampleMappingFile", errorMessage);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (sampleMappingFile == null && controlSampleFile == null) {
            addActionError("File is required.");
        } 
        if (sampleMappingFile != null) {
            validateSampleMappingFile();
        }
        if (controlSampleFile != null) {
            validateControlMappingFile();
        }
        prepareValueStack();
    }

    private void validateControlMappingFile() {
        if (StringUtils.isEmpty(getControlSampleSetName())) {
            addFieldError("controlSampleSetName", "Name is required");
        } else if (getStudyConfiguration().getControlSampleSet(getControlSampleSetName()) != null) {
            addFieldError("controlSampleSetName", "Duplicate name, please enter a new name.");
        }
        if (getControlSampleFile().length() == 0) {
            setControlMappingFieldError("File is empty");
        }
    }

    private void validateSampleMappingFile() {
        if (sampleMappingFile.length() == 0) {
            setSampleMappingFieldError("File is empty");
        }
    }


    /**
     * @return the sampleMappingFile
     */
    public File getSampleMappingFile() {
        return sampleMappingFile;
    }

    /**
     * @param sampleMappingFile the sampleMappingFile to set
     */
    public void setSampleMappingFile(File sampleMappingFile) {
        this.sampleMappingFile = sampleMappingFile;
    }

    /**
     * @return the sampleMappingFileContentType
     */
    public String getSampleMappingFileContentType() {
        return sampleMappingFileContentType;
    }

    /**
     * @param sampleMappingFileContentType the sampleMappingFileContentType to set
     */
    public void setSampleMappingFileContentType(String sampleMappingFileContentType) {
        this.sampleMappingFileContentType = sampleMappingFileContentType;
    }

    /**
     * @return the sampleMappingFileFileName
     */
    public String getSampleMappingFileFileName() {
        return sampleMappingFileFileName;
    }

    /**
     * @param sampleMappingFileFileName the sampleMappingFileFileName to set
     */
    public void setSampleMappingFileFileName(String sampleMappingFileFileName) {
        this.sampleMappingFileFileName = sampleMappingFileFileName;
    }

    /**
     * @return the controlSampleSetName
     */
    public String getControlSampleSetName() {
        return controlSampleSetName;
    }

    /**
     * @param controlSampleSetName the controlSampleSetName to set
     */
    public void setControlSampleSetName(String controlSampleSetName) {
        this.controlSampleSetName = controlSampleSetName;
    }

    /**
     * @return the controlSampleFile
     */
    public File getControlSampleFile() {
        return controlSampleFile;
    }

    /**
     * @param controlSampleFile the controlSampleFile to set
     */
    public void setControlSampleFile(File controlSampleFile) {
        this.controlSampleFile = controlSampleFile;
    }

    /**
     * @return the controlSampleFileContentType
     */
    public String getControlSampleFileContentType() {
        return controlSampleFileContentType;
    }

    /**
     * @param controlSampleFileContentType the controlSampleFileContentType to set
     */
    public void setControlSampleFileContentType(String controlSampleFileContentType) {
        this.controlSampleFileContentType = controlSampleFileContentType;
    }

    /**
     * @return the controlSampleFileFileName
     */
    public String getControlSampleFileFileName() {
        return controlSampleFileFileName;
    }

    /**
     * @param controlSampleFileFileName the controlSampleFileFileName to set
     */
    public void setControlSampleFileFileName(String controlSampleFileFileName) {
        this.controlSampleFileFileName = controlSampleFileFileName;
    }
       
}
