/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

/**
 * Communicates whether or not a set of data were valid.
 */
public class ValidationResult {
    
    private boolean valid;
    private String invalidMessage;

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the invalidMessage
     */
    public String getInvalidMessage() {
        return invalidMessage;
    }

    /**
     * @param invalidMessage the invalidMessage to set
     */
    public void setInvalidMessage(String invalidMessage) {
        valid = (invalidMessage == null);
        this.invalidMessage = invalidMessage;
    }
    
    

}
