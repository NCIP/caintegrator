/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultValue;

import org.junit.Test;

public class DisplayableResultValueTest {


    @Test
    public void testDisplayableResultValueResultValue() {
        ResultValue resultValue = new ResultValue();
        ResultColumn column = new ResultColumn();
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        column.setAnnotationFieldDescriptor(afd);
        resultValue.setColumn(column);


        AnnotationDefinition stringDefinition = new AnnotationDefinition();
        stringDefinition.setDataType(AnnotationTypeEnum.STRING);
        afd.setDefinition(stringDefinition);
        StringAnnotationValue urlStringValue = new StringAnnotationValue();
        urlStringValue.setStringValue("htTp://someURL.com");
        resultValue.setValue(urlStringValue);

        DisplayableResultValue value = new DisplayableResultValue(resultValue);
        assertTrue(value.isUrlType());

        urlStringValue.setStringValue("htTpsomeInvalidURL.com");
        value = new DisplayableResultValue(resultValue);
        assertFalse(value.isUrlType());

        urlStringValue.setStringValue("ftp://someInvalidURL.com");
        value = new DisplayableResultValue(resultValue);
        assertTrue(value.isUrlType());
    }
}
