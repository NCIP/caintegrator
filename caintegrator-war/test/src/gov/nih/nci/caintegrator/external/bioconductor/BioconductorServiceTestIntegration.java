/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.bioconductor;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixCnPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.PublicAffymetrixDnaAnalysisChpParser;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;

/**
 * Bioconductor service integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class BioconductorServiceTestIntegration {
    @Autowired
    private BioconductorServiceImpl service;
    @Autowired
    private ArrayDataService arrayDataService;
    @Autowired
    private CaIntegrator2Dao dao;

    @Test
    public void testAddSegmentationData() throws ConnectionException, PlatformLoadingException, DataRetrievalException {
        Platform platform = getOrCreatePlatform();
        List<DnaAnalysisReporter> reporters = getReporters(platform);
        DnaAnalysisData dnaAnalysisData = new DnaAnalysisData(reporters);
        ArrayData arrayData = new ArrayData();
        // create study for access to study name and deployment date
        Study study = new Study();
        study.setShortTitleText("Integration Test-StudyName");
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setDeploymentStartDate(new Date());
        study.setStudyConfiguration(studyConfiguration);
        // create sample for access to sample name
        Sample sample = new Sample();
        sample.setName("E10003");
        arrayData.setId(0L);
        arrayData.setSample(sample);
        arrayData.setStudy(study);
        dnaAnalysisData.addDnaAnalysisData(arrayData, getValues(TestDataFiles.HIND_COPY_NUMBER_CHP_FILE, reporters));
        DnaAnalysisDataConfiguration configuration = new DnaAnalysisDataConfiguration();
        ServerConnectionProfile server = new ServerConnectionProfile();
        configuration.setChangePointSignificanceLevel(0.0);
        configuration.setEarlyStoppingCriterion(0.0);
        configuration.setPermutationReplicates(0);
        configuration.setRandomNumberSeed(25);
        configuration.setNumberLevelCall(4);
        // test caDNAcopy segmentation service
        server.setUrl("http://bioconductor.nci.nih.gov:8080/wsrf/services/cagrid/CaDNAcopy");
        configuration.setSegmentationService(server);
        configuration.setUseCghCall(false);
        service.addSegmentationData(dnaAnalysisData, configuration);
        assertEquals(23, arrayData.getSegmentDatas().size());
        // test caCGHcall segmentation service
        server.setUrl("http://ncias-s412.nci.nih.gov:8080/wsrf/services/cagrid/CaCGHcall");
        configuration.setSegmentationService(server);
        configuration.setUseCghCall(true);
        service.addSegmentationData(dnaAnalysisData, configuration);
        assertEquals(23, arrayData.getSegmentDatas().size());
    }

    @SuppressWarnings({ "unchecked" })
    private float[] getValues(File chpFile, List<DnaAnalysisReporter> reporters) throws DataRetrievalException {
        PublicAffymetrixDnaAnalysisChpParser parser = new PublicAffymetrixDnaAnalysisChpParser(chpFile);
        ArrayData arrayData = new ArrayData();
        List untypedReporters = reporters;
        ArrayDataValues values = new ArrayDataValues(untypedReporters);
        parser.parse(values, arrayData, MultiDataType.CopyNumberMultiDataType);
        return values.getFloatValues(arrayData, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO);
    }

    @SuppressWarnings({ "unchecked" })
    private List<DnaAnalysisReporter> getReporters(Platform platform) {
        PlatformHelper helper = new PlatformHelper(platform);
        List list = helper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        return list;
    }

    private Platform getOrCreatePlatform() throws PlatformLoadingException {
        Platform platform = dao.getPlatform("Mapping50K_Hind240");
        if (platform == null) {
            List<File> files = new ArrayList<File>();
            files.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
            AffymetrixCnPlatformSource source = new AffymetrixCnPlatformSource(files, "Mapping50K_Hind240");
            PlatformConfiguration configuration = new PlatformConfiguration(source);
            arrayDataService.savePlatformConfiguration(configuration);
            platform = arrayDataService.loadArrayDesign(configuration).getPlatform();
        }
        return platform;
    }
}