/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.security;

import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
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
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSInsufficientAttributesException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Session;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Providers methods to access authentication and authorization data.
 */
public class SecurityManagerImpl implements SecurityManager {

    private static final String APPLICATION_CONTEXT_NAME = "caintegrator2";
    private static final String STUDY_MANAGER_ROLE = "STUDY_MANAGER_ROLE";
    private static final String STUDY_INVESTIGATOR_ROLE = "STUDY_INVESTIGATOR_ROLE";
    private static final String STUDY_OBJECT = "gov.nih.nci.caintegrator2.domain.translational.Study";
    private static final String STUDY_ATTRIBUTE = "id";
    private static final String AUTHORIZED_STUDY_ELEMENTS_GROUP_OBJECT =
                            "gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup";
    private static final String AUTHORIZED_STUDY_ELEMENTS_GROUP_ATTRIBUTE = "id";
    private static final String UNCHECKED = "unchecked";

    private AuthorizationManagerFactory authorizationManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        if (doesUserExist(studyConfiguration.getUserWorkspace().getUsername())) {
            User user = retrieveCsmUser(studyConfiguration.getUserWorkspace().getUsername());
            String userId = String.valueOf(user.getUserId());
            ProtectionElement element = createProtectionElementInstance(studyConfiguration);
            element.setProtectionElementName(studyConfiguration.getStudy().getShortTitleText());
            Set<User> owners = new HashSet<User>();
            owners.add(user);
            element.setOwners(owners);
            element.setProtectionGroups(retrieveProtectionGroups(userId, STUDY_MANAGER_ROLE));

            getAuthorizationManager().createProtectionElement(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    public void deleteProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        ProtectionElement element = createProtectionElementInstance(studyConfiguration);
        SearchCriteria elementCriteria = new ProtectionElementSearchCriteria(element);
        List<ProtectionElement> retrievedElements = getAuthorizationManager().getObjects(elementCriteria);
        for (ProtectionElement pe : retrievedElements) {
            getAuthorizationManager().removeProtectionElement(String.valueOf(pe.getProtectionElementId()));
        }
        List<AuthorizedStudyElementsGroup> authStudyElementsGroups =
                                                studyConfiguration.getAuthorizedStudyElementsGroups();
        for (AuthorizedStudyElementsGroup aseg : authStudyElementsGroups) {
            deleteProtectionElement(aseg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    public void initializeFiltersForUserGroups(String username, Session session) throws CSException {
        if (doesUserExist(username)) {
            List<String> groupNames = new ArrayList<String>();
            String userId = String.valueOf(retrieveCsmUser(username).getUserId());
            for (Group group : (Set<Group>) getAuthorizationManager().getGroups(userId)) {
                groupNames.add(group.getGroupName());
            }
            InstanceLevelSecurityHelper.initializeFiltersForGroups(groupNames.toArray(new String[groupNames.size()]),
                                                                   session, getAuthorizationManager());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AuthorizedStudyElementsGroup> retrieveAuthorizedStudyElementsGroupsForInvestigator(String username,
                                                                    Set<AuthorizedStudyElementsGroup> availableGroups)
        throws CSException {
        if (!doesUserExist(username)) {
            return new HashSet<AuthorizedStudyElementsGroup>();
        }

        Set<AuthorizedStudyElementsGroup> authorizedStudyElementsGroups = new HashSet<AuthorizedStudyElementsGroup>();
        Set<ProtectionGroup> userProtectionGroups =
            retrieveProtectionGroups(String.valueOf(retrieveCsmUser(username).getUserId()),
                                                                        STUDY_INVESTIGATOR_ROLE);
        Set<Long> authorizedStudyElementsGroupIds = retrieveAuthorizedStudyElementsGroupIds(userProtectionGroups);
        for (AuthorizedStudyElementsGroup group : availableGroups) {
            if (authorizedStudyElementsGroupIds.contains(group.getId())) {
                authorizedStudyElementsGroups.add(group);
            }
        }
        return authorizedStudyElementsGroups;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StudyConfiguration> retrieveManagedStudyConfigurations(String username, Collection<Study> studies)
        throws CSException {
        if (!doesUserExist(username)) {
            return new HashSet<StudyConfiguration>();
        }
        Set<StudyConfiguration> managedStudies = new HashSet<StudyConfiguration>();
        Set<ProtectionGroup> studyManagerProtectionGroups =
            retrieveProtectionGroups(String.valueOf(retrieveCsmUser(username).getUserId()), STUDY_MANAGER_ROLE);
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

    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    private Set<Long> retrieveAuthorizedStudyElementsGroupIds(Set<ProtectionGroup> protectionGroups)
                                                                                    throws CSException {
        Set<Long> authorizedStudyElementsGroupIds = new HashSet<Long>();
        for (ProtectionGroup group : protectionGroups) {
            Set<ProtectionElement> elements =
                getAuthorizationManager().getProtectionElements(String.valueOf(group.getProtectionGroupId()));
            for (ProtectionElement element : elements) {
                if (AUTHORIZED_STUDY_ELEMENTS_GROUP_OBJECT.equals(element.getObjectId())
                                                            && NumberUtils.isNumber(element.getValue())) {
                    authorizedStudyElementsGroupIds.add(Long.valueOf(element.getValue()));
                }
            }
        }
        return authorizedStudyElementsGroupIds;
    }


    /**
     * {@inheritDoc}
     */
    @Override
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
    private Set<ProtectionGroup> retrieveProtectionGroups(String userId, String csmRoleToBeRetrieved)
            throws CSException {
        Set<ProtectionGroup> protectionGroups = new HashSet<ProtectionGroup>();
        Set<Group> groups = getAuthorizationManager().getGroups(userId);
        for (Group group : groups) {
            Set<ProtectionGroupRoleContext> pgrcs =
                getAuthorizationManager().getProtectionGroupRoleContextForGroup(String.valueOf(group.getGroupId()));
            for (ProtectionGroupRoleContext pgrc : pgrcs) {
                for (Role role : (Set<Role>) pgrc.getRoles()) {
                    if (csmRoleToBeRetrieved.equals(role.getName())) {
                        protectionGroups.add(pgrc.getProtectionGroup());
                        break;
                    }
                }
            }
        }
        return protectionGroups;
    }

    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    private Set<ProtectionGroup> retrieveProtectionGroups(Group group, String csmRoleToBeRetrieved) throws CSException {
        Set<ProtectionGroup> protectionGroups = new HashSet<ProtectionGroup>();
        Set<ProtectionGroupRoleContext> pgrcs = getAuthorizationManager()
            .getProtectionGroupRoleContextForGroup(String.valueOf(group.getGroupId()));
        for (ProtectionGroupRoleContext pgrc : pgrcs) {
            for (Role role : (Set<Role>) pgrc.getRoles()) {
                if (csmRoleToBeRetrieved.equals(role.getName())) {
                    protectionGroups.add(pgrc.getProtectionGroup());
                    break;
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
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void createProtectionElement(StudyConfiguration studyConfiguration,
            AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException {
        if (doesUserExist(studyConfiguration.getUserWorkspace().getUsername())) {
            User user = retrieveCsmUser(studyConfiguration.getUserWorkspace().getUsername());
            ProtectionElement element = createProtectionElementInstance(authorizedStudyElementsGroup);
            if (element == null) {
                throw new CSInsufficientAttributesException();
            } else {
                Group authorizedGroup = authorizedStudyElementsGroup.getAuthorizedGroup();
                element.setProtectionElementName(authorizedGroup.getGroupName());
                Set<User> owners = new HashSet<User>();
                owners.add(user);
                element.setOwners(owners);

                Set<ProtectionGroup> protectionGroups = new HashSet<ProtectionGroup>();
                protectionGroups.addAll(retrieveProtectionGroups(authorizedGroup, STUDY_INVESTIGATOR_ROLE));
                protectionGroups.addAll(retrieveProtectionGroups(authorizedGroup, STUDY_MANAGER_ROLE));
                element.setProtectionGroups(protectionGroups);
                getAuthorizationManager().createProtectionElement(element);
            }
        }

    }

    /**
     * Create ProtectElementInstance in CSM.
     * @param authorizedStudyElementsGroup
     * @return element or null if ProtectionElement can not be created
     */
    private ProtectionElement createProtectionElementInstance(
            AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        ProtectionElement element = new ProtectionElement();
        String value = String.valueOf(authorizedStudyElementsGroup.getId());
        if (value.equalsIgnoreCase("null")) {
            element = null;
        } else {
            element.setAttribute(AUTHORIZED_STUDY_ELEMENTS_GROUP_ATTRIBUTE);
            element.setObjectId(AUTHORIZED_STUDY_ELEMENTS_GROUP_OBJECT);
            element.setValue(value);
        }
        return element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings(UNCHECKED) // CSM API is untyped
    public void deleteProtectionElement(AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException {
        ProtectionElement element = createProtectionElementInstance(authorizedStudyElementsGroup);
        SearchCriteria elementCriteria = new ProtectionElementSearchCriteria(element);
        List<ProtectionElement> retrievedElements = getAuthorizationManager().getObjects(elementCriteria);
        for (ProtectionElement pe : retrievedElements) {
            getAuthorizationManager().removeProtectionElement(String.valueOf(pe.getProtectionElementId()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings(UNCHECKED)
    public Collection<Group> getUnauthorizedGroups(StudyConfiguration studyConfiguration) throws CSException {
        SearchCriteria criteria = new GroupSearchCriteria(new Group());
        List<Group> groups = getAuthorizationManager().getObjects(criteria);
        List<AuthorizedStudyElementsGroup> authorizedGroups = studyConfiguration.getAuthorizedStudyElementsGroups();
        final List<String> authorizedGroupNames = Lists.newArrayList();
        for (AuthorizedStudyElementsGroup aseg : authorizedGroups) {
            authorizedGroupNames.add(aseg.getAuthorizedGroup().getGroupName());
        }
        Collection<Group> results = Collections2.filter(groups, new Predicate<Group>() {
            @Override
            public boolean apply(Group group) {
                return !authorizedGroupNames.contains(group.getGroupName());
            }
        });
        return results;
    }
}
