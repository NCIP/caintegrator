/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicIntervalTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>CopyNumberAlterationCriterion</code>.
 */
abstract class AbstractCopyNumberCoordinateWrapper extends AbstractGenomicCriterionWrapper {

    private int minNumberParameters;

    private final CopyNumberAlterationCriterion criterion;

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractGenomicCriterion getAbstractGenomicCriterion() {
        return criterion;
    }

    /**
     * @return the minNumberParameters
     */
    protected int getMinNumberParameters() {
        return minNumberParameters;
    }

    /**
     * @param minNumberParameters the minNumberParameters to set
     */
    protected void setMinNumberParameters(int minNumberParameters) {
        this.minNumberParameters = minNumberParameters;
    }

    /**
     * @param row
     */
    AbstractCopyNumberCoordinateWrapper(AbstractCriterionRow row, CopyNumberAlterationCriterion criterion) {
        super(row);
        this.criterion = criterion;
    }

    @Override
    abstract String getFieldName();
    @Override
    abstract CriterionTypeEnum getCriterionType();

    protected SelectListParameter<GenomicIntervalTypeEnum> createIntervalTypeParameter() {
        OptionList<GenomicIntervalTypeEnum> options = new OptionList<GenomicIntervalTypeEnum>();
        options.addOption(GenomicIntervalTypeEnum.GENE_NAME.getValue(), GenomicIntervalTypeEnum.GENE_NAME);
        options.addOption(GenomicIntervalTypeEnum.CHROMOSOME_COORDINATES.getValue(),
                GenomicIntervalTypeEnum.CHROMOSOME_COORDINATES);
        ValueSelectedHandler<GenomicIntervalTypeEnum> handler = new ValueSelectedHandler<GenomicIntervalTypeEnum>() {
            @Override
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

    protected void setIntervalParameters() {
        removeExistingIntervalParameters();
        addIntervalParameters();
    }

    private void removeExistingIntervalParameters() {
        int paramLastIndex = getParameters().size() - 1;
        for (int i = paramLastIndex; i > getMinNumberParameters(); i--) {
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

            @Override
            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            @Override
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

            @Override
            public boolean isValid(String value) {
                return StringUtils.isBlank(value) || NumberUtils.isNumber(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            @Override
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

            @Override
            public boolean isValid(String value) {
                return !StringUtils.isBlank(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Chromosome number value required for " + label);
                }
            }

            @Override
            public void valueChanged(String value) {
                criterion.setChromosomeNumber(value);
            }
        };
        textParameter.setValueHandler(valueChangeHandler);
        return textParameter;
    }

    /**
     * @return the criterion
     */
    protected CopyNumberAlterationCriterion getCopyNumberAlterationCriterion() {
        return criterion;
    }

}
