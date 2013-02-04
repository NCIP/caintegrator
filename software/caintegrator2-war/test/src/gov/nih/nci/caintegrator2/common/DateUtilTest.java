/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;


/**
 * 
 */
public class DateUtilTest {

    @Test
    public void testCreateDate() throws ParseException {
        Date date = DateUtil.createDate("01/02/1970");
        assertTrue("01/02/1970".equalsIgnoreCase(DateUtil.toString(date)));
        assertTrue("1970/01/02".equalsIgnoreCase(DateUtil.toStringForComparison(date)));
        
        date = DateUtil.createDate("1/2/1970");
        assertTrue("01/02/1970".equalsIgnoreCase(DateUtil.toString(date)));
        assertTrue("1970/01/02".equalsIgnoreCase(DateUtil.toStringForComparison(date)));
        
        date = DateUtil.createDate("1-2-1970");
        assertTrue("01/02/1970".equalsIgnoreCase(DateUtil.toString(date)));
        assertTrue("1970/01/02".equalsIgnoreCase(DateUtil.toStringForComparison(date)));
        
        date = DateUtil.createDate("");
        assertTrue("".equalsIgnoreCase(DateUtil.toString(date)));
        assertTrue("".equalsIgnoreCase(DateUtil.toStringForComparison(date)));
        
        date = DateUtil.createDate(null);
        assertTrue("".equalsIgnoreCase(DateUtil.toString(date)));
        assertTrue("".equalsIgnoreCase(DateUtil.toStringForComparison(date)));
    }
    
    @Test
    public void testToString() throws ParseException {
        List<String> stringDates = new ArrayList<String>();
        stringDates.add("9/5/2000");
        stringDates.add("08/15/2001");
        stringDates = DateUtil.toString(stringDates);
        assertEquals("09/05/2000", stringDates.get(0));
        assertEquals("08/15/2001", stringDates.get(1));
    }
    
    @Test
    public void testCompareDatesInMinutes() {
        Date date1 = new Date(1000);
        Date date2 = new Date(125000);
        assertEquals(Long.valueOf(2), DateUtil.compareDatesInMinutes(date1, date2));
    }

}
