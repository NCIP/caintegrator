/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

/**
 * Type of data in an <code>ArrayData</code> instance.
 */
public enum ArrayDataType {
    
    /**
     * Gene expression.
     */
    GENE_EXPRESSION,
    
    /**
     * Copy number.
     */
    COPY_NUMBER,
    
    /**
     * Gistic Analysis.
     */
    GISTIC_ANALYSIS;

}
