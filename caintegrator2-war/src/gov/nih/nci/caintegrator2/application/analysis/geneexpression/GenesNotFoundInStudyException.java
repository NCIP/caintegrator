/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

/**
 * Indicates a problem where specified genes are not found for a study.
 */
public class GenesNotFoundInStudyException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    public GenesNotFoundInStudyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param message describes the problem.
     */
    public GenesNotFoundInStudyException(String message) {
        super(message);
    }

}
