/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * 
 */
public class DisplayableAnnotationFieldDescriptor implements Comparable<DisplayableAnnotationFieldDescriptor> {
    
    private AnnotationFieldDescriptor fieldDescriptor;
    private String annotationGroupName;
    private boolean identifierType = false;
    private boolean timepointType = false;
    private List<String> dataValues = new ArrayList<String>();
    private String originalGroupName;
    
    /**
     * Default constructor.
     */
    public DisplayableAnnotationFieldDescriptor() { 
        // Empty Constructor
    }
    
    /**
     * Constructor for fileColumn.
     * @param fileColumn used to construct this object.
     */
    public DisplayableAnnotationFieldDescriptor(FileColumn fileColumn) {
        initialize(fileColumn.getFieldDescriptor());
        try {
            dataValues = fileColumn.getDataValues();
        } catch (ValidationException e) {
            dataValues.clear();
        }
    }
    
    /**
     * Constructor for annotationFieldDescriptor.
     * @param annotationFieldDescriptor used to construct this object.
     */
    public DisplayableAnnotationFieldDescriptor(AnnotationFieldDescriptor annotationFieldDescriptor) {
        initialize(annotationFieldDescriptor);
    }

    private void initialize(AnnotationFieldDescriptor newFieldDescriptor) {
        this.fieldDescriptor = newFieldDescriptor;
        if (fieldDescriptor != null) { // Currently identifiers don't have field descriptors... that will change.
            annotationGroupName = fieldDescriptor.getAnnotationGroup() == null ? "" : fieldDescriptor
                    .getAnnotationGroup().getName();
            originalGroupName = annotationGroupName;
            identifierType = AnnotationFieldType.IDENTIFIER.equals(fieldDescriptor.getType());
            timepointType = AnnotationFieldType.TIMEPOINT.equals(fieldDescriptor.getType());
        }
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
     * @return the annotationGroupName
     */
    public String getAnnotationGroupName() {
        return annotationGroupName;
    }
    /**
     * @param annotationGroupName the annotationGroupName to set
     */
    public void setAnnotationGroupName(String annotationGroupName) {
        this.annotationGroupName = annotationGroupName;
    }

    /**
     * @return the identifierType
     */
    public boolean isIdentifierType() {
        return identifierType;
    }

    /**
     * @param identifierType the identifierType to set
     */
    public void setIdentifierType(boolean identifierType) {
        this.identifierType = identifierType;
    }

    /**
     * @return the timepointType
     */
    public boolean isTimepointType() {
        return timepointType;
    }

    /**
     * @param timepointType the timepointType to set
     */
    public void setTimepointType(boolean timepointType) {
        this.timepointType = timepointType;
    }


    /**
     * @return the dataValues
     */
    public List<String> getDataValues() {
        return dataValues;
    }

    /**
     * @param dataValues the dataValues to set
     */
    public void setDataValues(List<String> dataValues) {
        this.dataValues = dataValues;
    }
    
    /**
     * Determines if the group value has changed.
     * @return T/F value.
     */
    public boolean isGroupChanged() {
        return !StringUtils.equals(originalGroupName, annotationGroupName);
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(DisplayableAnnotationFieldDescriptor o) {
        if (isIdentifierType() && !o.identifierType) {
            return -1;
        } else if (!isIdentifierType() && o.identifierType) {
            return 1;
        }
        return fieldDescriptor.getName().compareTo(o.getFieldDescriptor().getName());
    }

}
