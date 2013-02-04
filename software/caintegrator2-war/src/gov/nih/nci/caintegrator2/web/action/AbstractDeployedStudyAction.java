/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import java.util.List;

/**
 * Base class for all Struts 2 <code>Actions</code> in the application, provides context set up
 * for the current request.
 */
/**
 * 
 */
public abstract class AbstractDeployedStudyAction extends AbstractCaIntegrator2Action {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (getStudySubscription() == null) {
            addActionError("Please select a study under \"My Studies\".");
            return;
        } else if (!getStudy().isDeployed()) {
            addActionError("The study '"
                    + getStudy().getShortTitleText()
                    + "' is not yet deployed.");
            return;
        } 
    }
    
    /**
     * @param functionDescription the function description to display in the error message.
     */
    public void validateStudyHasGenomicData(String functionDescription) {
        if (!hasActionErrors() && !getStudy().hasGenomicDataSources()) {
            addActionError("There are no genomic data defined for this study, "
                    + "unable to perform " + functionDescription + ".");
        }
    }
    
    /**
     * Get all control sample set names in the study.
     * @return all control sample set names.
     */
    public List<String> getControlSampleSets() {
        return getStudy().getStudyConfiguration().getControlSampleSetNames();
    }
}
