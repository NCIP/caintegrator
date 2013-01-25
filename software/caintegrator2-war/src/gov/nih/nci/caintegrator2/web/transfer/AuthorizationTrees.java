/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.transfer;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedQuery;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class AuthorizationTrees {
    private final Set<TreeNode> annotationGroups = new TreeSet<TreeNode>();
    private final Set<TreeNode> expressionDataSources = new TreeSet<TreeNode>();
    private final Set<TreeNode> copyNumberDataSources = new TreeSet<TreeNode>();
    private final Set<TreeNode> fieldDescriptors = new TreeSet<TreeNode>();

    /**
     * Class constructor.
     * @param studyConfig the study configuration
     * @param authGroup the group authorization
     */
    public AuthorizationTrees(StudyConfiguration studyConfig, final AuthorizedStudyElementsGroup authGroup) {
        handleDataSources(studyConfig, authGroup);
        handleAnnotationGroups(studyConfig, authGroup);
        handleAnnotationFieldDescriptors(studyConfig, authGroup);
    }

    private void handleAnnotationFieldDescriptors(StudyConfiguration studyConfig,
            final AuthorizedStudyElementsGroup authGroup) {
        for (final AnnotationFieldDescriptor descriptor : studyConfig.getStudy().getAuthorizedFieldDescriptors()) {
            TreeNode node = new TreeNode(descriptor.getId(), descriptor.getDisplayName(), false);
            Set<String> permissibleValues = PermissibleValueUtil.getDisplayPermissibleValue(descriptor
                .getPermissibleValues());
            node.getChildren().addAll(Collections2.transform(permissibleValues, new Function<String, TreeNode>() {
                @Override
                public TreeNode apply(String value) {
                    TreeNode node = new TreeNode(null, value, isSelected(descriptor, value, authGroup));
                    return node;
                }
            }));
            fieldDescriptors.add(node);
        }
    }

    private void handleAnnotationGroups(StudyConfiguration studyConfig, final AuthorizedStudyElementsGroup authGroup) {
        for (AnnotationGroup group : studyConfig.getStudy().getSortedAnnotationGroups()) {
            TreeNode node = new TreeNode(null, group.getName(), false);
            node.getChildren().addAll(Collections2.transform(group.getAnnotationFieldDescriptors(),
                    new Function<AnnotationFieldDescriptor, TreeNode>() {
                @Override
                public TreeNode apply(AnnotationFieldDescriptor afd) {
                    TreeNode node = new TreeNode(afd.getId(), afd.getName(), isSelected(afd, authGroup));
                    return node;
                }

            }));
            annotationGroups.add(node);
        }
    }

    private void handleDataSources(StudyConfiguration studyConfig, final AuthorizedStudyElementsGroup authGroup) {
        for (GenomicDataSourceConfiguration config : studyConfig.getGenomicDataSources()) {
            TreeNode node = new TreeNode(config.getId(), config.getExperimentIdentifier(),
                    isSelected(config, authGroup));
            if (config.getDataType() == PlatformDataTypeEnum.EXPRESSION) {
                expressionDataSources.add(node);
            } else if (config.getDataType() == PlatformDataTypeEnum.COPY_NUMBER) {
                copyNumberDataSources.add(node);
            }
        }
    }

    private boolean isSelected(GenomicDataSourceConfiguration dataSource, AuthorizedStudyElementsGroup authGroup) {
        boolean results = false;
        for (AuthorizedGenomicDataSourceConfiguration ds : authGroup.getAuthorizedGenomicDataSourceConfigurations()) {
            if (ds.getGenomicDataSourceConfiguration().getId().equals(dataSource.getId())) {
                results = true;
                break;
            }
        }
        return results;
    }

    private boolean isSelected(AnnotationFieldDescriptor descriptor, AuthorizedStudyElementsGroup authGroup) {
        boolean results = false;
        for (AuthorizedAnnotationFieldDescriptor d : authGroup.getAuthorizedAnnotationFieldDescriptors()) {
            if (d.getAnnotationFieldDescriptor().getId().equals(descriptor.getId())) {
                results = true;
                break;
            }
        }
        return results;
    }

    private boolean isSelected(AnnotationFieldDescriptor descriptor, String value,
            AuthorizedStudyElementsGroup authGroup) {
        for (AuthorizedQuery query : authGroup.getAuthorizedQuerys()) {
            if (isQuerySelected(descriptor, value, query)) {
                return true;
            }
        }
        return false;
    }

    private boolean isQuerySelected(AnnotationFieldDescriptor descriptor, String value, AuthorizedQuery query) {
        for (AbstractCriterion crit : query.getQuery().getCompoundCriterion().getCriterionCollection()) {
            if (crit instanceof AbstractComparisonCriterion) {
                AbstractComparisonCriterion compCrit = (AbstractComparisonCriterion) crit;
                if (compCrit.getAnnotationFieldDescriptor().equals(descriptor)
                        && StringUtils.equals(compCrit.getStringValue(), value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the expression data sources
     */
    public Set<TreeNode> getExpressionDataSources() {
        return expressionDataSources;
    }

    /**
     * @return the copy number data sources
     */
    public Set<TreeNode> getCopyNumberDataSources() {
        return copyNumberDataSources;
    }

    /**
     * @return the annotationGroups
     */
    public Set<TreeNode> getAnnotationGroups() {
        return annotationGroups;
    }

    /**
     * @return the fieldDescriptors
     */
    public Set<TreeNode> getFieldDescriptors() {
        return fieldDescriptors;
    }
}
