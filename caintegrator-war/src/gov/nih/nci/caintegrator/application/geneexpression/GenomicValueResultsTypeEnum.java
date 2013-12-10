/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;


/**
 *
 */
public enum GenomicValueResultsTypeEnum {

    /**
     * Mean.
     */
    GENE_EXPRESSION("Expression Intensity"),

    /**
     * Median.
     */
    FOLD_CHANGE("Fold Change");

    private String value;

    private GenomicValueResultsTypeEnum(String value) {
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
