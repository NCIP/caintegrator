/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.AnnotationUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Represents a CSV annotation text file.
 */
public class AnnotationFile extends AbstractCaIntegrator2Object {
    private static final long serialVersionUID = 1L;
    private String path;
    private List<FileColumn> columns = new ArrayList<FileColumn>();
    private String currentlyLoaded;
    private transient File file;
    private transient CSVReader reader;
    private transient String[] currentLineValues;
    private transient Map<AnnotationFieldDescriptor, FileColumn> descriptorToColumnMap;
    private final transient Set<String> currentlyLoadedIdentifier = new HashSet<String>();


    /**
     * No-arg constructor required by Hibernate.
     */
    @Deprecated
    public AnnotationFile() {
        super();
    }

    private AnnotationFile(File file) {
        setPath(file.getAbsolutePath());
    }

    /**
     * @return the columns
     */
    public List<FileColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<FileColumn> columns) {
        this.columns = columns;
    }

    static AnnotationFile load(File file, CaIntegrator2Dao dao, StudyConfiguration studyConfiguration,
            EntityTypeEnum type, boolean createNewAnnotationDefinition) throws ValidationException {
        return createAndLoadFile(file, dao, studyConfiguration, type, createNewAnnotationDefinition);
    }

    private static AnnotationFile createAndLoadFile(File file, CaIntegrator2Dao dao,
            StudyConfiguration studyConfiguration, EntityTypeEnum type, boolean createNewAnnotationDefinition)
    throws ValidationException {
        AnnotationFile annotationFile = new AnnotationFile(file);
        annotationFile.validateFileFormat();
        annotationFile.loadColumns(dao, studyConfiguration, type, createNewAnnotationDefinition);
        return annotationFile;
    }

    private void loadColumns(CaIntegrator2Dao dao, StudyConfiguration studyConfiguration,
            EntityTypeEnum type, boolean createNewAnnotationDefinition)
    throws ValidationException {
        resetReader();
        loadNextLine();
        for (int index = 0; index < currentLineValues.length; index++) {
            FileColumn column = new FileColumn(this);
            column.setPosition(index);
            column.setName(currentLineValues[index].trim());
            column.retrieveOrCreateFieldDescriptor(dao, studyConfiguration, type, createNewAnnotationDefinition);
            columns.add(column);
        }
    }

    private void validateFileFormat() throws ValidationException {
        validateFileExists();
        validateFileNotEmpty();
        validateFileHasData();
        validateDataLinesConsistent();
        validateUniqueNonNullHeaders();
    }

    private void validateFileNotEmpty() throws ValidationException {
        resetReader();
        if (readNext() == null) {
            throwValidationException("The data file was empty.");
        }

    }

    private void validateFileHasData() throws ValidationException {
        positionAtData();
        if (readNext() == null) {
            throwValidationException("The data file contained no data (header line only).");
        }
    }

    private void validateUniqueNonNullHeaders() throws ValidationException {
        resetReader();
        loadNextLine();
        Set<String> headerValues = new HashSet<String>();
        for (int index = 0; index < currentLineValues.length; index++) {
            String currentHeaderValue = currentLineValues[index];
            if (StringUtils.isBlank(currentHeaderValue)) {
                throwValidationException("The column header at index " + Integer.valueOf(index + 1) + " is blank.");
            }
            if (headerValues.contains(currentHeaderValue.toUpperCase(Locale.ENGLISH))) {
                throwValidationException("The column header '" + currentHeaderValue + "' is used more than once in "
                        + "this file, please make sure all header names are unique and re-upload the file.");
            }

            Pattern p = Pattern.compile("^[A-Za-z0-9_\\-\\(\\)\\[\\]\\/:. ]+$");
            if (!p.matcher(currentHeaderValue).matches()) {
                throwValidationException("The column header name '" + currentHeaderValue
                        + "' contains invalid characters. "
                        + "Allowed characters are a-z, A-Z, 0-9, "
                        + "underscore, hyphen, space, forward slash, "
                        + "parentheses, square brackets, colon, period.");
            }
            headerValues.add(currentHeaderValue.toUpperCase(Locale.ENGLISH));
        }
    }

    private void validateDataLinesConsistent() throws ValidationException {
        resetReader();
        int numberOfHeaders = readNext().length;
        int currentLine = 2;
        String[] values;
        while ((values = readNext()) != null) {
            if (values.length != numberOfHeaders) {
                throwValidationException("Number of values in line " + currentLine
                        + " inconsistent with header line. Expected " + numberOfHeaders + " but found "
                        + values.length + " values.");
            }
            currentLine++;
        }
    }

    private void validateFileExists() throws ValidationException {
        if (!getFile().exists()) {
            throwValidationException("The file " + getFile().getAbsolutePath() + " could not be found");
        }
    }
    private void throwValidationException(String message) throws ValidationException {
        ValidationResult result = new ValidationResult();
        result.setInvalidMessage(message);
        throw new ValidationException(result);
    }

    /**
     * Returns the underlying file.
     *
     * @return the file.
     */
    public File getFile() {
        if (file == null) {
            file = new File(getPath());
        }
        return file;
    }

    private String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the identifierColumn
     */
    public FileColumn getIdentifierColumn() {
        for (FileColumn column : columns) {
            if (column.isIdentifierColumn()) {
                return column;
            }
        }
        return null;
    }

    /**
     * @param identifierColumn the identifierColumn to set
     */
    public void setIdentifierColumn(FileColumn identifierColumn) {
        FileColumn currentIdentifier = getIdentifierColumn();
        if (currentIdentifier != null && !currentIdentifier.equals(identifierColumn)) {
            currentIdentifier.getFieldDescriptor().setType(AnnotationFieldType.ANNOTATION);
        }
        if (identifierColumn != null) {
            identifierColumn.setupAnnotationFieldDescriptor(AnnotationFieldType.IDENTIFIER);
            identifierColumn.getFieldDescriptor().setDefinition(null);
        }
    }


    /**
     * @return the timepointColumn
     */
    public FileColumn getTimepointColumn() {
        for (FileColumn column : columns) {
            if (column.isTimepointColumn()) {
                return column;
            }
        }
        return null;
    }

    /**
     * @param timepointColumn the timepointColumn to set
     */
    public void setTimepointColumn(FileColumn timepointColumn) {
        FileColumn currentTimepoint = getTimepointColumn();
        if (currentTimepoint != null && !currentTimepoint.equals(timepointColumn)) {
            currentTimepoint.getFieldDescriptor().setType(AnnotationFieldType.ANNOTATION);
        }
        if (timepointColumn != null) {
            timepointColumn.setupAnnotationFieldDescriptor(AnnotationFieldType.TIMEPOINT);
        }
    }

    private String[] readNext() throws ValidationException {
        try {
            return reader.readNext();
        } catch (IOException e) {
            throwValidationException("Error reading file.");
            return null;
        }
    }

    void positionAtData() throws ValidationException {
        resetReader();
        loadNextLine();
    }

    private void loadNextLine() throws ValidationException {
         currentLineValues = readNext();
    }

    boolean hasNextDataLine() throws ValidationException {
        loadNextLine();
        return currentLineValues != null;
    }

    String getDataValue(AnnotationFieldDescriptor descriptor) throws ValidationException {
        return getDataValue(getDescriptorToColumnMap().get(descriptor));
    }

    String getDataValue(FileColumn fileColumn) throws ValidationException {
        if (currentLineValues == null) {
            throwValidationException("Caller must first call hasNextDataLine() before retrieving data.");
        }
        return currentLineValues[fileColumn.getPosition()];
    }

    private void resetReader() throws ValidationException {
        try {
            reader = new CSVReader(new FileReader(getFile()));
        } catch (FileNotFoundException e) {
            throwValidationException("Can't reset file: " + getFile().getAbsolutePath());
        }
    }

    private Map<AnnotationFieldDescriptor, FileColumn> getDescriptorToColumnMap() {
        if (descriptorToColumnMap == null) {
            loadDescriptorToColumnMap();
        }
        return descriptorToColumnMap;
    }

    private void loadDescriptorToColumnMap() {
        descriptorToColumnMap = new HashMap<AnnotationFieldDescriptor, FileColumn>();
        for (FileColumn column : columns) {
            if (column.getFieldDescriptor() != null) {
                descriptorToColumnMap.put(column.getFieldDescriptor(), column);
            }
        }
    }

    List<AnnotationFieldDescriptor> getAnnotationTypeDescriptors() {
        List<AnnotationFieldDescriptor> descriptors = new ArrayList<AnnotationFieldDescriptor>();
        for (FileColumn column : columns) {
            if (column.getFieldDescriptor() != null
                && AnnotationFieldType.ANNOTATION.equals(column.getFieldDescriptor().getType())) {
                descriptors.add(column.getFieldDescriptor());
            }
        }
        return descriptors;
    }

    List<AnnotationFieldDescriptor> getDescriptors() {
        List<AnnotationFieldDescriptor> descriptors = new ArrayList<AnnotationFieldDescriptor>();
        for (FileColumn column : columns) {
            if (column.getFieldDescriptor() != null) {
                descriptors.add(column.getFieldDescriptor());
            }
        }
        return descriptors;
    }

    ValidationResult validate() {
        ValidationResult result = new ValidationResult();
        if (getIdentifierColumn() == null) {
            result.setInvalidMessage("The data file does not have an identifier column indicated.");
        }
        return result;
    }

    /**
     * Indicates the identifier column by position.
     *
     * @param index the index of the column (zero-based)
     */
    public void setIdentifierColumnIndex(int index) {
        setIdentifierColumn(getColumns().get(index));
    }

    void unloadAnnotation() {
        currentlyLoadedIdentifier.clear();
        setCurrentlyLoaded(String.valueOf(false));
    }

    void loadAnnontation(AbstractAnnotationHandler handler) throws ValidationException {
        currentlyLoadedIdentifier.clear();
        positionAtData();
        while (hasNextDataLine()) {
            String identifier = getDataValue(getIdentifierColumn());
            if (currentlyLoadedIdentifier.contains(identifier)) {
                throw new ValidationException("Multiples identifiers found for '" + identifier + "'");
            }
            handler.handleIdentifier(identifier);
            currentlyLoadedIdentifier.add(identifier);
            loadAnnotationLine(handler);
        }
        setCurrentlyLoaded(String.valueOf(true));
    }

    private void loadAnnotationLine(AbstractAnnotationHandler handler) throws ValidationException {
        for (AnnotationFieldDescriptor annotationDescriptor : getAnnotationTypeDescriptors()) {
            String value = getDataValue(annotationDescriptor);
            AbstractAnnotationValue annotationValue = AnnotationUtil.createAnnotationValue(annotationDescriptor, value);
            if (getTimepointColumn() != null) {
                String timepointValue = getDataValue(getTimepointColumn());
                handler.handleAnnotationValue(annotationValue, timepointValue);
            } else {
                handler.handleAnnotationValue(annotationValue);
            }
        }
    }


    boolean isLoadable() {
        if (getIdentifierColumn() == null) {
            return false;
        }
        for (FileColumn column : getColumns()) {
            if (!column.isLoadable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the currentlyLoaded
     */
    public String getCurrentlyLoaded() {
        if (currentlyLoaded == null) {
            currentlyLoaded = String.valueOf(false);
        }
        return currentlyLoaded;
    }

    /**
     * @param currentlyLoaded the currentlyLoaded to set
     */
    public void setCurrentlyLoaded(String currentlyLoaded) {
        this.currentlyLoaded = currentlyLoaded;
    }

    /**
     * @return a list of visible annotation definitions
     */
    public List<AnnotationDefinition> getVisibleAnnotationDefinition() {
        List<AnnotationDefinition> visibleList = new ArrayList<AnnotationDefinition>();
        for (FileColumn fileColumn : columns) {
            if (fileColumn.getFieldDescriptor() != null
                    && AnnotationFieldType.ANNOTATION.equals(fileColumn.getFieldDescriptor().getType())
                    && fileColumn.getFieldDescriptor().isShownInBrowse()) {
                visibleList.add(fileColumn.getFieldDescriptor().getDefinition());
            }
        }
        return visibleList;
    }


}
