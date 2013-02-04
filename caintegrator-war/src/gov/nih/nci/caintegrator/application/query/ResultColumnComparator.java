/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.domain.application.ResultColumn;

import java.util.Comparator;

/**
 * 
 */
class ResultColumnComparator implements Comparator<ResultColumn> {

    /**
     * {@inheritDoc}
     */
    public int compare(ResultColumn o1, ResultColumn o2) {
        return o1.getSortOrder() <= o2.getSortOrder() ? -1 : 1;
    }

}
