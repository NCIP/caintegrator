/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.Set;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.Gene;

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
        gene.setSymbol(getUniqueString().toUpperCase(Locale.getDefault()));
        gene.setUnigeneclusterID(getUniqueString());
    }

}
