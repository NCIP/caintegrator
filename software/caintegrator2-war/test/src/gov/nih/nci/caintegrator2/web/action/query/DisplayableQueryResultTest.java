package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class DisplayableQueryResultTest {
    
    private static StudySubjectAssignment assignment1;
    private static StudySubjectAssignment assignment2;

    @Test
    public void testDisplayableQueryResult() {
        DisplayableQueryResult result = getTestResult();
        assertEquals("Column1", result.getHeaders().get(0));
        assertEquals("Column2", result.getHeaders().get(1));
        assertTrue(result.getHasImageSeries());
        assertTrue(result.getHasSubjects());
        assertFalse(result.getHasSamples());
        assertEquals("value1", result.getRows().get(0).getValues().get(0).toString());
        assertEquals("2", result.getRows().get(0).getValues().get(1).toString());
        
        assertTrue(result.getSelectAll());
        result.setSelectAll(false);
        assertFalse(result.getSelectAll());
        assertTrue(result.getRows().get(0).isImagingRow());
    }

    public static DisplayableQueryResult getTestResult() {
        QueryResult sourceResult = new QueryResult();
        Query query = createQuery();
        sourceResult.setQuery(query);
        ResultRow row1 = retrieveImageSeriesRow(sourceResult, query);
        sourceResult.getRowCollection().add(row1);
        
        DisplayableQueryResult result = new DisplayableQueryResult(sourceResult);
        return result;
    }

    /**
     * @return
     */
    private static Query createQuery() {
        Query query = new Query();
        query.setColumnCollection(new ArrayList<ResultColumn>());
        
        ResultColumn column1 = new ResultColumn();
        AnnotationDefinition annotationDefinition1 = new AnnotationDefinition();
        annotationDefinition1.setDisplayName("Column1");
        column1.setAnnotationDefinition(annotationDefinition1 );
        column1.setColumnIndex(0);
        query.getColumnCollection().add(column1);
        
        ResultColumn column2 = new ResultColumn();
        AnnotationDefinition annotationDefinition2 = new AnnotationDefinition();
        annotationDefinition2.setDisplayName("Column2");
        column2.setAnnotationDefinition(annotationDefinition2 );
        column2.setColumnIndex(1);
        query.getColumnCollection().add(column2);
        
        ResultColumn column3 = new ResultColumn();
        AnnotationDefinition annotationDefinition3 = new AnnotationDefinition();
        annotationDefinition3.setDisplayName("Column3");
        column3.setAnnotationDefinition(annotationDefinition3);
        column3.setColumnIndex(2);
        query.getColumnCollection().add(column3);
        
        return query;
    }

    private static ResultRow retrieveImageSeriesRow(QueryResult sourceResult, Query query) {
        ResultColumn column1 = null;
        ResultColumn column2 = null;
        ResultColumn column3 = null;
        int x = 1;
        for (ResultColumn column : query.getColumnCollection()) {
            if (x == 1) {
                column1 = column;
            } else if (x == 2) {
                column2 = column;
            } else {
                column3 = column;
            }
            x++;
        }
        sourceResult.setRowCollection(new ArrayList<ResultRow>());
        ResultRow row = new ResultRow();
        row.setValueCollection(new ArrayList<ResultValue>());
        
        ResultValue row1Value1 = new ResultValue();
        row1Value1.setColumn(column1);
        StringAnnotationValue annotationValue1 = new StringAnnotationValue();
        annotationValue1.setStringValue("value1");
        row1Value1.setValue(annotationValue1);
        row.getValueCollection().add(row1Value1);
        
        ResultValue row1Value2 = new ResultValue();
        row1Value2.setColumn(column2);
        NumericAnnotationValue annotationValue2 = new NumericAnnotationValue();
        annotationValue2.setNumericValue((double) 2);
        row1Value2.setValue(annotationValue2);
        row.getValueCollection().add(row1Value2);

        ResultValue row1Value3 = new ResultValue();
        row1Value3.setColumn(column3);
        DateAnnotationValue annotationValue3 = new DateAnnotationValue();
        annotationValue3.setDateValue(new Date());
        row1Value3.setValue(annotationValue3);
        row.getValueCollection().add(row1Value3);
        
        assignment1 = new StudySubjectAssignment();
        assignment1.setId(Long.valueOf(1));
        row.setSubjectAssignment(assignment1);
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setIdentifier("1.2.3.4.5");
        row.setImageSeries(imageSeries);
        return row;
    }
    
    private static ResultRow retrieveImageStudyRow(QueryResult sourceResult, Query query) {
        ResultColumn column1 = null;
        ResultColumn column2 = null;
        int x = 1;
        for (ResultColumn column : query.getColumnCollection()) {
            if (x == 1) {
                column1 = column;
            } else {
                column2 = column;
            }
            x++;
        }
        sourceResult.setRowCollection(new ArrayList<ResultRow>());
        ResultRow row = new ResultRow();
        row.setValueCollection(new ArrayList<ResultValue>());
        ResultValue row1Value1 = new ResultValue();
        row1Value1.setColumn(column1);
        StringAnnotationValue annotationValue1 = new StringAnnotationValue();
        annotationValue1.setStringValue("value1");
        row1Value1.setValue(annotationValue1);
        row.getValueCollection().add(row1Value1);
        ResultValue row1Value2 = new ResultValue();
        row1Value2.setColumn(column2);
        NumericAnnotationValue annotationValue2 = new NumericAnnotationValue();
        annotationValue2.setNumericValue((double) 2);
        row1Value2.setValue(annotationValue2);
        row.getValueCollection().add(row1Value2);
        assignment2 = new StudySubjectAssignment();
        assignment2.setId(Long.valueOf(2));
        row.setSubjectAssignment(assignment2);
        ImageSeriesAcquisition acquisition = new ImageSeriesAcquisition();
        acquisition.setIdentifier("1.2.3.4");
        row.getSubjectAssignment().getImageStudyCollection().add(acquisition);
        return row;
    }
    
    
    public static DisplayableQueryResult getImagingSeriesResult() {
        QueryResult sourceResult = new QueryResult();
        Query query = createQuery();
        sourceResult.setQuery(query);
        ResultRow row1 = retrieveImageSeriesRow(sourceResult, query);
        sourceResult.getRowCollection().add(row1);
        
        DisplayableQueryResult result = new DisplayableQueryResult(sourceResult);
        return result;
    }
    
    public static DisplayableQueryResult getImageStudyResult() {
        QueryResult sourceResult = new QueryResult();
        Query query = createQuery();
        sourceResult.setQuery(query);
        ResultRow row1 = retrieveImageSeriesRow(sourceResult, query);
        sourceResult.getRowCollection().add(row1);
        ResultRow row2 = retrieveImageStudyRow(sourceResult, query);
        sourceResult.getRowCollection().add(row2);
        
        DisplayableQueryResult result = new DisplayableQueryResult(sourceResult);
        return result;
    }
    
    public static StudySubjectAssignment retrieveStudySubjectAssignment(Long id) {
        if (id.equals(assignment1.getId())) {
            return assignment1;
        } else if (id.equals(assignment2.getId())) {
            return assignment2;            
        }
        return null;
    }

}
