package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class UserWorkspace extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String username;
    private Collection<StudySubscription> subscriptionCollection;
    private Set<StudyConfiguration> studyConfigurationJobs = new HashSet<StudyConfiguration>();
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

    /**
     * @return the studyConfigurationJobs
     */
    public Set<StudyConfiguration> getStudyConfigurationJobs() {
        return studyConfigurationJobs;
    }

    /**
     * @param studyConfigurationJobs the studyConfigurationJobs to set
     */
    @SuppressWarnings("unused") // For hibernate.
    private void setStudyConfigurationJobs(Set<StudyConfiguration> studyConfigurationJobs) {
        this.studyConfigurationJobs = studyConfigurationJobs;
    }

}