/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.analysis.KMGeneExpressionBasedParameters;
import gov.nih.nci.caintegrator.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action dealing with Kaplan-Meier Gene Expression based plotting.
 */
@Component("kmPlotGeneExpressionBasedAction")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
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
        kmPlotParameters.setExpressionType(getForm().getExpressionType());
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
