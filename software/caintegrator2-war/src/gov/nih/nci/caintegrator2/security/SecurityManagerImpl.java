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
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Session;

/**
 * Providers methods to access authentication and authorization data.
 */
public class SecurityManagerImpl implements SecurityManager {
    
    private static final String APPLICATION_CONTEXT_NAME = "caintegrator2";
    private static final String STUDY_MANAGER_ROLE = "STUDY_MANAGER_ROLE";
    private static final String STUDY_OBJECT = "gov.nih.nci.caintegrator2.domain.translational.Study";
    private static final String STUDY_ATTRIBUTE = "id";
    private static final String UNCHECKED = "unchecked";
    
    private AuthorizationManagerFactory authorizationManagerFactory;

    /**
     * {@inheritDoc}
     */
    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        User user = retrieveCsmUser(studyConfiguration.getUserWorkspace().getUsername());
        String userId = String.valueOf(user.getUserId());
        ProtectionElement element = createProtectionElementInstance(studyConfiguration);
        element.setProtectionElementName(studyConfiguration.getStudy().getShortTitleText());
        Set<User> owners = new HashSet<User>();
        owners.add(user);
        element.setOwners(owners);
        element.setProtectionGroups(retrieveStudyManagerProtectionGroups(userId));
        
        getAuthorizationManager().createProtectionElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    public void deleteProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        ProtectionElement element = createProtectionElementInstance(studyConfiguration);
        SearchCriteria elementCriteria = new ProtectionElementSearchCriteria(element);
        List<ProtectionElement> retrievedElements = getAuthorizationManager().getObjects(elementCriteria);
        for (ProtectionElement pe : retrievedElements) {
            getAuthorizationManager().removeProtectionElement(String.valueOf(pe.getProtectionElementId()));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    public void initializeFiltersForUserGroups(String username, Session session) throws CSException {
        List<String> groupNames = new ArrayList<String>();
        String userId = String.valueOf(retrieveCsmUser(username).getUserId());
        for (Group group : (Set<Group>) getAuthorizationManager().getGroups(userId)) {
            groupNames.add(group.getGroupName());
        }
        InstanceLevelSecurityHelper.initializeFiltersForGroups(groupNames.toArray(new String[groupNames.size()]), 
                                                               session, getAuthorizationManager());
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<StudyConfiguration> retrieveManagedStudyConfigurations(String username, Collection<Study> studies) 
        throws CSException {
        Set<StudyConfiguration> managedStudies = new HashSet<StudyConfiguration>();
        Set<ProtectionGroup> studyManagerProtectionGroups = 
            retrieveStudyManagerProtectionGroups(String.valueOf(retrieveCsmUser(username).getUserId()));
        Set<Long> managedStudyIds = retrieveStudyIds(studyManagerProtectionGroups);
        for (Study study : studies) {
            // I think there's a bug with this function, so having to do it the hard way.
//            if (getAuthorizationManager().checkPermission(username, STUDY_OBJECT, STUDY_ATTRIBUTE, 
//                                String.valueOf(study.getId()), Constants.CSM_UPDATE_PRIVILEGE)) {
//                managedStudies.add(study.getStudyConfiguration());
//            }
            if (managedStudyIds.contains(study.getId())) {
                managedStudies.add(study.getStudyConfiguration());
            }
        }
        return managedStudies;
    }
    
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    private Set<Long> retrieveStudyIds(Set<ProtectionGroup> protectionGroups) throws CSException {
        Set<Long> managedStudyIds = new HashSet<Long>();
        for (ProtectionGroup group : protectionGroups) {
            Set<ProtectionElement> elements = 
                getAuthorizationManager().getProtectionElements(String.valueOf(group.getProtectionGroupId()));
            for (ProtectionElement element : elements) {
                if (STUDY_OBJECT.equals(element.getObjectId()) && NumberUtils.isNumber(element.getValue())) {
                    managedStudyIds.add(Long.valueOf(element.getValue()));
                }
            }
        }
        return managedStudyIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean doesUserExist(String username) {
        try {
            return retrieveCsmUser(username) != null ? true : false;
        } catch (CSException e) {
            return false;
        }
    }
    
    private User retrieveCsmUser(String username) throws CSException {
        return getAuthorizationManager().getUser(username);
    }
    
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    private Set<ProtectionGroup> retrieveStudyManagerProtectionGroups(String userId) 
    throws CSException {
        Set<ProtectionGroup> protectionGroups = new HashSet<ProtectionGroup>();
        Set<Group> groups = getAuthorizationManager().getGroups(userId);
        for (Group group : groups) {
            Set<ProtectionGroupRoleContext> pgrcs = 
                getAuthorizationManager().getProtectionGroupRoleContextForGroup(String.valueOf(group.getGroupId()));
            for (ProtectionGroupRoleContext pgrc : pgrcs) {
                for (Role role : (Set<Role>) pgrc.getRoles()) {
                    if (STUDY_MANAGER_ROLE.equals(role.getName())) {
                        protectionGroups.add(pgrc.getProtectionGroup());
                        break;
                    }
                }
            }
        }
        return protectionGroups;
    }

    private ProtectionElement createProtectionElementInstance(StudyConfiguration studyConfiguration) {
        ProtectionElement element = new ProtectionElement();
        element.setAttribute(STUDY_ATTRIBUTE);
        element.setObjectId(STUDY_OBJECT);
        element.setValue(String.valueOf(studyConfiguration.getStudy().getId()));
        return element;
    }
    
    /**
     * {@inheritDoc}
     */
    public AuthorizationManager getAuthorizationManager() throws CSException {
        return authorizationManagerFactory.getAuthorizationManager(APPLICATION_CONTEXT_NAME);
    }

    /**
     * @return the authorizationManagerFactory
     */
    public AuthorizationManagerFactory getAuthorizationManagerFactory() {
        return authorizationManagerFactory;
    }

    /**
     * @param authorizationManagerFactory the authorizationManagerFactory to set
     */
    public void setAuthorizationManagerFactory(AuthorizationManagerFactory authorizationManagerFactory) {
        this.authorizationManagerFactory = authorizationManagerFactory;
    }

}
