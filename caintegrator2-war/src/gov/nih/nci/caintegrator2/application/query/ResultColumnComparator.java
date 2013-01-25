/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.ResultColumn;

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
