/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

/**
 * An AuthorizedGenomicDataSourceConfiguration is an <code>GenomicDataSourceConfiguration</code> that has been
 * authorized for inclusion in an <code>AuthorizedStudyElementsGroup</code>.
 */
public class AuthorizedGenomicDataSourceConfiguration extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private GenomicDataSourceConfiguration genomicDataSourceConfiguration;
    private AuthorizedStudyElementsGroup authorizedStudyElementsGroup;

   
    /**
     * @param authorizedStudyElementsGroup the authorizedStudyElementsGroup to set
     */
    public void setAuthorizedStudyElementsGroup(AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        this.authorizedStudyElementsGroup = authorizedStudyElementsGroup;
    }
    /**
     * @return the authorizedStudyElementsGroup
     */
    public AuthorizedStudyElementsGroup getAuthorizedStudyElementsGroup() {
        return authorizedStudyElementsGroup;
    }
    /**
     * @param genomicDataSourceConfiguration the genomicDataSourceConfiguration to set
     */
    public void setGenomicDataSourceConfiguration(GenomicDataSourceConfiguration genomicDataSourceConfiguration) {
        this.genomicDataSourceConfiguration = genomicDataSourceConfiguration;
    }
    /**
     * @return the genomicDataSourceConfiguration
     */
    public GenomicDataSourceConfiguration getGenomicDataSourceConfiguration() {
        return genomicDataSourceConfiguration;
    }

}
