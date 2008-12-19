package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;

import org.junit.Test;

public class DisplayableQueryResultTest {

    @Test
    public void testDisplayableQueryResult() {
        DisplayableQueryResult result = getTestResult();
        assertEquals("Column1", result.getHeaders().get(0));
        assertEquals("Column2", result.getHeaders().get(1));
        assertTrue(result.getHasImageSeries());
        assertTrue(result.getHasSubjects());
        assertFalse(result.getHasSamples());
        assertEquals("value1", result.getRows().get(0).getValues().get(0));
        assertEquals("2.0", result.getRows().get(0).getValues().get(1));
        
        assertTrue(result.getSelectAll());
        result.setSelectAll(false);
        assertFalse(result.getSelectAll());
    }

    public static DisplayableQueryResult getTestResult() {
        QueryResult sourceResult = new QueryResult();
        Query query = new Query();
        sourceResult.setQuery(query);
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
        
        sourceResult.setRowCollection(new ArrayList<ResultRow>());
        ResultRow row1 = new ResultRow();
        row1.setValueCollection(new ArrayList<ResultValue>());
        ResultValue row1Value1 = new ResultValue();
        row1Value1.setColumn(column1);
        StringAnnotationValue annotationValue1 = new StringAnnotationValue();
        annotationValue1.setStringValue("value1");
        row1Value1.setValue(annotationValue1);
        row1.getValueCollection().add(row1Value1);
        ResultValue row1Value2 = new ResultValue();
        row1Value2.setColumn(column2);
        NumericAnnotationValue annotationValue2 = new NumericAnnotationValue();
        annotationValue2.setNumericValue((double) 2);
        row1Value2.setValue(annotationValue2);
        row1.getValueCollection().add(row1Value2);
        
        row1.setSubjectAssignment(new StudySubjectAssignment());
        row1.setImageSeries(new ImageSeries());
        sourceResult.getRowCollection().add(row1);
        
        DisplayableQueryResult result = new DisplayableQueryResult(sourceResult);
        return result;
    }

}
