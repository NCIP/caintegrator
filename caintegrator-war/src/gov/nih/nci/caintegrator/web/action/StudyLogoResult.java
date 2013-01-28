/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action;

import gov.nih.nci.caintegrator.application.study.ImageContentTypeEnum;
import gov.nih.nci.caintegrator.application.study.StudyLogo;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * Struts2 result type, which streams the study logo (or default if none exists) for the current study subscription.
 */
public class StudyLogoResult implements Result {
    
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    private static final String DEFAULT_LOGO = File.separator + "images" + File.separator + "logo_sample_study.gif";
    private static final ImageContentTypeEnum DEFAULT_CONTENT_TYPE = ImageContentTypeEnum.GIF; 

    /**
     * {@inheritDoc}
     */
    public void execute(ActionInvocation invocation) throws IOException {
        StudyLogo studyLogo = SessionHelper.getInstance().getDisplayableUserWorkspace().getStudyLogo();
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        String imagePath = "";
        imagePath = retrieveImagePath(request, response, studyLogo);
        OutputStream outputStream = response.getOutputStream();
        File imageFile = new File(imagePath);
        FileInputStream inputStream = new FileInputStream(imageFile);
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();
    }

    private String retrieveImagePath(HttpServletRequest request, HttpServletResponse response, StudyLogo studyLogo) {
        String imagePath;
        if (studyLogo != null && studyLogo.getPath() != null) { // Use study logo.
            String contentType = studyLogo.getFileType();
            if (!ImageContentTypeEnum.checkType(contentType)) {
                contentType = ImageContentTypeEnum.JPEG.getValue();
            }
            response.setContentType(contentType);
            imagePath = studyLogo.getPath();
        } else { // Use default logo, if the studyLogo doesn't have a path.
            imagePath = request.getSession().getServletContext().getRealPath("/") + DEFAULT_LOGO;
            response.setContentType(DEFAULT_CONTENT_TYPE.getValue());
        }
        return imagePath;
    }

}
