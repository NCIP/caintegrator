/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Array data service integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ArrayDataServiceTestIntegration {

    private static final int NUMBER_OF_HG18_GENE_LOCATIONS = 22404;
    private static final int NUMBER_OF_HG19_GENE_LOCATIONS = 22397;
    @Autowired
    private ArrayDataService arrayDataService;
    @Autowired
    private CaIntegrator2Dao dao;

    /**
     * Tests loading of gene location files.
     * @throws ValidationException on unexpected error
     * @throws IOException on unexpected error
     */
    @Test
    public void testLoadGeneLocationFile() throws ValidationException, IOException {
        GeneLocationConfiguration geneLocationConf =
            arrayDataService.loadGeneLocationFile(TestDataFiles.HG18_GENE_LOCATIONS_FILE, GenomeBuildVersionEnum.HG18);
        assertEquals(NUMBER_OF_HG18_GENE_LOCATIONS, geneLocationConf.getGeneLocations().size());

        geneLocationConf =
            arrayDataService.loadGeneLocationFile(TestDataFiles.HG19_GENE_LOCATIONS_FILE, GenomeBuildVersionEnum.HG19);
        assertEquals(NUMBER_OF_HG19_GENE_LOCATIONS, geneLocationConf.getGeneLocations().size());
    }

    /**
     * Tests loading of an array design.
     * @throws PlatformLoadingException on unexpected error
     * @throws AffymetrixCdfReadException on unexpected error
     */
    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadArrayDesign(TestArrayDesignFiles.HG_U133_PLUS_2_CDF_FILE,
                TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
    }

    private void checkLoadArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException,
        AffymetrixCdfReadException {
        ArrayDesignChecker.checkLoadAffymetrixExpressionArrayDesign(cdfFile, annotationFile, arrayDataService);
    }
}
