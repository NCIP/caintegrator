/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.web.SessionHelper;

/**
 * Represents a gif/jpg logo image.
 */
public class StudyLogo extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private static final String LOGO_SERVLET_URL = "/" + SessionHelper.WAR_CONTEXT_NAME + "/logo?";
    private String fileName;
    private String path;
    private String fileType;
    private StudyConfiguration studyConfiguration;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName.replaceAll(" ", "_");
    }
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }
    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }
    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    /**
     * Retrieves the  URL for the study logo.
     * @return - the URL for the servlet which serves up the study logo.
     */
    public String getLogoUrl() {
        return LOGO_SERVLET_URL + "studyId=" + studyConfiguration.getStudy().getId() 
        + "&studyName=" + studyConfiguration.getStudy().getShortTitleText();                           
    }    

    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }
    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }    
}
