/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains and manages a gene expression criterion.
 */
public class GeneExpressionCriterionRow extends AbstractCriterionRow {

    private AbstractGenomicCriterionWrapper genomicCriterionWrapper;
    private final Study study;

    GeneExpressionCriterionRow(Study study, CriteriaGroup criteriaGroup) {
        super(criteriaGroup);
        this.study = study;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add(GeneNameCriterionWrapper.FIELD_NAME);
        fieldNames.add(ExpressionLevelCriterionWrapper.EXPRESSION_LEVEL);
        if (hasControlSamples()) {
            fieldNames.add(FoldChangeCriterionWrapper.FOLD_CHANGE);
        }
        return fieldNames;
    }

    private boolean hasControlSamples() {
        return getGroup().getForm().isControlSamplesInStudy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCriterion getCriterion() {
        if (getGenomicCriterionWrapper() != null) {
            return getGenomicCriterionWrapper().getCriterion();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName() {
        if (getGenomicCriterionWrapper() != null) {
            return getGenomicCriterionWrapper().getFieldName();
        } else {
            return null;
        }
    }

    @Override
    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            setGenomicCriterionWrapper(null);
        } else if (fieldName.equals(GeneNameCriterionWrapper.FIELD_NAME)) {
            setGenomicCriterionWrapper(new GeneNameCriterionWrapper(this, GenomicCriterionTypeEnum.GENE_EXPRESSION));
        } else if (fieldName.equals(FoldChangeCriterionWrapper.FOLD_CHANGE)) {
            setGenomicCriterionWrapper(new FoldChangeCriterionWrapper(study, this));
        } else if (fieldName.equals(ExpressionLevelCriterionWrapper.EXPRESSION_LEVEL)) {
            setGenomicCriterionWrapper(new ExpressionLevelCriterionWrapper(this));

        } else {
            throw new IllegalArgumentException("Unsupported genomic field name " + fieldName);
        }
    }

    private AbstractGenomicCriterionWrapper getGenomicCriterionWrapper() {
        return genomicCriterionWrapper;
    }

    private void setGenomicCriterionWrapper(AbstractGenomicCriterionWrapper genomicCriterionWrapper) {
        if (this.genomicCriterionWrapper != null) {
            removeCriterionFromQuery();
        }
        this.genomicCriterionWrapper = genomicCriterionWrapper;
        if (genomicCriterionWrapper != null) {
            addCriterionToQuery();
        }
    }

    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return getGenomicCriterionWrapper();
    }

    @Override
    void setCriterion(AbstractCriterion criterion) {
        this.genomicCriterionWrapper = CriterionWrapperBuilder.createGenomicCriterionWrapper(criterion, this, study);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return CriterionRowTypeEnum.GENE_EXPRESSION.getValue();
    }

}
