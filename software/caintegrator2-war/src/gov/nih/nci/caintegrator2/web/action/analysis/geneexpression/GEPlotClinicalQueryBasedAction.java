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
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotClinicalQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.web.Cai2WebUtil;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.analysis.DisplayableQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

/**
 * Action dealing with Gene Expression Clinical Query Based plotting.
 */
public class GEPlotClinicalQueryBasedAction extends AbstractGeneExpressionAction {

    private static final long serialVersionUID = 1L;
    private static final String CLINICAL_QUERY_MEAN_PLOT_URL = "retrieveClinicalQueryGEPlot_mean.action?";
    private static final String CLINICAL_QUERY_MEDIAN_PLOT_URL = "retrieveClinicalQueryGEPlot_median.action?";
    private static final String CLINICAL_QUERY_LOG2_PLOT_URL = "retrieveClinicalQueryGEPlot_log2.action?";
    private static final String CLINICAL_QUERY_BW_PLOT_URL = "retrieveClinicalQueryGEPlot_bw.action?";
    private final GEPlotClinicalQueryBasedParameters plotParameters = new GEPlotClinicalQueryBasedParameters();

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(CLINICAL_QUERY_TAB);
        getForm().setDisplayableQueries(
                Cai2WebUtil.retrieveDisplayableQueries(getStudySubscription(), getQueryManagementService(), false));
        getForm().getDisplayableQueryMap().clear();
        for (DisplayableQuery displayableQuery : getForm().getDisplayableQueries()) {
            getForm().getDisplayableQueryMap().put(displayableQuery.getDisplayName(), displayableQuery);
        }
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
        if (isStudyHasMultiplePlatforms()) {
            plotParameters.setMultiplePlatformsInStudy(true);
        }
    }


    private void retrieveFormValues() {
        plotParameters.setGeneSymbol(getForm().getGeneSymbol());
        plotParameters.setReporterType(ReporterTypeEnum.getByValue(getForm().getReporterType()));
        plotParameters.setExclusiveGroups(getForm().isExclusiveGroups());
        plotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        plotParameters.setAddControlSamplesGroup(getForm().isAddControlSamplesGroup());
        plotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
        plotParameters.setPlatformName(getForm().getPlatformName());
        if (!getForm().getSelectedQueryNames().isEmpty()) {
            plotParameters.getQueries().clear();
            for (String name : getForm().getSelectedQueryNames()) {
                plotParameters.getQueries().add(getForm().getDisplayableQueryMap().get(name).getQuery());
            }
        }
    }

    private void refreshObjectInstances() {
        if (!plotParameters.getQueries().isEmpty()) {
            List <Query> newValues = new ArrayList<Query>();
            for (Query value : plotParameters.getQueries()) {
                if (!value.isSubjectListQuery()) {
                    Query newValue = getQueryManagementService().getRefreshedEntity(value);
                    newValues.add(newValue);
                } else {
                    newValues.add(value);
                }
            }
            plotParameters.getQueries().clear();
            plotParameters.getQueries().addAll(newValues);
        }
    }

    private void populateQueries() {
        initialize(getForm().getDisplayableQueries());
        loadSelectedQueries(getForm().getDisplayableQueries());
    }

    private void initialize(List<DisplayableQuery> displayableQueries) {
        if (!displayableQueries.isEmpty()
            && getForm().getSelectedQueries().isEmpty()
            && getForm().getUnselectedQueries().isEmpty()) {
            getForm().setUnselectedQueries(new TreeMap<String, DisplayableQuery>());
            addQueriesToForm(displayableQueries);
        }
    }

    private void addQueriesToForm(List<DisplayableQuery> displayableQueries) {
        for (DisplayableQuery query : displayableQueries) {
            getForm().getUnselectedQueries().put(query.getDisplayName(), query);
        }
    }

    private void loadSelectedQueries(List<DisplayableQuery> displayableQueries) {
        if (!plotParameters.getQueries().isEmpty()) {
            getForm().getSelectedQueries().clear();
            Set<Query> usedQueries = new HashSet<Query>();
            for (Query query : plotParameters.getQueries()) {
                DisplayableQuery displayableQuery = getForm().getDisplayableQueryMap().get(DisplayableQuery
                        .getDisplayableQueryName(query));
                getForm().getSelectedQueries().put(displayableQuery.getDisplayName(), displayableQuery);
                usedQueries.add(query);
            }
            loadAvailableQueries(displayableQueries, usedQueries);
        }
    }

    private void loadAvailableQueries(List<DisplayableQuery> displayableQueries, Set<Query> usedQueries) {
        getForm().getUnselectedQueries().clear();
        for (DisplayableQuery displayableQuery : displayableQueries) {
            if (!usedQueries.contains(displayableQuery.getQuery())) {
                getForm().getUnselectedQueries().put(displayableQuery.getDisplayName(), displayableQuery);
            }
        }
    }

    /**
     * Updates the control sample sets based on the platform selected.
     * @return struts return value.
     */
    public String updateControlSampleSets() {
        getForm().getControlSampleSets().clear();
        if (StringUtils.isBlank(plotParameters.getPlatformName())) {
            addActionError(getText("struts.messages.error.select.valid.platform"));
            return INPUT;
        }
        getForm().setControlSampleSets(getStudy().getStudyConfiguration().getControlSampleSetNames(
                plotParameters.getPlatformName()));
        clearClinicalQueryBasedGePlot();
        return SUCCESS;
    }

    /**

    /**
     * Clears all input values and ge plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        if (isResetSelected()) {
            clearClinicalQueryBasedGePlot();
            getForm().clear();
            plotParameters.clear();
        }
        return SUCCESS;
    }

    private void clearClinicalQueryBasedGePlot() {
        SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    @Override
    public String input() {
        setDisplayTab(CLINICAL_QUERY_TAB);
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            clearClinicalQueryBasedGePlot();
            if (plotParameters.validate()) {
                try {
                    GeneExpressionPlotGroup plots = getAnalysisService().
                            createGeneExpressionPlot(getStudySubscription(), plotParameters);
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, plots);
                } catch (ControlSamplesNotMappedException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
                    addActionError(getText("struts.messages.error.geplot.selected.controls.not.mapped.to.patients",
                            getArgs("6", e.getMessage())));
                } catch (Exception e) {
                    SessionHelper.setGePlots(PlotTypeEnum.CLINICAL_QUERY_BASED, null);
                    addActionError(e.getMessage());
                }
            }
            setCreatePlotRunning(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMeanPlotUrl() {
        return CLINICAL_QUERY_MEAN_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMedianPlotUrl() {
        return CLINICAL_QUERY_MEDIAN_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLog2PlotUrl() {
        return CLINICAL_QUERY_LOG2_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBoxWhiskerPlotUrl() {
        return CLINICAL_QUERY_BW_PLOT_URL;
    }


    /**
     * @return
     */
    private GEPlotClinicalQueryBasedActionForm getForm() {
        return getGePlotForm().getClinicalQueryBasedForm();
    }

    /**
     * @return the plotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public GEPlotClinicalQueryBasedParameters getPlotParameters() {
        return plotParameters;
    }

    /**
     * This is set only when the reset button on the JSP is pushed, so we know that a reset needs to occur.
     * @param resetSelected T/F value.
     */
    public void setResetSelected(boolean resetSelected) {
        getForm().setResetSelected(resetSelected);
    }

    private boolean isResetSelected() {
        return getForm().isResetSelected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getControlSampleSets() {
        return isStudyHasMultiplePlatforms() ? getForm().getControlSampleSets()
                : super.getControlSampleSets();
    }


}
