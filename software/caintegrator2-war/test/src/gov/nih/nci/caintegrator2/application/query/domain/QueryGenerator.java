/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public final class QueryGenerator extends AbstractTestDataGenerator<Query> {

    public static final QueryGenerator INSTANCE = new QueryGenerator();
    
    private QueryGenerator() {
        super();
    }

    @Override
    public void compareFields(Query original, Query retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getDescription(), retrieved.getDescription());
        CompoundCriterionGenerator.INSTANCE.compare(original.getCompoundCriterion(), retrieved.getCompoundCriterion());
        assertEquals(original.getColumnCollection().size(), retrieved.getColumnCollection().size());
        assertEquals(original.getColumnCollection().size(), 3);
        assertEquals(original.getReporterType(), retrieved.getReporterType());
        assertEquals(original.getOrientation(), retrieved.getOrientation());
    }

    @Override
    public Query createPersistentObject() {
        return new Query();
    }

    @Override
    public void setValues(Query query, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        query.setDescription(getUniqueString());
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        query.setColumnCollection(new HashSet<ResultColumn>());
        for (int x=0; x<3; x++) {
            query.getColumnCollection().add(ResultColumnGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects));
        }
        query.setReporterType(getNewEnumValue(query.getReporterType(), ReporterTypeEnum.values()));
        query.setOrientation(getNewEnumValue(query.getOrientation(), ResultsOrientationEnum.values()));
        CompoundCriterionGenerator.INSTANCE.setValues(compoundCriterion, nonCascadedObjects);
        query.setCompoundCriterion(compoundCriterion);

    }

}
