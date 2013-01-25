/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test case.
 */
public class QueryTranslatorTest {

    @Test
    public void testExecute() throws InvalidCriterionException {
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("query-test-config.xml", QueryTranslatorTest.class); 
        CaIntegrator2DaoStub daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        ArrayDataServiceStub arrayDataServiceStub = (ArrayDataServiceStub) context.getBean("arrayDataServiceStub");
        ResultHandlerStub resultHandlerStub = (ResultHandlerStub) context.getBean("resultHandlerStub");
        resultHandlerStub.clear();
        daoStub.clear();       
        
        Study study = new Study();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        Query query = new Query();
        query.setCompoundCriterion(compoundCriterion);
        query.setSubscription(subscription);
        query.setColumnCollection(new HashSet<ResultColumn>());

        QueryTranslator queryTranslator = new QueryTranslator(query, daoStub, arrayDataServiceStub, resultHandlerStub);
        queryTranslator.execute();
        assertTrue(resultHandlerStub.createResultsCalled);
    }


}
