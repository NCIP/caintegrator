/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;


abstract public class AbstractReporterGenerator<T extends AbstractReporter> extends AbstractTestDataGenerator<T> {
    
     AbstractReporterGenerator() { 
        super();
    }
    
    @Override
    public void compareFields(T original, T retrieved) {
        assertEquals(original.getName(), retrieved.getName());
        assertEquals(original.getIndex(), retrieved.getIndex());
        compareCollections(original.getGenes(), retrieved.getGenes(), GeneGenerator.INSTANCE);
    }

    @Override
    public void setValues(T reporter, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        reporter.setName(getUniqueString());
        reporter.getGenes().clear();
        for (int i = 0; i < 3; i++) {
            reporter.getGenes().add(GeneGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects));
        }
    }

    public static void compare(List<AbstractReporter> originalReporters, List<AbstractReporter> retreivedReporters) {
        assertEquals(originalReporters.size(), retreivedReporters.size());
        for (int i = 0; i < originalReporters.size(); i++) {
            if (originalReporters.get(i) instanceof GeneExpressionReporter) {
                assertTrue(retreivedReporters.get(i) instanceof GeneExpressionReporter);
                GeneExpressionReporterGenerator.INSTANCE.compare((GeneExpressionReporter) originalReporters.get(i), (GeneExpressionReporter) retreivedReporters.get(i));
            } else if (originalReporters.get(i) instanceof DnaAnalysisReporter) {
                assertTrue(retreivedReporters.get(i) instanceof DnaAnalysisReporter);
                DnaAnalysisReporterGenerator.INSTANCE.compare((DnaAnalysisReporter) originalReporters.get(i), (DnaAnalysisReporter) retreivedReporters.get(i));
            }
        }
    }

}
