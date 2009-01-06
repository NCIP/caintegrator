package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collection;

/**
 * 
 */
public class StudySubscription extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Study study;
    private Collection<AbstractList> listCollection;
    private Collection<Query> queryCollection;
    
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

}