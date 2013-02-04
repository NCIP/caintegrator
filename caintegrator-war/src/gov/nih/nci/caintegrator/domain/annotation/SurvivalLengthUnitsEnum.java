/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Possible survival length type values for <code>SurvivalValueDefinition.survivalLengthUnits</code>.
 */
public enum SurvivalLengthUnitsEnum {

    /**
     * Days.
     */
    DAYS("Days"),

    /**
     * Weeks.
     */
    WEEKS("Weeks"),
    
    /**
     * Months.
     */
    MONTHS("Months");
    
    private static Map<String, SurvivalLengthUnitsEnum> valueToTypeMap = new HashMap<String, SurvivalLengthUnitsEnum>();

    private String value;
    
    private SurvivalLengthUnitsEnum(String value) {
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
    
    /**
     * Displayable values.
     * @return Values to display in the dropdown list.
     */
    public static List<String> getDisplayableValues() {
        List<String> values = new ArrayList<String>();
        values.add(DAYS.getValue());
        values.add(WEEKS.getValue());
        values.add(MONTHS.getValue());
        return values;
    }

    private static Map<String, SurvivalLengthUnitsEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (SurvivalLengthUnitsEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>SurvivalLengthUnitsEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static SurvivalLengthUnitsEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>SurvivalLengthUnitsEnum</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
}
