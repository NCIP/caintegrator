/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.DateComparisonOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.action.query.form.AnnotationCriterionRow;
import gov.nih.nci.caintegrator.web.action.query.form.CriteriaGroup;
import gov.nih.nci.caintegrator.web.action.query.form.CriterionOperatorEnum;
import gov.nih.nci.caintegrator.web.action.query.form.CriterionTypeEnum;
import gov.nih.nci.caintegrator.web.action.query.form.DateComparisonCriterionWrapper;
import gov.nih.nci.caintegrator.web.action.query.form.QueryForm;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class DateComparisonCriterionWrapperTest {

    DateComparisonCriterionWrapper wrapper;

    @Before
    public void setUp() {
        DateComparisonCriterion criterion = new DateComparisonCriterion();
        QueryForm queryForm = new QueryForm();
        Query query = new Query();
        query.setSubscription(new StudySubscription());
        query.getSubscription().setStudy(new Study());
        query.getSubscription().getStudy().setStudyConfiguration(new StudyConfiguration());
        queryForm.setQuery(query, null, null, null);
        queryForm.getQuery().setCompoundCriterion(new CompoundCriterion());
        AnnotationCriterionRow row = new AnnotationCriterionRow(new CriteriaGroup(queryForm), "testGroup");
        wrapper = new DateComparisonCriterionWrapper(criterion, row);
    }
    
    /**
     * Test method for {@link gov.nih.nci.caintegrator.web.action.query.form.DateComparisonCriterionWrapper#getCriterionType()}.
     */
    @Test
    public void testGetCriterionType() {
        assertEquals(CriterionTypeEnum.DATE_COMPARISON, wrapper.getCriterionType());
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.web.action.query.form.DateComparisonCriterionWrapper#getAbstractAnnotationCriterion()}.
     */
    @Test
    public void testGetAbstractAnnotationCriterion() {
        assertTrue(wrapper.getAbstractAnnotationCriterion() instanceof DateComparisonCriterion);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.web.action.query.form.DateComparisonCriterionWrapper#getAvailableOperators()}.
     */
    @Test
    public void testGetAvailableOperators() {
        assertEquals(5, wrapper.getAvailableOperators().length);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.web.action.query.form.DateComparisonCriterionWrapper#getOperator()}.
     */
    @Test
    public void testGetOperator() {
        assertNull(wrapper.getOperator());
        wrapper.operatorChanged(null, null);
        DateComparisonCriterion criterion = (DateComparisonCriterion) wrapper.getCriterion();
        assertNull(criterion.getDateComparisonOperator());
        criterion.setDateComparisonOperator(DateComparisonOperatorEnum.EQUAL);
        assertEquals(CriterionOperatorEnum.EQUALS, wrapper.getOperator());
        wrapper.operatorChanged(null, CriterionOperatorEnum.LESS_THAN);
        assertEquals(CriterionOperatorEnum.LESS_THAN, wrapper.getOperator());
    }

}
