/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Extend AbstractCaIntegrator2Object with a listCollection.
 */
public abstract class AbstractCaIntegrator2StudyObject extends AbstractCaIntegrator2Object {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;
    
    private Set<AbstractList> listCollection = new HashSet<AbstractList>();
    
    /**
     * @return the listCollection
     */
    public Set<AbstractList> getListCollection() {
        return listCollection;
    }
    
    /**
     * @param listCollection the listCollection to set
     */
    public void setListCollection(Set<AbstractList> listCollection) {
        this.listCollection = listCollection;
    }
    
    /**
     * @return a list of gene lists, ordered by name.
     */
    public List<GeneList> getGeneLists() {
        List<GeneList> resultLists = AbstractList.getListByType(listCollection, GeneList.class);
        Collections.sort(resultLists, AbstractList.ABSTRACT_LIST_NAME_COMPARATOR);
        return resultLists;
    }
    
    /**
     * @return a list of gene list names
     */
    public List<String> getGeneListNames() {
        return AbstractList.getListNamesByType(listCollection, GeneList.class);
    }
    
    /**
     * @param name then gene list name to get
     * @return The gene list
     */
    public GeneList getGeneList(String name) {
        return (GeneList) AbstractList.getListByType(listCollection, name, GeneList.class);
    }
    
    /**
     * @return a list of subject lists, ordered by name.
     */
    public List<SubjectList> getSubjectLists() {
        List<SubjectList> resultLists = AbstractList.getListByType(listCollection, SubjectList.class);
        Collections.sort(resultLists, AbstractList.ABSTRACT_LIST_NAME_COMPARATOR);
        return resultLists;
    }
    
    /**
     * @return a list of subject list names
     */
    public List<String> getSubjectListNames() {
        return AbstractList.getListNamesByType(listCollection, SubjectList.class);
    }
    
    /**
     * @param name then subject list name to get
     * @return The subject list
     */
    public SubjectList getSubjectList(String name) {
        return (SubjectList) AbstractList.getListByType(listCollection, name, SubjectList.class);
    }
}
