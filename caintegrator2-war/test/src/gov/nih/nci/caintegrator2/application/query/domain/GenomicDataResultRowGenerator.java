/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.Set;


public final class GenomicDataResultRowGenerator extends AbstractTestDataGenerator<GenomicDataResultRow> {

    public static final GenomicDataResultRowGenerator INSTANCE = new GenomicDataResultRowGenerator();
    
    private GenomicDataResultRowGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataResultRow original, GenomicDataResultRow retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getValues().size(), retrieved.getValues().size());
    }


    @Override
    public GenomicDataResultRow createPersistentObject() {
        return new GenomicDataResultRow();
    }


    @Override
    public void setValues(GenomicDataResultRow resultRow, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        for (int i = 0; i < 3; i++) {
            resultRow.getValues().add(GenomicDataResultValueGenerator.INSTANCE.createPersistentObject());
        }
        resultRow.setReporter(new GeneExpressionReporter());
        resultRow.getReporter().setIndex(0);
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        resultRow.getReporter().setReporterList(reporterList);
    }

}
