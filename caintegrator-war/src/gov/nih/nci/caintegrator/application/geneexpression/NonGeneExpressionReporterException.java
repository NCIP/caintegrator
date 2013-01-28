/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

/**
 * Indicates a reporter is not of type GeneExpressionReporter.
 */
public class NonGeneExpressionReporterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance based on an underlying exception.
     * 
     * @param message describes the problem.
     */
    public NonGeneExpressionReporterException(String message) {
        super(message);
    }
    
    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the problem
     * @param cause the source exception
     */
    public NonGeneExpressionReporterException(String message, Throwable cause) {
        super(message, cause);
    }

}
