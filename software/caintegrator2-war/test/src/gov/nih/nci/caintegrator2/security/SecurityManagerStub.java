/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.security;

import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
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
        if (username.equals("userExists") || username.equals("username")) {
            return true;
        }
        return false;
    }

    @Override
    public void createProtectionElement(StudyConfiguration studyConfiguration, AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException {
        createProtectionElementCalled = true;
    }

    @Override
    public void deleteProtectionElement(AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException {
        deleteProtectionElementCalled = true;    }

    @Override
    public Set<AuthorizedStudyElementsGroup> retrieveAuthorizedStudyElementsGroupsForInvestigator(String username,
            Set<AuthorizedStudyElementsGroup> availableGroups) throws CSException {
        return new HashSet<AuthorizedStudyElementsGroup>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Group> getUnauthorizedGroups(StudyConfiguration studyConfiguration) throws CSException {
        return new ArrayList<Group>();
    }
}
