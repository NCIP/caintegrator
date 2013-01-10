/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class AbstractList extends AbstractCaIntegrator2Object implements TimeStampable {

    private static final long serialVersionUID = 1L;
    
    private String description;
    private String name;
    private Visibility visibility;
    private Date lastModifiedDate;
    private StudyConfiguration studyConfiguration;
    private StudySubscription subscription;
    
    /**
     * List name comparator.
     */
    public static final Comparator<AbstractList> ABSTRACT_LIST_NAME_COMPARATOR = new Comparator<AbstractList>() {
        public int compare(AbstractList list1, AbstractList list2) {
            return list1.getName().compareToIgnoreCase(list2.getName());
        }
    };
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the subscription
     */
    public StudySubscription getSubscription() {
        return subscription;
    }

    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(StudySubscription subscription) {
        this.subscription = subscription;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate); 
    }

    /**
     * Goes through the listCollection and retrieve all lists matching class type.
     * @param <T> a subclass of AbstractList.
     * @param listCollection to retrieve list from.
     * @param listType the subclass to retrieve.
     * @return List of all lists matching class type.
     */
    @SuppressWarnings("unchecked") // converting T to the class type.
    public static <T> List <T> getListByType(Set<AbstractList> listCollection, Class <T> listType) {
        List<T> resultLists = new ArrayList<T>();
        if (listCollection != null) {
            for (AbstractList abstractList : listCollection) {
                if (abstractList.getClass().equals(listType)) {
                    resultLists.add((T) abstractList);
                }
            }
        }
        return resultLists;
    }
    
    /**
     * @param <T> a subclass of AbstractList.
     * @param listCollection to extract from.
     * @param listType the subclass to retrieve.
     * @return a list of abstract list names
     */
    public static final <T> List<String> getListNamesByType(Set<AbstractList> listCollection, Class <T> listType) {
        List<String> resultListNames = new ArrayList<String>();
        for (T list : getListByType(listCollection, listType)) {
            resultListNames.add(((AbstractList) list).getName());
        }
        return resultListNames;
    }
    
    /**
     * @param <T> a subclass of AbstractList.
     * @param listCollection to extract from.
     * @param name then gene list name to get
     * @param listType the subclass to retrieve.
     * @return The abstract list
     */
    public static final <T> AbstractList getListByType(Set<AbstractList> listCollection,
            String name, Class <T> listType) {
        for (T list : getListByType(listCollection, listType)) {
            if (((AbstractList) list).getName().equals(name)) {
                return (AbstractList) list;
            }
        }
        return null;
    }
}
