package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;

import static org.junit.Assert.assertEquals;

public class SampleGenerator extends AbstractTestDataGenerator<Sample> {
    
    public static final SampleGenerator INSTANCE = new SampleGenerator();

    @Override
    public void compareFields(Sample original, Sample retrieved) {
        assertEquals(original.getName(), retrieved.getName());
    }

    @Override
    public Sample createPersistentObject() {
        return new Sample();
    }

    @Override
    public void setValues(Sample sample) {
        sample.setName(getUniqueString());
    }


}
