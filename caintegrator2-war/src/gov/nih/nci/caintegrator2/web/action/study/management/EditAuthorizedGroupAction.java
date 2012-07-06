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
import org.apache.commons.lang.StringUtils;

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
