/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
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

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AnnotationCriterionHandlerTest {

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        ApplicationContext context = new ClassPathXmlApplicationContext("query-test-config.xml", AnnotationCriterionHandlerTest.class);
        CaIntegrator2DaoStub daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        ArrayDataServiceStub arrayDataServiceStub = (ArrayDataServiceStub) context.getBean("arrayDataServiceStub");
        daoStub.clear();

        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);


        AnnotationGroup group = new AnnotationGroup();
        group.setName("Group");
        study.getAnnotationGroups().add(group);
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");

        AnnotationFieldDescriptor afd1 = new AnnotationFieldDescriptor();
        afd1.setDefinition(annotationDefinition);
        afd1.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        group.getAnnotationFieldDescriptors().add(afd1);

        Query query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);

        AbstractAnnotationCriterion abstractAnnotationCriterion = new StringComparisonCriterion();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        AnnotationCriterionHandler annotationCriterionHandler = new AnnotationCriterionHandler(abstractAnnotationCriterion);
        annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingSamplesCalled);

        daoStub.clear();
        abstractAnnotationCriterion.setAnnotationFieldDescriptor(afd1);
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        try {
            annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
            fail("Expecting invalid criterion becuase the study has no imageSeries data.");
        } catch (InvalidCriterionException e) { }
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setDefinition(annotationDefinition);
        afd2.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        group.getAnnotationFieldDescriptors().add(afd2);
        abstractAnnotationCriterion.setAnnotationFieldDescriptor(afd2);
        annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingImageSeriesCalled);

        daoStub.clear();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingSubjectsCalled);

        // negative testing of copy number criterion
        assertFalse(annotationCriterionHandler.hasCriterionSpecifiedSegmentValues());
        assertFalse(annotationCriterionHandler.hasCriterionSpecifiedSegmentCallsValues());
        assertTrue(GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(annotationCriterionHandler.getSegmentValueMatchCriterionType(1F)));
        assertTrue(GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(annotationCriterionHandler.getSegmentCallsValueMatchCriterionType(1)));
    }

}
