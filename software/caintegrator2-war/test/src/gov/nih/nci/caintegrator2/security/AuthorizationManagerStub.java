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

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.ApplicationContext;
import gov.nih.nci.security.authorization.domainobjects.FilterClause;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.InstanceLevelMappingElement;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.authorization.jaas.AccessPermission;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.exceptions.CSDataAccessException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;

import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;

public class AuthorizationManagerStub implements AuthorizationManager {
    public boolean createProtectionElementCalled;
    public boolean removeProtectionElementCalled;
    
    public void clear() {
        createProtectionElementCalled = false;
        removeProtectionElementCalled = false;
    }
    
    public void addGroupRoleToProtectionGroup(String protectionGroupId, String groupId, String[] rolesId)
            throws CSTransactionException {
        

    }

    public void addGroupsToUser(String userId, String[] groupIds) throws CSTransactionException {
        

    }

    public void addOwners(String protectionElementId, String[] userIds) throws CSTransactionException {
        

    }

    public void addPrivilegesToRole(String roleId, String[] privilegeIds) throws CSTransactionException {
        

    }

    public void addProtectionElements(String protectionGroupId, String[] protectionElementIds)
            throws CSTransactionException {
        

    }

    public void addToProtectionGroups(String protectionElementId, String[] protectionGroupIds)
            throws CSTransactionException {
        

    }

    public void addUserRoleToProtectionGroup(String userId, String[] rolesId, String protectionGroupId)
            throws CSTransactionException {
        

    }

    public void addUsersToGroup(String groupId, String[] userIds) throws CSTransactionException {
        

    }

    public void assignGroupRoleToProtectionGroup(String protectionGroupId, String groupId, String[] rolesId)
            throws CSTransactionException {
        

    }

    public void assignGroupsToUser(String userId, String[] groupIds) throws CSTransactionException {
        

    }

    public void assignOwners(String protectionElementId, String[] userIds) throws CSTransactionException {
        

    }

    public void assignParentProtectionGroup(String parentProtectionGroupId, String childProtectionGroupId)
            throws CSTransactionException {
        

    }

    public void assignPrivilegesToRole(String roleId, String[] privilegeIds) throws CSTransactionException {
        

    }

    public void assignProtectionElement(String protectionGroupName, String protectionElementObjectId)
            throws CSTransactionException {
        

    }

    public void assignProtectionElement(String protectionGroupName, String protectionElementObjectId,
            String protectionElementAttributeName) throws CSTransactionException {
        

    }

    public void assignProtectionElements(String protectionGroupId, String[] protectionElementIds)
            throws CSTransactionException {
        

    }

    public void assignToProtectionGroups(String protectionElementId, String[] protectionGroupIds)
            throws CSTransactionException {
        

    }

    public void assignUserRoleToProtectionGroup(String userId, String[] rolesId, String protectionGroupId)
            throws CSTransactionException {
        

    }

    public void assignUserToGroup(String userName, String groupName) throws CSTransactionException {
        

    }

    public void assignUsersToGroup(String groupId, String[] userIds) throws CSTransactionException {
        

    }

    public boolean checkOwnership(String userName, String protectionElementObjectId) {
        
        return false;
    }

    public boolean checkPermission(AccessPermission permission, Subject subject) throws CSException {
        
        return false;
    }

    public boolean checkPermission(AccessPermission permission, String userName) throws CSException {
        
        return false;
    }

    public boolean checkPermission(String userName, String objectId, String privilegeName) throws CSException {
        
        return false;
    }

    public boolean checkPermission(String userName, String objectId, String attributeName, String privilegeName)
            throws CSException {
        
        return false;
    }

    public boolean checkPermission(String userName, String objectId, String attributeName, String attributeValue,
            String privilegeName) throws CSException {
        
        return false;
    }

    public boolean checkPermissionForGroup(String groupName, String objectId, String privilegeName) throws CSException {
        
        return false;
    }

    public boolean checkPermissionForGroup(String groupName, String objectId, String attributeName, String privilegeName)
            throws CSException {
        
        return false;
    }

    public boolean checkPermissionForGroup(String groupName, String objectId, String attributeName,
            String attributeValue, String privilegeName) throws CSException {
        
        return false;
    }

    public void createApplication(Application application) throws CSTransactionException {
        

    }

    public void createFilterClause(FilterClause filterClause) throws CSTransactionException {
        

    }

    public void createGroup(Group group) throws CSTransactionException {
        

    }

    public void createPrivilege(Privilege privilege) throws CSTransactionException {
        

    }

