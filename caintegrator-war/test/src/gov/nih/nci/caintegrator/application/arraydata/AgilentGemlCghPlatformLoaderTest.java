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
import gov.nih.nci.caintegrator.application.arraydata.AgilentCnPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.AgilentGemlCghPlatformLoader;
import gov.nih.nci.caintegrator.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

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
