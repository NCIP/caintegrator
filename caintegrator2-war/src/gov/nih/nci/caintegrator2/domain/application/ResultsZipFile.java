/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import java.io.File;
import java.io.Serializable;

/**
 * Provides persistent access to a zipped results file.
 */
public class ResultsZipFile implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String path;

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
     * The file.
     * 
     * @return the file.
     */
    public File getFile() {
        return new File(getPath());
    }

}
