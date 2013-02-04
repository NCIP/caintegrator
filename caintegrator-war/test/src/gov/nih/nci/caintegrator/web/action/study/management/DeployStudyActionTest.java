/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.domain.application.GeneList;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.DeployStudyAction;
import gov.nih.nci.caintegrator.web.ajax.IStudyDeploymentAjaxUpdater;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.opensymphony.xwork2.Action;

public class DeployStudyActionTest extends AbstractSessionBasedTest {

    private DeployStudyAction action;
    private IStudyDeploymentAjaxUpdater ajaxUpdater;
    private SessionHelper sessionHelper;
    private MockHttpSession session;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        ajaxUpdater = mock(IStudyDeploymentAjaxUpdater.class);

        action = new DeployStudyAction();
        action.setAjaxUpdater(ajaxUpdater);
        action.setDeploymentService(deploymentService);
        action.setWorkspaceService(workspaceService);
        action.setStudyManagementService(new StudyManagementServiceStub());
        setupSession();
    }

    private void setupSession() {
        sessionHelper = SessionHelper.getInstance();
        action.prepare();

        sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().clear();
        sessionHelper.getDisplayableUserWorkspace().setCurrentStudySubscription(createStudySubscription(1L));

        MockHttpServletRequest request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        ServletActionContext.setRequest(request);
    }

    private StudySubscription createStudySubscription(long id) {
        StudySubscription studySubscription = new StudySubscription();
        addGeneList(studySubscription);
        addSubjectList(studySubscription);
        Study study = new Study();
        study.setId(1l);
        AnnotationGroup group = new AnnotationGroup();
        group.setName("group");
        study.getAnnotationGroups().add(group);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        studySubscription.setStudy(study);
        studySubscription.setId(id);
        sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().add(studySubscription);
        setStudySubscription(studySubscription);
        return studySubscription;
    }

    private void addGeneList(StudySubscription studySubscription) {
        GeneList geneList = new GeneList();
        geneList.setName("GeneList1");
        Gene gene = new Gene();
        gene.setSymbol("EGFR");
        geneList.getGeneCollection().add(gene);
        studySubscription.getListCollection().add(geneList);
    }

    private void addSubjectList(StudySubscription studySubscription) {
        SubjectList subjectList = new SubjectList();
        subjectList.setName("SubjectList1");
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier("100");
        subjectList.getSubjectIdentifiers().add(subjectIdentifier);
        studySubscription.getListCollection().add(subjectList);
    }

    @Test
    public void testExecute() {
        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath(anyString())).thenReturn(TestDataFiles.VALID_FILE.getAbsolutePath());

        action.setServletContext(context);
        assertEquals(Action.SUCCESS, action.execute());
        verify(ajaxUpdater, times(1)).runJob(any(StudyConfiguration.class), any(HeatmapParameters.class));
        verify(deploymentService, times(1)).prepareForDeployment(any(StudyConfiguration.class));
    }
}
