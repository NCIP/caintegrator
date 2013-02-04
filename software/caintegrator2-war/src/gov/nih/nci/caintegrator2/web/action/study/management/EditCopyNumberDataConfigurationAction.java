/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.study.CopyNumberDataConfiguration;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;


/**
 * Action called to create or edit a <code>GenomicDataSourceConfiguration</code>.
 */
public class EditCopyNumberDataConfigurationAction extends AbstractGenomicSourceAction {

    private static final long serialVersionUID = 1L;

    /**
     * Action name for edit.
     */
    public static final String EDIT_ACTION = "edit";

    /**
     * Action name for save.
     */
    public static final String SAVE_ACTION = "save";
    
    private String formAction;
    private File copyNumberMappingFile;
    private String copyNumberMappingFileContentType;
    private String copyNumberMappingFileFileName;
    private String gladUrl;
    private String caDnaCopyUrl;
    private boolean useGlad = false;
    private ConfigurationHelper configurationHelper;
    private static final String COPY_NUMBER_MAPPING_FILE = "copyNumberMappingFile";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        updateServiceUrl();
        if (getCopyNumberDataConfiguration().isCaDNACopyConfiguration()) {
            setCaDnaCopyUrl(getCopyNumberDataConfiguration().getSegmentationService().getUrl());
            setUseGlad(false);
        } else {
            setGladUrl(getCopyNumberDataConfiguration().getSegmentationService().getUrl());
            setUseGlad(true);
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
     * Called to open the copy number data configuration page.
     * 
     * @return the struts results
     */
    public String edit() {
        getCopyNumberDataConfiguration();
        setFormAction(SAVE_ACTION);
        return SUCCESS;
    }

    /**
     * Returns the copy number data configuration.
     * 
     * @return the configuration.
     */
    public CopyNumberDataConfiguration getCopyNumberDataConfiguration() {
        
        if (getGenomicSource().getCopyNumberDataConfiguration() == null) {
            CopyNumberDataConfiguration configuration = new CopyNumberDataConfiguration();
            getGenomicSource().setCopyNumberDataConfiguration(configuration);
            
            if (getUseGlad()) {
                configuration.getSegmentationService().setUrl(
                        configurationHelper.getString(ConfigurationParameter.GENE_PATTERN_URL));
            } else {
                configuration.getSegmentationService().setUrl(
                        configurationHelper.getString(ConfigurationParameter.CA_DNA_COPY_URL));
            }
            
        }
        
        return getGenomicSource().getCopyNumberDataConfiguration();
    }
    
    /**
     * Called to save the copy number mapping data configuration.
     * 
     * @return the struts results
     */
    public String save() {
        try {
            updateServiceUrl();
            getStudyManagementService().saveCopyNumberMappingFile(getGenomicSource(), getCopyNumberMappingFile(), 
                    getCopyNumberMappingFileFileName());
            getStudyManagementService().save(getStudyConfiguration());
            setLastModifiedByCurrentUser();
            return SUCCESS;
        } catch (Exception e) {
            addActionError("An unexpected error has occurred, please report this problem - " + e.getMessage());
            return INPUT;
        } 
    }

    private void updateServiceUrl() {
        if (getUseGlad()) {
            getCopyNumberDataConfiguration().getSegmentationService().setUrl(getGladUrl());
        } else {
            getCopyNumberDataConfiguration().getSegmentationService().setUrl(getCaDnaCopyUrl());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (SAVE_ACTION.equals(getFormAction())) {
            validateSave();
        }
        prepareValueStack();
    }

    private void validateSave() {
        if (copyNumberMappingFile == null) {
            addFieldError(COPY_NUMBER_MAPPING_FILE, " File is required");
        } else if (copyNumberMappingFile.length() == 0) {
            addFieldError(COPY_NUMBER_MAPPING_FILE, " File is empty");
        } else {
            validateFileFormat();
        }
        validateServiceUrl();
    }
    
    private void validateFileFormat() {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(copyNumberMappingFile));
            String[] fields;
            int lineNum = 0;
            while ((fields = reader.readNext()) != null) {
                lineNum++;
                if (fields.length != 3) {
                    addFieldError(COPY_NUMBER_MAPPING_FILE,
                            " File must have 3 columns instead of " + fields.length
                            + " on line number " + lineNum);
                    return;
                }
            }
        } catch (IOException e) {
            addFieldError(COPY_NUMBER_MAPPING_FILE, " Error reading mapping file");
        }
    }

    private void validateServiceUrl() {
        if (getUseGlad() && StringUtils.isBlank(getGladUrl())) {
            addFieldError("gladUrl", "GLAD Service URL is required.");
        } else if (!getUseGlad() && StringUtils.isBlank(getCaDnaCopyUrl())) {
            addFieldError("caDnaCopyUrl", "CaDNACopy Service URL is required.");
        }
    }

    /**
     * @return the copyNumberMappingFile
     */
    public File getCopyNumberMappingFile() {
        return copyNumberMappingFile;
    }

    /**
     * @param copyNumberMappingFile the copyNumberMappingFile to set
     */
    public void setCopyNumberMappingFile(File copyNumberMappingFile) {
        this.copyNumberMappingFile = copyNumberMappingFile;
    }

    /**
     * @return the copyNumberMappingFileContentType
     */
    public String getCopyNumberMappingFileContentType() {
        return copyNumberMappingFileContentType;
    }

    /**
     * @param copyNumberMappingFileContentType the copyNumberMappingFileContentType to set
     */
    public void setCopyNumberMappingFileContentType(String copyNumberMappingFileContentType) {
        this.copyNumberMappingFileContentType = copyNumberMappingFileContentType;
    }

    /**
     * @return the copyNumberMappingFileFileName
     */
    public String getCopyNumberMappingFileFileName() {
        return copyNumberMappingFileFileName;
    }

    /**
     * @param copyNumberMappingFileFileName the copyNumberMappingFileFileName to set
     */
    public void setCopyNumberMappingFileFileName(String copyNumberMappingFileFileName) {
        this.copyNumberMappingFileFileName = copyNumberMappingFileFileName;
    }

    /**
     * @return the action
     */
    public String getFormAction() {
        return formAction;
    }

    /**
     * @param action the action to set
     */
    public void setFormAction(String action) {
        this.formAction = action;
    }

    /**
     * @return available PCA services.
     */
    public Map<String, String> getCaDnaCopyServices() {
        return GridDiscoveryServiceJob.getGridCaDnaCopyServices();
    }

    /**
     * @return the gladUrl
     */
    public String getGladUrl() {
        return gladUrl;
    }

    /**
     * @param gladUrl the gladUrl to set
     */
    public void setGladUrl(String gladUrl) {
        this.gladUrl = gladUrl;
    }

    /**
     * @return the caDnaCopyUrl
     */
    public String getCaDnaCopyUrl() {
        return caDnaCopyUrl;
    }

    /**
     * @param caDnaCopyUrl the caDnaCopyUrl to set
     */
    public void setCaDnaCopyUrl(String caDnaCopyUrl) {
        this.caDnaCopyUrl = caDnaCopyUrl;
    }

    /**
     * @return the useGlad
     */
    public boolean getUseGlad() {
        return useGlad;
    }

    /**
     * @param useGlad the useGlad to set
     */
    public void setUseGlad(boolean useGlad) {
        this.useGlad = useGlad;
    }

    /**
     * @return the configurationHelper
     */
    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }
}
