/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Set;

/**
 * Sample generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class SampleGenerator extends AbstractTestDataGenerator<Sample> {

    /**
     * The instance.
     */
    public static final SampleGenerator INSTANCE = new SampleGenerator();

    @Override
    public void compareFields(Sample original, Sample retrieved) {
        assertEquals(original.getName(), retrieved.getName());
        assertEquals(original.getArrayDataCollection().size(), retrieved.getArrayDataCollection().size());
        assertEquals(original.getArrayCollection().size(), retrieved.getArrayCollection().size());
        compareCollections(original.getArrayDataCollection(),
                retrieved.getArrayDataCollection(), ArrayDataGenerator.INSTANCE);
    }

    @Override
    public Sample createPersistentObject() {
        return new Sample();
    }

    @Override
    public void setValues(Sample sample, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        sample.setName(getUniqueString());
        sample.getArrayCollection().clear();
        sample.getArrayDataCollection().clear();
        ArrayData arrayData = ArrayDataGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects);
        Array array = new Array();
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        array.getSampleCollection().add(sample);
    }


}
