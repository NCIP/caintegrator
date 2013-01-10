/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/**/dao-test-config.xml"})
@Transactional
public class AnnotationCriterionHandlerTestIntegration extends AbstractMockitoTest {

    @Autowired
    private CaIntegrator2Dao dao;

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        StudyHelper studyHelper = new StudyHelper();
        dao.save(studyHelper.getPlatform());
        Study study = studyHelper.populateAndRetrieveStudy().getStudy();
        dao.save(study);

        Query query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);

        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(12.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        criterion.setAnnotationFieldDescriptor(studyHelper.getSampleAnnotationFieldDescriptor());
        criterion.setEntityType(EntityTypeEnum.SAMPLE);

        AnnotationCriterionHandler annotationCriterionHandler = new AnnotationCriterionHandler(criterion);
        assertEquals(4, annotationCriterionHandler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>()).size());
    }
}
