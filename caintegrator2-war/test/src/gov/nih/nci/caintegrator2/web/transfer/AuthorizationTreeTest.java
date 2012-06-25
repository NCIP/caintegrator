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
import org.apache.commons.lang.StringUtils;
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
