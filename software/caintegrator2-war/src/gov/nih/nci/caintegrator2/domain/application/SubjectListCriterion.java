package gov.nih.nci.caintegrator2.domain.application;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class SubjectListCriterion extends AbstractCriterion implements Cloneable {
    
    private static final long serialVersionUID = 1L;
    
    private Set<SubjectList> subjectListCollection = new HashSet<SubjectList>();

    
    /**
     * @return the subjectListCollection
     */
    public Set<SubjectList> getSubjectListCollection() {
        return subjectListCollection;
    }

    /**
     * @param subjectListCollection the subjectListCollection to set
     */
    public void setSubjectListCollection(Set<SubjectList> subjectListCollection) {
        this.subjectListCollection = subjectListCollection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SubjectListCriterion clone() throws CloneNotSupportedException {
        SubjectListCriterion clone = (SubjectListCriterion) super.clone();
        clone.getSubjectListCollection().addAll(cloneValueCollection());
        return clone;
    }
    
    private Set<SubjectList> cloneValueCollection() {
        Set<SubjectList> clone = new HashSet<SubjectList>();
        for (SubjectList subjectList : subjectListCollection) {
            clone.add(subjectList);
        }
        return clone;
    }
    
    /**
     * Retrieves the list of subject identifiers for this criterion.
     * @return subject identifiers in this criterion.
     */
    public Set<String> getSubjectIdentifiers() {
        Set<String> identifiers = new HashSet<String>();
        for (SubjectList subjectList : getSubjectListCollection()) {
            for (SubjectIdentifier subjectIdentifier : subjectList.getSubjectIdentifiers()) {
                identifiers.add(subjectIdentifier.getIdentifier());
            }
        }
        return identifiers;
    }

}