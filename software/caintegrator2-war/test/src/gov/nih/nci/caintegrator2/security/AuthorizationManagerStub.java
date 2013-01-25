/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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
import gov.nih.nci.security.dao.GroupSearchCriteria;
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
        } else if (searchCriteria instanceof GroupSearchCriteria) {
           Group g = new Group();
           g.setGroupName("Group Name");
           g.setGroupDesc("Group Description");
           List<Group> groups = new ArrayList<Group>();
           groups.add(g);
           return groups;
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
