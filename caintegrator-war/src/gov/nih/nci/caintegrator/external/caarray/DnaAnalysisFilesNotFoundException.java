/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

/**
 * Indicates a problem finding dna analysis files in a caArray experiment.
 */
public class DnaAnalysisFilesNotFoundException extends ExperimentNotFoundException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance based on an underlying exception.
     * 
     * @param message describes the connection problem
     * @param cause the source exception
     */
    public DnaAnalysisFilesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param message describes the problem.
     */
    public DnaAnalysisFilesNotFoundException(String message) {
        super(message);
    }

}
