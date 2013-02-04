/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Object that hold the upload file context.
 */
public class AnnotationGroupUploadFileHandler {
    
    private final File uploadFile;
    private final List<String> existingAfdNames = new ArrayList<String>();

    private static final int FILE_NUMBER_COLUMNS = 9;
    private static final String HEADER_LINE =  "File Column Name";
    
    /**
     * @param studyConfiguration the study configuration to get all existing annotation field definitions
     * @param uploadFile the upload file
     */
    public AnnotationGroupUploadFileHandler(StudyConfiguration studyConfiguration, File uploadFile) {
        super();
        this.uploadFile = uploadFile;
        initializeAfdNames(studyConfiguration);
    }

    private void initializeAfdNames(StudyConfiguration studyConfiguration) {
        for (AnnotationFieldDescriptor afd : studyConfiguration.getAllExistingDescriptors()) {
            existingAfdNames.add(afd.getName());
        }
    }

    /**
     * Extract annotation group upload data.
     * @return a list of AnnotationGroupUpload
     * @throws ValidationException validation exception
     * @throws IOException I/O exception
     */
    public List<AnnotationGroupUploadContent> extractUploadData()
    throws ValidationException, IOException {
        List<AnnotationGroupUploadContent> annotationGroupUploads = new ArrayList<AnnotationGroupUploadContent>();

        CSVReader reader;
        reader = new CSVReader(new FileReader(uploadFile));
        String[] fields;
        int lineNum = 0;
        while ((fields = reader.readNext()) != null) {
            lineNum++;
            if (fields.length != FILE_NUMBER_COLUMNS) {
                throw new ValidationException("File must have " + FILE_NUMBER_COLUMNS
                        + " columns instead of " + fields.length + " on line number " + lineNum);
            }
            readDataLine(annotationGroupUploads, fields);
        }
        reader.close();
        return annotationGroupUploads;
    }

    private void readDataLine(List<AnnotationGroupUploadContent> annotationGroupUploads, String[] fields) 
    throws ValidationException {
        if (!HEADER_LINE.equals(trimBlank(fields[0]))) {
            try {
                AnnotationGroupUploadContent uploadContent = new AnnotationGroupUploadContent();
                uploadContent.setColumnName(trimBlank(fields[0]));
                uploadContent.setAnnotationType(trimBlank(fields[1]));
                uploadContent.setEntityType(trimBlank(fields[2]));
                uploadContent.setCdeId(trimBlank(fields[3]));
                uploadContent.setVersion(trimBlank(fields[4]));
                uploadContent.setDefinitionName(trimBlank(fields[5]));
                if (uploadContent.getCdeId() == null) {
                    uploadContent.setDataType(trimBlank(fields[6]));
                    uploadContent.setPermissible(trimBlank(fields[7]));
                }
                uploadContent.setVisible(trimBlank(fields[8]));
                checkForDuplicateColumnName(annotationGroupUploads, uploadContent);
                annotationGroupUploads.add(uploadContent);
            } catch (Exception e) {
                throw new ValidationException(e.getMessage(), e);
            }
        }
    }

    private void checkForDuplicateColumnName(List<AnnotationGroupUploadContent> annotationGroupUploads,
            AnnotationGroupUploadContent uploadContent)
    throws ValidationException {
        for (AnnotationGroupUploadContent annotationGroupUploadContent : annotationGroupUploads) {
            if (annotationGroupUploadContent.getColumnName().equals(uploadContent.getColumnName())
                    || annotationGroupUploadContent.getDefinitionName().equals(uploadContent.getDefinitionName())) {
                throw new ValidationException("Duplicate definition: " + uploadContent.getColumnName());
            }
        }
        if (existingAfdNames.contains(uploadContent.getColumnName())) {
            throw new ValidationException("Definition: " + uploadContent.getColumnName() + " already exist.");
        }
    }
    
    private String trimBlank(String value) {
        return value == null ? null : value.trim();
    }
}
