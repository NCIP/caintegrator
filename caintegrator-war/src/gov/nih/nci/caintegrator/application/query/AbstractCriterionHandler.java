/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.Set;

/**
 * Abstract Criterion Handler Class.
 */
abstract class AbstractCriterionHandler {
    
    /**
     * Gets all matches from the DAO.
     * @param dao object to use.
     * @param arrayDataService TODO
     * @param query TODO
     * @param query the query the criterion belongs to
     * @param entityTypes entityTypes that are in this criterion to use.
     * @return set of ResultRows that match..
     * @throws InvalidCriterionException if a criterion is not valid.
     */
    abstract Set<ResultRow> getMatches(CaIntegrator2Dao dao, ArrayDataService arrayDataService, Query query, 
            Set<EntityTypeEnum> entityTypes) throws InvalidCriterionException;
    
    /**
     * Gets all <code>AbstractReporters</code> matched by this handler.
     * 
     * @param dao DAO to use
     * @param study being queried.
     * @param reporterType reporter type to use.
     * @param platform the platform to use.
     * @return the matching reporters.
     */
    abstract Set<AbstractReporter> getReporterMatches(CaIntegrator2Dao dao, 
                                                      Study study, 
                                                      ReporterTypeEnum reporterType,
                                                      Platform platform);

    /**
     * Gets all <code>SegmentData</code> matched by this handler.
     * 
     * @param dao DAO to use
     * @param study being queries.
     * @param platform the platform to use.
     * @return the matching segment datas.
     */
    abstract Set<SegmentData> getSegmentDataMatches(CaIntegrator2Dao dao, Study study, Platform platform)
        throws InvalidCriterionException;
    
    /**
     * Determines whether this handler provieds segment data matches.
     * @return true if can match segment datas.
     */
    abstract boolean hasSegmentDataCriterion();
    
    /**
     * Determines whether this handler provides reporter matches.
     * 
     * @return true if can match reporters.
     */
    abstract boolean isReporterMatchHandler();

    abstract boolean hasReporterCriterion();

    /**
     * Determines if the handler has reporter values which are narrowed down by the criterion.
     * @return true if has criterion specified reporter values (fold change criterion for example).
     */
    abstract boolean hasCriterionSpecifiedReporterValues();
    
    /**
     * Determines if the handler has segment values which are narrowed down by the criterion.
     * @return true if has criterion specified criterion values (copy number alteration criterion, for example).
     */
    abstract boolean hasCriterionSpecifiedSegmentValues();
    
    /**
     * Determines if the handler has segment Calls values which are narrowed down by the criterion.
     * @return true if has criterion specified values (copy number alteration criterion, for example).
     */
    abstract boolean hasCriterionSpecifiedSegmentCallsValues();    
    
    /**
     * Determines if the value matches the criterion specified for the genes, 
     * at the current time only foldChangeCriterion would use this, but if we ever do 
     * copy number data or other genomic criterion, that would as well.
     * @param genes the genes associated with a given reporter to check the value against.
     * @param value is the value to match criterion against.
     * @return true if the given value matches the criterion.
     */
    abstract GenomicCriteriaMatchTypeEnum getGenomicValueMatchCriterionType(Set<Gene> genes, Float value);
    
    /**
     * Determines if the value matches the criterion.
     * @param value is the value to match the criterion against.
     * @return true if the given value matches the criterion.
     */
    abstract GenomicCriteriaMatchTypeEnum getSegmentValueMatchCriterionType(Float value);

    /**
     * Determines if the Calls value matches the criterion.
     * @param callsValue
     * @return true if the given value matches the criterion.
     */
    abstract GenomicCriteriaMatchTypeEnum getSegmentCallsValueMatchCriterionType(Integer callsValue);

}
