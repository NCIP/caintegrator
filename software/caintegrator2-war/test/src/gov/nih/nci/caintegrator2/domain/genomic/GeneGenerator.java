/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

public class GeneGenerator extends AbstractTestDataGenerator<Gene> {

    public static final GeneGenerator INSTANCE = new GeneGenerator();;

    @Override
    public void compareFields(Gene original, Gene retrieved) {
        assertEquals(original.getSymbol(), retrieved.getSymbol());
    }

    @Override
    public Gene createPersistentObject() {
        return new Gene();
    }

    @Override
    public void setValues(Gene gene, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        gene.setEnsemblgeneID(getUniqueString());
        gene.setEntrezgeneID(getUniqueString());
        gene.setFullName(getUniqueString());
        gene.setGenbankAccession(getUniqueString());
        gene.setGenbankAccessionVersion(getUniqueString());
        gene.setSymbol(getUniqueString());
        gene.setUnigeneclusterID(getUniqueString());
    }

}
