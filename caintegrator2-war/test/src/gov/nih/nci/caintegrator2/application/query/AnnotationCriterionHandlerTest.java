/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;


public class AnnotationCriterionHandlerTest extends AbstractMockitoTest {
    private Query query;
    private AnnotationFieldDescriptor afd1;
    private AnnotationDefinition annotationDefinition;
    private AnnotationGroup group;
    private CaIntegrator2DaoStub dao;

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

        afd1 = new AnnotationFieldDescriptor();
        afd1.setDefinition(annotationDefinition);
        afd1.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        group.getAnnotationFieldDescriptors().add(afd1);

        query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        AbstractAnnotationCriterion abstractAnnotationCriterion = new StringComparisonCriterion();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        AnnotationCriterionHandler annotationCriterionHandler = new AnnotationCriterionHandler(abstractAnnotationCriterion);
        annotationCriterionHandler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(dao.findMatchingSamplesCalled);

        dao.clear();
        abstractAnnotationCriterion.setAnnotationFieldDescriptor(afd1);
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        try {
            annotationCriterionHandler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
            fail("Expecting invalid criterion because the study has no imageSeries data.");
        } catch (InvalidCriterionException e) {

        }
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setDefinition(annotationDefinition);
        afd2.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        group.getAnnotationFieldDescriptors().add(afd2);
        abstractAnnotationCriterion.setAnnotationFieldDescriptor(afd2);
        annotationCriterionHandler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(dao.findMatchingImageSeriesCalled);

        dao.clear();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        annotationCriterionHandler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(dao.findMatchingSubjectsCalled);

        // negative testing of copy number criterion
        assertFalse(annotationCriterionHandler.hasCriterionSpecifiedSegmentValues());
        assertFalse(annotationCriterionHandler.hasCriterionSpecifiedSegmentCallsValues());
        assertTrue(GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(annotationCriterionHandler.getSegmentValueMatchCriterionType(1F)));
        assertTrue(GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(annotationCriterionHandler.getSegmentCallsValueMatchCriterionType(1)));
    }
}
