/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;


import org.apache.commons.lang3.ArrayUtils;

/**
 * Supported Platforms.
 */
public enum PlatformTypeEnum {

    /**
     * Affymetrix Gene Expression platform.
     */
    AFFYMETRIX_GENE_EXPRESSION("Affymetrix Gene Expression", PlatformDataTypeEnum.EXPRESSION),

    /**
     * Affymetrix SNP platform.
     */
    AFFYMETRIX_SNP("Affymetrix SNP", PlatformDataTypeEnum.SNP),

    /**
     * Affymetrix Copy Number platform.
     */
    AFFYMETRIX_COPY_NUMBER("Affymetrix Copy Number", PlatformDataTypeEnum.COPY_NUMBER),

    /**
     * Agilent Gene Expression platform.
     */
    AGILENT_GENE_EXPRESSION("Agilent Gene Expression", PlatformDataTypeEnum.EXPRESSION),

    /**
     * Agilent Copy Number platform.
     */
    AGILENT_COPY_NUMBER("Agilent Copy Number", PlatformDataTypeEnum.COPY_NUMBER);

    private static final PlatformTypeEnum[] ENABLED_VALUES = {AFFYMETRIX_GENE_EXPRESSION, AFFYMETRIX_COPY_NUMBER,
        AGILENT_GENE_EXPRESSION, AGILENT_COPY_NUMBER };

    private String value;
    private PlatformDataTypeEnum dataType;

    private PlatformTypeEnum(String value, PlatformDataTypeEnum dataType) {
        this.value = value;
        this.dataType = dataType;
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
     * @return the dataType
     */
    public PlatformDataTypeEnum getDataType() {
        return dataType;
    }

    /**
     * The enabled platforms.
     * @return the platforms that are available to the user for usage.
     */
    public static PlatformTypeEnum[] enabledPlatforms() {
        return ArrayUtils.clone(ENABLED_VALUES);
    }

    /**
     * @return boolean is a gene expression data type.
     */
    public boolean isGeneExpression() {
        return PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getDataType() == getDataType();
    }

    /**
     *
     * @return boolean is a copy number data type
     */
    public boolean isCopyNumber() {
        return PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getDataType() == getDataType();
    }
}
