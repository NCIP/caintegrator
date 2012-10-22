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
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.ExpressionTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.KMGeneExpressionBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Action dealing with Kaplan-Meier Gene Expression based plotting.
 */
public class KMPlotGeneExpressionBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;
    private static final String GENE_EXPRESSION_PLOT_URL = "/" + SessionHelper.WAR_CONTEXT_NAME
            + "/retrieveGeneExpressionKMPlot.action?";
    private KMGeneExpressionBasedParameters kmPlotParameters = new KMGeneExpressionBasedParameters();
    private List<String> platformsInStudy = new ArrayList<String>();


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(GENE_EXPRESSION_TAB);
        retrieveFormValues();
        checkForControlValues();
        platformsInStudy = new ArrayList<String>(
                getQueryManagementService().retrieveGeneExpressionPlatformsForStudy(getStudy()));
        Collections.sort(platformsInStudy);
        if (platformsInStudy.size() > 1) {
            getKmPlotParameters().setMultiplePlatformsInStudy(true);
        }
    }

    private void checkForControlValues() {
        if (!getStudy().getStudyConfiguration().hasControlSamples()) {
            getForm().setNoControlsInStudy();
        }
    }

    private void retrieveFormValues() {
        if (NumberUtils.isNumber(getForm().getOverexpressedNumber())) {
            kmPlotParameters.setOverValue(Float.valueOf(getForm().getOverexpressedNumber()));
        }
        if (NumberUtils.isNumber(getForm().getUnderexpressedNumber())) {
            kmPlotParameters.setUnderValue(Float.valueOf(getForm().getUnderexpressedNumber()));
        }
        kmPlotParameters.setGeneSymbol(getForm().getGeneSymbol());
        kmPlotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
        kmPlotParameters.setExpressionType(ExpressionTypeEnum.getByValue(getForm().getExpressionType()));
        kmPlotParameters.setPlatformName(getForm().getPlatformName());
    }

    /**
     * @return
     */
    private KMPlotGeneExpressionBasedActionForm getForm() {
        return getKmPlotForm().getGeneExpressionBasedForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        return true;
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    @Override
    public String input() {
        if (!getForm().isInitialized()) {
            getForm().clear();
        }
        setDisplayTab(GENE_EXPRESSION_TAB);
        return SUCCESS;
    }

    /**
     * Clears all input values and km plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        if (isResetSelected()) {
            clearGeneExpressionBasedKmPlot();
            getForm().clear();
            kmPlotParameters.clear();
        }
        return SUCCESS;
    }

    /**
     * Updates the control sample sets based on the platform selected.
     * @return struts return value.
     */
    public String updateControlSampleSets() {
        getForm().getControlSampleSets().clear();
        if (StringUtils.isBlank(getForm().getPlatformName())) {
            addActionError(getText("struts.messages.error.select.valid.item", getArgs("platform")));
            return INPUT;
        }
        getForm().setControlSampleSets(getStudy().getStudyConfiguration().getControlSampleSetNames(
                getForm().getPlatformName()));
        clearGeneExpressionBasedKmPlot();
        return SUCCESS;
    }

    private void clearGeneExpressionBasedKmPlot() {
        SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            clearGeneExpressionBasedKmPlot();
            if (kmPlotParameters.validate()) {
                try {
                    KMPlot plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
                    SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, plot);
                } catch (Exception e) {
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
    public String getPlotUrl() {
        return GENE_EXPRESSION_PLOT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, SortedMap<String, String>> getAllStringPValues() {
        if (SessionHelper.getGeneExpressionBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getGeneExpressionBasedKmPlot());
        }
        return new TreeMap<String, SortedMap<String, String>>();
    }

    /**
     *
     * @return list of genesNotFound.
     */
    public List<String> getGenesNotFound() {
        if (SessionHelper.getGeneExpressionBasedKmPlot() != null) {
            return SessionHelper.getGeneExpressionBasedKmPlot().getConfiguration().getGenesNotFound();
        }
        return new ArrayList<String>();
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
     * @return the kmPlotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public KMGeneExpressionBasedParameters getKmPlotParameters() {
        return kmPlotParameters;
    }

    /**
     * @param kmPlotParameters the kmPlotParameters to set
     */
    public void setKmPlotParameters(KMGeneExpressionBasedParameters kmPlotParameters) {
        this.kmPlotParameters = kmPlotParameters;
    }

    /**
     * @return the platformsInStudy
     */
    public List<String> getPlatformsInStudy() {
        return platformsInStudy;
    }

    /**
     * Determines if study has multiple platforms.
     * @return T/F value if study has multiple platforms.
     */
    public boolean isStudyHasMultiplePlatforms() {
        return platformsInStudy.size() > 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getControlSampleSets() {
        return isStudyHasMultiplePlatforms() ? getForm().getControlSampleSets()
                : super.getControlSampleSets();
    }

    /**
     *
     * @return prefix on JSP.
     */
    public String getOverValueTextPrefix() {
        return getForm().isFoldChangeType() ? "Overexpressed >= " : "Above Expression Level ";
    }

    /**
     *
     * @return prefix on JSP.
     */
    public String getUnderValueTextPrefix() {
        return getForm().isFoldChangeType() ? "Underexpressed >= " : "Below Expression Level ";
    }

    /**
     *
     * @return suffix on JSP.
     */
    public String getValueSuffix() {
        return getForm().isFoldChangeType() ? "fold" : "";
    }

    /**
     *
     * @return JSP value for display of controls.
     */
    public String getControlsDisplayStyle() {
        return getForm().isFoldChangeType() ? "" : "display: none";
    }

}
