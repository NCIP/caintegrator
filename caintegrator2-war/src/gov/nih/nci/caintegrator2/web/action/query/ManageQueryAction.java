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

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.application.query.GenomicDataResultRowComparator;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultComparator;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.DownloadableFile;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator2.web.action.query.form.AbstractCriterionRow;
import gov.nih.nci.caintegrator2.web.action.query.form.CriterionRowTypeEnum;
import gov.nih.nci.caintegrator2.web.action.query.form.GeneNameCriterionWrapper;
import gov.nih.nci.caintegrator2.web.action.query.form.MultiSelectParameter;
import gov.nih.nci.caintegrator2.web.action.query.form.SelectListParameter;
import gov.nih.nci.caintegrator2.web.action.query.form.SubjectListCriterionWrapper;
import gov.nih.nci.caintegrator2.web.action.query.form.TextFieldParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.interceptor.ParameterNameAware;

/**
 * Handles the form in which the user constructs, edits and runs a query.
 */
public class ManageQueryAction extends AbstractDeployedStudyAction implements ParameterNameAware, ServletContextAware {

    private static final long serialVersionUID = 1L;

    private static final String RESULTS_TAB = "searchResults";
    private static final String CRITERIA_TAB = "criteria";
    private static final String COLUMNS_TAB = "columns";
    private static final String SORTING_TAB = "sorting";
    private static final String SAVE_AS_TAB = "saveAs";

    private static final String EXECUTE_QUERY = "executeQuery";
    private Set<String> geneExpressionPlatformsInStudy = new HashSet<String>();
    private Set<String> copyNumberPlatformsInStudy = new HashSet<String>();
    private Set<String> copyNumberPlatformsWithCghCallInStudy = new HashSet<String>();
    private QueryManagementService queryManagementService;
    private StudyManagementService studyManagementService;
    private AnalysisService analysisService;
    private NCIABasket nciaBasket;
    private String selectedAction = EXECUTE_QUERY;
    private String displayTab;
    private int rowNumber;
    private Long queryId = null;
    private boolean export = false;
    private String geneListName = "";
    private String subjectListName = "";
    private String subjectListDescription = "";
    private boolean subjectListVisibleToOthers = false;
    private String genomicSortingType;
    private String genomicSortingIndex;
    private ServletContext context;

    /**
     * @return the genomicSortingType
     */
    public String getGenomicSortingType() {
        return genomicSortingType;
    }

    /**
     * @param genomicSortingType the genomicSortingType to set
     */
    public void setGenomicSortingType(String genomicSortingType) {
        this.genomicSortingType = genomicSortingType;
    }

    /**
     * @return the genomicSortingIndex
     */
    public String getGenomicSortingIndex() {
        return genomicSortingIndex;
    }


