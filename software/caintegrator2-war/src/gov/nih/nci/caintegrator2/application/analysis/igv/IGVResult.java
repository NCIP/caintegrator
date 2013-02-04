/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.igv;

import java.io.File;

/**
 * 
 */
public class IGVResult {
    
    private File sessionFile;
    private File geneExpressionFile;
    private File segmentationFile;
    private File sampleInfoFile;
    
    
    /**
     * @return the sessionFile
     */
    public File getSessionFile() {
        return sessionFile;
    }
    
    /**
     * @param sessionFile the sessionFile to set
     */
    public void setSessionFile(File sessionFile) {
        this.sessionFile = sessionFile;
    }
    
    /**
     * @return the geneExpressionFile
     */
    public File getGeneExpressionFile() {
        return geneExpressionFile;
    }
    
    /**
     * @param geneExpressionFile the geneExpressionFile to set
     */
    public void setGeneExpressionFile(File geneExpressionFile) {
        this.geneExpressionFile = geneExpressionFile;
    }
    
    /**
     * @return the segmentationFile
     */
    public File getSegmentationFile() {
        return segmentationFile;
    }
    
    /**
     * @param segmentationFile the segmentationFile to set
     */
    public void setSegmentationFile(File segmentationFile) {
        this.segmentationFile = segmentationFile;
    }

    /**
     * @return the sampleInfoFile
     */
    public File getSampleInfoFile() {
        return sampleInfoFile;
    }

    /**
     * @param sampleInfoFile the sampleInfoFile to set
     */
    public void setSampleInfoFile(File sampleInfoFile) {
        this.sampleInfoFile = sampleInfoFile;
    }
}
