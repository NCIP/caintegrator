/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Represents a CSV annotation text file.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // switch statement and argument checking
public class AnnotationFile extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private String path;
    private List<FileColumn> columns = new ArrayList<FileColumn>();
    private FileColumn identifierColumn;
    private FileColumn timepointColumn;
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
    
    static AnnotationFile load(File file, CaIntegrator2Dao dao) throws ValidationException {
        AnnotationFile annotationFile = new AnnotationFile(file);
        annotationFile.validateFileFormat();
        annotationFile.loadColumns(dao);
        return annotationFile;
    }

    private void loadColumns(CaIntegrator2Dao dao) throws ValidationException {
        resetReader();
        loadNextLine();
        for (int index = 0; index < currentLineValues.length; index++) {
            FileColumn column = new FileColumn(this);
            column.setPosition(index);
            column.setName(currentLineValues[index].trim());
            column.createFieldDescriptor(dao);
            columns.add(column);
        }
    }

    private void validateFileFormat() throws ValidationException {
        validateFileExists();
        validateFileNotEmpty();
        validateFileHasData();
        validateDataLinesConsistent();
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
        return identifierColumn;
    }

    /**
     * @param identifierColumn the identifierColumn to set
     */
    public void setIdentifierColumn(FileColumn identifierColumn) {
        this.identifierColumn = identifierColumn;
        if (identifierColumn != null) {
            identifierColumn.setFieldDescriptor(null);
            if (identifierColumn.equals(getTimepointColumn())) {
                setTimepointColumn(null);
            }
        }
    }
    

    /**
     * @return the timepointColumn
     */
    public FileColumn getTimepointColumn() {
        return timepointColumn;
    }

    /**
     * @param timepointColumn the timepointColumn to set
     */
    public void setTimepointColumn(FileColumn timepointColumn) {
        this.timepointColumn = timepointColumn;
        if (timepointColumn != null) {
            timepointColumn.setFieldDescriptor(null);
            if (timepointColumn.equals(getIdentifierColumn())) {
                setIdentifierColumn(null);
            }
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

    void loadDescriptors(Collection<AnnotationFieldDescriptor> existingDesciptors) {
        for (FileColumn column : columns) {
            loadDescriptor(column, existingDesciptors);
        }
    }

    private void loadDescriptor(FileColumn column, Collection<AnnotationFieldDescriptor> existingDesciptors) {
        if (!isAnnotationColumn(column)) {
            return;
        }
        for (AnnotationFieldDescriptor descriptor : existingDesciptors) {
            if (descriptor.getName().equals(column.getName())) {
                column.setFieldDescriptor(descriptor);
                return;
            }
        }
    }

    private boolean isAnnotationColumn(FileColumn column) {
        return !(column.equals(getIdentifierColumn()) || column.equals(getTimepointColumn()));
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

    void loadAnnontation(AbstractAnnotationHandler handler) throws ValidationException {
        currentlyLoadedIdentifier.clear();
        handler.addDefinitionsToStudy(getAnnotationDefinitions());
        positionAtData();
        while (hasNextDataLine()) {
            String identifier = getDataValue(getIdentifierColumn());
            if (currentlyLoadedIdentifier.contains(identifier)) {
                throw new ValidationException("Multiples identifiers found for '" + identifier + "'");
            }
            currentlyLoadedIdentifier.add(identifier);
            handler.handleIdentifier(identifier);
            loadAnnotationLine(handler);
        }
        setCurrentlyLoaded(String.valueOf(true));
    }

    private Set<AnnotationDefinition> getAnnotationDefinitions() {
        Set<AnnotationDefinition> definitions = new HashSet<AnnotationDefinition>();
        for (AnnotationFieldDescriptor descriptor : getDescriptors()) {
            if (descriptor.getDefinition() != null) {
                definitions.add(descriptor.getDefinition());
            }
        }
        return definitions;
    }

    private void loadAnnotationLine(AbstractAnnotationHandler handler) throws ValidationException {
        for (AnnotationFieldDescriptor annotationDescriptor : getDescriptors()) {
            String value = getDataValue(annotationDescriptor);
            AbstractAnnotationValue annotationValue = createAnnotationValue(annotationDescriptor, value);
            if (getTimepointColumn() != null) {
                String timepointValue = getDataValue(getTimepointColumn());
                handler.handleAnnotationValue(annotationValue, timepointValue);
            } else {
                handler.handleAnnotationValue(annotationValue);
            }
        }
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // switch statement and argument checking
     private AbstractAnnotationValue createAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) throws ValidationException {
        if (annotationDescriptor.getDefinition() == null 
                || annotationDescriptor.getDefinition().getDataType() == null) {
            throwValidationException("Type for field " + annotationDescriptor.getName() + " was not set.");
        }
        AnnotationTypeEnum type = annotationDescriptor.getDefinition().getDataType();
        switch (type) {
        case DATE:
            return createDateAnnotationValue(annotationDescriptor, value);
        case STRING:
            return createStringAnnotationValue(annotationDescriptor, value);
        case NUMERIC:
            return createNumericAnnotationValue(annotationDescriptor, value);
        default:
            throwValidationException("Unknown AnnotationDefinitionType: " + type);
            return null;
        }
    }

    private StringAnnotationValue createStringAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) {
        StringAnnotationValue annotationValue = new StringAnnotationValue();
        annotationValue.setStringValue(value);
        annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
        return annotationValue;
    }

    private DateAnnotationValue createDateAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) throws ValidationException {
        DateAnnotationValue annotationValue = new DateAnnotationValue();
        try {
            annotationValue.setDateValue(getDateValue(value));
        } catch (ParseException e) {
            throwValidationException(createFormatErrorMsg(annotationDescriptor, value, 
                    "The two formats allowed are MM-dd-yyyy and MM/dd/yyyy"));
        }
        annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
        return annotationValue;
    }
    
    private Date getDateValue(String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return DateUtil.createDate(value);
        }
    }

    private NumericAnnotationValue createNumericAnnotationValue(AnnotationFieldDescriptor annotationDescriptor, 
            String value) throws ValidationException {
        NumericAnnotationValue annotationValue = new NumericAnnotationValue();
        try {
            annotationValue.setNumericValue(getNumericValue(value));
        } catch (NumberFormatException e) {
            throwValidationException(createFormatErrorMsg(annotationDescriptor, value, null));
        }
        annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
        return annotationValue;
    }

    private Double getNumericValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        } else {
            return Double.parseDouble(value);
        }
    }

    private String createFormatErrorMsg(AnnotationFieldDescriptor descriptor, String value, String allowedFormats) {
        return "Invalid format for data type '" + descriptor.getDefinition().getDataType()
            + "' on field '" + descriptor.getName()
            + "' of descriptor '" + descriptor.getDefinition().getDisplayName()
            + "' with value = '" + value + "'. " + allowedFormats;
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
                    && fileColumn.getFieldDescriptor().isShownInBrowse()) {
                visibleList.add(fileColumn.getFieldDescriptor().getDefinition());
            }
        }
        return visibleList;
    }


}
