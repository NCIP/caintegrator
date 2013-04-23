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
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.GenericMultiSamplePerFileParser;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import affymetrix.calvin.exception.UnsignedOutOfLimitsException;

/**
 * Tests for generic unparsed supplemental data.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenericUnparsedSupplementalFileHandlerTest extends AbstractMockitoTest {
    /**
     * Expected exception.
     */
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private CaArrayFacade caArrayFacade;

    /**
     * Sets up the test.
     * @throws Exception on error
     */
    @Before
    public void setUp() throws Exception {
        caArrayFacade = mock(CaArrayFacade.class);
        when(caArrayFacade.retrieveFile(any(GenomicDataSourceConfiguration.class), anyString()))
            .thenReturn(FileUtils.readFileToByteArray(TestDataFiles.TCGA_LEVEL_2_DATA_FILE));
    }

    /**
     * Tests loading array data without a mapping file.
     * @throws DataRetrievalException on error
     * @throws ConnectionException on error
     * @throws ValidationException on error
     */
    @Test
    public void loadArrayDataWithoutMappingFile()
            throws DataRetrievalException, ConnectionException, ValidationException {
        expectedException.expect(DataRetrievalException.class);
        expectedException.expectMessage("Sample mapping file not found");

        CaIntegrator2DaoStub dao = new LocalCaIntegrator2DaoStub();
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getOrCreateSubjectAssignment("E09176");
        source.setStudyConfiguration(studyConfiguration);
        source.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        source.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        GenericSupplementalMultiSamplePerFileHandler handler = new GenericSupplementalMultiSamplePerFileHandler(
                source, caArrayFacade, arrayDataService, dao);
        handler.loadArrayData();
    }


    /**
     * Tests loading array data with agilent copy number data mapping file.
     * @throws DataRetrievalException on error
     * @throws ConnectionException on error
     * @throws ValidationException on error
     */
    @Test
    public void loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException {
        CaIntegrator2DaoStub dao = new LocalCaIntegrator2DaoStub();
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getOrCreateSubjectAssignment("E09176");
        source.setStudyConfiguration(studyConfiguration);
        source.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        source.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        source.getDnaAnalysisDataConfiguration()
            .setMappingFilePath(TestDataFiles.TCGA_AGILENT_COPY_NUMBER_MAPPING_FILE.getAbsolutePath());
        GenericSupplementalMultiSamplePerFileHandler handler =
                new GenericSupplementalMultiSamplePerFileHandler(source, caArrayFacade, arrayDataService, dao);
        handler.loadArrayData();

        Set<ArrayData> arrayDatas =
                studyConfiguration.getStudy().getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, null);
        assertEquals(1, arrayDatas.size());
        for (ArrayData arrayData : arrayDatas) {
            checkArrayData(arrayData);
        }
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
     * Local DAO stub implementation.
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
                addReporterList(platform, TestDataFiles.TCGA_LEVEL_2_DATA_FILE, "TCGA_244A");
                return platform;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private void addReporterList(Platform platform, File dataFile, String reporterListName)
        throws IOException, UnsignedOutOfLimitsException, DataRetrievalException {
            List<String> sampleNames = new ArrayList<String>();
            sampleNames.add("TCGA-13-0805-01A-01D-0357-04");
            GenericMultiSamplePerFileParser parser =
                    new GenericMultiSamplePerFileParser(dataFile, "ProbeID", "Hybridization Ref", sampleNames);
            Map<String, Map<String, float[]>> dataMap = new HashMap<String, Map<String, float[]>>();
            parser.loadData(dataMap);
            Map<String, float[]> reporterMap = dataMap.values().iterator().next();

            ReporterList reporterList =
                    platform.addReporterList(reporterListName, ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
            for (String probeName : reporterMap.keySet()) {
                DnaAnalysisReporter reporter = new DnaAnalysisReporter();
                reporter.setName(probeName);
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
