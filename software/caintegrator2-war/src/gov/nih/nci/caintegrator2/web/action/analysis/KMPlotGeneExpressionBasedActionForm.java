/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import java.util.ArrayList;
import java.util.List;


/**
 * Form used to store input values for Gene Expression Based KM Plots.
 */
public class KMPlotGeneExpressionBasedActionForm {
    
    private static final String DEFAULT_OVER_EXPRESSED = "2.0";
    private static final String DEFAULT_UNDER_EXPRESSED = "2.0";
    
    private String geneSymbol;
    private String overexpressedNumber;
    private String underexpressedNumber;
    private String controlSampleSetName;
    private String platformName;
    private List<String> controlSampleSets = new ArrayList<String>();
    private boolean initialized = false;
    private boolean resetSelected = false;
    

    /**
     * Clears all the variables to null.
     */
    public void clear() {
        geneSymbol = null;
        overexpressedNumber = DEFAULT_OVER_EXPRESSED;
        underexpressedNumber = DEFAULT_UNDER_EXPRESSED;
        initialized = true;
        platformName = null;
        controlSampleSets = new ArrayList<String>();
    }

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     * @return the overexpressedNumber
     */
    public String getOverexpressedNumber() {
        return overexpressedNumber;
    }

    /**
     * @param overexpressedNumber the overexpressedNumber to set
     */
    public void setOverexpressedNumber(String overexpressedNumber) {
        this.overexpressedNumber = overexpressedNumber;
    }

    /**
     * @return the underexpressedNumber
     */
    public String getUnderexpressedNumber() {
        return underexpressedNumber;
    }

    /**
     * @param underexpressedNumber the underexpressedNumber to set
     */
    public void setUnderexpressedNumber(String underexpressedNumber) {
        this.underexpressedNumber = underexpressedNumber;
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    
    /**
     * @return the resetSelected
     */
    public boolean isResetSelected() {
        return resetSelected;
    }

    /**
     * @param resetSelected the resetSelected to set
     */
    public void setResetSelected(boolean resetSelected) {
        this.resetSelected = resetSelected;
    }

    /**
     * @return the controlSampleSetName
     */
    public String getControlSampleSetName() {
        return controlSampleSetName;
    }

    /**
     * @param controlSampleSetName the controlSampleSetName to set
     */
    public void setControlSampleSetName(String controlSampleSetName) {
        this.controlSampleSetName = controlSampleSetName;
    }

    /**
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
    
    /**
     * @return the controlSampleSets
     */
    public List<String> getControlSampleSets() {
        return controlSampleSets;
    }

    /**
     * @param controlSampleSets the controlSampleSets to set
     */
    public void setControlSampleSets(List<String> controlSampleSets) {
        this.controlSampleSets = controlSampleSets;
    }
}
