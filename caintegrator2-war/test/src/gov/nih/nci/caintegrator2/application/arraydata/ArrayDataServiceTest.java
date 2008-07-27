package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ArrayDataServiceTest {
    
    private CaIntegrator2DaoStub daoStub;
    private ArrayDataService service;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("service-test-config.xml", ArrayDataServiceTest.class); 
        service = (ArrayDataService) context.getBean("arrayDataService"); 
        daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        daoStub.clear();                
    }

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadArrayDesign(TestArrayDesignFiles.YEAST_2_CDF_FILE, TestArrayDesignFiles.YEAST_2_ANNOTATION_FILE);
    }

    private void checkLoadArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException, AffymetrixCdfReadException {
        ArrayDesignChecker.checkLoadArrayDesign(cdfFile, annotationFile, service);
        assertTrue(daoStub.saveCalled);
    }

}
