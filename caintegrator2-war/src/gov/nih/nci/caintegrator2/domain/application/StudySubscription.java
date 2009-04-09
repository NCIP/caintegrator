package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class StudySubscription extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Study study;
    private Collection<AbstractList> listCollection;
    private Collection<Query> queryCollection;
    private Set<GenePatternAnalysisJob> genePatternAnalysisJobCollection = new HashSet<GenePatternAnalysisJob>();
    private Set<ComparativeMarkerSelectionAnalysisJob> comparativeMarkerSelectionAnalysisJobCollection =
        new HashSet<ComparativeMarkerSelectionAnalysisJob>();
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
     * @return the genePatternAnalysisJobCollection
     */
    public Set<GenePatternAnalysisJob> getGenePatternAnalysisJobCollection() {
        return genePatternAnalysisJobCollection;
    }

    /**
     * @param genePatternAnalysisJobCollection the genePatternAnalysisJobCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setGenePatternAnalysisJobCollection(Set<GenePatternAnalysisJob> genePatternAnalysisJobCollection) {
        this.genePatternAnalysisJobCollection = genePatternAnalysisJobCollection;
    }

    /**
     * @return the comparativeMarkerSelectionAnalysisJobCollection
     */
    public Set<ComparativeMarkerSelectionAnalysisJob> getComparativeMarkerSelectionAnalysisJobCollection() {
        return comparativeMarkerSelectionAnalysisJobCollection;
    }

    /**
     * @param comparativeMarkerSelectionAnalysisJobCollection the comparativeMarkerSelectionAnalysisJobCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setComparativeMarkerSelectionAnalysisJobCollection(
            Set<ComparativeMarkerSelectionAnalysisJob> comparativeMarkerSelectionAnalysisJobCollection) {
        this.comparativeMarkerSelectionAnalysisJobCollection = comparativeMarkerSelectionAnalysisJobCollection;
    }

}