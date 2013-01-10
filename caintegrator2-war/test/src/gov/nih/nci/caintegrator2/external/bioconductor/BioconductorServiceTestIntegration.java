/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.bioconductor;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixCnPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.PublicAffymetrixDnaAnalysisChpParser;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;

public class BioconductorServiceTestIntegration extends AbstractTransactionalSpringContextTests {

    private BioconductorServiceImpl service = new BioconductorServiceImpl();
    private ArrayDataService arrayDataService;
    private CaIntegrator2Dao dao;

    public BioconductorServiceTestIntegration() {
        setDefaultRollback(true);
    }
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/service-test-integration-config.xml"};
    }
    
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
            getArrayDataService().savePlatformConfiguration(configuration);
            platform = getArrayDataService().loadArrayDesign(configuration).getPlatform();
        }
        return platform;
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
