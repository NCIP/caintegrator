/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package gov.nih.nci.caintegrator.domain.application;

/**
 * Base class for annotation criteria that compare values.
 */
public abstract class AbstractComparisonCriterion extends AbstractAnnotationCriterion {

    /**
     * Gets the value that this criterion compares to.
     * @return the value as a string
     */
    public abstract String getStringValue();


}
