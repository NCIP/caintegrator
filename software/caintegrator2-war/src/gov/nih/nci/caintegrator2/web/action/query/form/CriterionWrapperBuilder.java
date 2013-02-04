/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.translational.Study;

/**
 * 
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // methods create objects based on criterion type, many type checks.
public final class CriterionWrapperBuilder {
    
    private CriterionWrapperBuilder() { }
    
    static AbstractCriterionWrapper createAnnotationCriterionWrapper(AbstractCriterion criterion, 
            AnnotationCriterionRow row, StudySubscription subscription) {
        if (criterion instanceof StringComparisonCriterion) {
            StringComparisonCriterion stringComparisonCriterion = (StringComparisonCriterion) criterion;
            return new StringComparisonCriterionWrapper(stringComparisonCriterion, row);
        } else if (criterion instanceof NumericComparisonCriterion) {
            NumericComparisonCriterion numericComparisonCriterion = (NumericComparisonCriterion) criterion;
            return new NumericComparisonCriterionWrapper(numericComparisonCriterion, row);
        } else if (criterion instanceof DateComparisonCriterion) {
            DateComparisonCriterion dateComparisonCriterion = (DateComparisonCriterion) criterion;
            return new DateComparisonCriterionWrapper(dateComparisonCriterion, row);
        } else if (criterion instanceof SelectedValueCriterion) {
            SelectedValueCriterion selectedValueCriterion = (SelectedValueCriterion) criterion;
            return new SelectedValueCriterionWrapper(selectedValueCriterion, row);
        } else {
            throw new IllegalArgumentException("Illegal criterion type " + criterion.getClass());
        }
    }
    
    static AbstractGenomicCriterionWrapper createGenomicCriterionWrapper(AbstractCriterion criterion, 
            GeneExpressionCriterionRow row, Study study) {
        if (criterion instanceof FoldChangeCriterion) {
            FoldChangeCriterion foldChangeCriterion = (FoldChangeCriterion) criterion;
            return new FoldChangeCriterionWrapper(study, foldChangeCriterion, row);
        } else if (criterion instanceof GeneNameCriterion) {
            GeneNameCriterion geneNameCriterion = (GeneNameCriterion) criterion;
            return new GeneNameCriterionWrapper(geneNameCriterion, row);
        } else {
            throw new IllegalArgumentException("Illegal criterion type " + criterion.getClass());
        }
    }
    
    static AbstractCriterionWrapper createIdentifierCriterionWrapper(AbstractCriterion criterion, 
            IdentifierCriterionRow row) {
        return new IdentifierCriterionWrapper((IdentifierCriterion) criterion, row);
    }
    
    static AbstractCriterionWrapper createSavedListCriterionWrapper(AbstractCriterion criterion, 
            SavedListCriterionRow row, StudySubscription subscription, String fieldName) {
        return new SubjectListCriterionWrapper((SubjectListCriterion) criterion, subscription, fieldName, row);
    }

}
