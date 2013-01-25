/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler that matches genes based on the Symbol name.
 */
final class GeneNameCriterionHandler extends AbstractCriterionHandler {

    private final GeneNameCriterion criterion;

    private GeneNameCriterionHandler(GeneNameCriterion criterion) {
        this.criterion = criterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) {
        Study study = query.getSubscription().getStudy();
        ReporterTypeEnum reporterType = query.getReporterType();
        Set<AbstractReporter> reporters = 
            getReporterMatches(dao, study, reporterType, query.getGeneExpressionPlatform());
        Set<SampleAcquisition> sampleAcquisitions = new HashSet<SampleAcquisition>();
        for (AbstractReporter reporter : reporters) {
            for (ArrayData arrayData : reporter.getReporterList().getArrayDatas()) {
                Sample sample = arrayData.getSample();
                if (study.equals(arrayData.getStudy()) && sample.getSampleAcquisition() != null) {
                    sampleAcquisitions.add(sample.getSampleAcquisition());
                }
            }
        }
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        return rowFactory.getSampleRows(sampleAcquisitions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType, 
            Platform platform) {
        if (reporterType == null) {
            throw new IllegalArgumentException("ReporterType is not set.");
        }
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        reporters.addAll(dao.findReportersForGenes(criterion.getGeneSymbols(), reporterType, study, platform));
        return reporters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isReporterMatchHandler() {
        return true;
    }

    public static GeneNameCriterionHandler create(GeneNameCriterion criterion) {
        return new GeneNameCriterionHandler(criterion);
    }

    @Override
    boolean hasReporterCriterion() {
        return true;
    }

    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        return false;
    }

    @Override
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        for (Gene gene : genes) {
            if (criterion.getGeneSymbol().contains(gene.getSymbol())) {
                return GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE;
            }
        }
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
