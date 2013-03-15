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
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.PersistedAnalysisJobAjaxUpdater;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ComparativeMarkerSelectionAnalysisActionTest extends AbstractSessionBasedTest {
    private ComparativeMarkerSelectionAnalysisAction action;
    private StudySubscription subscription;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        subscription = new StudySubscription();

        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        subscription.setStudy(study);
        Query query1 = new Query();
        query1.setName("query1");
        query1.setId(Long.valueOf(1));
        query1.setCompoundCriterion(new CompoundCriterion());
        Query query2 = new Query();
        query2.setName("query2");
        query2.setId(Long.valueOf(2));
        query2.setCompoundCriterion(new CompoundCriterion());
        subscription.getQueryCollection().add(query1);
        subscription.getQueryCollection().add(query2);
        query1.setSubscription(subscription);
        query2.setSubscription(subscription);
        subscription.setId(100L);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);

        setStudySubscription(subscription);

        action = new ComparativeMarkerSelectionAnalysisAction();
        action.setAnalysisService(analysisService);
        action.setQueryManagementService(queryManagementService);
        action.setWorkspaceService(workspaceService);
        action.setAjaxUpdater(new PersistedAnalysisJobAjaxUpdater());
        action.prepare();
    }


    @Test
    public void testOpen() {
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.OPEN_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());
    }

    @Test
    public void testValidate() {
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources()
            .add(new GenomicDataSourceConfiguration());
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentComparativeMarkerSelectionAnalysisJob().setName("Test");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().add("[Q]-query1");
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().add("[Q]-query2");
        action.validate();
        assertFalse(action.hasErrors());

        // Test platforms
        Set<String> platforms = new HashSet<String>();
        platforms.add("platform1");
        when(queryManagementService.retrieveGeneExpressionPlatformsForStudy(any(Study.class))).thenReturn(platforms);

        action.prepare();
        assertFalse(action.isStudyHasMultiplePlatforms());
        action.validate();
        assertFalse(action.hasErrors());

        platforms.add("platform2");
        action.prepare();
        action.clearErrorsAndMessages();
        assertTrue(action.isStudyHasMultiplePlatforms());
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getComparativeMarkerSelectionAnalysisForm().setPlatformName("platform1");
        action.validate();
        assertFalse(action.hasErrors());
    }

    @Test
    public void execute() {
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.OPEN_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        assertEquals("status", action.execute());
    }

    @Test
    public void executeValidResults() throws Exception {
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.OPEN_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());

        when(queryManagementService.execute(any(Query.class))).thenReturn(getValidQueryResults());
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().add("[Q]-query1");
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().add("[Q]-query2");
        assertEquals("status", action.execute());
        assertFalse(action.hasErrors());
    }

    @Test
    public void executeInvalidResults() throws Exception {
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.OPEN_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());
        when(queryManagementService.execute(any(Query.class))).thenReturn(getInvalidQueryResults());
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().add("[Q]-query1");
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().add("[Q]-query2");
        assertEquals("input", action.execute());
        assertTrue(action.hasErrors());
    }

    private QueryResult getValidQueryResults() {
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(new HashSet<ResultRow>());
        ResultRow row1 = new ResultRow();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        row1.setSubjectAssignment(assignment1);
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        assignment1.getSampleAcquisitionCollection().add(sampleAcquisition1);

        ResultRow row2 = new ResultRow();
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        row2.setSubjectAssignment(assignment2);
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        assignment2.getSampleAcquisitionCollection().add(sampleAcquisition2);

        queryResult.getRowCollection().add(row1);
        queryResult.getRowCollection().add(row2);
        return queryResult;
    }

    private QueryResult getInvalidQueryResults() {
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(new HashSet<ResultRow>());

        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        assignment1.getSampleAcquisitionCollection().add(sampleAcquisition1);

        ResultRow row1 = new ResultRow();
        row1.setSubjectAssignment(assignment1);

        queryResult.getRowCollection().add(row1);
        return queryResult;
    }
}
