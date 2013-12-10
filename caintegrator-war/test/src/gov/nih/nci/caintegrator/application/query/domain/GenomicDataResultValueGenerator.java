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
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;

import java.util.Set;

/**
 * Data generator for genomic data result values.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class GenomicDataResultValueGenerator extends AbstractTestDataGenerator<GenomicDataResultValue> {

    /**
     * Generator instance.
     */
    public static final GenomicDataResultValueGenerator INSTANCE = new GenomicDataResultValueGenerator();

    private GenomicDataResultValueGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataResultValue original, GenomicDataResultValue retrieved) {
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
