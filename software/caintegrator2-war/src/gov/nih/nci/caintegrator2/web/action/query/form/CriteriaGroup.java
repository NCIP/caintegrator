/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Contains a set of rows that are either logically ANDed or ORed together. There is a
 * single top-level group per query.
 */
public class CriteriaGroup {
    
    private final CompoundCriterion compoundCriterion;
    private final QueryForm form;
    private CriterionRowTypeEnum criterionType;
    private final List<AbstractCriterionRow> rows = new ArrayList<AbstractCriterionRow>();

    CriteriaGroup(QueryForm form) {
        if (form.getQuery() == null || form.getQuery().getCompoundCriterion() == null) {
            throw new IllegalArgumentException("Argument queryForm requires an initialized query.");
        }
        this.form = form;
        this.compoundCriterion = form.getQuery().getCompoundCriterion();
        initializeCriteria(form.getQuery().getSubscription().getStudy(), compoundCriterion.getCriterionCollection());
    }

    private void initializeCriteria(Study study, Collection<AbstractCriterion> criterionCollection) {
        Iterator<AbstractCriterion> iterator = criterionCollection.iterator();
        while (iterator.hasNext()) {            
            addCriterionRow(study, iterator.next());
        }
    }

    private AbstractCriterionRow addCriterionRow(Study study, AbstractCriterion criterion) {
        AbstractCriterionRow row = createRow(study, getCriterionRowType(criterion));
        rows.add(row);
        row.setCriterion(criterion);
        return row;
    }

    private CriterionRowTypeEnum getCriterionRowType(AbstractCriterion criterion) {
        if (criterion instanceof AbstractGenomicCriterion) {
            return CriterionRowTypeEnum.GENE_EXPRESSION;
        } else if (criterion instanceof AbstractAnnotationCriterion) {
            AbstractAnnotationCriterion annotationCriterion = (AbstractAnnotationCriterion) criterion;
            switch (annotationCriterion.getEntityType()) {
            case IMAGESERIES:
                return CriterionRowTypeEnum.IMAGE_SERIES;
            case SUBJECT:
                return CriterionRowTypeEnum.CLINICAL;
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + annotationCriterion.getEntityType());
            }
        } else {
            throw new IllegalArgumentException("Unsupported criterion: " + criterion.getClass());
        }
    }

    /**
     * @return the booleanOperator
     */
    public String getBooleanOperator() {
        return compoundCriterion.getBooleanOperator().getValue();
    }

    /**
     * @param booleanOperator the booleanOperator to set
     */
    public void setBooleanOperator(String booleanOperator) {
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.getByValue(booleanOperator));
    }
    
    /**
     * Removes the row at the given row number.
     * @param rowNumber to remove from list.
     */
    public void removeRow(int rowNumber) {
        rows.get(rowNumber).removeCriterionFromQuery();
        rows.remove(rowNumber);
        for (AbstractCriterionRow row : rows) {
            int rowIndex = rows.indexOf(row);
            for (AbstractCriterionParameter parameter : row.getParameters()) {
                parameter.setRowIndex(rowIndex);
            }
        }
    }

    /**
     * Adds a new criterion of the currently selected type.
     * @param study to populate the controlSampleSet options
     */
    public void addCriterion(Study study) {
        rows.add(createRow(study, criterionType));
    }

    private AbstractCriterionRow createRow(Study study, CriterionRowTypeEnum rowType) {
        AbstractCriterionRow criterionRow;
        if (rowType == null) {
            throw new IllegalStateException("Invalid CriterionRowTypeEnum " + rowType);
        }
        switch (rowType) {
        case CLINICAL:
            criterionRow = new ClinicalCriterionRow(this);
            break;
        case IMAGE_SERIES:
            criterionRow = new ImageSeriesCriterionRow(this);
            break;
        case GENE_EXPRESSION:
            criterionRow = new GeneExpressionCriterionRow(study, this);
            break;
        default:
            throw new IllegalStateException("Invalid CriterionRowTypeEnum " + rowType);
        }
        return criterionRow;
    }

    /**
     * @return the criterionTypeName
     */
    public String getCriterionTypeName() {
        if (criterionType == null) {
            return "";
        } else {
            return criterionType.getValue();
        }
    }

    /**
     * @return the valid criterionTypeNames
     */
    public List<String> getCriterionTypeNames() {
        List<String> names = new ArrayList<String>();
        for (CriterionRowTypeEnum rowType : CriterionRowTypeEnum.values()) {
            names.add(rowType.getValue());
        }
        return names;
    }

    /**
     * @param criterionTypeName the criterionTypeName to set
     */
    public void setCriterionTypeName(String criterionTypeName) {
        if (StringUtils.isBlank(criterionTypeName)) {
            criterionType = null;
        } else {
            criterionType = CriterionRowTypeEnum.getByValue(criterionTypeName);
        }
    }

    QueryForm getForm() {
        return form;
    }

    /**
     * @return the rows
     */
    public List<AbstractCriterionRow> getRows() {
        return rows;
    }

    CompoundCriterion getCompoundCriterion() {
        return compoundCriterion;
    }

    void validate(ValidationAware action) {
        for (AbstractCriterionRow row : rows) {
            row.validate(action);
        }
    }

    void processCriteriaChanges() {
        for (AbstractCriterionRow row : getRows()) {
            row.processCriteriaChanges();
        }
    }
    
    /**
     * @return boolean of having no genomic criterion
     */
    public boolean hasNoGenomicCriterion() {
        for (AbstractCriterionRow row : getRows()) {
            if (row.getCriterion() instanceof AbstractGenomicCriterion) {
                return false;
            }
        }
        return true;
    }
}
