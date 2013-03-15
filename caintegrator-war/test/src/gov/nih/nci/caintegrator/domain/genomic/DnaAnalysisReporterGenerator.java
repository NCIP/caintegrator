/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Set;


/**
 * DNA analysis data generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class DnaAnalysisReporterGenerator extends AbstractReporterGenerator<DnaAnalysisReporter> {

    /**
     * The instance.
     */
    public static final DnaAnalysisReporterGenerator INSTANCE = new DnaAnalysisReporterGenerator();

    private DnaAnalysisReporterGenerator() {
        super();
    }

    @Override
    public void compareFields(DnaAnalysisReporter original, DnaAnalysisReporter retrieved) {
        assertEquals(original.getAlleleA(), retrieved.getAlleleA());
        assertEquals(original.getAlleleB(), retrieved.getAlleleB());
        assertEquals(original.getChromosome(), retrieved.getChromosome());
        assertEquals(original.getDbSnpId(), retrieved.getDbSnpId());
        assertEquals(original.getPosition(), retrieved.getPosition());
        super.compareFields(original, retrieved);
    }

    @Override
    public DnaAnalysisReporter createPersistentObject() {
        return new DnaAnalysisReporter();
    }

    @Override
    public void setValues(DnaAnalysisReporter reporter, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        super.setValues(reporter, nonCascadedObjects);
        reporter.setAlleleA(getUniqueChar());
        reporter.setAlleleB(getUniqueChar());
        reporter.setChromosome(getUniqueString(2));
        reporter.setDbSnpId(getUniqueString());
        reporter.setPosition(getUniqueInt());
    }

}
