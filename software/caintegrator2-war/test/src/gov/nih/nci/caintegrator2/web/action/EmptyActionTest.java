/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class EmptyActionTest {
    
    @Test
    public void testEmptyActionpage() throws Exception {
        assertEquals(ActionSupport.SUCCESS, new EmptyAction().execute());
    }

}
