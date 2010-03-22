/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.web.action.platform;

import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixCnPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentCnPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.study.management.AbstractStudyManagementAction;
import gov.nih.nci.caintegrator2.web.ajax.IPlatformDeploymentAjaxUpdater;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Provides functionality to list and add array designs.
 */
@SuppressWarnings("PMD") // See createPlatform method
public class ManagePlatformsAction extends AbstractStudyManagementAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ManagePlatformsAction.class);
    private ArrayDataService arrayDataService;
    private FileManager fileManager;
    private File platformFile;
    private String platformFileContentType;
    private String platformFileFileName;
    private String platformName;
    private String platformType = PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue();
    private String selectedAction;
    private IPlatformDeploymentAjaxUpdater ajaxUpdater;
    private String platformConfigurationId;
    private String selectedPlatformType;

    private static final String CREATE_PLATFORM_ACTION = "createPlatform";
    private static final String ADD_FILE_ACTION = "addAnnotationFile";
    private static final String PLATFORM_NAME = "platformName";
    private static final String PLATFORM_FILE = "platformFile";
    
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
            addActionError("Cannot update platform because the id is not given.");
            return ERROR;
        }
        if (!SessionHelper.getInstance().isAuthenticated()) {
            addActionError("User is unauthenticated");
            return ERROR;
        }
        if ("delete".equalsIgnoreCase(selectedAction)) {
            getArrayDataService().deletePlatform(Long.valueOf(platformConfigurationId));
        } else {
            PlatformConfiguration platformConfiguration = getArrayDataService().
                getRefreshedPlatformConfiguration(Long.valueOf(platformConfigurationId));
            platformConfiguration.setPlatformType(PlatformTypeEnum.getByValue(getSelectedPlatformType()));
            getArrayDataService().savePlatformConfiguration(platformConfiguration);
        }
        return SUCCESS;
    }
    
    private void checkAnnotationFileExtension() {
        switch (PlatformTypeEnum.getByValue(platformType)) {
        case AFFYMETRIX_GENE_EXPRESSION:
            if (!platformFileFileName.endsWith(".csv")) {
                extensionNotSupported();
            }
            break;

        case AFFYMETRIX_SNP:
            if (!platformFileFileName.endsWith(".csv")) {
                extensionNotSupported();
            }
            break;

        case AFFYMETRIX_COPY_NUMBER:
            if (!platformFileFileName.endsWith(".csv")) {
                extensionNotSupported();
            }
            break;
            
        case AGILENT_GENE_EXPRESSION:
            if (!platformFileFileName.endsWith(".csv")
                    && !platformFileFileName.endsWith(".xml")
                    && !platformFileFileName.endsWith(".adf")) {
                extensionNotSupported();
            }
            break;
            
        case AGILENT_COPY_NUMBER:
            if (!platformFileFileName.endsWith(".adf")
                    && !platformFileFileName.endsWith(".xml")) {
                extensionNotSupported();
            }
            break;

        default:
            addActionError("Invalid platform type: " + platformType);
        }
    }
    
    private void extensionNotSupported() {
        addFieldError(PLATFORM_FILE, "This file format is not supported: " + platformFileFileName);
    }
    
    private boolean checkAddedPlatformFile() {
        if (platformFile == null) {
            addFieldError(PLATFORM_FILE, "Annotation file is required");
            return false;
        } else if (platformFile.length() == 0) {
            addFieldError(PLATFORM_FILE, "Annotation file is empty");
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
            addFieldError(PLATFORM_NAME, "Platform name is required for this platform type and annotation type.");
        }
    }

    private void checkAffyDnaPlatformType() {
        if (getPlatformForm().getAnnotationFiles().isEmpty()) {
            addActionError("Annotation File(s) Selected is empty.");
        }
        if (StringUtils.isEmpty(platformName)) {
            addFieldError(PLATFORM_NAME, "Platform name is required for this platform type.");
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
            addFieldError(PLATFORM_FILE, "Error up load file: " + platformFileFileName);
            return ERROR;
        }
        return INPUT;
    }
    
    /**
     * Create the platform.
     * @return the Struts result.
     */
    @SuppressWarnings("PMD") // Check for different platform types
    public String createPlatform() {
        try {
            AbstractPlatformSource source;
            switch (PlatformTypeEnum.getByValue(platformType)) {
            case AFFYMETRIX_GENE_EXPRESSION:
                source = new AffymetrixExpressionPlatformSource(
                        getPlatformFileCopy());
                break;

            case AFFYMETRIX_SNP:
                source = new AffymetrixSnpPlatformSource(
                        getPlatformForm().getAnnotationFiles(), getPlatformName());
                break;

            case AFFYMETRIX_COPY_NUMBER:
                source = new AffymetrixCnPlatformSource(
                        getPlatformForm().getAnnotationFiles(), getPlatformName());
                break;
                
            case AGILENT_GENE_EXPRESSION:
                source = new AgilentExpressionPlatformSource(
                        getPlatformFileCopy(), getPlatformName(), platformFileFileName);
                break;
                
            case AGILENT_COPY_NUMBER:
                source = new AgilentCnPlatformSource(
                        getPlatformFileCopy(), getPlatformName(), platformFileFileName);
                break;

            default:
                addActionError("Invalid platform type: " + platformType);
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
            addActionError("Please contact the system administrator. Couldn't copy the uploaded files: " 
                    + e.getMessage());
            return ERROR;
        } catch (PlatformLoadingException e) {
            LOGGER.error("Couldn't loaded annotation file", e);
            addActionError("Couldn't read annotation file: " + e.getMessage());
            return ERROR;
        }
    }
    
    private boolean validatePlatformName(String name) {
        if (name == null) {
            addFieldError(PLATFORM_FILE, "Platform name not found in annotation file: "
                    + platformFileFileName);
            return false;
        } else if (getArrayDataService().getPlatformConfiguration(name) != null) {
            addActionError("Platform name is duplicate or the platform is already been loaded: "
                    + name);
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
        int i = fileName.lastIndexOf(".");
        if (i > 0 && i < fileName.length() - 1) {
            ext = fileName.substring(i).toLowerCase();
            fileName = fileName.substring(0, i) + ext;
        }
        return fileName;
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
            return "display: block;";
        } else {
            return "display: none;";
        }
    }
    
    /**
     * Display status for the add annotation file button.
     * @return whether to display the add button.
     */
    public String getAddButtonDisplay() {
        if (PlatformTypeEnum.AFFYMETRIX_SNP.getValue().equals(platformType)
                || PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue().equals(platformType)) {
            return "display: block;";
        } else {
            return "display: none;";
        }
    }
    
    /**
     * Disable the adf geml comment.
     * @return whether to display the adf geml comment.
     */
    public String getAdfGemlFileDisplay() {
        if (PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue().equals(platformType)) {
            return "display: block;";
        } else {
            return "display: none;";
        }
    }
    
    /**
     * Disable the csv comment.
     * @return whether to display the csv comment.
     */
    public String getCsvlFileDisplay() {
        if (PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue().equals(platformType)) {
            return "display: none;";
        } else {
            return "display: block;";
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
}
