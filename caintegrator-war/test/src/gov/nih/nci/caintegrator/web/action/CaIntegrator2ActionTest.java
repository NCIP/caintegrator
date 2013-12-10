/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

public class CaIntegrator2ActionTest extends AbstractSessionBasedTest {
    private AbstractCaIntegrator2Action action;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        action = new AbstractCaIntegrator2ActionStub();
        action.setWorkspaceService(workspaceService);
    }

    @Test
    public void testPrepare() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertNull(action.getWorkspace());
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        action.prepare();
        assertNotNull(action.getWorkspace());
    }

    @Test
    public void testGetters() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertNull(action.getDisplayableWorkspace());
        assertNull(action.getStudySubscription());
        assertNull(action.getStudy());
        assertEquals(action.getStudy(), action.getCurrentStudy());
        assertNull(action.getQueryResult());
        assertNull(action.getGenomicDataQueryResult());
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        action.prepare();
        assertNotNull(action.getDisplayableWorkspace());
        assertNotNull(action.getStudySubscription());
        assertNotNull(action.getStudy());
        GenomicDataQueryResult genomicDataQueryResult = new GenomicDataQueryResult();
        action.setGenomicDataQueryResult(genomicDataQueryResult);
        assertEquals(genomicDataQueryResult, action.getGenomicDataQueryResult());
        action.setStudySubscription(null);
        action.prepare();
        assertNull(action.getQueryResult());
        assertNull(action.getGenomicDataQueryResult());
    }

    private static class AbstractCaIntegrator2ActionStub extends AbstractCaIntegrator2Action {
        private static final long serialVersionUID = 1L;
    }
}
