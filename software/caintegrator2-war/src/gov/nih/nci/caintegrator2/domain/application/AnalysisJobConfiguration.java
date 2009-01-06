package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.analysis.AnalysisMethod;

/**
 * 
 */
public class AnalysisJobConfiguration extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private StudySubscription subscription;
    private AnalysisMethod analysisMethod;
    
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
     * @return the analysisMethod
     */
    public AnalysisMethod getAnalysisMethod() {
        return analysisMethod;
    }
    
    /**
     * @param analysisMethod the analysisMethod to set
     */
    public void setAnalysisMethod(AnalysisMethod analysisMethod) {
        this.analysisMethod = analysisMethod;
    }

}