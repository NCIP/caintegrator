/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.common;

import gov.nih.nci.caintegrator.application.study.AnnotationFile;
import gov.nih.nci.caintegrator.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.common.HibernateUtil;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;

import org.junit.Test;

/**
 * 
 */
public class HibernateUtilTest {
    
    @Test
    public void testLoadCollection() {
        Query query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        query.setSubscription(subscription);
        HibernateUtil.loadCollection(query);
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        query.setCompoundCriterion(compoundCriterion);
        HibernateUtil.loadCollection(query);

        compoundCriterion.getCriterionCollection().add(new SelectedValueCriterion());
        compoundCriterion.getCriterionCollection().add(new FoldChangeCriterion());
        compoundCriterion.getCriterionCollection().add(new GeneNameCriterion());
        HibernateUtil.loadCollection(query);
        
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testLoadCollectionStudyConfiguration() {
        StudyHelper studyHelper = new StudyHelper();
        StudyConfiguration studyConfiguration = studyHelper.
                    populateAndRetrieveStudyWithSourceConfigurations().getStudyConfiguration();
        ImageAnnotationConfiguration imageConf = new ImageAnnotationConfiguration();
        studyConfiguration.getImageDataSources().get(0).setImageAnnotationConfiguration(imageConf);
        AnnotationFile imageAnnotationFile = new AnnotationFile();
        imageConf.setAnnotationFile(imageAnnotationFile);

        HibernateUtil.loadCollection(studyConfiguration);
    }

}
