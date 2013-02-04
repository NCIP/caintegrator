/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;

/**
 * Base class for actions that require retrieval of persistent <code>StudyConfigurations</code>.
 */
public abstract class AbstractClinicalSourceAction extends AbstractStudyAction {
    
    private DelimitedTextClinicalSourceConfiguration clinicalSource = new DelimitedTextClinicalSourceConfiguration();

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (getClinicalSource().getId() != null) {
            setClinicalSource(getStudyManagementService().getRefreshedEntity(getClinicalSource()));
        }
    }

    /**
     * @return the clinicalSource
     */
    public DelimitedTextClinicalSourceConfiguration getClinicalSource() {
        return clinicalSource;
    }

    /**
     * @param clinicalSource the clinicalSource to set
     */
    public void setClinicalSource(DelimitedTextClinicalSourceConfiguration clinicalSource) {
        this.clinicalSource = clinicalSource;
    }

}
