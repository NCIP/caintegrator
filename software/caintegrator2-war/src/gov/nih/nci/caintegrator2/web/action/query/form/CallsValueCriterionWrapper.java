/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;

import java.util.List;

/**
 * Wraps access to a single <code>CopyNumberAlterationCriterion</code>.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity",
    "PMD.ConstructorCallsOverridableMethod" })  // bogus error; mistakenly thinks isValid is called
public class CallsValueCriterionWrapper extends AbstractCopyNumberCoordinateWrapper {

    /**
     * The gene name label.
     */
    public static final String FIELD_NAME = "Calls";

//    private static final String PMD_COMPLEXITY = "PMD.CyclomaticComplexity";
//    private static final String NUMERIC_VALUE_REQUIRED = "Numeric value required or blank for ";

    CallsValueCriterionWrapper(AbstractCriterionRow row) {
        this(new CopyNumberAlterationCriterion(), row);
    }

    CallsValueCriterionWrapper(CopyNumberAlterationCriterion criterion, AbstractCriterionRow row) {
        super(row, criterion);
        criterion.setCopyNumberCriterionType(CopyNumberCriterionTypeEnum.CALLS_VALUE);
        setMinNumberParameters(1);
        if (isStudyHasMultipleCopyNumberPlatformsWithCghCall()) {
            getParameters().add(createPlatformNameParameter(getCopyNumberPlatformNamesWithCghCall()));
            setMinNumberParameters(2);
        }
        getParameters().add(createCallsValueParameter());
/*        
        getParameters().add(createProbabilityLossParameter());
        getParameters().add(createProbabilityNormalParameter());
        getParameters().add(createProbabilityGainParameter());
        getParameters().add(createProbabilityAmplificationParameter());
*/
        getParameters().add(createIntervalTypeParameter());
        setIntervalParameters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String getFieldName() {
        return FIELD_NAME;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.CGHCALL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateControlParameters() {
        // no-op, no control parameters for segmentation criterion.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean platformParameterUpdateOnChange() {
        return false;
    }

    private MultiSelectParameter<Integer> createCallsValueParameter() {
        OptionList<Integer> options = new OptionList<Integer>();
        options.addOption("-1", -1);
        options.addOption("0", 0);
        options.addOption("1", 1);
        options.addOption("2", 2);
        ValuesSelectedHandler<Integer> handler = new ValuesSelectedHandler<Integer>() {
            public void valuesSelected(List<Integer> values) {
                getCopyNumberAlterationCriterion().getCallsValues().clear();
                getCopyNumberAlterationCriterion().getCallsValues().addAll(values);
            }
        };
        MultiSelectParameter<Integer> callsValueParameter = 
            new MultiSelectParameter<Integer>(getParameters().size(), getRow().getRowIndex(), 
                    options, handler, getCopyNumberAlterationCriterion().getCallsValues());
        callsValueParameter.setLabel("Calls Value in");
        return callsValueParameter;
    }
/*
    @SuppressWarnings(PMD_COMPLEXITY)
    private TextFieldParameter createProbabilityLossParameter() {
        final String label = "Probability Loss >=";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getDisplayUpperLimit());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError(NUMERIC_VALUE_REQUIRED + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    criterion.setUpperLimit(null);
                } else {
                    criterion.setUpperLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    @SuppressWarnings(PMD_COMPLEXITY)
    private TextFieldParameter createProbabilityNormalParameter() {
        final String label = "Probability Normal >=";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getDisplayLowerLimit());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError(NUMERIC_VALUE_REQUIRED + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    criterion.setLowerLimit(null);
                } else {
                    criterion.setLowerLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    @SuppressWarnings(PMD_COMPLEXITY)
    private TextFieldParameter createProbabilityGainParameter() {
        final String label = "Probability Gain >=";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getDisplayLowerLimit());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError(NUMERIC_VALUE_REQUIRED + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    criterion.setLowerLimit(null);
                } else {
                    criterion.setLowerLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    @SuppressWarnings(PMD_COMPLEXITY)
    private TextFieldParameter createProbabilityAmplificationParameter() {
        final String label = "Probability Amplification >=";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getDisplayLowerLimit());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError(NUMERIC_VALUE_REQUIRED + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    criterion.setLowerLimit(null);
                } else {
                    criterion.setLowerLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }
*/

}
