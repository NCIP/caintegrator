package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class Query extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    
    private String description;
    private String name;
    private String reporterType;
    private String resultType;
    private String visibility;
    
    private StudySubscription subscription;
    private CompoundCriterion compoundCriterion;
    private Collection<ResultColumn> columnCollection;
    
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
     * @return the reporterType
     */
    public String getReporterType() {
        return reporterType;
    }
    
    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(String reporterType) {
        this.reporterType = reporterType;
    }
    
    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }
    
    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
    
    /**
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }
    
    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
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
     * @return the compoundCriterion
     */
    public CompoundCriterion getCompoundCriterion() {
        return compoundCriterion;
    }
    
    /**
     * @param compoundCriterion the compoundCriterion to set
     */
    public void setCompoundCriterion(CompoundCriterion compoundCriterion) {
        this.compoundCriterion = compoundCriterion;
    }
    
    /**
     * @return the columnCollection
     */
    public Collection<ResultColumn> getColumnCollection() {
        return columnCollection;
    }
    
    /**
     * @param columnCollection the columnCollection to set
     */
    public void setColumnCollection(Collection<ResultColumn> columnCollection) {
        this.columnCollection = columnCollection;
    }

}