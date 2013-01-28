/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static java.lang.Character.isDigit;

import java.io.Serializable;

/**
 * Represents a segment of a chromosome.
 */
public class ChromosomalLocation implements Serializable, Comparable<ChromosomalLocation> {
    
    private static final long serialVersionUID = 1L;
    
    private String chromosome;
    private Integer startPosition;
    private Integer endPosition;
    
    /**
     * @return the chromosome
     */
    public String getChromosome() {
        return chromosome;
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
    public Integer getStartPosition() {
        return startPosition;
    }
    
    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }
    
    /**
     * @return the endPosition
     */
    public Integer getEndPosition() {
        return endPosition;
    }
    
    /**
     * @param endPosition the endPosition to set
     */
    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }
    
    /**
     * {@inheritDoc}
     */
    public int compareTo(ChromosomalLocation location) {
        int chromosomeComparison = compareChromosome(location);
        return chromosomeComparison == 0 ? comparePosition(location) : chromosomeComparison;
        
    }

    private int compareChromosome(ChromosomalLocation location) {
        if (getChromosome() == null) {
            return location.getChromosome() == null ? 0 : 1;
        } else if (location.getChromosome() == null) {
            return -1;
        } else {
            return compareChromosomeStrings(location);
        }
    }

    private int compareChromosomeStrings(ChromosomalLocation location) {
        if (isDigit(getChromosome().charAt(0)) && isDigit(location.getChromosome().charAt(0))) {
            return Integer.valueOf(getChromosome()) - Integer.valueOf(location.getChromosome());
        } else {
            return getChromosome().compareTo(location.getChromosome());
        }
    }

    private int comparePosition(ChromosomalLocation location) {
        if (getStartPosition() == null) {
            return location.getStartPosition() == null ? 0 : 1;
        } else if (location.getStartPosition() == null) {
            return -1;
        } else {
            return getStartPosition().compareTo(location.getStartPosition());
        }
    }

}
