/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action;

import java.util.List;

/**
 * Base class for all Struts 2 <code>Actions</code> in the application, provides context set up
 * for the current request.
 */
public abstract class AbstractDeployedStudyAction extends AbstractCaIntegrator2Action {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getCurrentStudy() == null) {
            setInvalidDataBeingAccessed(true);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (getStudySubscription() == null) {
            addActionError(getText("struts.messages.error.study.not.selected"));
            return;
        } else if (!getStudy().isDeployed()) {
            addActionError(getText("struts.messages.error.study.not.deployed", 
                    getArgs(getStudy().getShortTitleText())));
            return;
        } 
    }
    
    /**
     * @param functionDescription the function description to display in the error message.
     */
    public void validateStudyHasGenomicData(String functionDescription) {
        if (!hasActionErrors() && !getStudy().hasGenomicDataSources()) {
            addActionError(getText("struts.messages.error.study.has.no.genomic.data", getArgs(functionDescription)));
        }
    }
    
    /**
     * Get all control sample set names in the study.
     * @return all control sample set names.
     */
    public List<String> getControlSampleSets() {
        return getStudy().getStudyConfiguration().getControlSampleSetNames(null);
    }
}
