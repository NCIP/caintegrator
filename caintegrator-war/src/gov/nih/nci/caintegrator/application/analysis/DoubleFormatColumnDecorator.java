/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import java.text.DecimalFormat;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * This is a Displaytag Decorator for the Double columns with 2 decimal points.
 */
public class DoubleFormatColumnDecorator implements DisplaytagColumnDecorator {
    
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#0.0000");
    /**
     * {@inheritDoc}
     */
    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
        return DOUBLE_FORMAT.format(Double.parseDouble((String) columnValue)).toString();
    }

}
