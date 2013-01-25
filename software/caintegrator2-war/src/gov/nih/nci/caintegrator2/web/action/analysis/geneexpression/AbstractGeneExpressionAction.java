/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;

import java.util.Calendar;

/**
 * Abstract Action dealing with GeneExpression plotting.
 */
public abstract class AbstractGeneExpressionAction extends AbstractDeployedStudyAction {

    /**
     * Annotation Tab.
     */
    protected static final String ANNOTATION_TAB = "annotationTab";
    /**
     * Genomic Query Tab.
     */
    protected static final String GENOMIC_QUERY_TAB = "genomicQueryTab";
    /**
     * Clinical Query Tab.
     */
    protected static final String CLINICAL_QUERY_TAB = "clinicalQueryTab";

    private static final Integer DELAY_TIME_BETWEEN_PLOT_CREATE = 25;
    private static final String GEPLOT_RESULT = "gePlotResult";
    private StudyManagementService studyManagementService;
    private AnalysisService analysisService;
    private String displayTab;
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (!getCurrentStudy().hasExpressionData()) {
            addActionError("There are no gene expression data defined for this study, "
                    + "unable to perform GeneExpression plot.");
        }
    }

    /**
     * Starting point for the gePlot_tile.jsp.
     * @return Struts string.
     */
    public String initializeGePlot() {
        return SUCCESS;
    }
    
    /**
     * Return the gePlotParameters for the action.
     * @param <T> implementation subclass of AbstractGEPlotParameter.
     * @return implementation subclass of AbstractGEPlotParameter.
     */
    public abstract <T extends AbstractGEPlotParameters> T getPlotParameters();
    
    /**
     * The URL for the action which retrieves the GE Plot mean based graph image for display.
     * @return URL string.
     */
    public abstract String getMeanPlotUrl();
    
    /**
     * The URL for the action which retrieves the GE Plot median based graph image for display.
     * @return URL string.
     */
    public abstract String getMedianPlotUrl();
    
    /**
     * The URL for the action which retrieves the GE Plot log2 based graph image for display.
     * @return URL string.
     */
    public abstract String getLog2PlotUrl();
    
    /**
     * The URL for the action which retrieves the GE Plot box-whisker based graph image for display.
     * @return URL string.
     */
    public abstract String getBoxWhiskerPlotUrl();
    
    /**
     * Clears the gene name.
     */
    protected void clearGePlot() {
        SessionHelper.clearGePlots();
    }
    
    /**
     * Returns the GEPlotResult image to the JSP.
     * @return Struts return value.
     */
    public String retrievePlot() {
        return GEPLOT_RESULT;
    }
    
    /**
     * Determines if the "Create Plot" button should be displayed.
     * @return T/F value.
     */
    public abstract boolean isCreatable();
    
    /**
     * This function figures out which gePlot action it needs to forward to, and then adds the current
     * time to the end to be absolutely sure the image gets redrawn everytime (and not cached), since 
     * the graph will be drawn without a page refresh using AJAX.
     * @param calculationType - the PlatCalculationTypeEnum to use for the plot.
     * @return action URL to return to JSP.
     */
    public String retrieveGePlotUrl(String calculationType) {
        String url =  "";
        switch (PlotCalculationTypeEnum.getByValue(calculationType)) {
        case MEAN:
            url = getMeanPlotUrl();
            break;
        case MEDIAN:
            url = getMedianPlotUrl();
            break;
        case BOX_WHISKER_LOG2_INTENSITY:
            url = getBoxWhiskerPlotUrl();
            break;
        case LOG2_INTENSITY:
            url = getLog2PlotUrl();
            break;
        default:
        }
        return url + Calendar.getInstance().getTimeInMillis();
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
        AbstractGEPlotParameters plotParameters = getPlotParameters();
        if (!plotParameters.validate()) {
            for (String errorMessages : plotParameters.getErrorMessages()) {
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
    
    /**
     * Checks to see if the study has any control samples associated with it (to display the checkbox).
     * @return T/F value.
     */
    public boolean hasControlSamples() {
        return getStudy().getStudyConfiguration().hasControlSamples();
    }
    
}
