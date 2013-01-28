/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator.external.ncia.NCIAFacade;

/**
 * This is what calls the NCIAFacade to retrieve the DICOM files through the grid, it is 
 * thread based, so it is asynchronous and started via the DicomRetrievalAjaxUpdater.
 */
public class DicomRetrievalAjaxRunner implements Runnable {

    private final DicomRetrievalAjaxUpdater updater;
    
    DicomRetrievalAjaxRunner(DicomRetrievalAjaxUpdater updater) {
        this.updater = updater;
     }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        NCIADicomJob dicomJob = updater.getDicomJob();
        NCIAFacade nciaFacade = updater.getNciaFacade();
        if (!dicomJob.isCompleted()) {   
            try {
                updater.updateCurrentStatus("Retrieving file from NCIA through grid... Please Wait");
                updater.getDicomJob().setDicomFile(nciaFacade.retrieveDicomFiles(dicomJob));
                if (dicomJob.getDicomFile() == null) {
                    updater.addErrorMessage("There was either no incoming data or unable to save to local system.");
                    return;
                }
                updater.finish();
            } catch (ConnectionException e) {
                updater.addErrorMessage("Image download service not available on server: : " 
                                        + dicomJob.getServerConnection().getUrl());
            }
        } else {
            updater.finish();
        }
    }

}
