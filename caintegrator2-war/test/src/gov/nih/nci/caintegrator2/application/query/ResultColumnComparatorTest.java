/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


public class ResultColumnComparatorTest {

    @Test
    public void testCompare() {
        ResultColumn col1 = new ResultColumn();
        ResultColumn col2 = new ResultColumn();
        col1.setSortOrder(2);
        col1.setId(Long.valueOf(1));
        col2.setSortOrder(1);
        col2.setId(Long.valueOf(2));
        List<ResultColumn> columns = new ArrayList<ResultColumn>();
        columns.add(col1);
        columns.add(col2);
        
        Collections.sort(columns, new ResultColumnComparator());
        
        // Make sure they flip flopped
        assertEquals(col2.getId(), columns.get(0).getId());
        assertEquals(col1.getId(), columns.get(1).getId());
        
    }

}
