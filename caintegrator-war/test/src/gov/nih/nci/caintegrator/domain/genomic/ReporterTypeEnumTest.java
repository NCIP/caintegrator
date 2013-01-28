/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import org.junit.Test;

public class ReporterTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ReporterTypeEnum.GENE_EXPRESSION_GENE, ReporterTypeEnum.getByValue(ReporterTypeEnum.GENE_EXPRESSION_GENE.getValue()));
        assertEquals(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, ReporterTypeEnum.getByValue(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        ReporterTypeEnum.checkType("not found");
    }
    
    @Test
    public void testGetValueToDisplayableMap() {
        assertEquals("Gene", ReporterTypeEnum.getValueToDisplayableMap().get(ReporterTypeEnum.GENE_EXPRESSION_GENE.getValue()));
        assertEquals("Reporter Id", ReporterTypeEnum.getValueToDisplayableMap().get(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue()));
    }

}
