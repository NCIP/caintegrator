/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class GenomicDataResultRowComparatorTest {

    @Test
    public void testCompare() {
        Gene egfr = new Gene();
        egfr.setSymbol("egfr");
        Gene brca1 = new Gene();
        brca1.setSymbol("brca1");
        
        GenomicDataResultRow resultRow1 = new GenomicDataResultRow();
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        reporter1.setName("xyz");
        resultRow1.setReporter(reporter1);
        reporter1.getGenes().add(egfr);
        
        
        GenomicDataResultRow resultRow2 = new GenomicDataResultRow();
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporter2.setName("abc");
        resultRow2.setReporter(reporter2);
        reporter2.getGenes().add(egfr);
        
        
        GenomicDataResultRow resultRow3 = new GenomicDataResultRow();
        GeneExpressionReporter reporter3 = new GeneExpressionReporter();
        reporter3.setName("zzz");
        resultRow3.setReporter(reporter3);
        reporter3.getGenes().add(brca1);
        
        List<GenomicDataResultRow> resultRows = new ArrayList<GenomicDataResultRow>();
        resultRows.add(resultRow1);
        resultRows.add(resultRow2);
        resultRows.add(resultRow3);
        
        Collections.sort(resultRows, new GenomicDataResultRowComparator());
        
        assertEquals(resultRow3, resultRows.get(0));
        assertEquals(resultRow2, resultRows.get(1));
        assertEquals(resultRow1, resultRows.get(2));
        
    }

}
