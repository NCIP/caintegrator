/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
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
    
    private CompoundCriterionHandler(Collection <AbstractCriterionHandler> handlers, 
                                     CompoundCriterion compoundCriterion) {
        this.handlers = handlers;
        this.compoundCriterion = compoundCriterion;
    }
    

    /**
     * Creates the CompoundCriterionHandler based on the given CompoundCriterion.
     * @param compoundCriterion - compound criterion to create from.
     * @return CompoundCriterionHandler object returned, with the handlers collection filled.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // requires switch-like statement
    static CompoundCriterionHandler create(CompoundCriterion compoundCriterion) {
        Collection<AbstractCriterionHandler> handlers = new HashSet<AbstractCriterionHandler>();
        if (compoundCriterion.getCriterionCollection() != null) {
            for (AbstractCriterion abstractCriterion : compoundCriterion.getCriterionCollection()) {
                if (abstractCriterion instanceof AbstractAnnotationCriterion) {
                    handlers.add(new AnnotationCriterionHandler((AbstractAnnotationCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof CompoundCriterion) {
                    handlers.add(CompoundCriterionHandler.create((CompoundCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof GeneNameCriterion) {
                    handlers.add(GeneNameCriterionHandler.create((GeneNameCriterion) abstractCriterion));
                } else if (abstractCriterion instanceof FoldChangeCriterion) {
                    handlers.add(FoldChangeCriterionHandler.create((FoldChangeCriterion) abstractCriterion));
                } else {
                    throw new IllegalStateException("Unknown AbstractCriterion class: " + abstractCriterion);
                }
            }
        }
        return new CompoundCriterionHandler(handlers, compoundCriterion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException {
        Study study = query.getSubscription().getStudy();
        if (!hasEntityCriterion()) {
            return getAllRows(study, entityTypes);
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
            Set<EntityTypeEnum> entityTypes, 
            Query query) throws InvalidCriterionException {
        boolean rowsRetrieved = false;
        Set<ResultRow> allValidRows = new HashSet<ResultRow>();
        for (AbstractCriterionHandler handler : handlers) {
            if (!handler.isEntityMatchHandler()) {
                continue;
            }
            Set<ResultRow> newRows = handler.getMatches(dao, arrayDataService, query, entityTypes);
            if (!rowsRetrieved) {
                allValidRows = newRows;
                rowsRetrieved = true;
            } else {
                allValidRows = combineResults(allValidRows, newRows);
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
                                          Set<ResultRow> newRows) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        if (compoundCriterion.getBooleanOperator() != null) {
           switch(compoundCriterion.getBooleanOperator()) {
           case AND:
               combinedResults = combineResultsForAndOperator(currentValidRows, newRows);
           break;
           case OR:
               combinedResults = combineResultsForOrOperator(currentValidRows, newRows);
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
                                   Set<ResultRow> newRows) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
           for (ResultRow row : newRows) {
               if (Cai2Util.resultRowSetContainsResultRow(currentValidRows, row)) {
                   combinedResults.add(row);
               }
           }
           return combinedResults;
    }
    
    private Set<ResultRow> combineResultsForOrOperator(Set<ResultRow> currentValidRows,
                                             Set<ResultRow> newRows) {
        Set<ResultRow> combinedResults = new HashSet<ResultRow>();
        combinedResults.addAll(currentValidRows);
        for (ResultRow row : newRows) {
            if (!Cai2Util.resultRowSetContainsResultRow(combinedResults, row)) {
                combinedResults.add(row);
            }
            
        }
        return combinedResults;
    }

    @Override
    Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, Study study, ReporterTypeEnum reporterType) {
        Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
        for (AbstractCriterionHandler handler : handlers) {
            if (handler.isReporterMatchHandler()) {
                reporters.addAll(handler.getReporterMatches(dao, study, reporterType));
            }
        }
        return reporters;
    }

    @Override
    boolean isEntityMatchHandler() {
        return true;
    }

    @Override
    boolean isReporterMatchHandler() {
        return true;
    }

    @Override
    boolean hasEntityCriterion() {
        boolean hasEntityCriterion = false;
        for (AbstractCriterionHandler handler : handlers) {
            hasEntityCriterion |= handler.hasEntityCriterion();
        }
        return hasEntityCriterion;
    }


    @Override
    boolean hasReporterCriterion() {
        boolean hasReporterCriterion = false;
        for (AbstractCriterionHandler handler : handlers) {
            hasReporterCriterion |= handler.hasReporterCriterion();
        }
        return hasReporterCriterion;
    }
    
    @Override
    boolean hasCriterionSpecifiedReporterValues() {
        boolean hasCriterionSpecifiedReporterValues = false;
        for (AbstractCriterionHandler handler : handlers) {
            hasCriterionSpecifiedReporterValues |= handler.hasCriterionSpecifiedReporterValues();
        }
        return hasCriterionSpecifiedReporterValues;
    }


    @Override
    boolean isGenomicValueMatchCriterion(Set<Gene> genes, Float value) {
        boolean isReporterValueMatchCriterion = false;
        for (AbstractCriterionHandler handler : handlers) {
            isReporterValueMatchCriterion |= handler.isGenomicValueMatchCriterion(genes, value);
        }
        return isReporterValueMatchCriterion;
    }
}
