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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Represents a column in a <code>DelimitedTextFile</code>.
 */
public class FileColumn extends AbstractCaIntegrator2Object implements Comparable<FileColumn> {
    
    private static final long serialVersionUID = 1L;
    private int position;
    private String name;
    private AnnotationFieldDescriptor fieldDescriptor;
    private AnnotationFile annotationFile;
    private transient List<String> dataValues;

    FileColumn(AnnotationFile annotationFile) {
        this.annotationFile = annotationFile;
    }

    /**
     * Creates a new instance.
     */
    public FileColumn() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(FileColumn column) {
        return position - column.position;
    }

    /**
     * @return the fieldDescriptor
     */
    public AnnotationFieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }

    /**
     * @param fieldDescriptor the fieldDescriptor to set
     */
    public void setFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }

    /**
     * @return the annotationFile
     */
    public AnnotationFile getAnnotationFile() {
        return annotationFile;
    }

    /**
     * @param annotationFile the annotationFile to set
     */
    public void setAnnotationFile(AnnotationFile annotationFile) {
        this.annotationFile = annotationFile;
    }
    
    /**
     * Returns the list of values in this column.
     * 
     * @return the values.
     * @throws ValidationException fail to load
     */
    public List<String> getDataValues() throws ValidationException {
        if (dataValues == null) {
            loadDataValues();
        }
        return dataValues;
    }
    
    /**
     * Retrieves the unique data values represented in the format of the class type.
     * @param <T> the format of the class type.
     * @param classType the format of the class type.
     * @return Set of values.
     * @throws ValidationException if file column is invalid.
     */
    @SuppressWarnings("unchecked")
    public <T> Set <T> getUniqueDataValues(Class<T> classType) throws ValidationException {
        Set<String> uniqueStringDataValues = new HashSet<String>();
        uniqueStringDataValues.addAll(getDataValues());
        if (classType.equals(String.class)) {
            return (Set<T>) uniqueStringDataValues;
        }
        Set<T> uniqueDataValues = new HashSet<T>();
        for (String stringValue : uniqueStringDataValues) {
            uniqueDataValues.add((T) retrieveValueAsClassType(classType, stringValue));
        }
        return uniqueDataValues;
       
    }

    private <T> Object retrieveValueAsClassType(Class<T> classType, String stringValue)
            throws ValidationException {
        if (classType.equals(Double.class)) {
            if (!NumberUtils.isNumber(stringValue)) {
                throw new ValidationException("Cannot cast column as a number, because of value: " + stringValue);
            }
            return Double.valueOf(stringValue);
        } else if (classType.equals(Date.class)) {
            try {
                return DateUtil.createDate(stringValue);
            } catch (ParseException e) {
                throw new ValidationException("Unable to parse date from the column, because of value: " 
                        + stringValue, e);
            }
        } else {
            throw new IllegalArgumentException("classType is not valid.");
        }
    }

    private void loadDataValues() throws ValidationException {
        dataValues = new ArrayList<String>();
        getAnnotationFile().positionAtData();
        while (getAnnotationFile().hasNextDataLine()) {
            dataValues.add(getAnnotationFile().getDataValue(this));
        }
    }

    /**
     * @return true if this is the identifier column in the file.
     */
    public boolean isIdentifierColumn() {
        return getAnnotationFile() != null && this.equals(annotationFile.getIdentifierColumn());
    }

    /**
     * @return true if this is the timepoint column in the file.
     */
    public boolean isTimepointColumn() {
        return getAnnotationFile() != null && this.equals(annotationFile.getTimepointColumn());
    }

    /**
     * Sets this column up as an annotation column.
     */
    public void makeAnnotationColumn() {
        if (isIdentifierColumn()) {
            getAnnotationFile().setIdentifierColumn(null);
        } else if (isTimepointColumn()) {
            getAnnotationFile().setTimepointColumn(null);
        }
        if (getFieldDescriptor() == null) {
            setFieldDescriptor(new AnnotationFieldDescriptor());
        }
    }

    boolean isLoadable() {
        return isIdentifierColumn() || isTimepointColumn() 
        || (getFieldDescriptor() != null && getFieldDescriptor().getDefinition() != null);
    }
    
    /**
     * Checks to see if this would be a valid identifier column.
     * @throws ValidationException if invalid column for identifier.
     */
    public void checkValidIdentifierColumn() throws ValidationException {
        Set<String> currentValues = new HashSet<String>();
        for (String value : getDataValues()) {
            if (currentValues.contains(value)) {
                throw new ValidationException("This column cannot be an identifier column because it "
                        + "has a duplicate value for '" + value + "'.");
            }
            currentValues.add(value);
        }
    }

    void createFieldDescriptor(CaIntegrator2Dao dao) {
        setFieldDescriptor(new AnnotationFieldDescriptor());
        getFieldDescriptor().setName(getName());
        getFieldDescriptor().setDefinition(dao.getAnnotationDefinition(getName()));
    }

}
