/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class FileColumnTest {

    @Test
    public void testCompareTo() {
        FileColumn column0 = new FileColumn();
        column0.setPosition((short) 0);
        column0.setId(Long.valueOf(1));
        column0.setName("Test");
        FileColumn column1 = new FileColumn();
        column1.setPosition((short) 1);
        column1.setId(Long.valueOf(1));
        FileColumn column2 = new FileColumn();
        column2.setId(Long.valueOf(1));
        column2.setPosition((short) 2);
        List<FileColumn> columns = new ArrayList<FileColumn>();
        columns.add(column1);
        columns.add(column0);
        columns.add(column2);
        Collections.sort(columns);
        assertEquals(column0, columns.get(0));
        assertEquals(column1, columns.get(1));
        assertEquals(column2, columns.get(2));
    }
    
    @Test
    public void testGetDataValues() throws ValidationException {
        AnnotationFile annotationFile = AnnotationFile.load(TestDataFiles.VALID_FILE, new CaIntegrator2DaoStub());
        List<String> dataValues = annotationFile.getColumns().get(0).getDataValues();
        assertEquals("100", dataValues.get(0));
        assertEquals("101", dataValues.get(1));
        dataValues = annotationFile.getColumns().get(3).getDataValues();
        assertEquals("N", dataValues.get(0));
        assertEquals("Y", dataValues.get(1));
    }
    
    @Test
    public void testGetUniqueDataValues() throws ValidationException {
        AnnotationFile annotationFile = AnnotationFile.load(TestDataFiles.VALID_FILE, new CaIntegrator2DaoStub());
        Set<String> stringDataValues = annotationFile.getColumns().get(0).getUniqueDataValues(String.class);
        assertTrue(stringDataValues.contains("100"));
        assertTrue(stringDataValues.contains("101"));
        
        Set<Double> doubleDataValues = annotationFile.getColumns().get(0).getUniqueDataValues(Double.class);
        assertTrue(doubleDataValues.contains(100.0));
        assertTrue(doubleDataValues.contains(101.0));
        
        Set<Date> dateDataValues = new HashSet<Date>();
        try {
            annotationFile.getColumns().get(0).getUniqueDataValues(Date.class);
            fail();
        } catch (ValidationException e) {
            // noop - should catch exception.
        }
        
        dateDataValues = annotationFile.getColumns().get(4).getUniqueDataValues(Date.class);
        assertEquals(2, dateDataValues.size());
        
        try {
            doubleDataValues = annotationFile.getColumns().get(4).getUniqueDataValues(Double.class);
            fail();
        } catch (ValidationException e) {
            // noop - should catch exception.
        }
        
    }

}
