package gov.nih.nci.caintegrator2.domain.application;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class SubjectList extends AbstractList {

    private static final long serialVersionUID = 1L;
    
    private List<SubjectIdentifier> subjectIdentifiers = new ArrayList<SubjectIdentifier>();

    /**
     * @return the subjectIdentifiers
     */
    public List<SubjectIdentifier> getSubjectIdentifiers() {
        return subjectIdentifiers;
    }

    /**
     * @param subjectIdentifiers the subjectIdentifiers to set
     */
    @SuppressWarnings("unused") // Used only for hibernate.
    private void setSubjectIdentifiers(List<SubjectIdentifier> subjectIdentifiers) {
        this.subjectIdentifiers = subjectIdentifiers;
    }

    
}