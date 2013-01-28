/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import gov.nih.nci.caintegrator.application.study.deployment.GenericSupplementalMultiSamplePerFileHandler;
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
import org.junit.Test;

import affymetrix.calvin.exception.UnsignedOutOfLimitsException;

public class GenericUnparsedSupplementalFileHandlerTest extends AbstractMockitoTest {
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

    @Test
    public void testLoadCopyNumberData() throws ConnectionException, ValidationException {
        CaIntegrator2DaoStub dao = new LocalCaIntegrator2DaoStub();
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getOrCreateSubjectAssignment("E09176");
        source.setStudyConfiguration(studyConfiguration);
        source.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        source.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        GenericSupplementalMultiSamplePerFileHandler handler = new GenericSupplementalMultiSamplePerFileHandler(
                source, caArrayFacade, arrayDataService, dao);
        boolean exceptionCaught = false;
        try {
            handler.loadArrayData();
        } catch (DataRetrievalException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        exceptionCaught = false;
        source.getDnaAnalysisDataConfiguration().setMappingFilePath(TestDataFiles.TCGA_AGILENT_COPY_NUMBER_MAPPING_FILE.getAbsolutePath());
        try {
            handler.loadArrayData();
        } catch (DataRetrievalException e) {
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
        Set<ArrayData> arrayDatas = studyConfiguration.getStudy().getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, null);
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

    static class LocalCaIntegrator2DaoStub extends CaIntegrator2DaoStub {

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
            GenericMultiSamplePerFileParser parser = new GenericMultiSamplePerFileParser(
                    dataFile, "ProbeID", "Hybridization Ref", sampleNames);
            Map<String, Map<String, List<Float>>> dataMap = new HashMap<String, Map<String, List<Float>>>();
            parser.loadData(dataMap);
            Map<String, List<Float>> reporterMap = dataMap.values().iterator().next();

            ReporterList reporterList = platform.addReporterList(reporterListName, ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
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
