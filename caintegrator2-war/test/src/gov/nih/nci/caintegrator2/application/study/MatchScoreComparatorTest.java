/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MatchScoreComparatorTest {

    @Test
    public void testCompare() {
        List<String> keywords = new ArrayList<String>();
        keywords.add("test1");
        keywords.add("test2");
        keywords.add("test3");
        
        AnnotationDefinition ad1 = new AnnotationDefinition();
        AnnotationDefinition ad2 = new AnnotationDefinition();
        ad1.setKeywords("test1");
        ad2.setKeywords("test2 test3");
        
        
        MatchScoreComparator msc = new MatchScoreComparator(keywords);
        
        assertTrue(msc.compare(ad1, ad2) > 0 );
        assertTrue(msc.compare(ad2, ad1) < 0 );
        
       
    }

}
