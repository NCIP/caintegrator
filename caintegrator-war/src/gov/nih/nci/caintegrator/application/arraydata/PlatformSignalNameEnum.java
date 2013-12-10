/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;



/**
 * Possible names of the signal column in the array data
 * as it appears in the genomic data source files in caArray.
 */
public enum PlatformSignalNameEnum {

    /**
     * One-Color platform signal name.
     */
    AFFYMETRIX_SIGNAL_NAME("CHPSignal"),

    /**
     * One-Color platform signal name.
     */
    ONE_COLOR_SIGNAL_NAME("gProcessedSignal"),

    /**
     * Two-Color platform signal name.
     */
    TWO_COLOR_SIGNAL_NAME("gProcessedSignal");

    private String value;

    private PlatformSignalNameEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
