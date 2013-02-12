/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;


/**
 * Indicates that an action couldn't be performed due to invalid data.
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final ValidationResult result;

    /**
     * Creates a new exception.
     * @param result validation result.
     */
    public ValidationException(ValidationResult result) {
        super(result.getInvalidMessage());
        this.result = result;
    }

    /**
     * Creates a new exception.
     *
     * @param invalidMessage the message
     */
    public ValidationException(String invalidMessage) {
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
    public ValidationException(String message, Throwable cause) {
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