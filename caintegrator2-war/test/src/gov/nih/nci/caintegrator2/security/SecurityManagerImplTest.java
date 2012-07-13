/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
