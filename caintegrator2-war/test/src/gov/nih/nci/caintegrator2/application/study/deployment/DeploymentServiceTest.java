/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DeploymentServiceTest extends AbstractMockitoTest {

    private DeploymentServiceImpl deploymentServiceImpl;
    private DeploymentDaoStub daoStub;

    @Before
    public void setUp() throws Exception {
        daoStub = new DeploymentDaoStub();

        deploymentServiceImpl = new DeploymentServiceImpl();
        deploymentServiceImpl.setCaArrayFacade(caArrayFacade);
        deploymentServiceImpl.setDao(daoStub);
        deploymentServiceImpl.setDnaAnalysisHandlerFactory(new DnaAnalysisHandlerFactoryImpl());
    }

    @Test
    public void testPrepareForDeployment() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        daoStub.studyConfiguration = studyConfiguration;
        deploymentServiceImpl.prepareForDeployment(studyConfiguration);
        assertEquals(Status.PROCESSING, studyConfiguration.getStatus());
        assertTrue(daoStub.saveCalled);
    }
    @Test
    public void testPerformDeployment()
    throws ConnectionException, DataRetrievalException, ValidationException, IOException, InvalidCriterionException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        daoStub.studyConfiguration = studyConfiguration;
        StudyConfiguration studyConfiguration2 =
            deploymentServiceImpl.performDeployment(studyConfiguration, new HeatmapParameters());
        assertEquals(Status.DEPLOYED, studyConfiguration2.getStatus());
        assertTrue(daoStub.saveCalled);
    }

    @Test
    public void testPerformDeploymentWithError() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        genomicSource.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        genomicSource.getDnaAnalysisDataConfiguration().setMappingFilePath(TestDataFiles.INVALID_FILE_DOESNT_EXIST_PATH);
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        daoStub.studyConfiguration = studyConfiguration;
        studyConfiguration.getGenomicDataSources().add(genomicSource);
        deploymentServiceImpl.performDeployment(studyConfiguration, new HeatmapParameters());
        assertEquals(Status.ERROR, studyConfiguration.getStatus());
    }
    public static class DeploymentDaoStub extends CaIntegrator2DaoStub {
        private StudyConfiguration studyConfiguration;
        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Long id, Class<T> objectClass) {
            return (T) studyConfiguration;
        }
    }

}
