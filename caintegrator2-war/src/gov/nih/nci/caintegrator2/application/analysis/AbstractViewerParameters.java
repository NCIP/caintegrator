/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

/**
 * Parameters used to run viewer.
 */
public abstract class AbstractViewerParameters {
    private StudySubscription studySubscription;
    private Query query;
    private String sessionId; 
    private String urlPrefix;
    private boolean viewAllData = false;
    private boolean useCGHCall = false;
    
    /**
     * @return the viewAllIGV
     */
    public boolean isViewAllData() {
        return viewAllData;
    }

    /**
     * @param viewAllData the viewAlldata to set
     */
    public void setViewAllData(boolean viewAllData) {
        this.viewAllData = viewAllData;
    }

    /**
     * @return the useCGHCall
     */
    public boolean isUseCGHCall() {
        return useCGHCall;
    }

    /**
     * @param useCGHCall the useCGHCall to set
     */
    public void setUseCGHCall(boolean useCGHCall) {
        this.useCGHCall = useCGHCall;
    }

    /**
     * @return the studySubscription
     */
    public StudySubscription getStudySubscription() {
        return studySubscription;
    }
    
    /**
     * @param studySubscription the studySubscription to set
     */
    public void setStudySubscription(StudySubscription studySubscription) {
        this.studySubscription = studySubscription;
    }
    
    /**
     * @return the query
     */
    public Query getQuery() {
        return query;
    }
    
    /**
     * @param query the query to set
     */
    public void setQuery(Query query) {
        this.query = query;
    }
    
    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }
    
    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    /**
     * @return the urlPrefix
     */
    public String getUrlPrefix() {
        return urlPrefix;
    }
    
    /**
     * @param urlPrefix the urlPrefix to set
     */
    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
