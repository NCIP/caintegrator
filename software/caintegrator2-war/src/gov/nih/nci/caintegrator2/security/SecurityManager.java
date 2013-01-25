/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.security;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Collection;
import java.util.Set;

import org.hibernate.Session;

/**
 * Interface to manage authentication and authorization data.
 */
public interface SecurityManager {
    
    
    /**
     * Creates the protectionElement on a Study given the StudyConfiguration.
     * @param studyConfiguration for which to create protection element on.
     * @throws CSException If there's a problem creating the PE.
     */
    void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException; 

    /**
     * Deletes a proteectionElement on a study given the Study Configuration.
     * @param studyConfiguration to delete the protection element for.
     * @throws CSException If there's a problem deleting the PE.
     */
    void deleteProtectionElement(StudyConfiguration studyConfiguration) throws CSException; 
    
    /**
     * Adds filter initialization to the session.
     * @param username to get all this users groups and filter based on those.
     * @param session hibernate session.
     * @throws CSException if there's problem using CSM.
     */
    void initializeFiltersForUserGroups(String username, Session session) throws CSException;
    
    /**
     * Retrieves authorization manager.
     * @return authorization manager for CaIntegrator2.
     * @throws CSException if there's a problem retrieving authorization manager from CSM.
     */
    AuthorizationManager getAuthorizationManager() throws CSException;
    
    /**
     * Retrieves a list of study configurations that the user has "UPDATE" access on.
     * @param username to check permissions for.
     * @param studies to check UPDATE permissions on.
     * @return study configurations that are managed by this user.
     * @throws CSException if there's a problem retrieving authorization manager from CSM.
     */
    Set<StudyConfiguration> retrieveManagedStudyConfigurations(String username, Collection<Study> studies) 
        throws CSException;

    /**
     * Checks to see if the given username exists.
     * @param username to check if it exists or not.
     * @return T/F value if it exists.
     */
    boolean doesUserExist(String username);
    
}
