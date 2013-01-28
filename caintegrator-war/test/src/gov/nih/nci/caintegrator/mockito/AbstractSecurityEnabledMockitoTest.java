/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package gov.nih.nci.caintegrator.mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.security.AuthorizationManagerFactory;
import gov.nih.nci.caintegrator.security.SecurityManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * Base test class that sets up authorization and security manager mocks.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public abstract class AbstractSecurityEnabledMockitoTest extends AbstractMockitoTest {
    protected static final String USER_EXISTS = "userExists";
    protected static final String USERNAME = "username";
    protected AuthorizationManagerFactory authManagerFactory;
    protected AuthorizationManager authManager;
    protected SecurityManager secManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    public void setUpMocks() throws Exception {
        super.setUpMocks();
        setUpAuthManager();
        setUpSecurityManager();
        authManagerFactory = mock(AuthorizationManagerFactory.class);
        when(authManagerFactory.getAuthorizationManager(anyString())).thenReturn(authManager);
    }

    protected void setUpAuthManager() throws Exception {
        authManager = mock(AuthorizationManager.class);
        User user = new User();
        user.setUserId(1L);
        when(authManager.getUser(eq(USER_EXISTS))).thenReturn(user);
        when(authManager.getUser(eq(USERNAME))).thenReturn(user);

        when(authManager.getObjects(any(SearchCriteria.class))).thenAnswer(new Answer<List<?>>() {
            @Override
            public List<?> answer(InvocationOnMock invocation) throws Throwable {
                SearchCriteria criteria = (SearchCriteria) invocation.getArguments()[0];
                if (criteria instanceof ProtectionElementSearchCriteria) {
                    return Arrays.asList(new ProtectionElement());
                } else if (criteria instanceof GroupSearchCriteria) {
                    Group g = new Group();
                    g.setGroupName("Group Name");
                    g.setGroupDesc("Group Description");
                    return Arrays.asList(g);
                }
                return null;
            }
        });
        when(authManager.getGroups(anyString())).thenAnswer(new Answer<Set<Group>>() {
            @Override
            public Set<Group> answer(InvocationOnMock invocation) throws Throwable {
                Group group = new Group();
                group.setGroupId(Long.valueOf(1));
                Set<Group> groups = new HashSet<Group>();
                groups.add(group);
                return groups;
            }
        });
        when(authManager.getProtectionElements(anyString())).thenAnswer(new Answer<Set<ProtectionElement>>() {
            @Override
            public Set<ProtectionElement> answer(InvocationOnMock invocation) throws Throwable {
                Set<ProtectionElement> protectionElements = new HashSet<ProtectionElement>();
                ProtectionElement element1 = new ProtectionElement();
                element1.setObjectId("gov.nih.nci.caintegrator.domain.translational.Study");
                element1.setAttribute("id");
                element1.setValue("1");
                protectionElements.add(element1);
                ProtectionElement element2 = new ProtectionElement();
                element2.setObjectId("Invalid");
                protectionElements.add(element2);
                ProtectionElement element3 = new ProtectionElement();
                element3.setObjectId("gov.nih.nci.caintegrator.domain.translational.Study");
                element3.setValue("invalidId");
                protectionElements.add(element3);
                ProtectionElement element4 = new ProtectionElement();
                element4.setObjectId("gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup");
                element4.setValue("invalidId");
                protectionElements.add(element4);
                return protectionElements;
            }
        });
        when(authManager.getProtectionGroupRoleContextForGroup(anyString())).thenAnswer(new Answer<Set<ProtectionGroupRoleContext>>() {
            @Override
            public Set<ProtectionGroupRoleContext> answer(InvocationOnMock invocation) throws Throwable {
                Set<ProtectionGroupRoleContext> pgrcs = new HashSet<ProtectionGroupRoleContext>();
                ProtectionGroupRoleContext pgrc1 = new ProtectionGroupRoleContext();
                Set<Role> rolesValid = new HashSet<Role>();
                Role validRole = new Role();
                validRole.setName("STUDY_MANAGER_ROLE");
                rolesValid.add(validRole);
                pgrc1.setRoles(rolesValid);
                ProtectionGroup pg1 = new ProtectionGroup();
                pg1.setProtectionGroupId(1l);
                pgrc1.setProtectionGroup(pg1);

                ProtectionGroupRoleContext pgrc2 = new ProtectionGroupRoleContext();
                Set<Role> rolesInvalid = new HashSet<Role>();
                Role invalidRole = new Role();
                invalidRole.setName("invalid_role");
                rolesInvalid.add(invalidRole);
                pgrc2.setRoles(rolesInvalid);
                ProtectionGroup pg2 = new ProtectionGroup();
                pg2.setProtectionGroupId(2l);
                pgrc2.setProtectionGroup(pg2);
                pgrcs.add(pgrc1);
                pgrcs.add(pgrc2);
                return pgrcs;
            }
        });
    }

    private void setUpSecurityManager() throws Exception {
        secManager = mock(SecurityManager.class);
        when(secManager.getAuthorizationManager()).thenReturn(authManager);
        when(secManager.doesUserExist(eq(USER_EXISTS))).thenReturn(Boolean.TRUE);
        when(secManager.doesUserExist(eq(USERNAME))).thenReturn(Boolean.TRUE);
        when(secManager.retrieveManagedStudyConfigurations(anyString(), anyCollectionOf(Study.class))).thenAnswer(new Answer<Set<StudyConfiguration>>() {

            @Override
            public Set<StudyConfiguration> answer(InvocationOnMock invocation) throws Throwable {
                Set<StudyConfiguration> results = new HashSet<StudyConfiguration>();
                Collection<Study> studies = (Collection<Study>) invocation.getArguments()[1];
                if (CollectionUtils.isNotEmpty(studies)) {
                    for (Study study : studies) {
                        results.add(study.getStudyConfiguration());
                    }
                }
                return results;
            }
        });
        when(secManager.retrieveAuthorizedStudyElementsGroupsForInvestigator(anyString(), anySetOf(AuthorizedStudyElementsGroup.class)))
            .thenReturn(new HashSet<AuthorizedStudyElementsGroup>());
        when(secManager.getUnauthorizedGroups(any(StudyConfiguration.class))).thenReturn(new ArrayList<Group>());
    }
}
