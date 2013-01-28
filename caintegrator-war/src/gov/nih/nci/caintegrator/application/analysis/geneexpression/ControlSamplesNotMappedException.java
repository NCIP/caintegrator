/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.geneexpression;

/**
 * Indicates a problem where control samples are not mapped to patients.
 */
public class ControlSamplesNotMappedException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    ControlSamplesNotMappedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param message describes the problem.
     */
    ControlSamplesNotMappedException(String message) {
        super(message);
    }

}
