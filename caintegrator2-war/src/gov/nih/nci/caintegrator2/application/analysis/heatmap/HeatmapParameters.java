/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import gov.nih.nci.caintegrator2.application.analysis.AbstractViewerParameters;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;

/**
 * Parameters used to run Heatmap.
 */
public class HeatmapParameters extends AbstractViewerParameters {
    private String heatmapJarUrlPrefix;
    private Platform platform;
    private String smallBinsFile;
    private String largeBinsFile;

    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     * @return the smallBinsFile
     */
    public String getSmallBinsFile() {
        return smallBinsFile;
    }

    /**
     * @param smallBinsFile the smallBinsFile to set
     */
    public void setSmallBinsFile(String smallBinsFile) {
        this.smallBinsFile = smallBinsFile;
    }

    /**
     * @return the largeBinsFile
     */
    public String getLargeBinsFile() {
        return largeBinsFile;
    }

    /**
     * @param largeBinsFile the largeBinsFile to set
     */
    public void setLargeBinsFile(String largeBinsFile) {
        this.largeBinsFile = largeBinsFile;
    }

    /**
     * @return the heatmapJarUrlPrefix
     */
    public String getHeatmapJarUrlPrefix() {
        return heatmapJarUrlPrefix;
    }

    /**
     * @param heatmapJarUrlPrefix the heatmapJarUrlPrefix to set
     */
    public void setHeatmapJarUrlPrefix(String heatmapJarUrlPrefix) {
        this.heatmapJarUrlPrefix = heatmapJarUrlPrefix;
    }
}
