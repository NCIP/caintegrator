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
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Tests that the CompoundCriterionHandler object can get the matches for various CompoundCriterion
 */
public class CompoundCriterionHandlerTestIntegration extends AbstractTransactionalSpringContextTests {

    private CaIntegrator2Dao dao;
    private ArrayDataService arrayDataService;

    @Override
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/dao-test-config.xml"};
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        StudyHelper studyHelper = new StudyHelper();
        dao.save(studyHelper.getPlatform());
        StudySubscription subscription = studyHelper.populateAndRetrieveStudy();
        Study study = subscription.getStudy();
        Set<EntityTypeEnum> entityTypesInQuery = new HashSet<EntityTypeEnum>();
        dao.save(study);

        Query query = new Query();
        query.setSubscription(subscription);

        // Try compoundCriterion1
        CompoundCriterion compoundCriterion1 = studyHelper.createCompoundCriterion1();
        entityTypesInQuery.add(EntityTypeEnum.IMAGESERIES);
        entityTypesInQuery.add(EntityTypeEnum.SAMPLE);
        entityTypesInQuery.add(EntityTypeEnum.SUBJECT);
        compoundCriterion1.setBooleanOperator(BooleanOperatorEnum.AND);
        CompoundCriterionHandler compoundCriterionHandler1 = CompoundCriterionHandler.create(compoundCriterion1,
                ResultTypeEnum.GENE_EXPRESSION);

        assertEquals(1, compoundCriterionHandler1.getMatches(dao, arrayDataService, query, entityTypesInQuery).size());

        compoundCriterion1.setBooleanOperator(BooleanOperatorEnum.OR);
        assertEquals(10, compoundCriterionHandler1.getMatches(dao, arrayDataService, query, entityTypesInQuery).size());

        // Try compoundCriterion2.
        CompoundCriterion compoundCriterion2 = studyHelper.createCompoundCriterion2();
        compoundCriterion2.setBooleanOperator(BooleanOperatorEnum.AND);
        CompoundCriterionHandler compoundCriterionHandler2 = CompoundCriterionHandler.create(compoundCriterion2,
                ResultTypeEnum.GENE_EXPRESSION);
        assertEquals(0, compoundCriterionHandler2.getMatches(dao, arrayDataService, query, entityTypesInQuery).size());

        compoundCriterion2.setBooleanOperator(BooleanOperatorEnum.OR);
        assertEquals(11, compoundCriterionHandler2.getMatches(dao, arrayDataService, query, entityTypesInQuery).size());

        // Try to combine criterion 1 and criterion 2 into a new compoundCriterion.
        CompoundCriterion compoundCriterion3 = new CompoundCriterion();
        compoundCriterion3.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion3.getCriterionCollection().add(compoundCriterion1);
        compoundCriterion3.getCriterionCollection().add(compoundCriterion2);
        // 6 results for 1
        compoundCriterion1.setBooleanOperator(BooleanOperatorEnum.OR);
        // 4 results for 2
        compoundCriterion2.setBooleanOperator(BooleanOperatorEnum.OR);

        // If we AND them together we should get 7 results
        compoundCriterion3.setBooleanOperator(BooleanOperatorEnum.AND);
        CompoundCriterionHandler compoundCriterionHandler3 = CompoundCriterionHandler.create(compoundCriterion3,
                ResultTypeEnum.GENE_EXPRESSION);
        assertEquals(7, compoundCriterionHandler3.getMatches(dao, arrayDataService, query, entityTypesInQuery).size());

        // If we OR them together we should get 7 results, all 5 subjects, plus 2 more samples for subject1.
        compoundCriterion3.setBooleanOperator(BooleanOperatorEnum.OR);
        Set<ResultRow> mostComplexRows = compoundCriterionHandler3.getMatches(dao, arrayDataService, query, entityTypesInQuery);
        assertEquals(14, mostComplexRows.size());

        // Test out the selected value criterion on the permissible value's for Samples.
        entityTypesInQuery = new HashSet<EntityTypeEnum>();
        entityTypesInQuery.add(EntityTypeEnum.SAMPLE);
        CompoundCriterion compoundCriterion4 = studyHelper.createCompoundCriterion3();
        CompoundCriterionHandler compoundCriterionHandler4 = CompoundCriterionHandler.create(compoundCriterion4,
                ResultTypeEnum.GENE_EXPRESSION);
        assertEquals(1, compoundCriterionHandler4.getMatches(dao, arrayDataService, query, entityTypesInQuery).size());
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
