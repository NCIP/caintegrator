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
import gov.nih.nci.caintegrator.application.study.NumericAnnotationValueGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultValue;


public final class ResultValueGenerator extends AbstractTestDataGenerator<ResultValue> {

    public static final ResultValueGenerator INSTANCE = new ResultValueGenerator();
    
    private ResultValueGenerator() {
        super();
    }

    @Override
    public void compareFields(ResultValue original, ResultValue retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getColumn(), retrieved.getColumn());
        NumericAnnotationValue nav1 = null;
        NumericAnnotationValue nav2 = null;
        nav1 = (NumericAnnotationValue)original.getValue();
        nav2 = (NumericAnnotationValue)retrieved.getValue();
        NumericAnnotationValueGenerator.INSTANCE.compare(nav1, nav2);
        
    }


    @Override
    public ResultValue createPersistentObject() {
        return new ResultValue();
    }


    @Override
    public void setValues(ResultValue resultValue, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        NumericAnnotationValue nav = new NumericAnnotationValue();
        NumericAnnotationValueGenerator.INSTANCE.setValues(nav, nonCascadedObjects);
        resultValue.setValue(nav);
        ResultColumn col = new ResultColumn();
        ResultColumnGenerator.INSTANCE.setValues(col, nonCascadedObjects);
        resultValue.setColumn(col);
        

    }

}
