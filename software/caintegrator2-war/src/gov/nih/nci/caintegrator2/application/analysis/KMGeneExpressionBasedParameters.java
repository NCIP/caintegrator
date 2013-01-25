/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import org.apache.commons.lang.StringUtils;

/**
 * Parameters used for creating a Gene Expression Based KaplanMeier plot. 
 */
public class KMGeneExpressionBasedParameters extends AbstractKMParameters {

    private Float underexpressedFoldChangeNumber;
    private Float overexpressedFoldChangeNumber;
    private String controlSampleSetName;
    private String geneSymbol;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        if (StringUtils.isBlank(geneSymbol)) {
            getErrorMessages().add("Gene Symbol is blank, please enter a valid Gene.");
            isValid = false;
        }
        if (underexpressedFoldChangeNumber == null) {
            getErrorMessages().add("Under Expressed Fold Change value is not a valid number, must be >= 1.");
            isValid = false;
        }
        if (overexpressedFoldChangeNumber == null) {
            getErrorMessages().add("Over Expressed Fold Change value is not a valid number, must be >= 1.");
            isValid = false;
        }
        isValid = validateSurvivalValueDefinition(isValid);
        return isValid;
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
    

}
