/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
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
            if (!study.hasVisibleImageSeriesData()) {
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
        checkVisibility();
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        rows.addAll(rowFactory.getImageSeriesRows(dao.findMatchingImageSeries(abstractAnnotationCriterion, study)));
    }

    private void checkVisibility() throws InvalidCriterionException {
        if (!(abstractAnnotationCriterion instanceof IdentifierCriterion)) {
            if (abstractAnnotationCriterion.getAnnotationFieldDescriptor() == null) {
                throw new InvalidCriterionException("Study Manager deleted an annotation definition belonging to a "
                        + "criterion below.  Please delete this criterion and/or select another.");
            }
            if (abstractAnnotationCriterion.getAnnotationFieldDescriptor().getDefinition() == null) {
                throw new InvalidCriterionException("The AnnotationFieldDescriptor '"
                        + abstractAnnotationCriterion.getAnnotationFieldDescriptor().getName()
                        + "' is not associated to an AnnotationDefinition.");
            }
            if (!abstractAnnotationCriterion.getAnnotationFieldDescriptor().isShownInBrowse()) {
                throw new InvalidCriterionException("Invalid criterion exist due to '"
                        + abstractAnnotationCriterion.getAnnotationFieldDescriptor().getDefinition().getDisplayName()
                        + "' is not visible.");
            }
        }
    }

    private void handleSampleRow(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes,
            Set<ResultRow> rows) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        rows.addAll(rowFactory.getSampleRows(dao.findMatchingSamples(abstractAnnotationCriterion, study)));
    }

    private void handleSubjectRow(CaIntegrator2Dao dao, Study study, Set<EntityTypeEnum> entityTypes,
            Set<ResultRow> rows) throws InvalidCriterionException {
        checkVisibility();
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        rows.addAll(rowFactory.getSubjectRows(dao.findMatchingSubjects(abstractAnnotationCriterion, study)));
    }

    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType,
            Platform platform) {
        return Collections.emptySet();
    }

    @Override
    boolean isReporterMatchHandler() {
        return false;
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
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

    @Override
    Set<SegmentData> getSegmentDataMatches(CaIntegrator2Dao dao, Study study, Platform platform) {
        return Collections.emptySet();
    }

    @Override
    boolean hasSegmentDataCriterion() {
        return false;
    }

    @Override
    boolean hasCriterionSpecifiedSegmentValues() {
        return false;
    }

    @Override
    boolean hasCriterionSpecifiedSegmentCallsValues() {
        return false;
    }

    @Override
    GenomicCriteriaMatchTypeEnum getSegmentValueMatchCriterionType(Float value) {
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

    @Override
    GenomicCriteriaMatchTypeEnum getSegmentCallsValueMatchCriterionType(Integer callsValue) {
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

}
