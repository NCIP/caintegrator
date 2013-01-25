/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentCnPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentGemlCghPlatformLoader;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfigurationFactory;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AbstractCaArrayCopyNumberFacadeTestIntegration {

    private CaArrayFacadeImpl caArrayFacade;
    private static String HOSTNAME = "array-stage.nci.nih.gov";
    private static int PORT = 8080;
    private static String PLATFORM_NAME = "022522_D_F_20090107";
    private static String EXPERIMENT = "EXP-475";
    
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("caarray-test-config.xml", CaArrayFacadeTestIntegration.class); 
        caArrayFacade = (CaArrayFacadeImpl) context.getBean("CaArrayFacadeIntegration");
        caArrayFacade.setDao(new LocalCaIntegrator2DaoStub());
    }
    
    @Test
    public void testGetSamples() throws ConnectionException, ExperimentNotFoundException {
        ServerConnectionProfile profile = new ServerConnectionProfile();
        profile.setHostname(HOSTNAME);
        profile.setPort(PORT);
        List<Sample> samples = caArrayFacade.getSamples(EXPERIMENT, profile);
        assertFalse(samples.isEmpty());
    }
    
    @Test (expected=ExperimentNotFoundException.class)
    public void testGetSamplesInvalidExperiment() throws ConnectionException, ExperimentNotFoundException {
        ServerConnectionProfile profile = new ServerConnectionProfile();
        profile.setHostname(HOSTNAME);
        profile.setPort(PORT);
        caArrayFacade.getSamples("INVALID EXPERIMENT ID", profile);        
    }
    
    @Test
    public void testRetrieveFile() throws FileNotFoundException, ConnectionException {
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.getServerProfile().setHostname(HOSTNAME);
        genomicSource.getServerProfile().setPort(PORT);
        genomicSource.setExperimentIdentifier(EXPERIMENT);
        byte[] retrieveFile = caArrayFacade.retrieveFile(genomicSource, "Netherlands_Demo_20.idf");
        assertEquals(255, retrieveFile.length);
    }

    @Test
    public void testRetrieveData() throws ConnectionException, ExperimentNotFoundException, DataRetrievalException {
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.getServerProfile().setHostname(HOSTNAME);
        genomicSource.getServerProfile().setPort(PORT);
        genomicSource.setExperimentIdentifier(EXPERIMENT);
        genomicSource.setPlatformName(PLATFORM_NAME);
        genomicSource.setPlatformVendor(PlatformVendorEnum.AGILENT);
        genomicSource.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        genomicSource.getSamples().addAll(caArrayFacade.getSamples(EXPERIMENT, genomicSource.getServerProfile()));
        genomicSource.setStudyConfiguration(StudyConfigurationFactory.createNewStudyConfiguration());
        List<ArrayDataValues> valuesList = caArrayFacade.retrieveDnaAnalysisData(genomicSource, new ArrayDataServiceStub());
        assertEquals(3, valuesList.size());
        assertEquals(174341, valuesList.iterator().next().getReporters().size());
    }

    private static class LocalCaIntegrator2DaoStub extends CaIntegrator2DaoStub {

        private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
        
        @Override
        public Platform getPlatform(String name) {
            Platform platform = new Platform();
            platform.setName(PLATFORM_NAME);

            AgilentCnPlatformSource agilentCnPlatformSource = new AgilentCnPlatformSource (
                    TestArrayDesignFiles.AGILENT_22522_XML_ANNOTATION_FILE,
                    PLATFORM_NAME,
                    TestArrayDesignFiles.AGILENT_22522_XML_ANNOTATION_FILE_PATH);

            AgilentGemlCghPlatformLoader xmlLoader = new AgilentGemlCghPlatformLoader(
                    agilentCnPlatformSource);
            
            try {
                platform = xmlLoader.load(dao);
            } catch (PlatformLoadingException e) {
                throw new IllegalStateException(e);
            }

            return platform;
        }
        
    }

}
