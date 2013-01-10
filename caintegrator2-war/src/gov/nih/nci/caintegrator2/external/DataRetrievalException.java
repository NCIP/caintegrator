/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external;

/**
 * Used to indicate that data couldn't be retrieved from caArray.
 */
public class DataRetrievalException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * 
     * @param message the message
     */
    public DataRetrievalException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     * 
     * @param message the message
     * @param e exception to extract the message
     */
    public DataRetrievalException(String message, Exception e) {
        super(message + " " + e.getMessage());
    }

}
