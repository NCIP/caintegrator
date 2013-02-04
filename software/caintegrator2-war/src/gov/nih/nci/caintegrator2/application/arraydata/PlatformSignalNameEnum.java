/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Possible names of the signal column in the array data
 * as it appears in the genomic data source files in caArray.
 */
public enum PlatformSignalNameEnum {

    /**
     * One-Color platform signal name.
     */
    AFFYMETRIX_SIGNAL_NAME("CHPSignal"),
    
    /**
     * One-Color platform signal name.
     */
    ONE_COLOR_SIGNAL_NAME("gProcessedSignal"),
    
    /**
     * Two-Color platform signal name.
     */
    TWO_COLOR_SIGNAL_NAME("gProcessedSignal");
    
    private static Map<String, PlatformSignalNameEnum> valueToTypeMap = new HashMap<String, PlatformSignalNameEnum>();

    private String value;
    
    private PlatformSignalNameEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private static Map<String, PlatformSignalNameEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (PlatformSignalNameEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>PlatformSignalNameEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static PlatformSignalNameEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value == null) {
            throw new IllegalArgumentException("No matching type for null");
        } else if (!getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformSignalNameEnum@getValuesToDisplay()" 
     * 
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static List<String> getValuesToDisplay() {
        List<String> list = new ArrayList<String>();
        for (PlatformSignalNameEnum type : values()) {
            list.add(type.getValue());
        }
        return list;
    }
}
