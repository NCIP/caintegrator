/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Affymetrix design loading integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class LoadAffymetrixDesignsTestIntegration {
    @Autowired
    private ArrayDataService arrayDataService;
    @Autowired
    private CaIntegrator2Dao dao;

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U133_PLUS_2_CDF_FILE,
                TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U133A_CDF_FILE,
                TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U133B_CDF_FILE,
                TestArrayDesignFiles.HG_U133B_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95AV2_CDF_FILE,
                TestArrayDesignFiles.HG_U95AV2_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95B_CDF_FILE,
                TestArrayDesignFiles.HG_U95B_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95C_CDF_FILE,
                TestArrayDesignFiles.HG_U95C_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95D_CDF_FILE,
                TestArrayDesignFiles.HG_U95D_ANNOTATION_FILE);
        List<File> files = new ArrayList<File>();
        files.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        files.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        AffymetrixSnpPlatformSource source = new AffymetrixSnpPlatformSource(files, "GeneChip Human Mapping 100K Set");
        File[] cdfs = new File[] {TestArrayDesignFiles.MAPPING_50K_HIND_CDF_FILE,
                TestArrayDesignFiles.MAPPING_50K_XBA_CDF};
        ArrayDesignChecker.checkLoadAffymetrixSnpArrayDesign(cdfs, source, arrayDataService);
    }

    private void checkLoadAffymetrixArrayDesign(File cdfFile, File annotationFile)
            throws PlatformLoadingException, AffymetrixCdfReadException {
        ArrayDesignChecker.checkLoadAffymetrixExpressionArrayDesign(cdfFile, annotationFile, arrayDataService);
    }
}
