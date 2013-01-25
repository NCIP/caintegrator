/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Holds information for a single clinical criterion.
 */
public class AnnotationCriterionRow extends AbstractCriterionRow {

    private AbstractCriterionWrapper annotationCriterionWrapper;
    private final String annotationGroupName;

    AnnotationCriterionRow(CriteriaGroup group, String annotationGroupName) {
        super(group);
        this.annotationGroupName = annotationGroupName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName() {
        if (getAnnotationCriterionWrapper() == null) {
            return "";
        } else {
            return getAnnotationCriterionWrapper().getFieldName();
        }
    }

    @Override
    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            setAnnotationCriterionWrapper(null);
        } else if (getAnnotationCriterionWrapper() == null) {
            createAnnotationCriterionWrapper(fieldName);
        } else {
            AnnotationFieldDescriptor field = getDescriptor(fieldName);
            CriterionTypeEnum criterionType = getCriterionType(field.getDefinition());
            if (criterionType.equals(getAnnotationCriterionWrapper().getCriterionType())) {
                getAnnotationCriterionWrapper().setField(field);
            } else {
                createAnnotationCriterionWrapper(fieldName);
            }
        }
    }

    private AnnotationFieldDescriptor getDescriptor(String fieldName) {
        return getAnnotationFieldDescriptorList().getDefinition(fieldName);
    }

    private CriterionTypeEnum getCriterionType(AnnotationDefinition field) {
        if (!field.getPermissibleValueCollection().isEmpty()) {
            return CriterionTypeEnum.SELECTED_VALUE;
        } else if (AnnotationTypeEnum.STRING.equals(field.getDataType())) {
            return CriterionTypeEnum.STRING_COMPARISON;
        } else if (AnnotationTypeEnum.NUMERIC.equals(field.getDataType())) {
            return CriterionTypeEnum.NUMERIC_COMPARISON;
        } else if (AnnotationTypeEnum.DATE.equals(field.getDataType())) {
            return CriterionTypeEnum.DATE_COMPARISON;
        } else {
            throw new IllegalArgumentException("Unsupported type " + field.getDataType());
        }
    }

    void createAnnotationCriterionWrapper(String fieldName) {
        AnnotationFieldDescriptor field = getDescriptor(fieldName);
        CriterionTypeEnum type = getCriterionType(field.getDefinition());
        switch (type) {
        case NUMERIC_COMPARISON:
            setAnnotationCriterionWrapper(createNumericCriterionWrapper(field));
            break;
        case STRING_COMPARISON:
            setAnnotationCriterionWrapper(createStringComparisonCriterionWrapper(field));
            break;
        case DATE_COMPARISON:
            setAnnotationCriterionWrapper(createDateCriterionWrapper(field));
            break;
        case SELECTED_VALUE:
            setAnnotationCriterionWrapper(createSelectedValueCriterionWrapper(field));
            break;
        default:
            throw new IllegalStateException("Unsupported AnnotationType " + type);
        }
    }

    private SelectedValueCriterionWrapper createSelectedValueCriterionWrapper(AnnotationFieldDescriptor field) {
        SelectedValueCriterion criterion = new SelectedValueCriterion();
        criterion.setAnnotationFieldDescriptor(field);
        criterion.setEntityType(field.getAnnotationEntityType());
        criterion.setValueCollection(new HashSet<PermissibleValue>());
        return new SelectedValueCriterionWrapper(criterion, this);
    }

    private StringComparisonCriterionWrapper createStringComparisonCriterionWrapper(AnnotationFieldDescriptor field) {
        StringComparisonCriterion criterion = new StringComparisonCriterion();
        criterion.setAnnotationFieldDescriptor(field);
        criterion.setEntityType(field.getAnnotationEntityType());
        return new StringComparisonCriterionWrapper(criterion, this);
    }

    private NumericComparisonCriterionWrapper createNumericCriterionWrapper(AnnotationFieldDescriptor field) {
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setAnnotationFieldDescriptor(field);
        criterion.setEntityType(field.getAnnotationEntityType());
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
        return new NumericComparisonCriterionWrapper(criterion, this);
    }

    private DateComparisonCriterionWrapper createDateCriterionWrapper(AnnotationFieldDescriptor field) {
        DateComparisonCriterion criterion = new DateComparisonCriterion();
        criterion.setAnnotationFieldDescriptor(field);
        criterion.setEntityType(field.getAnnotationEntityType());
        criterion.setDateComparisonOperator(DateComparisonOperatorEnum.EQUAL);
        return new DateComparisonCriterionWrapper(criterion, this);
    }

    AnnotationFieldDescriptorList getAnnotationFieldDescriptorList() {
        return getGroup().getForm().getAnnotations(annotationGroupName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return getAnnotationCriterionWrapper();
    }

    private AbstractCriterionWrapper getAnnotationCriterionWrapper() {
        return annotationCriterionWrapper;
    }

    private void setAnnotationCriterionWrapper(AbstractCriterionWrapper annotationCriterionWrapper) {
        if (this.annotationCriterionWrapper != null) {
            removeCriterionFromQuery();
        }
        this.annotationCriterionWrapper = annotationCriterionWrapper;
        if (annotationCriterionWrapper != null) {
            addCriterionToQuery();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        List<String> availableFieldNames = new ArrayList<String>();
        availableFieldNames.addAll(getAnnotationFieldDescriptorList().getNames());
        return availableFieldNames;
    }

    @Override
    void setCriterion(AbstractCriterion criterion) {
        this.annotationCriterionWrapper = CriterionWrapperBuilder.createAnnotationCriterionWrapper(criterion, this,
                getSubscription());
    };

    private StudySubscription getSubscription() {
        return getGroup().getSubscription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return annotationGroupName;
    }

}
