/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.file.FileManagerImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;


public class Cai2UtilTest {


    @Test
    public void testContainsIgnoreCase() {
        Collection<String> stringsCollection = new HashSet<String>();
        stringsCollection.add("HELLO");
        assertTrue(Cai2Util.containsIgnoreCase(stringsCollection, "hello"));
        assertFalse(Cai2Util.containsIgnoreCase(stringsCollection, "helo"));
    }


    @Test
    public void testResultRowSetContainsResultRow() {
        
        Set<ResultRow> rowSet = new HashSet<ResultRow>();
        ResultRow row1 = new ResultRow();
        StudySubjectAssignment subjectAssignment = new StudySubjectAssignment();
        subjectAssignment.setId(Long.valueOf(1));
        row1.setSubjectAssignment(subjectAssignment);
        rowSet.add(row1);
        ResultRow rowToTest = new ResultRow();
        rowToTest.setSubjectAssignment(subjectAssignment);
        
        assertTrue(Cai2Util.resultRowSetContainsResultRow(rowSet, rowToTest));
        
    }
    
    @Test
    public void testRetrieveValueFromRowColumn() {
        ResultRow row = new ResultRow();
        ResultColumn column = new ResultColumn();
        AnnotationDefinition testDef = new AnnotationDefinition();
        testDef.setId(Long.valueOf(1));
        column.setAnnotationDefinition(testDef);
        ResultValue nullValue = Cai2Util.retrieveValueFromRowColumn(row, column);
        
        assertNull(nullValue);
        
        ResultValue realValue = new ResultValue();
        realValue.setColumn(column);
        realValue.setId(Long.valueOf(2));
        Collection<ResultValue> valueCollection = new HashSet<ResultValue>();
        valueCollection.add(realValue);
        row.setValueCollection(valueCollection);
        
        ResultValue retrievedRealValue = Cai2Util.retrieveValueFromRowColumn(row, column);
        assertEquals(realValue, retrievedRealValue);
    }

    @Test
    public void testColumnCollectionContainsColumn() {
        Collection<ResultColumn> columnCollection = new HashSet<ResultColumn>();
        ResultColumn column = new ResultColumn();
        AnnotationDefinition ad = new AnnotationDefinition();
        column.setAnnotationDefinition(ad);
        ad.setId(Long.valueOf(1));
        assertFalse(Cai2Util.columnCollectionContainsColumn(columnCollection, column));
        columnCollection.add(column);
        assertTrue(Cai2Util.columnCollectionContainsColumn(columnCollection, column));
    }
    
    @Test
    public void testAnnotationValueBelongToPermissibleValue() {
        StringAnnotationValue stringValue = new StringAnnotationValue();
        stringValue.setStringValue("TeSt");
        PermissibleValue stringPermissibleValue = new PermissibleValue();
        stringPermissibleValue.setValue("tEsT");
        assertTrue(Cai2Util.annotationValueBelongToPermissibleValue(stringValue, stringPermissibleValue));
        stringPermissibleValue.setValue("Not Equals");
        assertFalse(Cai2Util.annotationValueBelongToPermissibleValue(stringValue, stringPermissibleValue));
        
        NumericAnnotationValue numericValue = new NumericAnnotationValue();
        numericValue.setNumericValue(50.0);
        PermissibleValue numericPermissibleValue = new PermissibleValue();
        numericPermissibleValue.setValue("50.0");
        assertTrue(Cai2Util.annotationValueBelongToPermissibleValue(numericValue, numericPermissibleValue));
        DateAnnotationValue dateValue = new DateAnnotationValue();
        long currentTime = System.currentTimeMillis();
        dateValue.setDateValue(new Date(currentTime));
        
        PermissibleValue datePermissibleValue = new PermissibleValue();
        datePermissibleValue.setValue(DateUtil.toString(new Date(currentTime)));
        
        assertTrue(Cai2Util.annotationValueBelongToPermissibleValue(dateValue, datePermissibleValue));
        
    }
    
