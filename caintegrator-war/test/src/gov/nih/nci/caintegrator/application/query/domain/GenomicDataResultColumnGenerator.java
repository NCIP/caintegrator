/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;


/**
 * Genomic data result column generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class GenomicDataResultColumnGenerator extends AbstractTestDataGenerator<GenomicDataResultColumn> {

    /**
     * Data generator.
     */
    public static final GenomicDataResultColumnGenerator INSTANCE = new GenomicDataResultColumnGenerator();

    private GenomicDataResultColumnGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataResultColumn original, GenomicDataResultColumn retrieved) {
        assertEquals(original.getSampleAcquisition(), retrieved.getSampleAcquisition());
    }


    @Override
    public GenomicDataResultColumn createPersistentObject() {
        return new GenomicDataResultColumn();
    }


    @Override
    public void setValues(GenomicDataResultColumn rc, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        rc.setSampleAcquisition(new SampleAcquisition());
    }

}
