/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.KMGeneExpressionBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Action dealing with Kaplan-Meier Gene Expression based plotting.
 */
public class KMPlotGeneExpressionBasedAction extends AbstractKaplanMeierAction {
    
    private static final long serialVersionUID = 1L;
    private static final String GENE_EXPRESSION_PLOT_URL = "/caintegrator2/retrieveGeneExpressionKMPlot.action?";
    private KMGeneExpressionBasedParameters kmPlotParameters = new KMGeneExpressionBasedParameters();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(GENE_EXPRESSION_TAB);
        retrieveFormValues();
    }
    
    private void retrieveFormValues() {
        if (NumberUtils.isNumber(getForm().getOverexpressedNumber())
            && Float.valueOf(getForm().getOverexpressedNumber()) >= 1) {
            kmPlotParameters.setOverexpressedFoldChangeNumber(Float.valueOf(getForm().getOverexpressedNumber()));
        }
        if (NumberUtils.isNumber(getForm().getUnderexpressedNumber()) 
            && Float.valueOf(getForm().getUnderexpressedNumber()) >= 1) {
            kmPlotParameters.setUnderexpressedFoldChangeNumber(Float.valueOf(getForm().getUnderexpressedNumber()));
        }
        kmPlotParameters.setGeneSymbol(getForm().getGeneSymbol());
        kmPlotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
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
            SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, null);
            getForm().clear();
            kmPlotParameters.clear();
        }
        return SUCCESS;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            if (kmPlotParameters.validate()) {
                try {
                    KMPlot plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
                    SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, plot);
                } catch (InvalidCriterionException e) {
                    SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, null);
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
    public Map<String, Map<String, String>> getAllStringPValues() {
        if (SessionHelper.getGeneExpressionBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getGeneExpressionBasedKmPlot());
        }
        return new HashMap<String, Map<String, String>>();
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

}