    /**
     * @param genomicSortingIndex the genomicSortingIndex to set
     */
    public void setGenomicSortingIndex(String genomicSortingIndex) {
        this.genomicSortingIndex = genomicSortingIndex;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean acceptableParameterName(String parameterName) {
        boolean retVal = true;
        if (parameterName != null) {
            if (parameterName.startsWith("d-")) {
                retVal = false;
                if (parameterName.endsWith("-e")) {
                    setExport(true);
                }
            } else if (StringUtils.isNumeric(parameterName.substring(0, 1))) {
                retVal = false;
            }
        }
        return retVal;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        refreshGenomicSources();
        geneExpressionPlatformsInStudy = getQueryManagementService().
            retrieveGeneExpressionPlatformsForStudy(getStudy());
        copyNumberPlatformsInStudy = getQueryManagementService().
            retrieveCopyNumberPlatformsForStudy(getStudy());
        copyNumberPlatformsWithCghCallInStudy = getQueryManagementService().
            retrieveCopyNumberPlatformsWithCghCallForStudy(getStudy());
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
            getQueryForm().setQuery(null, geneExpressionPlatformsInStudy, copyNumberPlatformsInStudy,
                    copyNumberPlatformsWithCghCallInStudy);
            validateExecuteQuery();
        } else if (EXECUTE_QUERY.equals(selectedAction)) {
            validateExecuteQuery();
        } else if ("saveQuery".equals(selectedAction)
                || "saveAsQuery".equals(selectedAction)) {
            validateSaveQuery();
        } else if ("addCriterionRow".equals(selectedAction)) {
            validateAddCriterionRow();
        } else if ("saveSubjectList".equals(selectedAction)) {
            validateSaveSubjectList();
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
        try {
            getQuery().getCompoundCriterion().validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            displayTab = CRITERIA_TAB;
        }

    }

    private void validateSaveSubjectList() {
        if (StringUtils.isEmpty(getSubjectListName())) {
            addActionError(getText("struts.messages.error.query.subject.list.name.empty"));
        } else if (getStudySubscription().getSubjectListNames().contains(getSubjectListName())) {
            addActionError(getText("struts.messages.error.query.subject.list.name.duplicate",
                    getArgs(getSubjectListName())));
        }
        if (this.hasErrors()) {
            displayTab = RESULTS_TAB;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {

        if (studyHasEmptyPlatformTypes()) {
            return "missingPlatformChannelType";
        }

        String returnValue = ERROR;

        if ("selectedTabSearchResults".equals(selectedAction)) {
            return (isExport()) ? "export" : SUCCESS;
        }

        // Check which user action is submitted
        if ("remove".equals(selectedAction)) {
            resetQueryResult();
            returnValue = removeRow();
        } else if ("addCriterionRow".equals(selectedAction)) {
            setQueryResult(null);
            returnValue = addCriterionRow();
        } else if (EXECUTE_QUERY.equals(selectedAction)
                || "loadExecute".equals(selectedAction)) {
            returnValue = executeQuery();
        } else if ("saveQuery".equals(selectedAction)) {
            clearAnalysisCache();
            returnValue = saveQuery();
        } else if ("saveAsQuery".equals(selectedAction)) {
            clearAnalysisCache();
            returnValue = saveAsQuery();
        } else if ("deleteQuery".equals(selectedAction)) {
            clearAnalysisCache();
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
            returnValue = createNewQuery();
        } else if ("forwardToNcia".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = forwardToNciaBasket();
        } else if ("viewIGV".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = viewIGV();
        } else if ("viewHeatmap".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = viewHeatmap();
        } else if ("selectAll".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            getDisplayableWorkspace().getQueryResult().setSelectAll(true);
            returnValue = toggleCheckboxSelections();
        } else if ("selectNone".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            getDisplayableWorkspace().getQueryResult().setSelectAll(false);
            returnValue = toggleCheckboxSelections();
        } else if ("selectAllSubject".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            getDisplayableWorkspace().getQueryResult().setSelectAllSubject(true);
            returnValue = toggleSubjectCheckboxSelections();
        } else if ("selectNoneSubject".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            getDisplayableWorkspace().getQueryResult().setSelectAllSubject(false);
            returnValue = toggleSubjectCheckboxSelections();
        } else if ("retrieveDicomImages".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = createDicomJob();
        } else if ("loadQuery".equals(selectedAction)) {
            returnValue = executeQuery();
            displayTab = CRITERIA_TAB;
        } else if ("saveSubjectList".equals(selectedAction)) {
            displayTab = RESULTS_TAB;
            returnValue = saveSubjectList();
        } else if ("loadSubjectListExecute".equals(selectedAction)) {
            returnValue = loadSubjectListExecute(false);
        } else if ("loadGeneListExecute".equals(selectedAction)) {
            returnValue = loadGeneListExecute(false);
        } else if ("loadGlobalSubjectListExecute".equals(selectedAction)) {
            returnValue = loadSubjectListExecute(true);
        } else if ("loadGlobalGeneListExecute".equals(selectedAction)) {
            returnValue = loadGeneListExecute(true);
        } else if ("sortGenomicResult".equals(selectedAction)) {
            returnValue = sortGenomicResult();
        } else {
            addActionError(getText("struts.messages.error.invalid.action", getArgs(selectedAction)));
            returnValue = ERROR;
        }
        return returnValue;
    }

    private boolean studyHasEmptyPlatformTypes() {
        for (Platform platform : getWorkspaceService().retrievePlatformsInStudy(getStudy())) {
            if (Status.LOADED.equals(platform.getPlatformConfiguration().getStatus())
                    && (platform.getPlatformConfiguration().getPlatformType() == null
                            || platform.getPlatformConfiguration().getPlatformChannelType() == null)) {
                setActionError();
                return true;
            }
        }
        return false;
    }

    private void setActionError() {
        addActionError((isStudyManager())
                ? getText("struts.messages.error.query.platform.incomplete.manager")
                : getText("struts.messages.error.query.platform.incomplete.investigator"));
    }

    private String createNewQuery() {
        getQueryForm().createQuery(getStudySubscription(), geneExpressionPlatformsInStudy,
                copyNumberPlatformsInStudy, copyNumberPlatformsWithCghCallInStudy);
        resetQueryResult();
        return SUCCESS;
    }

    private String loadGeneListExecute(boolean isGlobal) {
        createNewQuery();
        if (getStudy().hasGenomicDataSources()) {
            loadGeneList(isGlobal);
            return executeQuery();
        }
        addActionError(getText("struts.messages.error.study.has.no.expression.data", getArgs("query")));
        displayTab = CRITERIA_TAB;
        return ERROR;
    }

    private void loadGeneList(boolean isGlobal) {
        getQueryForm().getCriteriaGroup().setCriterionTypeName(CriterionRowTypeEnum.GENE_EXPRESSION.getValue());
        addCriterionRow();
        AbstractCriterionRow criterionRow = getQueryForm().getCriteriaGroup().getRows().get(0);
        criterionRow.setFieldName(GeneNameCriterionWrapper.FIELD_NAME);
        updateCriteria();
        ((TextFieldParameter) criterionRow.getParameters().get(0)).setGeneSymbol(true);
        ((TextFieldParameter) criterionRow.getParameters().get(0)).setValue(getGeneSymbols(isGlobal));
        if (geneExpressionPlatformsInStudy.size() > 1) { // It has a platform select list.
            ((SelectListParameter<?>) criterionRow.getParameters().get(1)).
                    setValue(geneExpressionPlatformsInStudy.iterator().next());
        }
        getQueryForm().getResultConfiguration().setResultType(ResultTypeEnum.GENE_EXPRESSION.getValue());
        getQueryForm().getResultConfiguration().setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE.getValue());
        if (isGlobal) {
            setOpenGlobalGeneListName(geneListName);
            setOpenGeneListName(null);
        } else {
            setOpenGeneListName(geneListName);
            setOpenGlobalGeneListName(null);
        }
    }

    private String getGeneSymbols(boolean isGlobal) {
        StringBuffer geneSymbols = new StringBuffer();
        GeneList geneList = (isGlobal)
            ? getStudySubscription().getStudy().getStudyConfiguration().getGeneList(geneListName)
            : getStudySubscription().getGeneList(geneListName);
        for (Gene gene : geneList.getGeneCollection()) {
            if (geneSymbols.length() > 0) {
                geneSymbols.append(',');
            }
            geneSymbols.append(gene.getSymbol());
        }
        return geneSymbols.toString();
    }

    private String loadSubjectListExecute(boolean isGlobal) {
        createNewQuery();
        loadSubjectList(isGlobal);
        getQueryForm().getResultConfiguration().selectAllValues();
        return executeQuery();
    }

    @SuppressWarnings("unchecked")
    private void loadSubjectList(boolean isGlobal) {
        getQueryForm().getCriteriaGroup().setCriterionTypeName(CriterionRowTypeEnum.SAVED_LIST.getValue());
        addCriterionRow();
        AbstractCriterionRow criterionRow = getQueryForm().getCriteriaGroup().getRows().get(0);
        criterionRow.setFieldName((isGlobal)
                ? SubjectListCriterionWrapper.SUBJECT_GLOBAL_LIST_FIELD_NAME
                : SubjectListCriterionWrapper.SUBJECT_LIST_FIELD_NAME);
        updateCriteria();
        ((MultiSelectParameter) criterionRow.getParameters().get(0)).setValues(new String[]{subjectListName});
        if (isGlobal) {
            setOpenGlobalSubjectListName(subjectListName);
            setOpenSubjectListName(null);
        } else {
            setOpenSubjectListName(subjectListName);
            setOpenGlobalSubjectListName(null);
        }
    }

    private String exportGenomicResults() {
        File file = queryManagementService.createCsvFileFromGenomicResults(getGenomicDataQueryResult());
        DownloadableFile downloadableFile = new DownloadableFile();
        downloadableFile.setPath(file.getAbsolutePath());
        downloadableFile.setContentType(DownloadableFile.CONTENT_TYPE_CSV);
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
     * Selects all subject checkboxes.
     * @return the Struts result.
     */
    public String toggleSubjectCheckboxSelections() {
        if (getQueryResult() != null
                && getQueryResult().getRows() != null
                && !getQueryResult().getRows().isEmpty()) {
            for (DisplayableResultRow row : getQueryResult().getRows()) {
                row.setSelectedSubject(getDisplayableWorkspace().getQueryResult().getSelectAllSubject());
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
     * View IGV.
     * @return the Struts result.
     */
    public String viewIGV() {
        IGVParameters igvParameters = new IGVParameters();
        igvParameters.setQuery(getQuery());
        igvParameters.setStudySubscription(getStudySubscription());
        igvParameters.setSessionId(ServletActionContext.getRequest().getRequestedSessionId());
        igvParameters.setUrlPrefix(SessionHelper.getIgvSessionUrl());
        igvParameters.setUseCGHCall(CopyNumberCriterionTypeEnum.CALLS_VALUE.equals(
                getQuery().getCopyNumberCriterionType()));
        getDisplayableWorkspace().setIgvParameters(igvParameters);
        return "viewIGV";
    }

    /**
     * View IGV.
     * @return the Struts result.
     */
    public String viewHeatmap() {
        HeatmapParameters heatmapParameters = new HeatmapParameters();
        heatmapParameters.setQuery(getQuery());
        heatmapParameters.setStudySubscription(getStudySubscription());
        heatmapParameters.setSessionId(ServletActionContext.getRequest().getRequestedSessionId());
        heatmapParameters.setUrlPrefix(SessionHelper.getHeatmapSessionUrl());
        heatmapParameters.setHeatmapJarUrlPrefix(SessionHelper.getCaIntegratorCommonUrl());
        heatmapParameters.setLargeBinsFile(SessionHelper.getHeatmapLargeBinsFile(context));
        heatmapParameters.setSmallBinsFile(SessionHelper.getHeatmapSmallBinsFile(context));
        heatmapParameters.setUseCGHCall(CopyNumberCriterionTypeEnum.CALLS_VALUE.equals(
                getQuery().getCopyNumberCriterionType()));
        getDisplayableWorkspace().setHeatmapParameters(heatmapParameters);
        return "viewHeatmap";
    }

    /**
     * Creates a dicom job and runs.
     * @return the Struts result.
     */
    public String createDicomJob() {
        if (getDisplayableWorkspace().getDicomJob() != null
                && getDisplayableWorkspace().getDicomJob().isCurrentlyRunning()) {
            addActionError(getText("struts.messages.error.query.dicom.already.running"));
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

    private Set<String> retrieveSelectedSubjects() {
        Set <String> selectedIdentifiers = new HashSet<String>();
        if (getQueryResult() != null
            && getQueryResult().getRows() != null
            && !getQueryResult().getRows().isEmpty()) {
            for (DisplayableResultRow row : getQueryResult().getRows()) {
                if (row.isSelectedSubject()) {
                    selectedIdentifiers.add(row.getSubjectAssignment().getIdentifier());
                }
            }
        }
        return selectedIdentifiers;
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
        getQueryForm().setGenomicPreviousSorting("None");
        getQueryForm().setGenomicSortingOrder(-1);
        ensureQueryIsLoaded();
        updateSorting();
        resetQueryResult();
        return runQuery();
    }

    private String runQuery() {
        try {
            if (ResultTypeEnum.GENE_EXPRESSION.getValue().equals(
                    getQueryForm().getResultConfiguration().getResultType())) {
                setGenomicDataQueryResult(runGenomicQuery());
            } else if (ResultTypeEnum.COPY_NUMBER.getValue().equals(
                            getQueryForm().getResultConfiguration().getResultType())) {
                setCopyNumberQueryResult(new DisplayableCopyNumberQueryResult(runGenomicQuery(),
                        getQuery().getOrientation()));
            } else {
                QueryResult result = queryManagementService.execute(getQueryForm().getQuery());
                loadAllImages(result);
                setQueryResult(new DisplayableQueryResult(result));
            }
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            displayTab = CRITERIA_TAB;
            return ERROR;
        }
        displayTab = RESULTS_TAB;
        return SUCCESS;
    }

    private GenomicDataQueryResult runGenomicQuery() throws InvalidCriterionException {
        GenomicDataQueryResult genomicResult;
        genomicResult = queryManagementService.executeGenomicDataQuery(getQueryForm().getQuery());
        if (genomicResult.getRowCollection() != null) {
            Collections.sort(genomicResult.getRowCollection(), new GenomicDataResultRowComparator());
        }
        return genomicResult;
    }

    private void ensureQueryIsLoaded() {
        if (getQueryForm().getQuery() == null) {
            getQueryForm().setQuery(getQueryById(), geneExpressionPlatformsInStudy, copyNumberPlatformsInStudy,
                    copyNumberPlatformsWithCghCallInStudy);
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
        query.setLastModifiedDate(new Date());
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
            addActionError(getText("struts.messages.error.query.cloning"));
            return ERROR;
        }
        query.setLastModifiedDate(new Date());
        queryManagementService.save(query);
        setQueryId(query.getId());
        getStudySubscription().getQueryCollection().add(query);
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        getQueryForm().setQuery(getQueryById(), geneExpressionPlatformsInStudy, copyNumberPlatformsInStudy,
                copyNumberPlatformsWithCghCallInStudy);
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
        getQueryForm().createQuery(getStudySubscription(), geneExpressionPlatformsInStudy, copyNumberPlatformsInStudy,
                copyNumberPlatformsWithCghCallInStudy);
        resetQueryResult();
        return SUCCESS;
    }

    /**
     * Save the subject list.
     *
     * @return the Struts result.
     */
    public String saveSubjectList() {
        SubjectList subjectList = new SubjectList();
        subjectList.setName(getSubjectListName());
        subjectList.setDescription(getSubjectListDescription());
        subjectList.setLastModifiedDate(new Date());
        if (isSubjectListVisibleToOthers()) {
            subjectList.setVisibility(Visibility.GLOBAL);
            subjectList.setStudyConfiguration(getStudySubscription().getStudy().getStudyConfiguration());
        } else {
            subjectList.setVisibility(Visibility.PRIVATE);
            subjectList.setSubscription(getStudySubscription());
        }
        try {
            getWorkspaceService().createSubjectList(subjectList, retrieveSelectedSubjects());
        } catch (ValidationException e) {
            addActionError(e.getMessage());
            return ERROR;
        }
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
        if (getCopyNumberQueryResult() != null) {
            getCopyNumberQueryResult().setPageSize(pageSize);
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
       return dataSource.getServerProfile().getWebUrl() + "/externalDataBasketDisplay.jsf";
    }


    /**
     * @return the subjectListName
     */
    public String getSubjectListName() {
        return subjectListName;
    }


    /**
     * @param subjectListName the subjectListName to set
     */
    public void setSubjectListName(String subjectListName) {
        this.subjectListName = subjectListName.trim();
    }


    /**
     * @return the subjectListDescription
     */
    public String getSubjectListDescription() {
        return subjectListDescription;
    }


    /**
     * @param subjectListDescription the subjectListDescription to set
     */
    public void setSubjectListDescription(String subjectListDescription) {
        this.subjectListDescription = subjectListDescription.trim();
    }


    /**
     * @return the subjectListVisibleToOthers
     */
    public boolean isSubjectListVisibleToOthers() {
        return subjectListVisibleToOthers;
    }

    /**
     * @param subjectListVisibleToOthers the subjectListVisibleToOthers to set
     */
    public void setSubjectListVisibleToOthers(boolean subjectListVisibleToOthers) {
        this.subjectListVisibleToOthers = subjectListVisibleToOthers;
    }

    /**
     * @return the geneListName
     */
    public String getGeneListName() {
        return geneListName;
    }


    /**
     * @param geneListName the geneListName to set
     */
    public void setGeneListName(String geneListName) {
        this.geneListName = geneListName;
    }

    private String sortGenomicResult() {
        displayTab = RESULTS_TAB;
        if (getQueryForm().getGenomicPreviousSorting().equals(genomicSortingType + genomicSortingIndex)) {
            getQueryForm().reverseGenomicSortingOrder();
        } else {
            getQueryForm().setGenomicSortingOrder(-1);
        }
        getQueryForm().setGenomicPreviousSorting(genomicSortingType + genomicSortingIndex);
        return ("column".equals(genomicSortingType)) ? sortGenomicColumn() : sortGenomicRow();
    }

    private String sortGenomicColumn() {
        for (GenomicDataResultColumn genomicDataResultColumn : getGenomicDataQueryResult().getColumnCollection()) {
            genomicDataResultColumn.setSortedValue(
                    genomicDataResultColumn.getValues().get(Integer.valueOf(genomicSortingIndex)).getValue());
        }
        Collections.sort(getGenomicDataQueryResult().getColumnCollection(),
                new GenomicDataResultComparator(getQueryForm().getGenomicSortingOrder()));
        return SUCCESS;
    }

    private String sortGenomicRow() {
        for (GenomicDataResultRow genomicDataResultRow : getGenomicDataQueryResult().getRowCollection()) {
            genomicDataResultRow.setSortedValue(
                    genomicDataResultRow.getValues().get(Integer.valueOf(genomicSortingIndex)).getValue());
        }
        Collections.sort(getGenomicDataQueryResult().getRowCollection(),
                new GenomicDataResultComparator(getQueryForm().getGenomicSortingOrder()));
        return SUCCESS;
    }

    /**
     * @return Return the StudyConfiguration for jUnit test.
     */
    public StudyConfiguration getStudyConfiguration() {
        return getStudy().getStudyConfiguration();
    }

    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;

    }
}
