/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for platform source specifications.
 */
public abstract class AbstractPlatformSource implements Serializable {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;

    private boolean deleteFileOnCompletion;
    private final List<File> annotationFiles = new ArrayList<File>();
    
    /**
     * The platform loader.
     * @return platform loader.
     * @throws PlatformLoadingException when error reading the annotation file.
     */
    public abstract AbstractPlatformLoader getLoader() throws PlatformLoadingException;
    
    /**
     * Get the platform name either from user input or from the annotation file.
     * @return the platform name
     * @exception PlatformLoadingException loading annotation file error
     */
    public abstract String getPlatformName() throws PlatformLoadingException;

    /**
     * @return the platform type
     */
    public abstract PlatformTypeEnum getPlatformType();

    /**
     * @return the platform channel type
     */
    public abstract PlatformChannelTypeEnum getPlatformChannelType();
    
    /**
     * Creates a new instance.
     * 
     * @param annotationFile the CSV annotation file.
     */
    public AbstractPlatformSource(File annotationFile) {
        super();
        if (annotationFile == null || !annotationFile.exists()) {
            throw new IllegalArgumentException("Annotation file must exist.");
        }
        annotationFiles.add(annotationFile);
    }

    /**
     * Creates a new instance.
     * 
     * @param annotationFiles the list of CSV annotation files.
     */
    public AbstractPlatformSource(List<File> annotationFiles) {
        super();
        if (annotationFiles.isEmpty() || !filesExisted(annotationFiles)) {
            throw new IllegalArgumentException("Annotation file must exist.");
        }
        this.annotationFiles.addAll(annotationFiles);
    }
    
    private boolean filesExisted(List<File> files) {
        for (File file : files) {
            if (file == null || !file.exists()) {
                return false;
            }
        }
        return true;
    }
    /**
     * @return the deleteFileOnCompletion
     */
    public boolean getDeleteFileOnCompletion() {
        return deleteFileOnCompletion;
    }

    /**
     * @param deleteFileOnCompletion the deleteFileOnCompletion to set
     */
    public void setDeleteFileOnCompletion(boolean deleteFileOnCompletion) {
        this.deleteFileOnCompletion = deleteFileOnCompletion;
    }

    /**
     * @return the list of annotationFiles
     */
    public List<File> getAnnotationFiles() {
        return annotationFiles;
    }

    /**
     * @return the annotationFile names
     */
    public String getAnnotationFileNames() {
        StringBuffer names = new StringBuffer();
        for (File file : annotationFiles) {
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(file.getName());
        }
        return names.toString();
    }
    
}
