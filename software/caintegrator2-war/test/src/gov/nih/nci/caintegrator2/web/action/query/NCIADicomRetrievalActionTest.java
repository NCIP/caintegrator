/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class NCIADicomRetrievalActionTest {
    
    NCIADicomRetrievalAction nciaRetrievalAction;
    
    @Before
    public void setUp() throws Exception {
        nciaRetrievalAction = new NCIADicomRetrievalAction();
        nciaRetrievalAction.setWorkspaceService(new WorkspaceServiceStub());
        setupSession();
        
    }
    
    private void setupSession() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        NCIADicomJob dicomJob = new NCIADicomJob();
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
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
