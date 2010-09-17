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
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacade;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  The whole purpose
 * is to update the jsp page tables with data elements and annotation definitions for the user to select.  It
 * uses the reverse-ajax technology of DWR to achieve this.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See initializeAndCheckTimeout()
public class DataElementSearchAjaxUpdater implements IDataElementSearchAjaxUpdater {
    private static final Long TIMEOUT = Long.valueOf(60000); // 1 minute timeout
    private static final String AJAX_LOADING_GIF = "<img src=\"images/ajax-loader.gif\"/>";
    private static final String ANNOTATION_DEFINITION_TABLE = "annotationDefinitionTable";
    private static final String CADSR_TABLE = "cadsrTable";
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
    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks for thread
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
            String actionPrefix = "/caintegrator2";
            switch (this) {
            case CLINICAL_SOURCE:
                return actionPrefix + "/selectClinicalDefinition.action?";
            case IMAGING_SOURCE:
                return actionPrefix + "/selectImagingDefinition.action?";
            case GROUP_SOURCE:
                return actionPrefix + "/selectGroupDefinition.action?";
            default:
                return null;
            } 
        }
        
        /**
         * Gets the data element return URL.
         * @return return URL.
         */
        public String getDataElementReturnUrl() {
            String actionPrefix = "/caintegrator2";
            switch (this) {
            case CLINICAL_SOURCE:
                return actionPrefix + "/selectClinicalDataElement.action?";
            case IMAGING_SOURCE:
                return actionPrefix + "/selectImagingDataElement.action?";
            case GROUP_SOURCE:
                return actionPrefix + "/selectGroupDataElement.action?";
            default:
                return null;
            } 
        }
    }
    
}
