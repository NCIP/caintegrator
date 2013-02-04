/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Holds information for a single clinical criterion.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // see createNewCriterion
public abstract class AbstractAnnotationCriterionRow extends AbstractCriterionRow {
    
    private AbstractAnnotationCriterionWrapper annotationCriterionWrapper;

    AbstractAnnotationCriterionRow(CriteriaGroup group) {
        super(group);
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

    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            setAnnotationCriterionWrapper(null);
        } else if (getAnnotationCriterionWrapper() == null) {
            createAnnotationCriterionWrapper(fieldName);
        } else {
            AnnotationDefinition field = getAnnotationDefinitionList().getDefinition(fieldName);
            CriterionTypeEnum criterionType = getCriterionType(field);
            if (criterionType.equals(getAnnotationCriterionWrapper().getCriterionType())) {
                getAnnotationCriterionWrapper().setField(field);
            } else {
                createAnnotationCriterionWrapper(fieldName);
            }
        }
    }

    private CriterionTypeEnum getCriterionType(AnnotationDefinition field) {
        if (field == null) { // Assumption that a null field is an Identifier.
            return CriterionTypeEnum.IDENTIFIER;
        }
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
        AnnotationDefinition field = getAnnotationDefinitionList().getDefinition(fieldName);
        CriterionTypeEnum type = getCriterionType(field);
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
        case IDENTIFIER:
            setAnnotationCriterionWrapper(createIdentifierCriterionWrapper());
            break;
        default:
            throw new IllegalStateException("Unsupported AnnotationType " + type);
        }
    }
    
    private IdentifierCriterionWrapper createIdentifierCriterionWrapper() {
        IdentifierCriterion criterion = new IdentifierCriterion();
        criterion.setEntityType(getEntityType());
        return new IdentifierCriterionWrapper(criterion, this);
    }

    private SelectedValueCriterionWrapper createSelectedValueCriterionWrapper(AnnotationDefinition field) {
        SelectedValueCriterion criterion = new SelectedValueCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        criterion.setValueCollection(new HashSet<PermissibleValue>());
        return new SelectedValueCriterionWrapper(criterion, this);
    }

    private StringComparisonCriterionWrapper createStringComparisonCriterionWrapper(AnnotationDefinition field) {
        StringComparisonCriterion criterion = new StringComparisonCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        return new StringComparisonCriterionWrapper(criterion, this);
    }

    private NumericComparisonCriterionWrapper createNumericCriterionWrapper(AnnotationDefinition field) {
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
        return new NumericComparisonCriterionWrapper(criterion, this);
    }

    private DateComparisonCriterionWrapper createDateCriterionWrapper(AnnotationDefinition field) {
        DateComparisonCriterion criterion = new DateComparisonCriterion();
        criterion.setAnnotationDefinition(field);
        criterion.setEntityType(getEntityType());
        criterion.setDateComparisonOperator(DateComparisonOperatorEnum.EQUAL);
        return new DateComparisonCriterionWrapper(criterion, this);
    }

    abstract AnnotationDefinitionList getAnnotationDefinitionList();

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return getAnnotationCriterionWrapper();
    }

    private AbstractAnnotationCriterionWrapper getAnnotationCriterionWrapper() {
        return annotationCriterionWrapper;
    }

    private void setAnnotationCriterionWrapper(AbstractAnnotationCriterionWrapper annotationCriterionWrapper) {
        if (this.annotationCriterionWrapper != null) {
            removeCriterionFromQuery();
        }
        this.annotationCriterionWrapper = annotationCriterionWrapper;
        if (annotationCriterionWrapper != null) {
            addCriterionToQuery();
        }
    }

    /**
     * @return
     */
    abstract EntityTypeEnum getEntityType();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        return getAnnotationDefinitionList().getNames();
    }

    @Override
    void setCriterion(AbstractCriterion criterion) {
        this.annotationCriterionWrapper = createCriterionWrapper(criterion);
    };
    
    private AbstractAnnotationCriterionWrapper createCriterionWrapper(AbstractCriterion criterion) {
        if (criterion instanceof StringComparisonCriterion) {
            StringComparisonCriterion stringComparisonCriterion = (StringComparisonCriterion) criterion;
            return new StringComparisonCriterionWrapper(stringComparisonCriterion, this);
        } else if (criterion instanceof NumericComparisonCriterion) {
            NumericComparisonCriterion numericComparisonCriterion = (NumericComparisonCriterion) criterion;
            return new NumericComparisonCriterionWrapper(numericComparisonCriterion, this);
        } else if (criterion instanceof DateComparisonCriterion) {
            DateComparisonCriterion dateComparisonCriterion = (DateComparisonCriterion) criterion;
            return new DateComparisonCriterionWrapper(dateComparisonCriterion, this);
        } else if (criterion instanceof SelectedValueCriterion) {
            SelectedValueCriterion selectedValueCriterion = (SelectedValueCriterion) criterion;
            return new SelectedValueCriterionWrapper(selectedValueCriterion, this);
        } else {
            throw new IllegalArgumentException("Illegal criterion type " + criterion.getClass());
        }
    }

}
