/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.security.SecurityManager;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.EditAuthorizedGroupAction;
import gov.nih.nci.caintegrator.web.transfer.QueryNode;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        action.setWorkspaceService(workspaceService);

        Group group = new Group();
        group.setGroupName("Test Group");
        group.setGroupDesc("Description");

        authorizationManager = mock(AuthorizationManager.class);
        securityManager = mock(SecurityManager.class);
        when(securityManager.getUnauthorizedGroups(any(StudyConfiguration.class))).thenReturn(Arrays.asList(group));
        when(securityManager.getAuthorizationManager()).thenReturn(authorizationManager);
        action.setSecurityManager(securityManager);

        studyManagementService = mock(StudyManagementService.class);
        when(studyManagementService.getRefreshedEntity(any(AbstractCaIntegrator2Object.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object arg = invocation.getArguments()[0];
                if (arg instanceof AnnotationFieldDescriptor) {
                    AnnotationFieldDescriptor descriptor = (AnnotationFieldDescriptor) arg;
                    AnnotationDefinition definition = new AnnotationDefinition();
                    definition.setDataType(AnnotationTypeEnum.STRING);
                    descriptor.setDefinition(definition);
                    arg = descriptor;
                }
                return arg;
            }
        });
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
    @SuppressWarnings("unchecked")
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
        assertNotNull(action.getTrees());
    }

    /**
     * Tests saving an authorized user group.
     */
    @Test
    public void save() {
        action.setSelectedDescriptorIds(Arrays.asList(1L, 2L, 3L));
        action.setSelectedDataSourceIds(Arrays.asList(1L, 2L, 3L));
        List<QueryNode> nodes = new ArrayList<QueryNode>();
        for (int i = 0; i < 5; i++) {
            QueryNode node = new QueryNode();
            node.setAnnotationDefinitionId(1L);
            node.setValue(RandomStringUtils.randomAscii(8));
            nodes.add(node);
        }
        nodes.add(null);
        action.setSelectedQueryParameters(nodes);
        assertEquals(Action.SUCCESS, action.save());
    }
}
