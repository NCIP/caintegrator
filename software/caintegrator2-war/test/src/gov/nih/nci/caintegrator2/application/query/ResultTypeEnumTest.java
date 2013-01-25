/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;

import org.junit.Test;

public class ResultTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ResultTypeEnum.CLINICAL, ResultTypeEnum.getByValue(ResultTypeEnum.CLINICAL.getValue()));
        assertEquals(ResultTypeEnum.GENOMIC, ResultTypeEnum.getByValue(ResultTypeEnum.GENOMIC.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        ResultTypeEnum.checkType("not found");
    }
    
    @Test
    public void testGetValueToDisplayableMap() {
        assertEquals("Annotation", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.CLINICAL.getValue()));
        assertEquals("Genomic", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.GENOMIC.getValue()));
    }

}
