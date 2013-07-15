/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.investigation;

import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * View control samples action.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ViewControlSamplesAction extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;

    private GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();

    /**
     * {@inheritDoc}
     */
    @Override
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
