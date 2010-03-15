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
package gov.nih.nci.caintegrator2.web.action.analysis;


import gov.nih.nci.caintegrator.plots.kaplanmeier.KMAlgorithm;
import gov.nih.nci.caintegrator2.application.analysis.AbstractKMParameters;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * Abstract Action dealing with Kaplan-Meier plotting.
 */
public abstract class AbstractKaplanMeierAction extends AbstractDeployedStudyAction {

    /**
     * Annotation Tab.
     */
    protected static final String ANNOTATION_TAB = "annotationTab";
    /**
     * Gene Expression Tab.
     */
    protected static final String GENE_EXPRESSION_TAB = "geneExpressionTab";
    /**
     * Query Tab.
     */
    protected static final String QUERY_TAB = "queryTab";
    
    private static final Double SMALLEST_TWO_DIGIT_DECIMAL = .01;
    private static final Integer DELAY_TIME_BETWEEN_PLOT_CREATE = 25;
    private static final String KMPLOT_RESULT = "kmPlotResult";
    private QueryManagementService queryManagementService;
    private AnalysisService analysisService;
    private String displayTab;


    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        retrieveAndRefreshSurvivalValueDefinition();
        populateSurvivalValueDefinitions();
    }
    
    /**
     * Starting point for the kmPlot_tile.jsp.
     * @return Struts string.
     */
    public String initializeKmPlot() {
        return SUCCESS;
    }
    
    private void retrieveAndRefreshSurvivalValueDefinition() {
       AbstractKMParameters params = getKmPlotParameters();
       if (getKmPlotForm().getSurvivalValueDefinitionId() != null 
                && !StringUtils.isEmpty(getKmPlotForm().getSurvivalValueDefinitionId()) 
                && params != null
                && params.getSurvivalValueDefinition() != null) {
                params.getSurvivalValueDefinition().setId(
                       Long.valueOf(getKmPlotForm().getSurvivalValueDefinitionId()));
                params.setSurvivalValueDefinition(getQueryManagementService().
                        getRefreshedEntity(params.getSurvivalValueDefinition()));
       }
    }
    
    /**
     * Return the kmPlotParameters for the action.
     * @param <T> implementation subclass of AbstractKMParameter.
     * @return implementation subclass of AbstractKMParameter.
     */
    public abstract <T extends AbstractKMParameters> T getKmPlotParameters();
    
    /**
     * The URL for the action which retrieves the KM Plot graph image for display.
     * @return URL string.
     */
    public abstract String getPlotUrl();
    
    private void populateSurvivalValueDefinitions() {
        if (getStudy() != null 
            && getStudy().getSurvivalValueDefinitionCollection() != null) {
            Set<SurvivalValueDefinition> validDefinitions = new HashSet<SurvivalValueDefinition>();
            validDefinitions = Cai2Util.retrieveValidSurvivalValueDefinitions(getStudy()
                    .getSurvivalValueDefinitionCollection());
            addValidDefinitionsToForm(validDefinitions);
        }
    }

    private void addValidDefinitionsToForm(Set<SurvivalValueDefinition> validDefinitions) {
        if (getKmPlotForm().getSurvivalValueDefinitions().size() 
            != validDefinitions.size()) {
            getKmPlotForm().setSurvivalValueDefinitions(new HashMap<String, SurvivalValueDefinition>());
            for (SurvivalValueDefinition def : validDefinitions) {
                getKmPlotForm().getSurvivalValueDefinitions().put(def.getId().toString(), def);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (!hasActionErrors() && getKmPlotForm().getSurvivalValueDefinitions().isEmpty()) {
            addActionError("There are no valid survival value definitions defined for this study, "
                    + "unable to create Kaplan-Meier plot.");
        }
    }
    
    /**
     * Clears the survival value definition ID.
     */
    protected void clearKmPlot() {
        SessionHelper.clearKmPlots();
        getKmPlotForm().setSurvivalValueDefinitionId(null);
    }
    
    /**
     * Returns the KMPlotResult image to the JSP.
     * @return Struts return value.
     */
    public String retrievePlot() {
        return KMPLOT_RESULT;
    }
    
    /**
     * Determines if the "Create Plot" button should be displayed.
     * @return T/F value.
     */
    public abstract boolean isCreatable();

    /**
     * Gets all the string pvalues from the KMPlot on the session.
     * @return map of all string PValues.
     */
    public abstract SortedMap<String, SortedMap<String, String>> getAllStringPValues();

    /**
     * Retrieves all string PValues.
     * @param kmPlot Plot to use.
     * @return Map of Strings for PValues, for output on the JSP.
     */
    protected SortedMap<String, SortedMap<String, String>> retrieveAllStringPValues(KMPlot kmPlot) {
        
        SortedMap<String, SortedMap<String, String>> allPValues = new TreeMap<String, SortedMap<String, String>>();
        Set<SubjectGroup> currentlyCalculatedGroups = new HashSet<SubjectGroup>();

        for (SubjectGroup group1 : kmPlot.getConfiguration().getGroups()) {
            addStringPValuesToGroup(kmPlot, allPValues, currentlyCalculatedGroups, group1);
        }
        return allPValues;
    }

    private void addStringPValuesToGroup(KMPlot kmPlot,
            SortedMap<String, SortedMap<String, String>> allPValues,
            Set<SubjectGroup> currentlyCalculatedGroups, SubjectGroup group1) {
        String group1Name = group1.getName();
        if (!allPValues.containsKey(group1Name)) {
            allPValues.put(group1Name, new TreeMap<String, String>());
        }
        for (SubjectGroup group2 : kmPlot.getConfiguration().getGroups()) {
            if (group1 != group2 && !currentlyCalculatedGroups.contains(group2)) {
                allPValues.get(group1Name).put(group2.getName(), 
                                               formatDoubleValue(kmPlot.getPValue(group1, group2)));
            }
        }
        currentlyCalculatedGroups.add(group1);
    }
    
    private String formatDoubleValue(Double number) {
        if (number == null || KMAlgorithm.UNKNOWN_PVALUE.equals(number)) {
            return "N/A";
        }
        String pattern = "0.00";
        if (number < SMALLEST_TWO_DIGIT_DECIMAL && number > 0) {
            pattern = "0.00E0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }
    
    /**
     * This function figures out which kmPlot action it needs to forward to, and then adds the current
     * time to the end to be absolutely sure the image gets redrawn everytime (and not cached), since 
     * the graph will be drawn without a page refresh using AJAX.
     * @return action URL to return to JSP.
     */
    public String retrieveKmPlotUrl() {
        return getPlotUrl() + Calendar.getInstance().getTimeInMillis();
    }
    
    /**
     * When the form is filled out and the user clicks "Create Plot" this calls the
     * analysis service to generate a KMPlot object.
     * @return Struts return value.
     * @throws InterruptedException thread interrupted.
     */
    public String createPlot() throws InterruptedException {
        String returnString = SUCCESS;
        if (isCreatePlotSelected()) { // createPlot() gets called everytime page loads, making sure variable is set
            runFirstCreatePlotThread();
            returnString = validateAndWaitSecondCreatePlotThread();
        }
        return returnString;
    }
    
    /**
     * Since createPlot creates 2 calls, this function is used to do the actual running
     * of the service layer to generate the plot.
     */
    protected abstract void runFirstCreatePlotThread(); 
    
    /**
     * This gets called by the CreatePlot function to add validation error messages and to see if it's the
     * second call to CreatePlot().  There is a bug when using s:div inside an s:tabbedPanel where it will call
     * the function two times, the work around is to have the second thread just wait until the first one is
     * finished and return the same value (INPUT or SUCCESS).
     * @return struts 2 return value.
     * @throws InterruptedException if thread.wait doesn't work.
     */
    protected synchronized String validateAndWaitSecondCreatePlotThread() 
        throws InterruptedException {
        String returnString = SUCCESS;
        AbstractKMParameters kmParameters = getKmPlotParameters();
        if (!kmParameters.validate()) {
            for (String errorMessages : kmParameters.getErrorMessages()) {
                addActionError(errorMessages);
            }
            returnString = INPUT;
        }
        if (isCreatePlotRunning()) {
            while (isCreatePlotRunning()) {
                this.wait(DELAY_TIME_BETWEEN_PLOT_CREATE);
            }
            setCreatePlotSelected(false); // Second thread will set this to false, so both threads validate.
        }
        return returnString;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }


    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * @return the displayTab
     */
    public String getDisplayTab() {
        if (displayTab == null) {
            displayTab = ANNOTATION_TAB;
        }
        return displayTab;
    }

    /**
     * @param displayTab the displayTab to set
     */
    public void setDisplayTab(String displayTab) {
        this.displayTab = displayTab;
    }

    /**
     * @return the createPlotSelected
     */
    public boolean isCreatePlotSelected() {
        return getDisplayableWorkspace().isCreatePlotSelected();
    }

    /**
     * @param createPlotSelected the createPlotSelected to set
     */
    public void setCreatePlotSelected(boolean createPlotSelected) {
        getDisplayableWorkspace().setCreatePlotSelected(createPlotSelected);
    }
    
    /**
     * @return the createPlotSelected
     */
    public boolean isCreatePlotRunning() {
        return getDisplayableWorkspace().isCreatePlotRunning();
    }

    /**
     * @param createPlotRunning the createPlotRunning to set
     */
    public void setCreatePlotRunning(boolean createPlotRunning) {
        getDisplayableWorkspace().setCreatePlotRunning(createPlotRunning);
    }
    
}
