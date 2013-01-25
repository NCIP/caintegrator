/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // Complex case statement to determine the match type.
public final class CopyNumberAlterationCriterionHandler extends AbstractCriterionHandler {

    private static final int SEGMENT_BUFFER_SIZE = 500;
    private final CopyNumberAlterationCriterion criterion;

    private CopyNumberAlterationCriterionHandler(CopyNumberAlterationCriterion criterion) {
        this.criterion = criterion;
    }
    
    /**
     * 
     * @param criterion the copy number alteration criterion 
     * @return the copy number alteration criterion handler
     */
    public static CopyNumberAlterationCriterionHandler create(CopyNumberAlterationCriterion criterion) {
        return new CopyNumberAlterationCriterionHandler(criterion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query,
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        Study study = query.getSubscription().getStudy();
        Platform platform = query.getCopyNumberPlatform();
        List<SegmentData> segmentDatas = dao.findMatchingSegmentDatas(criterion, study, platform);
        
        return getRows(segmentDatas, entityTypes);
    }
    
    private Set<ResultRow> getRows(List<SegmentData> segmentDatas, Set<EntityTypeEnum> entityTypes) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        Set<SampleAcquisition> sampleAcquisitions = new HashSet<SampleAcquisition>();
        for (SegmentData segmentData : segmentDatas) {
            if (segmentData.getArrayData().getSample().getSampleAcquisition() != null) {
                sampleAcquisitions.add(segmentData.getArrayData().getSample().getSampleAcquisition());
            }
        }
        return rowFactory.getSampleRows(sampleAcquisitions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType,
            Platform platform) {
        return Collections.emptySet();
    }
    
    Set<SegmentData> getSegmentDataMatches(CaIntegrator2Dao dao, Study study, Platform platform) 
        throws InvalidCriterionException {
        List<SegmentData> segmentDataMatchesFromDao = dao.findMatchingSegmentDatas(criterion, study, platform);
        List<List<SegmentData>> segmentDataMatchesList = new ArrayList<List<SegmentData>>();
        int startPos = 0;
        int endPos = SEGMENT_BUFFER_SIZE;
        int totalSize = segmentDataMatchesFromDao.size();
        while (totalSize > startPos) {
            int positionToEnd = endPos >= totalSize ? totalSize : endPos;
            segmentDataMatchesList.add(segmentDataMatchesFromDao.subList(startPos, positionToEnd));
            startPos = positionToEnd + 1;
            endPos += SEGMENT_BUFFER_SIZE;
        }
        HashSet<SegmentData> segmentDataMatches = new HashSet<SegmentData>();
        for (List<SegmentData> segments : segmentDataMatchesList) {
            segmentDataMatches.addAll(dao.findMatchingSegmentDatasByLocation(segments, study, platform));
        }
        return segmentDataMatches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasReporterCriterion() {
        return false;
    }
    
    boolean hasSegmentDataCriterion() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // Complex case statement to determine the match type.
    @Override
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }
    
    GenomicCriteriaMatchTypeEnum getSegmentValueMatchCriterionType(Float value) {
        boolean upperLimitValid = true;
        boolean lowerLimitValid = true;
        if (criterion.getUpperLimit() != null) {
            upperLimitValid = value <= criterion.getUpperLimit();
        }
        if (criterion.getLowerLimit() != null) {
            lowerLimitValid = value >= criterion.getLowerLimit();
        }
        if (criterion.isOutsideBoundaryType()) { // For outside boundary type
            if (upperLimitValid || lowerLimitValid) { // if it is upper limit valid or lower limit valid
                return GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE;
            }
        } else { // Inside boundary type
            if (upperLimitValid && lowerLimitValid) { // both upper limit and lower limit must be valid.
                return GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }
    
    boolean hasCriterionSpecifiedSegmentValues() {
        return true;
    }
    
    @Override
    boolean hasCriterionSpecifiedSegmentCallsValues() {
        if (criterion.getCopyNumberCriterionType().equals(CopyNumberCriterionTypeEnum.CALLS_VALUE)) {
            return true;
        }
        return false;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isReporterMatchHandler() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    GenomicCriteriaMatchTypeEnum getSegmentCallsValueMatchCriterionType(
            Integer callsValue) {
        if (criterion.getCallsValues().contains(callsValue)) {
            return GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE;
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }
    
}
