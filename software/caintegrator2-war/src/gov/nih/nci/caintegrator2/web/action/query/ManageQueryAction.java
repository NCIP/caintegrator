/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;


import gov.nih.nci.caintegrator2.application.query.GenomicDataResultRowComparator;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.DownloadableFile;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.interceptor.ParameterNameAware;

/**
 * Handles the form in which the user constructs, edits and runs a query.
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" }) // see execute method

public class ManageQueryAction extends AbstractCaIntegrator2Action implements ParameterNameAware {

    private static final long serialVersionUID = 1L;

    private static final String RESULTS_TAB = "searchResults";
    private static final String CRITERIA_TAB = "criteria";
    private static final String COLUMNS_TAB = "columns";
    private static final String SORTING_TAB = "sorting";
    private static final String SAVE_AS_TAB = "saveAs";

    private static final String EXECUTE_QUERY = "executeQuery";
    
    private QueryManagementService queryManagementService;
    private StudyManagementService studyManagementService;
    private NCIABasket nciaBasket;
    private String selectedAction = EXECUTE_QUERY;
    private String displayTab;
    private int rowNumber;
    private Long queryId = null;
    private boolean export = false;

    /**
     * {@inheritDoc}
     */
    public boolean acceptableParameterName(String parameterName) {
        boolean retVal = true;
        String firstCharacter = parameterName.substring(0, 1);

        if (parameterName != null) {
            if (parameterName.startsWith("d-")) {
                retVal = false;
                if (parameterName.endsWith("-e")) {
                    setExport(true);
                }
            } else if (StringUtils.isNumeric(firstCharacter)) {
                retVal = false;
            }
        }
        return retVal;
    }
    

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if ("selectedTabSearchResults".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
        } else {
            displayTab = CRITERIA_TAB;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (hasActionErrors()) {
            return;
        } else if ("selectedTabSearchResults".equals(selectedAction)) {
            return;
        } else if ("loadQuery".equals(selectedAction)
                || "loadExecute".equals(selectedAction)) {
            getQueryForm().setQuery(null);
            validateExecuteQuery(); 
        } else if (EXECUTE_QUERY.equals(selectedAction)) {
            validateExecuteQuery(); 
        } else if ("saveQuery".equals(selectedAction)) {
            validateSaveQuery();
        } else if ("saveAsQuery".equals(selectedAction)) {
            validateSaveQuery();
        } else if ("addCriterionRow".equals(selectedAction)) {
            validateAddCriterionRow();
        }
    }
    
    private void validateAddCriterionRow() {
        getQueryForm().validate(this);
    }
    
    private void validateExecuteQuery() {
        ensureQueryIsLoaded();
        getQueryForm().validate(this);
    }
    
    private void validateSaveQuery() {
        getQueryForm().validateForSave(this);
        if (this.hasErrors()) {
            displayTab = SAVE_AS_TAB;
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveMethodLength" }) // Checking action type.
    public String execute() {
        
        String returnValue = ERROR;

        if ("selectedTabSearchResults".equals(selectedAction)) {
            return (isExport()) ? "export" : SUCCESS;
        }

        // Check which user action is submitted
        if ("remove".equals(selectedAction)) {
            setQueryResult(null);
            returnValue = removeRow();
        } else if ("addCriterionRow".equals(selectedAction)) {
            setQueryResult(null);
            returnValue = addCriterionRow();
        } else if (EXECUTE_QUERY.equals(selectedAction)
                || "loadExecute".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = executeQuery();
        } else if ("saveQuery".equals(selectedAction)) {
            returnValue = saveQuery();
        } else if ("saveAsQuery".equals(selectedAction)) {
            returnValue = saveAsQuery();
        } else if ("deleteQuery".equals(selectedAction)) {
            returnValue = deleteQuery();
        } else if ("updateCriteria".equals(selectedAction)) {
            updateCriteria();
            returnValue = SUCCESS;
        } else if ("updateColumns".equals(selectedAction)) {
            displayTab = COLUMNS_TAB;
            returnValue = SUCCESS;
        } else if ("updateSorting".equals(selectedAction)) {
            updateSorting();
            displayTab = SORTING_TAB;
            returnValue = SUCCESS;
        } else if ("updateResultsPerPage".equals(selectedAction)) {
               displayTab = RESULTS_TAB;
               returnValue = SUCCESS;
        } else if ("exportGenomicResults".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = exportGenomicResults();
        } else if ("createNewQuery".equals(selectedAction)) {
            getQueryForm().createQuery(getStudySubscription());
            setQueryResult(null);
            returnValue = SUCCESS;    
        } else if ("forwardToNcia".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = forwardToNciaBasket();
        } else if ("selectAll".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            getDisplayableWorkspace().getQueryResult().setSelectAll(true);
            returnValue = toggleCheckboxSelections();
        } else if ("selectNone".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            getDisplayableWorkspace().getQueryResult().setSelectAll(false);
            returnValue = toggleCheckboxSelections();
        } else if ("retrieveDicomImages".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = createDicomJob();
        } else if ("loadQuery".equals(selectedAction)) {
            displayTab = CRITERIA_TAB;
            returnValue = executeQuery();
        } else {
            addActionError("Unknown action '" + selectedAction + "'");
            returnValue = ERROR; 
        }
        return returnValue;
    }

    private String exportGenomicResults() {
        File file = queryManagementService.createCsvFileFromGenomicResults(getGenomicDataQueryResult());
        DownloadableFile downloadableFile = new DownloadableFile();
        downloadableFile.setPath(file.getAbsolutePath());
        downloadableFile.setContentType(DownloadableFile.CONTENT_TYPE_TEXT);
        downloadableFile.setFilename("genomicResults.csv");
        downloadableFile.setDeleteFile(true);
        getDisplayableWorkspace().setTemporaryDownloadFile(downloadableFile);
        return "exportGenomicResults";
    }


    private void updateCriteria() {
        displayTab = CRITERIA_TAB;
        getQueryForm().processCriteriaChanges();
    }
    
    private void updateSorting() {
        getQueryForm().getResultConfiguration().reindexColumns();
    }

    /**
     * Selects all checkboxes.
     * @return the Struts result.
     */
    public String toggleCheckboxSelections() {
        if (getQueryResult() != null 
                && getQueryResult().getRows() != null
                && !getQueryResult().getRows().isEmpty()) {
            for (DisplayableResultRow row : getQueryResult().getRows()) {
                row.setCheckedRow(getDisplayableWorkspace().getQueryResult().getSelectAll());
            }
        }
        return SUCCESS;
    }
    
    /**
     * Forwards to NCIA.
     * @return the Struts result.
     */
    public String forwardToNciaBasket() {
        nciaBasket = queryManagementService.createNciaBasket(retrieveCheckedRows());
        return "nciaBasket";
    }

    /**
     * Creates a dicom job and runs.
     * @return the Struts result.
     */
    public String createDicomJob() {
        if (getDisplayableWorkspace().getDicomJob() != null 
                && getDisplayableWorkspace().getDicomJob().isCurrentlyRunning()) {
            addActionError("There is currently a Dicom retrieval job running on this session, " 
                            + "please wait for that to complete before running another.");
            return "dicomJobCurrentlyRunning";
        }
        NCIADicomJob dicomJob = queryManagementService.createDicomJob(retrieveCheckedRows());
        ImageDataSourceConfiguration dataSource = studyManagementService.retrieveImageDataSource(getStudy());
        if (dataSource != null) {
            dicomJob.setServerConnection(dataSource.getServerProfile());
        }
        getDisplayableWorkspace().setDicomJob(dicomJob);
        dicomJob.setCurrentlyRunning(true);
        return "dicomJobAjax";
    }

    private List<DisplayableResultRow> retrieveCheckedRows() {
        List <DisplayableResultRow> checkedRows = new ArrayList<DisplayableResultRow>();
        if (getQueryResult() != null 
            && getQueryResult().getRows() != null
            && !getQueryResult().getRows().isEmpty()) {
            for (DisplayableResultRow row : getQueryResult().getRows()) {
                if (row.isCheckedRow()) {
                    checkedRows.add(row);
                }
            }
        }
        return checkedRows;
    }
    
    /**
     * Add a a criterion row to the query, and validates that row has data for this study.
     * 
     * @return the Struts result.
     */
    public String addCriterionRow() {
        getQueryForm().getCriteriaGroup().addCriterion(getCurrentStudy());
        return SUCCESS;
    }
    
    private String removeRow() {
        getQueryForm().getCriteriaGroup().removeRow(rowNumber);
        return SUCCESS;
    }
    
    /**
     * Execute the current query.
     * 
     * @return the Struts result.
     */
    public String executeQuery() {
        ensureQueryIsLoaded();
        updateSorting();
        try {
            if (ResultTypeEnum.GENOMIC.getValue().equals(getQueryForm().getResultConfiguration().getResultType())) {
                GenomicDataQueryResult genomicResult;
                genomicResult = queryManagementService.executeGenomicDataQuery(getQueryForm().getQuery());
                if (genomicResult.getRowCollection() != null) {
                    Collections.sort(genomicResult.getRowCollection(), new GenomicDataResultRowComparator());
                }
                setGenomicDataQueryResult(genomicResult);
            } else {
                QueryResult result = queryManagementService.execute(getQueryForm().getQuery());
                loadAllImages(result);
                setQueryResult(new DisplayableQueryResult(result));
            }
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    private void ensureQueryIsLoaded() {
        if (getQueryForm().getQuery() == null) {
            getQueryForm().setQuery(getQueryById());
            getQueryForm().setOrgQueryName(getQueryForm().getQuery().getName());
        }
    }

    private void loadAllImages(QueryResult result) {
        for (ResultRow resultRow : result.getRowCollection()) {
            if (resultRow.getImageSeries() != null) {
                resultRow.getImageSeries().getImageCollection().isEmpty();
                resultRow.getImageSeries().getImageStudy().getImageDataSource().getServerProfile().getHostname();
            }
            if (resultRow.getSubjectAssignment().getImageStudyCollection() != null) {
                resultRow.getSubjectAssignment().getImageStudyCollection().isEmpty();
            }
        }
    }
    
    /**
     * Save the current query.
     * 
     * @return the Struts result.
     */
    public String saveQuery() {
        updateSorting();
        Query query = getQueryForm().getQuery();
        if (!getStudySubscription().getQueryCollection().contains(query)) {
            getStudySubscription().getQueryCollection().add(query);
            query.setSubscription(getStudySubscription());
        }
        queryManagementService.save(query);
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        getQueryForm().setOrgQueryName(query.getName());
        return SUCCESS;
    }
    
    /**
     * Save the current saved query as a new query.
     * 
     * @return the Struts result.
     */
    public String saveAsQuery() {
        updateSorting();
        Query query;
        try {
            query = getQuery().clone();
        } catch (CloneNotSupportedException e) {
            addActionError("Error cloning the Query.");
            return ERROR;
        }
        queryManagementService.save(query);
        setQueryId(query.getId());
        getStudySubscription().getQueryCollection().add(query);
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        getQueryForm().setQuery(getQueryById());
        getQueryForm().setOrgQueryName(getQueryForm().getQuery().getName());
        return SUCCESS;
    }

    /**
     * Delete the current saved query.
     * @return the Struts result.
     */
    public String deleteQuery() {
        queryId = getQueryForm().getQuery().getId();
        Query query = getQueryById();
        getStudySubscription().getQueryCollection().remove(query);
        queryManagementService.delete(query);
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        getQueryForm().createQuery(getStudySubscription());
        setQueryResult(null);
        return SUCCESS;
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
     * @return the rowNumber
     */
    public String getRowNumber() {
        return String.valueOf(rowNumber);
    }
    
    /**
     * @param rowNumber the rowNumber to set
     */
    public void setRowNumber(String rowNumber) {
        if (!StringUtils.isBlank(rowNumber)) {
            this.rowNumber = Integer.parseInt(rowNumber);
        }
    }
        
    /**
     * @return the displayTab
     */
    public String getDisplayTab() {
        return displayTab;
    }

    /**
     * @param queryId the queryId to set
     */
    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    private Query getQueryById() {
        for (Query query : getStudySubscription().getQueryCollection()) {
            if (queryId.equals(query.getId())) {
                return query;
            }
        }
        return null;
    }

    /**
     * @return the currently open query id for the left nav menu.
     */
    public Long getOpenQueryId() {
        if (getQueryForm().getQuery() != null) {
            return getQueryForm().getQuery().getId();
        } else {
            return null;
        }
    }
    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }
    /**
     * @param studyManagementService the studyManagementService to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }
    
    /**
     * @return the export
     */
    public boolean isExport() {
        return export;
    }

    /**
     * @param export the export to set
     */
    public void setExport(boolean export) {
        this.export = export;
    }
        
    /**
     * Set the page size.
     * @param pageSize the page size
     */
    public void setPageSize(int pageSize) {
        if (getQueryResult() != null) {
            getQueryResult().setPageSize(pageSize);
        }
    }

    /**
     * @return the nciaBasket
     */
    public NCIABasket getNciaBasket() {
        return nciaBasket;
    }

    /**
     * @return the nciaBasket
     */
    public String getNciaBasketUrl() {
        ImageDataSourceConfiguration dataSource = studyManagementService.retrieveImageDataSource(getStudy());
       return "https://" + dataSource.getServerProfile().getHostname() + "/ncia/externalDataBasketDisplay.jsf";
    }
}
