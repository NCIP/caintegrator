/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Set;


public final class SegmentDataGenerator extends AbstractTestDataGenerator<SegmentData> {

    public static final SegmentDataGenerator INSTANCE = new SegmentDataGenerator();

    private SegmentDataGenerator() {
        super();
    }

    @Override
    public void compareFields(SegmentData original, SegmentData retrieved) {
        assertEquals(original.getArrayData(), retrieved.getArrayData());
        assertEquals(original.getNumberOfMarkers(), retrieved.getNumberOfMarkers());
        assertEquals(original.getSegmentValue(), retrieved.getSegmentValue(), 0.0);
        if (original.getLocation() == null) {
            assertNull(retrieved.getLocation());
        } else {
            assertEquals(original.getLocation().getChromosome(), retrieved.getLocation().getChromosome());
            assertEquals(original.getLocation().getStartPosition(), retrieved.getLocation().getStartPosition());
            assertEquals(original.getLocation().getEndPosition(), retrieved.getLocation().getEndPosition());
        }
    }

    @Override
    public SegmentData createPersistentObject() {
        return new SegmentData();
    }

    @Override
    public void setValues(SegmentData segmentData, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        segmentData.setNumberOfMarkers(getUniqueInt());
        segmentData.setSegmentValue(getUniqueFloat());
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.getLocation().setChromosome(getUniqueString(2));
        segmentData.getLocation().setStartPosition(getUniqueInt());
        segmentData.getLocation().setEndPosition(getUniqueInt());
    }

}
