/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class NCIADicomRetrievalActionTest extends AbstractSessionBasedTest {

    NCIADicomRetrievalAction nciaRetrievalAction;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        NCIADicomJob dicomJob = new NCIADicomJob();
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
        nciaRetrievalAction = new NCIADicomRetrievalAction();
        nciaRetrievalAction.setWorkspaceService(workspaceService);
    }

    @Test
    public void testDownloadDicomFile() {
        NCIADicomJob dicomJob = new NCIADicomJob();
        assertEquals(ActionSupport.ERROR, nciaRetrievalAction.downloadDicomFile());
        dicomJob.setCompleted(true);
        dicomJob.setDicomFile(TestDataFiles.VALID_FILE);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
        assertEquals("dicomFileResult", nciaRetrievalAction.downloadDicomFile());
    }

}
