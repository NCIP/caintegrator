/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import java.util.HashMap;
import java.util.Map;

/**
 * Query result types.
 */
public enum ResultsOrientationEnum {

    /**
     * Display the subjects, samples, etc. as the columns and the reporters as the rows.
     */
    SUBJECTS_AS_COLUMNS("subjectsAsColumns"),

    /**
     * Display the subjects, samples, etc. as the rows and the reporters as the columns.
     */
    SUBJECTS_AS_ROWS("subjectsAsRows");

    private static Map<String, ResultsOrientationEnum> valueToTypeMap = new HashMap<String, ResultsOrientationEnum>();

    private String value;
    
    private ResultsOrientationEnum(String value) {
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

    private static Map<String, ResultsOrientationEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ResultsOrientationEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator.application.query.ResultTypeEnum@getValueToDisplayableMap()" 
     *              listKey="key" listValue="value"
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static Map<String, String> getValueToDisplayableMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ResultsOrientationEnum.SUBJECTS_AS_COLUMNS.getValue(), "Genes in Rows / Subjects in Columns");
        map.put(ResultsOrientationEnum.SUBJECTS_AS_ROWS.getValue(), "Genes in Columns / Subjects in Rows");
        return map;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values for Copy Number results.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator.application.query.ResultTypeEnum@getValueToDisplayableMap()" 
     *              listKey="key" listValue="value"
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static Map<String, String> getCopyNumberValueToDisplayableMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ResultsOrientationEnum.SUBJECTS_AS_COLUMNS.getValue(), "Samples in Columns");
        map.put(ResultsOrientationEnum.SUBJECTS_AS_ROWS.getValue(), "Samples in Rows");
        return map;
    }
    
    /**
     * Returns the <code>ResultTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ResultsOrientationEnum getByValue(String value) {
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
