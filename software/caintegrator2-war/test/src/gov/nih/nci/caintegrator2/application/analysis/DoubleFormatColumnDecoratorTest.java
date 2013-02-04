/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import static org.junit.Assert.assertEquals;

import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.junit.Test;

/**
 * 
 */
public class DoubleFormatColumnDecoratorTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.query.DateFormatColumnDecorator#decorate(java.lang.Object, javax.servlet.jsp.PageContext, org.displaytag.properties.MediaTypeEnum)}.
     * @throws DecoratorException 
     */
    @Test
    public void testDecorate() throws DecoratorException {
        String columnValue = "0.12345678";
        DoubleFormatColumnDecorator decorator = new DoubleFormatColumnDecorator();
        assertEquals("0.1235", (String) decorator.decorate((Object)columnValue, null, MediaTypeEnum.HTML));
    }

}
