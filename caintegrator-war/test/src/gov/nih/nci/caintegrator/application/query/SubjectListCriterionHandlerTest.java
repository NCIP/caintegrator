/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.SubjectListCriterionHandler;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SubjectListCriterionHandlerTest {
    
    private static final String SUBJECT_ID = "SubjectID";
    private static final Long ASSIGNMENT_ID = Long.valueOf(1);
    private CaIntegrator2DaoStub daoStub = new DaoStub();
    private Query query;
    private Study study;
    private SubjectList subjectList;
    private StudySubjectAssignment assignment;
    
    @Before
    public void setUp() {
        daoStub.clear();
        subjectList = new SubjectList();
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier();
        subjectIdentifier.setIdentifier(SUBJECT_ID);
        subjectList.getSubjectIdentifiers().add(subjectIdentifier);
        study = new Study();
        query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
        assignment = new StudySubjectAssignment();
        study.getAssignmentCollection().add(assignment);
        assignment.setId(ASSIGNMENT_ID);
        assignment.setIdentifier(SUBJECT_ID);
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        SubjectListCriterion criterion = new SubjectListCriterion();
        criterion.getSubjectListCollection().add(subjectList);
        SubjectListCriterionHandler handler = SubjectListCriterionHandler.create(criterion);
        assertTrue(criterion.getSubjectIdentifiers().contains(SUBJECT_ID));
        
        Set<ResultRow> rows = handler.getMatches(daoStub, null, query, new HashSet<EntityTypeEnum>());
        ResultRow row = rows.iterator().next();
        assertEquals(ASSIGNMENT_ID, row.getSubjectAssignment().getId());
        assertNull(row.getSampleAcquisition());
    }
    
    @Test(expected = InvalidCriterionException.class)
    public void testGetMatchesInvalidCriterion() throws InvalidCriterionException {
        SubjectListCriterion criterion = new SubjectListCriterion();
        SubjectListCriterionHandler handler = SubjectListCriterionHandler.create(criterion);
        
        handler.getMatches(daoStub, null, query, new HashSet<EntityTypeEnum>());
    }
    
    @Test
    public void testIsReporterMatchHandler() {
        assertFalse(SubjectListCriterionHandler.create(null).isReporterMatchHandler());
    }
    
    private class DaoStub extends CaIntegrator2DaoStub {

        @Override
        public java.util.List<StudySubjectAssignment> findMatchingSubjects(SubjectListCriterion subjectListCriterion, Study study) {
            List<StudySubjectAssignment> studySubjectAssignments = new ArrayList<StudySubjectAssignment>();
            studySubjectAssignments.add(assignment);
            findMatchingSubjectsCalled = true;
            return studySubjectAssignments;
        }
        
    }

}
