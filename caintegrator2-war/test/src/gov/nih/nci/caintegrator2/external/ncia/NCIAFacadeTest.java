/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 */
public class NCIAFacadeTest extends AbstractMockitoTest {
    private static final Logger LOGGER = Logger.getLogger(NCIAFacadeTest.class);
    private NCIAFacade nciaFacade;
    private ServerConnectionProfile connection;
    private NCIADicomJobFactory nciaDicomJobFactory;
    private NCIADicomJobRunner runner;
    private NCIASearchService nciaSearchService;

    @Before
    public void setUp() throws Exception {
        connection = new ServerConnectionProfile();
        runner = mock(NCIADicomJobRunner.class);
        when(runner.retrieveDicomFiles()).thenReturn(new File("."));
        nciaDicomJobFactory = mock(NCIADicomJobFactory.class);
        when(nciaDicomJobFactory.createNCIADicomJobRunner(any(FileManager.class), any(NCIADicomJob.class))).thenReturn(runner);

        nciaSearchService = mock(NCIASearchService.class);
        when(nciaSearchService.validate(anyString())).thenReturn(Boolean.TRUE);
        when(nciaSearchService.retrieveRepresentativeImageBySeries(anyString())).then(new Answer<Image>() {
            @Override
            public Image answer(InvocationOnMock invocation) throws Throwable {
                Image image = new Image();
                image.setId(456);
                return image;
            }
        });
        when(nciaSearchService.retrieveRepresentativeImageBySeries(anyString())).then(new Answer<Image>() {
            @Override
            public Image answer(InvocationOnMock invocation) throws Throwable {
                Image image = new Image();
                image.setId(123);
                return image;
            }
        });
        when(nciaSearchService.retrieveImageSeriesCollectionFromStudy(anyString())).then(new Answer<List<Series>>() {

            @Override
            public List<Series> answer(InvocationOnMock invocation) throws Throwable {
                Series s = new Series();
                s.setSeriesInstanceUID("SERIESUID");
                s.setId(123);
                return Arrays.asList(s);
            }
        });
        when(nciaSearchService.retrieveImageSeriesCollectionIdsFromStudy(anyString())).thenReturn(Arrays.asList("SERIESUID"));
        when(nciaSearchService.retrieveStudyCollectionFromPatient(anyString())).then(new Answer<List<Study>>() {
            @Override
            public List<Study> answer(InvocationOnMock invocation) throws Throwable {
                Study s = new Study();
                s.setStudyInstanceUID("STUDYUID");
                s.setStudyDescription("DESCRIPTION");
                return Arrays.asList(s);
            }
        });
        when(nciaSearchService.retrieveStudyCollectionIdsFromPatient(anyString())).thenReturn(Arrays.asList("STUDYUID"));
        when(nciaSearchService.retrievePatientCollectionFromCollectionNameProject(anyString())).then(new Answer<List<Patient>>() {
            @Override
            public List<Patient> answer(InvocationOnMock invocation) throws Throwable {
                Patient p = new Patient();
                p.setPatientId("PATIENTID");
                p.setPatientName("PATIENTNAME");
                return Arrays.asList(p);
            }
        });
        when(nciaSearchService.retrievePatientCollectionIdsFromCollectionNameProject(anyString())).thenReturn(Arrays.asList("PATIENTID"));
        when(nciaSearchService.retrieveAllCollectionNameProjects()).thenReturn(Arrays.asList("Project1", "Project2"));

        NCIAServiceFactory nciaServiceFactory = mock(NCIAServiceFactory.class);
        when(nciaServiceFactory.createNCIASearchService(any(ServerConnectionProfile.class))).thenReturn(nciaSearchService);

        NCIAFacadeImpl nciaFacadeImpl = new NCIAFacadeImpl();
        nciaFacadeImpl.setNciaServiceFactory(nciaServiceFactory);
        nciaFacadeImpl.setFileManager(fileManager);
        nciaFacadeImpl.setNciaDicomJobFactory(nciaDicomJobFactory);
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
    public void testGetImageSeriesAcquisition() throws ConnectionException, InvalidImagingCollectionException {
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
        verify(runner, times(1)).retrieveDicomFiles();
    }
}
