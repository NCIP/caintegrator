/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicIntervalTypeEnum;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>CopyNumberAlterationCriterion</code>.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity",
    "PMD.ConstructorCallsOverridableMethod" })  // bogus error; mistakenly thinks isValid is called
public class SegmentCriterionWrapper extends AbstractGenomicCriterionWrapper {

    /**
     * The gene name label.
     */
    public static final String FIELD_NAME = "Segmentation";
    
    private int minNumberParameters = 2;

    private final CopyNumberAlterationCriterion criterion;

    SegmentCriterionWrapper(AbstractCriterionRow row) {
        this(new CopyNumberAlterationCriterion(), row);
    }

    SegmentCriterionWrapper(CopyNumberAlterationCriterion criterion, AbstractCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (isStudyHasMultipleCopyNumberPlatforms()) {
            getParameters().add(createPlatformNameParameter(getCopyNumberPlatformNames()));
            minNumberParameters = 3;
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
    AbstractGenomicCriterion getAbstractGenomicCriterion() {
        return criterion;
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
                getRow().getRowIndex(), criterion.getDisplayUpperLimit());
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
                    criterion.setUpperLimit(null);
                } else {
                    criterion.setUpperLimit(Float.valueOf(value));
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
                getRow().getRowIndex(), criterion.getDisplayLowerLimit());
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
                    criterion.setLowerLimit(null);
                } else {
                    criterion.setLowerLimit(Float.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    private SelectListParameter<GenomicIntervalTypeEnum> createIntervalTypeParameter() {
        OptionList<GenomicIntervalTypeEnum> options = new OptionList<GenomicIntervalTypeEnum>();
        options.addOption(GenomicIntervalTypeEnum.GENE_NAME.getValue(), GenomicIntervalTypeEnum.GENE_NAME);
        options.addOption(GenomicIntervalTypeEnum.CHROMOSOME_COORDINATES.getValue(),
                GenomicIntervalTypeEnum.CHROMOSOME_COORDINATES);
        ValueSelectedHandler<GenomicIntervalTypeEnum> handler = new ValueSelectedHandler<GenomicIntervalTypeEnum>() {
            public void valueSelected(GenomicIntervalTypeEnum value) {
                criterion.setGenomicIntervalType(value);
                setIntervalParameters();
            }
        };
        SelectListParameter<GenomicIntervalTypeEnum> intervalTypeParameter = 
            new SelectListParameter<GenomicIntervalTypeEnum>(getParameters().size(), getRow().getRowIndex(), 
                                                        options, handler, criterion.getGenomicIntervalType());
        intervalTypeParameter.setLabel("Genome Interval");
        intervalTypeParameter.setUpdateFormOnChange(true);
        return intervalTypeParameter;
    }

    private void setIntervalParameters() {
        removeExistingIntervalParameters();
        addIntervalParameters();
    }

    private void removeExistingIntervalParameters() {
        int paramLastIndex = getParameters().size() - 1;
        for (int i = paramLastIndex; i > minNumberParameters; i--) {
            getParameters().remove(i);
        }
    }

    private void addIntervalParameters() {
        switch (criterion.getGenomicIntervalType()) {
        case GENE_NAME:
            getParameters().add(createGeneSymbolParameter());
            break;
        case CHROMOSOME_COORDINATES:
            getParameters().add(createChromosomeNumberParameter());
            getParameters().add(createFromCoordinateParameter());
            getParameters().add(createToCoordinateParameter());
            break;
        default:
            return;    
        }
    }

    /**
     * @return
     */
    private AbstractCriterionParameter createToCoordinateParameter() {
        final String label = "To";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getDisplayChromosomeCoordinateHigh());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    criterion.setChromosomeCoordinateHigh(null);
                } else {
                    criterion.setChromosomeCoordinateHigh(Integer.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    /**
     * @return
     */
    private AbstractCriterionParameter createFromCoordinateParameter() {
        final String label = "From";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getDisplayChromosomeCoordinateLow());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            public void valueChanged(String value) {
                if (StringUtils.isBlank(value)) {
                    criterion.setChromosomeCoordinateLow(null);
                } else {
                    criterion.setChromosomeCoordinateLow(Integer.valueOf(value));
                }
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    /**
     * @return
     */
    private AbstractCriterionParameter createChromosomeNumberParameter() {
        final String label = "Chromosome Number";
        TextFieldParameter textParameter = new TextFieldParameter(getParameters().size(),
                getRow().getRowIndex(), criterion.getChromosomeNumber());
        textParameter.setLabel(label);
        ValueHandler valueChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return !StringUtils.isBlank(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Chromosome number value required for " + label);
                }
            }

            public void valueChanged(String value) {
                criterion.setChromosomeNumber(value);
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

}
