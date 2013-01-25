/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * This is a static utility class used by different caIntegrator2 query objects. 
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public final class QueryUtil {    
    
    private QueryUtil() { }
    
    /**
     * Used to see if a Set of ResultRow's contains a specific ResultRow (based on SubjectAssignments matching).
     * This needs to be tweaked, not sure if the algorithm is correct.
     * @param rowSet - set of rows.
     * @param rowToTest - ResultRow item to test if it exists in set.
     * @return true/false value.
     */
    public static boolean resultRowSetContainsResultRow(Set<ResultRow> rowSet, 
                                                        ResultRow rowToTest) {
        for (ResultRow curRow : rowSet) {
            if (curRow.getSubjectAssignment() == rowToTest.getSubjectAssignment()
                    && curRow.getImageSeries() == rowToTest.getImageSeries()
                    && curRow.getSampleAcquisition() == rowToTest.getSampleAcquisition()) {
                    return true;
                }
            }
        return false;
    }

    /**
     * This function checks to see if an AnnotationValue belongs to a PermissibleValue.
     * @param value object to check to see if it belongs to permissible value.
     * @param permissibleValue object that the value uses to validate against.
     * @return true or false value.
     */
    public static boolean annotationValueBelongToPermissibleValue(AbstractAnnotationValue value, 
                                                                  PermissibleValue permissibleValue) {
        if (value instanceof StringAnnotationValue) {
            return handleStringValues(value, permissibleValue);
        } else if (value instanceof NumericAnnotationValue) {
            return handleNumericValues(value, permissibleValue);
        } else if (value instanceof DateAnnotationValue) {
            return handleDateValues(value, permissibleValue);
        }
        return false; 
    }
    
    private static boolean handleStringValues(AbstractAnnotationValue value, 
                                              PermissibleValue permissibleValue) {
        StringAnnotationValue stringValue = (StringAnnotationValue) value;
        if (stringValue.getStringValue() != null
            && stringValue.getStringValue().equalsIgnoreCase(permissibleValue.getValue())) {
            return true;
        }
        return false;
    }
    
    private static boolean handleNumericValues(AbstractAnnotationValue value, 
                                               PermissibleValue permissibleValue) {
        if (!NumberUtils.isNumber(permissibleValue.getValue())) {
            throw new IllegalArgumentException("value is of type Numeric, but permissibleValue is not.");    
        }
        NumericAnnotationValue numericValue = (NumericAnnotationValue) value;
        if (numericValue.getNumericValue() != null 
             && numericValue.getNumericValue().equals(Double.valueOf(permissibleValue.getValue()))) {
            return true;
        }
        return false;
    }

    private static boolean handleDateValues(AbstractAnnotationValue value, PermissibleValue permissibleValue) {
        DateAnnotationValue dateValue = (DateAnnotationValue) value;
        if (dateValue.getDateValue() != null && permissibleValue.getValue() != null 
            && permissibleValue.getValue().equals(DateUtil.toString(dateValue.getDateValue()))) {
            return true;
        }
        return false;
    }
    
    /**
     * Determines if a query is genomic (genomic results type or if it has genomic criterion).
     * @param query to check to see if it is genomic.
     * @return T/F if it is genomic type or not.
     */
    public static boolean isQueryGenomic(Query query) {
        return (ResultTypeEnum.GENOMIC.equals(query.getResultType()) 
                || QueryUtil.isCompoundCriterionGenomic(query.getCompoundCriterion())) ? true : false;
    }

    /**
     * Recursive function that goes through all criterion in a CompoundCriterion to determine
     * if any of them are genomic based criterion.
     * @param criterion input for the recursive function.
     * @return T/F value.
     */
    public static boolean isCompoundCriterionGenomic(CompoundCriterion criterion) {
        for (AbstractCriterion abstractCriterion : criterion.getCriterionCollection()) {
            if (abstractCriterion instanceof CompoundCriterion) {
                if (isCompoundCriterionGenomic((CompoundCriterion) abstractCriterion)) {
                    return true;
                }
            } else if (abstractCriterion instanceof GeneNameCriterion) {
                return true;
            } else if (abstractCriterion instanceof FoldChangeCriterion) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Determines if a query has fold change criterion.
     * @param query check to see if this query has fold change criterion.
     * @return T/F value.
     */
    public static boolean isFoldChangeQuery(Query query) {
        return !getFoldChangeCriterion(query).isEmpty();
    }

    /**
     * Retrieves a list of fold change criterion for a given query.
     * @param query to retrieve fold change criterion for.
     * @return the fold change criterion.
     */
    public static List<FoldChangeCriterion> getFoldChangeCriterion(Query query) {
        return getFoldChangeCriterionFromCompoundCriterion(query.getCompoundCriterion());
    }

    private static List<FoldChangeCriterion> getFoldChangeCriterionFromCompoundCriterion(
            CompoundCriterion compoundCriterion) {
        List<FoldChangeCriterion> foldChangeCriterionResults = new ArrayList<FoldChangeCriterion>();
        for (AbstractCriterion criterion : compoundCriterion.getCriterionCollection()) {
            getFoldChangeCriterion(criterion, foldChangeCriterionResults);
        }
        return foldChangeCriterionResults;
    }

    private static void getFoldChangeCriterion(AbstractCriterion criterion,
            List<FoldChangeCriterion> foldChangeCriterionResults) {
        if (criterion instanceof FoldChangeCriterion) {
            foldChangeCriterionResults.add((FoldChangeCriterion) criterion);
        } else if (criterion instanceof CompoundCriterion) {
            CompoundCriterion compoundCriterion = (CompoundCriterion) criterion;
            foldChangeCriterionResults.addAll(getFoldChangeCriterionFromCompoundCriterion(compoundCriterion));
        }
    }
    
    /**
     * Creates a query for all genomic data in the study, and limits it to the given clinical queries (if any
     * are given).
     * @param studySubscription for querying.
     * @param clinicalQueries to limit genomic data returned based.
     * @param platformName if this is not null, specifies the platform to use for the genomic query.
     * @return a Query which contains all genomic data for all clinical queries given (if any).
     */
    public static Query createAllGenomicDataQuery(StudySubscription studySubscription, Set<Query> clinicalQueries,
            String platformName) {
        Query query = createQuery(studySubscription);
        query.setResultType(ResultTypeEnum.GENOMIC);
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        if (clinicalQueries != null && !clinicalQueries.isEmpty()) {
            CompoundCriterion clinicalCompoundCriterions = new CompoundCriterion();
            clinicalCompoundCriterions.setBooleanOperator(BooleanOperatorEnum.OR);
            for (Query clinicalQuery : clinicalQueries) {
                CompoundCriterion clinicalCompoundCriterion = clinicalQuery.getCompoundCriterion();
                if (isCompoundCriterionGenomic(clinicalCompoundCriterion)) {
                    throw new IllegalArgumentException("Clinical query has genomic criterion");
                }
                clinicalCompoundCriterions.getCriterionCollection().add(clinicalCompoundCriterion);
            }
            query.getCompoundCriterion().getCriterionCollection().add(clinicalCompoundCriterions);
        }
        if (StringUtils.isNotBlank(platformName)) {
            GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
            geneNameCriterion.setPlatformName(platformName);
            query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        }
        return query;
    }

    private static Query createQuery(StudySubscription studySubscription) {
        Query query = new Query();
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setSubscription(studySubscription);
        return query;
    }

    /**
     * Goes through all criterion in a query looking for the given abstractCriterionType and returning that set.
     * @param <T> a subclass of AbstractCriterion.
     * @param query to retrieve criterion from.
     * @param abstractCriterionType must be a subclass of AbstractCriterion.
     * @return Set of all criterion matching the given class type.
     */
    public static <T> Set <T> getCriterionTypeFromQuery(Query query, 
            Class<T> abstractCriterionType) {
        Set<T> criterionSet = new HashSet<T>();
        CompoundCriterion compoundCriterion = query.getCompoundCriterion();
        getCriterionFromCompoundCriterion(abstractCriterionType, criterionSet, compoundCriterion);
        return criterionSet;
    }

   @SuppressWarnings("unchecked") // converting T to the class type.
   private static <T> void getCriterionFromCompoundCriterion(Class<T> type,
            Set<T> criterionSet, CompoundCriterion compoundCriterion) {
        for (AbstractCriterion criterion : compoundCriterion.getCriterionCollection()) {
            if (criterion.getClass().equals(type)) {
                criterionSet.add((T) criterion);
            } else if (criterion instanceof CompoundCriterion) {
                getCriterionFromCompoundCriterion(type, criterionSet, (CompoundCriterion) criterion);
            }
            
        }
    }
}
