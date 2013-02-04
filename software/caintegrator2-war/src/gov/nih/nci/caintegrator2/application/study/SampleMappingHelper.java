/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Helper class used to map samples to subjects.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
class SampleMappingHelper {

    private final StudyConfiguration studyConfiguration;
    private final File mappingFile;
    private final GenomicDataSourceConfiguration genomicSource;
    
    SampleMappingHelper(StudyConfiguration studyConfiguration, File mappingFile, 
            GenomicDataSourceConfiguration genomicSource) {
        this.studyConfiguration = studyConfiguration;
        this.mappingFile = mappingFile;
        this.genomicSource = genomicSource;
        
    }

    void mapSamples() throws ValidationException, IOException {
        CSVReader reader = new CSVReader(new FileReader(mappingFile));
        String[] values;
        unmapSamples(); // First unmap the previous mappings.
        while ((values = reader.readNext()) != null) {
            if (values.length != 2) {
                throw new ValidationException("Invalid file format - Expect 2 columns but has " + values.length);
            }
            String subjectIdentifier = values[0].trim();
            String sampleName = values[1].trim();
               
            StudySubjectAssignment subjectAssignment = getSubjectAssignment(subjectIdentifier);
            Sample sample = getSample(sampleName);
                
            // map is throwing an exception.  This is a temporary check for null to prevent it.
            if (subjectAssignment == null) {
                throw new ValidationException("Subject Identifier not found '" + subjectIdentifier + "'");
            }
            if (StringUtils.isBlank(sampleName)) {
                throw new ValidationException("No sample name for subject '" + subjectIdentifier + "'");
            }
            if (sample == null) {
                throw new ValidationException("Sample not found '" + sampleName + "'");
            }
            map(subjectAssignment, sample);
        }
    }
    
    private void unmapSamples() {
        for (Sample sample : genomicSource.getSamples()) {
            sample.removeSampleAcquisitionAssociations();
        }
    }

    private void map(StudySubjectAssignment subjectAssignment, Sample sample) {
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        sampleAcquisition.setSample(sample);
        sample.setSampleAcquisition(sampleAcquisition);
        subjectAssignment.getSampleAcquisitionCollection().add(sampleAcquisition);
    }
    

    private StudySubjectAssignment getSubjectAssignment(String subjectIdentifier) {
        return studyConfiguration.getSubjectAssignment(subjectIdentifier);
    }
    
    private Sample getSample(String sampleName) {
        return genomicSource.getSample(sampleName);
    }

}
