/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;


import gov.nih.nci.caintegrator.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.GEPlotAnnotationBasedParameters;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.security.SecurityHelper;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Action dealing with Gene Expression Annotation Based plotting.
 */
public class GEPlotAnnotationBasedAction extends AbstractGeneExpressionAction {

    private static final long serialVersionUID = 1L;
    private static final String ANNOTATION_MEAN_PLOT_URL = "retrieveAnnotationGEPlot_mean.action?";
    private static final String ANNOTATION_MEDIAN_PLOT_URL = "retrieveAnnotationGEPlot_median.action?";
    private static final String ANNOTATION_LOG2_PLOT_URL = "retrieveAnnotationGEPlot_log2.action?";
    private static final String ANNOTATION_BW_PLOT_URL = "retrieveAnnotationGEPlot_bw.action?";
    private GEPlotAnnotationBasedParameters plotParameters = new GEPlotAnnotationBasedParameters();
    private StudyManagementService studyManagementService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(ANNOTATION_TAB);
        retrieveFormValues();
        refreshObjectInstances();
        if (isStudyHasMultiplePlatforms()) {
            plotParameters.setMultiplePlatformsInStudy(true);
        }
    }


    private void retrieveFormValues() {
        plotParameters.setGeneSymbol(getGePlotForm().getAnnotationBasedForm().getGeneSymbol());
        plotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        plotParameters.setAddControlSamplesGroup(getForm().isAddControlSamplesGroup());
        plotParameters.setControlSampleSetName(getForm().getControlSampleSetName());
        plotParameters.setPlatformName(getForm().getPlatformName());
        plotParameters.setReporterType(ReporterTypeEnum.
                        getByValue(getGePlotForm().getAnnotationBasedForm().getReporterType()));
        if (getForm().getSelectedAnnotationId() != null
                && !StringUtils.isEmpty(getForm().getSelectedAnnotationId())) {
                plotParameters.getSelectedAnnotation().setId(
                       Long.valueOf(getForm().getSelectedAnnotationId()));
           }
       retrieveFormSelectedValues();
    }

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
            plotParameters.setSelectedAnnotation(getQueryManagementService().
                    getRefreshedEntity(plotParameters.getSelectedAnnotation()));
        }
    }

    private void refreshSelectedAnnotationValuesInstance() {
        if (!plotParameters.getSelectedValues().isEmpty()) {
            Collection <PermissibleValue> newValues = new HashSet<PermissibleValue>();
            for (PermissibleValue value : plotParameters.getSelectedValues()) {
                PermissibleValue newValue = getQueryManagementService().getRefreshedEntity(value);
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
        setCreatePlotRunning(false);
        return SUCCESS;
    }

    private void clearAnnotationBasedGePlot() {
        SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    @Override
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
        if (!validateAnnotationGroup()) {
            return INPUT;
        }
        clearAnnotationBasedGePlot();
        getForm().clearPermissibleValues();
        if (!loadAnnotationDefinitions()) {
            return INPUT;
        }
        return SUCCESS;
    }

    private boolean validateAnnotationGroup() {
        if (getForm().getAnnotationGroupSelection() == null) {
            addActionError(getText("struts.messages.error.select.valid.item", getArgs("annotation group")));
            return false;
        }
        if (getStudy().getAnnotationGroup(getForm().getAnnotationGroupSelection()) == null) {
            addActionError(getText("struts.messages.error.select.valid.item", getArgs("annotation group")));
            return false;
        }
        return true;
    }


    private boolean loadAnnotationDefinitions() {
        if (getForm().getAnnotationGroupSelection() == null
                || getForm().getAnnotationGroupSelection().equals("invalidSelection")) {
            addActionError(getText("struts.messages.error.must.select.annotation.group"));
            return false;
        }
        loadValidAnnotationFieldDescriptors();
        return true;
    }

    private void loadValidAnnotationFieldDescriptors() {
        Set<AnnotationFieldDescriptor> annotationFieldDescriptors = getAnnotationFieldsForUser();
        getForm().setAnnotationFieldDescriptors(new HashMap<String, AnnotationFieldDescriptor>());
        for (AnnotationFieldDescriptor annotationFieldDescriptor : annotationFieldDescriptors) {
            if (hasFieldValues(annotationFieldDescriptor)) {
                getForm().getAnnotationFieldDescriptors().put(annotationFieldDescriptor.getId().toString(),
                                                              annotationFieldDescriptor);
            }
        }
    }

    private boolean hasFieldValues(AnnotationFieldDescriptor annotationFieldDescriptor) {
        return annotationFieldDescriptor.getDefinition() != null
                && !annotationFieldDescriptor.getDefinition().getPermissibleValueCollection().isEmpty();
    }

    private Set<AnnotationFieldDescriptor> getAnnotationFieldsForUser() {
        AnnotationGroup annotationGroup = getStudy().getAnnotationGroup(getForm().getAnnotationGroupSelection());
        String username = SecurityHelper.getCurrentUsername();
        return studyManagementService.getVisibleAnnotationFieldDescriptorsForUser(annotationGroup, username);
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
                addActionError(getText("struts.messages.error.select.valid.item", getArgs("annotation")));
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
              : plotParameters.getSelectedAnnotation().getDefinition().getSortedPermissibleValueList()) {
            getForm().getPermissibleValues().put(value.getId().toString(),
                    value.toString());
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
        clearAnnotationBasedGePlot();
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        setPermissibleValuesNeedUpdate(false);
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            clearAnnotationBasedGePlot();
            if (plotParameters.validate()) {
                try {
                    loadAnnotationDefinitions();
                    loadPermissibleValues();
                    GeneExpressionPlotGroup plots = getAnalysisService().
                                    createGeneExpressionPlot(getStudySubscription(), plotParameters);
                    SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, plots);
                } catch (ControlSamplesNotMappedException e) {
                    SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
                    addActionError(getText("struts.messages.error.geplot.selected.controls.not.mapped.to.patients",
                            getArgs("5", e.getMessage())));
                } catch (Exception e) {
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
        return !StringUtils.equals("-1", getForm().getSelectedAnnotationId())
                && getForm().getAnnotationGroupSelection() != null;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getControlSampleSets() {
        return isStudyHasMultiplePlatforms() ? getForm().getControlSampleSets()
                : super.getControlSampleSets();
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

}
