/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator.external.cadsr.CaDSRFacade;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  The whole purpose
 * is to update the jsp page tables with data elements and annotation definitions for the user to select.  It
 * uses the reverse-ajax technology of DWR to achieve this.
 */
public class DataElementSearchAjaxUpdater implements IDataElementSearchAjaxUpdater {
    private static final Long TIMEOUT = Long.valueOf(60000); // 1 minute timeout
    private static final String AJAX_LOADING_GIF = "<img src=\"images/ajax-loader.gif\"/>";
    private static final String ANNOTATION_DEFINITION_TABLE = "annotationDefinitionTable";
    private static final String CADSR_TABLE = "cadsrTable";
    private static final String ACTION_PREFIX = "/" + SessionHelper.WAR_CONTEXT_NAME;
    private Long lastRunSearch;
    private Util utilThis;
    private StudyManagementService studyManagementService;
    private List<String> keywordsList = new ArrayList<String>();
    private DisplayableUserWorkspace workspace;
    private String studyConfigurationId;
    private String fieldDescriptorId;
    private Thread annotationDefinitionSearchThread;
    private Thread caDsrSearchThread;
    private int currentlyRunningThreads = 0;
    private ReturnTypeEnum type;

    /**
     * {@inheritDoc}
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void runSearch(String returnType, String studyConfId, String fieldDescId, String keywords,
            String searchResultJsp) throws ServletException, IOException {
        inititalizeAndCheckTimeout();

        if (!searchResultJsp.equalsIgnoreCase("")) {
            WebContext wctx = WebContextFactory.get();
            utilThis.setValue("searchResult", wctx.forwardToString(searchResultJsp), false);
        }

        if (!isCurrentlyRunning()) {
            this.studyConfigurationId = studyConfId;
            this.fieldDescriptorId = fieldDescId;
            this.type = ReturnTypeEnum.getByValue(returnType);
            if (StringUtils.isNotBlank(keywords)) {
                lastRunSearch = System.currentTimeMillis();
                workspace.getDataElementSearchObject().clear();
                workspace.getDataElementSearchObject().setKeywordsForSearch(keywords);
                keywordsList = Arrays.asList(StringUtils.split(keywords));
                annotationDefinitionSearchThread = new Thread(new AnnotationDefinitionSearchAjaxRunner(this));
                annotationDefinitionSearchThread.start();
                caDsrSearchThread = new Thread(new CaDsrSearchAjaxRunner(this));
                caDsrSearchThread.start();
            }
        } else {
            setErrorMessage("There is currently a search in progress, please wait for that to finish.");
        }
    }

    /**
     * {@inheritDoc}
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void initializeJsp(String searchResultJsp) throws ServletException, IOException {
        WebContext wctx = WebContextFactory.get();
        workspace = (DisplayableUserWorkspace) wctx.getSession().getAttribute("displayableWorkspace");
        if (workspace.getDataElementSearchObject().getKeywordsForSearch() != null) {
            utilThis = new Util(wctx.getScriptSession());
            utilThis.setValue("searchResult", wctx.forwardToString(searchResultJsp), false);
            utilThis.setValue("errorMessages", "");
            populateAnnotationDefinitionTable();
            populateCaDsrTable();
        }
        boolean killedThreads = inititalizeAndCheckTimeout();
        if (annotationDefinitionSearchThread != null
                && annotationDefinitionSearchThread.isAlive()
                && !killedThreads) {
                setAnnotationDefinitionInProgress();
        }
        if (caDsrSearchThread != null
                && caDsrSearchThread.isAlive()
                && !killedThreads) {
                setCaDsrInProgress();
        }
    }

    private void populateAnnotationDefinitionTable() {
        utilThis.removeAllRows(ANNOTATION_DEFINITION_TABLE);
        List<AnnotationDefinition> definitions = workspace.getDataElementSearchObject().getSearchDefinitions();
        if (definitions == null || definitions.isEmpty()) {
            setTableRowMessage(ANNOTATION_DEFINITION_TABLE, "No Definitions Found!");
        } else {
            fillAnnotationDefinitionRows(definitions);
        }

    }

    private void populateCaDsrTable() {
        utilThis.removeAllRows(CADSR_TABLE);
        List<CommonDataElement> dataElements = workspace.getDataElementSearchObject().getSearchCommonDataElements();
        if (dataElements == null || dataElements.isEmpty()) {
            setTableRowMessage(CADSR_TABLE, "No matches found in caDSR for your search.");
        } else {
            fillCaDsrRows(dataElements);
        }

    }

    private void killRunningThreads() {
        if (annotationDefinitionSearchThread != null
                && annotationDefinitionSearchThread.isAlive()) {
            setErrorMessage("Previous search timed out!");
            annotationDefinitionSearchThread.interrupt();
        }
        if (caDsrSearchThread != null
            && caDsrSearchThread.isAlive()) {
            setErrorMessage("Previous search timed out!");
            caDsrSearchThread.interrupt();
        }
        currentlyRunningThreads = 0;
    }

    private boolean inititalizeAndCheckTimeout() {
        WebContext wctx = WebContextFactory.get();
        workspace = (DisplayableUserWorkspace) wctx.getSession().getAttribute("displayableWorkspace");
        utilThis = new Util(wctx.getScriptSession());
        utilThis.setValue("errorMessages", "");
        if (checkTimeout()) {
            killRunningThreads();
            return true;
        }
        return false;
    }

    private boolean checkTimeout() {
        if (lastRunSearch != null
            && isCurrentlyRunning()
            && ((System.currentTimeMillis() - lastRunSearch) > TIMEOUT)) {
            return true;
        }
        return false;
    }

    void updateAnnotationDefinitionTable(List<AnnotationDefinition> definitions) {
        workspace.getDataElementSearchObject().getSearchDefinitions().clear();
        workspace.getDataElementSearchObject().getSearchDefinitions().addAll(definitions);
        populateAnnotationDefinitionTable();
    }

    private void fillAnnotationDefinitionRows(List<AnnotationDefinition> definitions) {
        int counter = 0;
        String action = type.getDefinitionReturnUrl();
        for (AnnotationDefinition definition : definitions) {
            String[][] rowString = new String[1][5];

            rowString[0][0] = definition.getDisplayName();

            rowString[0][1] = "<a href=\"" + action
                                + "studyConfiguration.id=" + studyConfigurationId
                                + "&amp;fieldDescriptor.id=" + fieldDescriptorId
                                + "&amp;definitionIndex=" + counter + "\">" + "Select" + "</a>";

            if (definition.getCommonDataElement() != null && definition.getCommonDataElement().getPublicID() != null) {
                rowString[0][2] = String.valueOf(definition.getCommonDataElement().getPublicID());
            }
            rowString[0][3] = definition.getDataType().getValue();
            rowString[0][4] = definition.getCommonDataElement().getDefinition();
            utilThis.addRows(ANNOTATION_DEFINITION_TABLE, rowString, retrieveRowOptions(counter));
            counter++;
        }
    }

    void updateCaDsrTable(List<CommonDataElement> dataElements) {
        workspace.getDataElementSearchObject().getSearchCommonDataElements().clear();
        workspace.getDataElementSearchObject().getSearchCommonDataElements().addAll(dataElements);
        populateCaDsrTable();
    }


    private void fillCaDsrRows(List<CommonDataElement> dataElements) {
        int counter = 0;
        String action = type.getDataElementReturnUrl();
        for (CommonDataElement dataElement : dataElements) {
            String[][] rowString = new String[1][6];
            rowString[0][0] = dataElement.getLongName();
            rowString[0][1] = "<a href=\"" + action
                                    + "studyConfiguration.id=" + studyConfigurationId
                                    + "&amp;fieldDescriptor.id=" + fieldDescriptorId
                                    + "&amp;dataElementIndex=" + counter + "\"> Select </a> | "
                                    + "<a href=\"" + CaDSRFacade.CDE_URL
                                    + "&amp;cdeId=" + dataElement.getPublicID()
                                    + "&amp;version=" + dataElement.getVersion()
                                    + "\" target=\"_blank\"> View </a>";
            rowString[0][2] = String.valueOf(dataElement.getPublicID());
            rowString[0][3] = dataElement.getContextName();
            rowString[0][4] = dataElement.getWorkflowStatus();
            rowString[0][5] = dataElement.getDefinition();
            utilThis.addRows(CADSR_TABLE, rowString, retrieveRowOptions(counter));
            counter++;
        }
    }

    void setAnnotationDefinitionInProgress() {
        setTableRowMessage(ANNOTATION_DEFINITION_TABLE, AJAX_LOADING_GIF);
    }

    /**
     * @param string
     */
    void setCaDsrError(String errorString) {
        setTableRowMessage(CADSR_TABLE, errorString);
    }

