/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests loading type retrieval from enum.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class ArrayDataLoadingTypeEnumTest {

    /**
     * Tests loading type retrieval.
     */
    @Test
    public void getLoadingTypes() {
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AFFYMETRIX,
                PlatformDataTypeEnum.EXPRESSION).contains(ArrayDataLoadingTypeEnum.PARSED_DATA));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AFFYMETRIX,
                PlatformDataTypeEnum.COPY_NUMBER).contains(ArrayDataLoadingTypeEnum.CNCHP));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AFFYMETRIX,
                PlatformDataTypeEnum.SNP).contains(ArrayDataLoadingTypeEnum.CHP));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AGILENT,
                PlatformDataTypeEnum.EXPRESSION).contains(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AGILENT,
                PlatformDataTypeEnum.COPY_NUMBER).contains(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AGILENT,
                PlatformDataTypeEnum.SNP).contains(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE));
    }
}
