/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

/**
 * The configuration for loading a gene location file.
 */
public class GeneLocationConfiguration extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    private GenomeBuildVersionEnum genomeBuildVersion;
    private Status status = Status.NOT_LOADED;
    private String statusDescription;
    private Set<GeneChromosomalLocation> geneLocations = new HashSet<GeneChromosomalLocation>();

    
    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }
    
    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * @return the genomeBuildVersion
     */
    public GenomeBuildVersionEnum getGenomeBuildVersion() {
        return genomeBuildVersion;
    }

    /**
     * @param genomeBuildVersion the genomeBuildVersion to set
     */
    public void setGenomeBuildVersion(GenomeBuildVersionEnum genomeBuildVersion) {
        this.genomeBuildVersion = genomeBuildVersion;
    }

    /**
     * @return the geneLocations
     */
    public Set<GeneChromosomalLocation> getGeneLocations() {
        return geneLocations;
    }

    /**
     * @param geneLocations the geneLocations to set
     */
    public void setGeneLocations(Set<GeneChromosomalLocation> geneLocations) {
        this.geneLocations = geneLocations;
    }

}
