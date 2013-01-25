/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.common.Cai2Util;
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
    private boolean useGlad = false;
    private String caCghCallUrl;
    private String caDnaCopyUrl;
    private Integer numberLevelCall = 3;

    private ConfigurationHelper configurationHelper;
    private static final String MAPPING_FILE = "mappingFile";
    private static final String RAND_NUM_SEED = "dnaAnalysisDataConfiguration.randomNumberSeed";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getDnaAnalysisDataConfiguration().isCaDNACopyConfiguration()
                || getDnaAnalysisDataConfiguration().isCaCGHCallConfiguration()) {
            setUseGlad(false);
        } else {
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
        if (!getStudyConfiguration().hasLoadedClinicalDataSource()) {
            addActionError(getText("struts.messages.error.study.no.subject"));
        }
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
            setParams();
            if (Status.LOADED.equals(getGenomicSource().getStatus())) {
                recreateGenomicSource();
            }
            getStudyManagementService().saveDnaAnalysisMappingFile(getGenomicSource(), getMappingFile(), 
                    getMappingFileFileName());
            getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
            getStudyManagementService().save(getStudyConfiguration());
            setStudyLastModifiedByCurrentUser(getGenomicSource(), 
                    LogEntry.getSystemLogAddCopyNumberMapping(getGenomicSource(), getMappingFileFileName()));
            return SUCCESS;
        } catch (Exception e) {
            addActionError(getText("struts.messages.exception.unexpected", getArgs(e.getMessage())));
            return INPUT;
        } 
    }

    private void setParams() {
        if (getDnaAnalysisDataConfiguration().isUseCghCall()) {
            getDnaAnalysisDataConfiguration().getSegmentationService().setUrl(getCaCghCallUrl());
        } else {
            getDnaAnalysisDataConfiguration().getSegmentationService().setUrl(getCaDnaCopyUrl());
        }
        getDnaAnalysisDataConfiguration().setNumberLevelCall(getNumberLevelCall());
    }

    /**
     * 
     */
    private void recreateGenomicSource() {
        GenomicDataSourceConfiguration genomicSource = createNewGenomicSource();
        delete();
        getStudyManagementService().addGenomicSourceToStudy(getStudyConfiguration(), genomicSource);
        setStudyLastModifiedByCurrentUser(genomicSource, LogEntry.getSystemLogLoad(genomicSource));
        setGenomicSource(genomicSource);
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
            addFieldError(MAPPING_FILE, getText("struts.messages.error.file.required"));
        } else if (mappingFile.length() == 0) {
            addFieldError(MAPPING_FILE, getText("struts.messages.error.file.empty"));
        } else if (getDnaAnalysisDataConfiguration().getRandomNumberSeed() == null) {
            addFieldError(RAND_NUM_SEED, getText("struts.messages.error.form.value.required"));
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
            int columnNumber = (ArrayDataLoadingTypeEnum.PARSED_DATA.equals(getGenomicSource().getLoadingType()))
                ? 2 : getGenomicSource().getPlatformVendor().getDnaAnalysisMappingColumns();
            while ((fields = Cai2Util.readDataLine(reader)) != null) {
                lineNum++;
                if (fields.length < columnNumber) {
                    addFieldError(MAPPING_FILE,
                            getText("struts.messages.error.invalid.file.column.length",
                                    getArgs(String.valueOf(columnNumber), String.valueOf(fields.length), 
                                    String.valueOf(lineNum))));
                    return;
                }
            }
        } catch (IOException e) {
            addFieldError(MAPPING_FILE, " Error reading mapping file");
        }
    }

    private void validateServiceUrl() {
        if (StringUtils.isBlank(getDnaAnalysisDataConfiguration().getSegmentationService().getUrl())) {
            addFieldError("dnaAnalysisDataConfiguration.segmentationService.url", 
                    getText("struts.messages.error.url.required", getArgs("CaDNACopy Service")));
        } else {
            getDnaAnalysisDataConfiguration().getSegmentationService().setUrl(
                Cai2Util.fixUrlForEditableSelect(getDnaAnalysisDataConfiguration().getSegmentationService().getUrl()));
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
     * @return available PCA services.
     */
    public Map<String, String> getCaCghCallServices() {
        return GridDiscoveryServiceJob.getGridCGHCallServices();
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
    
    /**
     * @return the caCghCallUrl
     */
    public String getCaCghCallUrl() {
        return caCghCallUrl;
    }

    /**
     * @param caCghCallUrl the caCghCallUrl to set
     */
    public void setCaCghCallUrl(String caCghCallUrl) {
        this.caCghCallUrl = caCghCallUrl;
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
     * @return the numberLevelCall
     */
    public Integer getNumberLevelCall() {
        return numberLevelCall;
    }

    /**
     * @param numberLevelCall the numberLevelCall to set
     */
    public void setNumberLevelCall(Integer numberLevelCall) {
        this.numberLevelCall = numberLevelCall;
    }
    
    /**
     * Retrieve the default css for the bioconductor service.
     * @return css to control the display of bioconductor parameters.
     */
    public String getDefaultBioconductorSelectCss() {
        return (getDnaAnalysisDataConfiguration().isUseCghCall()) ? "display: block;" : "display: none;";
    }    
}
