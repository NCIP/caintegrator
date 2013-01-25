/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.ajax.PersistedAnalysisJobAjaxUpdater;

import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ComparativeMarkerSelectionAnalysisActionTest {
    
    private ComparativeMarkerSelectionAnalysisAction action;
    private QueryManagementServiceStubComparativeMarker queryManagementService;

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        StudySubscription subscription = new StudySubscription();
        queryManagementService = new QueryManagementServiceStubComparativeMarker();
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        subscription.setStudy(study);
        subscription.setQueryCollection(new HashSet<Query>());
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
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new ComparativeMarkerSelectionAnalysisAction();
        action.setAnalysisService(new AnalysisServiceStub());
        action.setQueryManagementService(queryManagementService);
        action.setWorkspaceService(new WorkspaceServiceStub());
        action.setAjaxUpdater(new PersistedAnalysisJobAjaxUpdater());
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
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentComparativeMarkerSelectionAnalysisJob().setName("Test");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryIDs().add("1");
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryIDs().add("2");
        action.validate();
        assertFalse(action.hasErrors());
    }
    
    @Test
    public void testExecute() {
        testOpen();
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        assertEquals("status", action.execute());
        testOpen();
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryIDs().add("1");
        action.getComparativeMarkerSelectionAnalysisForm().getSelectedQueryIDs().add("2");
        queryManagementService.setupInvalidQueryResult();
        assertEquals("input", action.execute());
        testOpen();
        action.setSelectedAction(ComparativeMarkerSelectionAnalysisAction.EXECUTE_ACTION);
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        queryManagementService.setupValidQueryResult();
        assertEquals("status", action.execute());
        assertFalse(action.hasErrors());
    }
    
    private class QueryManagementServiceStubComparativeMarker extends QueryManagementServiceStub {
        private QueryResult queryResult;
        
        @Override
        public QueryResult execute(Query query) {
            
            return queryResult;
        }
        
        public void setupValidQueryResult() {
            queryResult = new QueryResult();
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
            
        }
        
        public void setupInvalidQueryResult() {
            queryResult = new QueryResult();
            queryResult.setRowCollection(new HashSet<ResultRow>());
            ResultRow row1 = new ResultRow();
            StudySubjectAssignment assignment1 = new StudySubjectAssignment();
            row1.setSubjectAssignment(assignment1);
            SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
            assignment1.getSampleAcquisitionCollection().add(sampleAcquisition1);
            
            queryResult.getRowCollection().add(row1);
            
        }
    }

}
