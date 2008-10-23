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
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.application.study.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.PersistentSet;


/**
 * Helper class to construct queries.
 */
public class QueryHelper {
    private boolean advancedView = false;
    private String basicQueryOperator = "";
    
    /**
     * @param queryManagementService A QueryManagementService instance
     * @param rowObjList list of populated QueryAnnotationCriteria (query row criteria).
     * @param resultColumns Columns that are to be displayed in the  Result page
     * @return QueryResult Valid results from the query execution, or null 
     * 
     */
    public QueryResult executeQuery(QueryManagementService queryManagementService, 
            List<QueryAnnotationCriteria> rowObjList, Collection<ResultColumn> resultColumns) {
        QueryResult queryResult = null;
               
        Query query = buildQuery(queryManagementService, rowObjList, null, null, resultColumns);
        queryResult = queryManagementService.execute(query);
        
        return queryResult;
    }
    
    /**
     * @param queryManagementService An instance of QueryManagementService.
     * @param rowObjList A list of populated QueryAnnotationCriteria (query row criteria).
     * @param queryName The name of the query.
     * @param queryDescription The description of the query.
     * @param resultColumns Columns that are to be displayed in the  Result page
     * @return Query An instantiated, populated Query object.
     */
    public Query buildQuery(QueryManagementService queryManagementService,
            List<QueryAnnotationCriteria> rowObjList,
            String queryName,
            String queryDescription,
            Collection<ResultColumn> resultColumns) {
        Query query = null;
        
        if (!advancedView) {
            query = buildBasicQuery(queryManagementService, rowObjList, queryName, queryDescription, resultColumns);
        }
        // TODO Advanced query
        
        return query;
    }
    
    /**
     * 
     * @param queryManagementService An instance of QueryManagementService.
     * @param rowObjList A list of populated QueryAnnotationCriteria (query row criteria).
     * @param queryName The name of the query.
     * @param queryDescription The description of the query.
     * @param resultColumns Columns that are to be displayed in the  Result page
     * @return Query An instantiated, populated Query object.
     */
    @SuppressWarnings({ "PMD" })
    public Query buildBasicQuery(QueryManagementService queryManagementService,
            List<QueryAnnotationCriteria> rowObjList,
            String queryName,
            String queryDescription,
            Collection<ResultColumn> resultColumns) {
        Query query = null;
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        
        QueryAnnotationCriteria queryAnnotationCriteria = null;
        Iterator<QueryAnnotationCriteria> iter = rowObjList.iterator();
        Collection<ResultColumn> columnCollection = new HashSet<ResultColumn>();
        // Iterate over the list of query annotation criteria objects
        while (iter.hasNext()) {
            queryAnnotationCriteria = iter.next();
            AbstractCriterion abstractCriterion = buildCriterion(queryAnnotationCriteria);
            compoundCriterion.getCriterionCollection().add(abstractCriterion);
            // Right now, we are adding all columns for the criterion, but only once per AnnotationDefinition
            Collection<ResultColumn> columns = resultColumns;
            for (ResultColumn column : columns) {
                if (!Cai2Util.columnCollectionContainsColumn(columnCollection, column)) {
                    columnCollection.add(column);
                }
            }
        }
        
        validateColumnColection(columnCollection);
        
        String booleanOperator = getBasicQueryBooleanOperator();
        compoundCriterion.setBooleanOperator(booleanOperator);
        
        query = new Query();
        query.setName(queryName);
        query.setDescription(queryDescription);
        query.setCompoundCriterion(compoundCriterion);
        query.setResultType(EntityTypeEnum.SUBJECT.getValue());
        StudySubscription studySubscription = 
            SessionHelper.getInstance().getDisplayableUserWorkspace().getCurrentStudySubscription();
        query.setSubscription(studySubscription);

        
        query.setColumnCollection(columnCollection);

        return query;
    }
    
    /**
     * The purpose of this function is to make sure that all of the column's indexes are unique so that
     * they are placed in the proper order.
     * @param columnCollection
     */
    private void validateColumnColection(Collection<ResultColumn> columnCollection) {
        Map<Integer, Boolean> usedColumns = new HashMap<Integer, Boolean>();
        for (int i = 0; i < columnCollection.size(); i++) {
            usedColumns.put(i, false);
        }
        for (ResultColumn column : columnCollection) {
            validateColumn(columnCollection, usedColumns, column);
        }
    }

    private void validateColumn(Collection<ResultColumn> columnCollection, 
                                Map<Integer, Boolean> usedColumns,
                                ResultColumn column) {
        if (!(column.getColumnIndex() < columnCollection.size())
             || usedColumns.get(column.getColumnIndex())) {
            for (int x = 0; x < columnCollection.size(); x++) {
                if (!usedColumns.get(x)) {
                    column.setColumnIndex(x);
                    break;
                }
            }
        }
        usedColumns.put(column.getColumnIndex(), true);
    }

