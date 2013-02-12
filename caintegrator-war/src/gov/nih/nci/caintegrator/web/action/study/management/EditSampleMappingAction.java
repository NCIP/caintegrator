/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.caarray.ExperimentNotFoundException;

/**
 * Action called to create or edit a <code>GenomicDataSourceConfiguration</code>.
 */
public class EditSampleMappingAction extends AbstractGenomicSourceAction {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (!getStudyConfiguration().hasLoadedClinicalDataSource()) {
            addActionError(getText("struts.messages.error.study.no.subject"));
        }
        try {
            this.getStudyManagementService().checkForSampleUpdates(getStudyConfiguration());
        } catch (ConnectionException e) {
            addActionError("The configured server couldn't be reached. Please check the configuration settings.");
        } catch (ExperimentNotFoundException e) {
            addActionError(e.getMessage());
        }
        return SUCCESS;
    }

}