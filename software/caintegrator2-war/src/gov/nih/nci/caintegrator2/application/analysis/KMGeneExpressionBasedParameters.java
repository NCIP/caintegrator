/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Parameters used for creating a Gene Expression Based KaplanMeier plot. 
 */
public class KMGeneExpressionBasedParameters extends AbstractKMParameters {

    private Float underexpressedFoldChangeNumber;
    private Float overexpressedFoldChangeNumber;
    private String controlSampleSetName;
    private String geneSymbol;
    private final List<String> genesNotFound = new ArrayList<String>();
    private final List<String> genesFoundInStudy = new ArrayList<String>();
    private boolean multiplePlatformsInStudy = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        isValid &= validateGeneName();
        isValid &= validateUnderexpressed();
        isValid &= validateOverexpressed();
        isValid &= validateControlSetSelected();
        isValid = validateSurvivalValueDefinition(isValid);
        return isValid;
    }
    
    private boolean validateControlSetSelected() {
        if (StringUtils.isBlank(controlSampleSetName)) {
            getErrorMessages().add("Must select a control sample set. "
                    + "If there are multiple platforms in this study, select a platform first.");
            return false;
        }
        return true;
    }

    private boolean validateOverexpressed() {
        if (overexpressedFoldChangeNumber == null) {
            getErrorMessages().add("Over Expressed Fold Change value is not a valid number, must be >= 1.");
            return false;
        }
        return true;
    }


    private boolean validateUnderexpressed() {
        if (underexpressedFoldChangeNumber == null) {
            getErrorMessages().add("Under Expressed Fold Change value is not a valid number, must be >= 1.");
            return false;
        }
        return true;
    }

    private boolean validateGeneName() {
        if (StringUtils.isBlank(geneSymbol)) {
            getErrorMessages().add("Gene Symbol is blank, please enter a valid Gene.");
            return false;
        }
        return true;
    }


    /**
     * @return the underexpressedFoldChangeNumber
     */
    public Float getUnderexpressedFoldChangeNumber() {
        return underexpressedFoldChangeNumber;
    }


    /**
     * @param underexpressedFoldChangeNumber the underexpressedFoldChangeNumber to set
     */
    public void setUnderexpressedFoldChangeNumber(Float underexpressedFoldChangeNumber) {
        this.underexpressedFoldChangeNumber = underexpressedFoldChangeNumber;
    }


    /**
     * @return the overexpressedFoldChangeNumber
     */
    public Float getOverexpressedFoldChangeNumber() {
        return overexpressedFoldChangeNumber;
    }


    /**
     * @param overexpressedFoldChangeNumber the overexpressedFoldChangeNumber to set
     */
    public void setOverexpressedFoldChangeNumber(Float overexpressedFoldChangeNumber) {
        this.overexpressedFoldChangeNumber = overexpressedFoldChangeNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        underexpressedFoldChangeNumber = null;
        overexpressedFoldChangeNumber = null;
        geneSymbol = null;
        
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
     * @return the genesFoundInStudy
     */
    public List<String> getGenesFoundInStudy() {
        return genesFoundInStudy;
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
