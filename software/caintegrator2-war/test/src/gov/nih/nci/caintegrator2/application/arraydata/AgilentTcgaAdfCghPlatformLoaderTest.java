/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.*;

import java.util.SortedSet;

import org.junit.Test;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

/**
 * 
 */
public class AgilentTcgaAdfCghPlatformLoaderTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();

    
    @Test
    public void testLoad() throws PlatformLoadingException {
        AgilentDnaPlatformSource agilentDnaPlatformSource = new AgilentDnaPlatformSource (
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_FILE,
                "AgilentPlatform",
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_PATH);

        AgilentTcgaAdfCghPlatformLoader loader = new AgilentTcgaAdfCghPlatformLoader(
                agilentDnaPlatformSource);
        
        Platform platform = loader.load(dao);
        assertTrue("AgilentPlatform".equals(loader.getPlatformName()));
        assertTrue("AgilentPlatform".equals(platform.getName()));
        SortedSet<ReporterList> reporterLists = platform.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertEquals(1, reporterLists.size());
        ReporterList reporterList = reporterLists.iterator().next();
        assertTrue("Hg18".equals(reporterList.getGenomeVersion()));
        assertEquals(5, reporterList.getReporters().size());
        
        // Test wrong header file
        agilentDnaPlatformSource = new AgilentDnaPlatformSource (
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_FILE,
                "AgilentPlatform",
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_PATH);

        loader = new AgilentTcgaAdfCghPlatformLoader(
                agilentDnaPlatformSource);
        boolean hasException = false;
        try {
            platform = loader.load(dao);
        } catch (PlatformLoadingException e) {
            hasException = true;
            assertEquals("Invalid file format; Headers not match.", e.getMessage());
        }
        assertTrue(hasException);
    }
}
