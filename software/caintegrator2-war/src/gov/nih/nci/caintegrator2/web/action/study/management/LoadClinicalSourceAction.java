/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

/**
 * Action called to edit an existing clinical data source.
 */
public class LoadClinicalSourceAction extends AbstractClinicalSourceAction {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        try {
            setLastModifiedByCurrentUser();
            getStudyManagementService().loadClinicalAnnotation(getStudyConfiguration(), getClinicalSource());
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            return ERROR;
        }
        return SUCCESS;
    }
    
    /**
     * Reload a clinical source file.
     * @return string
     */
    public String reLoad() {
        try {
            setLastModifiedByCurrentUser();
            getStudyManagementService().reLoadClinicalAnnotation(getStudyConfiguration());
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            return ERROR;
        }
        return SUCCESS;
    }
    
    /**
     * Delete a clinical source file.
     * @return string
     */
    public String delete() {
        try {
            setLastModifiedByCurrentUser();
            getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
            getStudyManagementService().delete(getStudyConfiguration(), getClinicalSource());
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            return ERROR;
        }
        return SUCCESS;
    }
    
}
