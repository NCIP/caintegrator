package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class NCIADicomRetrievalActionTest extends AbstractSessionBasedTest {
    
    NCIADicomRetrievalAction nciaRetrievalAction;
    
    @Before
    public void setUp() {
        nciaRetrievalAction = new NCIADicomRetrievalAction();
        nciaRetrievalAction.setWorkspaceService(new WorkspaceServiceStub());
        setupSession();
        
    }
    
    private void setupSession() {
        super.setUp();
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
