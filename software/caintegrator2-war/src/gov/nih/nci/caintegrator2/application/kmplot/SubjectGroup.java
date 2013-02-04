/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains data and configuration for a single group to be plotted.
 */
public class SubjectGroup {

    private String name;
    private Color color;
    private final List<SubjectSurvivalData> survivalData = new ArrayList<SubjectSurvivalData>();
    
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
     * @return the color
     */
    public Color getColor() {
        return color;
    }
    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
    /**
     * @return the survivalData
     */
    public List<SubjectSurvivalData> getSurvivalData() {
        return survivalData;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }
    
}
