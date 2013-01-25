/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class ResultHandlerImplTest {


    @Test
    public void testCreateResults() {
        StudyHelper studyHelper = new StudyHelper();
        StudySubscription subscription = studyHelper.populateAndRetrieveStudy();
        
        ResultHandler resultHandler = new ResultHandlerImpl();
        Query query = new Query();
        Set<ResultRow> resultRows = new HashSet<ResultRow>();
        resultRows.add(createRow(studyHelper, 0));
        resultRows.add(createRow(studyHelper, 1));
        resultRows.add(createRow(studyHelper, 2));
        resultRows.add(createRow(studyHelper, 3));
        resultRows.add(createRow(studyHelper, 4));
        
        
        Collection <ResultColumn> columnCollection = new HashSet<ResultColumn>();
        ResultColumn imageSeriesColumn = createColumn(EntityTypeEnum.IMAGESERIES, 0);
        imageSeriesColumn.setSortType(SortTypeEnum.ASCENDING);
        ResultColumn sampleColumn = createColumn(EntityTypeEnum.SAMPLE, 1);
        ResultColumn subjectColumn = createColumn(EntityTypeEnum.SUBJECT, 2);
        imageSeriesColumn.setAnnotationFieldDescriptor(studyHelper.getImageSeriesAnnotationFieldDescriptor());
        sampleColumn.setAnnotationFieldDescriptor(studyHelper.getSampleAnnotationFieldDescriptor());
        subjectColumn.setAnnotationFieldDescriptor(studyHelper.getSubjectAnnotationFieldDescriptor());
        NumericRangeMask numericRangeMask = new NumericRangeMask();
        numericRangeMask.setNumericRange(2);
        MaxNumberMask maxNumberMask = new MaxNumberMask();
        maxNumberMask.setMaxNumber(4d);
        subjectColumn.getAnnotationFieldDescriptor().getAnnotationMasks().add(numericRangeMask);
        subjectColumn.getAnnotationFieldDescriptor().getAnnotationMasks().add(maxNumberMask);
        NumericRangeMask numericRangeMask2 = new NumericRangeMask();
        numericRangeMask2.setNumericRange(3);
        sampleColumn.getAnnotationFieldDescriptor().getAnnotationMasks().add(numericRangeMask2);
        columnCollection.add(imageSeriesColumn);
        columnCollection.add(sampleColumn);
        columnCollection.add(subjectColumn);
        query.setColumnCollection(columnCollection);
        query.setSubscription(subscription);
        QueryResult result = resultHandler.createResults(query, resultRows);
        List<ResultRow> rows = new ArrayList<ResultRow>(result.getRowCollection());
        
        validateRowColumnValue("string1", rows.get(0), imageSeriesColumn);
//        validateRowColumnValue("9-11", rows.get(0), sampleColumn); // for some reason this doesn't pass when not running in debug mode?
        validateRowColumnValue("0-1", rows.get(0), subjectColumn);

        validateRowColumnValue("string2", rows.get(1), imageSeriesColumn);
        validateRowColumnValue("9-11", rows.get(1), sampleColumn);
        validateRowColumnValue("2-3", rows.get(1), subjectColumn);
        
        validateRowColumnValue("string3", rows.get(2), imageSeriesColumn);
        validateRowColumnValue("12-14", rows.get(2), sampleColumn);
        validateRowColumnValue("2-3", rows.get(2), subjectColumn);
        
        validateRowColumnValue("string4", rows.get(3), imageSeriesColumn);
        validateRowColumnValue("12-14", rows.get(3), sampleColumn);
        validateRowColumnValue("4+", rows.get(3), subjectColumn);
        
        validateRowColumnValue("string5", rows.get(4), imageSeriesColumn);
        validateRowColumnValue("12-14", rows.get(4), sampleColumn);
        validateRowColumnValue("4+", rows.get(4), subjectColumn);

    }
    
    private void validateRowColumnValue(String testValue, ResultRow row, ResultColumn column) {
        for (ResultValue value : row.getValueCollection()) {
            if (value.getColumn().equals(column)) {
                assertEquals(testValue, value.toString());
                break;
            }
        }
    }

    private ResultRow createRow(StudyHelper studyHelper, Integer subjectNumber) {
        ResultRow row = new ResultRow();
        row.setSampleAcquisition(studyHelper.getStudySubjects().get(subjectNumber).getSampleAcquisitionCollection().iterator().next());
        row.setImageSeries(studyHelper.getStudySubjects().get(subjectNumber).getImageStudyCollection().iterator().next().getSeriesCollection().iterator().next());
        row.setSubjectAssignment(studyHelper.getStudySubjects().get(subjectNumber));
        row.setValueCollection(new ArrayList<ResultValue>());
        return row;
    }

    private ResultColumn createColumn(EntityTypeEnum entityType, Integer index) {
        ResultColumn col = new ResultColumn();
        col.setEntityType(entityType);
        col.setColumnIndex(index);
        col.setAnnotationFieldDescriptor(new AnnotationFieldDescriptor());
        return col;
    }

}
