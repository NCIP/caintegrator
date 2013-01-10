/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

/**
 * Action to run NCIA Dicom jobs which retrieve and download DICOM files.
 */
public class NCIADicomRetrievalAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    
    /**
     * Downloads dicom file.
     * @return Struts 2 result.
     */
    public String downloadDicomFile() {
        NCIADicomJob dicomJob = getDisplayableWorkspace().getDicomJob();
        if (dicomJob != null && dicomJob.getDicomFile() != null && dicomJob.isCompleted()) {
            return "dicomFileResult";
        }
        addActionError(getText("struts.messages.error.query.dicom.not.exist"));
        return ERROR;
    }
}
