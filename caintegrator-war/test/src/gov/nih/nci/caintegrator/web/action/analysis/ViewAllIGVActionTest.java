/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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
import gov.nih.nci.caintegrator.web.action.analysis.ViewAllIGVAction;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;


/**
 *
 */
public class ViewAllIGVActionTest extends AbstractSessionBasedTest {

    private ViewAllIGVAction viewAllIGVAction = new ViewAllIGVAction();
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
        viewAllIGVAction.setWorkspaceService(workspaceService);
        viewAllIGVAction.setQueryManagementService(queryManagementService);
        viewAllIGVAction.setArrayDataService(arrayDataService);
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
        viewAllIGVAction.prepare();
        verify(queryManagementService, times(1)).retrieveCopyNumberPlatformsForStudy(any(Study.class));
        verify(queryManagementService, times(1)).retrieveGeneExpressionPlatformsForStudy(any(Study.class));

        // Test validate
        viewAllIGVAction.validate();
        assertFalse(viewAllIGVAction.hasActionErrors());
        viewAllIGVAction.setSelectedAction("viewAll");
        viewAllIGVAction.setExpressionPlatformName("hg18");
        viewAllIGVAction.validate();
        assertFalse(viewAllIGVAction.hasActionErrors());
        viewAllIGVAction.setCopyNumberPlatformName("hg17");
        viewAllIGVAction.validate();
        assertTrue(viewAllIGVAction.hasActionErrors());
    }

    @Test
    public void testExecute() {

        // Test Prepare
        viewAllIGVAction.prepare();
        verify(queryManagementService, times(1)).retrieveCopyNumberPlatformsForStudy(any(Study.class));
        verify(queryManagementService, times(1)).retrieveGeneExpressionPlatformsForStudy(any(Study.class));

        // Test execute
        assertEquals("success", viewAllIGVAction.execute());
        viewAllIGVAction.setSelectedAction("cancel");
        assertEquals("homePage", viewAllIGVAction.execute());

        //Test get platform option
        assertEquals("-- None Available --", viewAllIGVAction.getExpressionPlatformOption());
        assertEquals("-- None Available --", viewAllIGVAction.getCopyNumberPlatformOption());
        Set<String> platforms = new HashSet<String>();
        platforms.add("Platform 1");
        viewAllIGVAction.setExpressionPlatformsInStudy(platforms);
        viewAllIGVAction.setCopyNumberPlatformsInStudy(platforms);
        assertEquals("", viewAllIGVAction.getExpressionPlatformOption());
        assertEquals("", viewAllIGVAction.getCopyNumberPlatformOption());

    }
}
