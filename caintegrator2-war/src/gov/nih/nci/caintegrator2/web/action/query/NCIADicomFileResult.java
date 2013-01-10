/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * Struts2 result type for downloading the dicom file stored on the temporary storage file system, and
 * then cleaning up the job on the session and deleting the file.
 */
public class NCIADicomFileResult implements Result {

    private static final long serialVersionUID = 1L;
    private static final Integer BUFSIZE = 4096;

    /**
     * {@inheritDoc}
     */
    public void execute(ActionInvocation invocation) throws IOException {
        NCIADicomJob dicomJob = SessionHelper.getInstance().getDisplayableUserWorkspace().getDicomJob();
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletOutputStream op = response.getOutputStream();
        File dicomFile = dicomJob.getDicomFile();
        response.setContentType("application/zip");
        response.setContentLength((int) dicomFile.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"dicomImages.zip\""); 
        
        byte[] bbuf = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(dicomFile));
        int length;
        while ((length = in.read(bbuf)) != -1) {
            op.write(bbuf, 0, length);
        }
        in.close();
        op.flush();
        cleanupDicomJob(dicomJob);
    }


    private void cleanupDicomJob(NCIADicomJob dicomJob) {
        dicomJob.getDicomFile().delete();
        dicomJob.setDicomFile(null);
        dicomJob.setCompleted(false);
    }
}
