/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.application.study.ValidationException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action called to create or edit a <code>GenomicDataSourceConfiguration</code>.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
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
    public void prepare() {
        super.prepare();
        try {
            this.getStudyManagementService().checkForSampleUpdates(getGenomicSource());
        } catch (Exception e) {
            LOG.error("Error retrieving sample update information.", e);
        }
    }

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
                setStudyLastModifiedByCurrentUser(getGenomicSource(),
                        LogEntry.getSystemLogAddControlSampleMappingFile(getGenomicSource(),
                                getControlSampleFileFileName()));
                return SUCCESS;
            } catch (ValidationException e) {
                setControlMappingFieldError(getText("struts.messages.exception.invalid.file",
                        getArgs(e.getResult().getInvalidMessage())));
                return INPUT;
            } catch (IOException e) {
                setControlMappingFieldError(getText("struts.messages.exception.file.ioexception",
                        getArgs(e.getMessage())));
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
                setStudyLastModifiedByCurrentUser(getGenomicSource(),
                        LogEntry.getSystemLogAddSampleMappingFile(getGenomicSource(), getSampleMappingFileFileName()));
                return SUCCESS;
            } catch (ValidationException e) {
                setSampleMappingFieldError(getText("struts.messages.exception.invalid.file",
                        getArgs(e.getResult().getInvalidMessage())));
                return INPUT;
            } catch (IOException e) {
                setSampleMappingFieldError(getText("struts.messages.exception.file.ioexception",
                        getArgs(e.getMessage())));
                return INPUT;
            } catch (Exception e) {
                setSampleMappingFieldError(getText("struts.messages.exception.unexpected", getArgs(e.getMessage())));
                return INPUT;
            } finally {
                prepare(); // Reloads the genomic source if there's an exception.
            }
        }
        return SUCCESS;
    }

    private void persistFileName() {
        try {
            getGenomicSource().setSampleMappingFileName(getSampleMappingFileFileName());
            if (!ArrayDataLoadingTypeEnum.PARSED_DATA.equals(getGenomicSource().getLoadingType())) {
                getStudyManagementService().saveSampleMappingFile(getGenomicSource(), getSampleMappingFile(),
                        getSampleMappingFileFileName());
            }
            getStudyManagementService().save(getStudyConfiguration());
        } catch (Exception e) {
            addActionError(getText("struts.messages.exception.unexpected",
                    getArgs(e.getMessage())));
        }
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
            addActionError(getText("struts.messages.error.file.required", getArgs("")));
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
            addFieldError("controlSampleSetName", getText("struts.messages.error.name.required",
                    getArgs("")));
        } else if (getStudyConfiguration().getControlSampleSet(getControlSampleSetName()) != null) {
            addFieldError("controlSampleSetName", getText("struts.messages.error.duplicate.name",
                    getArgs("Control Set", getControlSampleSetName())));
        }
        if (getControlSampleFile().length() == 0) {
            setControlMappingFieldError(getText("struts.messages.error.file.empty", getArgs("")));
        }
    }

    private void validateSampleMappingFile() {
        if (sampleMappingFile.length() == 0) {
            setSampleMappingFieldError(getText("struts.messages.error.file.empty", getArgs("")));
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
