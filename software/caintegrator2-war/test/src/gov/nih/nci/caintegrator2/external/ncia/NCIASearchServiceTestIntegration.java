/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class NCIASearchServiceTestIntegration {

    NCIASearchService searchService;
    
    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("ncia-test-config.xml", NCIASearchServiceTestIntegration.class); 
        //ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaServerConnectionProfile");
        //ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaNiamsServerConnectionProfile");
        //ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaQaServerConnectionProfile");
        ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaQaDirectConnectionProfile");
        NCIAServiceFactoryImpl nciaServiceClient = (NCIAServiceFactoryImpl) context.getBean("nciaServiceFactoryIntegration");
        searchService = nciaServiceClient.createNCIASearchService(profile);
    }
    
    @Test
    public void testRetrieveRealObjects() throws ConnectionException {
        assertNotNull(searchService.retrieveAllCollectionNameProjects());

        List<Patient> patients = searchService.retrievePatientCollectionFromCollectionNameProject("NCRI"); 
        List<String> patientIds = searchService.retrievePatientCollectionIdsFromCollectionNameProject("NCRI");
        assertNotNull(patients);
        assertEquals(patients.size(), patientIds.size());
        
        List<Study> studies = searchService.retrieveStudyCollectionFromPatient(patients.get(0).getPatientId());
        assertNotNull(studies);
        
        List<Series> series = searchService.retrieveImageSeriesCollectionFromStudy(studies.get(0).getStudyInstanceUID());
        assertNotNull(series);
        
        assertFalse(searchService.validate("INVALID SERIES UID"));
    }
    
    @Test
    public void testRetrieveRealObjectsIdentifiers() throws ConnectionException {
        assertNotNull(searchService.retrieveAllCollectionNameProjects());

        List<String> patients = searchService.retrievePatientCollectionIdsFromCollectionNameProject("NCRI");
        assertNotNull(patients);
        
        List<String> studies = searchService.retrieveStudyCollectionIdsFromPatient(patients.get(0));
        assertNotNull(studies);
        
        List<String> series = searchService.retrieveImageSeriesCollectionIdsFromStudy(studies.get(0));
        assertNotNull(series);
        
        List<String> images = searchService.retrieveImageCollectionIdsFromSeries(series.get(0));
        assertNotNull(images);
        
        Image image = searchService.retrieveRepresentativeImageBySeries(series.get(0));
        String imageId = image.getSopInstanceUID();
        assertTrue(images.contains(imageId));
        assertTrue(searchService.validate(series.get(0)));
    }
    
//    @Test
//    public void testRetrieveRealObjectsIdentifiersAnalytic() throws ConnectionException {
//        assertNotNull(searchService.retrieveAllCollectionNameProjects());
//
//        List<String> patients = searchService.retrievePatientCollectionIdsFromCollectionNameProject("NCRI");
//        assertNotNull(patients);
//        
//        List<String> studies = searchService.retrieveStudyCollectionIdsFromPatient(patients.get(0));
//        assertNotNull(studies);
//        
//        List<String> series = searchService.retrieveImageSeriesCollectionIdsFromStudy(studies.get(0));
//        assertNotNull(series);
//
//        //List<String> images = searchService.retrieveImageCollectionIdsFromSeries("1.3.6.1.4.1.21767.172.16.10.81.1194988596.2.0.7");
//        String[] results = searchService.getSOPInstanceUIDsFromSeriesInstanceUIDNew(series.get(0));
//        List<String> images = Arrays.asList(results);
//        assertNotNull(images);
//
//        Image image = searchService.retrieveRepresentativeImageBySeries(series.get(0));
//        String imageId = image.getSopInstanceUID();
//        assertTrue(images.contains(imageId));
//        assertTrue(searchService.validate(series.get(0)));
//    }
//    @Test
//    public void testRetrieveRealObjectsIdentifiersMany() throws ConnectionException {
//        // This methods tests retrieval of a large number of
//        // image identifiers using a NIAMS NBIA server which has a lot of images.
//        Boolean test=Boolean.FALSE;
//        
//        ApplicationContext context = new ClassPathXmlApplicationContext("ncia-test-config.xml", NCIASearchServiceTestIntegration.class); 
//        ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaNiamsServerConnectionProfile");
//        NCIAServiceFactoryImpl nciaServiceClient = (NCIAServiceFactoryImpl) context.getBean("nciaServiceFactoryIntegration");
//        searchService = nciaServiceClient.createNCIASearchService(profile);
//        
//        assertNotNull(searchService.retrieveAllCollectionNameProjects());
//
//        List<String> images = searchService.retrieveImageCollectionIdsFromSeries("1.3.6.1.4.1.21767.172.16.10.81.1194988596.2.0.7");
//        assertNotNull(images);
//        if(images.size() > 0) {
//            test=Boolean.TRUE;
//        }
//                
//        assertTrue(test);
//    }    

}
