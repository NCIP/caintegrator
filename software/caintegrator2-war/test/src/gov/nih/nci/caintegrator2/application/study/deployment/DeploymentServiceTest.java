/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.DeploymentListener;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceTest;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("PMD")
public class DeploymentServiceTest {
    
    private DeploymentServiceImpl deploymentServiceImpl;
    private DeploymentDaoStub daoStub;    

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("studymanagement-test-config.xml", StudyManagementServiceTest.class); 
        deploymentServiceImpl = (DeploymentServiceImpl) context.getBean("deploymentService"); 
        daoStub = new DeploymentDaoStub();
        deploymentServiceImpl.setDao(daoStub);
    }

    @Test
    public void testPrepareForDeployment() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        daoStub.studyConfiguration = studyConfiguration;
        TestListener listener = new TestListener();
        deploymentServiceImpl.prepareForDeployment(studyConfiguration, listener);
        assertTrue(listener.statuses.contains(Status.PROCESSING));
        assertEquals(Status.PROCESSING, listener.configuration.getStatus());
        assertTrue(daoStub.saveCalled);
    }
    @Test
    public void testPerformDeployment() throws ConnectionException, DataRetrievalException, ValidationException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        daoStub.studyConfiguration = studyConfiguration;
        TestListener listener = new TestListener();
        deploymentServiceImpl.performDeployment(studyConfiguration, listener);
        assertTrue(listener.statuses.contains(Status.DEPLOYED));
        assertEquals(Status.DEPLOYED, listener.configuration.getStatus());
        assertTrue(daoStub.saveCalled);
    }

    @Test
    public void testPerformDeploymentWithError() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        TestListener listener = new TestListener();
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setDataType(GenomicDataSourceDataTypeEnum.COPY_NUMBER);
        genomicSource.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        genomicSource.getDnaAnalysisDataConfiguration().setMappingFilePath(TestDataFiles.INVALID_FILE_DOESNT_EXIST_PATH);
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX.getValue());
        daoStub.studyConfiguration = studyConfiguration;
        studyConfiguration.getGenomicDataSources().add(genomicSource);
        boolean cautghException = false;
        try {
            deploymentServiceImpl.performDeployment(studyConfiguration, listener);
        } catch (Exception e) {
            cautghException = true;
        }
        assertTrue(cautghException);
    }
    public static class DeploymentDaoStub extends CaIntegrator2DaoStub {
        private StudyConfiguration studyConfiguration;
        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Long id, Class<T> objectClass) {
            return (T) studyConfiguration;
        }
    }
    
    private static class TestListener implements DeploymentListener {
        private Set<Status> statuses = new HashSet<Status>();
        private StudyConfiguration configuration;
        public void statusUpdated(StudyConfiguration configuration) {
            this.configuration = configuration;
            statuses.add(configuration.getStatus());
        }
    }
    
}
