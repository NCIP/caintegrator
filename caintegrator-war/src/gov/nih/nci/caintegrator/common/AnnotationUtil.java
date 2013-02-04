/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.common;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.application.study.ValidationResult;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * This is a static utility class used by the UI to display the annotation value.
 */
public final class AnnotationUtil {

    private AnnotationUtil() {

    }

    /**
     * @param abstractAnnotationValues abstractAnnotationValues
     * @param dataValues the values from the current upload file
     * @param filterList the list of display string to filter
     * @return set of distinct available values
     */
    public static Set<String> getAdditionalValue(Collection<AbstractAnnotationValue> abstractAnnotationValues,
            List<String> dataValues, Set<String> filterList) {
        Set<String> results = new HashSet<String>();
        for (String dataValue : dataValues) {
            if (!StringUtils.isBlank(dataValue) && !filterList.contains(dataValue)) {
                if (NumberUtils.isNumber(dataValue)) {
                    dataValue = NumericUtil.formatDisplay(dataValue);
                }
                results.add(dataValue);
            }
        }
        for (AbstractAnnotationValue abstractAnnotationValue : abstractAnnotationValues) {
            String displayString = abstractAnnotationValue.toString();
            if (StringUtils.isNotBlank(displayString) && !filterList.contains(displayString)) {
                results.add(displayString);
            }
        }
        return results;
    }

