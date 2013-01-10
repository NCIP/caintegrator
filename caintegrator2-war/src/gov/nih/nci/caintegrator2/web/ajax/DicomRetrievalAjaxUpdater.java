/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAFacade;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  The whole purpose
 * is to update the jsp page with information based on the DicomRetrievalAjaxRunner's status updates.  It
 * uses the reverse-ajax technology of DWR to achieve this.
 */
public class DicomRetrievalAjaxUpdater implements IDicomRetrievalAjaxUpdater { 
    private Util utilThis;
    private NCIAFacade nciaFacade;
    private NCIADicomJob dicomJob;
    

    /**
     * {@inheritDoc}
     */
    public void runDicomJob() {
        WebContext wctx = WebContextFactory.get();
        DisplayableUserWorkspace workspace = (DisplayableUserWorkspace) 
            wctx.getSession().getAttribute("displayableWorkspace");
        utilThis = new Util(wctx.getScriptSession());
        dicomJob = workspace.getDicomJob();
        
        if (dicomJob != null) {
            initializeJobDescription();
            DicomRetrievalAjaxRunner runner = new DicomRetrievalAjaxRunner(this);
            new Thread(runner).start();
        } else {
            addErrorMessage("No Dicom Job was found on the Session");
        }
    }
    
    private void initializeJobDescription() {
        if (!dicomJob.getImageSeriesIDs().isEmpty()) {
            utilThis.setValue("imageSeriesStatus", "<h3>Image Series UIDs</h3>", false);
            utilThis.addOptions("imageSeriesList", dicomJob.getImageSeriesIDs().
                                                   toArray(new String[dicomJob.getImageSeriesIDs().size()]));
        }
        
        if (!dicomJob.getImageStudyIDs().isEmpty()) {
            utilThis.setValue("imageStudyStatus", "<h3>Image Study UIDs</h3>");
            utilThis.addOptions("imageStudyList", dicomJob.getImageStudyIDs().
                                                   toArray(new String[dicomJob.getImageStudyIDs().size()]));
        }
    }
    
    /**
     * 
     * @param currentStatus sets status on JSP page.
     */
    public void updateCurrentStatus(String currentStatus) {
        utilThis.setValue("currentStatus", currentStatus);
    }
    
    /**
     * The final update of the dynamic test, assuming a successful finish occurs.
     */
    public void finish() {
        completeJob();
        
        String finalizedText = "<b> Job Finished </b> <br>" 
                               + "<a href=\"downloadDicomFile.action\">Download DICOM Files</a>";
        utilThis.setValue("finalizedText", finalizedText, false);
    }
    
    /**
     * Dynamically adds an error message to the page.
     * @param message error message to add.
     */
    public void addErrorMessage(String message) {
        completeJob();
        utilThis.setValue("errorMessages", message);
    }

    private void completeJob() {
        if (dicomJob != null) {
            dicomJob.setCurrentlyRunning(false);
        }
        removeStatusDiv();
    }
    
    private void removeStatusDiv() {
        utilThis.setValue("overallStatusDiv", "");
    }

    /**
     * @return the nciaFacade
     */
    public NCIAFacade getNciaFacade() {
        return nciaFacade;
    }

    /**
     * @param nciaFacade the nciaFacade to set
     */
    public void setNciaFacade(NCIAFacade nciaFacade) {
        this.nciaFacade = nciaFacade;
    }

    /**
     * @return the dicomJob
     */
    public NCIADicomJob getDicomJob() {
        return dicomJob;
    }

}
