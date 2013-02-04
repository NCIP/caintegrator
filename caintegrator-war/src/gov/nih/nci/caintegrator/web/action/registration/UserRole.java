/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.registration;

/**
 * 
 */
public enum UserRole {
    /** 
     * Study Manager Role. 
     */
    STUDY_MANAGER ("Study Manager"),
    /** 
     * Study Investigator Role. 
     */
    STUDY_INVESTIGATOR ("Study Investigator");

    private final String name;


    /**
     * the constructor.
     */
    UserRole(String name) {
        this.name = name;
    }

    

    /**
     * @return display name
     */
    public String getName() {
        return this.name;
    }

}
