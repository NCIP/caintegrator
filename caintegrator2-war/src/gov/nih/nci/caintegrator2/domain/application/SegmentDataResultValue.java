/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
        int i = 0;
        try {
            // Try casting the chromosome to an integer, if it fails then just compare as string.
            i = Integer.valueOf(chromosomalLocation.getChromosome()).compareTo(
                    Integer.valueOf(segmentDataResultValue.getChromosomalLocation().getChromosome()));
        } catch (Exception e) {
            i = chromosomalLocation.getChromosome().compareTo(
                    segmentDataResultValue.getChromosomalLocation().getChromosome());
        }
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
