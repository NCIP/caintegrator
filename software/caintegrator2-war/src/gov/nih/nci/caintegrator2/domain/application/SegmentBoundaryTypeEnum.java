/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Segment boundary type values for <code>CopyNumberAlterationCriterion.segmentBoundaryType</code>.
 */
public enum SegmentBoundaryTypeEnum {

    /**
     * One or more.
     */
    ONE_OR_MORE("One or more"),
    
    /**
     * Inclusive.
     */
    INCLUSIVE("Inclusive");

    
    private static Map<String, SegmentBoundaryTypeEnum> valueToTypeMap = 
        new HashMap<String, SegmentBoundaryTypeEnum>();
    
    private String value;
    
    
    private SegmentBoundaryTypeEnum(String value) {
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


    private static Map<String, SegmentBoundaryTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (SegmentBoundaryTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>SegmentBoundaryTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static SegmentBoundaryTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>SegmentBoundaryTypeEnum</code> value.
     * 
     * @param value the value to check;
     * @return T/F value depending on if is a valid type.
     */
    public static boolean checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            return false;
        }
        return true;
    }
}
