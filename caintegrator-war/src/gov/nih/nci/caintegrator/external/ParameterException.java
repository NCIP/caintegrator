/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external;

/**
 * Indicates a problem connecting to an external server.
 */
public class ParameterException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the invalid parameter
     * @param cause the source exception
     */
    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     * 
     * @param message the message
     */
    public ParameterException(String message) {
        super(message);
    }

}
