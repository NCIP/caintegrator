/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that represents input parameters for a GeneExpression plot.
 */
public abstract class AbstractGEPlotParameters {
    private String geneSymbol;
    private boolean addControlSamplesGroup;
    private boolean multiplePlatformsInStudy = false;
    private String controlSampleSetName;
    private String platformName;
    private final List<String> errorMessages = new ArrayList<String>();
    private final List<String> genesNotFound = new ArrayList<String>();
    
    /**
     * Validates that all parameters are set.
     * @return T/F value.
     */
    public abstract boolean validate();
    
    /**
     * Clears all values.
     */
    public abstract void clear();
    

    /**
     * @return the errorMessages
     */
    public List<String> getErrorMessages() {
        return errorMessages;
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
     * @return the addControlSamplesGroup
     */
    public boolean isAddControlSamplesGroup() {
        return addControlSamplesGroup;
    }

    /**
     * @param addControlSamplesGroup the addControlSamplesGroup to set
     */
    public void setAddControlSamplesGroup(boolean addControlSamplesGroup) {
        this.addControlSamplesGroup = addControlSamplesGroup;
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
     * @return the genesNotFound
     */
    public List<String> getGenesNotFound() {
        return genesNotFound;
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
     * @return the multiplePlatformsInStudy
     */
    public boolean isMultiplePlatformsInStudy() {
        return multiplePlatformsInStudy;
    }

    /**
     * @param multiplePlatformsInStudy the multiplePlatformsInStudy to set
     */
    public void setMultiplePlatformsInStudy(boolean multiplePlatformsInStudy) {
        this.multiplePlatformsInStudy = multiplePlatformsInStudy;
    }

}
