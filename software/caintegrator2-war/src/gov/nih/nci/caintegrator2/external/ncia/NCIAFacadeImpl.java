/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

/**
 * Implementation of the NCIAFacade.
 */
public class NCIAFacadeImpl implements NCIAFacade {
    
    private static final Logger LOGGER = Logger.getLogger(NCIAFacadeImpl.class);
    private NCIAServiceFactory nciaServiceFactory;
    private NCIADicomJobFactory nciaDicomJobFactory;
    private FileManager fileManager;

    /**
     * {@inheritDoc}
     */
    public List<String> getAllCollectionNameProjects(ServerConnectionProfile profile) 
    throws ConnectionException {
        NCIASearchService client = nciaServiceFactory.createNCIASearchService(profile);
        return client.retrieveAllCollectionNameProjects();
    }
    
    /**
     * {@inheritDoc}
     */
    public void validateImagingSourceConnection(ServerConnectionProfile profile, String collectionNameProject) 
    throws ConnectionException, InvalidImagingCollectionException {
        List<String> validCollectionNames = getAllCollectionNameProjects(profile);
        if (!validCollectionNames.contains(collectionNameProject)) {
            throw new InvalidImagingCollectionException("No collection exists with the name '" 
                + collectionNameProject + "'.  The valid names are:  " 
                + StringUtils.join(validCollectionNames, " // "));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ImageSeriesAcquisition> getImageSeriesAcquisitions(String collectionNameProject, 
            ServerConnectionProfile profile) throws ConnectionException, InvalidImagingCollectionException {
        LOGGER.info(new String("Retrieving ImageSeriesAcquisitions for " + collectionNameProject));
        NCIASearchService client = nciaServiceFactory.createNCIASearchService(profile);
        List<ImageSeriesAcquisition> imageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        List<Patient> patientCollection = 
            client.retrievePatientCollectionFromCollectionNameProject(collectionNameProject);
        for (Patient patient : patientCollection) {
            imageSeriesAcquisitions.addAll(createImageSeriesAcquisitions(patient, client));
        }
        if (imageSeriesAcquisitions.isEmpty()) {
            throw new InvalidImagingCollectionException(
                    "There are no image series available for this collection");
        }
        LOGGER.info(new String("Completed retrieving ImageSeriesAcquisitions for " + collectionNameProject));
        return imageSeriesAcquisitions;
    }

    private List<ImageSeriesAcquisition> createImageSeriesAcquisitions(Patient patient, NCIASearchService client) 
    throws ConnectionException {
        List<Study> studies = client.retrieveStudyCollectionFromPatient(patient.getPatientId());
        List<ImageSeriesAcquisition> acquisitions = new ArrayList<ImageSeriesAcquisition>(studies.size());
        for (Study study : studies) {
            acquisitions.add(convertToImageSeriesAcquisition(study, client, patient.getPatientId()));
        }
        return acquisitions;
    }

    private ImageSeriesAcquisition convertToImageSeriesAcquisition(Study study, NCIASearchService client, 
            String patientId) 
    throws ConnectionException {
        ImageSeriesAcquisition acquisition = new ImageSeriesAcquisition();
        acquisition.setIdentifier(study.getStudyInstanceUID());
        acquisition.setSeriesCollection(new HashSet<ImageSeries>());
        acquisition.setPatientIdentifier(patientId);
        List<Series> seriesList = client.retrieveImageSeriesCollectionFromStudy(study.getStudyInstanceUID());
        for (Series series : seriesList) {
            ImageSeries imageSeries = convertToImageSeries(series, client);
            acquisition.getSeriesCollection().add(imageSeries);
            imageSeries.setImageStudy(acquisition);
        }
        return acquisition;
    }

    private ImageSeries convertToImageSeries(Series series, NCIASearchService client) throws ConnectionException {
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setIdentifier(series.getSeriesInstanceUID());
        Image nciaImage = client.retrieveRepresentativeImageBySeries(series.getSeriesInstanceUID());
        // TODO - 5/25/09 Ngoc, temporary check because this method is only available on Dev
        if (nciaImage == null) {
            return imageSeries;
        }
        gov.nih.nci.caintegrator2.domain.imaging.Image image = convertToImage(nciaImage);
        imageSeries.getImageCollection().add(image);
        image.setSeries(imageSeries);
        return imageSeries;
    }

    private gov.nih.nci.caintegrator2.domain.imaging.Image convertToImage(Image nciaImage) {
        gov.nih.nci.caintegrator2.domain.imaging.Image image = new gov.nih.nci.caintegrator2.domain.imaging.Image();
        image.setIdentifier(nciaImage.getSopInstanceUID());
        return image;
    }
    
    /**
     * {@inheritDoc}
     */
    public File retrieveDicomFiles(NCIADicomJob job) 
        throws ConnectionException {
        if (!job.hasData()) {
            return null;
        }
        NCIADicomJobRunner jobRunner = nciaDicomJobFactory.createNCIADicomJobRunner(fileManager, job);
        return jobRunner.retrieveDicomFiles();
    }

    /**
     * @return the nciaServiceFactory
     */
    public NCIAServiceFactory getNciaServiceFactory() {
        return nciaServiceFactory;
    }

    /**
     * @param nciaServiceFactory the nciaServiceFactory to set
     */
    public void setNciaServiceFactory(NCIAServiceFactory nciaServiceFactory) {
        this.nciaServiceFactory = nciaServiceFactory;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return the nciaDicomJobFactory
     */
    public NCIADicomJobFactory getNciaDicomJobFactory() {
        return nciaDicomJobFactory;
    }

    /**
     * @param nciaDicomJobFactory the nciaDicomJobFactory to set
     */
    public void setNciaDicomJobFactory(NCIADicomJobFactory nciaDicomJobFactory) {
        this.nciaDicomJobFactory = nciaDicomJobFactory;
    }
  
}
