/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Runs queries that return <code>GenomicDataQueryResults</code>.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See addMatchesFromArrayDatas
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
        ArrayDataValues values = getDataValues();
        return createResult(values);
    }

    private GenomicDataQueryResult createResult(ArrayDataValues values) {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        createResultRows(result, values);
        Map<AbstractReporter, GenomicDataResultRow> reporterToRowMap = createReporterToRowMap(result);
        for (ArrayData arrayData : values.getArrayDatas()) {
            addToResult(values, result, reporterToRowMap, arrayData);
        }
        result.setQuery(query);
        return result;
    }

    private void addToResult(ArrayDataValues values, GenomicDataQueryResult result,
        Map<AbstractReporter, GenomicDataResultRow> reporterToRowMap, ArrayData arrayData) {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(query.getCompoundCriterion());
        result.setHasCriterionSpecifiedReporterValues(criterionHandler.hasCriterionSpecifiedReporterValues());
        GenomicDataResultColumn column = result.addColumn();
        column.setSampleAcquisition(arrayData.getSample().getSampleAcquisition());
        for (AbstractReporter reporter : values.getReporters()) {
            HibernateUtil.loadCollection(reporter.getGenes());
            HibernateUtil.loadCollection(reporter.getSamplesHighVariance());
            GenomicDataResultRow row = reporterToRowMap.get(reporter);
            GenomicDataResultValue value = new GenomicDataResultValue();
            value.setColumn(column);
            Float floatValue = values.getFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL);
            if (floatValue != null) {
                value.setValue(Math.round(floatValue * DECIMAL_100) / DECIMAL_100);
                checkMeetsCriterion(result, criterionHandler, reporter, row, value);
                checkHighVariance(result, arrayData, reporter, value);
            }
            row.getValues().add(value);
        }
    }

    private void checkMeetsCriterion(GenomicDataQueryResult result, CompoundCriterionHandler criterionHandler,
            AbstractReporter reporter, GenomicDataResultRow row, GenomicDataResultValue value) {
        if (result.isHasCriterionSpecifiedReporterValues()) {
            value.setMeetsCriterion(criterionHandler.
                    isGenomicValueMatchCriterion(reporter.getGenes(), value.getValue()));
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
    private Map<AbstractReporter, GenomicDataResultRow> createReporterToRowMap(GenomicDataQueryResult result) {
        Map<AbstractReporter, GenomicDataResultRow> rowMap = new HashMap<AbstractReporter, GenomicDataResultRow>();
        for (GenomicDataResultRow row : result.getRowCollection()) {
            rowMap.put(row.getReporter(), row);
        }
        return rowMap;
    }

    private void createResultRows(GenomicDataQueryResult result, ArrayDataValues values) {
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
        if (QueryUtil.isFoldChangeQuery(query)) {
            return arrayDataService.getFoldChangeValues(request, query);
        } else {
            return arrayDataService.getData(request);
        }
    }
    
    private Collection<ArrayData> getMatchingArrayDatas() throws InvalidCriterionException {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(query.getCompoundCriterion());
        if (criterionHandler.hasEntityCriterion()) {
            Set<EntityTypeEnum> samplesOnly = new HashSet<EntityTypeEnum>();
            samplesOnly.add(EntityTypeEnum.SAMPLE);
            Set<ResultRow> rows = criterionHandler.getMatches(dao, 
                    arrayDataService, query, samplesOnly);
            return getArrayDatas(rows, query.getReporterType());
        } else {
            return getAllArrayDatas(query.getReporterType());
        }
    }

    private Set<ArrayData> getAllArrayDatas(ReporterTypeEnum reporterType) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (StudySubjectAssignment assignment : query.getSubscription().getStudy().getAssignmentCollection()) {
            for (SampleAcquisition acquisition : assignment.getSampleAcquisitionCollection()) {
                addMatchesFromArrayDatas(arrayDatas, acquisition.getSample().getArrayDataCollection(), reporterType);
            }
        }
        return arrayDatas;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private void addMatchesFromArrayDatas(Set<ArrayData> matchingArrayDatas, 
            Collection<ArrayData> candidateArrayDatas,
            ReporterTypeEnum reporterType) {
        Platform platform = query.getPlatform();
        for (ArrayData arrayData : candidateArrayDatas) {
            for (ReporterList reporterList : arrayData.getReporterLists()) {
                if (reporterType.equals(reporterList.getReporterType())
                        && (platform.equals(arrayData.getArray().getPlatform()))) {
                    matchingArrayDatas.add(arrayData);
                }
            }
        }
    }

    private Set<ArrayData> getArrayDatas(Set<ResultRow> rows, ReporterTypeEnum reporterType) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (ResultRow row : rows) {
            if (row.getSampleAcquisition() != null) {
                Collection<ArrayData> candidateArrayDatas = 
                    row.getSampleAcquisition().getSample().getArrayDataCollection();
                addMatchesFromArrayDatas(arrayDatas, candidateArrayDatas, reporterType);
            }
        }
        return arrayDatas;
    }

    private Collection<AbstractReporter> getMatchingReporters(Collection<ArrayData> arrayDatas) {
        CompoundCriterionHandler criterionHandler = CompoundCriterionHandler.create(query.getCompoundCriterion());
        if (arrayDatas.isEmpty()) {
            return Collections.emptySet();
        } else if (criterionHandler.hasReporterCriterion()) {
            return criterionHandler.getReporterMatches(dao, query.getSubscription().getStudy(), 
                    query.getReporterType(), query.getPlatform());
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

}
