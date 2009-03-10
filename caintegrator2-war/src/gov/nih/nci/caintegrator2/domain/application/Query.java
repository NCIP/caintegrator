package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class Query extends AbstractCaIntegrator2Object implements Cloneable {
    
    private static final long serialVersionUID = 1L;
    
    private String description;
    private String name;
    private ReporterTypeEnum reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET;
    private ResultTypeEnum resultType = ResultTypeEnum.CLINICAL;
    private String visibility;
    
    private StudySubscription subscription;
    private CompoundCriterion compoundCriterion;
    private Collection<ResultColumn> columnCollection = new HashSet<ResultColumn>();
    
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

    /**
     * @return a  boolean of is a genomic result type
     */
    public boolean isGenomicResultType() {
        return getResultType().equals(ResultTypeEnum.GENOMIC);
    }

    /**
     * @return the reporterType
     */
    public ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(ReporterTypeEnum reporterType) {
        this.reporterType = reporterType;
    }

    /**
     * @return the resultType
     */
    public ResultTypeEnum getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(ResultTypeEnum resultType) {
        this.resultType = resultType;
    }

    /**
     * {@inheritDoc}
     */
    public Query clone() throws CloneNotSupportedException {
        Query clone = (Query) super.clone();
        clone.setCompoundCriterion((CompoundCriterion) compoundCriterion.clone());
        clone.setColumnCollection(cloneColumnCollection());
        return clone;
    }
    
    private Collection<ResultColumn> cloneColumnCollection() throws CloneNotSupportedException {
        Collection<ResultColumn> clone = new HashSet<ResultColumn>();
        for (ResultColumn resultColumn : columnCollection) {
            clone.add(resultColumn.clone());
        }
        return clone;
    }

}