/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.kmplot;



/**
 * Plot types.
 */
public enum PlotTypeEnum {

    /**
     * Annotation Based.
     */
    ANNOTATION_BASED("annotationBased"),

    /**
     * Gene Expression.
     */
    GENE_EXPRESSION("geneExpression"),
    /**
     * Query Based.
     */
    QUERY_BASED("queryBased"),
    /**
     * Genomic Query Based.
     */
    GENOMIC_QUERY_BASED("genomicQueryBased"),
    /**
     * Clinical Query Based.
     */
    CLINICAL_QUERY_BASED("clinicalQueryBased");

    private String value;

    private PlotTypeEnum(String value) {
        setValue(value);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }
}
