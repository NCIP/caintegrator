/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

/**
 * Indicates a problem where a SurvivalValueDefinition is not valid.
 */
public class InvalidSurvivalValueDefinitionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    public InvalidSurvivalValueDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param message describes the problem.
     */
    public InvalidSurvivalValueDefinitionException(String message) {
        super(message);
    }

}
