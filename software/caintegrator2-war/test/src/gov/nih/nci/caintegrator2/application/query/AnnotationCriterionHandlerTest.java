/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AnnotationCriterionHandlerTest {

    
    @SuppressWarnings("deprecation")
    @Test
    public void testGetMatches() throws InvalidCriterionException {
        ApplicationContext context = new ClassPathXmlApplicationContext("query-test-config.xml", AnnotationCriterionHandlerTest.class); 
        CaIntegrator2DaoStub daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        ArrayDataServiceStub arrayDataServiceStub = (ArrayDataServiceStub) context.getBean("arrayDataServiceStub");
        daoStub.clear();       
        
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        
        
        DelimitedTextClinicalSourceConfiguration clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        studyConfiguration.getClinicalConfigurationCollection().add(clinicalConf);
        AnnotationFile annotationFile = new AnnotationFile();
        clinicalConf.setAnnotationFile(annotationFile);
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setId(1L);
        annotationDefinition.setDisplayName("Testing");
        addColumn(annotationFile, annotationDefinition);


        ImageDataSourceConfiguration imagingSourceConf = new ImageDataSourceConfiguration();
        studyConfiguration.getImageDataSources().add(imagingSourceConf);
        ImageAnnotationConfiguration imageConf = new ImageAnnotationConfiguration();
        imagingSourceConf.setImageAnnotationConfiguration(imageConf);
        AnnotationFile imageAnnotationFile = new AnnotationFile();
        imageConf.setAnnotationFile(imageAnnotationFile);

        addColumn(imageAnnotationFile, annotationDefinition);
        
        Query query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
        
        AbstractAnnotationCriterion abstractAnnotationCriterion = new AbstractAnnotationCriterion();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        AnnotationCriterionHandler annotationCriterionHandler = new AnnotationCriterionHandler(abstractAnnotationCriterion);
        annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingSamplesCalled);
        
        daoStub.clear();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        abstractAnnotationCriterion.setAnnotationDefinition(annotationDefinition);
        try {
            annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
            fail("Expecting invalid criterion becuase the study has no imageSeries data.");
        } catch (InvalidCriterionException e) { }
        study.getImageSeriesAnnotationCollection().add(new AnnotationDefinition());
        
        annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingImageSeriesCalled);
        
        daoStub.clear();
        abstractAnnotationCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        annotationCriterionHandler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertTrue(daoStub.findMatchingSubjectsCalled);
    }
    
    private void addColumn(AnnotationFile annotationFile, AnnotationDefinition subjectDef) {
        FileColumn column = new FileColumn();
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setShownInBrowse(true);
        fieldDescriptor.setDefinition(subjectDef);
        column.setFieldDescriptor(fieldDescriptor);
        annotationFile.getColumns().add(column);
    }


}
