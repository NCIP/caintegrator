/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;


public final class ReporterListGenerator extends AbstractTestDataGenerator<ReporterList> {

    public static final ReporterListGenerator INSTANCE = new ReporterListGenerator();
    
    private ReporterListGenerator() {
        super();
    }

    @Override
    public void compareFields(ReporterList original, ReporterList retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getName(), retrieved.getName());
        assertEquals(original.getGenomeVersion(), retrieved.getGenomeVersion());
        AbstractReporterGenerator.compare(original.getReporters(), retrieved.getReporters());
    }


    @Override
    public ReporterList createPersistentObject() {
        return new ReporterList();
    }


    @Override
    public void setValues(ReporterList reporterList, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        reporterList.setGenomeVersion(getUniqueString());
        if (reporterList.getReporters() != null) {
            for (AbstractReporter reporter : reporterList.getReporters()) {
                reporter.setReporterList(null);
            }
        }
        reporterList.getReporters().clear();
        GeneExpressionReporter reporter = GeneExpressionReporterGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects);
        reporter.setIndex(0);
        reporter.setReporterList(reporterList);
        reporterList.getReporters().add(reporter);
        DnaAnalysisReporter reporter2 = DnaAnalysisReporterGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects);
        reporter2.setReporterList(reporterList);
        reporter2.setIndex(1);
        reporterList.getReporters().add(reporter2);
    }

}
