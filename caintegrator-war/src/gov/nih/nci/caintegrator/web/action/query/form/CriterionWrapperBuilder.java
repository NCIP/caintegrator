/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator.domain.translational.Study;

/**
 *
 */
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
        } else if (criterion instanceof ExpressionLevelCriterion) {
            return new ExpressionLevelCriterionWrapper((ExpressionLevelCriterion) criterion, row);
        } else {
            throw new IllegalArgumentException("Illegal criterion type " + criterion.getClass());
        }
    }

    static AbstractGenomicCriterionWrapper createGenomicCriterionWrapper(AbstractCriterion criterion,
            CopyNumberCriterionRow row, Study study) {
        if (criterion instanceof GeneNameCriterion) {
            GeneNameCriterion geneNameCriterion = (GeneNameCriterion) criterion;
            return new GeneNameCriterionWrapper(geneNameCriterion, row);
        } else if (criterion instanceof CopyNumberAlterationCriterion) {
            if (CopyNumberCriterionTypeEnum.SEGMENT_VALUE.equals(
                    ((CopyNumberAlterationCriterion) criterion).getCopyNumberCriterionType())) {
                return new SegmentCriterionWrapper((CopyNumberAlterationCriterion) criterion, row);
            }
            return new CallsValueCriterionWrapper((CopyNumberAlterationCriterion) criterion, row);
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
