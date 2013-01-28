/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.ncia.NCIASearchService;
import gov.nih.nci.caintegrator.external.ncia.NCIAServiceFactoryImpl;
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
        ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaQaServerConnectionProfile");
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
}
