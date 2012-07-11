/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

    @Override
    @Before
    public void setUp() throws Exception {
        manageQueryAction = new ManageQueryAction();
        manageQueryAction.setQueryManagementService(queryManagementService);
        manageQueryAction.setStudyManagementService(studyManagementService);
        manageQueryAction.setWorkspaceService(workspaceService);
        setupSession();
    }

    private void setupSession() throws Exception {
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
