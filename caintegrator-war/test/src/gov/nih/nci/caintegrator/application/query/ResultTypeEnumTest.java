/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import org.junit.Test;

public class ResultTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ResultTypeEnum.CLINICAL, ResultTypeEnum.getByValue(ResultTypeEnum.CLINICAL.getValue()));
        assertEquals(ResultTypeEnum.GENE_EXPRESSION, ResultTypeEnum.getByValue(ResultTypeEnum.GENE_EXPRESSION.getValue()));
        assertEquals(ResultTypeEnum.COPY_NUMBER, ResultTypeEnum.getByValue(ResultTypeEnum.COPY_NUMBER.getValue()));
        assertEquals(ResultTypeEnum.IGV_VIEWER, ResultTypeEnum.getByValue(ResultTypeEnum.IGV_VIEWER.getValue()));
        assertEquals(ResultTypeEnum.HEATMAP_VIEWER, ResultTypeEnum.getByValue(ResultTypeEnum.HEATMAP_VIEWER.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        ResultTypeEnum.checkType("not found");
    }
    
    @Test
    public void testGetValueToDisplayableMap() {
        assertEquals("Annotation", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.CLINICAL.getValue()));
        assertEquals("Gene Expression", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.GENE_EXPRESSION.getValue()));
        assertEquals("Copy Number", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.COPY_NUMBER.getValue()));
        assertEquals("Integrative Genomics Viewer", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.IGV_VIEWER.getValue()));
        assertEquals("Heat Map Viewer", ResultTypeEnum.getValueToDisplayableMap().get(ResultTypeEnum.HEATMAP_VIEWER.getValue()));
    }
    
    @Test
    public void testIsReporterMatch() {
        assertFalse(ResultTypeEnum.COPY_NUMBER.isReporterMatch(ReporterTypeEnum.GENE_EXPRESSION_GENE));
        assertTrue(ResultTypeEnum.COPY_NUMBER.isReporterMatch(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
    }

}
