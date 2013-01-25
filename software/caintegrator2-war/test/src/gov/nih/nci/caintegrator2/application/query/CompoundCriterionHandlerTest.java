/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("PMD")
public class CompoundCriterionHandlerTest {
    
    @Test
    public void testGetMatches() throws InvalidCriterionException {
        ApplicationContext context = new ClassPathXmlApplicationContext("query-test-config.xml", CompoundCriterionHandlerTest.class); 
        CaIntegrator2DaoStub daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        ArrayDataServiceStub arrayDataServiceStub = (ArrayDataServiceStub) context.getBean("arrayDataServiceStub");
        daoStub.clear();       
        
        Study study = new Study();
        AnnotationGroup group = new AnnotationGroup();
        group.setName("name");
        study.getAnnotationGroups().add(group);
        Query query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
        study.setDefaultTimepoint(new Timepoint());
        
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");
        AnnotationFieldDescriptor afd1 = new AnnotationFieldDescriptor();
        afd1.setDefinition(annotationDefinition);
        afd1.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setDefinition(annotationDefinition);
        afd2.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        group.getAnnotationFieldDescriptors().add(afd1);
        group.getAnnotationFieldDescriptors().add(afd2);
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        AbstractAnnotationCriterion abstractAnnotationCriterion = new AbstractAnnotationCriterion();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        AbstractAnnotationCriterion abstractAnnotationCriterion2 = new AbstractAnnotationCriterion();
        abstractAnnotationCriterion2.setEntityType(EntityTypeEnum.IMAGESERIES);
        abstractAnnotationCriterion2.setAnnotationFieldDescriptor(afd1);
        AbstractAnnotationCriterion abstractAnnotationCriterion3 = new AbstractAnnotationCriterion();
        abstractAnnotationCriterion3.setEntityType(EntityTypeEnum.SUBJECT);
        abstractAnnotationCriterion3.setAnnotationFieldDescriptor(afd2);
        compoundCriterion.getCriterionCollection().add(abstractAnnotationCriterion);
        
        CompoundCriterion compoundCriterion2 = new CompoundCriterion();
        compoundCriterion2.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion2.getCriterionCollection().add(abstractAnnotationCriterion2);
        compoundCriterion2.getCriterionCollection().add(abstractAnnotationCriterion3);
        compoundCriterion2.setBooleanOperator(BooleanOperatorEnum.AND);
        
        CompoundCriterion compoundCriterion3 = new CompoundCriterion();
        compoundCriterion3.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion3.getCriterionCollection().add(compoundCriterion);
        compoundCriterion3.getCriterionCollection().add(compoundCriterion2);
        CompoundCriterionHandler compoundCriterionHandler=CompoundCriterionHandler.create(compoundCriterion3, 
                ResultTypeEnum.GENE_EXPRESSION);
        compoundCriterion3.setBooleanOperator(BooleanOperatorEnum.OR);
        
        compoundCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingSamplesCalled);
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }


}
