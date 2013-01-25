/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Helper class used to map samples to subjects.
 */
class ImageSeriesAcquisitionMappingHelper {
    
    private static final Logger LOGGER = Logger.getLogger(ImageSeriesAcquisitionMappingHelper.class);

    private final StudyConfiguration studyConfiguration;
    private final ImageDataSourceConfiguration imageSource;
    private final File mappingFile;
    private final ImageDataSourceMappingTypeEnum mappingType;
    private Map<String, Set<ImageSeriesAcquisition>> nbiaIdentifierMap;
    

    ImageSeriesAcquisitionMappingHelper(StudyConfiguration studyConfiguration, File mappingFile,
            ImageDataSourceMappingTypeEnum mappingType, ImageDataSourceConfiguration imageSource) {
        this.studyConfiguration = studyConfiguration;
        this.mappingFile = mappingFile;
        this.mappingType = mappingType;
        this.imageSource = imageSource;
    }

    void mapImageSeries() throws ValidationException, IOException {
        if (ImageDataSourceMappingTypeEnum.AUTO.equals(mappingType)) {
            mapImageSeriesAutomatically();
            return;
        }
        CSVReader reader = new CSVReader(new FileReader(mappingFile));
        String[] values;
        while ((values = reader.readNext()) != null) {
            if (values.length != 2) {
                throw new ValidationException("Invalid file format - Expect 2 columns but has " + values.length);
            }
            map(getSubjectAssignment(values[0].trim()), getImageSeriesAcquisition(values[1].trim()));
        }
    }

    private void mapImageSeriesAutomatically() {
        for (ImageSeriesAcquisition imageSeriesAcquisition : imageSource.getImageSeriesAcquisitions()) {
            String patientIdentifier = imageSeriesAcquisition.getPatientIdentifier();
            map(getSubjectAssignment(patientIdentifier), getImageSeriesAcquisition(patientIdentifier));
        }
    }

    private void map(StudySubjectAssignment subjectAssignment, Set<ImageSeriesAcquisition> acquisitions) {
        if (subjectAssignment == null || acquisitions == null || acquisitions.isEmpty()) {
            LOGGER.warn("Couldn't map ImageSeriesAcquisition to StudySubjectAssignment due to null entity");
            return;
        }
        for (ImageSeriesAcquisition acquisition : acquisitions) {
            subjectAssignment.getImageStudyCollection().add(acquisition);
            acquisition.setAssignment(subjectAssignment);
        }
    }

    private Set<ImageSeriesAcquisition> getImageSeriesAcquisition(String identifier) {
        Set<ImageSeriesAcquisition> acquisition = getNbiaIdentifierMap().get(identifier);
        if (acquisition == null || acquisition.isEmpty()) {
            LOGGER.warn(new String("No ImageSeriesAcquisition found for identifier " + identifier));
        }
        return acquisition;
    }

    private Map<String, Set<ImageSeriesAcquisition>> getNbiaIdentifierMap() {
        if (nbiaIdentifierMap == null) {
            createNbiaIdentifierMap();
        }
        return nbiaIdentifierMap;
    }

    private void createNbiaIdentifierMap() {
        nbiaIdentifierMap = new HashMap<String, Set<ImageSeriesAcquisition>>();
        for (ImageSeriesAcquisition acquisition : imageSource.getImageSeriesAcquisitions()) {
            if (ImageDataSourceMappingTypeEnum.IMAGE_SERIES.equals(mappingType)) {
                addToNbiaIdentifierMap(acquisition.getIdentifier(), acquisition);
            } else {
                addToNbiaIdentifierMap(acquisition.getPatientIdentifier(), acquisition);
            }
        }
    }

    private void addToNbiaIdentifierMap(String identifier, ImageSeriesAcquisition acquisition) {
        Set<ImageSeriesAcquisition> imageSeriesSet = new HashSet<ImageSeriesAcquisition>();
        if (nbiaIdentifierMap.containsKey(identifier)) {
            imageSeriesSet = nbiaIdentifierMap.get(identifier);
        }
        imageSeriesSet.add(acquisition);
        nbiaIdentifierMap.put(identifier, imageSeriesSet);
        
    }

    private StudySubjectAssignment getSubjectAssignment(String subjectIdentifier) {
        StudySubjectAssignment assignment = studyConfiguration.getSubjectAssignment(subjectIdentifier);
        if (assignment == null) {
            LOGGER.warn(new String("No StudySubjectAssigment found for identifier " + subjectIdentifier));
        }
        return assignment;
    }

}
