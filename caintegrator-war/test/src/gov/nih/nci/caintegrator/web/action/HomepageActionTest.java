/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.web.action.HomepageAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class HomepageActionTest {
    private HomepageAction action;

    @Before
    public void setUp() {
        action = new HomepageAction();
        action.setStudynav("nav");
    }

    @Test
    public void execute() {
        assertEquals(Action.SUCCESS, action.execute());
        assertEquals("nav", action.getStudynav());
    }

    @Test
    public void getStudyDetails() {
        assertEquals(Action.SUCCESS, action.getStudyDetails());
        assertEquals("nav", action.getStudynav());
    }
}
