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

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.caintegrator2.web.transfer.AuthorizationTrees;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Action for handling adding, editing and deleting authorization groups.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class EditAuthorizedGroupAction extends AbstractStudyAction {
    private static final long serialVersionUID = 1L;
    private SecurityManager securityManager;
    private AuthorizedStudyElementsGroup authorizedGroup = new AuthorizedStudyElementsGroup();
    private Collection<Group> unauthorizedGroups = new ArrayList<Group>();
    private List<User> groupMembers = new ArrayList<User>();
    private Long selectedGroupId;
    private AuthorizationTrees trees;
    private List<Long> selectedDescriptorIds = new ArrayList<Long>();
    private List<Long> selectedDataSourceIds = new ArrayList<Long>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getAuthorizedGroup().getId() != null) {
            authorizedGroup = getStudyManagementService().getRefreshedEntity(getAuthorizedGroup());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        try {
            setUnauthorizedGroups(getSecurityManager().getUnauthorizedGroups(getStudyConfiguration()));
        } catch (CSException e) {
            addActionError("Error retrieving listing of unauthorized groups: " + e.getMessage());
            LOG.error("Error retrieving listing of unauthorized groups.", e);
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Cancel authorized group action.
     * @return the struts2 forwarding result
     */
    public String cancel() {
        return SUCCESS;
    }

    /**
     * Adds the selected group as an authorized group.
     * @return the struts2 forwarding result
     */
    public String add() {
        try {
            AuthorizedStudyElementsGroup group = getAuthorizedGroup();
            group.setStudyConfiguration(getStudyConfiguration());
            group.setAuthorizedGroup(getSecurityManager().getAuthorizationManager().getGroupById(
                    String.valueOf(getSelectedGroupId())));
            getStudyConfiguration().getAuthorizedStudyElementsGroups().add(group);
            getStudyManagementService().daoSave(group);
        } catch (CSException e) {
            addActionError("Error adding group authorization: " + e.getMessage());
            LOG.error("Error adding group authorization", e);
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Deletes the selected authorized group.
     * @return the struts2 forwarding result
     */
    public String delete() {
        try {
            if (getAuthorizedGroup().getId() != null) {
                getStudyManagementService().deleteAuthorizedStudyElementsGroup(getStudyConfiguration(),
                        getAuthorizedGroup());
            }
        } catch (CSException e) {
            addActionError("Error deleting authorized group: " + e.getMessage());
            LOG.error("Error deleting authorized group.", e);
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Edit the selected authorized group.
     * @return the struts2 forwarding result
     */
    @SuppressWarnings("unchecked")
    public String edit() {
        getGroupMembers().addAll(getAuthorizedGroup().getAuthorizedGroup().getUsers());
        setTrees(new AuthorizationTrees(getStudyConfiguration(), getAuthorizedGroup()));
        return SUCCESS;
    }

    /**
     * Saves the currently selected authorized group.
     * @return the struts2 forwarding result
     */
    public String save() {
        getAuthorizedGroup().getAuthorizedAnnotationFieldDescriptors().clear();
        for (Long id : getSelectedDescriptorIds()) {
            AnnotationFieldDescriptor field = new AnnotationFieldDescriptor();
            field.setId(id);
            field = getStudyManagementService().getRefreshedEntity(field);
            AuthorizedAnnotationFieldDescriptor aafd = new AuthorizedAnnotationFieldDescriptor();
            aafd.setAnnotationFieldDescriptor(field);
            aafd.setAuthorizedStudyElementsGroup(getAuthorizedGroup());
            getAuthorizedGroup().getAuthorizedAnnotationFieldDescriptors().add(aafd);
        }
        getAuthorizedGroup().getAuthorizedGenomicDataSourceConfigurations().clear();
        for (Long id : getSelectedDataSourceIds()) {
            GenomicDataSourceConfiguration config = getStudyManagementService().getRefreshedGenomicSource(id);
            AuthorizedGenomicDataSourceConfiguration authDataSource = new AuthorizedGenomicDataSourceConfiguration();
            authDataSource.setGenomicDataSourceConfiguration(config);
            authDataSource.setAuthorizedStudyElementsGroup(getAuthorizedGroup());
            getAuthorizedGroup().getAuthorizedGenomicDataSourceConfigurations().add(authDataSource);
        }
        getStudyManagementService().daoSave(getAuthorizedGroup());
        return SUCCESS;
    }

    /**
     * @return the authorizedGroup
     */
    public AuthorizedStudyElementsGroup getAuthorizedGroup() {
        return authorizedGroup;
    }

    /**
     * @param authorizedGroup the authorizedGroup to set
     */
    public void setAuthorizedGroup(AuthorizedStudyElementsGroup authorizedGroup) {
        this.authorizedGroup = authorizedGroup;
    }

    /**
     * @return the securityManager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    /**
     * @param securityManager the securityManager to set
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * @return the unauthorizedGroups
     */
    public Collection<Group> getUnauthorizedGroups() {
        return unauthorizedGroups;
    }

    /**
     * @param unauthorizedGroups the unauthorizedGroups to set
     */
    public void setUnauthorizedGroups(Collection<Group> unauthorizedGroups) {
        this.unauthorizedGroups = unauthorizedGroups;
    }

    /**
     * @return the groupMembers
     */
    public List<User> getGroupMembers() {
        return groupMembers;
    }

    /**
     * @param groupMembers the groupMembers to set
     */
    public void setGroupMembers(List<User> groupMembers) {
        this.groupMembers = groupMembers;
    }

    /**
     * @return the selectedGroupId
     */
    public Long getSelectedGroupId() {
        return selectedGroupId;
    }

    /**
     * @param selectedGroupId the selectedGroupId to set
     */
    public void setSelectedGroupId(Long selectedGroupId) {
        this.selectedGroupId = selectedGroupId;
    }

    /**
     * @return the trees
     */
    public AuthorizationTrees getTrees() {
        return trees;
    }

    /**
     * @param trees the trees to set
     */
    public void setTrees(AuthorizationTrees trees) {
        this.trees = trees;
    }

    /**
     * @return the selectedDescriptorIds
     */
    public List<Long> getSelectedDescriptorIds() {
        return selectedDescriptorIds;
    }

    /**
     * @param selectedDescriptorIds the selectedDescriptorIds to set
     */
    public void setSelectedDescriptorIds(List<Long> selectedDescriptorIds) {
        this.selectedDescriptorIds = selectedDescriptorIds;
    }

    /**
     * @return the selectedDataSourceIds
     */
    public List<Long> getSelectedDataSourceIds() {
        return selectedDataSourceIds;
    }

    /**
     * @param selectedDataSourceIds the selectedDataSourceIds to set
     */
    public void setSelectedDataSourceIds(List<Long> selectedDataSourceIds) {
        this.selectedDataSourceIds = selectedDataSourceIds;
    }
}
