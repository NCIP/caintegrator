/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class SubjectIdentifier extends AbstractCaIntegrator2Object {
    private static final long serialVersionUID = 1L;
    
    private String identifier;
    
    /**
     * Default constructor.
     */
    public SubjectIdentifier() {
        super();
    }

    /**
     * @param identifier the identifier to set
     */
    public SubjectIdentifier(String identifier) {
        super();
        this.identifier = identifier;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
