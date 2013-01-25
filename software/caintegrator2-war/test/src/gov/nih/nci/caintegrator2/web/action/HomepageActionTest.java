/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import static org.junit.Assert.*;

import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class HomepageActionTest {
    
    @Test
    public void testOpenHomepage() {
        assertEquals(ActionSupport.SUCCESS, new HomepageAction().execute());
    }

}
