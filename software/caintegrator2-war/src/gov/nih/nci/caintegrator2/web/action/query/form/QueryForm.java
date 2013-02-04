/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Top-level UI element for query management.
 */
public class QueryForm {
    
    private Query query;
    private AnnotationDefinitionList clinicalAnnotations;
    private AnnotationDefinitionList imageSeriesAnnotations;
    private CriteriaGroup criteriaGroup;
    private ResultConfiguration resultConfiguration;
    private String orgQueryName = "";
    private boolean controlSamplesInStudy = false;
    
    /**
     * Configures a new query.
     * 
     * @param subscription query belongs to this subscription
     */
    public void createQuery(StudySubscription subscription) {
        query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setSubscription(subscription);
        query.setResultType(ResultTypeEnum.CLINICAL);
        setQuery(query);
        setResultConfiguration(new ResultConfiguration(this));
    }

    private void initialize() {
        if (query != null) {
            Study study = getQuery().getSubscription().getStudy();
            clinicalAnnotations = new AnnotationDefinitionList(
                    study.getStudyConfiguration().getVisibleSubjectAnnotationCollection(), true);
            imageSeriesAnnotations = new AnnotationDefinitionList(
                    study.getStudyConfiguration().getVisibleImageSeriesAnnotationCollection(), true);
            criteriaGroup = new CriteriaGroup(this);
            resultConfiguration = new ResultConfiguration(this);
            controlSamplesInStudy = study.getStudyConfiguration().hasControlSamples();
        } else {
            criteriaGroup = null;
            resultConfiguration = null;
            clinicalAnnotations = null;
            imageSeriesAnnotations = null;
        }
        orgQueryName = "";
    }

    /**
     * Returns the current query.
     * 
     * @return the query.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the query for the form.
     * 
     * @param query the query for the form
     */
    public void setQuery(Query query) {
        this.query = query;
        initialize();
    }

    AnnotationDefinitionList getClinicalAnnotations() {
        return clinicalAnnotations;
    }

    AnnotationDefinitionList getImageSeriesAnnotations() {
        return imageSeriesAnnotations;
    }

    /**
     * @return the criteriaGroup
     */
    public CriteriaGroup getCriteriaGroup() {
        return criteriaGroup;
    }
    
    /**
     * Validates the form prior to saving the query.
     * 
     * @param action receives validation errors.
     */
    public void validate(ValidationAware action) {
        getCriteriaGroup().validate(action);
    }
    
    /**
     * Validates the form prior to saving the query.
     * 
     * @param action receives validation errors.
     */
    public void validateForSave(ValidationAware action) {
        validate(action);
        validateQueryName(action);
    }

    private void validateQueryName(ValidationAware action) {
        if (StringUtils.isBlank(getQuery().getName())) {
            action.addActionError("Query Name is required.");
        } else {
            validateUniqueQueryName(action);
        }
    }

    private void validateUniqueQueryName(ValidationAware action) {
        for (Query nextQuery : getQuery().getSubscription().getQueryCollection()) {
            if (getQuery().getName().equalsIgnoreCase(nextQuery.getName()) && !getQuery().equals(nextQuery)) {
                action.addActionError("There is already a Query named " + getQuery().getName() + ".");
            }
        }
    }

    /**
     * @return the resultConfiguration
     */
    public ResultConfiguration getResultConfiguration() {
        return resultConfiguration;
    }

    private void setResultConfiguration(ResultConfiguration resultConfiguration) {
        this.resultConfiguration = resultConfiguration;
    }

    /**
     * Updates the underlying query with any pending changes.
     */
    public void processCriteriaChanges() {
        getCriteriaGroup().processCriteriaChanges();
    }

    /**
     * Check if this is a saved query.
     * @return boolean of is a saved query
     */
    public boolean isSavedQuery() {
        return (getQuery().getId() != null);
    }
    
    /**
     * @return a list of criteria types
     */
    public List<String> getCriteriaTypeOptions() {
        Study study = query.getSubscription().getStudy();
        List<String> options = new ArrayList<String>();
        options.add(CriterionRowTypeEnum.CLINICAL.getValue());
        if (study.hasGenomicDataSources()) {
            options.add(CriterionRowTypeEnum.GENE_EXPRESSION.getValue());
        }
        if (study.hasImageSeriesData()) {
            options.add(CriterionRowTypeEnum.IMAGE_SERIES.getValue());
        }
        return options;
    }

    /**
     * Check if search result is genomic and all genes are going to be searched.
     * @return boolean of potentially a large query
     */
    public boolean isPotentiallyLargeQuery() {
        if (query != null && query.isGenomicResultType()) {
            return criteriaGroup.hasNoGenomicCriterion();
        }
        return false;
    }

    /**
     * @return the orgQueryName
     */
    public String getOrgQueryName() {
        return orgQueryName;
    }

    /**
     * @param orgQueryName the orgSaveName to set
     */
    public void setOrgQueryName(String orgQueryName) {
        this.orgQueryName = orgQueryName;
    }
    
    /**
     * @return boolean of has image mapping data
     */
    public boolean hasImageDataSources() {
        return query.getSubscription().getStudy().hasImageDataSources();
    }

    /**
     * @return the controlSamplesInStudy
     */
    public boolean isControlSamplesInStudy() {
        return controlSamplesInStudy;
    }
}
