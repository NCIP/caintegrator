/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  
 */
public class GenomicDataSourceAjaxUpdater extends AbstractDwrAjaxUpdater
    implements IGenomicDataSourceAjaxUpdater {
    
    private static final String STATUS_TABLE = "genomicSourceJobStatusTable";
    private static final String JOB_HOST_NAME = "genomicSourceHostName_";
    private static final String JOB_EXPERIMENT_IDENTIFIER = "genomicSourceExperimentId_";
    private static final String JOB_DATA_TYPE = "genomicSourceDataType_";
    private static final String JOB_DEPLOYMENT_STATUS = "genomicSourceStatus_";
    private static final String JOB_LAST_MODIFIED_DATE = "genomicSourceLastModifiedDate_";
    private static final String JOB_EDIT_URL = "genomicSourceEditUrl_";
    private static final String JOB_MAP_SAMPLES_URL = "genomicSourceMapSamplesUrl_";
    private static final String JOB_CONFIGURE_SNP_URL = "genomicSourceConfigureSnpUrl_";
    private static final String JOB_CONFIGURE_COPY_NUMBER_URL = "genomicSourceConfigureCopyNumberUrl_";
    private static final String JOB_DELETE_URL = "genomicSourceDeleteUrl_";
    private static final String JOB_FILE_DESCRIPTION = "genomicSourceFileDescription_";
    private static final String JOB_ACTION_BAR1 = "genomicSourceActionBar1_";
    private static final String JOB_ACTION_BAR2 = "genomicSourceActionBar2_";
    private static final String GENOMIC_SOURCES_LOADER = "genomicSourceLoader";
    
    private StudyManagementService studyManagementService;

    /**
     * {@inheritDoc}
     */
    protected void initializeDynamicTable(DisplayableUserWorkspace workspace) {
        String username = workspace.getUserWorkspace().getUsername();
        try {
            StudyConfiguration studyConfiguration = workspace.getCurrentStudyConfiguration();
            if (studyConfiguration != null && studyConfiguration.getId() != null) {
                studyConfiguration = studyManagementService.getRefreshedEntity(studyConfiguration);
                int counter = 0;
                for (GenomicDataSourceConfiguration genomicSource : studyConfiguration.getGenomicDataSources()) {
                    getDwrUtil(username).addRows(STATUS_TABLE, 
                                                    createRow(genomicSource), 
                                                    retrieveRowOptions(counter));
                    updateJobStatus(username, genomicSource, false);
                    counter++;
                }
            } 
        } finally {
            getDwrUtil(username).setValue(GENOMIC_SOURCES_LOADER, "");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void associateJobWithSession(DwrUtilFactory dwrUtilFactory, String username, Util util) {
        dwrUtilFactory.associateGenomicDataSourceWithSession(username, util);
    }

    private String[][] createRow(GenomicDataSourceConfiguration genomicSource) {
        String[][] rowString = new String[1][7];
        String id = genomicSource.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_HOST_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_EXPERIMENT_IDENTIFIER + id + endSpan;
        rowString[0][2] = startSpan + JOB_FILE_DESCRIPTION + id + endSpan;
        rowString[0][3] = startSpan + JOB_DATA_TYPE + id + endSpan;
        rowString[0][4] = startSpan + JOB_DEPLOYMENT_STATUS + id + endSpan;
        rowString[0][5] = startSpan + JOB_LAST_MODIFIED_DATE + id + endSpan;
        rowString[0][6] = startSpan + JOB_EDIT_URL + id + endSpan
                          + startSpan + getMappingActionUrl(genomicSource) + id + endSpan
                          + startSpan + JOB_DELETE_URL + id + endSpan;
        
        return rowString;
    }
    
    private String getMappingActionUrl(GenomicDataSourceConfiguration genomicSource) {
        if (genomicSource.isExpressionData()) {
            return JOB_MAP_SAMPLES_URL;
        } else if (genomicSource.isSnpData()) {
            return JOB_CONFIGURE_SNP_URL;
        } else {
            return JOB_CONFIGURE_COPY_NUMBER_URL;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(Long genomicSourceId) {
        Thread genomicSourceRunner = new Thread(new GenomicDataSourceAjaxRunner(this, genomicSourceId));
        genomicSourceRunner.start();
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected Util getDwrUtil(String username) {
        return getDwrUtilFactory().retrieveGenomicDataSourceUtil(username);
    }
    
    /**
     * Saves studyConfiguration to database, then updates the status to JSP.
     * @param username to update the status to.
     * @param genomicSource to save and update.
     */
    public void saveAndUpdateJobStatus(String username, GenomicDataSourceConfiguration genomicSource) {
        getStudyManagementService().daoSave(genomicSource);
        updateJobStatus(username, genomicSource, true);
    }

    /**
     * Updates studyConfiguration status.
     * @param username to update the status to.
     * @param genomicSource to update.
     * @param checkDeployButton determines whether to update the deploy button or not.
     */
    public void updateJobStatus(String username, GenomicDataSourceConfiguration genomicSource, 
            boolean checkDeployButton) {
        Util utilThis = getDwrUtil(username);
        String genomicSourceId = genomicSource.getId().toString();
        utilThis.setValue(JOB_HOST_NAME + genomicSourceId, 
                            genomicSource.getServerProfile().getHostname());
        utilThis.setValue(JOB_EXPERIMENT_IDENTIFIER + genomicSourceId, 
                            genomicSource.getExperimentIdentifier());
        updateRowFileDescriptions(utilThis, genomicSource, genomicSourceId);
        utilThis.setValue(JOB_DATA_TYPE + genomicSourceId, 
                genomicSource.getDataTypeString());
        utilThis.setValue(JOB_DEPLOYMENT_STATUS + genomicSourceId, getStatusMessage(genomicSource.getStatus()));
        utilThis.setValue(JOB_LAST_MODIFIED_DATE + genomicSourceId, 
                genomicSource.getDisplayableLastModifiedDate());
        updateRowActions(genomicSource, utilThis, genomicSourceId);
        // TJNOTE: this is checking to see if we can enable the button, but the "isDeployable()" function
        // on the study configuration is throwing lazy initialization exception (only on dev, not localhost).
        // for now disabling, but need to come back and debug this.
//        if (checkDeployButton && genomicSource.getStudyConfiguration().isDeployable()) {
//            utilThis.addFunctionCall("enableDeployButton");
//        }
    }

    private void updateRowFileDescriptions(Util utilThis, GenomicDataSourceConfiguration genomicSource,
            String genomicSourceId) {
        StringBuffer fileDescriptionString = new StringBuffer();
        String brString = "<br>";
        if (genomicSource.isExpressionData()) {
            fileDescriptionString.append("<i>Mapping File: </i>");
            fileDescriptionString.append(genomicSource.getSampleMappingFileName());
            fileDescriptionString.append(brString);
            
            fileDescriptionString.append("<i>Control Sample Mapping File(s): </i>");
            for (String fileName : genomicSource.getControlSampleMappingFileNames()) {
                fileDescriptionString.append(fileName);
                fileDescriptionString.append(brString);
            }
        } else if (genomicSource.isCopyNumberData()) {
            fileDescriptionString.append("<i>Copy Number Mapping File: </i>");
            fileDescriptionString.append(genomicSource.getDnaAnalysisMappingFileName());
        } else if (genomicSource.isSnpData()) {
            fileDescriptionString.append("<i>SNP Mapping File: </i>");
            fileDescriptionString.append(genomicSource.getDnaAnalysisMappingFileName());
        }
        utilThis.setValue(JOB_FILE_DESCRIPTION + genomicSourceId, fileDescriptionString.toString());
    }

    private void updateRowActions(GenomicDataSourceConfiguration genomicSource, Util utilThis, String genomicSourceId) {
        if (!Status.PROCESSING.equals(genomicSource.getStatus())) { // Not processing gets actions
            addNonProcessingActions(genomicSource, utilThis, genomicSourceId);
        } else { // Processing has no actions.
            utilThis.setValue(JOB_EDIT_URL + genomicSourceId, "");
            utilThis.setValue(getMappingActionUrl(genomicSource) + genomicSourceId, "");
            utilThis.setValue(JOB_DELETE_URL + genomicSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR1 + genomicSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR2 + genomicSourceId, "");
        }
    }

    private void addNonProcessingActions(GenomicDataSourceConfiguration genomicSource, Util utilThis,
            String genomicSourceId) {
        String jobActionBarString = "&nbsp;";
        if (!Status.ERROR.equals(genomicSource.getStatus())) {
            addNonErrorActions(genomicSource, utilThis, genomicSourceId, jobActionBarString);
        }
        utilThis.setValue(JOB_EDIT_URL + genomicSourceId, 
            retrieveUrl(genomicSource, "editGenomicSource", "Edit", "edit", false),
            false);
        utilThis.setValue(JOB_ACTION_BAR1 + genomicSourceId, jobActionBarString, false);
        utilThis.setValue(JOB_DELETE_URL + genomicSourceId, 
                retrieveUrl(genomicSource, "deleteGenomicSource", "Delete", "delete", true),
                false);
    }

    private void addNonErrorActions(GenomicDataSourceConfiguration genomicSource, 
                    Util utilThis, String genomicSourceId, String jobActionBarString) {
        if (genomicSource.isExpressionData()) {
            utilThis.setValue(JOB_MAP_SAMPLES_URL + genomicSourceId, 
                retrieveUrl(genomicSource, "editSampleMapping", "Map Samples", "map", false),
                false);
        } else if (genomicSource.isSnpData()) {
            utilThis.setValue(JOB_CONFIGURE_SNP_URL + genomicSourceId, 
                    retrieveUrl(genomicSource, "editDnaAnalysisDataConfiguration", "ConfigureSnpData",
                            "configure", false),
                            false);
        } else {
            utilThis.setValue(JOB_CONFIGURE_COPY_NUMBER_URL + genomicSourceId, 
                    retrieveUrl(genomicSource, "editDnaAnalysisDataConfiguration", "ConfigureCopyNumberData",
                            "configure", false),
                            false);
        }
        utilThis.setValue(JOB_ACTION_BAR2 + genomicSourceId, jobActionBarString, false);
    }

    private String retrieveUrl(GenomicDataSourceConfiguration genomicSource, String actionName, 
            String linkDisplay, String linkCssClass, boolean isDelete) {
        String deleteString = "", token = "", tokenName = "";

        try {
            token = SessionHelper.getInstance().getToken();
            tokenName = SessionHelper.getInstance().getTokenName();
        } catch (Exception e) { token = ""; }
        
        if (isDelete) {
            StringBuffer messageString = new StringBuffer("The Genomic Data Source " 
                + genomicSource.getExperimentIdentifier() + " will be permanently deleted.");
            if (genomicSource.isCopyNumberData()) {
                messageString.append(
                    "  Any copy number analysis jobs associated with samples in this source will also be deleted.");
            }
            deleteString = "onclick=\"return confirm('" + messageString.toString() + "')\"";
        }

        return "<a style=\"margin: 0pt;\" class=\"btn\" "
        + "onclick=\"updateUrlTokenParameters(this)\" href=\"" + actionName + ".action?studyConfiguration.id=" 
        + genomicSource.getStudyConfiguration().getId() 
        + "&genomicSource.id=" + genomicSource.getId()
        + "&struts.token.name=" + tokenName
        + "&struts.token=" + token
        + "\"" 
        + deleteString + "><span class=\"btn_img\"><span class=\""
        + linkCssClass + "\">" + linkDisplay + "</span></span></a>";
        
    }
    
    private String getStatusMessage(Status genomicSourceStatus) {
        if (Status.PROCESSING.equals(genomicSourceStatus)) {
            return AJAX_LOADING_GIF + " " + genomicSourceStatus.getValue();
        }
        return genomicSourceStatus.getValue();
    }

    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }


}
