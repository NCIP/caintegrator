/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.HeatmapAjaxUpdater;

import org.apache.struts2.ServletActionContext;
import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;


public class HeatmapAjaxUpdaterTest extends AbstractSessionBasedTest {

    private HeatmapAjaxUpdater updater;
    private MockHttpSession session;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new HeatmapAjaxUpdater();
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
        HeatmapParameters parameters = new HeatmapParameters();
        parameters.setStudySubscription(subscription);
        parameters.setQuery(query);
        parameters.setSessionId("sessionId");
        SessionHelper.getInstance().getDisplayableUserWorkspace().setHeatmapParameters(parameters);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
    }

    @Test
    public void testRunIgvFromQuery() throws Exception {
        updater.runViewer();
        Thread.sleep(2000);
        verify(analysisService, times(1)).executeHeatmap(any(HeatmapParameters.class));
        SessionHelper.getInstance().getDisplayableUserWorkspace().getHeatmapParameters().setQuery(null);
        updater.runViewer();
        Thread.sleep(2000);
        verify(analysisService, times(1)).executeHeatmap(any(HeatmapParameters.class));
    }

}
