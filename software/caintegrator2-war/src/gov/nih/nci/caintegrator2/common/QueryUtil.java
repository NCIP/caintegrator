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
     * Determines if a query is gene expression (gene expression results type or if it has gene expression criterion).
     * @param query to check to see if it is gene expression.
     * @return T/F if it is gene expression type or not.
     */
    public static boolean isQueryGeneExpression(Query query) {
        return (ResultTypeEnum.GENE_EXPRESSION.equals(query.getResultType()) 
                || QueryUtil.isCompoundCriterionGeneExpression(query.getCompoundCriterion())) ? true : false;
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
     * Creates a query for all gene expression data in the study, and limits it to the given clinical queries (if any
     * are given).
     * @param studySubscription for querying.
     * @param clinicalQueries to limit gene expression data returned based.
     * @param platformName if this is not null, specifies the platform to use for the gene expression query.
     * @return a Query which contains all gene expression data for all clinical queries given (if any).
     */
    public static Query createAllGeneExpressionDataQuery(StudySubscription studySubscription,
            Set<Query> clinicalQueries, String platformName) {
        Query query = createQuery(studySubscription);
        query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        if (clinicalQueries != null && !clinicalQueries.isEmpty()) {
            CompoundCriterion clinicalCompoundCriterions = new CompoundCriterion();
            clinicalCompoundCriterions.setBooleanOperator(BooleanOperatorEnum.OR);
            for (Query clinicalQuery : clinicalQueries) {
                CompoundCriterion clinicalCompoundCriterion = clinicalQuery.getCompoundCriterion();
                if (isCompoundCriterionGeneExpression(clinicalCompoundCriterion)) {
                    throw new IllegalArgumentException("Clinical query has gene expression criterion");
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
