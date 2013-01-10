/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Possible data types for platform.
 */
public enum ArrayDataLoadingTypeEnum {

    /**
     * Parsed data.
     */
    PARSED_DATA("Parsed Data", false, false),

    /**
     * Supplemental CHP file.
     */
    CHP("Supplemental CHP file", true, false),

    /**
     * Supplemental CN CHP file.
     */
    CNCHP("Supplemental CNCHP file", true, false),

    /**
     * Supplemental single sample per file.
     */
    SINGLE_SAMPLE_PER_FILE("Supplemental Single Sample Per File", true, false),

    /**
     * Supplemental multiple samples per file.
     */
    MULTI_SAMPLE_PER_FILE("Supplemental Multi Samples Per File", true, true);

    private String value;
    private boolean useSupplementalFiles;
    private boolean multiSamplesPerFile;

    private ArrayDataLoadingTypeEnum(String value, boolean useSupplementalFiles, boolean multiSamplesPerFile) {
        this.value = value;
        this.useSupplementalFiles = useSupplementalFiles;
        this.multiSamplesPerFile = multiSamplesPerFile;
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

    private static Map<String, ArrayDataLoadingTypeEnum> valueToTypeMap =
        new HashMap<String, ArrayDataLoadingTypeEnum>();

    private static Map<String, ArrayDataLoadingTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ArrayDataLoadingTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }

    /**
     * Used to retrieve all string values (by the JSP for display purposes).
     * @param vendor the platform vendor
     * @param type the platform data type
     * @return List of all string values which represent the ENUM values.
     */
    public static List<String> getLoadingTypes(PlatformVendorEnum vendor, PlatformDataTypeEnum type) {
        List<String> list = new ArrayList<String>();
        if (PlatformVendorEnum.AFFYMETRIX.equals(vendor)) {
            if (PlatformDataTypeEnum.EXPRESSION.equals(type)) {
                list.add(PARSED_DATA.value);
            } else if (PlatformDataTypeEnum.COPY_NUMBER.equals(type)) {
                list.add(CNCHP.value);
            } else {
                list.add(CHP.value);
            }
        } else if (PlatformVendorEnum.AGILENT.equals(vendor)) {
            list.add(PARSED_DATA.value);
        }
        list.add(SINGLE_SAMPLE_PER_FILE.value);
        list.add(MULTI_SAMPLE_PER_FILE.value);
        return list;
    }

    /**
     * Returns the <code>ArrayDataLoadingTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     *
     * @param value the value to match
     * @return the matching type.
     */
    public static ArrayDataLoadingTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>ArrayDataLoadingTypeEnum</code> value.
     *
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }

    /**
     * @return multiSamplePerFile
     */
    public boolean isMultiSamplesPerFile() {
        return multiSamplesPerFile;
    }

    /**
     * @return useSupplementalFiles
     */
    public boolean isUseSupplementalFiles() {
        return useSupplementalFiles;
    }
}
