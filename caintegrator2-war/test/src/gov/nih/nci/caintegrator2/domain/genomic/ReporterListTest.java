/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 */
public class ReporterListTest {

    @Test
    public void testSortAndLoadReporterIndexes() {
        Gene gene1 = new Gene();
        gene1.setSymbol("AAAA");
        Gene gene2 = new Gene();
        gene2.setSymbol("BBBB");
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        reporter1.getGenes().add(gene1);
        reporter1.setName("reporter1");
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporter2.getGenes().add(gene1);
        reporter2.setName("reporter2");
        GeneExpressionReporter reporter3 = new GeneExpressionReporter();
        reporter3.getGenes().add(gene2);
        reporter3.setName("reporter3");
        GeneExpressionReporter reporter4 = new GeneExpressionReporter();
        reporter4.setName("reporter4");
        GeneExpressionReporter reporter5 = new GeneExpressionReporter();
        reporter5.setName("reporter5");
        
        Platform platform = new Platform();
        ReporterList reporterList1 = platform.addReporterList("reporterList1", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList1.setId(1L);
        reporterList1.addReporter(reporter5);
        reporterList1.addReporter(reporter3);
        reporterList1.addReporter(reporter4);
        reporterList1.addReporter(reporter2);
        reporterList1.addReporter(reporter1);
        reporterList1.sortAndLoadReporterIndexes();
        assertEquals(reporter1, reporterList1.getReporters().get(0));
        assertEquals(0, (int) reporterList1.getReporters().get(0).getIndex());
        assertEquals(reporter2, reporterList1.getReporters().get(1));
        assertEquals(1, (int) reporterList1.getReporters().get(1).getIndex());
        assertEquals(reporter3, reporterList1.getReporters().get(2));
        assertEquals(2, (int) reporterList1.getReporters().get(2).getIndex());
        assertEquals(reporter4, reporterList1.getReporters().get(3));
        assertEquals(3, (int) reporterList1.getReporters().get(3).getIndex());
        assertEquals(reporter5, reporterList1.getReporters().get(4));
        assertEquals(4, (int) reporterList1.getReporters().get(4).getIndex());
        
        ReporterList reporterList0 = platform.addReporterList("reporterList0", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList0.setId(0L);
        GeneExpressionReporter reporter0 = new GeneExpressionReporter();
        reporter0.getGenes().add(gene1);
        reporter0.setName("reporter0");
        reporterList0.addReporter(reporter0);
        reporterList0.sortAndLoadReporterIndexes();
        
        reporterList1.setPlatform(platform);
        reporterList0.setPlatform(platform);

        assertEquals(0, (int) reporterList0.getReporters().get(0).getDataStorageIndex());
        assertEquals(1, (int) reporterList1.getReporters().get(0).getDataStorageIndex());
        assertEquals(2, (int) reporterList1.getReporters().get(1).getDataStorageIndex());
        assertEquals(3, (int) reporterList1.getReporters().get(2).getDataStorageIndex());
        assertEquals(4, (int) reporterList1.getReporters().get(3).getDataStorageIndex());
        assertEquals(5, (int) reporterList1.getReporters().get(4).getDataStorageIndex());
    }

}
