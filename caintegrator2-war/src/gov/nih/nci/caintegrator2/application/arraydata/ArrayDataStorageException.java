/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

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
