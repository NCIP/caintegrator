/**
 * Copyright (c) 2012, 5AM Solutions, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the author nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 * Tests for authorization group actions.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class EditAuthorizedGroupActionTest extends AbstractSessionBasedTest {
    private SecurityManager securityManager;
    private AuthorizationManager authorizationManager;
    private StudyManagementService studyManagementService;
    private EditAuthorizedGroupAction action;

    /**
     * Sets up the tests.
     */
    @Before
    public void prepareTest() throws Exception {
        super.setUp();
        action = new EditAuthorizedGroupAction();
        action.setWorkspaceService(new WorkspaceServiceStub());

        Group group = new Group();
        group.setGroupName("Test Group");
        group.setGroupDesc("Description");

        authorizationManager = mock(AuthorizationManager.class);
        securityManager = mock(SecurityManager.class);
        when(securityManager.getUnauthorizedGroups(any(StudyConfiguration.class))).thenReturn(Arrays.asList(group));
        when(securityManager.getAuthorizationManager()).thenReturn(authorizationManager);
        action.setSecurityManager(securityManager);

        studyManagementService = mock(StudyManagementService.class);
        action.setStudyManagementService(studyManagementService);
        action.clearErrorsAndMessages();
    }

    /**
     * Tests action prepare functionality.
     */
    @Test
    public void prepare() {
        action.prepare();
        verify(studyManagementService, times(0)).getRefreshedEntity(any(AuthorizedStudyElementsGroup.class));
        action.getAuthorizedGroup().setId(1L);
        action.prepare();
        verify(studyManagementService, times(1)).getRefreshedEntity(any(AuthorizedStudyElementsGroup.class));
    }

    /**
     * Tests execute method.
     */
    @Test
    public void execute() {
        assertEquals(Action.SUCCESS, action.execute());
        assertFalse(action.getUnauthorizedGroups().isEmpty());
        assertEquals(1, action.getUnauthorizedGroups().size());
    }

    /**
     * Tests execution when an exception has been thrown trying to retrieve unauthorized groups.
     */
    @Test
    public void executeException() throws CSException {
        when(securityManager.getUnauthorizedGroups(any(StudyConfiguration.class))).thenThrow(new CSException());
        assertEquals(Action.ERROR, action.execute());
    }

    /**
     * Tests cancellation method.
     */
    @Test
    public void cancel() {
        assertEquals(Action.SUCCESS, action.cancel());
    }

    /**
     * Tests adding an authorized group.
     */
    @Test
    public void add() {
        assertEquals(Action.SUCCESS, action.add());
    }

    /**
     * Tests adding an authorized group in the case of an exception.
     */
    @Test
    public void addException() throws CSException {
        when(authorizationManager.getGroupById(anyString())).thenThrow(new CSObjectNotFoundException());
        assertEquals(Action.ERROR, action.add());
    }


    /**
     * Tests deletion method.
     */
    @Test
    public void delete() throws CSException {
        assertEquals(Action.SUCCESS, action.delete());
        verify(studyManagementService, times(0)).deleteAuthorizedStudyElementsGroup(any(StudyConfiguration.class),
                any(AuthorizedStudyElementsGroup.class));

        AuthorizedStudyElementsGroup group = new AuthorizedStudyElementsGroup();
        group.setId(1L);
        action.setAuthorizedGroup(group);
        assertEquals(Action.SUCCESS, action.delete());
        verify(studyManagementService, times(1)).deleteAuthorizedStudyElementsGroup(any(StudyConfiguration.class),
                any(AuthorizedStudyElementsGroup.class));
    }

    /**
     * Tests deletion in the presence of an exception.
     */
    @Test
    public void deleteException() throws Exception {
       doThrow(new CSException()).when(studyManagementService).deleteAuthorizedStudyElementsGroup(any(StudyConfiguration.class),
                any(AuthorizedStudyElementsGroup.class));
       AuthorizedStudyElementsGroup group = new AuthorizedStudyElementsGroup();
       group.setId(1L);
       action.setAuthorizedGroup(group);
       assertEquals(Action.ERROR, action.delete());
    }

    /**
     * Tests editing of an authorized user group.
     */
    @Test
    public void edit() {
        Group group = new Group();
        group.setGroupName("Test Group");
        group.setGroupDesc("Description");

        group.setUsers(new HashSet<User>());
        group.getUsers().add(new User());

        AuthorizedStudyElementsGroup authorizedGroup = new AuthorizedStudyElementsGroup();
        authorizedGroup.setAuthorizedGroup(group);
        action.setAuthorizedGroup(authorizedGroup);
        action.setSelectedGroupId(1L);
        action.setGroupMembers(new ArrayList<User>());
        assertEquals(Action.SUCCESS, action.edit());
    }
}
