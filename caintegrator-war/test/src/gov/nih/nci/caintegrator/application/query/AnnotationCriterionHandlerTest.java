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
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Tests for the annotation criterion handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class AnnotationCriterionHandlerTest extends AbstractMockitoTest {
    private Query query;
    private AnnotationFieldDescriptor annotationFieldDescriptor;
    private AnnotationDefinition annotationDefinition;
    private AnnotationGroup group;
    private CaIntegrator2DaoStub dao;

    /**
     * Unit test setup.
     */
    @Before
    public void setUp() {
        dao = new CaIntegrator2DaoStub();
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);

        group = new AnnotationGroup();
        group.setName("Group");
        study.getAnnotationGroups().add(group);

        annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");

        annotationFieldDescriptor = new AnnotationFieldDescriptor();
        annotationFieldDescriptor.setDefinition(annotationDefinition);
        annotationFieldDescriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        group.getAnnotationFieldDescriptors().add(annotationFieldDescriptor);

        query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
    }

    /**
     * Ensures that a validation error is thrown when a attempting to search by images series on a study with
     * no image data.
     *
     * @throws InvalidCriterionException on the expected invalid criterion error
     */
    @Test(expected = InvalidCriterionException.class)
    public void invalidImageSeriesCriterion() throws InvalidCriterionException {
        StringComparisonCriterion annotationCriterion = new StringComparisonCriterion();
        annotationCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        annotationCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);

        AnnotationCriterionHandler handler = new AnnotationCriterionHandler(annotationCriterion);
        handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
    }

    /**
     * Tests retrieval of rows by image series.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void imageSeriesCriterion() throws InvalidCriterionException {
        StringComparisonCriterion annotationCriterion = new StringComparisonCriterion();
        annotationCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        annotationCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);

        AnnotationFieldDescriptor otherFieldDescriptor = new AnnotationFieldDescriptor();
        otherFieldDescriptor.setDefinition(annotationDefinition);
        otherFieldDescriptor.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        group.getAnnotationFieldDescriptors().add(otherFieldDescriptor);
        annotationCriterion.setAnnotationFieldDescriptor(otherFieldDescriptor);

        AnnotationCriterionHandler handler = new AnnotationCriterionHandler(annotationCriterion);

        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(dao.findMatchingImageSeriesCalled);
        assertEquals(1, results.size());
    }

    /**
     * Tests retrieval of rows by sample.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void sampleCriterion() throws InvalidCriterionException {
        StringComparisonCriterion annotationCriterion = new StringComparisonCriterion();
        annotationCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        annotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);

        AnnotationCriterionHandler handler = new AnnotationCriterionHandler(annotationCriterion);
        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(dao.findMatchingSamplesCalled);
        assertEquals(1, results.size());
    }

    /**
     * Tests retrieval of rows by subject.
     *
     * @throws InvalidCriterionException on an unexpected invalid criterion exception
     */
    @Test
    public void subjectCriterion() throws InvalidCriterionException {
        StringComparisonCriterion annotationCriterion = new StringComparisonCriterion();
        annotationCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        annotationCriterion.setEntityType(EntityTypeEnum.SUBJECT);

        AnnotationCriterionHandler handler = new AnnotationCriterionHandler(annotationCriterion);
        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(dao.findMatchingSubjectsCalled);
        assertEquals(1, results.size());
    }

    /**
     * Tests that the handler doesn't attempt to deal with data is should not.
     */
    @Test
    public void negativeCriterion() {
        StringComparisonCriterion annotationCriterion = new StringComparisonCriterion();
        annotationCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        annotationCriterion.setEntityType(EntityTypeEnum.SUBJECT);

        AnnotationCriterionHandler handler = new AnnotationCriterionHandler(annotationCriterion);

        assertFalse(handler.isReporterMatchHandler());
        assertFalse(handler.hasReporterCriterion());
        assertFalse(handler.hasSegmentDataCriterion());
        assertFalse(handler.hasCriterionSpecifiedSegmentValues());
        assertFalse(handler.hasCriterionSpecifiedSegmentCallsValues());
        assertFalse(handler.hasCriterionSpecifiedReporterValues());
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(1f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentCallsValueMatchCriterionType(1));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH,
                handler.getGenomicValueMatchCriterionType(Sets.<Gene>newHashSet(), 1f));
    }
}
