/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Parameters used for creating a Gene Expression Based KaplanMeier plot.
 */
public class KMGeneExpressionBasedParameters extends AbstractKMParameters {

    private Float underValue;
    private Float overValue;
    private String controlSampleSetName;
    private String geneSymbol;
    private final List<String> genesNotFound = new ArrayList<String>();
    private final List<String> genesFoundInStudy = new ArrayList<String>();
    private boolean multiplePlatformsInStudy = false;
    private String platformName;
    private ExpressionTypeEnum expressionType = ExpressionTypeEnum.FOLD_CHANGE;

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
        isValid &= validateExpressionLevelValues();
        isValid &= validatePlatformSelected();
        isValid &= validateControlSetSelected();
        isValid = validateSurvivalValueDefinition(isValid);
        return isValid;
    }

    private boolean validatePlatformSelected() {
        if (multiplePlatformsInStudy && StringUtils.isBlank(platformName)) {
            getErrorMessages().add("There are multiple platforms in this study, select a platform first.");
            return false;
        }
        return true;
    }

    private boolean validateControlSetSelected() {
        if (isFoldChangeType() && StringUtils.isBlank(controlSampleSetName)) {
            getErrorMessages().add("Must select a control sample set to create a Fold Change based plot.");
            return false;
        }
        return true;
    }

    private boolean validateOverexpressed() {
        String valueStringPrefix = isFoldChangeType() ? "Over Expressed Fold Change" : "Above Expression Level";
        if (overValue == null) {
            getErrorMessages().add(valueStringPrefix + " value cannot be null");
            return false;
        }
        if (isFoldChangeType() && overValue <= 0) {
            getErrorMessages().add(valueStringPrefix + " value is not a valid number, must be > 0.");
            return false;
        }

        return true;
    }

    private boolean validateExpressionLevelValues() {
        if (!isFoldChangeType() && overValue != null && underValue != null && overValue <= underValue) {
            getErrorMessages().add("Above Expression Level value must be greater than Below Expression Level value.");
            return false;
        }
        return true;
    }


    private boolean validateUnderexpressed() {
        String valueStringPrefix = isFoldChangeType() ? "Under Expressed Fold Change" : "Below Expression Level";
        if (underValue == null) {
            getErrorMessages().add(valueStringPrefix + " value cannot be null");
            return false;
        }
        if (isFoldChangeType() && underValue <= 0) {
            getErrorMessages().add(valueStringPrefix + " value is not a valid number, must be > 0.");
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
     * @return the underValue
     */
    public Float getUnderValue() {
        return underValue;
    }

    /**
     * @param underValue the underValue to set
     */
    public void setUnderValue(Float underValue) {
        this.underValue = underValue;
    }

    /**
     * @return the overValue
     */
    public Float getOverValue() {
        return overValue;
    }

    /**
     * @param overValue the overValue to set
     */
    public void setOverValue(Float overValue) {
        this.overValue = overValue;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        underValue = null;
        overValue = null;
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

    /**
     * @return the expressionType
     */
    public ExpressionTypeEnum getExpressionType() {
        return expressionType;
    }

    /**
     * @param expressionType the expressionType to set
     */
    public void setExpressionType(ExpressionTypeEnum expressionType) {
        this.expressionType = expressionType;
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
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * @return HashMap of EnumeratedValue's String to Displayable String.
     */
    public static List<String> getExpressionTypeValuesToDisplay() {
        List<String> list = new ArrayList<String>();
        for (ExpressionTypeEnum type : ExpressionTypeEnum.values()) {
            list.add(type.getValue());
        }
        return list;
    }

    private boolean isFoldChangeType() {
        return ExpressionTypeEnum.FOLD_CHANGE.equals(expressionType);
    }
}
