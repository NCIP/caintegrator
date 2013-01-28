/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.TimeStampable;
import gov.nih.nci.caintegrator.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.file.FileManagerImpl;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;


public class Cai2UtilTest extends AbstractMockitoTest {


    @Test
    public void testTrimDescription() {
        String string = "This is my test string, it may or may not need to be trimmed.";
        assertEquals(string, Cai2Util.trimDescription(string));
    }


    @Test
    public void testTrimStringIfTooLong() {
        String string = "This is my test string, it may or may not need to be trimmed.";
        try {
            Cai2Util.trimStringIfTooLong(string, 3);
            fail();
        } catch (IllegalArgumentException e) {
            // this is expected.
        }
        assertEquals(string.substring(0, 22) + "...", Cai2Util.trimStringIfTooLong(string, 25));

        assertEquals(string, Cai2Util.trimStringIfTooLong(string, 100));
    }


    @Test
    public void testContainsIgnoreCase() {
        Collection<String> stringsCollection = new HashSet<String>();
        stringsCollection.add("HELLO");
        assertTrue(Cai2Util.containsIgnoreCase(stringsCollection, "hello"));
        assertFalse(Cai2Util.containsIgnoreCase(stringsCollection, "helo"));
    }

    @Test
    public void testZipAndDeleteDirectory() throws IOException {
        FileManagerImpl fileManager = new FileManagerImpl();
        fileManager.setConfigurationHelper(configurationHelper);
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
        Cai2Util.printDirContents(tempDirectory);
        File zippedDirectory = Cai2Util.zipAndDeleteDirectory(tempDirectory.getCanonicalPath());
        assertEquals("cai2UtilTest.zip", zippedDirectory.getName());
        assertTrue(Cai2Util.isValidZipFile(zippedDirectory));
        assertFalse(tempDirectory.exists());
        Cai2Util.printDirContents(tempDirectory);
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
    public void testZipFileValidation() throws IOException {
        FileManagerImpl fileManager = new FileManagerImpl();
        fileManager.setConfigurationHelper(configurationHelper);
        File tempDirectory = fileManager.getNewTemporaryDirectory("cai2UtilTest");
        File destFile = new File(tempDirectory, "tempFile");
        File nullZipFile = Cai2Util.zipAndDeleteDirectory(tempDirectory.getCanonicalPath());
        assertNull(nullZipFile);
        assertFalse(Cai2Util.isValidZipFile(nullZipFile));
        // test if input is not a ZIP file
        assertFalse(Cai2Util.isValidZipFile(destFile));
    }

    @Test
    public void testByteArrayToFile() throws IOException {
        FileManagerImpl fileManager = new FileManagerImpl();
        fileManager.setConfigurationHelper(configurationHelper);
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
    public void testCreateGeneListFromString() {
        assertTrue(Cai2Util.createListFromCommaDelimitedString(null).isEmpty());
        assertEquals(2, Cai2Util.createListFromCommaDelimitedString("egfr, brca1").size());
    }

    @Test
    public void testRetrieveValidSurvivalValueDefinitions() {

        Set <SurvivalValueDefinition> survivalValues = new HashSet<SurvivalValueDefinition>();

        AnnotationDefinition validStartDate = new AnnotationDefinition();
        validStartDate.setDataType(AnnotationTypeEnum.DATE);

        AnnotationDefinition validDeathDate = new AnnotationDefinition();
        validDeathDate.setDataType(AnnotationTypeEnum.DATE);

        AnnotationDefinition validFollowupDate = new AnnotationDefinition();
        validFollowupDate.setDataType(AnnotationTypeEnum.DATE);

        SurvivalValueDefinition validSurvivalDefinition = new SurvivalValueDefinition();
        validSurvivalDefinition.setSurvivalStartDate(validStartDate);
        validSurvivalDefinition.setDeathDate(validDeathDate);
        validSurvivalDefinition.setLastFollowupDate(validFollowupDate);
        validSurvivalDefinition.setName("validName");

        SurvivalValueDefinition invalidSurvivalDefinition1 = new SurvivalValueDefinition(); // null start
        invalidSurvivalDefinition1.setDeathDate(validDeathDate);
        invalidSurvivalDefinition1.setLastFollowupDate(validFollowupDate);

        SurvivalValueDefinition invalidSurvivalDefinition2 = new SurvivalValueDefinition(); // null death
        invalidSurvivalDefinition2.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition2.setLastFollowupDate(validFollowupDate);

        SurvivalValueDefinition invalidSurvivalDefinition3 = new SurvivalValueDefinition(); // null followup
        invalidSurvivalDefinition3.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition3.setDeathDate(validDeathDate);

        SurvivalValueDefinition invalidSurvivalDefinition4 = new SurvivalValueDefinition(); // start and followup equal
        invalidSurvivalDefinition4.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition4.setLastFollowupDate(validStartDate);
        invalidSurvivalDefinition4.setDeathDate(validDeathDate);

        SurvivalValueDefinition invalidSurvivalDefinition5 = new SurvivalValueDefinition(); // followup and death are equal
        invalidSurvivalDefinition5.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition5.setLastFollowupDate(validFollowupDate);
        invalidSurvivalDefinition5.setDeathDate(validFollowupDate);

        SurvivalValueDefinition invalidSurvivalDefinition6 = new SurvivalValueDefinition(); // start and death are equal
        invalidSurvivalDefinition6.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition6.setLastFollowupDate(validFollowupDate);
        invalidSurvivalDefinition6.setDeathDate(validStartDate);

        SurvivalValueDefinition invalidSurvivalDefinition7 = new SurvivalValueDefinition(); // start not a date
        invalidSurvivalDefinition7.setSurvivalStartDate(new AnnotationDefinition());
        invalidSurvivalDefinition7.setLastFollowupDate(validFollowupDate);
        invalidSurvivalDefinition7.setDeathDate(validStartDate);

        SurvivalValueDefinition invalidSurvivalDefinition8 = new SurvivalValueDefinition(); // followup not a date
        invalidSurvivalDefinition8.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition8.setLastFollowupDate(new AnnotationDefinition());
        invalidSurvivalDefinition8.setDeathDate(validStartDate);

        SurvivalValueDefinition invalidSurvivalDefinition9 = new SurvivalValueDefinition(); // death not a date
        invalidSurvivalDefinition9.setSurvivalStartDate(validStartDate);
        invalidSurvivalDefinition9.setLastFollowupDate(validFollowupDate);
        invalidSurvivalDefinition9.setDeathDate(new AnnotationDefinition());

        survivalValues.add(validSurvivalDefinition);
        survivalValues.add(invalidSurvivalDefinition1);
        survivalValues.add(invalidSurvivalDefinition2);
        survivalValues.add(invalidSurvivalDefinition3);
        survivalValues.add(invalidSurvivalDefinition4);
        survivalValues.add(invalidSurvivalDefinition5);
        survivalValues.add(invalidSurvivalDefinition6);
        survivalValues.add(invalidSurvivalDefinition7);
        survivalValues.add(invalidSurvivalDefinition8);
        survivalValues.add(invalidSurvivalDefinition9);

        assertEquals(1, Cai2Util.retrieveValidSurvivalValueDefinitions(survivalValues).size());
    }

    @Test
    public void testLog2() {
        assertTrue(Cai2Util.log2(16) == 4);
        assertTrue(Cai2Util.antiLog2(4) == 16);
    }

    @Test
    public void testRetrieveLatestLastModifiedDate() {
        List<TimeStampable> timestampedObjects = new ArrayList<TimeStampable>();
        assertEquals(DateUtil.TIMESTAMP_UNAVAILABLE_STRING, Cai2Util.retrieveLatestLastModifiedDate(timestampedObjects));
        Query queryFirst = new Query();
        queryFirst.setLastModifiedDate(new Date());
        Query queryUnknown = new Query();
        Query queryLast = new Query();
        queryLast.setLastModifiedDate(new Date());

        timestampedObjects.add(queryUnknown);
        assertEquals(DateUtil.TIMESTAMP_UNAVAILABLE_STRING, Cai2Util.retrieveLatestLastModifiedDate(timestampedObjects));
        timestampedObjects.add(queryFirst);
        assertEquals(queryFirst.getDisplayableLastModifiedDate(), Cai2Util.retrieveLatestLastModifiedDate(timestampedObjects));
        timestampedObjects.add(queryLast);
        assertEquals(queryLast.getDisplayableLastModifiedDate(), Cai2Util.retrieveLatestLastModifiedDate(timestampedObjects));
    }

    @Test
    public void testRetrieveGisticAmplifiedDeletedGenes() {
        GisticAnalysis gisticAnalysis = new GisticAnalysis();
        List<Gene> amplifiedGenes = new ArrayList<Gene>();
        List<Gene> deletedGenes = new ArrayList<Gene>();
        Cai2Util.retrieveGisticAmplifiedDeletedGenes(gisticAnalysis, amplifiedGenes, deletedGenes);
        assertTrue(amplifiedGenes.isEmpty());
        assertTrue(deletedGenes.isEmpty());
        ReporterList reporterList = new ReporterList("gistic", ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        Gene gene1Amp = new Gene();
        gene1Amp.setSymbol("AMPGENE1");
        Gene gene2Amp = new Gene();
        gene2Amp.setSymbol("AMPGENE2");
        Gene gene1Del = new Gene();
        gene1Del.setSymbol("DELGENE1");
        Gene gene2Del = new Gene();
        gene2Del.setSymbol("DELGENE2");
        GisticGenomicRegionReporter reporter1 = new GisticGenomicRegionReporter();
        reporter1.setGeneAmplificationType(AmplificationTypeEnum.AMPLIFIED);
        reporter1.getGenes().add(gene2Amp);
        reporter1.getGenes().add(gene1Amp);

        GisticGenomicRegionReporter reporter2 = new GisticGenomicRegionReporter();
        reporter2.setGeneAmplificationType(AmplificationTypeEnum.DELETED);
        reporter2.getGenes().add(gene2Del);
        reporter2.getGenes().add(gene1Del);

        reporterList.addReporter(reporter1);
        reporterList.addReporter(reporter2);
        gisticAnalysis.setReporterList(reporterList);
        Cai2Util.retrieveGisticAmplifiedDeletedGenes(gisticAnalysis, amplifiedGenes, deletedGenes);
        assertEquals(2, amplifiedGenes.size());
        assertEquals(2, deletedGenes.size());
        assertEquals("AMPGENE1", amplifiedGenes.get(0).getSymbol());
        assertEquals("AMPGENE2", amplifiedGenes.get(1).getSymbol());
        assertEquals("DELGENE1", deletedGenes.get(0).getSymbol());
        assertEquals("DELGENE2", deletedGenes.get(1).getSymbol());
    }

    @Test
    public void testChromosome() {
        assertEquals("1", Cai2Util.getInternalChromosomeNumber("1"));
        assertEquals("23", Cai2Util.getInternalChromosomeNumber("X"));
        assertEquals("24", Cai2Util.getInternalChromosomeNumber("Y"));

        assertEquals("1", Cai2Util.getDisplayChromosomeNumber("1"));
        assertEquals("X / 23", Cai2Util.getDisplayChromosomeNumber("23"));
        assertEquals("Y / 24", Cai2Util.getDisplayChromosomeNumber("24"));

        assertNull(Cai2Util.getAlternateChromosome("1"));
        assertEquals("23", Cai2Util.getAlternateChromosome("X"));
        assertEquals("24", Cai2Util.getAlternateChromosome("Y"));
        assertEquals("X", Cai2Util.getAlternateChromosome("23"));
        assertEquals("Y", Cai2Util.getAlternateChromosome("24"));
    }

    @Test
    public void testGetHeapSize() {
        long heapSize = Cai2Util.getHeapSizeMB();
        System.out.println("Heap size = " + heapSize);
    }

}
