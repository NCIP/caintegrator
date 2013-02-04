/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier.dto;

import java.util.Collection;
import java.awt.*;

/**
 * @author caIntegrator Team
 * This class holds information about a group of samples such as the color
 * to be used in display, the name of the collective group and it stores the
 * actual collection of KMSampleDTOs itself.
 */

public class KMSampleGroupCriteriaDTO {

    private String sampleGroupName;
    private Collection<KMSampleDTO> kmSampleDTOCollection;
    private Color color;
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getSampleGroupName() {
        return sampleGroupName;
    }

    public void setSampleGroupName(String sampleGroupName) {
        this.sampleGroupName = sampleGroupName;
    }

    public Collection<KMSampleDTO> getKmSampleDTOCollection() {
        return kmSampleDTOCollection;
    }

    public void setKmSampleDTOCollection(Collection<KMSampleDTO> kmSampleDTOCollection) {
        this.kmSampleDTOCollection = kmSampleDTOCollection;
    }
}
