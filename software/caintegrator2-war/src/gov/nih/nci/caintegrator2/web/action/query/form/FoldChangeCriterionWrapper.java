/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a single <code>FoldChangeCriterion</code>.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // anonymous inner class
class FoldChangeCriterionWrapper extends AbstractGenomicCriterionWrapper {
    
    private static final float DEFAULT_FOLDS = 2.0f;
    private static final Float DEFAULT_FOLDS_UNCHANGED_DOWN = 0.8f;
    private static final Float DEFAULT_FOLDS_UNCHANGED_UP = 1.2f;
    private static final String SYMBOL_LABEL = "Gene Symbol(s) (comma separated list)";
    private static final String CONTROL_SAMPLE_SET_LABEL = "Control Sample Set";
    private static final String REGULATION_TYPE_LABEL = "Regulation Type";
    static final String FOLD_CHANGE = "Fold Change";

    private final FoldChangeCriterion criterion;

    FoldChangeCriterionWrapper(Study study, GeneExpressionCriterionRow row) {
        this(study, new FoldChangeCriterion(), row);
    }

    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")  // bogus error; mistakenly thinks isValid is called
    FoldChangeCriterionWrapper(Study study, FoldChangeCriterion criterion, GeneExpressionCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (criterion.getRegulationType() == null) {
            criterion.setRegulationType(RegulationTypeEnum.UP);
            setCriterionDefaults();
        }
        getParameters().add(createGeneSymbolParameter());
        getParameters().add(createControlSampleSetParameter(study));
        getParameters().add(createRegulationTypeParameter());
        addFoldsParameters();
    }

    private void setUpFoldsParameters() {
        setCriterionDefaults();
        removeExistingFoldsParameters();
        addFoldsParameters();
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
        if (getParameters().size() == 5) {
            getParameters().remove(4);
        }
        if (getParameters().size() == 4) {
            getParameters().remove(3);
        }
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

    private AbstractCriterionParameter createGeneSymbolParameter() {
        TextFieldParameter geneSymbolParameter = new TextFieldParameter(0, getRow().getRowIndex(), 
                                                                        criterion.getGeneSymbol());
        geneSymbolParameter.setLabel(SYMBOL_LABEL);
        geneSymbolParameter.setTitle("Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. )");
        geneSymbolParameter.setGeneSymbol(true);
        ValueHandler geneSymbolHandler = new ValueHandlerAdapter() {
            
            public boolean isValid(String value) {
                return !StringUtils.isBlank(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (StringUtils.isBlank(value)) {
                    action.addActionError("A value is required for Gene Symbol");
                }
            }

            public void valueChanged(String value) {
                criterion.setGeneSymbol(value);
            }
        };
        geneSymbolParameter.setValueHandler(geneSymbolHandler);
        return geneSymbolParameter;
    }

    private SelectListParameter<String> createControlSampleSetParameter(Study study) {
        OptionList<String> options = new OptionList<String>();
        for (String name : study.getStudyConfiguration().getControlSampleSetNames()) {
            options.addOption(name, name);
        }
        ValueSelectedHandler<String> handler = new ValueSelectedHandler<String>() {
            public void valueSelected(String value) {
                criterion.setControlSampleSetName(value);
            }
        };
        SelectListParameter<String> controlSampleSetNameParameter = 
            new SelectListParameter<String>(1, getRow().getRowIndex(), 
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
            public void valueSelected(RegulationTypeEnum value) {
                criterion.setRegulationType(value);
                setUpFoldsParameters();
            }
        };
        SelectListParameter<RegulationTypeEnum> regulationTypeParameter = 
            new SelectListParameter<RegulationTypeEnum>(2, getRow().getRowIndex(), 
                                                        options, handler, criterion.getRegulationType());
        regulationTypeParameter.setLabel(REGULATION_TYPE_LABEL);
        regulationTypeParameter.setUpdateFormOnChange(true);
        return regulationTypeParameter;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // anonymous inner class
    private TextFieldParameter createFoldsDownParameter() {
        final String label = 
            RegulationTypeEnum.UNCHANGED.equals(criterion.getRegulationType()) 
                ? "Folds between" : "Down-regulation folds";
        TextFieldParameter foldsParameter = new TextFieldParameter(3, getRow().getRowIndex(), 
                                                                   criterion.getFoldsDown().toString());
        foldsParameter.setLabel(label);
        ValueHandler foldsChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

            public void valueChanged(String value) {
                criterion.setFoldsDown(Float.valueOf(value));
            }
        };
        foldsParameter.setValueHandler(foldsChangeHandler);
        return foldsParameter;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")   // anonymous inner class
    private TextFieldParameter createFoldsUpParameter() {
        final String label = 
            RegulationTypeEnum.UNCHANGED.equals(criterion.getRegulationType()) ? "And" : "Up-regulation folds";
        int parameterIndex = RegulationTypeEnum.UP.equals(criterion.getRegulationType()) ? 3 : 4;
        TextFieldParameter foldsParameter = new TextFieldParameter(parameterIndex, getRow().getRowIndex(), 
                                                                   criterion.getFoldsUp().toString());
        foldsParameter.setLabel(label);
        ValueHandler foldsChangeHandler = new ValueHandlerAdapter() {

            public boolean isValid(String value) {
                return NumberUtils.isNumber(value);
            }

            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + label);
                }
            }

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

}
