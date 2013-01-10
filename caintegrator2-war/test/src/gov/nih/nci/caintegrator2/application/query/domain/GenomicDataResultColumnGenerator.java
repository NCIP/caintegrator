/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
/**
 * 
 */
public final class GenomicDataResultColumnGenerator extends AbstractTestDataGenerator<GenomicDataResultColumn> {

    public static final GenomicDataResultColumnGenerator INSTANCE = new GenomicDataResultColumnGenerator();
    
    private GenomicDataResultColumnGenerator() {
        super();
    }

    @Override
    public void compareFields(GenomicDataResultColumn original, GenomicDataResultColumn retrieved) {
        assertEquals(original.getId(), retrieved.getId());
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
