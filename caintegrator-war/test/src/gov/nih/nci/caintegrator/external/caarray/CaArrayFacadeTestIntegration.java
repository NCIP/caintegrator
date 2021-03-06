/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixCdfReadException;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixCdfReader;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfigurationFactory;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import affymetrix.fusion.cdf.FusionCDFData;

/**
 * caArray facade integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
public class CaArrayFacadeTestIntegration {

    @Autowired
    private CaArrayFacadeImpl caArrayFacade;

    @Before
    public void setUp() {
        caArrayFacade.setDao(new LocalCaIntegrator2DaoStub());
    }

    @Test
    public void testGetSamples() throws ConnectionException, ExperimentNotFoundException {
        ServerConnectionProfile profile = new ServerConnectionProfile();
        profile.setHostname("ncias-d227-v.nci.nih.gov");
        profile.setPort(31099);
        List<Sample> samples = caArrayFacade.getSamples("jagla-00034", profile);
        assertFalse(samples.isEmpty());
    }

    @Test (expected=ExperimentNotFoundException.class)
    public void testGetSamplesInvalidExperiment() throws ConnectionException, ExperimentNotFoundException {
        ServerConnectionProfile profile = new ServerConnectionProfile();
        profile.setHostname("ncias-d227-v.nci.nih.gov");
        profile.setPort(31099);
        caArrayFacade.getSamples("INVALID EXPERIMENT ID", profile);
    }

    @Test
    public void testRetrieveFile() throws FileNotFoundException, ConnectionException {
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.getServerProfile().setHostname("ncias-d227-v.nci.nih.gov");
        genomicSource.getServerProfile().setPort(31099);
        genomicSource.setExperimentIdentifier("jagla-00034");
        assertEquals(711652, caArrayFacade.retrieveFile(genomicSource, "s1_U133P2.CHP").length);
    }

    @Test
    public void testRetrieveData() throws ConnectionException, ExperimentNotFoundException, DataRetrievalException {
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.getServerProfile().setHostname("ncias-d227-v.nci.nih.gov");
        genomicSource.getServerProfile().setPort(31099);
        genomicSource.setExperimentIdentifier("jagla-00034");
        genomicSource.setPlatformName("HG-U133_Plus_2");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        genomicSource.getSamples().addAll(caArrayFacade.getSamples("jagla-00034", genomicSource.getServerProfile()));
        genomicSource.setStudyConfiguration(StudyConfigurationFactory.createNewStudyConfiguration());
        ArrayDataValues values = caArrayFacade.retrieveData(genomicSource);
        assertEquals(3, values.getArrayDatas().size());
        assertEquals(54675, values.getReporters().size());
    }

    private static class LocalCaIntegrator2DaoStub extends CaIntegrator2DaoStub {

        @Override
        public Platform getPlatform(String name) {
            Platform platform = new Platform();
            platform.setName("HG-U133_Plus_2");
            ReporterList reporterList = platform.addReporterList("HG-U133_Plus_2", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            try {
                AffymetrixCdfReader cdfReader = AffymetrixCdfReader.create(TestArrayDesignFiles.HG_U133_PLUS_2_CDF_FILE);
                FusionCDFData fusionCDFData = cdfReader.getCdfData();
                for (int i = 0; i < fusionCDFData.getHeader().getNumProbeSets(); i++) {
                    GeneExpressionReporter reporter = new GeneExpressionReporter();
                    reporter.setName(fusionCDFData.getProbeSetName(i));
                    reporterList.addReporter(reporter);
                }
                cdfReader.close();
            } catch (AffymetrixCdfReadException e) {
                throw new IllegalStateException(e);
            }

            return platform;
        }

    }

}
