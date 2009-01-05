package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataMatrix;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.file.FileManagerStub;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ArrayDataServiceTest {

    private CaIntegrator2DaoStub daoStub;
    private ArrayDataService service;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("arraydata-test-config.xml", ArrayDataServiceTest.class); 
        service = (ArrayDataService) context.getBean("arrayDataService"); 
        daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        daoStub.clear();                
    }

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadArrayDesign(TestArrayDesignFiles.YEAST_2_CDF_FILE, TestArrayDesignFiles.YEAST_2_ANNOTATION_FILE);
    }

    private void checkLoadArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException, AffymetrixCdfReadException {
        Platform platform = ArrayDesignChecker.checkLoadArrayDesign(cdfFile, annotationFile, service);
        assertTrue(daoStub.saveCalled);
        PlatformHelper platformHelper = new PlatformHelper(platform);
        Collection<AbstractReporter> geneReporters = platformHelper.getReporterSet(ReporterTypeEnum.GENE_EXPRESSION_GENE).getReporters();
        assertEquals(4562, geneReporters.size());
        for (AbstractReporter abstractReporter : geneReporters) {
            GeneExpressionReporter geneReporter = (GeneExpressionReporter) abstractReporter;
            Collection<AbstractReporter> probeSets = platformHelper.getReportersForGene(geneReporter.getGene(), ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            for (AbstractReporter probeSetReporter : probeSets) {
                GeneExpressionReporter probeSet = (GeneExpressionReporter) probeSetReporter;
                assertTrue(geneReporter.getGene() == probeSet.getGene());
            }
        }
    }
    
    @Test
    public void testGetFoldChangeValues() {
        ((ArrayDataServiceImpl) service).setFileManager(new FoldChangeTestFileManagerStub());
        ArrayDataValues values = new ArrayDataValues();
        ArrayDataMatrix arrayDataMatrix = new ArrayDataMatrix();
        arrayDataMatrix.setStudy(new Study());
        arrayDataMatrix.getStudy().setId(1L);
        arrayDataMatrix.setId(1L);
        values.setArrayDataMatrix(arrayDataMatrix);
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        reporter1.setId(1L);
        reporter1.setName("reporter1");
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporter2.setId(2L);
        reporter2.setName("reporter2");
        ArrayData data1 = new ArrayData();
        data1.setId(1L);
        ArrayData data2 = new ArrayData();
        data2.setId(2L);
        ArrayData controlData1 = new ArrayData();
        controlData1.setId(3L);
        ArrayData controlData2 = new ArrayData();
        controlData2.setId(4L);
        ArrayData controlData3 = new ArrayData();
        controlData3.setId(5L);
        
        data1.setArray(new Array());
        data1.getArray().setName("data1");
        data2.setArray(new Array());
        data2.getArray().setName("data2");
        controlData1.setArray(new Array());
        controlData1.getArray().setName("controlData1");
        controlData2.setArray(new Array());
        controlData2.getArray().setName("controlData2");
        controlData3.setArray(new Array());
        controlData3.getArray().setName("controlData3");
        
        List<ArrayData> arrayDatas = new ArrayList<ArrayData>();
        List<ArrayData> controlArrayDatas = new ArrayList<ArrayData>();
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        arrayDatas.add(data1);
        arrayDatas.add(data2);
        controlArrayDatas.add(controlData1);
        controlArrayDatas.add(controlData2);
        controlArrayDatas.add(controlData3);
        reporters.add(reporter1);
        reporters.add(reporter2);

        values.setValue(data1, reporter1, (float) 2.2);
        values.setValue(data1, reporter2, (float) 4.4);
        values.setValue(data2, reporter1, (float) 6.6);
        values.setValue(data2, reporter2, (float) 8.8);
        
        values.setValue(controlData1, reporter1, (float) 0.1);
        values.setValue(controlData2, reporter1, (float) 2.0);
        values.setValue(controlData3, reporter1, (float) 9.9);
        
        values.setValue(controlData1, reporter2, (float) 0.1);
        values.setValue(controlData2, reporter2, (float) 2.0);
        values.setValue(controlData3, reporter2, (float) 1.1);

        service.save(values);
        ArrayDataValues foldChangeValues = service.getFoldChangeValues(arrayDataMatrix, arrayDatas, reporters, controlArrayDatas);
        assertEquals(2, foldChangeValues.getAllArrayDatas().size());
        assertEquals(2, foldChangeValues.getAllReporters().size());
        assertEquals(1.1, (float) foldChangeValues.getValue(data1, reporter1), 0.0001);
        assertEquals(3.3, (float) foldChangeValues.getValue(data2, reporter1), 0.0001);
        assertEquals(4.0, (float) foldChangeValues.getValue(data1, reporter2), 0.0001);
        assertEquals(8.0, (float) foldChangeValues.getValue(data2, reporter2), 0.0001);
    }
    
    @Test
    public void testGetPlatforms() {
        service.getPlatforms();
        assertTrue(daoStub.getPlatformsCalled);
    }

    private static class FoldChangeTestFileManagerStub extends FileManagerStub implements FileManager {

        /**
         * {@inheritDoc}
         */
        @Override
        public File getStudyDirectory(Study study) {
            return new File(System.getProperty("java.io.tmpdir"));
        }
        
    }

}
