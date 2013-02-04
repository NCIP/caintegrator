/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * This is a Displaytag Decorator for the Date columns, which will come in in the format "yyyy-mm-dd", and the 
 * purpose is for it to be displayed in the format MM/dd/yyyy which is what will get returned after it is "decorated".
 * This was the most practical way to allow for sorting dates properly, as our design of using Struts2 + displaytag +
 * dynamic columns was not a well documented approach.
 */
public class DateFormatColumnDecorator implements DisplaytagColumnDecorator {
    
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})[\\/-](\\d{2})[\\/-](\\d{2})");
    private static final String DATE_DELIMITER = "/";
    /**
     * {@inheritDoc}
     */
    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
        Matcher matcher = DATE_PATTERN.matcher(columnValue.toString());
        if (matcher.find()) {
            return matcher.group(2) + DATE_DELIMITER + matcher.group(3) + DATE_DELIMITER + matcher.group(1);
        }
        return columnValue;
    }

}
