/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;

/**
 * Wraps access to a single <code>GeneNameCriterion</code>.
 */
public class GeneNameCriterionWrapper extends AbstractGenomicCriterionWrapper {

    /**
     * The gene name label.
     */
    public static final String FIELD_NAME = "Gene Name";

    private final GeneNameCriterion criterion;

    GeneNameCriterionWrapper(AbstractCriterionRow row, GenomicCriterionTypeEnum criterionType) {
        this(new GeneNameCriterion(criterionType), row);
    }

    GeneNameCriterionWrapper(GeneNameCriterion criterion, AbstractCriterionRow row) {
        super(row);
        this.criterion = criterion;
        setupDefaultGenomicParameters(criterion.getGenomicCriterionType());
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateControlParameters() {
        // no-op, no control parameters for gene name criterion.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean platformParameterUpdateOnChange() {
        return false;
    }

}
