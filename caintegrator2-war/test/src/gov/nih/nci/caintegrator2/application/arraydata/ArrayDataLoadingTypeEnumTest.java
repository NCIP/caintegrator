/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrayDataLoadingTypeEnumTest {

    @Test
    public void testGetValue() {
    }

    @Test
    public void testGetByValue() {
        assertEquals(ArrayDataLoadingTypeEnum.PARSED_DATA,
                ArrayDataLoadingTypeEnum.getByValue(ArrayDataLoadingTypeEnum.PARSED_DATA.getValue()));
        assertEquals(ArrayDataLoadingTypeEnum.CHP,
                ArrayDataLoadingTypeEnum.getByValue(ArrayDataLoadingTypeEnum.CHP.getValue()));
        assertEquals(ArrayDataLoadingTypeEnum.CNCHP,
                ArrayDataLoadingTypeEnum.getByValue(ArrayDataLoadingTypeEnum.CNCHP.getValue()));
        assertEquals(ArrayDataLoadingTypeEnum.SINGLE_SAMPLE_PER_FILE,
                ArrayDataLoadingTypeEnum.getByValue(ArrayDataLoadingTypeEnum.SINGLE_SAMPLE_PER_FILE.getValue()));
        assertEquals(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE,
                ArrayDataLoadingTypeEnum.getByValue(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        ArrayDataLoadingTypeEnum.checkType("not found");
    }

    @Test
    public void testGetLoadingTypes() {
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AFFYMETRIX,
                PlatformDataTypeEnum.EXPRESSION).contains(
                        ArrayDataLoadingTypeEnum.PARSED_DATA.getValue()));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AFFYMETRIX,
                PlatformDataTypeEnum.COPY_NUMBER).contains(
                        ArrayDataLoadingTypeEnum.CNCHP.getValue()));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AFFYMETRIX,
                PlatformDataTypeEnum.SNP).contains(
                        ArrayDataLoadingTypeEnum.CHP.getValue()));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AGILENT,
                PlatformDataTypeEnum.EXPRESSION).contains(
                        ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE.getValue()));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AGILENT,
                PlatformDataTypeEnum.COPY_NUMBER).contains(
                        ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE.getValue()));
        assertTrue(ArrayDataLoadingTypeEnum.getLoadingTypes(PlatformVendorEnum.AGILENT,
                PlatformDataTypeEnum.SNP).contains(
                        ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE.getValue()));
    }

}
