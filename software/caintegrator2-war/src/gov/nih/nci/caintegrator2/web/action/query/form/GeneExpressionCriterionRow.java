/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
    AbstractCriterion getCriterion() {
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

    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isBlank(fieldName)) {
            setGenomicCriterionWrapper(null);
        } else if (fieldName.equals(GeneNameCriterionWrapper.FIELD_NAME)) {
            setGenomicCriterionWrapper(new GeneNameCriterionWrapper(this));
        } else if (fieldName.equals(FoldChangeCriterionWrapper.FOLD_CHANGE)) {
            setGenomicCriterionWrapper(new FoldChangeCriterionWrapper(study, this));
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
        this.genomicCriterionWrapper = createCriterionWrapper(criterion);
    }
    
    private AbstractGenomicCriterionWrapper createCriterionWrapper(AbstractCriterion criterion) {
        if (criterion instanceof FoldChangeCriterion) {
            FoldChangeCriterion foldChangeCriterion = (FoldChangeCriterion) criterion;
            return new FoldChangeCriterionWrapper(study, foldChangeCriterion, this);
        } else if (criterion instanceof GeneNameCriterion) {
            GeneNameCriterion geneNameCriterion = (GeneNameCriterion) criterion;
            return new GeneNameCriterionWrapper(geneNameCriterion, this);
        } else {
            throw new IllegalArgumentException("Illegal criterion type " + criterion.getClass());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return CriterionRowTypeEnum.GENE_EXPRESSION.getValue();
    }

}
