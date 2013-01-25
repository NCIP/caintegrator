/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Set;


public final class GeneExpressionReporterGenerator extends AbstractReporterGenerator<GeneExpressionReporter> {

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
    public void setValues(GeneExpressionReporter geneExpressionReporter, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        super.setValues(geneExpressionReporter, nonCascadedObjects);
    }

}
