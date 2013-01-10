/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier.dto;

import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;

import java.util.Collection;

/**
 * @author caIntegrator Team
 * This class serves as the final input for the KMPlot Service. It holds an
 * indsicriminate amoutn of KMSampleGroupCriteriaDTO, which, in turn, hold
 * a collection of KMSampleDTOs. These KMSampleDTOs are basically samples with coordinates
 * attached to them.
 * 
 */

public class KMCriteriaDTO {
    private String plotTitle;
    private Collection<KMSampleGroupCriteriaDTO> sampleGroupCriteriaDTOCollection;
    String probablityAxisLabel;
    String durationAxisLabel;

    public String getProbablityAxisLabel() {
        return probablityAxisLabel;
    }

    public void setProbablityAxisLabel(String probablityAxisLabel) {
        this.probablityAxisLabel = probablityAxisLabel;
    }

    public String getDurationAxisLabel() {
        return durationAxisLabel;
    }

    public void setDurationAxisLabel(String durationAxisLabel) {
        this.durationAxisLabel = durationAxisLabel;
    }

    
    public KMCriteriaDTO(){}

    public String getPlotTitle() {
        return plotTitle;
    }

    public void setPlotTitle(String plotTitle) {
        this.plotTitle = plotTitle;
    }

    public Collection<KMSampleGroupCriteriaDTO> getSampleGroupCriteriaDTOCollection() {
        return sampleGroupCriteriaDTOCollection;
    }

    public void setSampleGroupCriteriaDTOCollection(Collection<KMSampleGroupCriteriaDTO> sampleGroupCriteriaDTOCollection) {
        this.sampleGroupCriteriaDTOCollection = sampleGroupCriteriaDTOCollection;
    }

}