    @Test
    public void testZipAndDeleteDirectory() throws IOException {
        FileManagerImpl fileManager = new FileManagerImpl();
        fileManager.setConfigurationHelper(new ConfigurationHelperStub());
        File tempDirectory = fileManager.getNewTemporaryDirectory("cai2UtilTest");
        File destFile = new File(tempDirectory, "tempFile");
        File nullZipFile = Cai2Util.zipAndDeleteDirectory(tempDirectory.getCanonicalPath());
        assertNull(nullZipFile);
        FileUtils.copyFile(TestDataFiles.VALID_FILE, destFile);
        try {
            Cai2Util.zipAndDeleteDirectory(destFile.getCanonicalPath());
            fail("Expected illegal argument exception, deleting a file and not directory.");
        } catch(IllegalArgumentException e) {
            
        }
        assertTrue(tempDirectory.exists());
        File zippedDirectory = Cai2Util.zipAndDeleteDirectory(tempDirectory.getCanonicalPath());
        assertEquals("cai2UtilTest.zip", zippedDirectory.getName());
        assertFalse(tempDirectory.exists());
        zippedDirectory.deleteOnExit();
        
        File tempDirectory2 = fileManager.getNewTemporaryDirectory("cai2UtilTest2");
        File destFile2 = new File(tempDirectory2, "tempFile2");
        FileUtils.copyFile(TestDataFiles.VALID_FILE, destFile2);
        File newZipFile = Cai2Util.addFilesToZipFile(zippedDirectory, destFile2);
        assertNotNull(newZipFile); // need to actually verify it contains 2 items now.
        FileUtils.deleteDirectory(tempDirectory);
        FileUtils.deleteDirectory(tempDirectory2);
        try {
            Cai2Util.addFilesToZipFile(TestDataFiles.VALID_FILE, new File(""));
            fail("Expected illegal argument exception, adding to a non-zip file");
        } catch (IllegalArgumentException e){
            
        }
        
    }
    
    @Test
    public void testByteArrayToFile() throws IOException {
        FileManagerImpl fileManager = new FileManagerImpl();
        fileManager.setConfigurationHelper(new ConfigurationHelperStub());
        File tempDirectory = fileManager.getNewTemporaryDirectory("cai2UtilTest3");
        File destFile = new File(tempDirectory, "tempFile");
        byte[] byteArray = "This is a test".getBytes();
        Cai2Util.byteArrayToFile(byteArray, destFile);
        assertEquals(destFile.length(), 14);
        InputStream is = new FileInputStream(destFile);
        byte[] byteCheck = new byte[14];
        is.read(byteCheck);
        is.close();
        String stringCheck = new String(byteCheck);
        assertTrue(stringCheck.equals("This is a test"));
        FileUtils.deleteDirectory(tempDirectory);
    }

    @Test
    public void testGetHostNameFromUrl() {
        String url = "https://imaging-dev.nci.nih.gov/ncia/externalDataBasketDisplay.jsf";
        assertEquals("imaging-dev.nci.nih.gov", Cai2Util.getHostNameFromUrl(url));
        assertNull(Cai2Util.getHostNameFromUrl(null));
        assertNull(Cai2Util.getHostNameFromUrl(""));
        assertNull(Cai2Util.getHostNameFromUrl("abc"));
    }
    
    @Test
    public void testIsCompoundCriterionGenomic() {
        CompoundCriterion compoundCriterion1 = new CompoundCriterion();
        compoundCriterion1.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterion compoundCriterion2 = new CompoundCriterion();
        compoundCriterion2.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion2.getCriterionCollection().add(new StringComparisonCriterion());
        compoundCriterion2.getCriterionCollection().add(new StringComparisonCriterion());
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        assertFalse(Cai2Util.isCompoundCriterionGenomic(compoundCriterion1));
        compoundCriterion1.getCriterionCollection().add(new GeneNameCriterion());
        assertTrue(Cai2Util.isCompoundCriterionGenomic(compoundCriterion1));
        
        compoundCriterion1.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        compoundCriterion1.getCriterionCollection().add(new StringComparisonCriterion());
        assertFalse(Cai2Util.isCompoundCriterionGenomic(compoundCriterion1));
        compoundCriterion2.getCriterionCollection().add(new FoldChangeCriterion());
        assertTrue(Cai2Util.isCompoundCriterionGenomic(compoundCriterion1));
    }
}
