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
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.xwork.StringUtils;

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
        if (StringUtils.isBlank(stringValue)) {
            return null;
        }
        if (classType.equals(Double.class)) {
            return handleNumericType(stringValue);
        } else if (classType.equals(Date.class)) {
            return handleDateType(stringValue);
        } else {
            throw new IllegalArgumentException("classType is not valid.");
        }
    }

    private Object handleNumericType(String stringValue) throws ValidationException {
        if (!NumberUtils.isNumber(stringValue)) {
            throw new ValidationException("Cannot cast column '" + name + "' as a number, because of value: "
                    + stringValue);
        }
        return Double.valueOf(stringValue);
    }
    
    private Object handleDateType(String stringValue) throws ValidationException {
        try {
            return DateUtil.createDate(stringValue);
        } catch (ParseException e) {
            throw new ValidationException("Unable to parse date from the column'" + name 
                    + "', because of value: " + stringValue, e);
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
        return getFieldDescriptor() != null && AnnotationFieldType.IDENTIFIER.equals(fieldDescriptor.getType());
    }

    /**
     * @return true if this is the timepoint column in the file.
     */
    public boolean isTimepointColumn() {
        return getFieldDescriptor() != null && AnnotationFieldType.TIMEPOINT.equals(fieldDescriptor.getType());
    }

    /**
     * If field descriptor doesn't exist, creates a new one, and then sets it to the given type.
     * @param type the field type to set this column as.
     */
    public void setupAnnotationFieldDescriptor(AnnotationFieldType type) {
        if (getFieldDescriptor() == null) {
            setFieldDescriptor(new AnnotationFieldDescriptor());
        }
        fieldDescriptor.setType(type);
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

    void retrieveOrCreateFieldDescriptor(CaIntegrator2Dao dao,
            StudyConfiguration studyConfiguration, EntityTypeEnum type, boolean createNewAnnotationDefinition) 
        throws ValidationException {
        if (studyConfiguration != null) {
            fieldDescriptor = studyConfiguration.getExistingFieldDescriptorInStudy(getName());
            validateFieldDescriptorEntityType(type);
        }
        if (fieldDescriptor == null) {
            createNewAnnotationFieldDescriptor(dao, studyConfiguration, type, createNewAnnotationDefinition);
        } else if (createNewAnnotationDefinition && fieldDescriptor.getDefinition() == null) {
            createNewAnnotationDefinition(dao);
        }
    }

    private void createNewAnnotationFieldDescriptor(CaIntegrator2Dao dao, StudyConfiguration studyConfiguration,
            EntityTypeEnum type, boolean createNewAnnotationDefinition) {
        fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setName(getName());
        fieldDescriptor.setType(AnnotationFieldType.ANNOTATION);
        fieldDescriptor.setAnnotationEntityType(type);
        if (createNewAnnotationDefinition) {
            createNewAnnotationDefinition(dao);
        }
        AnnotationGroup defaultGroup = studyConfiguration.getStudy().getOrCreateDefaultAnnotationGroup();
        fieldDescriptor.setAnnotationGroup(defaultGroup);
        defaultGroup.getAnnotationFieldDescriptors().add(fieldDescriptor);
    }

    private void createNewAnnotationDefinition(CaIntegrator2Dao dao) {
        AnnotationDefinition annotationDefinition = dao.getAnnotationDefinition(getName(),
                AnnotationTypeEnum.STRING);
        if (annotationDefinition == null) {
            annotationDefinition = new AnnotationDefinition();
            annotationDefinition.setDefault(fieldDescriptor.getName());
        }
        fieldDescriptor.setDefinition(annotationDefinition);
    }


    private void validateFieldDescriptorEntityType(EntityTypeEnum entityType) throws ValidationException {
        if (fieldDescriptor != null && !entityType.equals(fieldDescriptor.getAnnotationEntityType())) {
            throw new ValidationException(
                    "Found a currently existing field descriptor with the same name '"
                        + fieldDescriptor.getName() + "' in this study of type '"
                        + fieldDescriptor.getAnnotationEntityType() + "' which doesn't match type "
                        + entityType);
        }
    }

}
