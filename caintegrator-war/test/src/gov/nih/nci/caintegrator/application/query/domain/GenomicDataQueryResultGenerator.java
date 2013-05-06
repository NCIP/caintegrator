/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.Query;

import java.util.Set;

/**
 * Genomic data query result generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class GenomicDataQueryResultGenerator extends AbstractTestDataGenerator<GenomicDataQueryResult> {

    /**
     * Data generator.
     */
    public static final GenomicDataQueryResultGenerator INSTANCE = new GenomicDataQueryResultGenerator();

    private GenomicDataQueryResultGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataQueryResult original, GenomicDataQueryResult retrieved) {
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

