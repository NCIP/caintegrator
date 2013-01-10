/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.igv;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.caintegrator2.application.analysis.AbstractViewerParameters;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;

/**
 * Parameters used to run IGV.
 */
public class IGVParameters extends AbstractViewerParameters {
    private List<Platform> platforms = new ArrayList<Platform>();
    
    /**
     * @return the platforms
     */
    public List<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * @param platforms the platforms to set
     */
    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }
    
    /**
     * @param platform to add if not null
     */
    public void addPlatform(Platform platform) {
        if (platform != null) {
            getPlatforms().add(platform);
        }
    }
    
}
