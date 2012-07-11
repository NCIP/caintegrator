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
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class CompoundCriterionHandlerTest extends AbstractMockitoTest {

    private CaIntegrator2DaoStub daoStub;
    private Query query;
    private StudySubscription subscription;

    @Before
    public void setUp() throws Exception {
        daoStub = new CaIntegrator2DaoStub();
        daoStub.clear();

        StudyHelper studyHelper = new StudyHelper();
        subscription = studyHelper.populateAndRetrieveStudy();
        query = new Query();
        query.setSubscription(subscription);
    }

    @Test
    public void testNullCompoundCriterionNoReturnEntityTypes() throws Exception {
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(null, ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(matches.isEmpty());
    }

    @Test
    public void testNullCriterionCollectionNoReturnEntityTypes() throws Exception {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(null);
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(matches.isEmpty());
    }

    @Test
    public void testEmptyCriterionCollectionNoReturnEntityTypes() throws Exception {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(matches.isEmpty());
    }

    @Test
    public void testEmptyCriterionCollectionReturnSubjects() throws Exception {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);

        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SUBJECT);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertEquals(6, matches.size());
    }

    @Test
    public void testEmptyCriterionCollectionReturnSamples() throws Exception {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SAMPLE);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertEquals(7, matches.size());
        List<String> validSampleNames = Arrays.asList("SAMPLE_1", "SAMPLE_12", "SAMPLE_13", "SAMPLE_2", "SAMPLE_3",
                                                      "SAMPLE_4", "SAMPLE_5");
        for (ResultRow resultRow : matches) {
            SampleAcquisition sampleAcquisition = resultRow.getSampleAcquisition();
            assertTrue(validSampleNames.contains(sampleAcquisition.getSample().getName()));
        }
    }

    @Test
    public void testEmptyCriterionCollectionReturnImages() throws Exception {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.IMAGESERIES);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertEquals(5, matches.size());
        for (ResultRow resultRow : matches) {
            ImageSeries imageSeries = resultRow.getImageSeries();
            StringAnnotationValue sav = (StringAnnotationValue) imageSeries.getAnnotationCollection().iterator().next();
            assertTrue(Pattern.matches("string[1-5]", sav.getStringValue()));
        }
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        AnnotationGroup group = subscription.getStudy().getAnnotationGroups().iterator().next();
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");

        AnnotationFieldDescriptor imageSeriesAFD = new AnnotationFieldDescriptor();
        imageSeriesAFD.setDefinition(annotationDefinition);
        imageSeriesAFD.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        group.getAnnotationFieldDescriptors().add(imageSeriesAFD);

        AnnotationFieldDescriptor subjectAFD = new AnnotationFieldDescriptor();
        subjectAFD.setDefinition(annotationDefinition);
        subjectAFD.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        group.getAnnotationFieldDescriptors().add(subjectAFD);

        CompoundCriterion sampleCompoundCriterion = new CompoundCriterion();
        sampleCompoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());

        AbstractAnnotationCriterion sampleStringCriterion = new StringComparisonCriterion();
        sampleStringCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        sampleCompoundCriterion.getCriterionCollection().add(sampleStringCriterion);

        AbstractAnnotationCriterion imageSeriesStringCriterion = new StringComparisonCriterion();
        imageSeriesStringCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        imageSeriesStringCriterion.setAnnotationFieldDescriptor(imageSeriesAFD);

        AbstractAnnotationCriterion subjectStringCriterion = new StringComparisonCriterion();
        subjectStringCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        subjectStringCriterion.setAnnotationFieldDescriptor(subjectAFD);

        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setGeneSymbol("EGFR");

        CompoundCriterion imageAndSubjectCriteria = new CompoundCriterion();
        imageAndSubjectCriteria.setCriterionCollection(new HashSet<AbstractCriterion>());
        imageAndSubjectCriteria.getCriterionCollection().add(imageSeriesStringCriterion);
        imageAndSubjectCriteria.getCriterionCollection().add(subjectStringCriterion);
        imageAndSubjectCriteria.setBooleanOperator(BooleanOperatorEnum.AND);

        CompoundCriterion sampleOrImageAndSubjectCriteria = new CompoundCriterion();
        sampleOrImageAndSubjectCriteria.setCriterionCollection(new HashSet<AbstractCriterion>());
        sampleOrImageAndSubjectCriteria.getCriterionCollection().add(sampleCompoundCriterion);
        sampleOrImageAndSubjectCriteria.getCriterionCollection().add(imageAndSubjectCriteria);
        sampleOrImageAndSubjectCriteria.setBooleanOperator(BooleanOperatorEnum.OR);

        CompoundCriterionHandler compoundCriterionHandler = CompoundCriterionHandler
            .create(sampleOrImageAndSubjectCriteria, ResultTypeEnum.GENE_EXPRESSION);

        // test creating handler with ExpressionLevelCriterion
        CompoundCriterion compoundCriterion5 = new CompoundCriterion();
        compoundCriterion5.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion5.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion5.setBooleanOperator(BooleanOperatorEnum.OR);
        CompoundCriterionHandler compoundCriterionHandler3=CompoundCriterionHandler.create(compoundCriterion5,
                ResultTypeEnum.GENE_EXPRESSION);
        compoundCriterionHandler3.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());

        compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);

        //test for specific entity type
        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SUBJECT);
        compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.IMAGESERIES);
        compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SAMPLE);
        compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);

        // compound criterion with multiple criteria
        CompoundCriterion compoundCriterion6 = new CompoundCriterion();
        compoundCriterion6.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion6.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion6.getCriterionCollection().add(imageSeriesStringCriterion);
        compoundCriterion6.getCriterionCollection().add(subjectStringCriterion);
        compoundCriterion6.setBooleanOperator(BooleanOperatorEnum.AND);
        CompoundCriterionHandler compoundCriterionHandler4=CompoundCriterionHandler.create(compoundCriterion6,
                ResultTypeEnum.CLINICAL);
        compoundCriterionHandler4.getMatches(daoStub, arrayDataService, query, entityTypeSet);

        // Check if criterion somehow ends up being empty.
        entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SUBJECT);
        CompoundCriterion compoundCriterion7 = new CompoundCriterion();
        compoundCriterion7.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler compoundCriterionHandler5=CompoundCriterionHandler.create(compoundCriterion7,
                ResultTypeEnum.CLINICAL);
        compoundCriterionHandler5.getMatches(daoStub, arrayDataService, query, entityTypeSet);
    }

    private void assertEmptyHandlerCriteria(CompoundCriterionHandler handler) {
        assertFalse(handler.hasCriterionSpecifiedReporterValues());
        assertFalse(handler.hasCriterionSpecifiedSegmentCallsValues());
        assertFalse(handler.hasCriterionSpecifiedSegmentValues());
        assertFalse(handler.hasReporterCriterion());
        assertFalse(handler.hasSegmentDataCriterion());
    }
}
