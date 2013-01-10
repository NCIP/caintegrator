/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains and manages a gene expression criterion.
 */
public class CopyNumberCriterionRow extends AbstractCriterionRow {

    private AbstractGenomicCriterionWrapper genomicCriterionWrapper;
    private final Study study;

    CopyNumberCriterionRow(Study study, CriteriaGroup criteriaGroup) {
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
        fieldNames.add(SegmentCriterionWrapper.FIELD_NAME);
        if (study.hasCghCalls()) {
            fieldNames.add(CallsValueCriterionWrapper.FIELD_NAME);
        }
        return fieldNames;
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
            setGenomicCriterionWrapper(new GeneNameCriterionWrapper(this, GenomicCriterionTypeEnum.COPY_NUMBER));
        } else if (fieldName.equals(SegmentCriterionWrapper.FIELD_NAME)) {
            setGenomicCriterionWrapper(new SegmentCriterionWrapper(this));
        } else if (fieldName.equals(CallsValueCriterionWrapper.FIELD_NAME)) {
            setGenomicCriterionWrapper(new CallsValueCriterionWrapper(this));
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
        return CriterionRowTypeEnum.COPY_NUMBER.getValue();
    }

}
