/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Top-level UI element for query management.
 */
public class QueryForm {
    
    private Query query;
    private final List<AnnotationGroup> sortedAnnotationGroups = new ArrayList<AnnotationGroup>();
    private final List<String> annotationGroupNames = new ArrayList<String>();
    private final List<String> platformNames = new ArrayList<String>();
    private final Map<String, AnnotationFieldDescriptorList> annotationGroupMap = 
        new HashMap<String, AnnotationFieldDescriptorList>();
    private CriteriaGroup criteriaGroup;
    private ResultConfiguration resultConfiguration;
    private String orgQueryName = "";
    private boolean controlSamplesInStudy = false;
    private boolean studyHasSavedLists = false;
    private boolean studyHasMultiplePlatforms = false;
    
    private String genomicPreviousSorting;
    private int genomicSortingOrder = -1;
    
    /**
     * Configures a new query.
     * 
     * @param subscription query belongs to this subscription
     * @param geneExpressionPlatformsInStudy the platforms for the study (null if unknown)
     */
    public void createQuery(StudySubscription subscription, Set<String> geneExpressionPlatformsInStudy) {
        query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setSubscription(subscription);
        query.setResultType(ResultTypeEnum.CLINICAL);
        setQuery(query, geneExpressionPlatformsInStudy);
        setResultConfiguration(new ResultConfiguration(this));
    }

    private void initialize(Set<String> geneExpressionPlatformsInStudy) {
        studyHasMultiplePlatforms = false;
        platformNames.clear();
        if (query != null) {
            Study study = getQuery().getSubscription().getStudy();
            initializeAnnotationGroups(study);
            if (geneExpressionPlatformsInStudy != null && geneExpressionPlatformsInStudy.size() > 1) {
                setupPlatforms(geneExpressionPlatformsInStudy);
            } 
            criteriaGroup = new CriteriaGroup(this);
            resultConfiguration = new ResultConfiguration(this);
            controlSamplesInStudy = study.getStudyConfiguration().hasControlSamples();
            studyHasSavedLists = !getQuery().getSubscription().getSubjectLists().isEmpty();
        } else {
            criteriaGroup = null;
            resultConfiguration = null;
            annotationGroupNames.clear();
            annotationGroupMap.clear();
            sortedAnnotationGroups.clear();
            platformNames.clear();
        }
        orgQueryName = "";
    }

    private void setupPlatforms(Set<String> geneExpressionPlatformsInStudy) {
        studyHasMultiplePlatforms = true;
        platformNames.clear();
        platformNames.addAll(geneExpressionPlatformsInStudy);
        Collections.sort(platformNames);
    }
    
    private void initializeAnnotationGroups(Study study) {
        annotationGroupNames.clear();
        Set<AnnotationGroup> groupsWithNoVisibleFieldDescriptors = new HashSet<AnnotationGroup>();
        for (AnnotationGroup group : study.getAnnotationGroups()) {
            if (!group.getVisibleAnnotationFieldDescriptors().isEmpty()) {
                annotationGroupMap.put(group.getName(), new AnnotationFieldDescriptorList(group
                        .getVisibleAnnotationFieldDescriptors()));
                annotationGroupNames.add(group.getName());
            } else {
                groupsWithNoVisibleFieldDescriptors.add(group);
            }
        }
        sortedAnnotationGroups.clear();
        sortedAnnotationGroups.addAll(study.getSortedAnnotationGroups());
        sortedAnnotationGroups.removeAll(groupsWithNoVisibleFieldDescriptors);
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
     * @param q the query for the form
     * @param geneExpressionPlatformsInStudy the platforms for the study (null if unknown)
     */
    public void setQuery(Query q, Set<String> geneExpressionPlatformsInStudy) {
        this.query = q;
        initialize(geneExpressionPlatformsInStudy);
    }
    
    AnnotationFieldDescriptorList getAnnotations(String groupName) {
        return annotationGroupMap.get(groupName);
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
        for (AnnotationGroup group : sortedAnnotationGroups) {
            options.add(group.getName());
        }
        if (study.hasGenomicDataSources()) {
            options.add(CriterionRowTypeEnum.GENE_EXPRESSION.getValue());
        }
        options.add(CriterionRowTypeEnum.UNIQUE_IDENTIIFER.getValue());
        if (studyHasSavedLists) {
            options.add(CriterionRowTypeEnum.SAVED_LIST.getValue());
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

    /**
     * @return the annotationGroupNames
     */
    public List<String> getAnnotationGroupNames() {
        return annotationGroupNames;
    }

    /**
     * @return the genomicPreviousSorting
     */
    public String getGenomicPreviousSorting() {
        return genomicPreviousSorting;
    }

    /**
     * @param genomicPreviousSorting the genomicPreviousSorting to set
     */
    public void setGenomicPreviousSorting(String genomicPreviousSorting) {
        this.genomicPreviousSorting = genomicPreviousSorting;
    }

    /**
     * @return the genomicSortingOrder
     */
    public int getGenomicSortingOrder() {
        return genomicSortingOrder;
    }

    /**
     * @param genomicSortingOrder the genomicSortingOrder to set
     */
    public void setGenomicSortingOrder(int genomicSortingOrder) {
        this.genomicSortingOrder = genomicSortingOrder;
    }

    /**
     * Reverse the sorting order of the genomic results.
     */
    public void reverseGenomicSortingOrder() {
        this.genomicSortingOrder *= -1;
    }

    /**
     * @return the studyHasMultiplePlatforms
     */
    public boolean isStudyHasMultiplePlatforms() {
        return studyHasMultiplePlatforms;
    }

    /**
     * @return the platformNames
     */
    public List<String> getPlatformNames() {
        return platformNames;
    }
}
