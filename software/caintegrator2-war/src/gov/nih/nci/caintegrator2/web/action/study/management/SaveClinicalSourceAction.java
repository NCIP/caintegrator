/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

/**
 * Action called to edit an existing clinical data source.
 */
public class SaveClinicalSourceAction extends AbstractClinicalSourceAction {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        getStudyManagementService().save(getStudyConfiguration());
        return SUCCESS;
    }
    
}
