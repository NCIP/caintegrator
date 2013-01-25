/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;

public class AffymetrixDnaAnalysisPlatformLoaderTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    
    @Test
    public void testLoad() throws PlatformLoadingException {
        List<File> annotationFiles = new ArrayList<File>();
        annotationFiles.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_TEST_FILE);
        annotationFiles.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_TEST_FILE);
        AffymetrixDnaPlatformSource affymetrixDnaPlatformSource = new AffymetrixDnaPlatformSource (
                annotationFiles, "AffymetrixDnaPlatform");

        AffymetrixDnaAnalysisPlatformLoader loader = new AffymetrixDnaAnalysisPlatformLoader(
                affymetrixDnaPlatformSource);
        
        Platform platform = loader.load(dao);
        assertTrue("AffymetrixDnaPlatform".equals(loader.getPlatformName()));
        assertTrue("AffymetrixDnaPlatform".equals(platform.getName()));
        SortedSet<ReporterList> reporterLists = platform.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertEquals(2, reporterLists.size());
        ReporterList reporterList = reporterLists.iterator().next();
        assertTrue("hg18".equals(reporterList.getGenomeVersion()));
        assertEquals(10, reporterList.getReporters().size());
        
        // Test wrong header file
        annotationFiles.clear();
        annotationFiles.add(TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_FILE);
        affymetrixDnaPlatformSource = new AffymetrixDnaPlatformSource (
                annotationFiles, "AffymetrixDnaPlatform");

        loader = new AffymetrixDnaAnalysisPlatformLoader(
                affymetrixDnaPlatformSource);
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
