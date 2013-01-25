/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GenomicValueResultsTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomicValueResultsTypeEnum.GENE_EXPRESSION, GenomicValueResultsTypeEnum.getByValue(GenomicValueResultsTypeEnum.GENE_EXPRESSION.getValue()));
    }
    
    @Test
    public void testCheckType() {
        assertFalse(GenomicValueResultsTypeEnum.checkType("not found"));
        assertFalse(GenomicValueResultsTypeEnum.checkType(null));
    }

}
