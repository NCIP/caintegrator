/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;

import org.junit.Test;

/**
 * 
 */
public class HibernateUtilTest {
    
    @Test
    public void testLoadCollection() {
        Query query = new Query();
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
