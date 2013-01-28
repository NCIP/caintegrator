/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.application;

/**
 *
 */
public class StringComparisonCriterion extends AbstractComparisonCriterion {

    private static final long serialVersionUID = 1L;

    private String stringValue;
    private WildCardTypeEnum wildCardType;

    /**
     * @return the stringValue
     */
    @Override
    public String getStringValue() {
        return stringValue;
    }

    /**
     * @param stringValue the stringValue to set
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * @return the wildCardType
     */
    public WildCardTypeEnum getWildCardType() {
        return wildCardType;
    }

    /**
     * @param wildCardType the wildCardType to set
     */
    public void setWildCardType(WildCardTypeEnum wildCardType) {
        this.wildCardType = wildCardType;
    }

}