    private AbstractCriterion buildCriterion(QueryAnnotationCriteria queryAnnotationCriteria) {
        // Initialize the CompoundCriterion
        AbstractCriterion abstractCriterion = null;
        String queryType = getQueryType(queryAnnotationCriteria);
        if ("string".equals(queryType)) {
            abstractCriterion = buildStringCriterion(queryAnnotationCriteria, queryAnnotationCriteria.getRowType());
        } else if ("numeric".equals(queryType)) {
            abstractCriterion = buildNumericCriterion(queryAnnotationCriteria, queryAnnotationCriteria.getRowType());
        }

        return abstractCriterion;
    }
    
    private StringComparisonCriterion buildStringCriterion(QueryAnnotationCriteria queryAnnotationCriteria, 
                                                            EntityTypeEnum type) {
        StringComparisonCriterion sCriterion = new StringComparisonCriterion();
        
        sCriterion.setAnnotationDefinition(getAnnotationDefinition(queryAnnotationCriteria));
        sCriterion.setWildCardType(getWildCardType(queryAnnotationCriteria.getAnnotationOperatorSelection()));
        sCriterion.setEntityType(type.getValue());
        sCriterion.setStringValue(queryAnnotationCriteria.getAnnotationValue());
        
        return sCriterion;
    }
    
    private NumericComparisonCriterion buildNumericCriterion(QueryAnnotationCriteria queryAnnotationCriteria, 
                                                             EntityTypeEnum type) {
        NumericComparisonCriterion nCriterion = new NumericComparisonCriterion();
        
        nCriterion.setAnnotationDefinition(getAnnotationDefinition(queryAnnotationCriteria));
        nCriterion.setNumericComparisonOperator((NumericComparisonOperatorEnum.getByValue(
                queryAnnotationCriteria.getAnnotationOperatorSelection()).getValue()));
        nCriterion.setEntityType(type.getValue());
        nCriterion.setNumericValue(Double.valueOf(queryAnnotationCriteria.getAnnotationValue()));
        
        return nCriterion;
    }
    
    @SuppressWarnings({ "PMD", "unchecked" })
    private AnnotationDefinition getAnnotationDefinition(QueryAnnotationCriteria queryAnnotationCriteria) {
        AnnotationDefinition annoDef = null;
        
        AnnotationSelection annoHelper = queryAnnotationCriteria.getAnnotationSelections();
        PersistentSet annoDefs = (PersistentSet) annoHelper.getAnnotationDefinitions();
        
        Iterator iter = annoDefs.iterator();
        while (iter.hasNext()) {
            annoDef = (AnnotationDefinition) iter.next();
            if (annoDef.getDisplayName().equals(queryAnnotationCriteria.getAnnotationSelection())) {
                break;
            }
        }
        
        return annoDef;
    }

    
            
    private String getWildCardType(String stringOperator) {
        String wildCardType = "";
        
        if ("ends with".equals(stringOperator)) {
            wildCardType = WildCardTypeEnum.WILDCARD_BEFORE_STRING.getValue();
        } else if ("begins with".equals(stringOperator)) {
            wildCardType = WildCardTypeEnum.WILDCARD_AFTER_STRING.getValue();
        } else if ("contains".equals(stringOperator)) {
            wildCardType = WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING.getValue();
        } else if ("equals".equals(stringOperator)) {
            wildCardType = WildCardTypeEnum.WILDCARD_OFF.getValue();
        } else {
            // default even though an error condition
            wildCardType = WildCardTypeEnum.WILDCARD_OFF.getValue();
        }
        
        return wildCardType;
    }
    
    private String getQueryType(QueryAnnotationCriteria queryAnnotationCriteria) {
        String queryType = "";
        
        String annotationOperator = queryAnnotationCriteria.getAnnotationOperatorSelection();
        AnnotationSelection annoHelper = queryAnnotationCriteria.getAnnotationSelections();
        if (annoHelper.getStringOptionToEnumMap().containsKey(annotationOperator)) {
            queryType = "string";
        } else if (annoHelper.getNumericOptionToEnumMap().containsKey(annotationOperator)) {
            queryType = "numeric";
        } 
        // TODO Handle date type
        
        return queryType;
    }
    
    private String getBasicQueryBooleanOperator() {
        String boolOp = "";
        
        if ("and".equalsIgnoreCase(this.basicQueryOperator)) {
            boolOp = BooleanOperatorEnum.AND.getValue();
        } else if ("or".equalsIgnoreCase(this.basicQueryOperator)) {
            boolOp = BooleanOperatorEnum.OR.getValue();
        }
        
        return boolOp;
    }
    
    /**
     * @return the advancedView
     */
    public boolean isAdvancedView() {
        return advancedView;
    }
    /**
     * @param advancedView the advancedView to set
     */
    public void setAdvancedView(boolean advancedView) {
        this.advancedView = advancedView;
    }
    /**
     * @return the basicQueryOperator
     */
    public String getBasicQueryOperator() {
        return basicQueryOperator;
    }
    /**
     * @param basicQueryOperator the basicQueryOperator to set
     */
    public void setBasicQueryOperator(String basicQueryOperator) {
        this.basicQueryOperator = basicQueryOperator;
    };
    
    
    

}
