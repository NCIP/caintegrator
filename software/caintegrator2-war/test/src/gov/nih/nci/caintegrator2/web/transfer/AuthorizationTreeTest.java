/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedQuery;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class AuthorizationTreeTest {
    private int NUM_ANNOTATION_GROUPS = 5;
    private int NUM_ANNOTATION_DESCRIPTORS = 5;
    private int NUM_DATA_SOURCES = 10;
    private StudyConfiguration studyConfig;
    private StudyManagementService studyManagementService;
    private List<String> availableValues = Arrays.asList("Value 1" , "Value 2", "Value 3");

    /**
     * Sets up the data for the unit test.
     */
    @Before
    public void setUp() throws Exception {
        studyConfig = new StudyConfiguration();
        for (int i = 0; i < NUM_ANNOTATION_GROUPS; i++) {
            AnnotationGroup annotationGroup = new AnnotationGroup();
            annotationGroup.setId(Long.valueOf(i));
            annotationGroup.setName("Test Annotation Group #" + i);
            annotationGroup.setDescription("Test Annotation Group #" + i);
            for (int j = 0; j < NUM_ANNOTATION_DESCRIPTORS; j++) {
                AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
                descriptor.setId(Long.valueOf(j));
                descriptor.setName("Annotation Descriptor #" + j);
                descriptor.setAnnotationGroup(annotationGroup);
                descriptor.setShowInAuthorization(true);
                AnnotationDefinition def = new AnnotationDefinition();
                def.setDisplayName("Annotation Definition #" + j);
                def.addPermissibleValues(new HashSet<Object>(availableValues));
                descriptor.setDefinition(def);
                annotationGroup.getAnnotationFieldDescriptors().add(descriptor);
            }
            studyConfig.getStudy().getAnnotationGroups().add(annotationGroup);
        }

        for (int i = 0; i < NUM_DATA_SOURCES; i++) {
            GenomicDataSourceConfiguration dataSource = new GenomicDataSourceConfiguration();
            dataSource.setId(Long.valueOf(i));
            dataSource.setExperimentIdentifier("EXP-" + i);
            dataSource.setDataType(i % 2 == 0 ? PlatformDataTypeEnum.COPY_NUMBER : PlatformDataTypeEnum.EXPRESSION);
            dataSource.setStudyConfiguration(studyConfig);
            studyConfig.getGenomicDataSources().add(dataSource);
        }

        studyManagementService = mock(StudyManagementService.class);
    }

    /**
     * Tests generation of the authorization tree.
     */
    @Test
    public void construction() {
        AuthorizationTrees tree = new AuthorizationTrees(studyConfig, new AuthorizedStudyElementsGroup());
        assertEquals(NUM_DATA_SOURCES / 2, tree.getCopyNumberDataSources().size());
        for (TreeNode node : tree.getCopyNumberDataSources()) {
            assertNotNull(node.getNodeId());
            assertTrue(StringUtils.isNotEmpty(node.getLabel()));
            assertFalse(node.isSelected());
        }
        assertEquals(NUM_DATA_SOURCES / 2, tree.getExpressionDataSources().size());
        for (TreeNode node : tree.getExpressionDataSources()) {
            assertNotNull(node.getNodeId());
            assertTrue(StringUtils.isNotEmpty(node.getLabel()));
            assertFalse(node.isSelected());
        }
        assertEquals(NUM_ANNOTATION_GROUPS, tree.getAnnotationGroups().size());
        for (TreeNode node : tree.getAnnotationGroups()) {
            assertNull(node.getNodeId());
            assertTrue(StringUtils.isNotEmpty(node.getLabel()));
            assertFalse(node.getChildren().isEmpty());
            for (TreeNode childNode : node.getChildren()) {
                assertNotNull(childNode.getNodeId());
                assertTrue(StringUtils.isNotEmpty(childNode.getLabel()));
                assertFalse(childNode.isSelected());
            }
        }
        assertEquals(NUM_ANNOTATION_DESCRIPTORS, tree.getFieldDescriptors().size());
        for (TreeNode node : tree.getFieldDescriptors()) {
            assertNotNull(node.getNodeId());
            assertTrue(StringUtils.isNotEmpty(node.getLabel()));
            assertFalse(node.getChildren().isEmpty());
            for (TreeNode childNode : node.getChildren()) {
                assertNull(childNode.getNodeId());
                assertTrue(StringUtils.isNotEmpty(childNode.getLabel()));
                assertFalse(childNode.isSelected());
            }
        }
    }

    /**
     * Tests selection of already existing nodes in the tree.
     */
    @Test
    public void selection() {
        AuthorizedStudyElementsGroup authGroup = new AuthorizedStudyElementsGroup();
        for (GenomicDataSourceConfiguration dataSource : studyConfig.getGenomicDataSources()) {
            AuthorizedGenomicDataSourceConfiguration config = new AuthorizedGenomicDataSourceConfiguration();
            config.setAuthorizedStudyElementsGroup(authGroup);
            config.setGenomicDataSourceConfiguration(dataSource);
            authGroup.getAuthorizedGenomicDataSourceConfigurations().add(config);
        }

        for (AnnotationGroup group : studyConfig.getStudy().getSortedAnnotationGroups()) {
            for (AnnotationFieldDescriptor descriptor : group.getAnnotationFieldDescriptors()) {
                AuthorizedAnnotationFieldDescriptor authDesc = new AuthorizedAnnotationFieldDescriptor();
                authDesc.setAnnotationFieldDescriptor(descriptor);
                authDesc.setAuthorizedStudyElementsGroup(authGroup);
                authGroup.getAuthorizedAnnotationFieldDescriptors().add(authDesc);
            }
        }

        for (AnnotationGroup group : studyConfig.getStudy().getSortedAnnotationGroups()) {
            for (AnnotationFieldDescriptor descriptor : group.getAnnotationFieldDescriptors()) {
                Query query = new Query();
                for (String value : availableValues) {
                    StringComparisonCriterion criterion = new StringComparisonCriterion();
                    criterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
                    criterion.setStringValue(value);
                    criterion.setAnnotationFieldDescriptor(descriptor);
                    query.getCompoundCriterion().getCriterionCollection().add(criterion);
                }
                AuthorizedQuery authQuery = new AuthorizedQuery();
                authQuery.setAuthorizedStudyElementsGroup(authGroup);
                authQuery.setQuery(query);
                authGroup.getAuthorizedQuerys().add(authQuery);
            }
        }

        AuthorizationTrees tree = new AuthorizationTrees(studyConfig, authGroup);
        for (TreeNode node : tree.getCopyNumberDataSources()) {
            assertTrue(node.isSelected());
        }
        for (TreeNode node : tree.getExpressionDataSources()) {
            assertTrue(node.isSelected());
        }
        for (TreeNode node : tree.getAnnotationGroups()) {
            assertFalse(node.isSelected());
            for (TreeNode childNode : node.getChildren()) {
                assertTrue(childNode.isSelected());
            }
        }
        for (TreeNode node : tree.getFieldDescriptors()) {
            assertFalse(node.isSelected());
            for (TreeNode childNode : node.getChildren()) {
                assertTrue(childNode.isSelected());
            }
        }
    }

    /**
     * Tests the creation of a tree when the validation for field descriptors fail.
     */
    @Test
    public void constructionValidationException() throws ValidationException {
        when(studyManagementService.getAvailableValuesForFieldDescriptor(any(AnnotationFieldDescriptor.class))).thenThrow(new ValidationException("Error"));
        AuthorizationTrees tree = new AuthorizationTrees(studyConfig, new AuthorizedStudyElementsGroup());
        assertNotNull(tree);
    }
}
