/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;


import org.apache.commons.lang3.ArrayUtils;

/**
 * Possible data types for genomic data.
 */
public enum PlatformDataTypeEnum {

    /**
     * Expression.
     */
    EXPRESSION("Expression"),

    /**
     * CopyNumber.
     */
    COPY_NUMBER("Copy Number"),

    /**
     * SNP.
     */
    SNP("SNP");


    private static final PlatformDataTypeEnum[] ENABLED_VALUES = {EXPRESSION, COPY_NUMBER};

    private String value;

    private PlatformDataTypeEnum(String value) {
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
     * The enabled platform data types.
     * @return the platform data types that are available to the user for usage.
     */
    public static PlatformDataTypeEnum[] enabledValues() {
        return ArrayUtils.clone(ENABLED_VALUES);
    }
}
