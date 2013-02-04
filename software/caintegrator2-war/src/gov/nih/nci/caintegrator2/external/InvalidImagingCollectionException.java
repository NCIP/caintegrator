/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external;

/**
 * Indicates a problem connecting to an external server.
 */
public class InvalidImagingCollectionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    public InvalidImagingCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     * 
     * @param message the message
     */
    public InvalidImagingCollectionException(String message) {
        super(message);
    }

}
