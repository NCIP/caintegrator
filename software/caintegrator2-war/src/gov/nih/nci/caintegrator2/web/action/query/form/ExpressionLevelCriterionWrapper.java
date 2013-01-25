/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.RangeTypeEnum;

import org.apache.commons.lang.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>ExpressionLevelCriterion</code>.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // anonymous inner class
class ExpressionLevelCriterionWrapper extends AbstractGenomicCriterionWrapper {
    private static final int NUMBER_OF_MAX_PARAMETERS = 4;
    private static final int NUMBER_OF_MAX_PARAMETERS_MULTIPLE_PLATFORMS = NUMBER_OF_MAX_PARAMETERS + 1;
    private static final Float DEFAULT_VALUE = 100f;
    private static final String RANGE_TYPE_LABEL = "Range Type";
    static final String EXPRESSION_LEVEL = "Expression Level";

    private final ExpressionLevelCriterion criterion;

    ExpressionLevelCriterionWrapper(GeneExpressionCriterionRow row) {
        this(new ExpressionLevelCriterion(), row);
    }

    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")  // bogus error; mistakenly thinks isValid is called
    ExpressionLevelCriterionWrapper(ExpressionLevelCriterion criterion, GeneExpressionCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (criterion.getRangeType() == null) {
            criterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
            setCriterionDefaults();
        }
        setupDefaultGenomicParameters(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        getParameters().add(createRangeTypeParameter());
        addRangeParameters();
    }

    private void setUpRangeParameters() {
        setCriterionDefaults();
        removeExistingRangeParameters();
        addRangeParameters();
    }

    private void addRangeParameters() {
        switch (criterion.getRangeType()) {
            case GREATER_OR_EQUAL:
                getParameters().add(createGreaterOrEqualParameter());
                break;
            case LESS_OR_EQUAL:
                getParameters().add(createLessOrEqualParameter());
                break;
            case INSIDE_RANGE:
            case OUTSIDE_RANGE:
                getParameters().add(createGreaterOrEqualParameter());
                getParameters().add(createLessOrEqualParameter());
                break;
            default:
                break;
        }
    }

    private void removeExistingRangeParameters() {
        if (getParameters().size() == getNumberMaxParameters()) {
            getParameters().remove(getNumberMaxParameters() - 1);
        }
        if (getParameters().size() == getNumberMaxParameters() - 1) {
            getParameters().remove(getNumberMaxParameters() - 2);
        }
    }
    
    private int getNumberMaxParameters() {
        return isStudyHasMultipleGeneExpressionPlatforms() 
                ? NUMBER_OF_MAX_PARAMETERS_MULTIPLE_PLATFORMS : NUMBER_OF_MAX_PARAMETERS;
    }


    private void setCriterionDefaults() {
        switch (criterion.getRangeType()) {
            case GREATER_OR_EQUAL:
                criterion.setLowerLimit(DEFAULT_VALUE);
                break;
            case LESS_OR_EQUAL:
                criterion.setUpperLimit(DEFAULT_VALUE);
                break;
            case INSIDE_RANGE:
                criterion.setLowerLimit(DEFAULT_VALUE);
                criterion.setUpperLimit(DEFAULT_VALUE);
                break;
            case OUTSIDE_RANGE:
                criterion.setLowerLimit(DEFAULT_VALUE);
                criterion.setUpperLimit(DEFAULT_VALUE);
                break;
            default:
                break;
        }
    }

    private SelectListParameter<RangeTypeEnum> createRangeTypeParameter() {
        OptionList<RangeTypeEnum> options = new OptionList<RangeTypeEnum>();
        options.addOption(RangeTypeEnum.GREATER_OR_EQUAL.getValue(), RangeTypeEnum.GREATER_OR_EQUAL);
        options.addOption(RangeTypeEnum.LESS_OR_EQUAL.getValue(), RangeTypeEnum.LESS_OR_EQUAL);
        options.addOption(RangeTypeEnum.OUTSIDE_RANGE.getValue(), RangeTypeEnum.OUTSIDE_RANGE);
        options.addOption(RangeTypeEnum.INSIDE_RANGE.getValue(), RangeTypeEnum.INSIDE_RANGE);
        ValueSelectedHandler<RangeTypeEnum> handler = new ValueSelectedHandler<RangeTypeEnum>() {
            public void valueSelected(RangeTypeEnum value) {
                criterion.setRangeType(value);
                setUpRangeParameters();
            }
        };
        SelectListParameter<RangeTypeEnum> rangeTypeParameter = 
            new SelectListParameter<RangeTypeEnum>(getParameters().size(), getRow().getRowIndex(), 
                                                        options, handler, criterion.getRangeType());
        rangeTypeParameter.setLabel(RANGE_TYPE_LABEL);
        rangeTypeParameter.setUpdateFormOnChange(true);
        return rangeTypeParameter;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // anonymous inner class
    private TextFieldParameter createLessOrEqualParameter() {
        final String label = 
            RangeTypeEnum.INSIDE_RANGE.equals(criterion.getRangeType()) 
                || RangeTypeEnum.OUTSIDE_RANGE.equals(criterion.getRangeType())
                ? "And" : "Expression level <=";
        TextFieldParameter rangeParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(), 
                                                                   criterion.getUpperLimit().toString());
        rangeParameter.setLabel(label);
        ValueHandler expressionHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            public void valueChanged(String value) {
                criterion.setUpperLimit(Float.valueOf(value));
            }
        };
        rangeParameter.setValueHandler(expressionHandler);
        return rangeParameter;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // anonymous inner class
    private TextFieldParameter createGreaterOrEqualParameter() {
        String possibleLabel = "Expression level >=";
        if (RangeTypeEnum.INSIDE_RANGE.equals(criterion.getRangeType())) {
            possibleLabel = "Expression level between";
        } else if (RangeTypeEnum.OUTSIDE_RANGE.equals(criterion.getRangeType())) {
            possibleLabel = "Expression level outside of";
        }   
        final String label = possibleLabel;
        TextFieldParameter rangeParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(), 
                                                                   criterion.getLowerLimit().toString());
        rangeParameter.setLabel(label);
        ValueHandler expressionHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            public void valueChanged(String value) {
                criterion.setLowerLimit(Float.valueOf(value));
            }
        };
        rangeParameter.setValueHandler(expressionHandler);
        return rangeParameter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    AbstractGenomicCriterion getAbstractGenomicCriterion() {
        return criterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String getFieldName() {
        return EXPRESSION_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.EXPRESSION_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean platformParameterUpdateOnChange() {
        return false;
    }

    @Override
    protected void updateControlParameters() {
        // no-op, no control parameters for expression level criterion.
        
    }

}