    void setCaDsrInProgress() {
        setTableRowMessage(CADSR_TABLE, AJAX_LOADING_GIF + " Please Wait, caDSR Search In Progress...");
    }

    private void setTableRowMessage(String table, String rowString) {
        utilThis.removeAllRows(table);
        String[][] errorStringArray = new String[1][1];
        errorStringArray[0][0] = rowString;
        utilThis.addRows(table, errorStringArray, retrieveRowOptions(0));
    }

    private String retrieveRowOptions(int counter) {
        String bgcolor = "#f9f9f9";
        if (counter % 2 == 0) {
            bgcolor = "fff";
        }
        return "{ rowCreator:function(options) { "
            + " var row = document.createElement(\"tr\");"
            + " row.style.background=\"" + bgcolor + "\";"
            + "return row;"
            + "},"
            + "cellCreator:function(options) { "
            + "var td = document.createElement(\"td\");"
            + "if (options.cellNum == 1) { td.style.whiteSpace=\"nowrap\"; }"
            + "return td;"
            + "},"
            + " escapeHtml:false }";
    }

    boolean isCurrentlyRunning() {
        if (currentlyRunningThreads == 0) {
            return false;
        }
        return true;
    }

    private void setErrorMessage(String errorMessage) {
        utilThis.setValue("errorMessages", errorMessage);
    }


