/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import java.io.Serializable;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.primitives.Ints;

/**
 * Represents a segment of a chromosome.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class ChromosomalLocation implements Serializable, Comparable<ChromosomalLocation> {

    private static final long serialVersionUID = 1L;

    private String chromosome;
    private int startPosition;
    private int endPosition;

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
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * @return the endPosition
     */
    public int getEndPosition() {
        return endPosition;
    }

    /**
     * @param endPosition the endPosition to set
     */
    public void setEndPosition(int endPosition) {
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
        if (NumberUtils.isNumber(getChromosome()) && NumberUtils.isNumber(location.getChromosome())) {
            return Ints.compare(NumberUtils.toInt(getChromosome()), NumberUtils.toInt(location.getChromosome()));
        } else {
            return getChromosome().compareTo(location.getChromosome());
        }
    }

    private int comparePosition(ChromosomalLocation location) {
        return Ints.compare(getStartPosition(), location.getStartPosition());
    }
}
