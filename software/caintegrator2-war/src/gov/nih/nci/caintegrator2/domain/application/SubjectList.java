package gov.nih.nci.caintegrator2.domain.application;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class SubjectList extends AbstractList {

    private static final long serialVersionUID = 1L;
    
    private Set<SubjectIdentifier> subjectIdentifiers = new HashSet<SubjectIdentifier>();

    /**
     * @return the subjectIdentifiers
     */
    public Set<SubjectIdentifier> getSubjectIdentifiers() {
        return subjectIdentifiers;
    }

    /**
     * @param subjectIdentifiers the subjectIdentifiers to set
     */
    @SuppressWarnings("unused") // Used only for hibernate.
    private void setSubjectIdentifiers(Set<SubjectIdentifier> subjectIdentifiers) {
        this.subjectIdentifiers = subjectIdentifiers;
    }

    
}