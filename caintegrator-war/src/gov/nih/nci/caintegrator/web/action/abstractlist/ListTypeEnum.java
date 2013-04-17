/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.abstractlist;



/**
 * Possible job type values for <code>AbstractPersistedAnalysisJob.jobType</code>.
 */
public enum ListTypeEnum {

    /**
     * Gene.
     */
    GENE("Gene List"),

    /**
     * Subject.
     */
    SUBJECT("Subject List");

    private String value;

    private ListTypeEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
