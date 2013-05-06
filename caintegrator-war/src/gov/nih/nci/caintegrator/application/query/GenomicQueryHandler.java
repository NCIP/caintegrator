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
import gov.nih.nci.caintegrator.common.HibernateUtil;
import gov.nih.nci.caintegrator.common.QueryUtil;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.application.SegmentDataResultValue;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

/**
 * Runs queries that return <code>GenomicDataQueryResults</code>.
 */
class GenomicQueryHandler {
    private static final float DECIMAL_100 = 100.0f;
    private final Query query;
    private final CaIntegrator2Dao dao;
    private final ArrayDataService arrayDataService;

    GenomicQueryHandler(Query query, CaIntegrator2Dao dao, ArrayDataService arrayDataService) {
        this.query = query;
        this.dao = dao;
        this.arrayDataService = arrayDataService;
    }

    GenomicDataQueryResult execute() throws InvalidCriterionException {
        if (ResultTypeEnum.COPY_NUMBER.equals(query.getResultType())) {
            return createCopyNumberResult();
        }
        ArrayDataValues values = getDataValues();
        return createGeneExpressionResult(values);
    }

    Collection<SegmentData> retrieveSegmentDataQuery() throws InvalidCriterionException {
        Collection<ArrayData> arrayDatas = getMatchingArrayDatas();
        return getMatchingSegmentDatas(arrayDatas);

    }

    private GenomicDataQueryResult createGeneExpressionResult(ArrayDataValues values) {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        createGeneExpressionResultRows(result, values);
        Map<Long, GenomicDataResultRow> reporterIdToRowMap = createReporterIdToRowMap(result);
        for (ArrayData arrayData : values.getArrayDatas()) {
            addToGeneExpressionResult(values, result, reporterIdToRowMap, arrayData);
        }
        result.setQuery(query);
        return result;
    }

    private GenomicDataQueryResult createCopyNumberResult()
        throws InvalidCriterionException {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        Collection<ArrayData> arrayDatas = getMatchingArrayDatas();
        Collection<SegmentData> segmentDatas = getMatchingSegmentDatas(arrayDatas);
        Map<SegmentData, GenomicDataResultRow> segmentDataToRowMap =
            createCopyNumberResultRows(result, arrayDatas, segmentDatas);
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(
                query.getCompoundCriterion(), query.getResultType());
        result.setHasCriterionSpecifiedValues(criterionHandler.hasCriterionSpecifiedSegmentValues());
        for (ArrayData arrayData : arrayDatas) {
            addToCopyNumberResult(result, segmentDataToRowMap, arrayData, criterionHandler);
        }
        result.setQuery(query);
        return result;
    }

    private void addToCopyNumberResult(GenomicDataQueryResult result,
            Map<SegmentData, GenomicDataResultRow> segmentDataToRowMap, ArrayData arrayData,
            CompoundCriterionHandler criterionHandler) {

        GenomicDataResultColumn column = result.addColumn();
        //Just take the 1st sample aquisition
        column.setSampleAcquisition(arrayData.getSample().getSampleAcquisitions().iterator().next());
        for (SegmentData segmentData : segmentDataToRowMap.keySet()) {
            if (segmentData.getArrayData().equals(arrayData)) {
                GenomicDataResultRow row = segmentDataToRowMap.get(segmentData);
                GenomicDataResultValue value = new GenomicDataResultValue();
                value.setColumn(column);
                value.setValue(twoDecimalPoint(segmentData.getSegmentValue()));
                value.setCallsValue(segmentData.getCallsValue());
                value.setProbabilityAmplification(twoDecimalPoint(segmentData.getProbabilityAmplification()));
                value.setProbabilityGain(twoDecimalPoint(segmentData.getProbabilityGain()));
                value.setProbabilityLoss(twoDecimalPoint(segmentData.getProbabilityLoss()));
                value.setProbabilityNormal(twoDecimalPoint(segmentData.getProbabilityNormal()));
                checkMeetsCopyNumberCriterion(result, criterionHandler, row, value);
                row.getValues().add(value);
            }
        }
    }

    private float twoDecimalPoint(float floatValue) {
        return Math.round(floatValue * DECIMAL_100) / DECIMAL_100;
    }

