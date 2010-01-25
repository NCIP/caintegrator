package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class LoadAffymetrixDesignsTestIntegration extends AbstractTransactionalSpringContextTests {
    
    private ArrayDataService arrayDataService;
    private CaIntegrator2Dao dao;
    
    public LoadAffymetrixDesignsTestIntegration() {
        setDefaultRollback(false);
    }
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/service-test-integration-config.xml"};
    }

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U133_PLUS_2_CDF_FILE, TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U133A_CDF_FILE, TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U133B_CDF_FILE, TestArrayDesignFiles.HG_U133B_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95AV2_CDF_FILE, TestArrayDesignFiles.HG_U95AV2_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95B_CDF_FILE, TestArrayDesignFiles.HG_U95B_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95C_CDF_FILE, TestArrayDesignFiles.HG_U95C_ANNOTATION_FILE);
        checkLoadAffymetrixArrayDesign(TestArrayDesignFiles.HG_U95D_CDF_FILE, TestArrayDesignFiles.HG_U95D_ANNOTATION_FILE);
        List<File> files = new ArrayList<File>();
        files.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        files.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        AffymetrixSnpPlatformSource source = new AffymetrixSnpPlatformSource(files, "GeneChip Human Mapping 100K Set");
        File[] cdfs = new File[] {TestArrayDesignFiles.MAPPING_50K_HIND_CDF_FILE, TestArrayDesignFiles.MAPPING_50K_XBA_CDF};
        ArrayDesignChecker.checkLoadAffymetrixSnpArrayDesign(cdfs, source, arrayDataService);
    }

    private void checkLoadAffymetrixArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException, AffymetrixCdfReadException {
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
