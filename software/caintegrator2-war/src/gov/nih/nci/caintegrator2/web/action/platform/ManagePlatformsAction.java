/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.platform;

import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixCnPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentCnPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.study.management.AbstractCai2ManagementAction;
import gov.nih.nci.caintegrator2.web.ajax.IPlatformDeploymentAjaxUpdater;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Provides functionality to list and add array designs.
 */
public class ManagePlatformsAction extends AbstractCai2ManagementAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ManagePlatformsAction.class);
    private ArrayDataService arrayDataService;
    private FileManager fileManager;
    private File platformFile;
    private String platformFileContentType;
    private String platformFileFileName;
    private String platformName;
    private String platformType = PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue();
    private String platformChannelType = PlatformChannelTypeEnum.ONE_COLOR.getValue();
    private String selectedAction;
    private IPlatformDeploymentAjaxUpdater ajaxUpdater;
    private String platformConfigurationId;
    private String selectedPlatformType;
    private String selectedPlatformChannelType;

    private static final String CREATE_PLATFORM_ACTION = "createPlatform";
    private static final String ADD_FILE_ACTION = "addAnnotationFile";
    private static final String PLATFORM_NAME = "platformName";
    private static final String PLATFORM_FILE = "platformFile";
    private static final String DISPLAY_NONE = "display: none;";
    private static final String DISPLAY_BLOCK = "display: block;";
    private static final String AFFYMETRIX_PLATFORM_EXTENSION = ".csv";

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (!SessionHelper.getInstance().isPlatformManager()) {
            setAuthorizedPage(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return ADD_FILE_ACTION.equalsIgnoreCase(selectedAction)
            || CREATE_PLATFORM_ACTION.equalsIgnoreCase(selectedAction);
    }

    /**
     * @return the Struts result.
     */
    @Override
    public String execute() {
        getPlatformForm().clear();
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if (CREATE_PLATFORM_ACTION.equalsIgnoreCase(selectedAction)) {
            checkPlatformParameters();
            prepareValueStack();
        } else if (ADD_FILE_ACTION.equalsIgnoreCase(selectedAction)) {
            if (checkAddedPlatformFile()) {
                checkAnnotationFileExtension();
            }
            prepareValueStack();
        } else {
            super.validate();
        }
    }

    /**
     * @return the Struts result.
     */
    public String updatePlatform() {
        if (platformConfigurationId == null) {
            addActionError(getText("struts.messages.error.platform.no.id"));
            return ERROR;
        }
        if (!SessionHelper.getInstance().isAuthenticated()) {
            addActionError(getText("struts.messages.error.unauthenticated.user"));
            return ERROR;
        }
        if ("delete".equalsIgnoreCase(selectedAction)) {
            getArrayDataService().deletePlatform(Long.valueOf(platformConfigurationId));
        } else {
            doSavePlatform();
        }
        return SUCCESS;
    }

    private void doSavePlatform() {
        PlatformConfiguration platformConfiguration = getArrayDataService()
            .getRefreshedPlatformConfiguration(Long.valueOf(platformConfigurationId));
        if (platformConfiguration.getPlatformType() == null) {
            platformConfiguration.setPlatformType(PlatformTypeEnum.getByValue(getSelectedPlatformType()));
        }
        if (platformConfiguration.getPlatformChannelType() == null) {
            platformConfiguration.setPlatformChannelType(PlatformChannelTypeEnum
                .getByValue(getSelectedPlatformChannelType()));
        }
        getArrayDataService().savePlatformConfiguration(platformConfiguration);
    }

    private void checkAnnotationFileExtension() {
        boolean isUnsupportedExtension = false;
        switch (PlatformTypeEnum.getByValue(platformType)) {
        case AFFYMETRIX_GENE_EXPRESSION:
        case AFFYMETRIX_SNP:
        case AFFYMETRIX_COPY_NUMBER:
            isUnsupportedExtension = isInvalidAffymetrixExtension();
            break;

        case AGILENT_GENE_EXPRESSION:
            isUnsupportedExtension = isInvalidAgilentGeneExExtenstion();
            break;

        case AGILENT_COPY_NUMBER:
            isUnsupportedExtension = isInvalidAgilentCopyNumExtension();
            break;

        default:
            addActionError(getText("struts.messages.error.platform.invalid.type", getArgs(platformType)));
        }
        if (isUnsupportedExtension) {
            extensionNotSupported();
        }
    }

    private boolean isInvalidAgilentCopyNumExtension() {
        return !platformFileFileName.endsWith(".adf") && !platformFileFileName.endsWith(".xml");
    }

    private boolean isInvalidAgilentGeneExExtenstion() {
        return !checkCsvTsvTxtExtension() && !platformFileFileName.endsWith(".xml")
                && !platformFileFileName.endsWith(".adf");
    }

    private boolean isInvalidAffymetrixExtension() {
        return !platformFileFileName.endsWith(AFFYMETRIX_PLATFORM_EXTENSION);
    }

    private boolean checkCsvTsvTxtExtension() {
        return platformFileFileName.endsWith(".csv") || platformFileFileName.endsWith(".tsv")
                || platformFileFileName.endsWith(".txt");
    }

    private void extensionNotSupported() {
        addFieldError(PLATFORM_FILE, getText("struts.messages.error.platform.file.format.not.supported",
                getArgs(platformFileFileName)));
    }

    private boolean checkAddedPlatformFile() {
        if (platformFile == null) {
            addFieldError(PLATFORM_FILE, getText("struts.messages.error.file.required", getArgs("Annotation")));
            return false;
        } else if (platformFile.length() == 0) {
            addFieldError(PLATFORM_FILE, getText("struts.messages.error.file.empty", getArgs("Annotation")));
            return false;
        }
        return true;
    }

    private void checkPlatformParameters() {
        if (PlatformTypeEnum.AFFYMETRIX_SNP.getValue().equals(platformType)
                || PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue().equals(platformType)) {
            checkAffyDnaPlatformType();
        } else {
            if (checkAddedPlatformFile()) {
                checkAnnotationFileExtension();
                checkNonAffyDnaPlatformType();
            }
        }
    }

    private void checkNonAffyDnaPlatformType() {
        if (!PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue().equals(platformType)
                && StringUtils.isEmpty(platformName)
                && !platformFileFileName.endsWith(".xml")) {
            addFieldError(PLATFORM_NAME, getText("struts.messages.error.platform.non.affy.dna.name.req"));
        }
    }

    private void checkAffyDnaPlatformType() {
        if (getPlatformForm().getAnnotationFiles().isEmpty()) {
            addActionError(getText("struts.messages.error.platform.empty.annotation.files"));
        }
        if (StringUtils.isEmpty(platformName)) {
            addFieldError(PLATFORM_NAME, getText("struts.messages.error.platform.affy.dna.name.req"));
        }
    }

    /**
     * Add the annotation file to the platform form.
     * @return the Struts result.
     */
    public String addAnnotationFile() {
        try {
            getPlatformForm().add(getPlatformFileCopy(), platformFileFileName);
        } catch (IOException e) {
            addFieldError(PLATFORM_FILE, getText("struts.messages.error.uploading", getArgs(platformFileFileName)));
            return ERROR;
        }
        return INPUT;
    }

    /**
     * Create the platform.
     * @return the Struts result.
     */
    public String createPlatform() {
        try {
            AbstractPlatformSource source = getPlatformSource();
            if (source == null) {
                addActionError(getText("struts.messages.error.platform.invalid.type", getArgs(platformType)));
                return ERROR;
            }
            String extractedName = source.getLoader().getPlatformName();
            if (validatePlatformName(extractedName)) {
                submitPlatformCreation(extractedName, source);
                return SUCCESS;
            } else {
                return ERROR;
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't copy uploaded file", e);
            addActionError(getText("struts.messages.error.platform.copy.file", getArgs(e.getMessage())));
            return ERROR;
        } catch (PlatformLoadingException e) {
            LOGGER.error("Couldn't load annotation file", e);
            addActionError(getText("struts.messages.error.platform.read.file", getArgs(e.getMessage())));
            return ERROR;
        }
    }

    private AbstractPlatformSource getPlatformSource() throws IOException {
        AbstractPlatformSource source = null;
        switch (PlatformTypeEnum.getByValue(platformType)) {
        case AFFYMETRIX_GENE_EXPRESSION:
            source = new AffymetrixExpressionPlatformSource(getPlatformFileCopy());
            break;

        case AFFYMETRIX_SNP:
            source = new AffymetrixSnpPlatformSource(getPlatformForm().getAnnotationFiles(), getPlatformName());
            break;

        case AFFYMETRIX_COPY_NUMBER:
            source = new AffymetrixCnPlatformSource(getPlatformForm().getAnnotationFiles(), getPlatformName());
            break;

        case AGILENT_GENE_EXPRESSION:
            source = new AgilentExpressionPlatformSource(getPlatformFileCopy(), getPlatformName(),
                    platformFileFileName);
            break;

        case AGILENT_COPY_NUMBER:
            source = new AgilentCnPlatformSource(getPlatformFileCopy(), getPlatformName(), platformFileFileName);
            break;

        default:
            source = null;
        }
        return source;
    }

    private boolean validatePlatformName(String name) {
        if (name == null) {
            addFieldError(PLATFORM_FILE, getText("struts.messages.error.platform.name.not.found",
                    getArgs(platformFileFileName)));
            return false;
        } else if (getArrayDataService().getPlatformConfiguration(name) != null) {
            addActionError(getText("struts.messages.error.platform.name.duplicate.or.loaded", getArgs(name)));
            return false;
        }
        return true;
    }

    private void submitPlatformCreation(String name, AbstractPlatformSource source) {
        source.setDeleteFileOnCompletion(true);
        PlatformConfiguration configuration = new PlatformConfiguration(source);
        configuration.setName(name);
        configuration.setStatus(Status.PROCESSING);
        configuration.setPlatformType(PlatformTypeEnum.getByValue(platformType));
        configuration.setPlatformChannelType(PlatformChannelTypeEnum.getByValue(platformChannelType));
        arrayDataService.savePlatformConfiguration(configuration);
        ajaxUpdater.runJob(configuration, getWorkspace().getUsername());
        getPlatformForm().clear();
    }

    /**
     * Creates a copy of the uploaded file, as the original is deleted as soon as the action completes.
     *
     * @return the copied file
     * @throws IOException if the file couldn't be copied
     */
    private File getPlatformFileCopy() throws IOException {
        File copy = new File(getFileManager().getNewTemporaryDirectory("platform"), getPlatformFile().getName());
        FileUtils.copyFile(getPlatformFile(), copy);
        return copy;
    }


    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the platformFile
     */
    public File getPlatformFile() {
        return platformFile;
    }

    /**
     * @param platformFile the platformFile to set
     */
    public void setPlatformFile(File platformFile) {
        this.platformFile = platformFile;
    }

    /**
     * @return the platformFileContentType
     */
    public String getPlatformFileContentType() {
        return platformFileContentType;
    }

    /**
     * @param platformFileContentType the platformFileContentType to set
     */
    public void setPlatformFileContentType(String platformFileContentType) {
        this.platformFileContentType = platformFileContentType;
    }

    /**
     * @return the platformFileFileName
     */
    public String getPlatformFileFileName() {
        return platformFileFileName;
    }

    /**
     * @param platformFileFileName the platformFileFileName to set
     */
    public void setPlatformFileFileName(String platformFileFileName) {
        this.platformFileFileName = convertFileExtensionToLowercase(platformFileFileName);
    }

    private String convertFileExtensionToLowercase(String fileName) {
        String ext = "";
        String lowerCaseFileName = fileName;
        int i = lowerCaseFileName.lastIndexOf('.');
        if (i > 0 && i < lowerCaseFileName.length() - 1) {
            ext = lowerCaseFileName.substring(i).toLowerCase();
            lowerCaseFileName = lowerCaseFileName.substring(0, i) + ext;
        }
        return lowerCaseFileName;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the platformType
     */
    public String getPlatformType() {
        return platformType;
    }

    /**
     * @param platformType the platformType to set
     */
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    /**
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * Display status for the platform name.
     * @return whether to display the platform name.
     */
    public String getPlatformNameDisplay() {
        if (PlatformTypeEnum.AFFYMETRIX_SNP.getValue().equals(platformType)
                || PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue().equals(platformType)
                || PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue().equals(platformType)
                || PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue().equals(platformType)) {
            return DISPLAY_BLOCK;
        } else {
            return DISPLAY_NONE;
        }
    }

    /**
     * Display status for the platform channel type.
     * @return whether to display the platform channel type.
     */
    public String getPlatformChannelTypeDisplay() {
        if (PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue().equals(platformType)
                || PlatformTypeEnum.AFFYMETRIX_SNP.getValue().equals(platformType)
                || PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue().equals(platformType)) {
            return DISPLAY_NONE;
        } else {
            return DISPLAY_BLOCK;
        }
    }

    /**
     * Display status for the add annotation file button.
     * @return whether to display the add button.
     */
    public String getAddButtonDisplay() {
        if (PlatformTypeEnum.AFFYMETRIX_SNP.getValue().equals(platformType)
                || PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue().equals(platformType)) {
            return DISPLAY_BLOCK;
        } else {
            return DISPLAY_NONE;
        }
    }

    /**
     * Disable the adf geml comment.
     * @return whether to display the adf geml comment.
     */
    public String getAdfGemlFileDisplay() {
        if (PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue().equals(platformType)) {
            return DISPLAY_BLOCK;
        } else {
            return DISPLAY_NONE;
        }
    }

    /**
     * Disable the csv comment.
     * @return whether to display the csv comment.
     */
    public String getCsvlFileDisplay() {
        if (PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue().equals(platformType)) {
            return DISPLAY_NONE;
        } else {
            return DISPLAY_BLOCK;
        }
    }

    /**
     * @return the ajaxUpdater
     */
    public IPlatformDeploymentAjaxUpdater getAjaxUpdater() {
        return ajaxUpdater;
    }

    /**
     * @param ajaxUpdater the ajaxUpdater to set
     */
    public void setAjaxUpdater(IPlatformDeploymentAjaxUpdater ajaxUpdater) {
        this.ajaxUpdater = ajaxUpdater;
    }

    /**
     * @return the platformConfigurationId
     */
    public String getPlatformConfigurationId() {
        return platformConfigurationId;
    }

    /**
     * @param platformConfigurationId the platformConfigurationId to set
     */
    public void setPlatformConfigurationId(String platformConfigurationId) {
        this.platformConfigurationId = platformConfigurationId;
    }

    /**
     * @return the selectedPlatformType
     */
    public String getSelectedPlatformType() {
        return selectedPlatformType;
    }

    /**
     * @param selectedPlatformType the selectedPlatformType to set
     */
    public void setSelectedPlatformType(String selectedPlatformType) {
        this.selectedPlatformType = selectedPlatformType;
    }

    /**
     * @return the platformChannelType
     */
    public String getPlatformChannelType() {
        return platformChannelType;
    }

    /**
     * @param platformChannelType the platformChannelType to set
     */
    public void setPlatformChannelType(String platformChannelType) {
        this.platformChannelType = platformChannelType;
    }

    /**
     * @return the selectedPlatformChannelType
     */
    public String getSelectedPlatformChannelType() {
        return selectedPlatformChannelType;
    }

    /**
     * @param selectedPlatformChannelType the selectedPlatformChannelType to set
     */
    public void setSelectedPlatformChannelType(String selectedPlatformChannelType) {
        this.selectedPlatformChannelType = selectedPlatformChannelType;
    }
}
