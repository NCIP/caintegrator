/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;


import java.util.ArrayList;
import java.util.List;

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

    /**
     * Used to retrieve all string values (by the JSP for display purposes).
     * @param vendor the platform vendor
     * @param type the platform data type
     * @return List of all string values which represent the ENUM values.
     */
    public static List<ArrayDataLoadingTypeEnum> getLoadingTypes(PlatformVendorEnum vendor, PlatformDataTypeEnum type) {
        List<ArrayDataLoadingTypeEnum> list = new ArrayList<ArrayDataLoadingTypeEnum>();
        if (PlatformVendorEnum.AFFYMETRIX == vendor) {
            if (PlatformDataTypeEnum.EXPRESSION == type) {
                list.add(PARSED_DATA);
            } else if (PlatformDataTypeEnum.COPY_NUMBER == type) {
                list.add(CNCHP);
            } else {
                list.add(CHP);
            }
        } else if (PlatformVendorEnum.AGILENT == vendor) {
            list.add(PARSED_DATA);
        }
        list.add(SINGLE_SAMPLE_PER_FILE);
        list.add(MULTI_SAMPLE_PER_FILE);
        return list;
    }
}
