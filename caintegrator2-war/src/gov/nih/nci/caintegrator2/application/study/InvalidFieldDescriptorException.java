/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;


/**
 * Indicates that an error occured associated with field descriptors couldn't be performed due to invalid data.
 */
public class InvalidFieldDescriptorException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private final ValidationResult result;

    /**
     * Creates a new exception.
     * @param result validation result.
     */
    public InvalidFieldDescriptorException(ValidationResult result) {
        this.result = result;
    }

    /**
     * Creates a new exception.
     * 
     * @param invalidMessage the message
     */
    public InvalidFieldDescriptorException(String invalidMessage) {
        super(invalidMessage);
        this.result = new ValidationResult();
        this.result.setInvalidMessage(invalidMessage);
    }

    /**
     * Creates a new exception.
     * 
     * @param message the message
     * @param cause the source throwable
     */
    public InvalidFieldDescriptorException(String message, Throwable cause) {
        super(message, cause);
        this.result = new ValidationResult();
        this.result.setInvalidMessage(message);
    }

    /**
     * @return the result
     */
    public ValidationResult getResult() {
        return result;
    }

}
