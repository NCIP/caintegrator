/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.QueryTranslator;
import gov.nih.nci.caintegrator.application.query.ResultHandler;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;

import org.junit.Test;

/**
 * Test case.
 */
public class QueryTranslatorTest extends AbstractMockitoTest {

    @Test
    public void testExecute() throws InvalidCriterionException {
        CaIntegrator2DaoStub daoStub = new CaIntegrator2DaoStub();
        ResultHandler resultHandler = mock(ResultHandler.class);
        when(resultHandler.createResults(any(Query.class), anySetOf(ResultRow.class), any(CaIntegrator2Dao.class))).thenReturn(new QueryResult());
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

        QueryTranslator queryTranslator = new QueryTranslator(query, daoStub, arrayDataService, resultHandler);
        queryTranslator.execute();
        verify(resultHandler, atLeastOnce()).createResults(any(Query.class), anySetOf(ResultRow.class), any(CaIntegrator2Dao.class));
    }
}
