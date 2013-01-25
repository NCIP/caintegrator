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
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler that returns samples matching the given fold change criterion.
 */
final class FoldChangeCriterionHandler extends AbstractCriterionHandler {

    private final FoldChangeCriterion criterion;

    private FoldChangeCriterionHandler(FoldChangeCriterion criterion) {
        this.criterion = criterion;
    }
    
    static FoldChangeCriterionHandler create(FoldChangeCriterion foldChangeCriterion) {
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
        configureCompareToSamples(study, criterion.getControlSampleSetName());
        Set<AbstractReporter> reporters = getReporterMatches(dao, study, reporterType);
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addReporters(reporters);
        request.addArrayDatas(getCandidateArrayDatas(study, reporterType));
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        ArrayDataValues values = arrayDataService.getFoldChangeValues(request, getCompareToArrayDatas(reporterType));
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
    
    boolean isGenomicValueMatchCriterion(Set<Gene> genes, Float value) {
        boolean reporterMatch = false;
        for (Gene gene : genes) {
            if (criterion.getGeneSymbol().toUpperCase().contains(gene.getSymbol().toUpperCase())) {
                reporterMatch = true;
                break;
            }
        }
        if (reporterMatch && isFoldChangeMatch(value)) {
            return true;
        }
        return false;
    }

    private boolean isFoldsDownMatch(Float foldChangeValue) {
        return foldChangeValue <= -criterion.getFoldsDown();
    }

    private boolean isFoldsUpMatch(Float foldChangeValue) {
        return foldChangeValue >= criterion.getFoldsUp();
    }

    private Collection<ArrayData> getCandidateArrayDatas(Study study, ReporterTypeEnum reporterType) {
        Set<ArrayData> candidateDatas = new HashSet<ArrayData>();
        candidateDatas.addAll(study.getArrayDatas(reporterType));
        candidateDatas.removeAll(getCompareToArrayDatas(reporterType));
        return candidateDatas;
    }

    private Collection<ArrayData> getCompareToArrayDatas(ReporterTypeEnum reporterType) {
        Set<ArrayData> compareToDatas = new HashSet<ArrayData>();
        for (Sample sample : criterion.getCompareToSampleSet().getSamples()) {
            compareToDatas.addAll(sample.getArrayDatas(reporterType));
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
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType) {
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        Set<String> geneSymbols = new HashSet<String>();
        geneSymbols.addAll(Arrays.asList(criterion.getGeneSymbol().replaceAll("\\s*", "").split(",")));
        reporters.addAll(dao.findReportersForGenes(geneSymbols, reporterType, study));
        return reporters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasEntityCriterion() {
        return true;
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
    boolean isEntityMatchHandler() {
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


}
