/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedStudyTest;
import gov.nih.nci.caintegrator2.web.ajax.PersistedAnalysisJobAjaxUpdater;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 *
 */
public class DownloadAnalysisResultsActionTest extends AbstractSessionBasedStudyTest {
    private DownloadAnalysisResultsAction action;
    private StudyManagementServiceStub studyManagementService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        action = new DownloadAnalysisResultsAction();
        studyManagementService = new StudyManagementServiceStub();
        action.setWorkspaceService(workspaceService);
        action.setStudyManagementService(studyManagementService);
    }

    @Test
    public void testValidate() {
        action.validate();
        assertTrue(action.hasActionErrors());
        action.clearActionErrors();
        action.setJobId(1l);
        action.validate();
        assertFalse(action.hasActionErrors());

    }

    @Test
    public void testPrepare() {
        action.prepare();
    }

    @Test
    public void testExecute() {
        ComparativeMarkerSelectionAnalysisJob job = new ComparativeMarkerSelectionAnalysisJob();
        job.setJobType(AnalysisJobTypeEnum.CMS);
        action.setJob(job);
        // Invalid type
        action.setType("INVALID TYPE");
        assertEquals(Action.INPUT, action.execute());
        // no results zip file.
        action.setType(PersistedAnalysisJobAjaxUpdater.DownloadType.RESULTS.getType());
        assertEquals(Action.INPUT, action.execute());

        // no input zip file.
        action.setType(PersistedAnalysisJobAjaxUpdater.DownloadType.INPUT.getType());
        assertEquals(Action.INPUT, action.execute());

        job.setResultsZipFile(new ResultsZipFile());
        job.setInputZipFile(new ResultsZipFile());

        // Valid
        action.setType(PersistedAnalysisJobAjaxUpdater.DownloadType.RESULTS.getType());
        assertEquals("downloadResultFile", action.execute());
        action.setType(PersistedAnalysisJobAjaxUpdater.DownloadType.INPUT.getType());
        assertEquals("downloadResultFile", action.execute());

        assertTrue(SessionHelper.getInstance().getDisplayableUserWorkspace().getTemporaryDownloadFile().getFilename().contains("cms"));
        job.setJobType(AnalysisJobTypeEnum.GENE_PATTERN);
        action.execute();
        assertTrue(SessionHelper.getInstance().getDisplayableUserWorkspace().getTemporaryDownloadFile().getFilename().contains("genePattern"));
        job.setJobType(AnalysisJobTypeEnum.GISTIC);
        action.execute();
        assertTrue(SessionHelper.getInstance().getDisplayableUserWorkspace().getTemporaryDownloadFile().getFilename().contains("gistic"));
        job.setJobType(AnalysisJobTypeEnum.PCA);
        action.execute();
        assertTrue(SessionHelper.getInstance().getDisplayableUserWorkspace().getTemporaryDownloadFile().getFilename().contains("pca"));

    }

}
