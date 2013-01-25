/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class UserWorkspace extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String username;
    private Collection<StudySubscription> subscriptionCollection;
    private StudySubscription defaultSubscription;
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * @return the subscriptionCollection
     */
    public Collection<StudySubscription> getSubscriptionCollection() {
        return subscriptionCollection;
    }
    
    /**
     * @param subscriptionCollection the subscriptionCollection to set
     */
    public void setSubscriptionCollection(Collection<StudySubscription> subscriptionCollection) {
        this.subscriptionCollection = subscriptionCollection;
    }
    
    /**
     * @return the defaultSubscription
     */
    public StudySubscription getDefaultSubscription() {
        return defaultSubscription;
    }
    
    /**
     * @param defaultSubscription the defaultSubscription to set
     */
    public void setDefaultSubscription(StudySubscription defaultSubscription) {
        this.defaultSubscription = defaultSubscription;
    }

}
