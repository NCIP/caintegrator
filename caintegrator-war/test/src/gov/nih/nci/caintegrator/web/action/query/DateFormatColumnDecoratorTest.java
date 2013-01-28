/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.web.action.query.DateFormatColumnDecorator;

import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.junit.Test;

/**
 * 
 */
public class DateFormatColumnDecoratorTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator.web.action.query.DateFormatColumnDecorator#decorate(java.lang.Object, javax.servlet.jsp.PageContext, org.displaytag.properties.MediaTypeEnum)}.
     * @throws DecoratorException 
     */
    @Test
    public void testDecorate() throws DecoratorException {
        String columnValue = "2001-01-01 extraStuff";
        DateFormatColumnDecorator decorator = new DateFormatColumnDecorator();
        assertEquals("01/01/2001", (String) decorator.decorate((Object)columnValue, null, MediaTypeEnum.HTML));
        columnValue = "invalid";
        assertEquals("invalid", (String) decorator.decorate((Object)columnValue, null, MediaTypeEnum.HTML));
    }

}
