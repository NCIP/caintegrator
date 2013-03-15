/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import static org.junit.Assert.assertEquals;

import gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Test sorting numeric / non-numeric displaytag values.
 */
public class NumericColumnDisplayTagComparatorTest {

    @Test
    public void testCompare() {
        Object object1 = (Object) "100";
        Object object2 = (Object) "String-101";
        Object object3 = (Object) "1";
        Object object4 = (Object) "3";
        Object object5 = (Object) "Cell[staticValue=\r\n 0.2 \r\n]";
        Object object6 = (Object) "Cell[staticValue=\r\n \r\n]";
        List<Object> objects = new ArrayList<Object>();
        objects.add(object1);
        objects.add(object2);
        objects.add(object3);
        objects.add(object4);
        objects.add(object5);
        objects.add(object6);
        Collections.sort(objects, new NumericColumnDisplayTagComparator());
        assertEquals(object6, objects.get(0));
        assertEquals(object5, objects.get(1));
        assertEquals(object3, objects.get(2));
        assertEquals(object4, objects.get(3));
        assertEquals(object1, objects.get(4));
        assertEquals(object2, objects.get(5));
    }

}
