/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixCdfReadException;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataServiceImpl;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrayDataServiceTest extends AbstractMockitoTest {

    private CaIntegrator2DaoStub daoStub;
    private ArrayDataService service;

    @Before
    public void setUp() throws Exception {
        daoStub = new CaIntegrator2DaoStub();

        ArrayDataServiceImpl arrayDataService = new ArrayDataServiceImpl();
        arrayDataService.setDao(daoStub);
        service = arrayDataService;
    }

    @After
    public void tearDown() {
        File testFile = new File(fileManager.getStudyDirectory(null), "data1.nc");
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadAffymetrixExpressionArrayDesign(TestArrayDesignFiles.YEAST_2_CDF_FILE, TestArrayDesignFiles.YEAST_2_ANNOTATION_FILE);
        checkLoadAgilentExpressionArrayDesign(TestArrayDesignFiles.HUMAN_GENOME_CGH244A_ANNOTATION_FILE);
        checkLoadAgilentExpressionArrayDesign(TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_FILE);
        checkLoadAgilentCopyNumberArrayDesign(TestArrayDesignFiles.AGILENT_014693_XML_ANNOTATION_FILE);
        List<File> files = new ArrayList<File>();
        files.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        files.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        AffymetrixSnpPlatformSource source = new AffymetrixSnpPlatformSource(files, "GeneChip Human Mapping 100K Set");
        File[] cdfs = new File[] {TestArrayDesignFiles.MAPPING_50K_HIND_CDF_FILE, TestArrayDesignFiles.MAPPING_50K_XBA_CDF};
        checkLoadAffymetrixSnpArrayDesign(cdfs, source);

    }

    private void checkLoadAffymetrixExpressionArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException, AffymetrixCdfReadException {
        Platform platform = ArrayDesignChecker.checkLoadAffymetrixExpressionArrayDesign(cdfFile, annotationFile, service);
        checkGenesForReporters(platform);
        assertTrue(daoStub.saveCalled);
    }

    private void checkLoadAffymetrixSnpArrayDesign(File[] cdfs, AffymetrixSnpPlatformSource source) throws PlatformLoadingException, AffymetrixCdfReadException {
        ArrayDesignChecker.checkLoadAffymetrixSnpArrayDesign(cdfs, source, service);
        assertTrue(daoStub.saveCalled);
    }

    private void checkLoadAgilentExpressionArrayDesign(File annotationFile) throws PlatformLoadingException {
        Platform platform = ArrayDesignChecker.checkLoadAgilentExpressionArrayDesign(
                "Agilent Expression Platform", annotationFile, service);
        checkGenesForReporters(platform);
        assertTrue(daoStub.saveCalled);
    }

    private void checkLoadAgilentCopyNumberArrayDesign(File annotationFile) throws PlatformLoadingException {
        Platform platform = ArrayDesignChecker.checkLoadAgilentCopyNumberArrayDesign(
                "Agilent CN Platform", annotationFile, service);
        assertFalse(platform.getReporterLists().isEmpty());
        assertTrue(platform.getVendor().equals(PlatformVendorEnum.AGILENT));
        assertTrue(daoStub.saveCalled);
    }

    private void checkGenesForReporters(Platform platform) {
        PlatformHelper platformHelper = new PlatformHelper(platform);
        Collection<AbstractReporter> geneReporters = platformHelper.getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        for (AbstractReporter reporter : geneReporters) {
            Gene gene = reporter.getGenes().iterator().next();
            Collection<AbstractReporter> probeSets = platformHelper.getReportersForGene(gene, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            for (AbstractReporter probeSetReporter : probeSets) {
                GeneExpressionReporter probeSet = (GeneExpressionReporter) probeSetReporter;
                assertTrue(probeSet.getGenes().contains(gene));
            }
        }
    }

    @Test
    public void testGetFoldChangeValues() throws IOException {
        Platform platform = new Platform();
        platform.setId(1l);
        PlatformConfiguration platformConfiguration = new PlatformConfiguration();
        platform.setPlatformConfiguration(platformConfiguration);
        platformConfiguration.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        platformConfiguration.setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        ((ArrayDataServiceImpl) service).setFileManager(fileManager);
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList.setId(1L);
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        reporter1.setId(1L);
        reporter1.setName("reporter1");
        reporter1.setReporterList(reporterList);
        reporter1.setIndex(0);
        reporterList.getReporters().add(reporter1);
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporter2.setId(2L);
        reporter2.setName("reporter2");
        reporter2.setReporterList(reporterList);
        reporter2.setIndex(1);
        reporterList.getReporters().add(reporter2);
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        reporters.add(reporter1);
        reporters.add(reporter2);
        ArrayDataValues values = new ArrayDataValues(reporters);
        Study study = new Study();
        study.setId(1L);
        ArrayData data1 = createArrayData(1L, study, reporterList, platform);
        ArrayData data2 = createArrayData(2L, study, reporterList, platform);
        ArrayData controlData1 = createArrayData(3L, study, reporterList, platform);
        ArrayData controlData2 = createArrayData(4L, study, reporterList, platform);
        ArrayData controlData3 = createArrayData(5L, study, reporterList, platform);

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
        arrayDatas.add(data1);
        arrayDatas.add(data2);
        controlArrayDatas.add(controlData1);
        controlArrayDatas.add(controlData2);
        controlArrayDatas.add(controlData3);
        ArrayDataValues controlValues = new ArrayDataValues(reporters);
        controlValues.setFloatValue(controlData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 0.1);
        controlValues.setFloatValue(controlData2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 2.0);
        controlValues.setFloatValue(controlData3, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 9.9);
        controlValues.setFloatValue(controlData1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 0.1);
        controlValues.setFloatValue(controlData2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 2.0);
        controlValues.setFloatValue(controlData3, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 1.1);
        List<ArrayDataValues> controlValuesList = new ArrayList<ArrayDataValues>();
        controlValuesList.add(controlValues);

        values.setFloatValue(data1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 2.2);
        values.setFloatValue(data1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 4.4);
        values.setFloatValue(data2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 6.6);
        values.setFloatValue(data2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 8.8);

        values.setFloatValue(controlData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 0.1);
        values.setFloatValue(controlData2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 2.0);
        values.setFloatValue(controlData3, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 9.9);

        values.setFloatValue(controlData1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 0.1);
        values.setFloatValue(controlData2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 2.0);
        values.setFloatValue(controlData3, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, (float) 1.1);

        service.save(values);
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addArrayDatas(arrayDatas);
        request.addReporters(reporters);
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        ArrayDataValues foldChangeValues = service.getFoldChangeValues(request, controlValuesList, null);
        assertEquals(2, foldChangeValues.getArrayDatas().size());
        assertEquals(2, foldChangeValues.getReporters().size());
        assertEquals(-1.8182, foldChangeValues.getFloatValue(data1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0001);
        assertEquals(1.65, foldChangeValues.getFloatValue(data2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0001);
        assertEquals(4.125, foldChangeValues.getFloatValue(data1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0001);
        assertEquals(8.25, foldChangeValues.getFloatValue(data2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0001);

        // Test getFoldChangeValues by query
        Query query = new Query();
        query.setGeneExpressionPlatform(platform);
        query.setSubscription(new StudySubscription());
        query.getSubscription().setStudy(study);
        FoldChangeCriterion foldChangeCriterion = new FoldChangeCriterion();
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        query.getCompoundCriterion().getCriterionCollection().add(foldChangeCriterion);
        query.getCompoundCriterion().getCriterionCollection().add(compoundCriterion);
        foldChangeValues = service.getFoldChangeValues(request, query);
        assertEquals(2, foldChangeValues.getArrayDatas().size());
        assertEquals(2, foldChangeValues.getReporters().size());

    }

    private ArrayData createArrayData(Long id, Study study, ReporterList reporterList, Platform platform) {
        ArrayData arrayData = new ArrayData();
        Array array = new Array();
        array.setPlatform(platform);
        arrayData.setArray(array);
        arrayData.setStudy(study);
        arrayData.setId(id);
        arrayData.setSample(new Sample());
        arrayData.setArray(new Array());
        arrayData.getReporterLists().add(reporterList);

        SampleAcquisition sa = new SampleAcquisition();
        sa.setAssignment(new StudySubjectAssignment());
        sa.getAssignment().setStudy(study);

        arrayData.getSample().getSampleAcquisitions().add(sa);
        return arrayData;
    }

    @Test
    public void testGetPlatforms() {
        service.getPlatforms();
        assertTrue(daoStub.getPlatformsCalled);
    }

}
