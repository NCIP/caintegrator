/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixCnPlatformLoader;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixCnPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;

public class AffymetrixCnPlatformLoaderTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    
    @Test
    public void testLoad() throws PlatformLoadingException {
        List<File> annotationFiles = new ArrayList<File>();
        annotationFiles.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_TEST_FILE);
        AffymetrixCnPlatformSource affymetrixCnPlatformSource = new AffymetrixCnPlatformSource (
                annotationFiles, "AffymetrixCnPlatform");

        AffymetrixCnPlatformLoader loader = new AffymetrixCnPlatformLoader(
                affymetrixCnPlatformSource);
        
        Platform platform = loader.load(dao);
        assertTrue("AffymetrixCnPlatform".equals(loader.getPlatformName()));
        assertTrue("AffymetrixCnPlatform".equals(platform.getName()));
        SortedSet<ReporterList> reporterLists = platform.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertEquals(1, reporterLists.size());
        ReporterList reporterList = reporterLists.iterator().next();
        assertTrue("hg18".equals(reporterList.getGenomeVersion()));
        assertEquals(10, reporterList.getReporters().size());
        
        // Test wrong header file
        annotationFiles.clear();
        annotationFiles.add(TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_FILE);
        affymetrixCnPlatformSource = new AffymetrixCnPlatformSource (
                annotationFiles, "AffymetrixCnPlatform");

        loader = new AffymetrixCnPlatformLoader(
                affymetrixCnPlatformSource);
        boolean hasException = false;
        try {
            platform = loader.load(dao);
        } catch (PlatformLoadingException e) {
            hasException = true;
            assertEquals("Invalid file format; header 'Probe Set ID' is missing", e.getMessage());
        }
        assertTrue(hasException);
    }
}