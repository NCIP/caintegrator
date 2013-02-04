/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Handler that returns samples matching the given expression level criterion.
 */
public final class ExpressionLevelCriterionHandler extends AbstractCriterionHandler {

    private final ExpressionLevelCriterion criterion;

    private ExpressionLevelCriterionHandler(ExpressionLevelCriterion criterion) {
        this.criterion = criterion;
    }

    /**
     *
     * @param expressionLevelCriterion the expression level criterion
     * @return the expression level criterion handler
     */
    public static ExpressionLevelCriterionHandler create(ExpressionLevelCriterion expressionLevelCriterion) {
        return new ExpressionLevelCriterionHandler(expressionLevelCriterion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query,
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        Study study = query.getSubscription().getStudy();
        ReporterTypeEnum reporterType = query.getReporterType();
        Platform platform = query.getGeneExpressionPlatform();
        Set<AbstractReporter> reporters = getReporterMatches(dao, study, reporterType, platform);

        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addReporters(reporters);
        request.addArrayDatas(getCandidateArrayDatas(study, reporterType, platform));
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);

        ArrayDataValues values = arrayDataService.getData(request);
        return getRows(values, entityTypes);
    }

    private Set<ResultRow> getRows(ArrayDataValues values, Set<EntityTypeEnum> entityTypes) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        Set<SampleAcquisition> sampleAcquisitions = new HashSet<SampleAcquisition>();
        for (ArrayData arrayData : values.getArrayDatas()) {
            if (hasExpressionLevelMatch(arrayData, values)) {
                sampleAcquisitions.addAll(arrayData.getSample().getSampleAcquisitions());
            }
        }
        return rowFactory.getSampleRows(sampleAcquisitions);
    }

    private boolean hasExpressionLevelMatch(ArrayData arrayData, ArrayDataValues values) {
        for (AbstractReporter reporter : values.getReporters()) {
            Float expressionValue = values.getFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL);
            if (getExpressionMatchType(expressionValue) != GenomicCriteriaMatchTypeEnum.NO_MATCH) {
                return true;
            }
        }
        return false;
    }

    private GenomicCriteriaMatchTypeEnum getExpressionMatchType(Float expressionValue) {
        switch (criterion.getRangeType()) {
            case GREATER_OR_EQUAL:
                if (isExpressionGreaterMatch(expressionValue)) {
                    return GenomicCriteriaMatchTypeEnum.OVER;
                }
                break;
            case LESS_OR_EQUAL:
                if (isExpressionLowerMatch(expressionValue)) {
                    return GenomicCriteriaMatchTypeEnum.UNDER;
                }
                break;
            case INSIDE_RANGE:
                if (isExpressionGreaterMatch(expressionValue) && isExpressionLowerMatch(expressionValue)) {
                    return GenomicCriteriaMatchTypeEnum.BETWEEN;
                }
                break;
            case OUTSIDE_RANGE:
                if (!(isExpressionGreaterMatch(expressionValue) && isExpressionLowerMatch(expressionValue))) {
                    if (expressionValue >= criterion.getUpperLimit()) {
                        return GenomicCriteriaMatchTypeEnum.OVER;
                    }
                    return GenomicCriteriaMatchTypeEnum.UNDER;
                }
                break;
            default:
                throw new IllegalStateException("Illegal regulation type: " + criterion.getRangeType());
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

    @Override
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        if (isReporterMatch(genes)) {
            return getExpressionMatchType(value);
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

    private boolean isReporterMatch(Set<Gene> genes) {
        if (StringUtils.isBlank(criterion.getGeneSymbol())) {
            return true;
        } else {
            for (Gene gene : genes) {
                if (criterion.getGeneSymbol().toUpperCase().contains(gene.getSymbol().toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExpressionLowerMatch(Float expressionValue) {
        return criterion.getUpperLimit() == null ? true : expressionValue <= criterion.getUpperLimit();
    }

    private boolean isExpressionGreaterMatch(Float expressionValue) {
        return criterion.getLowerLimit() == null ? true : expressionValue >= criterion.getLowerLimit();
    }

    private Collection<ArrayData> getCandidateArrayDatas(Study study,
            ReporterTypeEnum reporterType
            , Platform platform) {
        Set<ArrayData> candidateDatas = new HashSet<ArrayData>();
        candidateDatas.addAll(study.getArrayDatas(reporterType, platform));
        return candidateDatas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType,
            Platform platform) {
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        if (StringUtils.isBlank(criterion.getGeneSymbol())) {
            reporters.addAll(findReporters(reporterType, study, platform));
        } else {
            reporters.addAll(dao.findReportersForGenes(criterion.getGeneSymbols(), reporterType, study, platform));
        }
        return reporters;
    }

    private Set<AbstractReporter> findReporters(ReporterTypeEnum reporterType, Study study, Platform platform) {
        Set<ReporterList> studyReporterLists = getStudyReporterLists(study, reporterType, platform);
        if (studyReporterLists.isEmpty()) {
            return Collections.emptySet();
        }
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (ReporterList reporterList : studyReporterLists) {
            reporters.addAll(reporterList.getReporters());
        }
        return reporters;
    }

    private Set<ReporterList> getStudyReporterLists(Study study, ReporterTypeEnum reporterType, Platform platform) {
        Set<ReporterList> reporterLists = new HashSet<ReporterList>();
        for (ArrayData arrayData : study.getArrayDatas(reporterType, platform)) {
            reporterLists.addAll(arrayData.getReporterLists());
        }
        return reporterLists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasReporterCriterion() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isReporterMatchHandler() {
        return true;
    }

    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        return true;
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
