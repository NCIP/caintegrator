/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.assertEquals;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Set;


public final class ArrayDataGenerator extends AbstractTestDataGenerator<ArrayData> {

    public static final ArrayDataGenerator INSTANCE = new ArrayDataGenerator();
    
    private ArrayDataGenerator() { 
        super();
    }
    
    @Override
    public void compareFields(ArrayData original, ArrayData retrieved) {
        assertEquals(original.getStudy(), retrieved.getStudy());
        assertEquals(original.getArray(), retrieved.getArray());
        compareCollections(original.getReporterLists(), retrieved.getReporterLists(), ReporterListGenerator.INSTANCE);
        assertEquals(original.getSample(), retrieved.getSample());
        assertEquals(original.getType(), retrieved.getType());
        compareCollections(original.getSegmentDatas(), retrieved.getSegmentDatas(), SegmentDataGenerator.INSTANCE);
    }

    @Override
    public ArrayData createPersistentObject() {
        return new ArrayData();
    }

    @Override
    public void setValues(ArrayData arrayData, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        arrayData.setType(getNewEnumValue(arrayData.getType(), ArrayDataType.values()));
        for (int i = 0; i < 3; i++) {
            SegmentData segmentData = SegmentDataGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects);
            segmentData.setArrayData(arrayData);
            arrayData.getSegmentDatas().add(segmentData);
        }
        for (int i = 0; i < 3; i++) {
            ReporterList reporterList = ReporterListGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects);
            reporterList.getArrayDatas().add(arrayData);
            arrayData.getReporterLists().add(reporterList);
            nonCascadedObjects.add(reporterList);
        }
    }

}
