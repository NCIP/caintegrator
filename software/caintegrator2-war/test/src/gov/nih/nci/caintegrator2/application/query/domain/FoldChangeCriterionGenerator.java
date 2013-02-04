/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleGenerator;

import java.util.Set;

/**
 * Gets called by the QueryResultTestIntegration through the CompoundCriterionGenerator.
 */
public final class FoldChangeCriterionGenerator extends AbstractTestDataGenerator<FoldChangeCriterion> {

    public static final FoldChangeCriterionGenerator INSTANCE = new FoldChangeCriterionGenerator();
    
    private FoldChangeCriterionGenerator() { 
        super();
    }
    @Override
    public void compareFields(FoldChangeCriterion original, FoldChangeCriterion retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertTrue(retrieved.getCompareToSampleSet().getSamples().isEmpty());
        assertEquals(original.getFoldsUp(), retrieved.getFoldsUp());
        assertEquals(original.getFoldsDown(), retrieved.getFoldsDown());
        assertEquals(original.getRegulationType(), retrieved.getRegulationType());
        
    }

    @Override
    public FoldChangeCriterion createPersistentObject() {
        return new FoldChangeCriterion();
    }

    @Override
    public void setValues(FoldChangeCriterion foldChangeCriterion, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        foldChangeCriterion.setFoldsUp(Float.valueOf(getUniqueInt()));
        foldChangeCriterion.setFoldsDown(Float.valueOf(getUniqueInt()));
        foldChangeCriterion.getCompareToSampleSet().getSamples().clear();
        nonCascadedObjects.add(foldChangeCriterion.getCompareToSampleSet());
        for (int i = 0; i < 3; i++) {
            Sample sample = SampleGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects);
            nonCascadedObjects.add(sample);
            foldChangeCriterion.getCompareToSampleSet().getSamples().add(sample);
        }
        foldChangeCriterion.setRegulationType(getNewEnumValue(foldChangeCriterion.getRegulationType(), RegulationTypeEnum.values()));
    }

}
