/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedQuery;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.caintegrator2.web.transfer.AuthorizationTrees;
import gov.nih.nci.caintegrator2.web.transfer.QueryNode;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Action for handling adding, editing and deleting authorization groups.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class EditAuthorizedGroupAction extends AbstractStudyAction {
    private static final long serialVersionUID = 1L;
    private static final int MAX_QUERY_NAME_LENGTH = 70;
    private SecurityManager securityManager;
    private AuthorizedStudyElementsGroup authorizedGroup = new AuthorizedStudyElementsGroup();
    private Collection<Group> unauthorizedGroups = new ArrayList<Group>();
    private List<User> groupMembers = new ArrayList<User>();
    private Long selectedGroupId;
    private AuthorizationTrees trees;
    private List<Long> selectedDescriptorIds = new ArrayList<Long>();
    private List<Long> selectedDataSourceIds = new ArrayList<Long>();
    private List<QueryNode> selectedQueryParameters = new ArrayList<QueryNode>();

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
            group.setAuthorizedGroup(getSelectedCsmGroup());
            getStudyManagementService().addAuthorizedStudyElementsGroup(getStudyConfiguration(), group);
        } catch (CSException e) {
            addActionError("Error adding group authorization: " + e.getMessage());
            LOG.error("Error adding group authorization", e);
            return ERROR;
        }
        return SUCCESS;
    }

    private Group getSelectedCsmGroup() throws CSException {
        String groupId = String.valueOf(getSelectedGroupId());
        return getSecurityManager().getAuthorizationManager().getGroupById(groupId);
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
        Collection<QueryNode> filteredParams = Collections2.filter(getSelectedQueryParameters(),
                new Predicate<QueryNode>() {
            @Override
            public boolean apply(QueryNode node) {
                return node != null;
            }
        });
        handleAuthorizedQueries(filteredParams);
        getStudyManagementService().daoSave(getAuthorizedGroup());
        return SUCCESS;
    }

    private void handleAuthorizedQueries(Collection<QueryNode> filteredParams) {
        final Map<Long, List<String>> mappedResults = new HashMap<Long, List<String>>();
        for (QueryNode node : filteredParams) {
            if (CollectionUtils.isEmpty(mappedResults.get(node.getAnnotationDefinitionId()))) {
                List<String> params = new ArrayList<String>();
                params.add(node.getValue());
                mappedResults.put(node.getAnnotationDefinitionId(), params);
            } else {
                mappedResults.get(node.getAnnotationDefinitionId()).add(node.getValue());
            }
        }
        getAuthorizedGroup().getAuthorizedQuerys().clear();
        for (Map.Entry<Long, List<String>> entry : mappedResults.entrySet()) {
            AuthorizedQuery authorizedQuery = createAuthorizedQuery(entry);
            getAuthorizedGroup().getAuthorizedQuerys().add(authorizedQuery);
        }
    }

    private AuthorizedQuery createAuthorizedQuery(Map.Entry<Long, List<String>> entry) {
        Query query = createQuery(entry.getKey(), entry.getValue());
        AuthorizedQuery authorizedQuery = new AuthorizedQuery();
        authorizedQuery.setAuthorizedStudyElementsGroup(getAuthorizedGroup());
        authorizedQuery.setQuery(query);
        return authorizedQuery;
    }

    private Query createQuery(Long descriptorId, List<String> values) {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setId(descriptorId);
        descriptor = getStudyManagementService().getRefreshedEntity(descriptor);

        Query query = new Query();
        // query name is 100 chars in the DB, so make sure the generated name isn't too long by abbreviated
        query.setName("Authorized Query created for " + StringUtils.abbreviate(descriptor.getDisplayName(),
                                                                               MAX_QUERY_NAME_LENGTH));
        query.setLastModifiedDate(new Date());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.OR);

        AnnotationTypeEnum descriptorType = descriptor.getDefinition().getDataType();

        for (String value : values) {
            if (descriptorType == AnnotationTypeEnum.NUMERIC) {
                query.getCompoundCriterion().getCriterionCollection()
                    .add(QueryUtil.createNumericComparisonCriterion(descriptor, value));
            } else if (descriptorType == AnnotationTypeEnum.DATE) {
                query.getCompoundCriterion().getCriterionCollection()
                    .add(QueryUtil.createDateComparisonCriterion(descriptor, value));
            } else {
                query.getCompoundCriterion().getCriterionCollection()
                    .add(QueryUtil.createStringComparisonCriterion(descriptor, value));
            }
        }
        return query;
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

    /**
     * @return the selectedQueryParameters
     */
    public List<QueryNode> getSelectedQueryParameters() {
        return selectedQueryParameters;
    }

    /**
     * @param selectedQueryParameters the selectedQueryParameters to set
     */
    public void setSelectedQueryParameters(List<QueryNode> selectedQueryParameters) {
        this.selectedQueryParameters = selectedQueryParameters;
    }
}