    private Map<SegmentData, GenomicDataResultRow> createCopyNumberResultRows(
            GenomicDataQueryResult result, Collection<ArrayData> arrayDatas,
            Collection<SegmentData> segmentDatas) {
        Map<Integer, Map<Integer, GenomicDataResultRow>> startEndPositionResultRowMap
            = new HashMap<Integer, Map<Integer, GenomicDataResultRow>>();
        Map<SegmentData, GenomicDataResultRow> segmentDataToRowMap = new HashMap<SegmentData, GenomicDataResultRow>();
        GenomeBuildVersionEnum genomeVersion = query.getCopyNumberPlatform().getGenomeVersion();
        boolean isGenomeVersionMapped = dao.isGenomeVersionMapped(genomeVersion);
        for (SegmentData segmentData : segmentDatas) {
            if (arrayDatas.contains(segmentData.getArrayData())) {
                int startPosition = segmentData.getLocation().getStartPosition();
                int endPosition = segmentData.getLocation().getEndPosition();
                if (!startEndPositionResultRowMap.containsKey(startPosition)) {
                    startEndPositionResultRowMap.put(startPosition, new HashMap<Integer, GenomicDataResultRow>());
                }
                if (!startEndPositionResultRowMap.get(startPosition).containsKey(endPosition)) {
                    GenomicDataResultRow row = new GenomicDataResultRow();
                    addSegmentDataToRow(segmentData, row, isGenomeVersionMapped, genomeVersion);
                    startEndPositionResultRowMap.get(startPosition).put(endPosition, row);
                    result.getRowCollection().add(row);
                }
                segmentDataToRowMap.put(segmentData, startEndPositionResultRowMap.get(startPosition).get(endPosition));
            }
        }
        return segmentDataToRowMap;
    }

    private void addSegmentDataToRow(SegmentData segmentData, GenomicDataResultRow row,
            boolean isGenomeVersionMapped, GenomeBuildVersionEnum genomeVersion) {
        row.setSegmentDataResultValue(new SegmentDataResultValue());
        row.getSegmentDataResultValue().setChromosomalLocation(segmentData.getLocation());
        if (isGenomeVersionMapped) {
            row.getSegmentDataResultValue().getGenes().addAll(
                    dao.findGenesByLocation(segmentData.getLocation().getChromosome(), segmentData.getLocation()
                            .getStartPosition(), segmentData.getLocation().getEndPosition(),
                            genomeVersion));
        }
    }

