/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

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

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class NCIASearchServiceTestIntegration {

    @Test
    public void testRetrieveRealObjects() throws ConnectionException {

        ApplicationContext context = new ClassPathXmlApplicationContext("ncia-test-config.xml", NCIASearchServiceTestIntegration.class); 
        // TODO - 5/27/09 Ngoc, temporary pointing Dev because retrieveRepresentativeImageBySeries is only available on Dev
        //ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaServerConnectionProfile");
        ServerConnectionProfile profile = (ServerConnectionProfile) context.getBean("nciaServerConnectionProfile");
        NCIAServiceFactoryImpl nciaServiceClient = (NCIAServiceFactoryImpl) context.getBean("nciaServiceFactoryIntegration");
        
        NCIASearchService searchService;

        searchService = nciaServiceClient.createNCIASearchService(profile);

        assertNotNull(searchService.retrieveAllCollectionNameProjects());

        List<Patient> patients = searchService.retrievePatientCollectionFromCollectionNameProject("NCRI"); 
        assertNotNull(patients);
        
        List<Study> studies = searchService.retrieveStudyCollectionFromPatient(patients.get(0).getPatientId());
        assertNotNull(studies);
        
        List<Series> series = searchService.retrieveImageSeriesCollectionFromStudy(studies.get(0).getStudyInstanceUID());
        assertNotNull(series);
        
        List<Image> images = searchService.retrieveImageCollectionFromSeries(series.get(0).getSeriesInstanceUID());
        assertNotNull(images);
        
        Image image = searchService.retrieveRepresentativeImageBySeries(series.get(0).getSeriesInstanceUID());
        assertTrue(contains(images, image));
        
        assertTrue(searchService.validate(series.get(0).getSeriesInstanceUID()));
        
        assertFalse(searchService.validate("INVALID SERIES UID"));
    }
    
    private boolean contains(List<Image> images, Image checkImage) {
        for (Image image : images) {
            if (image.getSopInstanceUID().equalsIgnoreCase(checkImage.getSopInstanceUID())) {
                return true;
            }
        }
        return false;
    }

}
