package gov.nih.nci.caintegrator2.domain.application;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;

/**
 * 
 */
public class SegmentDataResultValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private final List<Gene> genes = new ArrayList<Gene>();
    private ChromosomalLocation chromosomalLocation;
    
    /**
     * @return the genes
     */
    public List<Gene> getGenes() {
        return genes;
    }

    /**
     * @return the chromosomalLocation
     */
    public ChromosomalLocation getChromosomalLocation() {
        return chromosomalLocation;
    }
    /**
     * @param chromosomalLocation the chromosomalLocation to set
     */
    public void setChromosomalLocation(ChromosomalLocation chromosomalLocation) {
        this.chromosomalLocation = chromosomalLocation;
    }
    /**
     * @return the display list of genes
     */
    public String getDisplayGenes() {
        return StringUtils.join(genes, ", ");
    }
    /**
     * @param segmentDataResultValue to compare
     * @return the comparator code
     */
    public int compareTo(SegmentDataResultValue segmentDataResultValue) {
        int i = chromosomalLocation.getChromosome().compareTo(
                    segmentDataResultValue.getChromosomalLocation().getChromosome());
        if (i == 0) {
            i = chromosomalLocation.getStartPosition().compareTo(
                    segmentDataResultValue.getChromosomalLocation().getStartPosition());
        }
        if (i == 0) {
            i = chromosomalLocation.getEndPosition().compareTo(
                    segmentDataResultValue.getChromosomalLocation().getEndPosition());
        }
        return i;
    }
}