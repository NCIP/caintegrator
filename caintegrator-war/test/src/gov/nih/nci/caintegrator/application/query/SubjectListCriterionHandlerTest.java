/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Subject list criterion handler tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class SubjectListCriterionHandlerTest extends AbstractMockitoTest {
    private static final String SUBJECT_ID = "SubjectID";
    private static final Long ASSIGNMENT_ID = 1L;
    private Query query;
    private SubjectList subjectList;

    /**
     * Setup unit test.
     */
    @Before
    public void setUp() {
        subjectList = new SubjectList();
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier();
        subjectIdentifier.setIdentifier(SUBJECT_ID);
        subjectList.getSubjectIdentifiers().add(subjectIdentifier);

        Study study = new Study();

        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);

        query = new Query();
        query.setSubscription(subscription);

        StudySubjectAssignment assignment = new StudySubjectAssignment();
        assignment.setId(ASSIGNMENT_ID);
        assignment.setIdentifier(SUBJECT_ID);

        study.getAssignmentCollection().add(assignment);

        when(dao.findMatchingSubjects(any(SubjectListCriterion.class), any(Study.class)))
            .thenReturn(Lists.newArrayList(assignment));
    }

    /**
     * Tests retrieval of results by subject identifier.
     *
     * @throws InvalidCriterionException on unexpected invalid criterion error
     */
    @Test
    public void getMatches() throws InvalidCriterionException {
        SubjectListCriterion criterion = new SubjectListCriterion();
        criterion.getSubjectListCollection().add(subjectList);

        SubjectListCriterionHandler handler = SubjectListCriterionHandler.create(criterion);
        assertTrue(criterion.getSubjectIdentifiers().contains(SUBJECT_ID));

        Set<ResultRow> results = handler.getMatches(dao, null, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, results.size());

        ResultRow row = results.iterator().next();
        assertEquals(ASSIGNMENT_ID, row.getSubjectAssignment().getId());
        assertNull(row.getSampleAcquisition());
    }

    /**
     * Tests retrieval of results by subject identifier with an invalid criterion.
     *
     * @throws InvalidCriterionException on unexpected invalid criterion error
     */
    @Test(expected = InvalidCriterionException.class)
    public void getMatchesInvalidCriterion() throws InvalidCriterionException {
        SubjectListCriterion criterion = new SubjectListCriterion();
        SubjectListCriterionHandler handler = SubjectListCriterionHandler.create(criterion);

        handler.getMatches(dao, null, query, new HashSet<EntityTypeEnum>());
    }
}
