/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Asynchronous thread that runs Imaging Source Loading jobs and updates the status of those jobs.
 */
public class ImagingDataSourceAjaxRunner implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ImagingDataSourceAjaxRunner.class);
    private final ImagingDataSourceAjaxUpdater updater;
    private final Long imageDataSourceId;
    private final File imageClinicalMappingFile;
    private final ImageDataSourceMappingTypeEnum mappingType;
    private final boolean mapOnly;
    private ImageDataSourceConfiguration imagingSource;
    private String username;
    private final boolean loadAimAnnotation;

    ImagingDataSourceAjaxRunner(ImagingDataSourceAjaxUpdater updater, Long imageDataSourceId,
            File imageClinicalMappingFile, ImageDataSourceMappingTypeEnum mappingType, boolean mapOnly,
            boolean loadAimAnnotation) {
        this.updater = updater;
        this.imageDataSourceId = imageDataSourceId;
        this.imageClinicalMappingFile = imageClinicalMappingFile;
        this.mappingType = mappingType;
        this.mapOnly = mapOnly;
        this.loadAimAnnotation = loadAimAnnotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        setupSession();
        updater.updateJobStatus(username, imageDataSourceId);
        if (loadAimAnnotation) {
            runLoadAimAnnotation();
        } else {
            mapImaging();
        }
        updater.updateJobStatus(username, imageDataSourceId);
    }

    private void runLoadAimAnnotation() {
        try {
            updater.getStudyManagementService().loadAimAnnotations(imageDataSourceId);
        } catch (ConnectionException e) {
            addError("The configured AIM server couldn't be reached. Please check the configuration settings.", e);
        } catch (ValidationException e) {
            addError("AIM validation error: " + e.getMessage(), e);
        } catch (Exception e) {
            addError("AIM exception: " + e.getMessage(), e);
        }
    }

    private void mapImaging() {
        try {
            if (!mapOnly) {
                addSource();
            }
            mapSource();
            updater.refreshJsp(username);
        } catch (ConnectionException e) {
            addError("The configured server couldn't be reached. Please check the configuration settings.", e);
        } catch (Exception e) {
            addError(e.getMessage(), e);
        }
    }

    private void setupSession() {
        imagingSource = updater.getStudyManagementService().getRefreshedImageSource(imageDataSourceId);
        username = imagingSource.getStudyConfiguration().getLastModifiedBy().getUsername();
    }

    private void addSource() throws ConnectionException, InvalidImagingCollectionException {
        updater.getStudyManagementService().loadImageSource(imagingSource);
    }

    private void mapSource() {
        try {
            updater.getStudyManagementService().mapImageSeriesAcquisitions(imagingSource.getStudyConfiguration(),
                    imagingSource, imageClinicalMappingFile, mappingType);
        } catch (ValidationException e) {
            addError("Invalid file: " + e.getResult().getInvalidMessage(), e);
        } catch (IOException e) {
            addError(e.getMessage(), e);
        } finally {
            FileUtils.deleteQuietly(imageClinicalMappingFile);
        }
    }

    private void addError(String message, Exception e) {
        LOGGER.error("Deployment of imaging source failed.", e);
        imagingSource.setStatus(Status.ERROR);
        imagingSource.setStatusDescription(message);
        updater.saveAndUpdateJobStatus(username, imagingSource);
    }
}
