/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class UserWorkspace extends AbstractCaIntegrator2Object {
    
    /**
     * Anonymous username.
     */
    public static final String ANONYMOUS_USER_NAME = "anonymousUser";
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private Set<StudySubscription> subscriptionCollection = new HashSet<StudySubscription>();
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
    public Set<StudySubscription> getSubscriptionCollection() {
        return subscriptionCollection;
    }
    
    /**
     * @param subscriptionCollection the subscriptionCollection to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setSubscriptionCollection(Set<StudySubscription> subscriptionCollection) {
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
    
    /**
     * @return if this is an anonymous user or not.
     */
    public boolean isAnonymousUser() {
        return ANONYMOUS_USER_NAME.equals(username);
    }

}
