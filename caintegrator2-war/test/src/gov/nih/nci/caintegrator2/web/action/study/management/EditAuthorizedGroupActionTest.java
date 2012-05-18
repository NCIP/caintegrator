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
package gov.nih.nci.caintegrator2.web.action.study.management;

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
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.transfer.QueryNode;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
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
        when(studyManagementService.getRefreshedEntity(any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[0];
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