    public void createProtectionElement(ProtectionElement protectionElement) throws CSTransactionException {
        createProtectionElementCalled = true;

    }

    public void createProtectionGroup(ProtectionGroup protectionGroup) throws CSTransactionException {
        

    }

    public void createRole(Role role) throws CSTransactionException {
        

    }

    public void createUser(User user) throws CSTransactionException {
        

    }

    public void deAssignProtectionElements(String protectionGroupName, String protectionElementObjectId)
            throws CSTransactionException {
        

    }
    @SuppressWarnings("unchecked") // CSM API is untyped
    public List getAccessibleGroups(String objectId, String privilegeName) throws CSException {
        
        return null;
    }
    @SuppressWarnings("unchecked") // CSM API is untyped
    public List getAccessibleGroups(String objectId, String attributeName, String privilegeName) throws CSException {
        
        return null;
    }

    public Application getApplication(String applicationContextName) throws CSObjectNotFoundException {
        
        return null;
    }

    public Application getApplicationById(String applicationId) throws CSObjectNotFoundException {
        
        return null;
    }

    public ApplicationContext getApplicationContext() {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public List getAttributeMap(String userName, String className, String privilegeName) {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public List getAttributeMapForGroup(String groupName, String className, String privilegeName) {
        
        return null;
    }

    public FilterClause getFilterClauseById(String filterClauseId) throws CSObjectNotFoundException {
        
        return null;
    }

    public Group getGroupById(String groupId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getGroups(String userId) throws CSObjectNotFoundException {
        Group group = new Group();
        group.setGroupId(Long.valueOf(1));
        Set<Group> groups = new HashSet<Group>();
        groups.add(group);
        return groups;
    }
    
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public List getObjects(SearchCriteria searchCriteria) {
        if (searchCriteria instanceof ProtectionElementSearchCriteria) {
            ProtectionElement pe = new ProtectionElement();
            List<ProtectionElement> elements = new ArrayList<ProtectionElement>();
            elements.add(pe);
            return elements;
        }
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getOwners(String protectionElementId) throws CSObjectNotFoundException {
        
        return null;
    }

    public Principal[] getPrincipals(String userName) {
        
        return null;
    }

    public Privilege getPrivilegeById(String privilegeId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Collection getPrivilegeMap(String userName, Collection protectionElements) throws CSException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getPrivileges(String roleId) throws CSObjectNotFoundException {
        
        return null;
    }

    public ProtectionElement getProtectionElement(String objectId) throws CSObjectNotFoundException {
        
        return null;
    }

    public ProtectionElement getProtectionElement(String objectId, String attributeName) {
        
        return null;
    }

    public ProtectionElement getProtectionElementById(String protectionElementId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getProtectionElementPrivilegeContextForGroup(String groupId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getProtectionElementPrivilegeContextForUser(String userId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getProtectionElements(String protectionGroupId) throws CSObjectNotFoundException {
        Set<ProtectionElement> protectionElements = new HashSet<ProtectionElement>();
        ProtectionElement element1 = new ProtectionElement();
        element1.setObjectId("gov.nih.nci.caintegrator2.domain.translational.Study");
        element1.setAttribute("id");
        element1.setValue("1");
        protectionElements.add(element1);
        ProtectionElement element2 = new ProtectionElement();
        element2.setObjectId("Invalid");
        protectionElements.add(element2);
        ProtectionElement element3 = new ProtectionElement();
        element3.setObjectId("gov.nih.nci.caintegrator2.domain.translational.Study");
        element3.setValue("invalidId");
        protectionElements.add(element3);
        ProtectionElement element4 = new ProtectionElement();
        element4.setObjectId("gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup");
        element4.setValue("invalidId");
        protectionElements.add(element4);        
        return protectionElements;
    }

    public ProtectionGroup getProtectionGroupById(String protectionGroupId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getProtectionGroupRoleContextForGroup(String groupId) throws CSObjectNotFoundException {
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
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getProtectionGroupRoleContextForUser(String userId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public List getProtectionGroups() {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getProtectionGroups(String protectionElementId) throws CSObjectNotFoundException {
        
        return null;
    }

    public Role getRoleById(String roleId) throws CSObjectNotFoundException {
        
        return null;
    }

    public User getUser(String loginName) {
        User user = new User();
        user.setUserId(Long.valueOf(1));
        SecurityManagerStub securityManagerStub = new SecurityManagerStub();
        if (securityManagerStub.doesUserExist(loginName)) {
            return user;
        }
        return null;
    }

    public User getUserById(String userId) throws CSObjectNotFoundException {
        
        return null;
    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Set getUsers(String groupId) throws CSObjectNotFoundException {
        
        return null;
    }

    public void initialize(String applicationContextName) {
        

    }

    public void initialize(String applicationContextName, URL url) {
        

    }

    public void modifyApplication(Application application) throws CSTransactionException {
        

    }

    public void modifyFilterClause(FilterClause filterClause) throws CSTransactionException {
        

    }

    public void modifyGroup(Group group) throws CSTransactionException {
        

    }

    public void modifyPrivilege(Privilege privilege) throws CSTransactionException {
        

    }

    public void modifyProtectionElement(ProtectionElement protectionElement) throws CSTransactionException {
        

    }

    public void modifyProtectionGroup(ProtectionGroup protectionGroup) throws CSTransactionException {
        

    }

    public void modifyRole(Role role) throws CSTransactionException {
        

    }

    public void modifyUser(User user) throws CSTransactionException {
        

    }

    public void removeApplication(String applicationId) throws CSTransactionException {
        

    }

    public void removeFilterClause(String filterClauseId) throws CSTransactionException {
        

    }

    public void removeGroup(String groupId) throws CSTransactionException {
        

    }

    public void removeGroupFromProtectionGroup(String protectionGroupId, String groupId) throws CSTransactionException {
        

    }

    public void removeGroupRoleFromProtectionGroup(String protectionGroupId, String groupId, String[] roleId)
            throws CSTransactionException {
        

    }

    public void removeOwnerForProtectionElement(String protectionElementObjectId, String[] userNames)
            throws CSTransactionException {
        

    }

    public void removeOwnerForProtectionElement(String userName, String protectionElementObjectId,
            String protectionElementAttributeName) throws CSTransactionException {
        

    }

    public void removePrivilege(String privilegeId) throws CSTransactionException {
        

    }

    public void removeProtectionElement(String protectionElementId) throws CSTransactionException {
        removeProtectionElementCalled = true;

    }

    public void removeProtectionElementsFromProtectionGroup(String protectionGroupId, String[] protectionElementIds)
            throws CSTransactionException {
        

    }

    public void removeProtectionGroup(String protectionGroupId) throws CSTransactionException {
        

    }

    public void removeRole(String roleId) throws CSTransactionException {
        

    }

    public void removeUser(String userId) throws CSTransactionException {
        

    }

    public void removeUserFromGroup(String groupId, String userId) throws CSTransactionException {
        

    }

    public void removeUserFromProtectionGroup(String protectionGroupId, String userId) throws CSTransactionException {
        

    }

    public void removeUserRoleFromProtectionGroup(String protectionGroupId, String userId, String[] rolesId)
            throws CSTransactionException {
        

    }
    @SuppressWarnings({ "unchecked" }) // CSM API is untyped
    public Collection secureCollection(String userName, Collection objects) throws CSException {
        
        return null;
    }

    public Object secureObject(String userName, Object obj) throws CSException {
        
        return null;
    }

    public Object secureUpdate(String userName, Object originalObject, Object mutatedObject) throws CSException {
        
        return null;
    }

    public void setAuditUserInfo(String userName, String sessionId) {
        

    }

    public void setEncryptionEnabled(boolean isEncryptionEnabled) {
        

    }

    public void setOwnerForProtectionElement(String protectionElementObjectId, String[] userNames)
            throws CSTransactionException {
        

    }

    public void setOwnerForProtectionElement(String userName, String protectionElementObjectId,
            String protectionElementAttributeName) throws CSTransactionException {
        

    }

    public void createInstanceLevelMappingElement(InstanceLevelMappingElement instanceLevelMappingElement)
            throws CSTransactionException {
    }

    public InstanceLevelMappingElement getInstanceLevelMappingElementById(String instanceLevelMappingElementId)
            throws CSObjectNotFoundException {
        return null;
    }

    public void maintainInstanceTables(String instanceLevelMappingElementId) throws CSObjectNotFoundException,
            CSDataAccessException {
    }

    public void modifyInstanceLevelMappingElement(InstanceLevelMappingElement instanceLevelMappingElement)
            throws CSTransactionException {
    }

    public void refreshInstanceTables(boolean instanceLevelSecurityForUser) throws CSObjectNotFoundException,
            CSDataAccessException {
    }

    public void removeInstanceLevelMappingElement(String instanceLevelMappingElementId) throws CSTransactionException {
    }

}
