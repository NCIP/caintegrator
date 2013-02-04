/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

/**
 * Indicates the current status of a study configuration.
 */
public enum Status {
    
    /**
     * Not yet deployed.
     */
    NOT_DEPLOYED("Not Deployed"),
    
    /**
     * Not yet loaded.
     */
    NOT_LOADED("Not Loaded"),
    
    /**
     * Definition Incomplete.
     */
    DEFINITION_INCOMPLETE("Definition Incomplete"),
    
    /**
     * Processing.
     */
    PROCESSING("Processing"),
    
    /**
     * Error.
     */
    ERROR("Error"),
    
    /**
     * Currently deployed.
     */
    DEPLOYED("Deployed"),
    
    /**
     * Currently loaded (for data sources).
     */
    LOADED("Loaded"),
    
    /**
     * Not yet mapped.
     */
    NOT_MAPPED("Not Mapped");
    
    private String value;
    
    private Status(String value) {
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
