/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.mockito.AbstractSecurityEnabledMockitoTest;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

public class SecurityManagerImplTest extends AbstractSecurityEnabledMockitoTest {
    private SecurityManagerImpl securityManager;
    private StudyConfiguration studyConfiguration;
    private AuthorizedStudyElementsGroup authorizedStudyElementsGroup;
    private static final String USER_DOES_NOT_EXIST = "userDoesNotExist";

    @Before
    public void setUp() throws Exception {
        securityManager = new SecurityManagerImpl();
        securityManager.setAuthorizationManagerFactory(authManagerFactory);

        authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementsGroup.setId(Long.valueOf(1));
        authorizedStudyElementsGroup.setAuthorizedGroup(new Group());
        studyConfiguration = new StudyConfiguration();
        UserWorkspace workspace = new UserWorkspace();
        studyConfiguration.setUserWorkspace(workspace);
        workspace.setUsername("user");

        Group group = new Group();
        group.setGroupName("Authorized Group");;
        group.setGroupDesc("This is an Authorized Group");
        AuthorizedStudyElementsGroup authorizedGroup = new AuthorizedStudyElementsGroup();
        authorizedGroup.setAuthorizedGroup(group);
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedGroup);
    }

    @Test
    public void testCreateProtectionElementUserExists() throws CSException {
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_EXISTS);
        studyConfiguration.setUserWorkspace(userWorkspace);
        securityManager.createProtectionElement(studyConfiguration);
        verify(authManager, times(1)).createProtectionElement(any(ProtectionElement.class));
    }

    @Test
    public void testCreateProtectionElementUserDoesNotExist() throws CSException {
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_DOES_NOT_EXIST);
        studyConfiguration.setUserWorkspace(userWorkspace);
        securityManager.createProtectionElement(studyConfiguration);
        verify(authManager, never()).createProtectionElement(any(ProtectionElement.class));
    }

    @Test
    public void testDeleteProtectionElement() throws CSException {
        securityManager.deleteProtectionElement(studyConfiguration);
        verify(authManager, atLeastOnce()).removeProtectionElement(anyString());
    }

    @Test
    public void testDeleteProtectionElementWithAuthorized() throws CSException {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup1 = new AuthorizedStudyElementsGroup();
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup2 = new AuthorizedStudyElementsGroup();
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup1);
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup2);
        securityManager.deleteProtectionElement(studyConfiguration);
        verify(authManager, atLeastOnce()).removeProtectionElement(anyString());
    }

    @Test
    public void testInitializeFiltersForUserGroups() throws CSException {
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        when(session.getSessionFactory()).thenReturn(sessionFactory);
        securityManager.initializeFiltersForUserGroups(USERNAME, session);
        verify(sessionFactory, times(1)).getDefinedFilterNames();
    }

    @Test
    public void testInitializeFiltersForUserGroupsUserDoesNotExist() throws CSException {
        Session session = mock(Session.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        when(session.getSessionFactory()).thenReturn(sessionFactory);
        securityManager.initializeFiltersForUserGroups(USER_DOES_NOT_EXIST, session);
        verify(sessionFactory, never()).getDefinedFilterNames();
    }

    @Test
    public void retrieveManagedStudyConfigurations() throws CSException {
        Study study = new Study();
        Set<Study> studies = new HashSet<Study>();
        studies.add(study);
        study.setId(Long.valueOf(1));
        study.setStudyConfiguration(studyConfiguration);
        Set<StudyConfiguration> managedStudies = securityManager.retrieveManagedStudyConfigurations(USERNAME, studies);
        assertTrue(managedStudies.contains(studyConfiguration));
        assertEquals(1, managedStudies.size());
    }

    @Test
    public void retrieveManagedStudyConfigurationsUserDoesNotExist() throws CSException {
        Study study = new Study();
        Set<Study> studies = new HashSet<Study>();
        studies.add(study);
        study.setId(Long.valueOf(1));
        study.setStudyConfiguration(studyConfiguration);
        Set<StudyConfiguration> managedStudies = securityManager.retrieveManagedStudyConfigurations(USER_DOES_NOT_EXIST, studies);
        assertFalse(managedStudies.contains(studyConfiguration));
        assertEquals(0, managedStudies.size());
    }

    @Test
    public void testCreateProtectionElementForAuthorizedStudyElementsGroup() throws CSException {
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_EXISTS);
        studyConfiguration.setUserWorkspace(userWorkspace);
        securityManager.createProtectionElement(studyConfiguration, authorizedStudyElementsGroup);
        verify(authManager, times(1)).createProtectionElement(any(ProtectionElement.class));

        // test with invalid user
        securityManager.deleteProtectionElement(authorizedStudyElementsGroup);

        userWorkspace = new UserWorkspace();
        userWorkspace.setUsername("NAMENOTFOUND");
        studyConfiguration.setUserWorkspace(userWorkspace);
        securityManager.createProtectionElement(studyConfiguration, authorizedStudyElementsGroup);
        verify(authManager, times(1)).createProtectionElement(any(ProtectionElement.class));

        // test if authorizedStudyElementsGroup ID column is null in database
        securityManager.deleteProtectionElement(authorizedStudyElementsGroup);
        authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementsGroup.setId(null); // simulate null id
        Group group = new Group();
        group.setGroupName("Unit Test Group Name");
        authorizedStudyElementsGroup.setAuthorizedGroup(group);
        assertEquals("null", String.valueOf(authorizedStudyElementsGroup.getId()));
        userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_EXISTS);
        studyConfiguration.setUserWorkspace(userWorkspace);

        boolean thrown = false;
        try {
            securityManager.createProtectionElement(studyConfiguration, authorizedStudyElementsGroup);
        } catch (CSException e) {
          thrown = true;
        }
        assertTrue(thrown);
        verify(authManager, times(1)).createProtectionElement(any(ProtectionElement.class));
    }

    @Test
    public void testDeleteProtectionElementForAuthorizedStudyElementsGroup() throws CSException {
        securityManager.deleteProtectionElement(authorizedStudyElementsGroup);
        verify(authManager, times(1)).removeProtectionElement(anyString());
    }

    /**
     * Tests retrieval of unauthorized groups.
     * @throws CSException on error
     */
    @Test
    public void getUnauthorizedGroups() throws CSException {
        Collection<Group> groups = securityManager.getUnauthorizedGroups(studyConfiguration);
        assertFalse(groups.isEmpty());
        assertEquals(1, groups.size());

        Group group = groups.iterator().next();
        assertEquals("Group Name", group.getGroupName());

        Group authorizedGroup = new Group();
        authorizedGroup.setGroupName("Group Name");
        authorizedGroup.setGroupDesc("Group Description");

        AuthorizedStudyElementsGroup authorizedStudyElementGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementGroup.setAuthorizedGroup(authorizedGroup);

        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementGroup);

        groups = securityManager.getUnauthorizedGroups(studyConfiguration);
        assertTrue(groups.isEmpty());
    }
}
