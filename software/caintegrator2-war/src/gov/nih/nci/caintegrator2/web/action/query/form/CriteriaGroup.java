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
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Contains a set of rows that are either logically ANDed or ORed together. There is a
 * single top-level group per query.
 */
public class CriteriaGroup {
    
    private static final String NULL_ANNOTATION_AFD_TYPE = "~NULL_AFD_TYPE~";
    private final CompoundCriterion compoundCriterion;
    private final QueryForm form;
    private String criterionType = "";
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

    private void addCriterionRow(Study study, AbstractCriterion criterion) {
        AbstractCriterionRow row = createRow(study, getCriterionRowType(criterion));
        rows.add(row);
        row.setCriterion(criterion);
    }

    private String getCriterionRowType(AbstractCriterion criterion) {
        if (criterion instanceof AbstractGenomicCriterion) {
            return getGenomicCriterionRowType(criterion);
        } else if (criterion instanceof SubjectListCriterion) {
            return CriterionRowTypeEnum.SAVED_LIST.getValue();
        } else if (criterion instanceof IdentifierCriterion) {
            return CriterionRowTypeEnum.UNIQUE_IDENTIIFER.getValue();
        } else if (criterion instanceof AbstractAnnotationCriterion) {
            return getAnnotationCriterionRowName(criterion);
        } else {
            throw new IllegalArgumentException("Unsupported criterion: " + criterion.getClass());
        }
    }

    private String getGenomicCriterionRowType(AbstractCriterion criterion) {
        if (criterion instanceof GeneNameCriterion
                && GenomicCriterionTypeEnum.COPY_NUMBER.equals(((GeneNameCriterion) criterion)
                        .getGenomicCriterionType())) {
            return CriterionRowTypeEnum.COPY_NUMBER.getValue();
        } else if (criterion instanceof CopyNumberAlterationCriterion) {
            return CriterionRowTypeEnum.COPY_NUMBER.getValue();
        }
        return CriterionRowTypeEnum.GENE_EXPRESSION.getValue();
    }

    private String getAnnotationCriterionRowName(AbstractCriterion criterion) {
        AbstractAnnotationCriterion annotationCriterion = (AbstractAnnotationCriterion) criterion; 
        if (annotationCriterion.getAnnotationFieldDescriptor() == null 
                || annotationCriterion.getAnnotationFieldDescriptor().getAnnotationGroup() == null) {
            return NULL_ANNOTATION_AFD_TYPE;
        }
        return annotationCriterion.getAnnotationFieldDescriptor().getAnnotationGroup().getName();
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

    private AbstractCriterionRow createRow(Study study, String rowType) {
        AbstractCriterionRow criterionRow;
        if (rowType == null) {
            throw new IllegalStateException("Invalid CriterionRowTypeEnum " + rowType);
        }
        if (NULL_ANNOTATION_AFD_TYPE.equals(rowType)) {
            return new InvalidCriterionRow(this);
        }
        if (study.getAnnotationGroup(rowType) != null) {
            criterionRow = new AnnotationCriterionRow(this, rowType);
        } else {
            criterionRow = retrieveCriterionRowByType(study, rowType);
        }
        return criterionRow;
    }

    private AbstractCriterionRow retrieveCriterionRowByType(Study study, String rowType) {
        switch (CriterionRowTypeEnum.getByValue(rowType)) {
        case GENE_EXPRESSION:
            return new GeneExpressionCriterionRow(study, this);
        case COPY_NUMBER:
            return new CopyNumberCriterionRow(study, this);
        case SAVED_LIST:
            return new SavedListCriterionRow(this);
        case UNIQUE_IDENTIIFER:
            return new IdentifierCriterionRow(this);
        default:
            throw new IllegalStateException("Invalid CriterionRowTypeEnum " + rowType);
        }
    }

    /**
     * @return the criterionTypeName
     */
    public String getCriterionTypeName() {
        return criterionType;
    }

    /**
     * @param criterionTypeName the criterionTypeName to set
     */
    public void setCriterionTypeName(String criterionTypeName) {
        criterionType = criterionTypeName;
    }
    
    StudySubscription getSubscription() {
        return getForm().getQuery().getSubscription();
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
     * @return boolean of having no gene expression criterion
     */
    public boolean hasNoGeneExpressionCriterion() {
        for (AbstractCriterionRow row : getRows()) {
            if (row.getCriterion() instanceof GeneNameCriterion
                    || row.getCriterion() instanceof FoldChangeCriterion) {
                return false;
            }
        }
        return true;
    }
}
