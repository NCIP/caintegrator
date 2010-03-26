package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class StudySubscription extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Study study;
    private Set<AbstractList> listCollection = new HashSet<AbstractList>();
    private Set<Query> queryCollection = new HashSet<Query>();
    private Set<AbstractPersistedAnalysisJob> analysisJobCollection = new HashSet<AbstractPersistedAnalysisJob>();
    private UserWorkspace userWorkspace;
    
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    
    /**
     * @return the listCollection
     */
    public Set<AbstractList> getListCollection() {
        return listCollection;
    }
    
    /**
     * @param listCollection the listCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setListCollection(Set<AbstractList> listCollection) {
        this.listCollection = listCollection;
    }
    
    /**
     * @return the queryCollection
     */
    public Set<Query> getQueryCollection() {
        return queryCollection;
    }
    
    /**
     * @param queryCollection the queryCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setQueryCollection(Set<Query> queryCollection) {
        this.queryCollection = queryCollection;
    }

    /**
     * @return the userWorkspace
     */
    public UserWorkspace getUserWorkspace() {
        return userWorkspace;
    }

    /**
     * @param userWorkspace the userWorkspace to set
     */
    public void setUserWorkspace(UserWorkspace userWorkspace) {
        this.userWorkspace = userWorkspace;
    }

    /**
     * @return the analysisJobCollection
     */
    public Set<AbstractPersistedAnalysisJob> getAnalysisJobCollection() {
        return analysisJobCollection;
    }

    /**
     * @param analysisJobCollection the analysisJobCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setAnalysisJobCollection(Set<AbstractPersistedAnalysisJob> analysisJobCollection) {
        this.analysisJobCollection = analysisJobCollection;
    }
    
    private final Comparator<AbstractList> abstractListNameComparator = new Comparator<AbstractList>() {
        public int compare(AbstractList list1, AbstractList list2) {
            return list1.getName().compareToIgnoreCase(list2.getName());
        }
    };
    
    /**
     * @return a list of gene lists, ordered by name.
     */
    public List<GeneList> getGeneLists() {
        List<GeneList> geneLists = new ArrayList<GeneList>();
        if (listCollection != null) {
            for (AbstractList abstractList : listCollection) {
                if (abstractList instanceof GeneList) {
                    geneLists.add((GeneList) abstractList);
                }
            }
            Collections.sort(geneLists, abstractListNameComparator);
        }
        return geneLists;
    }
    
    /**
     * @return a list of gene list names
     */
    public List<String> getGeneListNames() {
        List<String> geneListNames = new ArrayList<String>();
        for (GeneList list : getGeneLists()) {
            geneListNames.add(list.getName());
        }
        return geneListNames;
    }
    
    /**
     * @param name then gene list name to get
     * @return The gene list
     */
    public GeneList getGeneList(String name) {
        for (GeneList list : getGeneLists()) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        return null;
    }
    
    /**
     * @return a list of subject lists, ordered by name.
     */
    public List<SubjectList> getSubjectLists() {
        List<SubjectList> subjectLists = new ArrayList<SubjectList>();
        if (listCollection != null) {
            for (AbstractList abstractList : listCollection) {
                if (abstractList instanceof SubjectList) {
                    subjectLists.add((SubjectList) abstractList);
                }
            }
            Collections.sort(subjectLists, abstractListNameComparator);
        }
        return subjectLists;
    }
    
    /**
     * @return a list of subject list names
     */
    public List<String> getSubjectListNames() {
        List<String> subjectListNames = new ArrayList<String>();
        for (SubjectList list : getSubjectLists()) {
            subjectListNames.add(list.getName());
        }
        return subjectListNames;
    }
    
    /**
     * @param name then subject list name to get
     * @return The subject list
     */
    public SubjectList getSubjectList(String name) {
        for (SubjectList list : getSubjectLists()) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        return null;
    }
}