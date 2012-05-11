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
package gov.nih.nci.caintegrator2.web.transfer;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class AuthorizationTrees {
    private final List<TreeNode> annotationGroups = new ArrayList<TreeNode>();
    private final List<TreeNode> expressionDataSources = new ArrayList<TreeNode>();
    private final List<TreeNode> copyNumberDataSources = new ArrayList<TreeNode>();

    /**
     * Class constructor.
     * @param studyConfig the study configuration
     * @param authGroup the group authorization
     */
    public AuthorizationTrees(StudyConfiguration studyConfig, final AuthorizedStudyElementsGroup authGroup) {
        for (GenomicDataSourceConfiguration config : studyConfig.getGenomicDataSources()) {
            if (config.getDataType() == PlatformDataTypeEnum.EXPRESSION) {
                TreeNode node = new TreeNode(config.getId(), TreeNodeTypeEnum.DATA_SOURCE,
                        config.getExperimentIdentifier());
                node.setSelected(isSelected(config, authGroup));
                expressionDataSources.add(node);
            } else if (config.getDataType() == PlatformDataTypeEnum.COPY_NUMBER) {
                TreeNode node = new TreeNode(config.getId(), TreeNodeTypeEnum.DATA_SOURCE,
                        config.getExperimentIdentifier());
                node.setSelected(isSelected(config, authGroup));
                copyNumberDataSources.add(node);
            }
        }

        for (AnnotationGroup group : studyConfig.getStudy().getSortedAnnotationGroups()) {
            TreeNode node = new TreeNode(null, TreeNodeTypeEnum.PARENT, group.getName());
            node.getChildren().addAll(Collections2.transform(group.getAnnotationFieldDescriptors(),
                    new Function<AnnotationFieldDescriptor, TreeNode>() {
                        @Override
                        public TreeNode apply(AnnotationFieldDescriptor afd) {
                            TreeNode node = new TreeNode(afd.getId(), TreeNodeTypeEnum.FIELD_DESCRIPTOR, afd.getName());
                            node.setSelected(isSelected(afd, authGroup));
                            return node;
                        }

            }));
            annotationGroups.add(node);
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

    /**
     * @return the expression data sources
     */
    public List<TreeNode> getExpressionDataSources() {
        return expressionDataSources;
    }

    /**
     * @return the copy number data sources
     */
    public List<TreeNode> getCopyNumberDataSources() {
        return copyNumberDataSources;
    }

    /**
     * @return the annotationGroups
     */
    public List<TreeNode> getAnnotationGroups() {
        return annotationGroups;
    }
}
