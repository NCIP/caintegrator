/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Grouping for samples in a Gene Expression plot (makes up the X-axis items).
 */
public class PlotSampleGroup {

    /**
     * Used to name the group for control samples.
     */
    public static final String CONTROL_SAMPLE_GROUP_NAME = "Control Samples";
    /**
     * Used to name the group for the "All Others" group.
     */
    public static final String ALL_OTHERS_GROUP_NAME = "Others";
    private String name;
    private int numberSubjects;
    private List<PlotReporterGroup> reporterGroups = new ArrayList<PlotReporterGroup>();
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the reporterGroups
     */
    public List<PlotReporterGroup> getReporterGroups() {
        return reporterGroups;
    }
    /**
     * @param reporterGroups the reporterGroups to set
     */
    public void setReporterGroups(List<PlotReporterGroup> reporterGroups) {
        this.reporterGroups = reporterGroups;
    }
    /**
     * @return the numberSubjects
     */
    public int getNumberSubjects() {
        return numberSubjects;
    }
    /**
     * @param numberSubjects the numberSubjects to set
     */
    public void setNumberSubjects(int numberSubjects) {
        this.numberSubjects = numberSubjects;
    }
    
    /**
     * Tests to see if this sample group is the control group.
     * @return T/F value.
     */
    public boolean isControlSampleGroup() { 
        return CONTROL_SAMPLE_GROUP_NAME.equals(name);
    }
    
}
