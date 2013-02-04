/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles CompoundCriterion objects.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
final class CompoundCriterionHandler extends AbstractCriterionHandler {

    private final Collection <AbstractCriterionHandler> handlers;
    private final CompoundCriterion compoundCriterion;
    private final ResultTypeEnum resultType;
    
    private CompoundCriterionHandler(Collection <AbstractCriterionHandler> handlers, 
                                     CompoundCriterion compoundCriterion,
                                     ResultTypeEnum resultType) {
        this.handlers = handlers;
        this.compoundCriterion = compoundCriterion;
        this.resultType = resultType;
    }
    

    /**
     * Creates the CompoundCriterionHandler based on the given CompoundCriterion.
     * @param compoundCriterion - compound criterion to create from.
     * @return CompoundCriterionHandler object returned, with the handlers collection filled.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // requires switch-like statement
    static CompoundCriterionHandler create(CompoundCriterion compoundCriterion, ResultTypeEnum resultType) {
        Collection<AbstractCriterionHandler> handlers = new HashSet<AbstractCriterionHandler>();
        if (compoundCriterion.getCriterionCollection() != null) {
            for (AbstractCriterion abstractCriterion : compoundCriterion.getCriterionCollection()) {
                if (abstractCriterion instanceof AbstractAnnotationCriterion) {
                    handlers.add(new AnnotationCriterionHandler((AbstractAnnotationCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof CompoundCriterion) {
                    handlers.add(CompoundCriterionHandler.create((CompoundCriterion) abstractCriterion, resultType));
                } else if (abstractCriterion instanceof GeneNameCriterion) {
                    handlers.add(GeneNameCriterionHandler.create((GeneNameCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof FoldChangeCriterion) {
                    handlers.add(FoldChangeCriterionHandler.create((FoldChangeCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof SubjectListCriterion) {
                    handlers.add(SubjectListCriterionHandler.create((SubjectListCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof CopyNumberAlterationCriterion) {
                   handlers.add(CopyNumberAlterationCriterionHandler.create((CopyNumberAlterationCriterion) 
                           abstractCriterion));
                } else if (abstractCriterion instanceof ExpressionLevelCriterion) {
                    handlers.add(ExpressionLevelCriterionHandler.create((ExpressionLevelCriterion) abstractCriterion));
                } else {
                    throw new IllegalStateException("Unknown AbstractCriterion class: " + abstractCriterion);
                }
            }
        }
        return new CompoundCriterionHandler(handlers, compoundCriterion, resultType);
    }
    
    static CompoundCriterionHandler createAllSampleAnnotation() {
        Collection<AbstractCriterionHandler> handlers = new HashSet<AbstractCriterionHandler>();
        AbstractAnnotationCriterion annotationCriterion = new AbstractAnnotationCriterion();
        annotationCriterion.setEntityType(EntityTypeEnum.SAMPLE);
        handlers.add(new AnnotationCriterionHandler(annotationCriterion));
        return new CompoundCriterionHandler(handlers, null, ResultTypeEnum.CLINICAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        if (compoundCriterion == null || compoundCriterion.getCriterionCollection().isEmpty()) {
            return getAllRows(query.getSubscription().getStudy(), entityTypes);
        } else {
            return getMatchingRows(dao, arrayDataService, entityTypes, query);
        }
    }
    
    private Set<ResultRow> getAllRows(Study study, Set<EntityTypeEnum> entityTypes) {
        ResultRowFactory rowFactory = new ResultRowFactory(entityTypes);
        if (entityTypes.contains(EntityTypeEnum.SUBJECT)) {
            return rowFactory.getSubjectRows(study.getAssignmentCollection());
        } else if (entityTypes.contains(EntityTypeEnum.IMAGESERIES)) {
            return rowFactory.getImageSeriesRows(getAllImageSeries(study));
        } else if (entityTypes.contains(EntityTypeEnum.SAMPLE)) {
            return rowFactory.getSampleRows(getAllSampleAcuisitions(study));
        } else {
            return Collections.emptySet();
        }
    }

    private Collection<SampleAcquisition> getAllSampleAcuisitions(Study study) {
        Set<SampleAcquisition> acquisitions = new HashSet<SampleAcquisition>();
        for (StudySubjectAssignment assignment : study.getAssignmentCollection()) {
            acquisitions.addAll(assignment.getSampleAcquisitionCollection());
        }
        return acquisitions;
    }

    private Collection<ImageSeries> getAllImageSeries(Study study) {
        Set<ImageSeries> imageSeriesSet = new HashSet<ImageSeries>();
        for (StudySubjectAssignment assignment : study.getAssignmentCollection()) {
            for (ImageSeriesAcquisition imageSeriesAcquisition : assignment.getImageStudyCollection()) {
                imageSeriesSet.addAll(imageSeriesAcquisition.getSeriesCollection());
            }
        }
        return imageSeriesSet;
    }

    private Set<ResultRow> getMatchingRows(CaIntegrator2Dao dao, ArrayDataService arrayDataService,
            Set<EntityTypeEnum> entityTypes, Query query) throws InvalidCriterionException {
        boolean rowsRetrieved = false;
        Set<ResultRow> allValidRows = new HashSet<ResultRow>();
        for (AbstractCriterionHandler handler : handlers) {
            Set<ResultRow> newRows = handler.getMatches(dao, arrayDataService, query, entityTypes);
            if (!rowsRetrieved) {
                allValidRows = newRows;
                rowsRetrieved = true;
            } else {
                allValidRows = combineResults(allValidRows, newRows, query.isMultiplePlatformQuery());
            }
            
        }
        return allValidRows;
    }
    
    /**
     * Combines the results of the rows.
     * @param currentValidRows - current rows that are valid.
     * @param newRows - new rows to validate.
     * @param defaultTimepoint - the default timepoint for the study.
     * @return - combination of rows.
     */
    private Set<ResultRow> combineResults(Set<ResultRow> currentValidRows, 
                                          Set<ResultRow> newRows, boolean isMultiplePlatformQuery) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        if (compoundCriterion.getBooleanOperator() != null) {
           switch(compoundCriterion.getBooleanOperator()) {
           case AND:
               combinedResults = combineResultsForAndOperator(currentValidRows, newRows, isMultiplePlatformQuery);
           break;
           case OR:
               combinedResults = combineResultsForOrOperator(currentValidRows, newRows, isMultiplePlatformQuery);
           break;
           default:
               // TODO : figure out what to actually do in this case?
               combinedResults.addAll(currentValidRows);
               combinedResults.addAll(newRows);
           break;
           }
           
        }
        return combinedResults;
    }

    
    private Set<ResultRow> combineResultsForAndOperator(Set<ResultRow> currentValidRows, 
                                   Set<ResultRow> newRows, boolean isMultiplePlatformQuery) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
           for (ResultRow row : newRows) {
               ResultRow rowFound = 
                   QueryUtil.resultRowSetContainsResultRow(currentValidRows, row, isMultiplePlatformQuery);
               if (rowFound != null) {
                   if (isMultiplePlatformQuery) {
                       combinedResults.add(checkRowsForAppropriateReporter(rowFound, row));
                   } else {
                       combinedResults.add(row);
                   }
               }
           }
           return combinedResults;
    }
    
    private ResultRow checkRowsForAppropriateReporter(ResultRow rowFound, ResultRow row) {
        if (rowFound.getSampleAcquisition() != null && rowFound.getSampleAcquisition().getSample() != null 
                && !rowFound.getSampleAcquisition().getSample().getArrayDataCollection().isEmpty()) {
            ReporterTypeEnum reporterType = 
                rowFound.getSampleAcquisition().getSample().getArrayDataCollection().
                    iterator().next().getReporterType();
            if (resultType.isReporterMatch(reporterType)) {
                return rowFound;
            }
        }
        return row;
    }


    private Set<ResultRow> combineResultsForOrOperator(Set<ResultRow> currentValidRows,
                                             Set<ResultRow> newRows, boolean isMultiplePlatformQuery) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        combinedResults.addAll(currentValidRows);
        for (ResultRow row : newRows) {
            ResultRow rowFound = 
                QueryUtil.resultRowSetContainsResultRow(currentValidRows, row, isMultiplePlatformQuery);
            if (rowFound == null) {
                combinedResults.add(row);
            }
            
        }
        return combinedResults;
    }

    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType, 
            Platform platform) {
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.isReporterMatchHandler()) {
                reporters.addAll(handler.getReporterMatches(dao, study, reporterType, platform));
            }
        }
        return reporters;
    }
    
    @Override
    Set<SegmentData> getSegmentDataMatches(CaIntegrator2Dao dao, Study study, Platform platform) 
        throws InvalidCriterionException {
        Set<SegmentData> segmentDatas = new HashSet<SegmentData>();
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasSegmentDataCriterion()) {
                segmentDatas.addAll(handler.getSegmentDataMatches(dao, study, platform));
            }
        }
        return segmentDatas;
    }

    @Override
    boolean isReporterMatchHandler() {
        return true;
    }


    @Override
    boolean hasReporterCriterion() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasReporterCriterion()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasCriterionSpecifiedReporterValues()) {
                return true;
            }
        }
        return false;
    }


    @Override
    GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value) {
        for (AbstractCriterionHandler handler : handlers) {
            GenomicCriteriaMatchTypeEnum matchType = handler.getGenomicValueMatchCriterionType(genes, value);
            if (!GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(matchType)) {
                return matchType;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }

    @Override
    boolean hasSegmentDataCriterion() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasSegmentDataCriterion()) {
                return true;
            }
        }
        return false;
    }


    @Override
    boolean hasCriterionSpecifiedSegmentValues() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasCriterionSpecifiedSegmentValues()) {
                return true;
            }
        }
        return false;
    }
    

    @Override
    boolean hasCriterionSpecifiedSegmentCallsValues() {
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.hasCriterionSpecifiedSegmentCallsValues()) {
                return true;
            }
        }
        return false;
    }    


    @Override
    GenomicCriteriaMatchTypeEnum getSegmentValueMatchCriterionType(Float value) {
        for (AbstractCriterionHandler handler : handlers) {
            GenomicCriteriaMatchTypeEnum matchType = handler.getSegmentValueMatchCriterionType(value);
            if (!GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(matchType)) {
                return matchType;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }


    /**
     * @param callsValue
     * @return
     */
    public GenomicCriteriaMatchTypeEnum getSegmentCallsValueMatchCriterionType(
            Integer callsValue) {
        for (AbstractCriterionHandler handler : handlers) {
            GenomicCriteriaMatchTypeEnum matchType = handler.getSegmentCallsValueMatchCriterionType(callsValue);
            if (!GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(matchType)) {
                return matchType;
            }
        }
        return GenomicCriteriaMatchTypeEnum.NO_MATCH;
    }
}
