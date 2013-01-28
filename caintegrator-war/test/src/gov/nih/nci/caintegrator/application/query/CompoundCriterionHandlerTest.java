/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.query.CompoundCriterionHandler;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.AbstractAnnotationCriterion;
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
