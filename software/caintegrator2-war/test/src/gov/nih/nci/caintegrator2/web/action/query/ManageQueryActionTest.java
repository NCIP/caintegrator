/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

import com.opensymphony.xwork2.Action;

public class ManageQueryActionTest extends AbstractSessionBasedTest {

    private ManageQueryAction manageQueryAction;
    private SessionHelper sessionHelper;
    private MockHttpSession session;

    // Study objects
    private final QueryManagementServiceStub queryManagementService = new QueryManagementServiceStub();
    private final StudyManagementServiceStub studyManagementService = new StudyManagementServiceStub();
    private final WorkspaceServiceStub workspaceSerivce = new WorkspaceServiceStub();

    @Override
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("query-management-action-test-config.xml", ManageQueryActionTest.class);
        manageQueryAction = (ManageQueryAction) context.getBean("manageQueryAction");
        manageQueryAction.setQueryManagementService(queryManagementService);
        manageQueryAction.setStudyManagementService(studyManagementService);
        manageQueryAction.setWorkspaceService(workspaceSerivce);
        setupSession();
    }

    private void setupSession() {
        super.setUp();
        sessionHelper = SessionHelper.getInstance();
        manageQueryAction.prepare();
        assertEquals("criteria", manageQueryAction.getDisplayTab());

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

        // test create new query
        manageQueryAction.setSelectedAction("createNewQuery");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test selectedTabSearchResults
        manageQueryAction.setSelectedAction("selectedTabSearchResults");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test execute query
        manageQueryAction.setSelectedAction("executeQuery");
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.executeCalled);
        manageQueryAction.getQueryForm().getResultConfiguration().setResultType(ResultTypeEnum.GENE_EXPRESSION.getValue());
        manageQueryAction.getQueryForm().getResultConfiguration().setCopyNumberType(CopyNumberCriterionTypeEnum.SEGMENT_VALUE.getValue());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.executeGenomicDataQueryCalled);

        // test load & execute my gene list
        queryManagementService.executeGenomicDataQueryCalled = false;
        manageQueryAction.setSelectedAction("loadGeneListExecute");
        manageQueryAction.setGeneListName("GeneList1");
        assertEquals(Action.ERROR, manageQueryAction.execute());
        assertFalse(queryManagementService.executeGenomicDataQueryCalled);
        assertEquals("criteria", manageQueryAction.getDisplayTab());

        manageQueryAction.getStudyConfiguration().getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.executeGenomicDataQueryCalled);
        assertEquals("searchResults", manageQueryAction.getDisplayTab());
        manageQueryAction.getStudyConfiguration().getGenomicDataSources().clear();
        // Clean up after testing
        manageQueryAction.setGeneListName("");
        manageQueryAction.setSelectedAction("remove");
        manageQueryAction.setRowNumber("0");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test load & execute my subject list
        manageQueryAction.setSelectedAction("loadSubjectListExecute");
        manageQueryAction.setSubjectListName("SubjectList1");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.executeCalled);
        assertEquals("searchResults", manageQueryAction.getDisplayTab());

        // test selectAll
        manageQueryAction.setSelectedAction("selectAll");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test selectAll
        manageQueryAction.setSelectedAction("selectAllSubject");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test selectAll
        manageQueryAction.setSelectedAction("selectNoneSubject");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // Clean up after testing
        manageQueryAction.setSubjectListName("");
        manageQueryAction.setSelectedAction("remove");
        manageQueryAction.setRowNumber("0");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test selectSearchTab
        manageQueryAction.setSelectedAction("selectedTabSearchResults");
        manageQueryAction.prepare();
        assertEquals("searchResults", manageQueryAction.getDisplayTab());

        // test save query
        manageQueryAction.setSelectedAction("saveQuery");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertTrue(manageQueryAction.hasErrors());
        manageQueryAction.clearErrorsAndMessages();
        manageQueryAction.getCurrentStudy().getStudyConfiguration().setStatus(Status.DEPLOYED);
        manageQueryAction.getQueryForm().getQuery().setName("query name");
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        manageQueryAction.setSelectedAction("selectedTabSearchResults");
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        manageQueryAction.setSelectedAction("saveQuery");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.saveCalled);
        queryManagementService.saveCalled = false;

        // test save as query
        manageQueryAction.setSelectedAction("saveAsQuery");
        manageQueryAction.prepare();
        manageQueryAction.getQueryForm().getQuery().setId(1L);
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.saveCalled);

        // test save subject
        manageQueryAction.setSelectedAction("saveSubjectList");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertTrue(manageQueryAction.hasErrors());
        manageQueryAction.clearErrorsAndMessages();
        manageQueryAction.getCurrentStudy().getStudyConfiguration().setStatus(Status.DEPLOYED);
        manageQueryAction.getQueryForm().getQuery().setName("query name");
        manageQueryAction.validate();
        assertTrue(manageQueryAction.hasErrors());
        manageQueryAction.clearErrorsAndMessages();
        manageQueryAction.setSubjectListName("Subject list 1");
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        manageQueryAction.setSubjectListVisibleToOthers(true);
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test load query
        manageQueryAction.setSelectedAction("loadQuery");
        manageQueryAction.setQueryId(1L);
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.executeCalled);
        manageQueryAction.getQueryForm().getResultConfiguration().setResultType(ResultTypeEnum.GENE_EXPRESSION.getValue());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertEquals("criteria", manageQueryAction.getDisplayTab());
        assertTrue(queryManagementService.executeGenomicDataQueryCalled);

        // test load & execute query
        manageQueryAction.setSelectedAction("loadExecute");
        manageQueryAction.setQueryId(1L);
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.executeCalled);
        manageQueryAction.getQueryForm().getResultConfiguration().setResultType(ResultTypeEnum.GENE_EXPRESSION.getValue());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertEquals("searchResults", manageQueryAction.getDisplayTab());
        assertTrue(queryManagementService.executeGenomicDataQueryCalled);

        // test - addition of criteria rows
        manageQueryAction.setSelectedAction("addCriterionRow");
        manageQueryAction.getQueryForm().getCriteriaGroup().setCriterionTypeName("group");
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertEquals(1, manageQueryAction.getQueryForm().getCriteriaGroup().getRows().size());

        // test removal of row
        manageQueryAction.setSelectedAction("remove");
        manageQueryAction.setRowNumber("0");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertEquals(0, manageQueryAction.getQueryForm().getCriteriaGroup().getRows().size());

        // test delete query
        manageQueryAction.setSelectedAction("deleteQuery");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        manageQueryAction.getQueryForm().getQuery().setName("query name");
        manageQueryAction.getQueryForm().getQuery().setId(1L);
        manageQueryAction.validate();
        assertFalse(manageQueryAction.hasErrors());
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertTrue(queryManagementService.deleteCalled);


        // test for invalid action
        manageQueryAction.setSelectedAction("invalid");
        assertEquals(Action.ERROR, manageQueryAction.execute());

        assertFalse(manageQueryAction.acceptableParameterName("d-12345-e"));
        assertFalse(manageQueryAction.acceptableParameterName("d-12345-p"));
        assertFalse(manageQueryAction.acceptableParameterName("12345f678"));
        assertTrue(manageQueryAction.acceptableParameterName("NewQuery"));

        // test viewer
        manageQueryAction.setSelectedAction("viewIGV");
        assertEquals("viewIGV", manageQueryAction.execute());
        manageQueryAction.setSelectedAction("viewHeatmap");
        manageQueryAction.setServletContext(new ServletContextStub());
        assertEquals("viewHeatmap", manageQueryAction.execute());

    }

    @Test
    public void testPostQueryResultExecuteMethods() {
        // test create new query
        manageQueryAction.setSelectedAction("createNewQuery");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        // Test creating NCIA basket
        manageQueryAction.setSelectedAction("forwardToNcia");
        assertEquals("nciaBasket", manageQueryAction.execute());
        DisplayableUserWorkspace displayableUserWorkspace = sessionHelper.getDisplayableUserWorkspace();
        DisplayableQueryResult displayableQueryResult = DisplayableQueryResultTest.getTestResult();
        displayableUserWorkspace.setQueryResult(displayableQueryResult);
        manageQueryAction.prepare();
        manageQueryAction.validate();

        assertEquals("nciaBasket", manageQueryAction.execute());

        assertEquals(null + "/externalDataBasketDisplay.jsf", manageQueryAction.getNciaBasketUrl());

        // Test retrieve DICOM images
        manageQueryAction.setSelectedAction("retrieveDicomImages");
        assertEquals("dicomJobAjax", manageQueryAction.execute());
        assertTrue(studyManagementService.retrieveImageDataSourceCalled);
        // If we call it again, it should already be on the session so it should error out.
        assertEquals("dicomJobCurrentlyRunning", manageQueryAction.execute());

        // Test select all on checkboxes
        manageQueryAction.setSelectedAction("selectAll");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        // Default is that all rows are checked
        for (DisplayableResultRow row : displayableUserWorkspace.getQueryResult().getRows()) {
            assertTrue(row.isCheckedRow());
        }
        manageQueryAction.setSelectedAction("selectNone");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        // All rows should now be unchecked
        for (DisplayableResultRow row : displayableUserWorkspace.getQueryResult().getRows()) {
            assertFalse(row.isCheckedRow());
        }

        // Test the page size switch.
        manageQueryAction.setPageSize(20);
        manageQueryAction.setSelectedAction("updateResultsPerPage");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        assertEquals(20, sessionHelper.getDisplayableUserWorkspace().getQueryResult().getPageSize());

        // Test exporting genomic results.
        manageQueryAction.setSelectedAction("exportGenomicResults");
        assertEquals("exportGenomicResults", manageQueryAction.execute());
        assertTrue(queryManagementService.createCsvFileFromGenomicResultCalled);
        assertNotNull(sessionHelper.getDisplayableUserWorkspace().getTemporaryDownloadFile());
    }

    @Test
    public void testAcceptableParameterName() {
        assertTrue(manageQueryAction.acceptableParameterName(null));
        assertTrue(manageQueryAction.acceptableParameterName("ABC"));
        assertFalse(manageQueryAction.acceptableParameterName("123"));
        assertFalse(manageQueryAction.acceptableParameterName("d-123-e"));
    }

    private class ServletContextStub extends MockServletContext {

        @Override
        public String getRealPath(String path) {
            return TestDataFiles.VALID_FILE.getAbsolutePath();
        }
    }

}
