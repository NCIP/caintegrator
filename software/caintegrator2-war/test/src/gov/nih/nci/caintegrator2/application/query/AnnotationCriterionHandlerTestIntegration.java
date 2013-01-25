/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnnotationCriterionHandlerTestIntegration extends AbstractTransactionalSpringContextTests {

    private CaIntegrator2Dao dao;
    private ArrayDataService arrayDataService; 
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/dao-test-config.xml"};
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        StudyHelper studyHelper = new StudyHelper();
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
    
    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setDao(CaIntegrator2Dao caIntegrator2Dao) {
        this.dao = caIntegrator2Dao;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

}
