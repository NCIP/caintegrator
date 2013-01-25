/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

/**
 * 
 */
public class DownloadableFile {
    /**
     * For zip file type.
     */
    public static final String CONTENT_TYPE_ZIP = "application/zip";
    /**
     * For plain text file type.
     */
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    
    private String path;
    private String filename;
    private String contentType;
    private boolean deleteFile;
    

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
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }
    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    /**
     * @return the deleteFile
     */
    public boolean isDeleteFile() {
        return deleteFile;
    }
    /**
     * @param deleteFile the deleteFile to set
     */
    public void setDeleteFile(boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

}
