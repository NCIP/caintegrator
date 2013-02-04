/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotAnnotationBasedParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * Action dealing with Gene Expression Annotation Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class GEPlotAnnotationBasedAction extends AbstractGeneExpressionAction {

    private static final long serialVersionUID = 1L;
    private static final String ANNOTATION_MEAN_PLOT_URL = "retrieveAnnotationGEPlot_mean.action?";
    private static final String ANNOTATION_MEDIAN_PLOT_URL = "retrieveAnnotationGEPlot_median.action?";
    private static final String ANNOTATION_LOG2_PLOT_URL = "retrieveAnnotationGEPlot_log2.action?";
    private static final String ANNOTATION_BW_PLOT_URL = "retrieveAnnotationGEPlot_bw.action?";
    private GEPlotAnnotationBasedParameters plotParameters = new GEPlotAnnotationBasedParameters();
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(ANNOTATION_TAB);
        retrieveFormValues();
        refreshObjectInstances();
    }
    
    
    private void retrieveFormValues() {
        plotParameters.setGeneSymbol(getGePlotForm().getAnnotationBasedForm().getGeneSymbol());
        plotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        plotParameters.setAddControlSamplesGroup(getForm().isAddControlSamplesGroup());
        plotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
        plotParameters.setReporterType(ReporterTypeEnum.
                        getByValue(getGePlotForm().getAnnotationBasedForm().getReporterType()));
        if (getForm().getSelectedAnnotationId() != null 
                && !StringUtils.isEmpty(getForm().getSelectedAnnotationId())) {
                plotParameters.getSelectedAnnotation().setId(
                       Long.valueOf(getForm().getSelectedAnnotationId()));
           }
       retrieveFormSelectedValues();
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks and switch statement
    private void retrieveFormSelectedValues() {
        if (!getForm().getSelectedValuesIds().isEmpty()) {
            refreshSelectedAnnotationInstance();
            plotParameters.getSelectedValues().clear();
            
            for (String id : getForm().getSelectedValuesIds()) {
                PermissibleValue value = new PermissibleValue();
                value.setId(Long.valueOf(id));
                plotParameters.getSelectedValues().add(value);
            }
        }
    }
    
    private void refreshObjectInstances() {
        refreshSelectedAnnotationInstance();
        refreshSelectedAnnotationValuesInstance();
    }
    
    private void refreshSelectedAnnotationInstance() {
        if (plotParameters.getSelectedAnnotation().getId() != null) {
            plotParameters.setSelectedAnnotation(getStudyManagementService().
                        getRefreshedStudyEntity(plotParameters.getSelectedAnnotation()));
        }
    }
    
    private void refreshSelectedAnnotationValuesInstance() {
        if (!plotParameters.getSelectedValues().isEmpty()) {
            Collection <PermissibleValue> newValues = new HashSet<PermissibleValue>();
            for (PermissibleValue value : plotParameters.getSelectedValues()) {
                PermissibleValue newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            plotParameters.getSelectedValues().clear();
            plotParameters.getSelectedValues().addAll(newValues);
        }
    }
    
    /**
     * Clears all input values and geneExpression plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        clearAnnotationBasedGePlot();
        getForm().clear();
        plotParameters.clear();
        return SUCCESS;
    }

    private void clearAnnotationBasedGePlot() {
        SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    public String input() {
        setDisplayTab(ANNOTATION_TAB);
        return SUCCESS;
    }
    
    /**
     * This is used to update the Annotation Definitions on the GE input form when
     * a user selects a valid Annotation Type.
     * @return Struts return value.
     */
    public String updateAnnotationDefinitions() {
        if (!validateAnnotationType()) {
            return INPUT;
        }
        clearAnnotationBasedGePlot();
        getForm().clearPermissibleValues();
        if (!loadAnnotationDefinitions()) {
            return INPUT;
        }
        return SUCCESS;
    }

    private boolean validateAnnotationType() {
        try {
            if (getForm().getAnnotationTypeSelection() == null) {
                addActionError("Annotation Type unknown for selected annotation.");
                return false;
            }
            EntityTypeEnum.checkType(getForm().getAnnotationTypeSelection());
        } catch (IllegalArgumentException e) {
            addActionError("Annotation Type unknown for selected annotation.");
            return false;
        }
        return true;
    }


    private boolean loadAnnotationDefinitions() {
        if (getForm().getAnnotationTypeSelection() == null 
                || getForm().getAnnotationTypeSelection().equals("invalidSelection")) {
            addActionError("Must select Annotation Type first");
            return false;
        }
        EntityTypeEnum annotationEntityType = 
            EntityTypeEnum.getByValue(getForm().getAnnotationTypeSelection());
        getForm().setAnnotationDefinitions(new HashMap<String, AnnotationDefinition>());
        Collection<AnnotationDefinition> studyDefinitionCollection = new HashSet<AnnotationDefinition>();
        switch (annotationEntityType) {
        case SUBJECT:
            studyDefinitionCollection = getStudy().getStudyConfiguration()
                .getVisibleSubjectAnnotationCollection();
            break;
        case IMAGESERIES:
            studyDefinitionCollection = getStudy().getStudyConfiguration()
                .getVisibleImageSeriesAnnotationCollection();
            break;
        case SAMPLE:
            studyDefinitionCollection = getStudy().getSampleAnnotationCollection();
            break;
        default:
            addActionError("Unknown Annotation Type");
            return false;
        }
        loadValidAnnotationDefinitions(studyDefinitionCollection);
        return true;
    }
    
    private void loadValidAnnotationDefinitions(Collection<AnnotationDefinition> definitions) {
        for (AnnotationDefinition definition : definitions) {
            if (definition.getPermissibleValueCollection() != null 
                && !definition.getPermissibleValueCollection().isEmpty()) {
                getForm().getAnnotationDefinitions().put(definition.getId().toString(), definition);
            }
        }
    }
    
    /**
     * This is used to update the Permissible Values on the Kaplan-Meier input form when
     * a user selects a valid AnnotationDefinition.
     * @return Struts return value.
     */
    public String updatePermissibleValues() {
        if (isPermissibleValuesNeedUpdate()) {
            loadAnnotationDefinitions();
            if (plotParameters.getSelectedAnnotation() == null) {
                addActionError("Please select a valid annotation");
                return INPUT;
            }
            getForm().clearPermissibleValues();
            clearAnnotationBasedGePlot();
            loadPermissibleValues();
        }
        return SUCCESS;
    }

    private void loadPermissibleValues() {
        for (PermissibleValue value 
              : plotParameters.getSelectedAnnotation().getPermissibleValueCollection()) {
            getForm().getPermissibleValues().put(value.getId().toString(), 
                    value.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        setPermissibleValuesNeedUpdate(false);
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            loadAnnotationDefinitions();
            loadPermissibleValues();
            if (plotParameters.validate()) {
                try {
                    plotParameters.setEntityType(EntityTypeEnum.getByValue(getForm().getAnnotationTypeSelection()));
                    GeneExpressionPlotGroup plots = getAnalysisService().
                                    createGeneExpressionPlot(getStudySubscription(), plotParameters);
                    SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, plots);
                } catch (ControlSamplesNotMappedException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
                    addActionError("Group selected in step 5 invalid, control samples must all be mapped to patients: "
                            + e.getMessage());
                } catch (InvalidCriterionException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
                    addActionError(e.getMessage());
                } catch (GenesNotFoundInStudyException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
                    addActionError(e.getMessage());
                }
            }
            setCreatePlotRunning(false);
        }
    }
    
    /**
     * This is set only when the dropdown on the JSP is selecting an annotation definition, so we know that
     * a permissible value change needs to occur.
     * @param needUpdate T/F value.
     */
    public void setPermissibleValuesNeedUpdate(boolean needUpdate) {
        getForm().setPermissibleValuesNeedUpdate(needUpdate);
    }
    
    private boolean isPermissibleValuesNeedUpdate() {
        return getForm().isPermissibleValuesNeedUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        if (getForm().getSelectedAnnotationId() != null 
            && !"-1".equals(getForm().getSelectedAnnotationId())
            && getForm().getAnnotationTypeSelection() != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMeanPlotUrl() {
        return ANNOTATION_MEAN_PLOT_URL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMedianPlotUrl() {
        return ANNOTATION_MEDIAN_PLOT_URL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLog2PlotUrl() {
        return ANNOTATION_LOG2_PLOT_URL;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBoxWhiskerPlotUrl() {
        return ANNOTATION_BW_PLOT_URL;
    }

    /**
     * @return
     */
    private GEPlotAnnotationBasedActionForm getForm() {
        return getGePlotForm().getAnnotationBasedForm();
    }


    /**
     * @return the plotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public GEPlotAnnotationBasedParameters getPlotParameters() {
        return plotParameters;
    }


    /**
     * @param plotParameters the plotParameters to set
     */
    public void setPlotParameters(GEPlotAnnotationBasedParameters plotParameters) {
        this.plotParameters = plotParameters;
    }

}
