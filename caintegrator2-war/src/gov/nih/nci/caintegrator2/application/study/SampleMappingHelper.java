/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Helper class used to map samples to subjects.
 */
class SampleMappingHelper {

    private final StudyConfiguration studyConfiguration;
    private final File mappingFile;
    private final GenomicDataSourceConfiguration genomicSource;

    private static final int PARSED_DATA_SAMPLE_MAPPING_COLUMN = 2;

    SampleMappingHelper(StudyConfiguration studyConfiguration, File mappingFile,
            GenomicDataSourceConfiguration genomicSource) {
        this.studyConfiguration = studyConfiguration;
        this.mappingFile = mappingFile;
        this.genomicSource = genomicSource;
    }

    void mapSamples() throws ValidationException, IOException {
        CSVReader reader = new CSVReader(new FileReader(mappingFile));
        String[] values;
        int columnNumber = ArrayDataLoadingTypeEnum.PARSED_DATA == genomicSource.getLoadingType()
            ? PARSED_DATA_SAMPLE_MAPPING_COLUMN : genomicSource.getPlatformVendor().getSampleMappingColumns();
        while ((values = Cai2Util.readDataLine(reader)) != null) {
            if (values.length < columnNumber) {
                throw new ValidationException("Invalid file format - Expect at least " + columnNumber
                        + " columns but has " + values.length);
            }
            String subjectIdentifier = values[0].trim();
            String sampleName = values[1].trim();

            StudySubjectAssignment subjectAssignment = getSubjectAssignment(subjectIdentifier);
            Sample sample = getSample(sampleName);

            if (sample == null) {
                sample = new Sample();
                sample.setName(sampleName);
                sample.setGenomicDataSource(genomicSource);
                sample.setCreationDate(new Date());
                genomicSource.getSamples().add(sample);
            }
            validateMapping(subjectIdentifier, sampleName, subjectAssignment);
            map(subjectAssignment, sample);
        }
    }

    private void validateMapping(String subjectIdentifier, String sampleName, StudySubjectAssignment subjectAssignment)
            throws ValidationException {
        // map is throwing an exception.  This is a temporary check for null to prevent it.
        if (subjectAssignment == null) {
            throw new ValidationException("Subject Identifier not found '" + subjectIdentifier + "'");
        }
        if (StringUtils.isBlank(sampleName)) {
            throw new ValidationException("No sample name for subject '" + subjectIdentifier + "'");
        }
    }

    private void map(StudySubjectAssignment subjectAssignment, Sample sample) {
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        sampleAcquisition.setSample(sample);
        sampleAcquisition.setAssignment(subjectAssignment);
        sample.getSampleAcquisitions().add(sampleAcquisition);

        //Remove the below call once we have a saner way of checking if samples have been mapped that doesn't rely on
        //checking mapped samples before they are persisted.
        subjectAssignment.getSampleAcquisitionCollection().add(sampleAcquisition);
    }


    private StudySubjectAssignment getSubjectAssignment(String subjectIdentifier) {
        return studyConfiguration.getSubjectAssignment(subjectIdentifier);
    }

    private Sample getSample(String sampleName) {
        return genomicSource.getSample(sampleName);
    }

}
