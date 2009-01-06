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