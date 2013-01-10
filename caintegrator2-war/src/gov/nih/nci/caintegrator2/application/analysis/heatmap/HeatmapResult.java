/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import java.io.File;

/**
 * 
 */
public class HeatmapResult {
    
    private File jnlpFile;
    private File genomicDataFile;
    private File layoutFile;
    private File sampleAnnotationFile;


    /**
     * @return the jnlpFile
     */
    public File getJnlpFile() {
        return jnlpFile;
    }

    /**
     * @param jnlpFile the jnlpFile to set
     */
    public void setJnlpFile(File jnlpFile) {
        this.jnlpFile = jnlpFile;
    }

    /**
     * @return the genomicDataFile
     */
    public File getGenomicDataFile() {
        return genomicDataFile;
    }

    /**
     * @param genomicDataFile the genomicDataFile to set
     */
    public void setGenomicDataFile(File genomicDataFile) {
        this.genomicDataFile = genomicDataFile;
    }

    /**
     * @return the layoutFile
     */
    public File getLayoutFile() {
        return layoutFile;
    }

    /**
     * @param layoutFile the layoutFile to set
     */
    public void setLayoutFile(File layoutFile) {
        this.layoutFile = layoutFile;
    }

    /**
     * @return the sampleAnnotationFile
     */
    public File getSampleAnnotationFile() {
        return sampleAnnotationFile;
    }

    /**
     * @param sampleAnnotationFile the sampleAnnotationFile to set
     */
    public void setSampleAnnotationFile(File sampleAnnotationFile) {
        this.sampleAnnotationFile = sampleAnnotationFile;
    }
    
}
