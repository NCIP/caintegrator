/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import gov.nih.nci.caintegrator.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action to run NCIA Dicom jobs which retrieve and download DICOM files.
 */
@Component("nciaDicomRetrievalAction")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
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
