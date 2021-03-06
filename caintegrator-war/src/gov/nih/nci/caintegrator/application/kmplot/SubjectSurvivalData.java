/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.kmplot;

/**
 * Survival data for a single subject.
 */
public class SubjectSurvivalData {
    
    private final int survivalLength;
    private final boolean censor;

    /**
     * Create a new, initialized survival data point.
     * 
     * @param survivalLength the number of days
     * @param censor true if a censor (last follow up)
     */
    public SubjectSurvivalData(int survivalLength, boolean censor) {
        this.survivalLength = survivalLength;
        this.censor = censor;
    }

    /**
     * @return the survivalLength
     */
    public int getSurvivalLength() {
        return survivalLength;
    }

    /**
     * @return the censor
     */
    public boolean isCensor() {
        return censor;
    }

}
