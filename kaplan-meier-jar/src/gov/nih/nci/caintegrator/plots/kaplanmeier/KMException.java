/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier;


@SuppressWarnings("serial")
public class KMException extends Exception {
    public KMException() {
    }

    public KMException(String message, Throwable cause) {
        super(message, cause);
    }

    public KMException(Throwable cause) {
        super(cause);
    }

    public KMException(String message) {
        super(message);
    }
}
