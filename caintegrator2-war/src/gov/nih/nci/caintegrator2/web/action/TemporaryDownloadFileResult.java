/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action;

import gov.nih.nci.caintegrator2.web.DownloadableFile;
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
 * Struts2 result type for downloading any temporary file, and
 * then deleting the file.
 */
public class TemporaryDownloadFileResult implements Result {

    private static final long serialVersionUID = 1L;
    private static final Integer BUFSIZE = 4096;

    /**
     * {@inheritDoc}
     */
    public void execute(ActionInvocation invocation) throws IOException {
        DownloadableFile downloadableFile = SessionHelper.getInstance().
                                                getDisplayableUserWorkspace().getTemporaryDownloadFile();
        File tempFile = new File(downloadableFile.getPath());
        SessionHelper.getInstance().getDisplayableUserWorkspace().setTemporaryDownloadFile(null);
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletOutputStream op = response.getOutputStream();
        response.setContentType(downloadableFile.getContentType());
        response.setContentLength((int) tempFile.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadableFile.getFilename() + "\""); 
        
        byte[] bbuf = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(tempFile));
        int length;
        while ((length = in.read(bbuf)) != -1) {
            op.write(bbuf, 0, length);
        }
        in.close();
        op.flush();
        if (downloadableFile.isDeleteFile()) {
            tempFile.delete();
        }
    }

}
