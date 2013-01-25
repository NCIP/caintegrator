/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * This is a Displaytag Decorator for the Div columns, which will insert s:div tag around the value which is
 * what will get returned after it is "decorated".
 * This was the most practical way to allow for exporting the value properly, as our design of using
 * Struts2 + displaytag + dynamic columns was not a well documented approach.
 */
public class DivFormatColumnDecorator implements DisplaytagColumnDecorator {
    
    /**
     * {@inheritDoc}
     */
    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
        String value = (String) columnValue;
        value = value.replaceAll("\r\n", "");
        value = value.trim();
        return "<div name=\"truncateDiv\">" + value + "</div>";
    }
        
}
