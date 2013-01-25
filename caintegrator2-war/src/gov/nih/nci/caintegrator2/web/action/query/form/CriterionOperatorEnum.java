/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;


import java.util.HashMap;
import java.util.Map;

/**
 * Query criteria operator types.
 */
public enum CriterionOperatorEnum {

    /**
     * Equals.
     */
    EQUALS("equals"),
    
    /**
     * Not Equal To.
     */
    NOT_EQUAL_TO("not equal to"),

    /**
     * Begins with.
     */
    BEGINS_WITH("begins with"),

    /**
     * Contains.
     */
    CONTAINS("contains"),

    /**
     * Ends with.
     */
    ENDS_WITH("ends with"),

    /**
     * Equals.
     */
    GREATER_THAN("greater than"),

    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO("greater than or equal to"),

    /**
     * Less than.
     */
    LESS_THAN("less than"),

    /**
     * Less than or equal to.
     */
    LESS_THAN_OR_EQUAL_TO("less than or equal to"),

    /**
     * In.
     */
    IN("in");
    
    static final CriterionOperatorEnum[] EMPTY = new CriterionOperatorEnum[0];
    static final CriterionOperatorEnum[] STRING_OPERATORS = new CriterionOperatorEnum[] {
        EQUALS, BEGINS_WITH, CONTAINS, ENDS_WITH, NOT_EQUAL_TO
    };
    static final CriterionOperatorEnum[] NUMERIC_OPERATORS = new CriterionOperatorEnum[] {
        EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO
    };
    static final CriterionOperatorEnum[] DATE_OPERATORS = new CriterionOperatorEnum[] {
        EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO
    };
    static final CriterionOperatorEnum[] SELECT_LIST_OPERATORS = new CriterionOperatorEnum[] {
        EQUALS, IN
    };
    static final CriterionOperatorEnum[] GENOMIC_OPERATORS = new CriterionOperatorEnum[] {
        EQUALS
    };
    
    private static Map<String, CriterionOperatorEnum> valueToTypeMap = new HashMap<String, CriterionOperatorEnum>();

    private final String value;
    
    private CriterionOperatorEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private static Map<String, CriterionOperatorEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (CriterionOperatorEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>CriterionRowTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static CriterionOperatorEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
}
