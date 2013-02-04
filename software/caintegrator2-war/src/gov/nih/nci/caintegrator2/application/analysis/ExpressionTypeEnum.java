/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the expression type.
 */
public enum ExpressionTypeEnum {
    
    /**
     * Fold Change.
     */
    FOLD_CHANGE("By Fold Change"),
    
    /**
     * Expression Level.
     */
    EXPRESSION_LEVEL("By Expression Level");
    
    private String value;
    private static Map<String, ExpressionTypeEnum> valueToTypeMap = new HashMap<String, ExpressionTypeEnum>();
    
    private ExpressionTypeEnum(String value) {
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

    private static Map<String, ExpressionTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ExpressionTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>ExpressionTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ExpressionTypeEnum getByValue(String value) {
        return getValueToTypeMap().get(value);
    }
}
