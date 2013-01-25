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
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
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
class SubjectListCriterionHandler extends AbstractCriterionHandler {

    private final SubjectListCriterion subjectListCriterion;
    
    /**
     * @param subjectListCriterion criterion object to use.
     */
    SubjectListCriterionHandler(SubjectListCriterion subjectListCriterion) {
        this.subjectListCriterion = subjectListCriterion;
    }
    
    static SubjectListCriterionHandler create(SubjectListCriterion subjectListCriterion) {
        return new SubjectListCriterionHandler(subjectListCriterion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        if (subjectListCriterion.getSubjectListCollection().isEmpty()) {
            throw new InvalidCriterionException(
                    "Must supply at least one SubjectList to use a SubjectListCriterion in a query.");
        }
        Study study = query.getSubscription().getStudy();
        Set<ResultRow> resultRows = new HashSet<ResultRow>();
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        resultRows.addAll(rowFactory.getSubjectRows(dao.findMatchingSubjects(subjectListCriterion, study)));
        return resultRows;
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
