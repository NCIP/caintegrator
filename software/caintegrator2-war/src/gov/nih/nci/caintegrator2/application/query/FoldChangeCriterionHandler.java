/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Handler that returns samples matching the given fold change criterion.
 */
public final class FoldChangeCriterionHandler extends AbstractCriterionHandler {

    private final FoldChangeCriterion criterion;

    private FoldChangeCriterionHandler(FoldChangeCriterion criterion) {
        this.criterion = criterion;
    }
    
    /**
     * 
     * @param foldChangeCriterion the fold change criterion 
     * @return the fold change criterion handler
     */
    public static FoldChangeCriterionHandler create(FoldChangeCriterion foldChangeCriterion) {
        return new FoldChangeCriterionHandler(foldChangeCriterion);
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
        configureCompareToSamples(study, criterion.getControlSampleSetName());
        Set<ArrayData> controlArrayData = getCompareToArrayDatas(reporterType, platform);
        Set<AbstractReporter> reporters = getReporterMatches(dao, study, reporterType, platform);
        
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addReporters(reporters);
        request.addArrayDatas(getCandidateArrayDatas(study, controlArrayData, reporterType, platform));
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);        

        DataRetrievalRequest controlDataRequest = new DataRetrievalRequest();
        controlDataRequest.addReporters(getReporterMatches(dao, study, reporterType, platform));
        controlDataRequest.addArrayDatas(controlArrayData);
        controlDataRequest.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        ArrayDataValues controlValues = arrayDataService.getData(controlDataRequest);
        List<ArrayDataValues> controlArrayDataList = new ArrayList<ArrayDataValues>();
        controlArrayDataList.add(controlValues);
        
        ArrayDataValues values = arrayDataService.getFoldChangeValues(request, controlArrayDataList,
                platform.getPlatformConfiguration().getPlatformChannelType());
        return getRows(values, entityTypes);
    }

    private Set<ResultRow> getRows(ArrayDataValues values, Set<EntityTypeEnum> entityTypes) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        Set<SampleAcquisition> sampleAcquisitions = new HashSet<SampleAcquisition>();
        for (ArrayData arrayData : values.getArrayDatas()) {
            if (hasFoldChangeMatch(arrayData, values)) {
                sampleAcquisitions.add(arrayData.getSample().getSampleAcquisition());
            }
        }
        return rowFactory.getSampleRows(sampleAcquisitions);
    }

    private boolean hasFoldChangeMatch(ArrayData arrayData, ArrayDataValues values) {
        for (AbstractReporter reporter : values.getReporters()) {
            Float foldChangeValue = values.getFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL);
            if (isFoldChangeMatch(foldChangeValue)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFoldChangeMatch(Float foldChangeValue) {
        switch (criterion.getRegulationType()) {
            case UP:
                return isFoldsUpMatch(foldChangeValue);
            case DOWN:
                return isFoldsDownMatch(foldChangeValue);
            case UP_OR_DOWN:
                return isFoldsUpMatch(foldChangeValue) || isFoldsDownMatch(foldChangeValue);
            case UNCHANGED:
                return !(isFoldsUpMatch(foldChangeValue) || isFoldsDownMatch(foldChangeValue));
            default:
                throw new IllegalStateException("Illegal regulation type: " + criterion.getRegulationType());
        }
    }
    
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        if (isReporterMatch(genes) && isFoldChangeMatch(value)) {
            return GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE;
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

    private boolean isFoldsDownMatch(Float foldChangeValue) {
        return foldChangeValue <= -criterion.getFoldsDown();
    }

    private boolean isFoldsUpMatch(Float foldChangeValue) {
        return foldChangeValue >= criterion.getFoldsUp();
    }

    private Collection<ArrayData> getCandidateArrayDatas(Study study, Set<ArrayData> controlArrayData,
            ReporterTypeEnum reporterType
            , Platform platform) {
        Set<ArrayData> candidateDatas = new HashSet<ArrayData>();
        candidateDatas.addAll(study.getArrayDatas(reporterType, platform));
        candidateDatas.removeAll(controlArrayData);
        return candidateDatas;
    }

    /**
     * Get control array data.
     * @param reporterType the reporter type
     * @param platform the platform
     * @return control array data
     */
    public Set<ArrayData> getCompareToArrayDatas(ReporterTypeEnum reporterType, Platform platform) {
        Set<ArrayData> compareToDatas = new HashSet<ArrayData>();
        for (Sample sample : criterion.getCompareToSampleSet().getSamples()) {
            compareToDatas.addAll(sample.getArrayDatas(reporterType, platform));
        }
        return compareToDatas;
    }

    private void configureCompareToSamples(Study study, String controlSampleSetName) throws InvalidCriterionException {
        if (study.getControlSampleSet(controlSampleSetName) == null
                || study.getControlSampleSet(controlSampleSetName).getSamples().isEmpty()) {
            throw new InvalidCriterionException(
                    "FoldChangeCriterion is invalid because there are no control samples for study '"
                            + study.getShortTitleText() + "'");
        }
        criterion.setCompareToSampleSet(study.getControlSampleSet(controlSampleSetName));
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