    private void addToGeneExpressionResult(ArrayDataValues values, GenomicDataQueryResult result,
        Map<Long, GenomicDataResultRow> reporterIdToRowMap, ArrayData arrayData) {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(
                query.getCompoundCriterion(), query.getResultType());
        result.setHasCriterionSpecifiedValues(criterionHandler.hasCriterionSpecifiedReporterValues());
        GenomicDataResultColumn column = result.addColumn();
        //Take the 1st sample acquisition
        column.setSampleAcquisition(arrayData.getSample().getSampleAcquisitions().iterator().next());
        for (AbstractReporter reporter : values.getReporters()) {
            if (query.isNeedsGenomicHighlighting()) {
                HibernateUtil.loadCollection(reporter.getGenes());
                HibernateUtil.loadCollection(reporter.getSamplesHighVariance());
            }
            GenomicDataResultRow row = reporterIdToRowMap.get(reporter.getId());
            GenomicDataResultValue value = new GenomicDataResultValue();
            value.setColumn(column);
            float floatValue = values.getFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL);
            value.setValue(twoDecimalPoint(floatValue));
            if (query.isNeedsGenomicHighlighting()) {
                checkMeetsGeneExpressionCriterion(result, criterionHandler, reporter, row, value);
                checkHighVariance(result, arrayData, reporter, value);
            }
            row.getValues().add(value);
        }
    }

    private void checkMeetsGeneExpressionCriterion(GenomicDataQueryResult result,
            CompoundCriterionHandler criterionHandler,
            AbstractReporter reporter, GenomicDataResultRow row, GenomicDataResultValue value) {
        if (result.isHasCriterionSpecifiedValues()) {
            value.setCriteriaMatchType(criterionHandler.
                    getGenomicValueMatchCriterionType(reporter.getGenes(), value.getValue()));
            row.setHasMatchingValues(row.isHasMatchingValues() || value.isMeetsCriterion());
        }
    }

    private void checkMeetsCopyNumberCriterion(GenomicDataQueryResult result, CompoundCriterionHandler criterionHandler,
            GenomicDataResultRow row, GenomicDataResultValue value) {
        if (result.isHasCriterionSpecifiedValues()) {
            if (criterionHandler.hasCriterionSpecifiedSegmentValues()
                    && !criterionHandler.hasCriterionSpecifiedSegmentCallsValues()) {
                value.setCriteriaMatchType(criterionHandler.getSegmentValueMatchCriterionType(value.getValue()));
            } else {
                value.setCriteriaMatchType(criterionHandler.
                        getSegmentCallsValueMatchCriterionType(value.getCallsValue()));
            }
            row.setHasMatchingValues(row.isHasMatchingValues() || value.isMeetsCriterion());
        }
    }

    private void checkHighVariance(GenomicDataQueryResult result,
            ArrayData arrayData, AbstractReporter reporter, GenomicDataResultValue value) {
        if (reporter.getSamplesHighVariance().contains(arrayData.getSample())) {
            value.setHighVariance(true);
            result.setHasHighVarianceValues(true);
        }
    }
    private Map<Long, GenomicDataResultRow> createReporterIdToRowMap(GenomicDataQueryResult result) {
        Map<Long, GenomicDataResultRow> rowMap = Maps.newHashMapWithExpectedSize(result.getRowCollection().size());
        for (GenomicDataResultRow row : result.getRowCollection()) {
            rowMap.put(row.getReporter().getId(), row);
        }
        return rowMap;
    }

    private void createGeneExpressionResultRows(GenomicDataQueryResult result, ArrayDataValues values) {
        for (AbstractReporter reporter : values.getReporters()) {
            GenomicDataResultRow row = new GenomicDataResultRow();
            row.setReporter(reporter);
            result.getRowCollection().add(row);
        }
    }

    private ArrayDataValues getDataValues() throws InvalidCriterionException {
        Collection<ArrayData> arrayDatas = getMatchingArrayDatas();
        Collection<AbstractReporter> reporters = getMatchingReporters(arrayDatas);
        return getDataValues(arrayDatas, reporters);
    }

    private ArrayDataValues getDataValues(Collection<ArrayData> arrayDatas, Collection<AbstractReporter> reporters) {
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addReporters(reporters);
        request.addArrayDatas(arrayDatas);
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        if (QueryUtil.isFoldChangeQuery(query) && !query.isAllGenomicDataQuery()) {
            return arrayDataService.getFoldChangeValues(request, query);
        } else {
            return arrayDataService.getData(request);
        }
    }

    private Collection<ArrayData> getMatchingArrayDatas() throws InvalidCriterionException {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(
                query.getCompoundCriterion(), query.getResultType());
        Set<EntityTypeEnum> samplesOnly = new HashSet<EntityTypeEnum>();
        samplesOnly.add(EntityTypeEnum.SAMPLE);
        Set<ResultRow> rows = criterionHandler.getMatches(dao, arrayDataService, query, samplesOnly);
        return getArrayDatas(rows);
    }

    private void addMatchesFromArrayDatas(Set<ArrayData> matchingArrayDatas,
            Collection<ArrayData> candidateArrayDatas) {
        Platform platform = query.getGeneExpressionPlatform();
        ReporterTypeEnum reporterTypeToUse = query.getReporterType();
        if (ResultTypeEnum.COPY_NUMBER == query.getResultType()) {
            platform = query.getCopyNumberPlatform();
            reporterTypeToUse = ReporterTypeEnum.DNA_ANALYSIS_REPORTER;
        }
        for (ArrayData arrayData : candidateArrayDatas) {
            for (ReporterList reporterList : arrayData.getReporterLists()) {
                if (isMatchingArrayData(platform, reporterTypeToUse, arrayData, reporterList)) {
                    matchingArrayDatas.add(arrayData);
                }
            }
        }
    }

    private boolean isMatchingArrayData(Platform platform, ReporterTypeEnum reporterType, ArrayData arrayData,
            ReporterList reporterList) {
        return reporterType == reporterList.getReporterType() && platform.equals(arrayData.getArray().getPlatform());
    }

    private Set<ArrayData> getArrayDatas(Set<ResultRow> rows) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (ResultRow row : rows) {
            if (row.getSampleAcquisition() != null) {
                Collection<ArrayData> candidateArrayDatas =
                    row.getSampleAcquisition().getSample().getArrayDataCollection();
                addMatchesFromArrayDatas(arrayDatas, candidateArrayDatas);
            }
        }
        return arrayDatas;
    }

    private Collection<AbstractReporter> getMatchingReporters(Collection<ArrayData> arrayDatas) {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(
                query.getCompoundCriterion(), query.getResultType());
        if (arrayDatas.isEmpty()) {
            return Collections.emptySet();
        } else if (criterionHandler.hasReporterCriterion() && !query.isAllGenomicDataQuery()) {
            return criterionHandler.getReporterMatches(dao, query.getSubscription().getStudy(),
                    query.getReporterType(), query.getGeneExpressionPlatform());
        } else {
            return getAllReporters(arrayDatas);
        }
    }

    private Collection<AbstractReporter> getAllReporters(Collection<ArrayData> arrayDatas) {
        HashSet<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (ArrayData arrayData : arrayDatas) {
            arrayData = dao.get(arrayData.getId(), ArrayData.class);
            reporters.addAll(arrayData.getReporters());
        }
        return reporters;
    }

    private Collection<SegmentData> getMatchingSegmentDatas(Collection<ArrayData> arrayDatas)
        throws InvalidCriterionException {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(
                query.getCompoundCriterion(), query.getResultType());
        if (arrayDatas.isEmpty()) {
            return Collections.emptySet();
        } else if (criterionHandler.hasSegmentDataCriterion() && !query.isAllGenomicDataQuery()) {
            return criterionHandler.getSegmentDataMatches(dao, query.getSubscription().getStudy(),
                    query.getCopyNumberPlatform());
        } else {
            return getAllSegmentDatas(arrayDatas);
        }
    }

    private Collection<SegmentData> getAllSegmentDatas(Collection<ArrayData> arrayDatas) {
        HashSet<SegmentData> segmentDatas = new HashSet<SegmentData>();
        for (ArrayData arrayData : arrayDatas) {
            arrayData = dao.get(arrayData.getId(), ArrayData.class);
            segmentDatas.addAll(arrayData.getSegmentDatas());
        }
        return segmentDatas;
    }

}
