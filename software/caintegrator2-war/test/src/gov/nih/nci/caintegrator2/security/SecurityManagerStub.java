/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.security;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

/**
 * 
 */
public class SecurityManagerStub implements SecurityManager {
    
    public boolean createProtectionElementCalled;
    public boolean deleteProtectionElementCalled;
    public boolean isStudyManagerCalled;
    public boolean doesUserExistCalled;
    
    public void clear() {
        createProtectionElementCalled = false;
        deleteProtectionElementCalled = false;
        isStudyManagerCalled = false;
        doesUserExistCalled = false;
    }
    
    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        createProtectionElementCalled = true;
    }

    public void deleteProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        deleteProtectionElementCalled = true;
    }

    public AuthorizationManager getAuthorizationManager() throws CSException {
        return new AuthorizationManagerStub();
    }

    public void initializeFiltersForUserGroups(String username, Session session) throws CSException {
        
    }

    public boolean isStudyManager(String userName) {
        isStudyManagerCalled = true;
        return false;
    }

    public Set<StudyConfiguration> retrieveManagedStudyConfigurations(String username, Collection<Study> studies)
            throws CSException {
        Set<StudyConfiguration> studyConfigurationSet = new HashSet<StudyConfiguration>();
        if (studies != null) {
            for (Study study : studies) {
                if (study.getStudyConfiguration() != null) {
                    studyConfigurationSet.add(study.getStudyConfiguration());
                }
            }
        }
        return studyConfigurationSet;
    }

    public boolean doesUserExist(String username) {
        doesUserExistCalled = true;
        if (username.equals("userExists")) {
            return true;
        }
        return false;
    }

}
