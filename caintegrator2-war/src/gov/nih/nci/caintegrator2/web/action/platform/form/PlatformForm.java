/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.platform.form;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The form to hold multiple annotation files when creating new Platform.
 */
public class PlatformForm {
    
    private final List<File> annotationFiles = new ArrayList<File>();
    private String fileNames;
    private static final String NEW_LINE = "\n";
    
    /**
     * Clear all attributes.
     */
    public void clear() {
        annotationFiles.clear();
        fileNames = "";
    }
    
    /**
     * Add an annotation file.
     * @param annotationFile file to add.
     * @param fileName file name to add.
     */
    public void add(File annotationFile, String fileName) {
        annotationFiles.add(annotationFile);
        fileNames += fileName + NEW_LINE; 
    }

    /**
     * @return the annotationFiles
     */
    public List<File> getAnnotationFiles() {
        return annotationFiles;
    }

    /**
     * @return the fileNames
     */
    public String getFileNames() {
        return fileNames;
    }

}
