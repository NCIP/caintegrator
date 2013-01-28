/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.data;

import gov.nih.nci.caintegrator.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator.domain.application.StringComparisonCriterion;

import org.hibernate.criterion.Criterion;

/**
 * Abstract class that handles translating caIntegrator2 Criterion into Hibernate Criterion.
 */
public abstract class AbstractAnnotationCriterionHandler {

    /**
     * Numeric Value.
     */
    public static final String NUMERIC_VALUE_COLUMN = "numericValue";
    /**
     * String Value.
     */
    public static final String STRING_VALUE_COLUMN = "stringValue";
    /**
     * Date Value.
     */
    public static final String DATE_VALUE_COLUMN = "dateValue";

    /**
     * Creates the appropriate Annotation Criterion Handler based on the criterion object.
     * @param criterion is the criterion of interest to be translated.
     * @return appropriate handler to return.
     */
    static AbstractAnnotationCriterionHandler create(AbstractAnnotationCriterion criterion) {
        if (criterion instanceof IdentifierCriterion) {
            return new IdentifierCriterionHandler((IdentifierCriterion) criterion);
        } else if (criterion instanceof StringComparisonCriterion) {
            return new StringComparisonCriterionHandler((StringComparisonCriterion) criterion);
        } else if (criterion instanceof NumericComparisonCriterion) {
            return new NumericComparisonCriterionHandler((NumericComparisonCriterion) criterion);
        } else if (criterion instanceof DateComparisonCriterion) {
            return new DateComparisonCriterionHandler((DateComparisonCriterion) criterion);
        } else if (criterion instanceof SelectedValueCriterion) {
            return new SelectedValueCriterionHandler((SelectedValueCriterion) criterion);
        } else {
            throw new IllegalStateException("Unknown AbstractAnnotationCriterion: " + criterion.getClass().toString());
        }
    }

    /**
     * Subclasses must create translated Criterion.
     * @return Hibernate Criterion object that is translated.
     */
    abstract Criterion translate();

}
