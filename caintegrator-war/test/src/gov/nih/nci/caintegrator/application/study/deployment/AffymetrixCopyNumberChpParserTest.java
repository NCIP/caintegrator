/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.AffymetrixDnaAnalysisChpParser;
import gov.nih.nci.caintegrator.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import affymetrix.calvin.data.ProbeSetMultiDataCopyNumberData;
import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import affymetrix.calvin.exception.UnsignedOutOfLimitsException;
import affymetrix.fusion.chp.FusionCHPDataReg;
import affymetrix.fusion.chp.FusionCHPMultiDataData;

public class AffymetrixCopyNumberChpParserTest {
    
    private GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
    private CentralTendencyCalculator centralTendencyCalculator = new CentralTendencyCalculator(
            genomicSource.getTechnicalReplicatesCentralTendency(), 
            genomicSource.isUseHighVarianceCalculation(), 
            genomicSource.getHighVarianceThreshold(), 
            genomicSource.getHighVarianceCalculationType());
    
    @Test
    public void testGetArrayDesignName() throws DataRetrievalException {
        AffymetrixDnaAnalysisChpParser parser = new AffymetrixDnaAnalysisChpParser(TestDataFiles.HIND_COPY_NUMBER_CHP_FILE,
                centralTendencyCalculator);
        assertEquals("Mapping50K_Hind240", parser.getArrayDesignName());
    }

    @Test
    public void testParse() throws DataRetrievalException, UnsignedOutOfLimitsException, IOException {
        List<AbstractReporter> reporters = getReporters(TestDataFiles.HIND_COPY_NUMBER_CHP_FILE);
        ArrayDataValues values = new ArrayDataValues(reporters);
        ArrayData arrayData = new ArrayData();
        AffymetrixDnaAnalysisChpParser parser = new AffymetrixDnaAnalysisChpParser(TestDataFiles.HIND_COPY_NUMBER_CHP_FILE,
                centralTendencyCalculator);
        parser.parse(values, arrayData, MultiDataType.CopyNumberMultiDataType);
        checkValues(reporters, values, arrayData);
        reporters = getReporters(TestDataFiles.XBA_COPY_NUMBER_CHP_FILE);
        values = new ArrayDataValues(reporters);
        parser = new AffymetrixDnaAnalysisChpParser(TestDataFiles.XBA_COPY_NUMBER_CHP_FILE, centralTendencyCalculator);
        parser.parse(values, arrayData, MultiDataType.CopyNumberMultiDataType);
        checkValues(reporters, values, arrayData);
    }

    private void checkValues(List<AbstractReporter> reporters, ArrayDataValues values, ArrayData arrayData) {
        assertEquals(1, values.getTypes().size());
        assertTrue(values.getTypes().contains(ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO));
        assertEquals(1, values.getArrayDatas().size());
        assertTrue(values.getArrayDatas().contains(arrayData));
        for (AbstractReporter reporter : reporters) {
            assertTrue(reporter.getName().startsWith("SNP_"));
            assertTrue(values.getFloatValue(arrayData, reporter, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO) != 0.0f);
        }
    }

    private List<AbstractReporter> getReporters(File chpFile) throws UnsignedOutOfLimitsException, IOException  {
        FusionCHPMultiDataData.registerReader();
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        FusionCHPMultiDataData chpData = FusionCHPMultiDataData.fromBase(FusionCHPDataReg.read(chpFile.getAbsolutePath()));
        int numProbeSets = chpData.getEntryCount(MultiDataType.CopyNumberMultiDataType);
        for (int i = 0; i < numProbeSets; i++) {
            ProbeSetMultiDataCopyNumberData probeSetData = 
                chpData.getCopyNumberEntry(MultiDataType.CopyNumberMultiDataType, i);
            DnaAnalysisReporter reporter = new DnaAnalysisReporter();
            reporter.setName(probeSetData.getName());
            reporters.add(reporter);
        }
        return reporters;
    }

}
