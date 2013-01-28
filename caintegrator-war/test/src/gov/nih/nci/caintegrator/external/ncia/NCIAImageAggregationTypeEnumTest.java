/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.external.ncia.NCIAImageAggregationTypeEnum;

import org.junit.Test;

public class NCIAImageAggregationTypeEnumTest {

    @Test
    public void testGetValue() {
        assertEquals("imageSeries", NCIAImageAggregationTypeEnum.IMAGESERIES.getValue());
    }

    @Test
    public void testGetByValue() {
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESERIES, NCIAImageAggregationTypeEnum.getByValue("imageSeries"));
        assertNull(NCIAImageAggregationTypeEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        NCIAImageAggregationTypeEnum.checkType("no match");
    }
    
    @Test
    public void testCheckTypeValid() {
        NCIAImageAggregationTypeEnum.checkType("imageSeries");
    }

}
