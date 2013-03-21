/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Tests result row sorting via comparator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class ResultColumnComparatorTest {

    /**
     * Tests comparison via comparator.
     */
    @Test
    public void compare() {
        ResultColumn columnOne = new ResultColumn();
        columnOne.setSortOrder(2);
        columnOne.setId(1L);

        ResultColumn columnTwo = new ResultColumn();
        columnTwo.setSortOrder(1);
        columnTwo.setId(2L);

        List<ResultColumn> columns = Lists.newArrayList(columnOne, columnTwo);
        Collections.sort(columns, new ResultColumnComparator());

        // Make sure they flip flopped
        assertEquals(columnTwo.getId(), columns.get(0).getId());
        assertEquals(columnOne.getId(), columns.get(1).getId());
    }
}
