/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.ncia.domain.Image;

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
        List<String> patientIdsCollection = 
            client.retrievePatientCollectionIdsFromCollectionNameProject(collectionNameProject);
        for (String patientId : patientIdsCollection) {
            imageSeriesAcquisitions.addAll(createImageSeriesAcquisitions(patientId, client));
        }
        if (imageSeriesAcquisitions.isEmpty()) {
            throw new InvalidImagingCollectionException(
                    "There are no image series available for this collection");
        }
        LOGGER.info(new String("Completed retrieving ImageSeriesAcquisitions for " + collectionNameProject));
        return imageSeriesAcquisitions;
    }

    private List<ImageSeriesAcquisition> createImageSeriesAcquisitions(String patientId, NCIASearchService client) 
    throws ConnectionException {
        List<String> studies = client.retrieveStudyCollectionIdsFromPatient(patientId);
        List<ImageSeriesAcquisition> acquisitions = new ArrayList<ImageSeriesAcquisition>(studies.size());
        for (String studyId : studies) {
            acquisitions.add(convertToImageSeriesAcquisition(studyId, client, patientId));
        }
        return acquisitions;
    }

    private ImageSeriesAcquisition convertToImageSeriesAcquisition(String studyId, NCIASearchService client, 
            String patientId) 
    throws ConnectionException {
        ImageSeriesAcquisition acquisition = new ImageSeriesAcquisition();
        acquisition.setIdentifier(studyId);
        acquisition.setSeriesCollection(new HashSet<ImageSeries>());
        acquisition.setPatientIdentifier(patientId);
        List<String> seriesIdList = client.retrieveImageSeriesCollectionIdsFromStudy(studyId);
        for (String seriesId : seriesIdList) {
            ImageSeries imageSeries = convertToImageSeries(seriesId, client);
            acquisition.getSeriesCollection().add(imageSeries);
            imageSeries.setImageStudy(acquisition);
        }
        return acquisition;
    }

    private ImageSeries convertToImageSeries(String seriesId, NCIASearchService client) throws ConnectionException {
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setIdentifier(seriesId);
        Image nciaImage = client.retrieveRepresentativeImageBySeries(seriesId);
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
