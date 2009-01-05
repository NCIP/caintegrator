package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAFacadeStub;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class NCIADicomRetrievalActionTest {
    
    NCIAFacadeStub stub;
    NCIADicomRetrievalAction nciaRetrievalAction;
    
    
    @Before
    public void setUp() throws Exception {
        stub = new NCIAFacadeStub();
        nciaRetrievalAction = new NCIADicomRetrievalAction();
        nciaRetrievalAction.setWorkspaceService(new WorkspaceServiceStub());
        nciaRetrievalAction.setNciaFacade(stub);
        stub.clear();
        setupSession();
        
    }
    
    private void setupSession() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        NCIADicomJob dicomJob = new NCIADicomJob();
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
        
    }

    @Test
    public void testRunDicomJob() {
        assertEquals(ActionSupport.SUCCESS, nciaRetrievalAction.runDicomJob());
        assertTrue(stub.retrieveDicomFilesCalled);
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
