/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
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
public class EditDnaAnalysisDataConfigurationAction extends AbstractGenomicSourceAction {

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
    private File mappingFile;
    private String mappingFileContentType;
    private String mappingFileFileName;
    private String gladUrl;
    private String caDnaCopyUrl;
    private boolean useGlad = false;
    private ConfigurationHelper configurationHelper;
    private static final String MAPPING_FILE = "mappingFile";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        updateServiceUrl();
        if (getDnaAnalysisDataConfiguration().isCaDNACopyConfiguration()) {
            setCaDnaCopyUrl(getDnaAnalysisDataConfiguration().getSegmentationService().getUrl());
            setUseGlad(false);
        } else {
            setGladUrl(getDnaAnalysisDataConfiguration().getSegmentationService().getUrl());
            setUseGlad(true);
        }
    }

    /**
     * Called to open the dna analysis data configuration page.
     * 
     * @return the struts results
     */
    public String edit() {
        getDnaAnalysisDataConfiguration();
        setFormAction(SAVE_ACTION);
        return SUCCESS;
    }

    /**
     * Returns the dna analysis data configuration.
     * 
     * @return the configuration.
     */
    public DnaAnalysisDataConfiguration getDnaAnalysisDataConfiguration() {
        
        if (getGenomicSource().getDnaAnalysisDataConfiguration() == null) {
            DnaAnalysisDataConfiguration configuration = new DnaAnalysisDataConfiguration();
            getGenomicSource().setDnaAnalysisDataConfiguration(configuration);
            
            if (getUseGlad()) {
                configuration.getSegmentationService().setUrl(
                        configurationHelper.getString(ConfigurationParameter.GENE_PATTERN_URL));
            } else {
                configuration.getSegmentationService().setUrl(
                        configurationHelper.getString(ConfigurationParameter.CA_DNA_COPY_URL));
            }
            
        }
        
        return getGenomicSource().getDnaAnalysisDataConfiguration();
    }
    
    /**
     * Called to save the dna analysis mapping data configuration.
     * 
     * @return the struts results
     */
    public String save() {
        try {
            updateServiceUrl();
            getStudyManagementService().saveDnaAnalysisMappingFile(getGenomicSource(), getMappingFile(), 
                    getMappingFileFileName());
            getStudyManagementService().save(getStudyConfiguration());
            setStudyLastModifiedByCurrentUser(getGenomicSource(), 
                    LogEntry.getSystemLogSave(getGenomicSource()));
            return SUCCESS;
        } catch (Exception e) {
            addActionError("An unexpected error has occurred, please report this problem - " + e.getMessage());
            return INPUT;
        } 
    }

    private void updateServiceUrl() {
        if (getUseGlad()) {
            getDnaAnalysisDataConfiguration().getSegmentationService().setUrl(getGladUrl());
        } else {
            getDnaAnalysisDataConfiguration().getSegmentationService().setUrl(getCaDnaCopyUrl());
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
        if (mappingFile == null) {
            addFieldError(MAPPING_FILE, " File is required");
        } else if (mappingFile.length() == 0) {
            addFieldError(MAPPING_FILE, " File is empty");
        } else {
            validateFileFormat();
        }
        validateServiceUrl();
    }
    
    private void validateFileFormat() {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(mappingFile));
            String[] fields;
            int lineNum = 0;
            int columnNumber = (getGenomicSource().isSingleDataFile())
                ? 3
                : PlatformVendorEnum.getByValue(getGenomicSource().getPlatformVendor()).getDnaAnalysisMappingColumns();
            while ((fields = reader.readNext()) != null) {
                lineNum++;
                if (fields.length != columnNumber) {
                    addFieldError(MAPPING_FILE,
                            " File must have " + columnNumber + " columns instead of " + fields.length
                            + " on line number " + lineNum);
                    return;
                }
            }
        } catch (IOException e) {
            addFieldError(MAPPING_FILE, " Error reading mapping file");
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
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }

    /**
     * @return the mappingFile
     */
    public File getMappingFile() {
        return mappingFile;
    }

    /**
     * @param mappingFile the mappingFile to set
     */
    public void setMappingFile(File mappingFile) {
        this.mappingFile = mappingFile;
    }

    /**
     * @return the mappingFileContentType
     */
    public String getMappingFileContentType() {
        return mappingFileContentType;
    }

    /**
     * @param mappingFileContentType the mappingFileContentType to set
     */
    public void setMappingFileContentType(String mappingFileContentType) {
        this.mappingFileContentType = mappingFileContentType;
    }

    /**
     * @return the mappingFileFileName
     */
    public String getMappingFileFileName() {
        return mappingFileFileName;
    }

    /**
     * @param mappingFileFileName the mappingFileFileName to set
     */
    public void setMappingFileFileName(String mappingFileFileName) {
        this.mappingFileFileName = mappingFileFileName;
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
