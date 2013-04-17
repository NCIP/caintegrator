/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;



/**
 * Supported Platforms.
 */
public enum PlatformVendorEnum {

    /**
     * Affymetrix platform.
     */
    AFFYMETRIX("Affymetrix", 5, 3),

    /**
     * Agilent platform.
     */
    AGILENT("Agilent", 6, 6);

    private String value;
    private int sampleMappingColumns;
    private int dnaAnalysisMappingColumns;

    private PlatformVendorEnum(String value, int sampleMappingColumns, int dnaAnalysisMappingColumns) {
        this.value = value;
        this.sampleMappingColumns = sampleMappingColumns;
        this.dnaAnalysisMappingColumns = dnaAnalysisMappingColumns;
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
     * @return the sampleMappingColumns
     */
    public int getSampleMappingColumns() {
        return sampleMappingColumns;
    }

    /**
     * @param sampleMappingColumns the sampleMappingColumns to set
     */
    public void setSampleMappingColumns(int sampleMappingColumns) {
        this.sampleMappingColumns = sampleMappingColumns;
    }

    /**
     * @return the dnaAnalysisMappingColumns
     */
    public int getDnaAnalysisMappingColumns() {
        return dnaAnalysisMappingColumns;
    }

    /**
     * @param dnaAnalysisMappingColumns the dnaAnalysisMappingColumns to set
     */
    public void setDnaAnalysisMappingColumns(int dnaAnalysisMappingColumns) {
        this.dnaAnalysisMappingColumns = dnaAnalysisMappingColumns;
    }
}
