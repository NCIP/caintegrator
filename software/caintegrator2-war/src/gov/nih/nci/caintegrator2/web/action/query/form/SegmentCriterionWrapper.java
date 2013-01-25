/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>CopyNumberAlterationCriterion</code>.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity",
    "PMD.ConstructorCallsOverridableMethod" })  // bogus error; mistakenly thinks isValid is called
public class SegmentCriterionWrapper extends AbstractCopyNumberCoordinateWrapper {

    /**
     * The gene name label.
     */
    public static final String FIELD_NAME = "Segmentation";

    SegmentCriterionWrapper(AbstractCriterionRow row) {
        this(new CopyNumberAlterationCriterion(), row);
    }

    SegmentCriterionWrapper(CopyNumberAlterationCriterion criterion, AbstractCriterionRow row) {
        super(row, criterion);
        setMinNumberParameters(2);
        if (isStudyHasMultipleCopyNumberPlatforms()) {
            getParameters().add(createPlatformNameParameter(getCopyNumberPlatformNames()));
            setMinNumberParameters(3);
        }
        getParameters().add(createUpperLimitParameter());
        getParameters().add(createLowerLimitParameter());
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
        return CriterionTypeEnum.SEGMENTATION;
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
    
    @SuppressWarnings("PMD.CyclomaticComplexity")
    private TextFieldParameter createUpperLimitParameter() {
        final String label = "Segment Mean Value <=";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), getCopyNumberAlterationCriterion().getDisplayUpperLimit());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required or blank for " + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    getCopyNumberAlterationCriterion().setUpperLimit(null);
                } else {
                    getCopyNumberAlterationCriterion().setUpperLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private TextFieldParameter createLowerLimitParameter() {
        final String label = "Segment Mean Value >=";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), getCopyNumberAlterationCriterion().getDisplayLowerLimit());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required or blank for " + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    getCopyNumberAlterationCriterion().setLowerLimit(null);
                } else {
                    getCopyNumberAlterationCriterion().setLowerLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

}
