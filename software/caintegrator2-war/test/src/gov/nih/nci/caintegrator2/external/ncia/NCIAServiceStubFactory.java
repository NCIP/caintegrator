/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class NCIAServiceStubFactory implements NCIAServiceFactory {

    public NCIASearchService createNCIASearchService(ServerConnectionProfile profile) throws ConnectionException {
           return new ServiceClientStub();
    }
    private static class ServiceClientStub implements NCIASearchService {
        
        public boolean validate(String seriesInstanceUID) throws ConnectionException {
            return true;
            
        }

        public Image retrieveRepresentativeImageBySeries(String seriesInstanceUID) throws ConnectionException {
            Image image = new Image();
            image.setId(456);
            return image;
        }
        
        public List<Image> retrieveImageCollectionFromSeries(String seriesInstanceUID) throws ConnectionException {
            Image i = new Image();
            i.setId(123);
            
            List<Image> images = new ArrayList<Image>();
            images.add(i);
            return images;
        }

        
        public List<Series> retrieveImageSeriesCollectionFromStudy(String studyInstanceUID) throws ConnectionException {
            Series s = new Series();
            s.setSeriesInstanceUID("SERIESUID");
            s.setId(123);
            List<Series> series = new ArrayList<Series>();
            series.add(s);
            return series;
        }
        
        public List<String> retrieveImageSeriesCollectionIdsFromStudy(String studyInstanceUID)
                throws ConnectionException {
            List<String> seriesIds = new ArrayList<String>();
            seriesIds.add("SERIESUID");
            return seriesIds;
        }
        
        public List<String> retrieveImageCollectionIdsFromSeries(String seriesInstanceUID) throws ConnectionException {
            return null;
        }

        
        public List<Study> retrieveStudyCollectionFromPatient(String patientId) throws ConnectionException {
            Study s = new Study();
            s.setStudyInstanceUID("STUDYUID");
            s.setStudyDescription("DESCRIPTION");
            List<Study> studies = new ArrayList<Study>();
            studies.add(s);
            return studies;
        }
        
        public List<String> retrieveStudyCollectionIdsFromPatient(String patientId) throws ConnectionException {
            List<String> studyIds = new ArrayList<String>();
            studyIds.add("STUDYUID");
            return studyIds;
        }

        
        public List<Patient> retrievePatientCollectionFromCollectionNameProject(String provenanceProject)
                throws ConnectionException {
            Patient p = new Patient();
            p.setPatientId("PATIENTID");
            p.setPatientName("PATIENTNAME");
            List<Patient> patients = new ArrayList<Patient>();
            patients.add(p);
            return patients;
        }
        
        public List<String> retrievePatientCollectionIdsFromCollectionNameProject(String collectionNameProject)
                throws ConnectionException {
            List<String> patientIds = new ArrayList<String>();
            patientIds.add("PATIENTID");
            return patientIds;
        }

        
        public List<String> retrieveAllCollectionNameProjects() throws ConnectionException {
            List<String> projects = new ArrayList<String>();
            projects.add("Project1");
            projects.add("Project2");
            return projects;
        }
        
        

    }

}
