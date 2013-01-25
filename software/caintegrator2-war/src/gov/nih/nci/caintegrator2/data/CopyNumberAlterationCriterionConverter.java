/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.genomic.GeneChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

/**
 * Handles creation of hibernate criteria objects given a CopyNumberAlterationCriterion.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See addSegmentValueCriterion()
public class CopyNumberAlterationCriterionConverter {
    
    private static final String LOCATION_START_ATTRIBUTE = "Location.startPosition";
    private static final String LOCATION_END_ATTRIBUTE = "Location.endPosition";
    private final CopyNumberAlterationCriterion copyNumberCriterion;
    
    CopyNumberAlterationCriterionConverter(CopyNumberAlterationCriterion copyNumberCriterion) {
        this.copyNumberCriterion = copyNumberCriterion;
    }
    
    Criteria retrieveSegmentDataCriteria(Study study, Platform platform, Session currentSession) {
        Criteria segmentDataCrit = currentSession.createCriteria(SegmentData.class);
        Criteria arrayDataCrit = segmentDataCrit.createCriteria("arrayData");
        arrayDataCrit.createCriteria("reporterLists").add(Restrictions.eq("platform", platform));
        arrayDataCrit.add(Restrictions.eq("study", study));
        addSegmentValueCriterion(segmentDataCrit);
        addGenomicIntervalTypeToCriteria(segmentDataCrit, currentSession, platform.getGenomeVersion());
        return segmentDataCrit;
    }
    
    @SuppressWarnings("PMD.CyclomaticComplexity") // There are 5 different cases of segment value criteria.
    private void addSegmentValueCriterion(Criteria segmentDataCrit) {
        // First case, if both are null.
        if (copyNumberCriterion.getUpperLimit() == null && copyNumberCriterion.getLowerLimit() == null) {
            return;
        }
        SimpleExpression upperLimitExpression = Restrictions.le("segmentValue", 
                copyNumberCriterion.getUpperLimit());
        SimpleExpression lowerLimitExpression = Restrictions.ge("segmentValue", 
                copyNumberCriterion.getLowerLimit());
        // Second case, upper limit is higher than lower limit, value is in between the two
        if (copyNumberCriterion.isInsideBoundaryType()) {
            segmentDataCrit.add(Restrictions.conjunction().add(upperLimitExpression).add(lowerLimitExpression));
            return;
         } 
        // Third case, lower limit is higher than upper limit, value is outside of the limits
        if (copyNumberCriterion.isOutsideBoundaryType()) {
            segmentDataCrit.add(Restrictions.disjunction().add(upperLimitExpression).add(lowerLimitExpression));
            return;
        }
        // Fourth case, upper limit has a value, lower limit is null.
        if (copyNumberCriterion.getUpperLimit() != null) {
            segmentDataCrit.add(upperLimitExpression);
            return;
        }
        // Fifth case, lower limit has a value, upper limit is null.
        if (copyNumberCriterion.getLowerLimit() != null) {
            segmentDataCrit.add(lowerLimitExpression);
        }
    }
    
    
    private void addGenomicIntervalTypeToCriteria(Criteria segmentDataCrit, Session currentSession, 
            GenomeBuildVersionEnum genomeBuildVersion) {
        switch (copyNumberCriterion.getGenomicIntervalType()) {
            case GENE_NAME:
            handleGeneNameCriteria(segmentDataCrit, currentSession, genomeBuildVersion);
                break;
            case CHROMOSOME_COORDINATES:
                addChromosomeCoordinatesToCriterion(copyNumberCriterion.getChromosomeCoordinateHigh(), 
                        copyNumberCriterion.getChromosomeCoordinateLow(), segmentDataCrit, 
                        Cai2Util.getInternalChromosomeNumber(copyNumberCriterion.getChromosomeNumber()));
                break;
            default:
                throw new IllegalStateException("Unknown genomic interval type");
            }
    }
    
    @SuppressWarnings("unchecked") // Hibernate operations are untyped    
    private void handleGeneNameCriteria(Criteria segmentDataCrit, Session currentSession, 
            GenomeBuildVersionEnum genomeBuildVersion) {
        if (StringUtils.isNotBlank(copyNumberCriterion.getGeneSymbol())) {
            List<GeneChromosomalLocation> geneLocations = 
                currentSession.createCriteria(GeneChromosomalLocation.class).add(
                Restrictions.in("geneSymbol", copyNumberCriterion.getGeneSymbols())).createCriteria(
                "geneLocationConfiguration").add(
                Restrictions.eq("genomeBuildVersion", genomeBuildVersion)).list();
            if (!geneLocations.isEmpty()) {
                addMultipleChromosomeCoordinatesToCriterion(segmentDataCrit, geneLocations);
            }
        }
    }
    
    private void addChromosomeCoordinatesToCriterion(Integer chromosomeCoordinateHigh, 
            Integer chromosomeCoordinateLow, Criteria segmentDataCrit, String chromosomeNumber) {
        if (chromosomeCoordinateHigh == null || chromosomeCoordinateLow == null) {
            segmentDataCrit.add(chromosomeNumberExpression(chromosomeNumber));
            if (chromosomeCoordinateHigh != null) {
                segmentDataCrit.add(segmentEndLessThanHigh(chromosomeCoordinateHigh));
            }
            if (chromosomeCoordinateLow != null) {
                segmentDataCrit.add(segmentStartGreaterThanLow(chromosomeCoordinateLow));
            }
        } else {
            Junction overallOrStatement = Restrictions.disjunction();
            // (loc.startPos <= lowerInput && loc.endPos >= lowerInput && loc.chromosome == chromosomeInput) 
            //  || (loc.startPos >= lowerInput  && loc.startPos <= higherInput && loc.chromosome == chromosomeInput) 
            overallOrStatement.add(Restrictions.conjunction().add(segmentStartLessThanLow(chromosomeCoordinateLow))
                    .add(segmentEndGreaterThanLow(chromosomeCoordinateLow)).add(
                            chromosomeNumberExpression(chromosomeNumber)));
            overallOrStatement.add(Restrictions.conjunction().add(segmentStartGreaterThanLow(chromosomeCoordinateLow))
                    .add(segmentStartLessThanHigh(chromosomeCoordinateHigh)).add(
                            chromosomeNumberExpression(chromosomeNumber)));
            segmentDataCrit.add(overallOrStatement);
        }
    }
    
    private void addMultipleChromosomeCoordinatesToCriterion(Criteria segmentDataCrit, 
            List<GeneChromosomalLocation> geneLocations) {
        Junction overallOrStatement = Restrictions.disjunction();
        // (loc.startPos <= lowerInput && loc.endPos >= lowerInput && loc.chromosome == chromosomeInput) 
        //  || (loc.startPos >= lowerInput  && loc.startPos <= higherInput && loc.chromosome == chromosomeInput) 
        for (GeneChromosomalLocation geneLocation : geneLocations) {
            Integer chromosomeCoordinateLow = geneLocation.getLocation().getStartPosition();
            Integer chromosomeCoordinateHigh = geneLocation.getLocation().getEndPosition();
            overallOrStatement.add(Restrictions.conjunction().add(segmentStartLessThanLow(chromosomeCoordinateLow))
                    .add(segmentEndGreaterThanLow(chromosomeCoordinateLow)).add(
                            chromosomeNumberExpression(Cai2Util.getInternalChromosomeNumber(
                                    geneLocation.getLocation().getChromosome()))));
            overallOrStatement.add(Restrictions.conjunction().add(segmentStartGreaterThanLow(chromosomeCoordinateLow))
                    .add(segmentStartLessThanHigh(chromosomeCoordinateHigh)).add(
                            chromosomeNumberExpression(Cai2Util.getInternalChromosomeNumber(
                                    geneLocation.getLocation().getChromosome()))));
        }
        segmentDataCrit.add(overallOrStatement);
        
    }
    private SimpleExpression segmentEndLessThanHigh(Integer chromosomeCoordinateHigh) {
        return Restrictions.le(LOCATION_END_ATTRIBUTE, chromosomeCoordinateHigh);
    }
    
    private SimpleExpression segmentStartGreaterThanLow(Integer chromosomeCoordinateLow) {
        return Restrictions.ge(LOCATION_START_ATTRIBUTE, chromosomeCoordinateLow);
    }
    
    private SimpleExpression segmentStartLessThanLow(Integer chromosomeCoordinateLow) {
        return Restrictions.le(LOCATION_START_ATTRIBUTE, chromosomeCoordinateLow);
    }
    
    private SimpleExpression segmentStartLessThanHigh(Integer chromosomeCoordinateHigh) {
        return Restrictions.le(LOCATION_START_ATTRIBUTE, chromosomeCoordinateHigh);
    }
    
    private SimpleExpression segmentEndGreaterThanLow(Integer chromosomeCoordinateLow) {
        return Restrictions.ge(LOCATION_END_ATTRIBUTE, chromosomeCoordinateLow);
    }
    
    private SimpleExpression chromosomeNumberExpression(String chromosomeNumber) {
        return Restrictions.eq("Location.chromosome", chromosomeNumber);
    }

}
