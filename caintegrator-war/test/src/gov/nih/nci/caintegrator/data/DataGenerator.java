/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.data;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.Collection;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is a utility for generating data to our database.
 */
@Transactional
@Component
public class DataGenerator {
    @Autowired
    private CaIntegrator2Dao dao;
    private Study study;
    private StudySubscription studySubscription;
    private AnnotationDefinition ad;

    @SuppressWarnings("resource")   // incorrect interpretation of Spring context as closeable resource
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("integration-test-config.xml", DataGenerator.class);
        DataGenerator generator = (DataGenerator) context.getBean("dataGenerator");
        generator.generate();
    }

    /**
     * This generates data for the different tables and saves it.
     */
    public void generate(){
        addStudy();
        addStudySubscription();
        addUserWorkspace();
        addAnnotationDefinition();
        addAnnotationFieldDescriptor();
        addNumericAnnotationValue();
        addGeneExpressionReporter();
    }

    private void addStudy() {
        StudyConfiguration configuration = new StudyConfiguration();
        Study study = configuration.getStudy();
        study.setLongTitleText("Sample study description");
        study.setShortTitleText("Sample Study");
        dao.save(configuration);
    }
    private void addStudySubscription() {
        studySubscription = new StudySubscription();
        studySubscription.setStudy(study);
        dao.save(studySubscription);

    }

    private void addUserWorkspace() {
        UserWorkspace workspace = new UserWorkspace();
        workspace.setDefaultSubscription(studySubscription);
        Collection <StudySubscription> studies = new TreeSet<StudySubscription>();
        studies.add(studySubscription);
        dao.save(workspace);

    }

    private void addAnnotationDefinition() {
        ad = new AnnotationDefinition();
        ad.getCommonDataElement().setLongName("Congestive Heart Failure");
        ad.getCommonDataElement().setDefinition("Congestive heart failure (CHF), congestive cardiac failure (CCF) or just heart failure, "
                + "is a condition that can result from any structural or functional cardiac disorder that impairs the ability "
                + "of the heart to fill with blood or pump a sufficient amount of blood through the body.");
        ad.setDataType(AnnotationTypeEnum.STRING);
        dao.save(ad);
    }

    private void addAnnotationFieldDescriptor() {
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setDefinition(ad);
        ad.setKeywords("congestive heart failure");
        afd.setName("Congestive Heart Failure");
        afd.setType(AnnotationFieldType.ANNOTATION);
        dao.save(afd);
    }

    private void addNumericAnnotationValue() {
        NumericAnnotationValue nav = new NumericAnnotationValue();
        nav.setAnnotationDefinition(ad);
        nav.setNumericValue(Double.valueOf("1231423"));
        dao.save(nav);
    }

    private void addGeneExpressionReporter() {
        GeneExpressionReporter ger = new GeneExpressionReporter();
        ger.setName("GENE1");
        dao.save(ger);
    }
}
