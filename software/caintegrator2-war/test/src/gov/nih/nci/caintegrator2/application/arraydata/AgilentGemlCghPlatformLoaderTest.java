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

import java.util.SortedSet;

import org.junit.Test;


/**
 * 
 */
public class AgilentGemlCghPlatformLoaderTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    
    @Test
    public void testLoad() throws PlatformLoadingException {
        
        AgilentCnPlatformSource agilentGemlCghPlatformSource = new AgilentCnPlatformSource (
                TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_FILE,
                "Agilent_G4502A",
                TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_XML_ANNOTATION_TEST_PATH);

        AgilentGemlCghPlatformLoader xmlLoader = new AgilentGemlCghPlatformLoader(
                agilentGemlCghPlatformSource);
        
        Platform platform = xmlLoader.load(dao);
        assertTrue("Agilent-017804".equals(xmlLoader.getPlatformName()));
        assertTrue("Agilent-017804".equals(platform.getName()));
        SortedSet<ReporterList>reporterLists = platform.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertEquals(1, reporterLists.size());
        ReporterList reporterList = reporterLists.iterator().next();
        assertEquals(4, reporterList.getReporters().size());
    }

}
