/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import affymetrix.calvin.data.ProbeSetMultiDataCopyNumberData;
import affymetrix.calvin.exception.UnsignedOutOfLimitsException;
import affymetrix.fusion.chp.FusionCHPDataReg;
import affymetrix.fusion.chp.FusionCHPMultiDataData;

/**
 * Tests for Affymetrix Copy Number Mapping file handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class AffymetrixCopyNumberMappingFileHandlerTest extends AbstractMockitoTest {

    /**
     * Expected Exception.
     */
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CaArrayFacade caArrayFacade;

    /**
     * Sets up the test.
     * @throws Exception on error
     */
    @Before
    public void setUp() throws Exception {
        caArrayFacade = mock(CaArrayFacade.class);
        when(caArrayFacade.retrieveFile(any(GenomicDataSourceConfiguration.class), anyString()))
            .thenReturn(FileUtils.readFileToByteArray(TestDataFiles.HIND_COPY_NUMBER_CHP_FILE));
    }

    /**
     * Tests loading copy number data.
     * @throws DataRetrievalException on error
     * @throws ConnectionException on error
     * @throws ValidationException on error
     * @throws FileNotFoundException on error
     */
    @Test
    public void testLoadCopyNumberData() throws DataRetrievalException, ConnectionException, ValidationException,
    FileNotFoundException {
        CaIntegrator2DaoStub dao = new LocalCaIntegrator2DaoStub();
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getOrCreateSubjectAssignment("E09176");

        Sample sample = new Sample();
        sample.setName("E09176");

        source.getSamples().add(sample);
        source.setStudyConfiguration(studyConfiguration);
        source.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        source.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        source.getDnaAnalysisDataConfiguration()
            .setMappingFilePath(TestDataFiles.SHORT_COPY_NUMBER_FILE.getAbsolutePath());
        AbstractAffymetrixDnaAnalysisMappingFileHandler handler =
                new AffymetrixCopyNumberMappingFileHandler(source, caArrayFacade, arrayDataService, dao);
        handler.loadArrayData();

        Set<ArrayData> arrayDatas =
                studyConfiguration.getStudy().getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, null);
        assertEquals(1, arrayDatas.size());
        for (ArrayData arrayData : arrayDatas) {
            checkArrayData(arrayData);
        }
    }

    /**
     * Test loading copy number data without a mapping file.
     * @throws DataRetrievalException on error
     * @throws ConnectionException on error
     * @throws ValidationException on error
     * @throws FileNotFoundException on expected error
     */
    @Test
    public void testLoadCopyNumberDataNoFile() throws DataRetrievalException, ConnectionException,
        ValidationException, FileNotFoundException {
        expectedException.expect(DataRetrievalException.class);
        expectedException.expectMessage("DNA analysis mapping file not found");

        CaIntegrator2DaoStub dao = new LocalCaIntegrator2DaoStub();
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getOrCreateSubjectAssignment("E09176");
        source.setStudyConfiguration(studyConfiguration);
        source.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        source.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        // test if file not found
        source.getDnaAnalysisDataConfiguration()
            .setMappingFilePath(TestDataFiles.INVALID_FILE_DOESNT_EXIST.getAbsolutePath());
        AbstractAffymetrixDnaAnalysisMappingFileHandler handler =
                new AffymetrixCopyNumberMappingFileHandler(source, caArrayFacade, arrayDataService, dao);
        handler.loadArrayData();
    }

    private void checkArrayData(ArrayData arrayData) {
        assertNotNull(arrayData.getArray());
        assertNotNull(arrayData.getArray().getPlatform());
        assertTrue(arrayData.getArray().getArrayDataCollection().contains(arrayData));
        assertNotNull(arrayData.getSample());
        assertTrue(arrayData.getSample().getArrayDataCollection().contains(arrayData));
        assertTrue(arrayData.getSample().getArrayCollection().contains(arrayData.getArray()));
    }

    /**
     * Local implementation for dao stub.
     *
     * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
     */
    private static class LocalCaIntegrator2DaoStub extends CaIntegrator2DaoStub {

        private Platform platform;

        @Override
        public Platform getPlatform(String name) {
            return getPlatform();
        }

        private Platform getPlatform() {
            if (platform != null) {
                return platform;
            }
            try {
                Platform platform = new Platform();
                FusionCHPMultiDataData.registerReader();
                addReporterList(platform, TestDataFiles.HIND_COPY_NUMBER_CHP_FILE, "Mapping50K_Hind240");
                addReporterList(platform, TestDataFiles.XBA_COPY_NUMBER_CHP_FILE, "Mapping50K_Xba240");
                return platform;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private void addReporterList(Platform platform, File chpFile, String reporterListName)
                throws IOException, UnsignedOutOfLimitsException {
            FusionCHPMultiDataData chpData =
                    FusionCHPMultiDataData.fromBase(FusionCHPDataReg.read(chpFile.getAbsolutePath()));
            ReporterList reporterList =
                    platform.addReporterList(reporterListName, ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
            int numProbeSets = chpData.getEntryCount(MultiDataType.CopyNumberMultiDataType);
            for (int i = 0; i < numProbeSets; i++) {
                ProbeSetMultiDataCopyNumberData probeSetData =
                    chpData.getCopyNumberEntry(MultiDataType.CopyNumberMultiDataType, i);
                DnaAnalysisReporter reporter = new DnaAnalysisReporter();
                reporter.setName(probeSetData.getName());
                reporterList.getReporters().add(reporter);
            }
        }

        @Override
        public ReporterList getReporterList(String name) {
            for (ReporterList reporterList : getPlatform().getReporterLists()) {
                if (name.equals(reporterList.getName())) {
                    return reporterList;
                }
            }
            return null;
        }

    }
}
