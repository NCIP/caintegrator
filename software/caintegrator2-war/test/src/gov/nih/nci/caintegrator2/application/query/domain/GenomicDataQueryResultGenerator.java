/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;

import java.util.Set;


public final class GenomicDataQueryResultGenerator extends AbstractTestDataGenerator<GenomicDataQueryResult> {

    public static final GenomicDataQueryResultGenerator INSTANCE = new GenomicDataQueryResultGenerator();
    
    private GenomicDataQueryResultGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataQueryResult original, GenomicDataQueryResult retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        QueryGenerator.INSTANCE.compareFields(original.getQuery(), retrieved.getQuery());
        assertEquals(original.getRowCollection().size(), retrieved.getRowCollection().size());
    }


    @Override
    public GenomicDataQueryResult createPersistentObject() {
        return new GenomicDataQueryResult();
    }


    @Override
    public void setValues(GenomicDataQueryResult queryResult, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        Query query = new Query();
        QueryGenerator.INSTANCE.setValues(query, nonCascadedObjects);
        queryResult.setQuery(query);
        for (int i = 0; i < 3; i++) {
            queryResult.getRowCollection().add(GenomicDataResultRowGenerator.INSTANCE.createPersistentObject());
        }
    }

}

