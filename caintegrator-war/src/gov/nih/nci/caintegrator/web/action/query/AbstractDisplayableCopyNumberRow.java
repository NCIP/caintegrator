/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import gov.nih.nci.caintegrator.common.Cai2Util;

/**
 * 
 */
abstract class AbstractDisplayableCopyNumberRow {

    private String chromosome;
    private String startPosition;
    private String endPosition;
    private String genes;

    /**
     * @return the display chromosome
     */
    public String getChromosome() {
        return Cai2Util.getDisplayChromosomeNumber(chromosome);
    }

    /**
     * @param chromosome the chromosome to set
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    /**
     * @return the startPosition
     */
    public String getStartPosition() {
        return startPosition;
    }

    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * @return the endPosition
     */
    public String getEndPosition() {
        return endPosition;
    }

    /**
     * @param endPosition the endPosition to set
     */
    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * @return the genes
     */
    public String getGenes() {
        return genes;
    }

    /**
     * @param genes the genes to set
     */
    public void setGenes(String genes) {
        this.genes = genes;
    }

}
