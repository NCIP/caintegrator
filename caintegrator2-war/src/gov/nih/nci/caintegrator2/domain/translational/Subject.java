/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class Subject extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Collection<StudySubjectAssignment> assignmentCollection;

    /**
     * @return the assignmentCollection
     */
    public Collection<StudySubjectAssignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    /**
     * @param assignmentCollection the assignmentCollection to set
     */
    public void setAssignmentCollection(Collection<StudySubjectAssignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
    }

}
