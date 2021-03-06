/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.SortTypeEnum;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests that the CompoundCriterionHandler object can get the matches for various CompoundCriterion.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@Transactional
public class QueryTranslatorTestIntegration {

    @Autowired
    private CaIntegrator2Dao dao;
    @Autowired
    private ArrayDataService arrayDataService;
    @Autowired
    private ResultHandlerImpl resultHandler;

    @Test
    public void testExecute() throws InvalidCriterionException {
        StudyHelper studyHelper = new StudyHelper();
        dao.save(studyHelper.getPlatform());
        StudySubscription studySubscription = studyHelper.populateAndRetrieveStudy();
        Study study = studySubscription.getStudy();
        dao.save(study);

        ResultColumn column1 = new ResultColumn();
        ResultColumn column2 = new ResultColumn();
        ResultColumn column3 = new ResultColumn();

        column1.setAnnotationFieldDescriptor(studyHelper.getImageSeriesAnnotationFieldDescriptor());
        column1.setColumnIndex(0);
        column1.setEntityType(EntityTypeEnum.IMAGESERIES);
        column2.setSortOrder(0);
        column2.setSortType(SortTypeEnum.UNSORTED);

        column2.setAnnotationFieldDescriptor(studyHelper.getSampleAnnotationFieldDescriptor());
        column2.setColumnIndex(1);
        column2.setEntityType(EntityTypeEnum.SAMPLE);
        column2.setSortOrder(2);
        column2.setSortType(SortTypeEnum.UNSORTED);

        column3.setAnnotationFieldDescriptor(studyHelper.getSubjectAnnotationFieldDescriptor());
        column3.setColumnIndex(2);
        column3.setEntityType(EntityTypeEnum.SUBJECT);
        column3.setSortOrder(1);
        column3.setSortType(SortTypeEnum.DESCENDING);

        Collection<ResultColumn> columnCollection = new HashSet<ResultColumn>();
        columnCollection.add(column1);
        columnCollection.add(column2);
        columnCollection.add(column3);

        CompoundCriterion compoundCriterion = studyHelper.createCompoundCriterion1();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.OR);

        Query query = new Query();
        query.setName("Test Query");
        query.setCompoundCriterion(compoundCriterion);
        query.setSubscription(studySubscription);
        query.setColumnCollection(columnCollection);

        QueryTranslator queryTranslator = new QueryTranslator(query, dao, arrayDataService, resultHandler);

        QueryResult queryResult = queryTranslator.execute();
        assertEquals(10, queryResult.getRowCollection().size());
        for (ResultRow row : queryResult.getRowCollection()) {
            assertNotNull(row.getRowIndex());
            if (row.getRowIndex() == 5) {
                // Subject should be 1 because these were sorted Descending
                for (SubjectAnnotation value : row.getSubjectAssignment().getSubjectAnnotationCollection()) {
                    NumericAnnotationValue numericValue = (NumericAnnotationValue) value.getAnnotationValue();
                    assertEquals(Double.valueOf(3.0), numericValue.getNumericValue());
                }
                // Sample should be 1.0 because samples are sorted Ascending
                for (AbstractAnnotationValue value : row.getSampleAcquisition().getAnnotationCollection()) {
                    NumericAnnotationValue numericValue = (NumericAnnotationValue) value;
                    assertEquals(Double.valueOf(12.0), numericValue.getNumericValue());
                }
            }

            if (row.getRowIndex() == 6) {
                // Subject should be 1 because these were sorted Descending
                for (SubjectAnnotation value : row.getSubjectAssignment().getSubjectAnnotationCollection()) {
                    NumericAnnotationValue numericValue = (NumericAnnotationValue) value.getAnnotationValue();
                    assertEquals(Double.valueOf(3.0), numericValue.getNumericValue());
                }
                // Sample should be 10.0 because samples are sorted Ascending
                for (AbstractAnnotationValue value : row.getSampleAcquisition().getAnnotationCollection()) {
                    NumericAnnotationValue numericValue = (NumericAnnotationValue) value;
                    assertEquals(Double.valueOf(12.0), numericValue.getNumericValue());
                }
            }
        }
    }
}
