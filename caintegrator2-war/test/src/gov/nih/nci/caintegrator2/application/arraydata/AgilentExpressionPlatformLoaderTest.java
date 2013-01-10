/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.SortedSet;

import org.junit.Test;

public class AgilentExpressionPlatformLoaderTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    
    @Test
    public void testLoad() throws PlatformLoadingException {
        // Test with csv file
        AgilentExpressionPlatformSource agilentExpressionPlatformSource = new AgilentExpressionPlatformSource (
                TestArrayDesignFiles.AGILENT_G4502A_07_01_ANNOTATION_TEST_FILE,
                "Agilent_G4502A",
                TestArrayDesignFiles.AGILENT_G4502A_07_01_ANNOTATION_TEST_PATH);

        AgilentExpressionPlatformLoader loader = new AgilentExpressionPlatformLoader(
                agilentExpressionPlatformSource);
        
        Platform platform = loader.load(dao);
        assertTrue("Agilent_G4502A".equals(loader.getPlatformName()));
        assertTrue("Agilent_G4502A".equals(platform.getName()));
        SortedSet<ReporterList> reporterLists = platform.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        assertEquals(1, reporterLists.size());
        ReporterList reporterList = reporterLists.iterator().next();
        assertEquals(10, reporterList.getReporters().size());

        // Test with ADF file
        agilentExpressionPlatformSource = new AgilentExpressionPlatformSource (
                TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_TEST_FILE,
                "Agilent_G4502A",
                TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_TEST_PATH);

        loader = new AgilentExpressionPlatformLoader(
                agilentExpressionPlatformSource);
        
        platform = loader.load(dao);
        assertTrue("Agilent_G4502A".equals(loader.getPlatformName()));
        assertTrue("Agilent_G4502A".equals(platform.getName()));
        reporterLists = platform.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        assertEquals(1, reporterLists.size());
        reporterList = reporterLists.iterator().next();
        assertEquals(4, reporterList.getReporters().size());

        // Test with XML file
        agilentExpressionPlatformSource = new AgilentExpressionPlatformSource (
                TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_FILE,
                "Agilent_G4502A",
                TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_PATH);

        AgilentExpressionXmlPlatformLoader xmlLoader = new AgilentExpressionXmlPlatformLoader(
                agilentExpressionPlatformSource);
        
        platform = xmlLoader.load(dao);
        assertTrue("Agilent-017804".equals(xmlLoader.getPlatformName()));
        assertTrue("Agilent-017804".equals(platform.getName()));
        reporterLists = platform.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        assertEquals(1, reporterLists.size());
        reporterList = reporterLists.iterator().next();
        assertEquals(4, reporterList.getReporters().size());
        
        // Test wrong header file
        agilentExpressionPlatformSource = new AgilentExpressionPlatformSource (
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_FILE,
                "Dummy",
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_PATH);

        loader = new AgilentExpressionPlatformLoader(
                agilentExpressionPlatformSource);
        boolean hasException = false;
        try {
            platform = loader.load(dao);
        } catch (PlatformLoadingException e) {
            hasException = true;
            assertEquals("Invalid Agilent annotation file; headers not found in file: mskcc.org_TCGA_HG-CGH-244A_v081008_Test_Bad.ADF",
                    e.getMessage());
        }
        assertTrue(hasException);
    }
}
