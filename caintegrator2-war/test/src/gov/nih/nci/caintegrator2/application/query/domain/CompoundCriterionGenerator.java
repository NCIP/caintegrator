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
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/**
 * This generator gets called by the test QueryResultTestIntegration, through the QueryGenerator.
 */
public final class CompoundCriterionGenerator extends AbstractTestDataGenerator<CompoundCriterion> {

    public static final CompoundCriterionGenerator INSTANCE = new CompoundCriterionGenerator();
    
    private CompoundCriterionGenerator() { 
        super();
    }
    @Override
    public void compareFields(CompoundCriterion original, CompoundCriterion retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getBooleanOperator(), retrieved.getBooleanOperator());
        assertEquals(original.getCriterionCollection().size(), retrieved.getCriterionCollection().size());
        
        NumericComparisonCriterion originalNumericCriterion = null;
        NumericComparisonCriterion retrievedNumericCriterion = null;
        StringComparisonCriterion originalStringCriterion = null;
        StringComparisonCriterion retrievedStringCriterion = null;
        SelectedValueCriterion originalSelectedValueCriterion = null;
        SelectedValueCriterion retrievedSelectedValueCriterion = null;
        FoldChangeCriterion originalFoldChangeCriterion = null;
        FoldChangeCriterion retrievedFoldChangeCriterion = null;
        
        for (AbstractCriterion criterion : original.getCriterionCollection()) {
            if (criterion instanceof NumericComparisonCriterion) {
                originalNumericCriterion = (NumericComparisonCriterion) criterion;
            }
            if (criterion instanceof StringComparisonCriterion) {
                originalStringCriterion = (StringComparisonCriterion) criterion;
            }
            if (criterion instanceof SelectedValueCriterion) {
                originalSelectedValueCriterion = (SelectedValueCriterion) criterion;
            }
            if (criterion instanceof FoldChangeCriterion) {
                originalFoldChangeCriterion = (FoldChangeCriterion) criterion;
            }
        }
        
        for (AbstractCriterion criterion : retrieved.getCriterionCollection()) {
            if (criterion instanceof NumericComparisonCriterion) {
                retrievedNumericCriterion = (NumericComparisonCriterion) criterion;
            }
            if (criterion instanceof StringComparisonCriterion) {
                retrievedStringCriterion = (StringComparisonCriterion) criterion;
            }
            if (criterion instanceof SelectedValueCriterion) {
                retrievedSelectedValueCriterion = (SelectedValueCriterion) criterion;
            }
            if (criterion instanceof FoldChangeCriterion) {
                retrievedFoldChangeCriterion = (FoldChangeCriterion) criterion;
            }
        }
        
        NumericComparisonCriterionGenerator.INSTANCE.compare(originalNumericCriterion, retrievedNumericCriterion);
        StringComparisonCriterionGenerator.INSTANCE.compare(originalStringCriterion, retrievedStringCriterion);
        SelectedValueCriterionGenerator.INSTANCE.compare(originalSelectedValueCriterion, retrievedSelectedValueCriterion);
        FoldChangeCriterionGenerator.INSTANCE.compare(originalFoldChangeCriterion, retrievedFoldChangeCriterion);
    }

    @Override
    public CompoundCriterion createPersistentObject() {
        return new CompoundCriterion();
    }

    @Override
    public void setValues(CompoundCriterion compoundCriterion, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        compoundCriterion.setBooleanOperator(getNewEnumValue(compoundCriterion.getBooleanOperator(), BooleanOperatorEnum.values()));
        
        Collection<AbstractCriterion> abstractCriterionCollection = new HashSet<AbstractCriterion>();
        StringComparisonCriterion scc = new StringComparisonCriterion();
        StringComparisonCriterionGenerator.INSTANCE.setValues(scc, nonCascadedObjects);
        abstractCriterionCollection.add(scc);
        
        NumericComparisonCriterion ncc = new NumericComparisonCriterion();
        NumericComparisonCriterionGenerator.INSTANCE.setValues(ncc, nonCascadedObjects);
        abstractCriterionCollection.add(ncc);
        
        SelectedValueCriterion svc = new SelectedValueCriterion();
        SelectedValueCriterionGenerator.INSTANCE.setValues(svc, nonCascadedObjects);
        abstractCriterionCollection.add(svc);
        
        FoldChangeCriterion fc = new FoldChangeCriterion();
        FoldChangeCriterionGenerator.INSTANCE.setValues(fc, nonCascadedObjects);
        abstractCriterionCollection.add(fc);
        
        compoundCriterion.setCriterionCollection(abstractCriterionCollection);

    }

}
