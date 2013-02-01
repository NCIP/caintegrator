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
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.DwrUtilFactory;
import gov.nih.nci.caintegrator.web.ajax.PersistedAnalysisJobAjaxUpdater;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GisticAnalysisActionTest extends AbstractSessionBasedTest {
    private GisticAnalysisAction action;
    private StudySubscription subscription;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        subscription = new StudySubscription();
        subscription.setUserWorkspace(new UserWorkspace());
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        ArrayData arrayData = new ArrayData();
        Platform platform = new Platform();
        Array array = new Array();
        array.setPlatform(platform);
        arrayData.setArray(array);
        ReporterList reporterList = platform.addReporterList("test", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        reporterList.setGenomeVersion("hg18");
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayDataCollection().add(arrayData);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        study.getAssignmentCollection().add(assignment);
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
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new GisticAnalysisAction();
        action.setAnalysisService(analysisService);
        action.setQueryManagementService(queryManagementService);
        action.setWorkspaceService(workspaceService);

        PersistedAnalysisJobAjaxUpdater updater = new PersistedAnalysisJobAjaxUpdater();
        updater.setAnalysisService(analysisService);
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(new DwrUtilFactory());

        action.setAjaxUpdater(updater);
        action.getGisticAnalysisForm().setGisticParameters(new GisticParameters());
        action.setConfigurationHelper(configurationHelper);
        action.getPlatformsInStudy().add("Platform 1");
        action.getPlatformsInStudy().add("Platform 2");
    }

    @Test
    public void testOpen() {
        action.setSelectedAction(GisticAnalysisAction.OPEN_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());
    }

    @Test
    public void testValidate() {
        action.setSelectedAction(GisticAnalysisAction.EXECUTE_ACTION);
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentGisticAnalysisJob().setName("Test");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getGisticAnalysisForm().setSelectedPlatformName("Platform 1");
        action.validate();
        assertFalse(action.hasErrors());
        action.getGisticAnalysisForm().getGisticParameters().setAmplificationsThreshold(-1f);
        action.validate();
        assertTrue(action.hasErrors());
    }

    @Test
    public void execute() {
        testOpen();
        action.setSelectedAction(GisticAnalysisAction.EXECUTE_ACTION);
        action.getCurrentGisticAnalysisJob().setId(1L);
        when(analysisService.validateGenePatternConnection(any(ServerConnectionProfile.class))).thenReturn(Boolean.TRUE);
        assertEquals("status", action.execute());
    }

    @Test
    public void executeValidConnection() throws Exception {
        testOpen();
        when(analysisService.validateGenePatternConnection(any(ServerConnectionProfile.class))).thenReturn(Boolean.TRUE);
        when(queryManagementService.execute(any(Query.class))).thenReturn(getValidQueryResult());
        action.setSelectedAction(GisticAnalysisAction.EXECUTE_ACTION);
        action.getGisticAnalysisForm().setSelectedQuery("[Q]-query1");
        action.getCurrentGisticAnalysisJob().setId(1L);
        assertEquals("status", action.execute());
    }

    @Test
    public void executeInvalidConnection() {
        testOpen();
        action.setSelectedAction(GisticAnalysisAction.EXECUTE_ACTION);
        when(analysisService.validateGenePatternConnection(any(ServerConnectionProfile.class))).thenReturn(Boolean.FALSE);
        assertEquals("input", action.execute());
    }

    private QueryResult getValidQueryResult() {
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(new HashSet<ResultRow>());
        ResultRow row1 = new ResultRow();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        row1.setSubjectAssignment(assignment1);
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        Sample sample1 = new Sample();
        ArrayData arrayData1 = new ArrayData();
        sample1.getArrayDataCollection().add(arrayData1);
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("test", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        reporterList.setGenomeVersion("hg18");
        arrayData1.getReporterLists().add(reporterList);
        sampleAcquisition1.setSample(sample1);
        assignment1.getSampleAcquisitionCollection().add(sampleAcquisition1);
        row1.setSampleAcquisition(sampleAcquisition1);

        ResultRow row2 = new ResultRow();
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        row2.setSubjectAssignment(assignment2);
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        Sample sample2 = new Sample();
        ArrayData arrayData2 = new ArrayData();
        arrayData2.getReporterLists().add(reporterList);
        sample2.getArrayDataCollection().add(arrayData1);
        sampleAcquisition2.setSample(sample2);
        assignment2.getSampleAcquisitionCollection().add(sampleAcquisition2);
        row2.setSampleAcquisition(sampleAcquisition2);

        queryResult.getRowCollection().add(row1);
        queryResult.getRowCollection().add(row2);
        return queryResult;
    }
}
