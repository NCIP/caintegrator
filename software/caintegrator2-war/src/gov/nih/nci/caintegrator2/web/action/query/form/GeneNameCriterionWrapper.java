/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;

/**
 * Wraps access to a single <code>GeneNameCriterion</code>.
 */
class GeneNameCriterionWrapper extends AbstractGenomicCriterionWrapper {

    static final String FIELD_NAME = "Gene Name";

    private final GeneNameCriterion criterion;

    GeneNameCriterionWrapper(GeneExpressionCriterionRow row) {
        this(new GeneNameCriterion(), row);
    }

    GeneNameCriterionWrapper(GeneNameCriterion criterion, GeneExpressionCriterionRow row) {
        super(row);
        this.criterion = criterion;
        getParameters().add(createGeneNameParameter());
    }

    private TextFieldParameter createGeneNameParameter() {
        TextFieldParameter valueParameter = new TextFieldParameter(0, getRow().getRowIndex(), 
                                                                   criterion.getGeneSymbol());
        valueParameter.setLabel("in list (comma separated: EGFR, BRCA1, etc. )");
        valueParameter.setTitle("Enter a comma separated list of gene symbols ( Ex: EGFR, BRCA1, etc. )");
        valueParameter.setGeneSymbol(true);
        ValueHandler valueHandler = new ValueHandlerAdapter() {
            /**
             * {@inheritDoc}
             */
            public void valueChanged(String value) {
                criterion.setGeneSymbol(value);
            }
        };
        valueParameter.setValueHandler(valueHandler);
        return valueParameter;
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
        return CriterionTypeEnum.GENE_NAME;
    }

}
