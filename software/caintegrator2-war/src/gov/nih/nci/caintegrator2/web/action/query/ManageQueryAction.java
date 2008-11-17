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
import gov.nih.nci.caintegrator2.application.query.ResultTypeEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

/**
 * Handles the form in which the user constructs, edits and runs a query.
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" }) // see execute method

public class ManageQueryAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    private QueryManagementService queryManagementService;
    private ManageQueryHelper manageQueryHelper;
    private String selectedAction = "";
    private String rowNumber = "";
    private String selectedRowCriterion = "uninitializedselectedRowCriterion";
    //Struts should automatically populate these arrays from the form element.
    private String[] selectedAnnotations;  //selected annotations for all criterion as a list.
    private String[] selectedOperators; //selected operators for all criterion as a list.
    private String[] selectedValues; //selected values for all criterion as a list.
    private String selectedBasicOperator = "or"; // user selects AND or OR operation for a basic query
    private String searchName;
    private String searchDescription;
    private Long[] selectedClinicalAnnotations; // selected clinical annotations from columns tab.
    private Long[] selectedImageAnnotations;    // selected image annotations from columns tab.
        
    /**
     * The 'prepare' interceptor will look for this method enabling 
     * preprocessing.
     */
    public void prepare() {
        super.prepare();
        // Instantiate/prepopulate manageQueryHelper if necessary
        manageQueryHelper = ManageQueryHelper.getInstance();
        if ("createNewQuery".equals(selectedAction)) {
            manageQueryHelper = ManageQueryHelper.resetSessionInstance();
        }

        if (getStudySubscription() != null) {
            if (!manageQueryHelper.isPrepopulated()) {
                manageQueryHelper.prepopulateAnnotationSelectLists(getStudy());
            }
            if (this.selectedAnnotations == null || this.selectedAnnotations.length == 0) {
                checkClinicalAnnotationDefinitions();     
                checkImageAnnotationDefinitions();
            }
        }
    }
    /**
     * Setting all the Clinical definitions checked in JSP as a default action.
     */
    public void checkClinicalAnnotationDefinitions() {
        Collection<AnnotationDefinition> annotationDefinitions = manageQueryHelper.getClinicalAnnotationDefinitions();
        if (annotationDefinitions != null) {
            this.selectedClinicalAnnotations = new Long[annotationDefinitions.size()];
            int i = 0;
            for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
                this.selectedClinicalAnnotations[i] = annotationDefinition.getId();
                i++;
            }
        }
    }
    
    /**
     * Setting all the Image definitions checked in JSP as a default action.
     */
    public void checkImageAnnotationDefinitions() {
        Collection<AnnotationDefinition> annotationDefinitions = manageQueryHelper.getImageAnnotationDefinitions();
        if (annotationDefinitions != null) {
            this.selectedImageAnnotations = new Long[annotationDefinitions.size()];
            int i = 0;
            for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
                this.selectedImageAnnotations[i] = annotationDefinition.getId();
                i++;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {

        if (getStudySubscription() == null) {
            addActionError("Please select a study under \"My Studies\".");
            return;
        }
        
        saveFormData();
        if ("executeQuery".equals(selectedAction)) {
            validateExecuteQuery(); 
        }
            
        if ("saveQuery".equals(selectedAction)) {
            validateSaveQuery();
        }
            
    }
    
    private void validateExecuteQuery() {
        validateHasCriterion();
    }
    
    private void validateSaveQuery() {
        validateHasCriterion();
        // Need to see if there's non-space characters in the saved name.
        if (StringUtils.isEmpty(searchName)) {
            addActionError("Must have a name for your query");
        } else {
            for (Query query : getStudySubscription().getQueryCollection()) {
                if (searchName.equalsIgnoreCase(query.getName())) {
                    addActionError("There is already a query named '" + searchName
                            + "', either delete that one, or use a different name.");
                    break;
                }
            }
        }
    }
    
    private void validateHasCriterion() {
        if (manageQueryHelper.getQueryCriteriaRowList().isEmpty()) {
            addActionError("No Criterion Added, must have Criteria to Search or Save a Query.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveMethodLength" }) // Checking action type.
    public String execute()  {
        
        // declarations
        String returnValue = ERROR;
        
        // Check which user action is submitted
        if ("remove".equals(selectedAction)) {
            returnValue = removeRow();
        } else if ("addCriterionRow".equals(selectedAction)) {
            returnValue = addCriterionRow();
        } else if ("executeQuery".equals(selectedAction)) {
            returnValue = executeQuery();
        } else if ("saveQuery".equals(selectedAction)) {
            returnValue = saveQuery();
        } else if ("editQuery".equals(selectedAction)) {
            // call editQuery
            returnValue = SUCCESS;
        } else if ("createNewQuery".equals(selectedAction)) {
            // call new query
            returnValue = SUCCESS;    
        } else if ("updateResultsPerPage".equals(selectedAction)) {
         // Does nothing right now, eventually might actually persist this value to db.
            returnValue = SUCCESS;
        } else if ("updateOperators".equals(selectedAction)) {
            returnValue = updateOperatorsForAnnotationSelection();
        } else {
            returnValue = ERROR; 
        }     
        
        return returnValue;
    }
    
    /**
     * Add a a criterion row to the query, and validates that row has data for this study.
     * 
     * @return the Struts result.
     */
    public String addCriterionRow() {
        
        if (EntityTypeEnum.SUBJECT.getValue().equals(this.selectedRowCriterion) 
            && !manageQueryHelper.configureClinicalQueryCriterionRow()) {
                addActionError("There are no clinical annotations defined for this study.");
        }
        if (EntityTypeEnum.IMAGESERIES.getValue().equals(this.selectedRowCriterion)
            && !manageQueryHelper.configureImageSeriesQueryCriterionRow()) {
                addActionError("There are no image series annotations defined for this study.");
        }
        
        if (EntityTypeEnum.GENEEXPRESSION.getValue().equals(this.selectedRowCriterion)) {
            if (!manageQueryHelper.configureGeneExpressionCriterionRow()) {
                addActionError("There are no gene expression annotations defined for this study.");
            } else {
                // Turn it into a genomic query type, by default, if they add a genomic row.
                manageQueryHelper.setResultType(ResultTypeEnum.GENOMIC.getValue());
            }
        }
            

        return SUCCESS;
    }
    
    private String removeRow() {
        try {
            if (!manageQueryHelper.removeQueryAnnotationCriteria(Integer.valueOf(rowNumber))) {
                addActionError("Invalid row to remove");
            }
        } catch (NumberFormatException e) {
            addActionError("rowNumber is not a valid number.");
        }
        return SUCCESS;
    }
    
    private String updateOperatorsForAnnotationSelection() {
        try {
             if (this.selectedAnnotations != null) {
                  manageQueryHelper.updateAnnotationDefinition(selectedAnnotations, Integer.valueOf(rowNumber));
                }
        } catch (NumberFormatException e) {
            addActionError("rowNumber is not a valid number.");  
        }
        return SUCCESS;
    }

    private void saveFormData() {
        manageQueryHelper.updateSelectedValues(getSelectedAnnotations());
        manageQueryHelper.updateSelectedOperatorValues(getSelectedOperators());
        manageQueryHelper.updateSelectedUserValues(getSelectedValues());
        manageQueryHelper.setResultColumnCollection(getSelectedClinicalAnnotations(), EntityTypeEnum.SUBJECT);
        manageQueryHelper.setResultColumnCollection(getSelectedImageAnnotations(), EntityTypeEnum.IMAGESERIES);
        manageQueryHelper.getColumnIndexOptions().clear();
        manageQueryHelper.indexOption();
    }    
    
    /**
     * Delete a criterion row from the query.
     * 
     * @return the Struts result.
     */
    public String deleteCriterionRow() {        
        return SUCCESS;
    }
    
    /**
     * Delete all criterion rows from the query.
     * 
     * @return the Struts result.
     */
    public String deleteCriterionRowAll() {
        return SUCCESS;
    }
    
    /**
     * Execute the current query.
     * 
     * @return the Struts result.
     */
    public String executeQuery() {
        
        if (ResultTypeEnum.GENOMIC.getValue().equals(manageQueryHelper.getResultType())) {
            GenomicDataQueryResult genomicResult =  
                manageQueryHelper.executeGenomicQuery(queryManagementService, 
                                                      selectedBasicOperator);
            setGenomicDataQueryResult(genomicResult);
        } else {
            QueryResult result = manageQueryHelper.executeQuery(queryManagementService, selectedBasicOperator);
            setQueryResult(new DisplayableQueryResult(result));
        }
        return SUCCESS;
    }
    
    /**
     * Select the columns to be returned in the query.
     * 
     * @return the Struts result.
     */
    public String selectColumns() {
        return SUCCESS;
    }
    
    /**
     * Select the sorting of columns that appear in the search results.
     * 
     * @return the Struts result.
     */
    public String selectSorting() {
        return SUCCESS;
    }
    
    /**
     * Save the current query.
     * 
     * @return the Struts result.
     */
    public String saveQuery() {
        manageQueryHelper.saveQuery(queryManagementService, selectedBasicOperator, searchName, searchDescription);
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        return SUCCESS;
    }

    /**
     * @return the manageQueryHelper
     */
    public ManageQueryHelper getManageQueryHelper() {
        return manageQueryHelper;
    }
    
    /**
     * @param manageQueryHelper the manageQueryHelper to set
     */
    public void setManageQueryHelper(ManageQueryHelper manageQueryHelper) {
        this.manageQueryHelper = manageQueryHelper;
    }

    /**
     * @return the selectedRowCriterion
     */
    public String getSelectedRowCriterion() {
        return selectedRowCriterion;
    }

    /**
     * @param selectedRowCriterion the selectedRowCriterion to set
     */
    public void setSelectedRowCriterion(String selectedRowCriterion) {
        this.selectedRowCriterion = selectedRowCriterion;
    }

    /**
     * @return the selectedAnnotations
     */
    public String[] getSelectedAnnotations() {
        String[] holdArray = {""};
        if (selectedAnnotations != null) {
             holdArray = this.selectedAnnotations.clone();
        }
        return holdArray;
    }

    /**
     * @param selectedAnnotations the selectedAnnotations to set
     */
    public void setSelectedAnnotations(String[] selectedAnnotations) {
        String [] holdArray;
        holdArray = selectedAnnotations.clone();
        this.selectedAnnotations = holdArray;
    }

    /**
     * @return the selectedOperators
     */
    public String[] getSelectedOperators() {
        String [] holdArray = {""};
        if (selectedOperators != null) {
            holdArray = selectedOperators.clone();
        }
        return holdArray;
    }

    /**
     * @param selectedOperators the selectedOperators to set
     */
    public void setSelectedOperators(String[] selectedOperators) {
        String [] holdArray;
        holdArray = selectedOperators.clone();
        this.selectedOperators = holdArray;
    }

    /**
     * @return the selectedValues
     */
    public String[] getSelectedValues() {
        String [] holdArray = {""};
        if (selectedValues != null) {
            holdArray = selectedValues.clone();
        }
        return holdArray;
    }

    /**
     * @param selectedValues the selectedValues to set
     */
    public void setSelectedValues(String[] selectedValues) {
        String [] holdArray;
        holdArray = selectedValues.clone();
        this.selectedValues = holdArray;
    }
    /**
     * @return the selectedClinicalAnnotations
     */
    public Long[] getSelectedClinicalAnnotations() {
        Long [] holdArray = null;
        if (selectedClinicalAnnotations != null) {
            holdArray = selectedClinicalAnnotations.clone();
        }
        return holdArray;

    }

    /**
     * @param selectedClinicalAnnotations the selectedClinicalAnnotations to set
     */
    public void setSelectedClinicalAnnotations(Long[] selectedClinicalAnnotations) {
        Long [] holdArray;
        holdArray = selectedClinicalAnnotations.clone();
        this.selectedClinicalAnnotations = holdArray;

    }

    /**
     * @return the selectedImageAnnotations
     */
    public Long[] getSelectedImageAnnotations() {
        Long [] holdArray = null;
        if (selectedImageAnnotations != null) {
            holdArray = selectedImageAnnotations.clone();
        }
        return holdArray;
    }

    /**
     * @param selectedImageAnnotations the selectedImageAnnotations to set
     */
    public void setSelectedImageAnnotations(Long[] selectedImageAnnotations) {
       Long [] holdArray;
        holdArray = selectedImageAnnotations.clone();
        this.selectedImageAnnotations = holdArray;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }
    
    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the action as selected in the input form
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the selectedBasicOperator
     */
    public String getSelectedBasicOperator() {
        return selectedBasicOperator;
    }

    /**
     * @param selectedBasicOperator the selectedBasicOperator to set
     */
    public void setSelectedBasicOperator(String selectedBasicOperator) {
        this.selectedBasicOperator = selectedBasicOperator;
    }

    /**
     * @return the searchName
     */
    public String getSearchName() {
        return searchName;
    }

    /**
     * @param searchName the searchName to set
     */
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    /**
     * @return the searchDescription
     */
    public String getSearchDescription() {
        return searchDescription;
    }

    /**
     * @param searchDescription the searchDescription to set
     */
    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    /**
     * @return the clinicalDefinitionsSize
     */
    public int getClinicalDefinitionsSize() {
        return manageQueryHelper.getClinicalAnnotationDefinitions().size();
        
    }

    
    /**
     * @return the imageDefinitionsSize
     */
    public int getImageDefinitionsSize() {
        return manageQueryHelper.getImageAnnotationDefinitions().size();
        
    }
    /**
     * @return the rowNumber
     */
    public String getRowNumber() {
        return rowNumber;
    }
    /**
     * @param rowNumber the rowNumber to set
     */
    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }
}
