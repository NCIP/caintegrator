/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

/**
 * Parameter type.
 */
public enum AnalysisParameterType {

    /**
     * Float.
     */
    FLOAT,

    /**
     * Integer.
     */
    INTEGER,

    /**
     * String.
     */
    STRING,

    /**
     * <code>GenomicDataResultSet</code>.
     */
    GENOMIC_DATA, 
    
    /**
     * Groups samples in <code>GENOMIC_DATA</code>.
     */
    SAMPLE_CLASSIFICATION;
    
}
