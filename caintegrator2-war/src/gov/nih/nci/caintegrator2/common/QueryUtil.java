/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * This is a static utility class used by different caIntegrator2 query objects.
 */
public final class QueryUtil {

    private QueryUtil() { }

    /**
     * Retrieves the matching row from the given set that matches the given row.
     * @param rows - set of rows.
     * @param row -  row to test
     * @param isMultiplePlatformQuery - if multiple platforms in query then we don't need to match sample acquisitions.
     * @return the matching rows
     */
    public static ResultRow getMatchingRow(Set<ResultRow> rows, ResultRow row, boolean isMultiplePlatformQuery) {
        ResultRow foundRow = null;
        for (ResultRow currentRow : rows) {
            if (checkSubjectAssignmentMatch(row, currentRow) && checkImageSeriesMatch(row, currentRow)
                    && checkSampleMatch(isMultiplePlatformQuery, row, currentRow)) {
                foundRow = currentRow;
                break;
            }
        }
        return foundRow;
    }

    private static boolean checkSampleMatch(boolean isMultiplePlatformQuery, ResultRow rowToTest, ResultRow curRow) {
        if (isMultiplePlatformQuery) {
            return true;
        }
        if (curRow.getSampleAcquisition() == null && rowToTest.getSampleAcquisition() == null) { // both null
            return true;
        }
        if (curRow.getSampleAcquisition() == null || rowToTest.getSampleAcquisition() == null) { // only one is null
            return false;
        }
        return curRow.getSampleAcquisition().equals(rowToTest.getSampleAcquisition());
    }

    private static boolean checkImageSeriesMatch(ResultRow rowToTest, ResultRow curRow) {
        if (curRow.getImageSeries() == null && rowToTest.getImageSeries() == null) { // both null
            return true;
        }
        if (curRow.getImageSeries() == null || rowToTest.getImageSeries() == null) { // only one is null
            return false;
        }
        return StringUtils.equalsIgnoreCase(curRow.getImageSeries().getIdentifier(),
                rowToTest.getImageSeries().getIdentifier());
    }

