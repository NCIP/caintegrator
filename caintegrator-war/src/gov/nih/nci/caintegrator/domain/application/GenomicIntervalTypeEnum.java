/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Genomic Interval values for <code>CopyNumberAlterationCriterion.genomicIntervalType</code>.
 */
public enum GenomicIntervalTypeEnum {

    /**
     * Gene Name.
     */
    GENE_NAME("Gene Name"),

    /**
     * Chromosome Coordinates.
     */
    CHROMOSOME_COORDINATES("Chromosome Coordinates");

    private String value;

    private GenomicIntervalTypeEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
