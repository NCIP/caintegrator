/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;


import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator.domain.genomic.Gene;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.primitives.Ints;

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
        if (NumberUtils.isNumber(chromosomalLocation.getChromosome())) {
            i = Ints.compare(NumberUtils.toInt(chromosomalLocation.getChromosome()),
                    NumberUtils.toInt(segmentDataResultValue.getChromosomalLocation().getChromosome()));
        } else {
            i = chromosomalLocation.getChromosome().compareTo(
                    segmentDataResultValue.getChromosomalLocation().getChromosome());
        }
        if (i == 0) {
            i = Ints.compare(chromosomalLocation.getStartPosition(),
                    segmentDataResultValue.getChromosomalLocation().getStartPosition());
        }
        if (i == 0) {
            i = Ints.compare(chromosomalLocation.getEndPosition(),
                    segmentDataResultValue.getChromosomalLocation().getEndPosition());
        }
        return i;
    }
}
