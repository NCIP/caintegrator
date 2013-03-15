/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Set;


/**
 * Gene expression data generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class GeneExpressionReporterGenerator extends AbstractReporterGenerator<GeneExpressionReporter> {

    /**
     * The instance.
     */
    public static final GeneExpressionReporterGenerator INSTANCE = new GeneExpressionReporterGenerator();

    private GeneExpressionReporterGenerator() {
        super();
    }

    @Override
    public void compareFields(GeneExpressionReporter original, GeneExpressionReporter retrieved) {
        super.compareFields(original, retrieved);
    }

    @Override
    public GeneExpressionReporter createPersistentObject() {
        return new GeneExpressionReporter();
    }

    @Override
    public void setValues(GeneExpressionReporter geneExpressionReporter,
            Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        super.setValues(geneExpressionReporter, nonCascadedObjects);
    }

}
