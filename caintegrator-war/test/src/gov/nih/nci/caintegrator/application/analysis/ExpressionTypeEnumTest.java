/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.analysis.ExpressionTypeEnum;

import org.junit.Test;

public class ExpressionTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ExpressionTypeEnum.EXPRESSION_LEVEL, ExpressionTypeEnum.getByValue(ExpressionTypeEnum.EXPRESSION_LEVEL.getValue()));
    }

}
