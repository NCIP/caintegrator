/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  
 */
public class PlatformDeploymentAjaxUpdater extends AbstractDwrAjaxUpdater
    implements IPlatformDeploymentAjaxUpdater {
    
    private static final String UNAVAILABLE_STRING = "---";
    private static final String STATUS_TABLE = "platformDeploymentJobStatusTable";
    private static final String JOB_PLATFORM_NAME = "platformName_";
    private static final String JOB_PLATFORM_TYPE = "platformType_";
    private static final String JOB_PLATFORM_CHANNEL_TYPE = "platformChannelType_";
    private static final String JOB_PLATFORM_VENDOR = "platformVendor_";
    private static final String JOB_PLATFORM_STATUS = "platformStatus_";
    private static final String JOB_PLATFORM_STATUS_DESCRIPTION = "platformStatusDescription_";
    private static final String JOB_ARRAY_NAME = "platformArrayName_";
    private static final String JOB_ACTION_PLATFORM_URL = "platformJobActionUrl_";
    private static final String PLATFORM_LOADER = "platformLoader";
    private static final String CLOSE_TAG = "\">";
    private ArrayDataService arrayDataService;

    /**
     * {@inheritDoc}
     */
    protected void initializeDynamicTable(DisplayableUserWorkspace workspace) {
        String username = workspace.getUserWorkspace().getUsername();
        try {
            int counter = 0;
            List<PlatformConfiguration> platformConfigurationList = new ArrayList<PlatformConfiguration>();
            platformConfigurationList.addAll(arrayDataService.getPlatformConfigurations());
            Comparator<PlatformConfiguration> nameComparator = new Comparator<PlatformConfiguration>() {
                public int compare(PlatformConfiguration configuration1, PlatformConfiguration configuration2) {
                    return retrievePlatformName(configuration1).compareToIgnoreCase(
                            retrievePlatformName(configuration2));
                }
            };
            Collections.sort(platformConfigurationList, nameComparator);
            for (PlatformConfiguration platformConfiguration : platformConfigurationList) {
                checkTimeout(platformConfiguration);
                getDwrUtil(username).addRows(STATUS_TABLE, createRow(platformConfiguration),
                        retrieveRowOptions(counter));
                updateJobStatus(username, platformConfiguration);
                counter++;
            }
        } finally {
            getDwrUtil(username).setValue(PLATFORM_LOADER, "");
        }
    }
    
    private void checkTimeout(PlatformConfiguration platformConfiguration) {
        if (Status.PROCESSING.equals(platformConfiguration.getStatus())
                && DateUtil.isTimeout(platformConfiguration.getDeploymentStartDate())) {
            platformConfiguration.setStatus(Status.ERROR);
            platformConfiguration.setStatusDescription("Time out after 12 hours");
            arrayDataService.savePlatformConfiguration(platformConfiguration);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void associateJobWithSession(DwrUtilFactory dwrUtilFactory, String username, Util util) {
        dwrUtilFactory.associatePlatformConfigurationJobWithSession(username, util);
    }

    private String[][] createRow(PlatformConfiguration platformConfiguration) {
        String[][] rowString = new String[1][8];
        String id = platformConfiguration.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_PLATFORM_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_PLATFORM_TYPE + id + endSpan;
        rowString[0][2] = startSpan + JOB_PLATFORM_CHANNEL_TYPE + id + endSpan;
        rowString[0][3] = startSpan + JOB_PLATFORM_VENDOR + id + endSpan;
        rowString[0][4] = startSpan + JOB_ARRAY_NAME + id + endSpan;
        rowString[0][5] = startSpan + JOB_PLATFORM_STATUS + id + endSpan;
        rowString[0][6] = startSpan + JOB_PLATFORM_STATUS_DESCRIPTION + id + endSpan;
        rowString[0][7] = startSpan + JOB_ACTION_PLATFORM_URL + id + endSpan;
        return rowString;
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(PlatformConfiguration platformConfiguration, String username) {
        Thread platformConfigurationRunner = new Thread(
                new PlatformDeploymentAjaxRunner(this, platformConfiguration, username));
        platformConfigurationRunner.start();
    }
    
    void addError(String errorMessage, String username) {
        getDwrUtil(username).setValue("errorMessages", errorMessage);
    }

    /**
     * @param username
     * @return
     */
    private Util getDwrUtil(String username) {
        return getDwrUtilFactory().retrievePlatformConfigurationUtil(username);
    }

    void updateJobStatus(String username, PlatformConfiguration platformConfiguration) {
        Util utilThis = getDwrUtil(username);
        String platformConfigurationId = platformConfiguration.getId().toString();
        utilThis.setValue(JOB_PLATFORM_NAME + platformConfigurationId, 
                          retrievePlatformName(platformConfiguration));
        updateRowPlatformType(platformConfiguration, utilThis, platformConfigurationId);
        updateRowPlatformChannelType(platformConfiguration, utilThis, platformConfigurationId);
        utilThis.setValue(JOB_PLATFORM_VENDOR + platformConfigurationId, 
                retrievePlatformVendor(platformConfiguration));
        utilThis.setValue(JOB_ARRAY_NAME + platformConfigurationId, 
                          retrievePlatformArrayNames(platformConfiguration));
        utilThis.setValue(JOB_PLATFORM_STATUS + platformConfigurationId, 
                getStatusMessage(platformConfiguration.getStatus()));
        utilThis.setValue(JOB_PLATFORM_STATUS_DESCRIPTION + platformConfigurationId, 
                platformConfiguration.getStatusDescription());
        updateRowActions(platformConfiguration, utilThis, platformConfigurationId);
    }

    private String retrievePlatformName(PlatformConfiguration platformConfiguration) {
        return platformConfiguration.getPlatform() == null ? platformConfiguration.getName() 
                                       : platformConfiguration.getPlatform().getName();
    }
    
    private String retrievePlatformType(PlatformConfiguration platformConfiguration) {
        return platformConfiguration.getPlatformType() == null ? UNAVAILABLE_STRING
                                       : platformConfiguration.getPlatformType().getValue();
    }
    
    private String retrievePlatformChannelType(PlatformConfiguration platformConfiguration) {
        return platformConfiguration.getPlatformChannelType() == null ? UNAVAILABLE_STRING
                                       : platformConfiguration.getPlatformChannelType().getValue();
    }

    private String retrievePlatformVendor(PlatformConfiguration platformConfiguration) {
        return platformConfiguration.getPlatform() == null ? UNAVAILABLE_STRING 
                                       : platformConfiguration.getPlatform().getVendor().getValue();
    }

    private String retrievePlatformArrayNames(PlatformConfiguration platformConfiguration) {
        return platformConfiguration.getPlatform() == null ? UNAVAILABLE_STRING
                                       : platformConfiguration.getPlatform().getDisplayableArrayNames();
    }

    private void updateRowPlatformType(PlatformConfiguration platformConfiguration, Util utilThis, 
            String platformConfigurationId) {
        if (UNAVAILABLE_STRING.equals(retrievePlatformType(platformConfiguration))) {
            utilThis.setValue(JOB_PLATFORM_TYPE + platformConfigurationId,
                    "<select id=\"platformType" + platformConfigurationId + CLOSE_TAG
                    + getPlatformTypeOptions() + "</select><br>");
        } else {
            utilThis.setValue(JOB_PLATFORM_TYPE + platformConfigurationId, 
                            retrievePlatformType(platformConfiguration));
        }
    }
    
    private String getPlatformTypeOptions() {
        StringBuffer options = new StringBuffer();
        for (String type : PlatformTypeEnum.getValuesToDisplay()) {
            options.append("<option value=\"" + type + CLOSE_TAG + type + "</option>");
        }
        return options.toString();
    }

    private void updateRowPlatformChannelType(PlatformConfiguration platformConfiguration, Util utilThis, 
            String platformConfigurationId) {
        if (UNAVAILABLE_STRING.equals(retrievePlatformChannelType(platformConfiguration))) {
            utilThis.setValue(JOB_PLATFORM_CHANNEL_TYPE + platformConfigurationId,
                    "<select id=\"platformChannelType" + platformConfigurationId + CLOSE_TAG
                    + getPlatformChannelTypeOptions() + "</select><br>");
        } else {
            utilThis.setValue(JOB_PLATFORM_CHANNEL_TYPE + platformConfigurationId, 
                            retrievePlatformChannelType(platformConfiguration));
        }
    }
    
    private String getPlatformChannelTypeOptions() {
        StringBuffer options = new StringBuffer();
        for (String type : PlatformChannelTypeEnum.getValuesToDisplay()) {
            options.append("<option value=\"" + type + CLOSE_TAG + type + "</option>");
        }
        return options.toString();
    }
    
    private void updateRowActions(PlatformConfiguration platformConfiguration, Util utilThis, 
            String platformConfigurationId) {
        if (UNAVAILABLE_STRING.equals(retrievePlatformType(platformConfiguration))
                || UNAVAILABLE_STRING.equals(retrievePlatformChannelType(platformConfiguration))) {
            utilThis.setValue(JOB_ACTION_PLATFORM_URL + platformConfigurationId,
                    "<a href=\"javascript:submitAction('updatePlatform'," + platformConfigurationId + ");\">save</a>",
                    false);
        } else if (!Status.PROCESSING.equals(platformConfiguration.getStatus()) && !platformConfiguration.isInUse()) {
            utilThis.setValue(JOB_ACTION_PLATFORM_URL + platformConfigurationId, 
                    "<a href=\"javascript:submitAction('delete'," + platformConfigurationId + ",'');\">Delete</a>",
                    false);
        } else {
            utilThis.setValue(JOB_ACTION_PLATFORM_URL, "None");
        }
    }
    
    private String getStatusMessage(Status studyConfigurationStatus) {
        if (Status.PROCESSING.equals(studyConfigurationStatus)) {
            return AJAX_LOADING_GIF + " " + studyConfigurationStatus.getValue();
        }
        return studyConfigurationStatus.getValue();
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
}
