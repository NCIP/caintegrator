/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;


public final class GenomicDataResultValueGenerator extends AbstractTestDataGenerator<GenomicDataResultValue> {

    public static final GenomicDataResultValueGenerator INSTANCE = new GenomicDataResultValueGenerator();
    
    private GenomicDataResultValueGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataResultValue original, GenomicDataResultValue retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getColumn(), retrieved.getColumn());
        
        
    }


    @Override
    public GenomicDataResultValue createPersistentObject() {
        return new GenomicDataResultValue();
    }


    @Override
    public void setValues(GenomicDataResultValue resultValue, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        resultValue.setValue(Float.valueOf(getUniqueInt()));
        GenomicDataResultColumn col = new GenomicDataResultColumn();
        GenomicDataResultColumnGenerator.INSTANCE.setValues(col, nonCascadedObjects);
        resultValue.setColumn(col);
        

    }

}
