/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

/**
 * Indicates that array data couldn't be written or retrieved.
 */
public class ArrayDataStorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    ArrayDataStorageException(String message, Throwable t) {
        super(message, t);
    }

     ArrayDataStorageException(String message) {
        super(message);
    }

}
