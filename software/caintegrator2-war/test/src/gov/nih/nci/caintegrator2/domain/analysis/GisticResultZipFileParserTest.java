/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.analysis;

import static org.junit.Assert.*;

import java.util.Map;

import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.domain.analysis.GisticResultZipFileParser;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;

import org.junit.Test;


/**
 * 
 */
public class GisticResultZipFileParserTest {

    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    @Test
    public void testParse() throws DataRetrievalException {
        ReporterList reporterList = new ReporterList("GISTIC test results", ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        Map<String, Map<GisticGenomicRegionReporter, Float>> gisticData =
            new GisticResultZipFileParser(reporterList, dao).parse(TestDataFiles.GISTIC_RESULT_FILE);
        assertEquals(3, gisticData.keySet().size());
        for (String sample : gisticData.keySet()) {
            Map<GisticGenomicRegionReporter, Float> data = gisticData.get(sample);
            assertEquals(38, data.keySet().size());
        }
    }
}
