/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IStudyDeploymentAjaxUpdater;

import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

import com.opensymphony.xwork2.Action;

public class DeployStudyActionTest extends AbstractSessionBasedTest {

    private DeployStudyAction action;
    private DeploymentServiceStub deploymentServiceStub;
    private DeployStudyAjaxUpdaterStub ajaxUpdaterStub;
    private final WorkspaceServiceStub workspaceSerivce = new WorkspaceServiceStub();
    private SessionHelper sessionHelper;
    private MockHttpSession session;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class);
        action = (DeployStudyAction) context.getBean("deployStudyAction");
        deploymentServiceStub = (DeploymentServiceStub) context.getBean("deploymentService");
        ajaxUpdaterStub = new DeployStudyAjaxUpdaterStub();
        action.setAjaxUpdater(ajaxUpdaterStub);
        setupSession();
    }

    private void setupSession() {
        super.setUp();
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
        workspaceSerivce.setSubscription(studySubscription);
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
        action.setServletContext(new ServletContextStub());
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(ajaxUpdaterStub.runJobCalled);
        assertTrue(deploymentServiceStub.prepareForDeploymentCalled);
    }

    private static class DeployStudyAjaxUpdaterStub implements IStudyDeploymentAjaxUpdater {

        public boolean runJobCalled = false;

        @Override
        public void initializeJsp() {
        }

        @Override
        public void runJob(StudyConfiguration configuration, HeatmapParameters heatmapParameters) {
            runJobCalled = true;
        }

    }

    private class ServletContextStub extends MockServletContext {

        @Override
        public String getRealPath(String path) {
            return TestDataFiles.VALID_FILE.getAbsolutePath();
        }
    }

}