    private static boolean checkSubjectAssignmentMatch(ResultRow rowToTest, ResultRow curRow) {
        if (curRow.getSubjectAssignment() == null && rowToTest.getSubjectAssignment() == null) { // both null
            return true;
        }
        if (curRow.getSubjectAssignment() == null || rowToTest.getSubjectAssignment() == null) { // only one is null
            return false;
        }
        return StringUtils.equalsIgnoreCase(curRow.getSubjectAssignment().getIdentifier(),
                rowToTest.getSubjectAssignment().getIdentifier());
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
     * Determines if a query has any genomic data associated.
     * @param query to check if it is genomic.
     * @return T/F value.
     */
    public static boolean isQueryGenomic(Query query) {
        return isQueryGeneExpression(query) || isQueryCopyNumber(query);
    }

    /**
     * Determines if a query is gene expression (gene expression results type or if it has gene expression criterion).
     * @param query to check to see if it is gene expression.
     * @return T/F if it is gene expression type or not.
     */
    public static boolean isQueryGeneExpression(Query query) {
        return (ResultTypeEnum.GENE_EXPRESSION.equals(query.getResultType())
                || QueryUtil.isCompoundCriterionGeneExpression(query.getCompoundCriterion())) ? true : false;
    }

    /**
     * Determines if a query is copy number (copy number results type or if it has copy number criterion).
     * @param query to check to see if it is copy number.
     * @return T/F if it is copy number type or not.
     */
    public static boolean isQueryCopyNumber(Query query) {
        return (ResultTypeEnum.COPY_NUMBER.equals(query.getResultType())
                || QueryUtil.isCompoundCriterionCopyNumber(query.getCompoundCriterion())) ? true : false;
    }

    /**
     * Recursive function that goes through all criterion in a CompoundCriterion to determine
     * if any of them are copy number based criterion.
     * @param criterion input for the recursive function.
     * @return T/F value.
     */
    public static boolean isCompoundCriterionCopyNumber(CompoundCriterion criterion) {
        for (AbstractCriterion abstractCriterion : criterion.getCriterionCollection()) {
            if (abstractCriterion instanceof CompoundCriterion) {
                if (isCompoundCriterionCopyNumber((CompoundCriterion) abstractCriterion)) {
                    return true;
                }
            } else if (abstractCriterion instanceof GeneNameCriterion
                    && (GenomicCriterionTypeEnum.COPY_NUMBER.equals(
                            ((GeneNameCriterion) abstractCriterion).getGenomicCriterionType()))) {
                return true;
            } else if (abstractCriterion instanceof CopyNumberAlterationCriterion) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recursive function that goes through all criterion in a CompoundCriterion to determine
     * if any of them are gene expression based criterion.
     * @param criterion input for the recursive function.
     * @return T/F value.
     */
    public static boolean isCompoundCriterionGeneExpression(CompoundCriterion criterion) {
        for (AbstractCriterion abstractCriterion : criterion.getCriterionCollection()) {
            if (abstractCriterion instanceof CompoundCriterion) {
                if (isCompoundCriterionGeneExpression((CompoundCriterion) abstractCriterion)) {
                    return true;
                }
            } else if (abstractCriterion instanceof GeneNameCriterion
                    && (GenomicCriterionTypeEnum.GENE_EXPRESSION.equals(
                            ((GeneNameCriterion) abstractCriterion).getGenomicCriterionType()))) {
                return true;
            } else if (abstractCriterion instanceof FoldChangeCriterion) {
                return true;
            } else if (abstractCriterion instanceof ExpressionLevelCriterion) {
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
     * Creates a query for all gene expression or copy number data in the study based on the input result type,
     *   and limits it to the given queries (if any are given).
     * @param studySubscription for querying.
     * @param queries to limit gene expression data returned based.
     * @param platformName if this is not null, specifies the platform to use for the gene expression query.
     * @param resultType the type for querying.
     * @return a Query which contains all gene expression data for all clinical queries given (if any).
     * @throws InvalidCriterionException if the queries passed in have different reporter types.
     */
    public static Query createAllGenomicDataQuery(StudySubscription studySubscription,
            Set<Query> queries, String platformName, ResultTypeEnum resultType) throws InvalidCriterionException {
        ReporterTypeEnum reporterType = getReporterTypeFromQueries(queries, resultType);
        Query query = createQuery(studySubscription);
        query.setAllGenomicDataQuery(true);
        query.setResultType(resultType);
        query.setReporterType(reporterType);
        if (queries != null && !queries.isEmpty()) {
            CompoundCriterion compoundCriterions = new CompoundCriterion();
            compoundCriterions.setBooleanOperator(BooleanOperatorEnum.OR);
            for (Query currentQuery : queries) {
                compoundCriterions.getCriterionCollection().add(currentQuery.getCompoundCriterion());
            }
            query.getCompoundCriterion().getCriterionCollection().add(compoundCriterions);
        }
        if (StringUtils.isNotBlank(platformName)) {
            GenomicCriterionTypeEnum criterionType = (ResultTypeEnum.GENE_EXPRESSION.equals(resultType))
                ? GenomicCriterionTypeEnum.GENE_EXPRESSION : GenomicCriterionTypeEnum.COPY_NUMBER;
            GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
            geneNameCriterion.setPlatformName(platformName);
            geneNameCriterion.setGenomicCriterionType(criterionType);
            query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        }
        return query;
    }

    private static ReporterTypeEnum getReporterTypeFromQueries(Set<Query> queries, ResultTypeEnum resultType)
    throws InvalidCriterionException {
        if (queries == null || queries.isEmpty()) {
            return ResultTypeEnum.GENE_EXPRESSION.equals(resultType)
                ? ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET : ReporterTypeEnum.DNA_ANALYSIS_REPORTER;
        }
        ReporterTypeEnum reporterType = null;
        for (Query query : queries) {
            if (reporterType == null) {
                reporterType = query.getReporterType();
            } else {
                if (reporterType != query.getReporterType()) {
                    throw new InvalidCriterionException("Trying to create a combined genomic query where two of the "
                            + "queries have different reporter types.");
                }
            }
        }
        return reporterType;
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

   /**
    * Converts a query result into a hashmap of Sample -> Column -> Value.  Used for the
    * <code>IGVSampleInfoFileWriter</code>.
    * @param result query result to convert.
    * @return the hashmap.
    */
   public static Map<Sample, Map<String, String>> retrieveSampleValuesMap(QueryResult result) {
       Map<Sample, Map<String, String>> sampleValuesMap
           = new HashMap<Sample, Map<String, String>>();
       for (ResultRow row : result.getRowCollection()) {
           for (SampleAcquisition sampleAcquisition : row.getSubjectAssignment().getSampleAcquisitionCollection()) {
               Sample sample = sampleAcquisition.getSample();
               if (sampleValuesMap.get(sample) == null) {
                   Map<String, String> columnToValueMap = new HashMap<String, String>();
                   for (ResultValue value : row.getValueCollection()) {
                       columnToValueMap.put(value.getColumn().getDisplayName(), value == null ? "" : value.toString());
                   }
                   sampleValuesMap.put(sample, columnToValueMap);
               }
           }
       }
       return sampleValuesMap;
   }

   /**
    * Generates a numeric comparison criterion from the given annotation field descriptor.
    * @param descriptor the annotation field descriptor
    * @param value the value
    * @return the created numeric comparison criterion
    */
   public static NumericComparisonCriterion createNumericComparisonCriterion(AnnotationFieldDescriptor descriptor,
           String value) {
       NumericComparisonCriterion criterion = new NumericComparisonCriterion();
       criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
       criterion.setNumericValue(Double.valueOf(value));
       criterion.setAnnotationFieldDescriptor(descriptor);
       return criterion;
   }

   /**
    * Generates a date comparison criterion from the given annotation field descriptor.
    * @param descriptor the annotation field descriptor
    * @param value the value
    * @return the created date comparison criterion
    */
   public static DateComparisonCriterion createDateComparisonCriterion(AnnotationFieldDescriptor descriptor,
           String value) {
       DateComparisonCriterion criterion = new DateComparisonCriterion();
       criterion.setDateComparisonOperator(DateComparisonOperatorEnum.EQUAL);
       try {
           criterion.setDateValue(DateUtil.createDate(value));
       } catch (ParseException e) {
           throw new IllegalStateException("Invalid date format for date " + value, e);
       }
       criterion.setAnnotationFieldDescriptor(descriptor);
       return criterion;
   }

   /**
    * Generates a string comparison criterion from the given annotation field descriptor.
    * @param descriptor the annotation field descriptor
    * @param value the value
    * @return the string numeric comparison criterion
    */
   public static StringComparisonCriterion createStringComparisonCriterion(AnnotationFieldDescriptor descriptor,
           String value) {
       StringComparisonCriterion criterion = new StringComparisonCriterion();
       criterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
       criterion.setStringValue(value);
       criterion.setAnnotationFieldDescriptor(descriptor);
       return criterion;
   }
}
