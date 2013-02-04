/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package edu.mit.broad.genepattern.gp.services;

/**
 * Indicates a problem communicating with the GenePattern service.
 */
public class GenePatternServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    GenePatternServiceException(Exception e) {
        super(e);
    }

}
