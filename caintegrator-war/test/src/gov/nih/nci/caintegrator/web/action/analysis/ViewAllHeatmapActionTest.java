/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.ViewAllHeatmapAction;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;


/**
 *
 */
public class ViewAllHeatmapActionTest extends AbstractSessionBasedTest {
    private ViewAllHeatmapAction viewAllHeatmapAction = new ViewAllHeatmapAction();
    private StudySubscription subscription;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        viewAllHeatmapAction.setWorkspaceService(workspaceService);
        viewAllHeatmapAction.setQueryManagementService(queryManagementService);
        viewAllHeatmapAction.setArrayDataService(arrayDataService);
        setStudySubscription(subscription);
    }

    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        return study;
    }

    @Test
    public void testValidate() {

        // Test Prepare
        viewAllHeatmapAction.prepare();
        verify(queryManagementService, times(1)).retrieveCopyNumberPlatformsForStudy(any(Study.class));

        // Test validate
        viewAllHeatmapAction.validate();
        assertFalse(viewAllHeatmapAction.hasActionErrors());
        viewAllHeatmapAction.setSelectedAction("viewAll");
        viewAllHeatmapAction.validate();
        assertTrue(viewAllHeatmapAction.hasActionErrors());
        viewAllHeatmapAction.clearActionErrors();
        viewAllHeatmapAction.setCopyNumberPlatformName("hg17");
        viewAllHeatmapAction.validate();
        assertFalse(viewAllHeatmapAction.hasActionErrors());
    }

    @Test
    public void testExecute() {

        // Test Prepare
        viewAllHeatmapAction.prepare();
        verify(queryManagementService, times(1)).retrieveCopyNumberPlatformsForStudy(any(Study.class));

        // Test execute
        assertEquals("success", viewAllHeatmapAction.execute());
        viewAllHeatmapAction.setSelectedAction("cancel");
        assertEquals("homePage", viewAllHeatmapAction.execute());

        //Test get platform option
        assertEquals("-- None Available --", viewAllHeatmapAction.getCopyNumberPlatformOption());
        Set<String> platforms = new HashSet<String>();
        platforms.add("Platform 1");
        viewAllHeatmapAction.setCopyNumberPlatformsInStudy(platforms);
        assertEquals("", viewAllHeatmapAction.getCopyNumberPlatformOption());

    }
}
