/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;


import gov.nih.nci.caintegrator2.application.analysis.KMAnnotationBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Action dealing with Kaplan-Meier Annotaion Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class KMPlotAnnotationBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;
    private static final String ANNOTATION_PLOT_URL = "/caintegrator2/retrieveAnnotationKMPlot.action?";
    private KMAnnotationBasedParameters kmPlotParameters = new KMAnnotationBasedParameters();
    

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
        if (getForm().getSelectedAnnotationId() != null 
                && !StringUtils.isEmpty(getForm().getSelectedAnnotationId())) {
               kmPlotParameters.getSelectedAnnotation().setId(
                       Long.valueOf(getForm().getSelectedAnnotationId()));
           }
       retrieveFormSelectedValues();
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks and switch statement
    private void retrieveFormSelectedValues() {
        if (!getForm().getSelectedValuesIds().isEmpty()) {
            refreshSelectedAnnotationInstance();
            kmPlotParameters.getSelectedValues().clear();
            
            for (String id : getForm().getSelectedValuesIds()) {
                PermissibleValue value = new PermissibleValue();
                value.setId(Long.valueOf(id));
                kmPlotParameters.getSelectedValues().add(value);
            }
        }
    }
    
    private void refreshObjectInstances() {
        refreshSelectedAnnotationInstance();
        refreshSelectedAnnotationValuesInstance();
    }
    
    private void refreshSelectedAnnotationInstance() {
        if (kmPlotParameters.getSelectedAnnotation().getId() != null) {
            kmPlotParameters.setSelectedAnnotation(getStudyManagementService().
                        getRefreshedStudyEntity(kmPlotParameters.getSelectedAnnotation()));
        }
    }
    
    private void refreshSelectedAnnotationValuesInstance() {
        if (!kmPlotParameters.getSelectedValues().isEmpty()) {
            Collection <PermissibleValue> newValues = new HashSet<PermissibleValue>();
            for (PermissibleValue value : kmPlotParameters.getSelectedValues()) {
                PermissibleValue newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            kmPlotParameters.getSelectedValues().clear();
            kmPlotParameters.getSelectedValues().addAll(newValues);
        }
    }
    
    /**
     * Clears all input values and km plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        clearAnnotationBasedKmPlot();
        getForm().clear();
        kmPlotParameters.clear();
        return SUCCESS;
    }

    private void clearAnnotationBasedKmPlot() {
        SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, null);
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
     * This is used to update the Annotation Definitions on the Kaplan-Meier input form when
     * a user selects a valid Annotation Type.
     * @return Struts return value.
     */
    public String updateAnnotationDefinitions() {
        if (!validateAnnotationType()) {
            return INPUT;
        }
        clearAnnotationBasedKmPlot();
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
            if (kmPlotParameters.getSelectedAnnotation() == null) {
                addActionError("Please select a valid annotation");
                return INPUT;
            }
            getForm().clearPermissibleValues();
            clearAnnotationBasedKmPlot();
            loadPermissibleValues();
        }
        return SUCCESS;
    }

    private void loadPermissibleValues() {
        for (PermissibleValue value 
              : kmPlotParameters.getSelectedAnnotation().getPermissibleValueCollection()) {
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
            if (kmPlotParameters.validate()) {
                kmPlotParameters.setEntityType(EntityTypeEnum.getByValue(getForm().getAnnotationTypeSelection()));
                KMPlot plot;
                try {
                    plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
                    SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, plot);
                } catch (InvalidCriterionException e) {
                    SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, null);
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
    public Map<String, Map<String, String>> getAllStringPValues() {
        if (SessionHelper.getAnnotationBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getAnnotationBasedKmPlot());
        }
        return new HashMap<String, Map<String, String>>();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        if (getForm().getSelectedAnnotationId() != null 
            && !"-1".equals(getForm().getSelectedAnnotationId())
            && getForm().getAnnotationTypeSelection() != null 
            && getKmPlotForm().getSurvivalValueDefinitionId() != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlotUrl() {
        return ANNOTATION_PLOT_URL;
    }

    /**
     * @return
     */
    private KMPlotAnnotationBasedActionForm getForm() {
        return getKmPlotForm().getAnnotationBasedForm();
    }

    /**
     * @return the kmPlotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public KMAnnotationBasedParameters getKmPlotParameters() {
        return kmPlotParameters;
    }

    /**
     * @param kmPlotParameters the kmPlotParameters to set
     */
    public void setKmPlotParameters(KMAnnotationBasedParameters kmPlotParameters) {
        this.kmPlotParameters = kmPlotParameters;
    }

}
