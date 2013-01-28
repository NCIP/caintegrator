/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

/**
 * Represents a quantitation type of microarray data.
 */
public enum ArrayDataValueType {
    
    /**
     * Expression signal.
     */
    EXPRESSION_SIGNAL(Float.class),
    
    /**
     * DNA analysis log2ratio.
     */
    DNA_ANALYSIS_LOG2_RATIO(Float.class);
    
    private final Class<?> typeClass;

    ArrayDataValueType(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    /**
     * @return the typeClass
     */
    public Class<?> getTypeClass() {
        return typeClass;
    }

}
