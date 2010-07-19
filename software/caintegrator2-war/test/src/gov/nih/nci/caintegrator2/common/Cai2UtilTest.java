/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.file.FileManagerImpl;

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


public class Cai2UtilTest {


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
}
