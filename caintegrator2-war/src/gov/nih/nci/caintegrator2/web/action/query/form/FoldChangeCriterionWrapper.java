/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>FoldChangeCriterion</code>.
 */
class FoldChangeCriterionWrapper extends AbstractGenomicCriterionWrapper {
    private static final int NUMBER_OF_MAX_PARAMETERS = 5;
    private static final int NUMBER_OF_MAX_PARAMETERS_MULTIPLE_PLATFORMS = NUMBER_OF_MAX_PARAMETERS + 1;
    private static final float DEFAULT_FOLDS = 2.0f;
    private static final Float DEFAULT_FOLDS_UNCHANGED_DOWN = 0.8f;
    private static final Float DEFAULT_FOLDS_UNCHANGED_UP = 1.2f;
    private static final String CONTROL_SAMPLE_SET_LABEL = "Control Sample Set";
    private static final String REGULATION_TYPE_LABEL = "Regulation Type";
    static final String FOLD_CHANGE = "Fold Change";
    private Integer controlParameterIndex = null;
    private Study study = null;

    private final FoldChangeCriterion criterion;

    FoldChangeCriterionWrapper(Study study, GeneExpressionCriterionRow row) {
        this(study, new FoldChangeCriterion(), row);
    }

    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")  // bogus error
    FoldChangeCriterionWrapper(Study study, FoldChangeCriterion criterion, GeneExpressionCriterionRow row) {
        super(row);
        this.study = study;
        this.criterion = criterion;
        if (criterion.getRegulationType() == null) {
            criterion.setRegulationType(RegulationTypeEnum.UP);
            setCriterionDefaults();
        }
        setupDefaultGenomicParameters(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        getParameters().add(createControlSampleSetParameter());
        getParameters().add(createRegulationTypeParameter());
        addFoldsParameters();
    }

    private void setUpFoldsParameters() {
        setCriterionDefaults();
        removeExistingFoldsParameters();
        addFoldsParameters();
    }

    @Override
    protected void updateControlParameters() {
        if (controlParameterIndex != null) {
            getParameters().remove(getParameters().get(controlParameterIndex));
            getParameters().add(controlParameterIndex, createControlSampleSetParameter());
        }
    }

    private void addFoldsParameters() {
        switch (criterion.getRegulationType()) {
            case UP:
                getParameters().add(createFoldsUpParameter());
                break;
            case DOWN:
                getParameters().add(createFoldsDownParameter());
                break;
            case UP_OR_DOWN:
            case UNCHANGED:
                getParameters().add(createFoldsDownParameter());
                getParameters().add(createFoldsUpParameter());
                break;
            default:
                break;
        }
    }

    private void removeExistingFoldsParameters() {
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
        switch (criterion.getRegulationType()) {
            case UP:
                criterion.setFoldsUp(DEFAULT_FOLDS);
                break;
            case DOWN:
                criterion.setFoldsDown(DEFAULT_FOLDS);
                break;
            case UP_OR_DOWN:
                criterion.setFoldsUp(DEFAULT_FOLDS);
                criterion.setFoldsDown(DEFAULT_FOLDS);
                break;
            case UNCHANGED:
                criterion.setFoldsDown(DEFAULT_FOLDS_UNCHANGED_DOWN);
                criterion.setFoldsUp(DEFAULT_FOLDS_UNCHANGED_UP);
                break;
            default:
                break;
        }
    }

    private SelectListParameter<String> createControlSampleSetParameter() {
        OptionList<String> options = new OptionList<String>();
        String platformNameToUse = null;
        if (isStudyHasMultipleGeneExpressionPlatforms()) {
            platformNameToUse = StringUtils.isBlank(criterion.getPlatformName())
                    ? "UNKNOWN" : criterion.getPlatformName();
        }
        for (String name : study.getStudyConfiguration().getControlSampleSetNames(platformNameToUse)) {
            options.addOption(name, name);
        }
        ValueSelectedHandler<String> handler = new ValueSelectedHandler<String>() {
            @Override
            public void valueSelected(String value) {
                criterion.setControlSampleSetName(value);
            }
        };
        if (controlParameterIndex == null) {
            controlParameterIndex = getParameters().size();
        }
        SelectListParameter<String> controlSampleSetNameParameter =
            new SelectListParameter<String>(controlParameterIndex, getRow().getRowIndex(),
                                            options, handler, criterion.getControlSampleSetName());
        controlSampleSetNameParameter.setLabel(CONTROL_SAMPLE_SET_LABEL);
        controlSampleSetNameParameter.setUpdateFormOnChange(false);
        return controlSampleSetNameParameter;
    }

    private SelectListParameter<RegulationTypeEnum> createRegulationTypeParameter() {
        OptionList<RegulationTypeEnum> options = new OptionList<RegulationTypeEnum>();
        options.addOption(RegulationTypeEnum.UP.getValue(), RegulationTypeEnum.UP);
        options.addOption(RegulationTypeEnum.DOWN.getValue(), RegulationTypeEnum.DOWN);
        options.addOption(RegulationTypeEnum.UP_OR_DOWN.getValue(), RegulationTypeEnum.UP_OR_DOWN);
        options.addOption(RegulationTypeEnum.UNCHANGED.getValue(), RegulationTypeEnum.UNCHANGED);
        ValueSelectedHandler<RegulationTypeEnum> handler = new ValueSelectedHandler<RegulationTypeEnum>() {
            @Override
            public void valueSelected(RegulationTypeEnum value) {
                criterion.setRegulationType(value);
                setUpFoldsParameters();
            }
        };
        SelectListParameter<RegulationTypeEnum> regulationTypeParameter =
            new SelectListParameter<RegulationTypeEnum>(getParameters().size(), getRow().getRowIndex(),
                                                        options, handler, criterion.getRegulationType());
        regulationTypeParameter.setLabel(REGULATION_TYPE_LABEL);
        regulationTypeParameter.setUpdateFormOnChange(true);
        return regulationTypeParameter;
    }

    private TextFieldParameter createFoldsDownParameter() {
        final String label =
            RegulationTypeEnum.UNCHANGED.equals(criterion.getRegulationType())
                ? "Folds between" : "Down-regulation folds";
        TextFieldParameter foldsParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(),
                                                                   criterion.getFoldsDown().toString());
        foldsParameter.setLabel(label);
        ValueHandler foldsChangeHandler = new ValueHandlerAdapter() {

            @Override
            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            @Override
            public void valueChanged(String value) {
                criterion.setFoldsDown(Float.valueOf(value));
            }
        };
        foldsParameter.setValueHandler(foldsChangeHandler);
        return foldsParameter;
    }

    private TextFieldParameter createFoldsUpParameter() {
        final String label =
            RegulationTypeEnum.UNCHANGED.equals(criterion.getRegulationType()) ? "And" : "Up-regulation folds";
        TextFieldParameter foldsParameter = new TextFieldParameter(getParameters().size(), getRow().getRowIndex(),
                                                                   criterion.getFoldsUp().toString());
        foldsParameter.setLabel(label);
        ValueHandler foldsChangeHandler = new ValueHandlerAdapter() {

            @Override
            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            @Override
            public void valueChanged(String value) {
                criterion.setFoldsUp(Float.valueOf(value));
            }
        };
        foldsParameter.setValueHandler(foldsChangeHandler);
        return foldsParameter;
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
        return FOLD_CHANGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.FOLD_CHANGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean platformParameterUpdateOnChange() {
        return true;
    }

}
