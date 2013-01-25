/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the configuration for a plot and the data to be plotted.
 */
public class KMPlotConfiguration {
    
    private static final String DEFAULT_DURATION_LABEL = "Months in Study";
    private static final String DEFAULT_PROBABILITY_LABEL = "Probability of Survival";
    private static final String DEFAULT_TITLE = "Kaplan-Meier Plot";
    
    private String probabilityLabel = DEFAULT_PROBABILITY_LABEL;
    private String durationLabel = DEFAULT_DURATION_LABEL;
    private String title = DEFAULT_TITLE;
    
    private final List<SubjectGroup> groups = new ArrayList<SubjectGroup>();
    private final List<SubjectGroup> filteredGroups = new ArrayList<SubjectGroup>();
    
    /**
     * @return the probabilityLabel
     */
    public String getProbabilityLabel() {
        return probabilityLabel;
    }
    
    /**
     * @param probabilityLabel the probabilityLabel to set
     */
    public void setProbabilityLabel(String probabilityLabel) {
        this.probabilityLabel = probabilityLabel;
    }
    
    /**
     * @return the durationLabel
     */
    public String getDurationLabel() {
        return durationLabel;
    }
    
    /**
     * @param durationLabel the durationLabel to set
     */
    public void setDurationLabel(String durationLabel) {
        this.durationLabel = durationLabel;
    }

    /**
     * @return the groups
     */
    public List<SubjectGroup> getGroups() {
        return groups;
    }
    
    /**
     * @return the filterredGroups
     */
    public List<SubjectGroup> getFilteredGroups() {
        return filteredGroups;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
