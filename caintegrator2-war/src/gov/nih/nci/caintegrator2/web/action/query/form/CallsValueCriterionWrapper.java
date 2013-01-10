/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;

import java.util.List;

/**
 * Wraps access to a single <code>CopyNumberAlterationCriterion</code>.
 */
public class CallsValueCriterionWrapper extends AbstractCopyNumberCoordinateWrapper {

    /**
     * The gene name label.
     */
    public static final String FIELD_NAME = "Calls";

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
            @Override
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

}
