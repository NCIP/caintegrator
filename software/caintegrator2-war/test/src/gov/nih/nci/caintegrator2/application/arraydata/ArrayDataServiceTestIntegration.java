/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ArrayDataServiceTestIntegration extends AbstractTransactionalSpringContextTests {
    
    private ArrayDataService arrayDataService;
    private CaIntegrator2Dao dao;
    
    public ArrayDataServiceTestIntegration() {
        setDefaultRollback(true);
    }
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/service-test-integration-config.xml"};
    }
    
    @Test
    public void testLoadGeneLocationFile() throws ValidationException, IOException {
        GeneLocationConfiguration geneLocationConf = 
            arrayDataService.loadGeneLocationFile(TestDataFiles.HG18_GENE_LOCATIONS_FILE, GenomeBuildVersionEnum.HG18);
        assertEquals(22404, geneLocationConf.getGeneLocations().size());
        
        geneLocationConf = 
            arrayDataService.loadGeneLocationFile(TestDataFiles.HG19_GENE_LOCATIONS_FILE, GenomeBuildVersionEnum.HG19);
        assertEquals(22397, geneLocationConf.getGeneLocations().size());
    }

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadArrayDesign(TestArrayDesignFiles.HG_U133_PLUS_2_CDF_FILE, TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
    }

    private void checkLoadArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException, AffymetrixCdfReadException {
        ArrayDesignChecker.checkLoadAffymetrixExpressionArrayDesign(cdfFile, annotationFile, arrayDataService);
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

}
