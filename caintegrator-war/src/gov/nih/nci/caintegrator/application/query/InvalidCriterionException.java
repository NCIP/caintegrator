/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

/**
 * Indicates a problem with a query's criterion.
 */
public class InvalidCriterionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    InvalidCriterionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param message describes the problem.
     */
    public InvalidCriterionException(String message) {
        super(message);
    }

}
