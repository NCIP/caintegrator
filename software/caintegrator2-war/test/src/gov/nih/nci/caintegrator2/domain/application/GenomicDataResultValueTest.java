/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.query.GenomicCriteriaMatchTypeEnum;

import org.junit.Test;

public class GenomicDataResultValueTest {


    @Test
    public void testGetHighlightColor() {
        GenomicDataResultValue value = new GenomicDataResultValue();
        value.setCriteriaMatchType(GenomicCriteriaMatchTypeEnum.OVER);
        value.setValue(1f);
        assertEquals("#CC3333", value.getHighlightColor());
        value.setCriteriaMatchType(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE);
        assertEquals("#CC3333", value.getHighlightColor());
        
        value.setValue(-1f);
        assertEquals("#0066CC", value.getHighlightColor());
        value.setCriteriaMatchType(GenomicCriteriaMatchTypeEnum.UNDER);
        assertEquals("#0066CC", value.getHighlightColor());
    }

}
