/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class ViewExternalLinksActionTest extends AbstractSessionBasedTest {

    private ViewExternalLinksAction action;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        action = new ViewExternalLinksAction();
        action.setWorkspaceService(workspaceService);
    }

    @Test
    public void testPrepare() {
        action.prepare();
        verify(workspaceService, never()).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
        action.getExternalLinkList().setId(1l);
        action.prepare();
        verify(workspaceService, times(1)).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());

    }

}
