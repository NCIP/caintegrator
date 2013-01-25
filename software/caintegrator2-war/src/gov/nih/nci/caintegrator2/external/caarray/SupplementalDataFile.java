/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import java.io.File;

/**
 * A detail about the unparsed data file in caArray.
 */
public class SupplementalDataFile {
    private String fileName;
    private File file;
    private String probeNameHeader;
    private String valueHeader;
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
        this.fileName = fileName;
    }
    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }
    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }
    /**
     * @return the probeNameHeader
     */
    public String getProbeNameHeader() {
        return probeNameHeader;
    }
    /**
     * @param probeNameHeader the probeNameHeader to set
     */
    public void setProbeNameHeader(String probeNameHeader) {
        this.probeNameHeader = probeNameHeader;
    }
    /**
     * @return the valueHeader
     */
    public String getValueHeader() {
        return valueHeader;
    }
    /**
     * @param valueHeader the valueHeader to set
     */
    public void setValueHeader(String valueHeader) {
        this.valueHeader = valueHeader;
    }
}
