/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.apache.struts2.ServletActionContext;
import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;


public class IGVAjaxUpdaterTest extends AbstractSessionBasedTest {

    private IGVAjaxUpdater updater;
    private MockHttpSession session;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new IGVAjaxUpdater();
        updater.setAnalysisService(analysisService);
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        Query query = new Query();
        query.setSubscription(subscription);
        MockHttpServletRequest request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        ServletActionContext.setRequest(request);
        IGVParameters parameters = new IGVParameters();
        parameters.setStudySubscription(subscription);
        parameters.setQuery(query);
        parameters.setSessionId("sessionId");
        SessionHelper.getInstance().getDisplayableUserWorkspace().setIgvParameters(parameters);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
    }

    @Test
    public void testRunIgvFromQuery() throws Exception {
        updater.runViewer();
        Thread.sleep(2000);
        verify(analysisService, times(1)).executeIGV(any(IGVParameters.class));
        SessionHelper.getInstance().getDisplayableUserWorkspace().getIgvParameters().setQuery(null);
        updater.runViewer();
        Thread.sleep(2000);
        verify(analysisService, times(2)).executeIGV(any(IGVParameters.class));
    }

}
