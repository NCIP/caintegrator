/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
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

public class AffymetrixExpressionPlatformLoaderTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    
    @Test
    public void testLoad() throws PlatformLoadingException {
        AffymetrixExpressionPlatformSource affymetrixExpressionPlatformSource = new AffymetrixExpressionPlatformSource (
                TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_TEST_FILE);

        AffymetrixExpressionPlatformLoader loader = new AffymetrixExpressionPlatformLoader(
                affymetrixExpressionPlatformSource);
        
        Platform platform = loader.load(dao);
        assertTrue("HG-U133_Plus_2".equals(loader.getPlatformName()));
        assertTrue("HG-U133_Plus_2".equals(platform.getName()));
        SortedSet<ReporterList> reporterLists = platform.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        assertEquals(1, reporterLists.size());
        ReporterList reporterList = reporterLists.iterator().next();
        assertTrue("NCBI Build 36.1".equals(reporterList.getGenomeVersion()));
        assertEquals(10, reporterList.getReporters().size());
        
        // Test wrong header file
        affymetrixExpressionPlatformSource = new AffymetrixExpressionPlatformSource (
                TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_TEST_BAD_FILE);

        loader = new AffymetrixExpressionPlatformLoader(
                affymetrixExpressionPlatformSource);
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
