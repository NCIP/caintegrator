package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.mock.MockActionInvocation;

public class NCIADicomFileResultTest extends AbstractSessionBasedTest {

    @Test
    public void testExecute() throws IOException {
        setUp();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletActionContext.setResponse(response);
        NCIADicomJob dicomJob = new NCIADicomJob();
        File destFile = new File(System.getProperty("java.io.tmpdir"), "tmpFile.zip");
        FileUtils.copyFile(TestDataFiles.VALID_FILE, destFile);
        dicomJob.setDicomFile(destFile);
        dicomJob.setCompleted(true);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
        assertTrue(destFile.exists());
        
        NCIADicomFileResult result = new NCIADicomFileResult();
        result.execute(new MockActionInvocation());
        
        assertFalse(destFile.exists());
        assertFalse(dicomJob.isCompleted());
        assertEquals("application/zip", response.getContentType());
    }

}