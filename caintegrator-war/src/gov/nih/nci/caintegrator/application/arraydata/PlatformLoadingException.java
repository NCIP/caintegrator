/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

/**
 * Indicates a problem loading an array design platform from the provided resources.
 */
public class PlatformLoadingException extends Exception {

    private static final long serialVersionUID = 1L;

    PlatformLoadingException(String message, Throwable t) {
        super(message, t);
    }

    PlatformLoadingException(String message) {
        super(message);
    }

}
