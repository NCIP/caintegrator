/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationFileStub;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.ISubjectDataSourceAjaxUpdater;
import gov.nih.nci.caintegrator2.web.ajax.SubjectDataSourceAjaxRunner.JobType;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class LoadClinicalSourceActionTest extends AbstractSessionBasedTest {

    private LoadClinicalSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    private ISubjectDataSourceAjaxUpdater updater;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = mock(ISubjectDataSourceAjaxUpdater.class);
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new LoadClinicalSourceAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
        action.getClinicalSource().setAnnotationFile(createAnnotationFile());
        action.setUpdater(updater);
    }

    @Test
    public void testExecute() {
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.execute());
        verify(updater, times(1)).runJob(anyLong(), anyLong(), any(JobType.class));
    }

    @Test
    public void testReLoad() {
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.reLoad());
        verify(updater, times(1)).runJob(anyLong(), anyLong(), any(JobType.class));
    }

    @Test
    public void testDelete() {
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.delete());
        verify(updater, times(1)).runJob(anyLong(), anyLong(), any(JobType.class));
    }

    private AnnotationFile createAnnotationFile() {
        DelimitedTextClinicalSourceConfiguration clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        AnnotationFileStub annotationFile = new AnnotationFileStub();
        clinicalConf.setAnnotationFile(annotationFile);
        return annotationFile;
    }
}
