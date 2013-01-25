/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * Represents data for a single gene chromosomal location.
 */
public class GeneChromosomalLocation extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String geneSymbol;
    private ChromosomalLocation location;
    private GeneLocationConfiguration geneLocationConfiguration;

    /**
     * @return the location
     */
    public ChromosomalLocation getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(ChromosomalLocation location) {
        this.location = location;
    }

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     * @return the geneLocationConfiguration
     */
    public GeneLocationConfiguration getGeneLocationConfiguration() {
        return geneLocationConfiguration;
    }

    /**
     * @param geneLocationConfiguration the geneLocationConfiguration to set
     */
    public void setGeneLocationConfiguration(GeneLocationConfiguration geneLocationConfiguration) {
        this.geneLocationConfiguration = geneLocationConfiguration;
    }
}
