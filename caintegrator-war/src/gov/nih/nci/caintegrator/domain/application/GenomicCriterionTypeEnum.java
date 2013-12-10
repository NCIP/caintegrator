/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;


/**
 * Possible entity type values for <code>AbstractAnnotationCriterion.entityType</code>.
 */
public enum GenomicCriterionTypeEnum {

    /**
     * Copy Number Type.
     */
    COPY_NUMBER("copyNumber"),

    /**
     * Gene Expression Type.
     */
    GENE_EXPRESSION("geneExpression");

    private String value;

    private GenomicCriterionTypeEnum(String value) {
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
