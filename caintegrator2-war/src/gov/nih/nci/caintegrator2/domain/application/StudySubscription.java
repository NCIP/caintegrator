package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collection;
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
    private Collection<AbstractList> listCollection;
    private Collection<Query> queryCollection;
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
    public Collection<AbstractList> getListCollection() {
        return listCollection;
    }
    
    /**
     * @param listCollection the listCollection to set
     */
    public void setListCollection(Collection<AbstractList> listCollection) {
        this.listCollection = listCollection;
    }
    
    /**
     * @return the queryCollection
     */
    public Collection<Query> getQueryCollection() {
        return queryCollection;
    }
    
    /**
     * @param queryCollection the queryCollection to set
     */
    public void setQueryCollection(Collection<Query> queryCollection) {
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
            Comparator<GeneList> nameComparator = new Comparator<GeneList>() {
                public int compare(GeneList geneList1, GeneList geneList2) {
                    return geneList1.getName().compareToIgnoreCase(geneList2.getName());
                }
            };
            Collections.sort(geneLists, nameComparator);
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
    

}