    /**
     * Creates an annotation value for the given value / field descriptor.
     * @param annotationDescriptor to create value for.
     * @param value string value.
     * @return annotation value.
     * @throws ValidationException for validation errors.
     */
    public static AbstractAnnotationValue createAnnotationValue(AnnotationFieldDescriptor annotationDescriptor,
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

    private static void throwValidationException(String message) throws ValidationException {
        ValidationResult result = new ValidationResult();
        result.setInvalidMessage(message);
        throw new ValidationException(result);
    }

   private static StringAnnotationValue createStringAnnotationValue(AnnotationFieldDescriptor annotationDescriptor,
           String value) {
       StringAnnotationValue annotationValue = new StringAnnotationValue();
       annotationValue.setStringValue(value);
       annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
       annotationDescriptor.getDefinition().getAnnotationValueCollection().add(annotationValue);
       return annotationValue;
   }

   private static DateAnnotationValue createDateAnnotationValue(AnnotationFieldDescriptor annotationDescriptor,
           String value) throws ValidationException {
       DateAnnotationValue annotationValue = new DateAnnotationValue();
       try {
           annotationValue.setDateValue(DateUtil.createDate(value));
       } catch (ParseException e) {
           throwValidationException(createFormatErrorMsg(annotationDescriptor, value,
                   "The two formats allowed are MM-dd-yyyy and MM/dd/yyyy"));
       }
       annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
       annotationDescriptor.getDefinition().getAnnotationValueCollection().add(annotationValue);
       return annotationValue;
   }

   private static NumericAnnotationValue createNumericAnnotationValue(AnnotationFieldDescriptor annotationDescriptor,
           String value) throws ValidationException {
       NumericAnnotationValue annotationValue = new NumericAnnotationValue();
       try {
           annotationValue.setNumericValue(getNumericValue(value));
       } catch (NumberFormatException e) {
           throwValidationException(createFormatErrorMsg(annotationDescriptor, value, null));
       }
       annotationValue.setAnnotationDefinition(annotationDescriptor.getDefinition());
       annotationDescriptor.getDefinition().getAnnotationValueCollection().add(annotationValue);
       return annotationValue;
   }

   private static Double getNumericValue(String value) {
       if (StringUtils.isBlank(value)) {
           return null;
       } else {
           return Double.parseDouble(value);
       }
   }

    private static String createFormatErrorMsg(AnnotationFieldDescriptor descriptor, String value,
            String allowedFormats) {
       return "Invalid format for data type '" + descriptor.getDefinition().getDataType()
           + "' on field '" + descriptor.getName()
           + "' of descriptor '" + descriptor.getDefinition().getDisplayName()
           + "' with value = '" + value + "'. " + allowedFormats;
   }

    /**
     * Retrieves existing field descriptor, and if that doesn't exist creates a new one.
     * @param dao database access object.
     * @param studyConfiguration study.
     * @param type entity type.
     * @param createNewAnnotationDefinition determines whether to create an annotation definition.
     * @param annotationFieldDescriptorName name of the afd.
     * @param annotationGroupName name of the group (optional, if null or blank will use default study group).
     * @return annotation field descriptor.
     * @throws ValidationException if invalid afd.
     */
    public static AnnotationFieldDescriptor retrieveOrCreateFieldDescriptor(CaIntegrator2Dao dao,
            StudyConfiguration studyConfiguration, EntityTypeEnum type, boolean createNewAnnotationDefinition,
            String annotationFieldDescriptorName, String annotationGroupName)
        throws ValidationException {
        AnnotationFieldDescriptor fieldDescriptor = null;
        if (studyConfiguration != null) {
            fieldDescriptor = studyConfiguration.getExistingFieldDescriptorInStudy(annotationFieldDescriptorName);
            validateFieldDescriptorEntityType(fieldDescriptor, type);
        }
        if (fieldDescriptor == null) {
            fieldDescriptor = createNewAnnotationFieldDescriptor(dao, studyConfiguration, type,
                    createNewAnnotationDefinition,
                    annotationFieldDescriptorName, annotationGroupName);
        } else if (createNewAnnotationDefinition
                    && AnnotationFieldType.ANNOTATION.equals(fieldDescriptor.getType())
                    && fieldDescriptor.getDefinition() == null) {
            createNewAnnotationDefinition(dao, fieldDescriptor);
        }
        return fieldDescriptor;
    }

    private static AnnotationFieldDescriptor createNewAnnotationFieldDescriptor(CaIntegrator2Dao dao,
            StudyConfiguration studyConfiguration,
            EntityTypeEnum type, boolean createNewAnnotationDefinition,
            String annotationFieldDescriptorName,
            String annotationGroupName) {
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setName(annotationFieldDescriptorName);
        fieldDescriptor.setType(AnnotationFieldType.ANNOTATION);
        fieldDescriptor.setAnnotationEntityType(type);
        if (createNewAnnotationDefinition) {
            createNewAnnotationDefinition(dao, fieldDescriptor);
        }
        AnnotationGroup group = studyConfiguration.getStudy().getOrCreateAnnotationGroup(
                StringUtils.isBlank(annotationGroupName) ? Study.DEFAULT_ANNOTATION_GROUP
                        : annotationGroupName);
        fieldDescriptor.setAnnotationGroup(group);
        group.getAnnotationFieldDescriptors().add(fieldDescriptor);
        return fieldDescriptor;
    }

    private static void createNewAnnotationDefinition(CaIntegrator2Dao dao,
            AnnotationFieldDescriptor fieldDescriptor) {
        AnnotationDefinition annotationDefinition = dao.getAnnotationDefinition(fieldDescriptor.getName(),
                AnnotationTypeEnum.STRING);
        if (annotationDefinition == null) {
            annotationDefinition = new AnnotationDefinition();
            annotationDefinition.setDefault(fieldDescriptor.getName());
        }
        fieldDescriptor.setDefinition(annotationDefinition);
    }


    private static void validateFieldDescriptorEntityType(AnnotationFieldDescriptor fieldDescriptor,
            EntityTypeEnum entityType) throws ValidationException {
        if (fieldDescriptor != null && !entityType.equals(fieldDescriptor.getAnnotationEntityType())) {
            throw new ValidationException(
                    "Found a currently existing field descriptor with the same name '"
                        + fieldDescriptor.getName() + "' in this study of type '"
                        + fieldDescriptor.getAnnotationEntityType() + "' which doesn't match type "
                        + entityType);
        }
    }

}
