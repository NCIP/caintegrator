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
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;
/**
 * 
 */
public final class ResultColumnGenerator extends AbstractTestDataGenerator<ResultColumn> {

    public static final ResultColumnGenerator INSTANCE = new ResultColumnGenerator();
    
    private ResultColumnGenerator() {
        super();
    }

    @Override
    public void compareFields(ResultColumn original, ResultColumn retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getAnnotationFieldDescriptor(), retrieved.getAnnotationFieldDescriptor());
        assertEquals(original.getColumnIndex(), retrieved.getColumnIndex());
        assertEquals(original.getEntityType(), retrieved.getEntityType());
        assertEquals(original.getSortOrder(), retrieved.getSortOrder());
        assertEquals(original.getSortType(), retrieved.getSortType());
    }


    @Override
    public ResultColumn createPersistentObject() {
        return new ResultColumn();
    }


    @Override
    public void setValues(ResultColumn rc, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        rc.setSortOrder(getUniqueInt());
        rc.setSortType(getNewEnumValue(rc.getSortType(), SortTypeEnum.values()));
        rc.setColumnIndex(getUniqueInt());
        rc.setEntityType(getNewEnumValue(rc.getEntityType(), EntityTypeEnum.values()));
    }

}
