/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.investigation;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;


/**
 * 
 */
public class ViewControlSamplesAction extends AbstractDeployedStudyAction {
    
    private static final long serialVersionUID = 1L;
    
    private GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
    
    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (genomicSource.getId() != null) {
            genomicSource = getWorkspaceService().getRefreshedEntity(genomicSource);
        }
        if (genomicSource.getId() == null || genomicSource.getStudyConfiguration().getStudy() != getCurrentStudy()) {
            setInvalidDataBeingAccessed(true);
        }
    }
    
    @Override
    public String execute() {
        return SUCCESS;
    }

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @param genomicSource the genomicSource to set
     */
    public void setGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        this.genomicSource = genomicSource;
    }
}
