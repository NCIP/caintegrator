/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.application.study.ImageContentTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.HttpRequestHandler;

/**
 * Servlet to render Study Logo's based on Study input parameters.
 */
public class LogoRenderingServlet implements HttpRequestHandler {
    
    private StudyManagementService studyManagementService;
    
    // Not sure what the actual default logo will be, but it will probably be in the images dir.
    private static final String DEFAULT_LOGO = File.separator + "images" + File.separator + "logo_sample_study.gif";
    private static final ImageContentTypeEnum DEFAULT_CONTENT_TYPE = ImageContentTypeEnum.GIF; 
    /**
     * {@inheritDoc}
     */
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String studyId = request.getParameter("studyId");
        String studyName = request.getParameter("studyName");
        StudyLogo studyLogo = null;
        String imagePath = "";
        if (studyId != null && studyName != null) {
            studyLogo = studyManagementService.retrieveStudyLogo(Long.valueOf(studyId), studyName);
        }
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

    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }

}
