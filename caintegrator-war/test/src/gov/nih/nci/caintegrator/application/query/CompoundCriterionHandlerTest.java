/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 *  Tests for the various functionality of the compound criterion handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class CompoundCriterionHandlerTest extends AbstractMockitoTest {
    private CaIntegrator2DaoStub daoStub;
    private Query query;
    private StudySubscription subscription;

    /**
     * Test setup.
     *
     * @throws Exception
     */
    @Before
    public void setUp() {
        daoStub = new CaIntegrator2DaoStub();

        StudyHelper studyHelper = new StudyHelper();
        subscription = studyHelper.populateAndRetrieveStudy();
        query = new Query();
        query.setSubscription(subscription);
    }

    /**
     * Tests that a null compound criterion returns no matches.
     *
     * @throws InvalidCriterionException on unexpected validation error
     */
    @Test
    public void testNullCompoundCriterionNoReturnEntityTypes() throws InvalidCriterionException {
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(null, ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(matches.isEmpty());
    }

    /**
     * Tests that a null compound criterion collection results in no matches.
     *
     * @throws InvalidCriterionException on unexpected validation error
     */
    @Test
    public void testNullCriterionCollectionNoReturnEntityTypes() throws InvalidCriterionException {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(null);
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(matches.isEmpty());
    }

    /**
     * Tests that an empty compound criteria collection results in no matches.
     *
     * @throws InvalidCriterionException on unexpected validation error
     */
    @Test
    public void testEmptyCriterionCollectionNoReturnEntityTypes() throws InvalidCriterionException {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler handler = CompoundCriterionHandler.create(compoundCriterion,
                                                                           ResultTypeEnum.GENE_EXPRESSION);
        assertEmptyHandlerCriteria(handler);
        Set<ResultRow> matches = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(matches.isEmpty());
    }

    /**
     * Tests that an empty compound criterion with subjects return matches.
     *
     * @throws InvalidCriterionException on unexpected validation error
     */
    @Test
    public void testEmptyCriterionCollectionReturnSubjects() throws InvalidCriterionException {
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

    /**
     * Tests that an empty compound criterion with samples return matches.
     *
     * @throws InvalidCriterionException on unexpected validation error
     */
    @Test
    public void testEmptyCriterionCollectionReturnSamples() throws InvalidCriterionException {
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

    /**
     * Tests that an empty compound criterion with image series return matches.
     *
     * @throws InvalidCriterionException on unexpected validation error
     */
    @Test
    public void testEmptyCriterionCollectionReturnImages() throws InvalidCriterionException {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterionHandler handler =
                CompoundCriterionHandler.create(compoundCriterion, ResultTypeEnum.GENE_EXPRESSION);
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

    /**
     * Tests handler creation and match searching for empty criteria with clincal results.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void emptyCriterionClinicalData() throws InvalidCriterionException {
        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SUBJECT);

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        CompoundCriterionHandler compoundCriterionHandler =
                CompoundCriterionHandler.create(compoundCriterion, ResultTypeEnum.CLINICAL);
        Set<ResultRow> matches = compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);

        assertFalse(matches.isEmpty());
        assertEquals(6, matches.size());
    }

    /**
     * Tests handler creation with expression level criterion.
     *
     * @throws InvalidCriterionException on unexpected invalid criterion exception
     */
    @Test
    public void expressionLevelSearch() throws InvalidCriterionException {
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setGeneSymbol("EGFR");

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        CompoundCriterionHandler compoundCriterionHandler =
                CompoundCriterionHandler.create(compoundCriterion, ResultTypeEnum.GENE_EXPRESSION);

        Set<ResultRow> matches = compoundCriterionHandler.getMatches(daoStub, arrayDataService, query,
                        Sets.<EntityTypeEnum>newHashSet());
        assertTrue(matches.isEmpty());
    }

    /**
     * Tests handler creation and match searching with sample or (subject and image series) criterion.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void sampleOrImageAndSubjectHandler() throws InvalidCriterionException {
        CompoundCriterionHandler compoundCriterionHandler =
                CompoundCriterionHandler.create(getSampleOrImageAndSubjectCriteria(), ResultTypeEnum.GENE_EXPRESSION);

        Set<ResultRow> matches = compoundCriterionHandler.getMatches(daoStub, arrayDataService, query,
                Sets.<EntityTypeEnum>newHashSet());
        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }

    /**
     * Tests handler creation and match searching with sample or (subject and image series) and expression level
     * with clinical results.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void sampleOrImageAndSubjectHandlerClinicalData() throws InvalidCriterionException {
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setGeneSymbol("EGFR");

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion.getCriterionCollection().add(getImageSeriesCriterion());
        compoundCriterion.getCriterionCollection().add(getSubjectCriterion());
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);

        CompoundCriterionHandler compoundCriterionHandler =
                CompoundCriterionHandler.create(getSampleOrImageAndSubjectCriteria(), ResultTypeEnum.CLINICAL);

        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SAMPLE);

        Set<ResultRow> matches = compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }

    /**
     * Tests handler creation and match searching with sample or (subject and image series) criterion for Subjects.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void sampleOrImageAndSubjectHandlerForSubjects() throws InvalidCriterionException {
        CompoundCriterionHandler compoundCriterionHandler = CompoundCriterionHandler
                .create(getSampleOrImageAndSubjectCriteria(), ResultTypeEnum.GENE_EXPRESSION);

        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SUBJECT);
        Set<ResultRow> matches =
                compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }

    /**
     * Tests handler creation and match searching with sample or (subject and image series) criterion for Image Series.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void sampleOrImageAndSubjectHandlerForImageSeries() throws InvalidCriterionException {
        CompoundCriterionHandler compoundCriterionHandler = CompoundCriterionHandler
                .create(getSampleOrImageAndSubjectCriteria(), ResultTypeEnum.GENE_EXPRESSION);

        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.IMAGESERIES);
        Set<ResultRow> matches =
                compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }

    /**
     * Tests handler creation and match searching with sample or (subject and image series) criterion for Samples.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void sampleOrImageAndSubjectHandlerForSamples() throws InvalidCriterionException {
        CompoundCriterionHandler compoundCriterionHandler =
                CompoundCriterionHandler.create(getSampleOrImageAndSubjectCriteria(), ResultTypeEnum.GENE_EXPRESSION);

        Set<EntityTypeEnum> entityTypeSet = new HashSet<EntityTypeEnum>();
        entityTypeSet.add(EntityTypeEnum.SAMPLE);
        Set<ResultRow> matches =
                compoundCriterionHandler.getMatches(daoStub, arrayDataService, query, entityTypeSet);
        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }

    private void assertEmptyHandlerCriteria(CompoundCriterionHandler handler) {
        assertFalse(handler.hasCriterionSpecifiedReporterValues());
        assertFalse(handler.hasCriterionSpecifiedSegmentCallsValues());
        assertFalse(handler.hasCriterionSpecifiedSegmentValues());
        assertFalse(handler.hasReporterCriterion());
        assertFalse(handler.hasSegmentDataCriterion());
    }

    private AbstractCriterion getImageSeriesCriterion() {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");

        AnnotationFieldDescriptor imageSeriesAFD = new AnnotationFieldDescriptor();
        imageSeriesAFD.setDefinition(annotationDefinition);
        imageSeriesAFD.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);

        AnnotationGroup group = subscription.getStudy().getAnnotationGroups().iterator().next();
        group.getAnnotationFieldDescriptors().add(imageSeriesAFD);

        StringComparisonCriterion imageSeriesStringCriterion = new StringComparisonCriterion();
        imageSeriesStringCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        imageSeriesStringCriterion.setAnnotationFieldDescriptor(imageSeriesAFD);
        return imageSeriesStringCriterion;
    }

    private AbstractCriterion getSubjectCriterion() {
        AnnotationGroup group = subscription.getStudy().getAnnotationGroups().iterator().next();
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");

        AnnotationFieldDescriptor subjectAFD = new AnnotationFieldDescriptor();
        subjectAFD.setDefinition(annotationDefinition);
        subjectAFD.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        group.getAnnotationFieldDescriptors().add(subjectAFD);

        StringComparisonCriterion subjectStringCriterion = new StringComparisonCriterion();
        subjectStringCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        subjectStringCriterion.setAnnotationFieldDescriptor(subjectAFD);
        return subjectStringCriterion;

    }

    private CompoundCriterion getImageAndSubjectCriteria() {
        CompoundCriterion imageAndSubjectCriteria = new CompoundCriterion();
        imageAndSubjectCriteria.getCriterionCollection().add(getImageSeriesCriterion());
        imageAndSubjectCriteria.getCriterionCollection().add(getSubjectCriterion());
        imageAndSubjectCriteria.setBooleanOperator(BooleanOperatorEnum.AND);
        return imageAndSubjectCriteria;
    }

    private CompoundCriterion getSampleOrImageAndSubjectCriteria() {
        StringComparisonCriterion sampleStringCriterion = new StringComparisonCriterion();
        sampleStringCriterion.setEntityType(EntityTypeEnum.SAMPLE);

        CompoundCriterion sampleOrImageAndSubjectCriteria = new CompoundCriterion();
        sampleOrImageAndSubjectCriteria.getCriterionCollection().add(sampleStringCriterion);
        sampleOrImageAndSubjectCriteria.getCriterionCollection().add(getImageAndSubjectCriteria());
        sampleOrImageAndSubjectCriteria.setBooleanOperator(BooleanOperatorEnum.OR);
        return sampleOrImageAndSubjectCriteria;
    }

}