    void increaseRunningThreadCount() {
        currentlyRunningThreads++;
    }

    void decreaseRunningThreadCount() {
        if (currentlyRunningThreads > 0) {
            currentlyRunningThreads--;
        }
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

    /**
     * @return the keywordsList
     */
    public List<String> getKeywordsList() {
        return keywordsList;
    }

    /**
     * Enum for the return type of the search results.
     */
    public static enum ReturnTypeEnum {
        /**
         * Clinical.
         */
        CLINICAL_SOURCE,
        /**
         * Imaging.
         */
        IMAGING_SOURCE,
        /**
         * Group.
         */
        GROUP_SOURCE;

        /**
         * Gets by value the correct enum.
         * @param returnType string value.
         * @return enum value returned.
         */
        public static ReturnTypeEnum getByValue(String returnType) {
            if (CLINICAL_SOURCE.toString().equals(returnType)) {
                return CLINICAL_SOURCE;
            } else if (IMAGING_SOURCE.toString().equals(returnType)) {
                return IMAGING_SOURCE;
            }
            return GROUP_SOURCE;
        }

        /**
         * Gets the definition return URL.
         * @return return URL.
         */
        public String getDefinitionReturnUrl() {
            switch (this) {
            case CLINICAL_SOURCE:
                return ACTION_PREFIX + "/selectClinicalDefinition.action?";
            case IMAGING_SOURCE:
                return ACTION_PREFIX + "/selectImagingDefinition.action?";
            case GROUP_SOURCE:
                return ACTION_PREFIX + "/selectGroupDefinition.action?";
            default:
                return null;
            }
        }

        /**
         * Gets the data element return URL.
         * @return return URL.
         */
        public String getDataElementReturnUrl() {
            switch (this) {
            case CLINICAL_SOURCE:
                return ACTION_PREFIX + "/selectClinicalDataElement.action?";
            case IMAGING_SOURCE:
                return ACTION_PREFIX + "/selectImagingDataElement.action?";
            case GROUP_SOURCE:
                return ACTION_PREFIX + "/selectGroupDataElement.action?";
            default:
                return null;
            }
        }
    }

}
