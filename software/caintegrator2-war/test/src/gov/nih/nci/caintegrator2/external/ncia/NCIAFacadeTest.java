/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */
public class NCIAFacadeTest {
    private static final Logger LOGGER = Logger.getLogger(NCIAFacadeTest.class);
    NCIAFacade nciaFacade;
    ServerConnectionProfile connection;
    
    
    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("ncia-test-config.xml", NCIAFacadeTest.class); 
        connection = new ServerConnectionProfile();
        NCIAFacadeImpl nciaFacadeImpl = (NCIAFacadeImpl) context.getBean("nciaFacade");
        nciaFacade = nciaFacadeImpl;
    }


    @Test
    public void testGetAllCollectionNameProjects() throws ConnectionException {
        List<String> allProjects;
        
        allProjects = nciaFacade.getAllCollectionNameProjects(connection);
        if (!allProjects.isEmpty()) {
            LOGGER.info("Retrieve Projects PASSED - " + allProjects.size() + " projects found.");
            assertEquals("Project1", allProjects.get(0));
            assertEquals("Project2", allProjects.get(1));
            assertTrue(true);
        } else {
            LOGGER.error("Retrieve Projects FAILED, might be a connection error!");
            fail();
        }
    }


    @Test
    public void testGetImageSeriesAcquisition() throws ConnectionException {
        String collectionNameProject = "RIDER";
        List<ImageSeriesAcquisition> imageStudies;
        
        imageStudies = nciaFacade.getImageSeriesAcquisitions(collectionNameProject, connection);
        if (!imageStudies.isEmpty()){
            LOGGER.info("Retrieve ImageSeriesAcquisition PASSED - " + imageStudies.size() + " were found.");
        } else {
            LOGGER.error("Retrieve ImageSeriesAcquisition FAILED, might be a connection error!");
        }
        assertTrue(true);
    }
    
    @Test
    public void testRetrieveDicomFiles() throws ConnectionException {
        NCIADicomJob job = new NCIADicomJob();
        job.setJobId("test");
        job.setServerConnection(new ServerConnectionProfile());
        assertNull(nciaFacade.retrieveDicomFiles(job));
        job.getImageSeriesIDs().add("test");
        assertNotNull(nciaFacade.retrieveDicomFiles(job));
        NCIAFacadeImpl nciaFacadeImpl = (NCIAFacadeImpl) nciaFacade;
        NCIADicomJobFactoryStub jobFactoryStub = (NCIADicomJobFactoryStub) nciaFacadeImpl.getNciaDicomJobFactory();
        assertTrue(jobFactoryStub.nciaDicomJobRunnerStub.retrieveDicomFilesCalled);
    }
        
}
