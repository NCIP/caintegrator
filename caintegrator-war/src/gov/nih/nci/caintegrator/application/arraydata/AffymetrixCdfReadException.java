/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

/**
 * Exception that indicates an Affymetrix CDF file couldn't be successfully read.
 */
public final class AffymetrixCdfReadException extends Exception {

    private static final long serialVersionUID = 1L;

    AffymetrixCdfReadException(String message) {
        super(message);
    }

    AffymetrixCdfReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
