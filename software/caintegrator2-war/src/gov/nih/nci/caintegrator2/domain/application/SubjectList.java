/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.translational.Subject;

import java.util.Collection;

/**
 * 
 */
public class SubjectList extends AbstractList {

    private static final long serialVersionUID = 1L;
    
    private Collection<Subject> subjectCollection;

    /**
     * @return the subjectCollection
     */
    public Collection<Subject> getSubjectCollection() {
        return subjectCollection;
    }

    /**
     * @param subjectCollection the subjectCollection to set
     */
    public void setSubjectCollection(Collection<Subject> subjectCollection) {
        this.subjectCollection = subjectCollection;
    }

}
