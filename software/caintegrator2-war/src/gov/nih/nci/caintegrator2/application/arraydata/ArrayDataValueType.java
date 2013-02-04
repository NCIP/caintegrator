/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

/**
 * Represents a quantitation type of microarray data.
 */
public enum ArrayDataValueType {
    
    /**
     * Expression signal.
     */
    EXPRESSION_SIGNAL(Float.class),
    
    /**
     * Copy number log2ratio.
     */
    COPY_NUMBER_LOG2_RATIO(Float.class);
    
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
