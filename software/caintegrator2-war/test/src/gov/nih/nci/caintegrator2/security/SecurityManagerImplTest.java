/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.security.exceptions.CSException;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

public class SecurityManagerImplTest {

    private SecurityManagerImpl securityManager;
    private AuthorizationManagerFactoryStub authorizationManagerFactoryStub;
    private StudyConfiguration studyConfiguration;
    
    @Before
    public void setUp() {
        securityManager = new SecurityManagerImpl();
        authorizationManagerFactoryStub = new AuthorizationManagerFactoryStub();
        securityManager.setAuthorizationManagerFactory(authorizationManagerFactoryStub);
        authorizationManagerFactoryStub.authorizationManager.clear();
        studyConfiguration = new StudyConfiguration();
        UserWorkspace workspace = new UserWorkspace();
        studyConfiguration.setUserWorkspace(workspace);
        workspace.setUsername("user");
    }
    
    @Test
    public void testCreateProtectionElement() throws CSException {
        securityManager.createProtectionElement(studyConfiguration);
        assertTrue(authorizationManagerFactoryStub.authorizationManager.createProtectionElementCalled);
    }

    @Test
    public void testDeleteProtectionElement() throws CSException {
        securityManager.deleteProtectionElement(studyConfiguration);
        assertTrue(authorizationManagerFactoryStub.authorizationManager.removeProtectionElementCalled);
    }
    
    @Test
    public void testInitializeFiltersForUserGroups() throws CSException {
        Session session = new HibernateSessionStub();
        HibernateSessionStub hibernateSession = (HibernateSessionStub) session;
        hibernateSession.sessionFactory.clear();
        securityManager.initializeFiltersForUserGroups("username", session);
        assertTrue(hibernateSession.sessionFactory.getDefinedFilterNamesCalled);
    }
    
    @Test
    public void retrieveManagedStudyConfigurations() throws CSException {
        Study study = new Study();
        Set<Study> studies = new HashSet<Study>();
        studies.add(study);
        study.setId(Long.valueOf(1));
        study.setStudyConfiguration(studyConfiguration);
        Set<StudyConfiguration> managedStudies = securityManager.retrieveManagedStudyConfigurations("username", studies);
        assertTrue(managedStudies.contains(studyConfiguration));
        assertEquals(1, managedStudies.size());
    }

}
