/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles AnnotationCriterion objects by retrieving the proper data from the DAO.
 */
class AnnotationCriterionHandler extends AbstractCriterionHandler {

    private final AbstractAnnotationCriterion abstractAnnotationCriterion;
    
    /**
     * Constructor based on the abstractAnnotationCriterion to use.
     * @param abstractAnnotationCriterion criterion object to use.
     */
    AnnotationCriterionHandler(AbstractAnnotationCriterion abstractAnnotationCriterion) {
        this.abstractAnnotationCriterion = abstractAnnotationCriterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        Study study = query.getSubscription().getStudy();
        EntityTypeEnum entityType = abstractAnnotationCriterion.getEntityType();
        Set<ResultRow> resultRows = new HashSet<ResultRow>();
        switch(entityType) {
        case IMAGESERIES:
            if (!study.hasImageSeriesData()) {
                throw new InvalidCriterionException("Invalid criterion exist due to no Imaging data in Study.");
            }
            handleImageSeriesRow(dao, study, entityTypes, resultRows);
            break;
        case SAMPLE:
            handleSampleRow(dao, study, entityTypes, resultRows);
            break;
        case SUBJECT:
            handleSubjectRow(dao, study, entityTypes, resultRows);
            break;
        default:
            throw new IllegalArgumentException("Unsupported EntityType: " + entityType);
        }
        return resultRows;
    }

    private void handleImageSeriesRow(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes,
            Set<ResultRow> rows) throws InvalidCriterionException {
        checkImageSeriesVisibility(study);
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        rows.addAll(rowFactory.getImageSeriesRows(dao.findMatchingImageSeries(abstractAnnotationCriterion, study)));
    }
    
    private void checkImageSeriesVisibility(Study study) throws InvalidCriterionException {
        if (!study.getStudyConfiguration().getVisibleImageSeriesAnnotationCollection().
                contains(abstractAnnotationCriterion.getAnnotationDefinition())) {
            throw new InvalidCriterionException(errorNotVisible());
        }
    }
    
    private String errorNotVisible() {
        return "Invalid criterion exist due to '"
            + abstractAnnotationCriterion.getAnnotationDefinition().getDisplayName()
            + "' is not visible.";
    }

    private void handleSampleRow(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes,
            Set<ResultRow> rows) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        rows.addAll(rowFactory.getSampleRows(dao.findMatchingSamples(abstractAnnotationCriterion, study)));
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity" }) // Lots of type checking and row adding.
    private void handleSubjectRow(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes,
            Set<ResultRow> rows) throws InvalidCriterionException {
        checkSubjectVisibility(study);
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        rows.addAll(rowFactory.getSubjectRows(dao.findMatchingSubjects(abstractAnnotationCriterion, study)));
    }
    
    private void checkSubjectVisibility(Study study) throws InvalidCriterionException {
        if (abstractAnnotationCriterion.getAnnotationDefinition() != null
                && !study.getStudyConfiguration().getVisibleSubjectAnnotationCollection().
                contains(abstractAnnotationCriterion.getAnnotationDefinition())) {
            throw new InvalidCriterionException(errorNotVisible());
        }
    }

    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType) {
        return Collections.emptySet();
    }

    @Override
    boolean isEntityMatchHandler() {
        return true;
    }

    @Override
    boolean isReporterMatchHandler() {
        return false;
    }

    @Override
    boolean hasEntityCriterion() {
        return true;
    }
    
    @Override
    boolean hasReporterCriterion() {
        return false;
    }

    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        return false;
    }

    @Override
    boolean isGenomicValueMatchCriterion(Set<Gene> genes, Float value) {
        return false;
    }

}
