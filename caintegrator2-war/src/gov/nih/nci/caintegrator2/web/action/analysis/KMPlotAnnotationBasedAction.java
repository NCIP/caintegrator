/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;


import gov.nih.nci.caintegrator2.application.analysis.KMAnnotationBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.security.SecurityHelper;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

/**
 * Action dealing with Kaplan-Meier Annotaion Based plotting.
 */
public class KMPlotAnnotationBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;
    private static final String ANNOTATION_PLOT_URL = "/" + SessionHelper.WAR_CONTEXT_NAME
            + "/retrieveAnnotationKMPlot.action?";
    private KMAnnotationBasedParameters kmPlotParameters = new KMAnnotationBasedParameters();
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
    }


    private void retrieveFormValues() {
        if (getForm().getSelectedAnnotationId() != null
                && !StringUtils.isEmpty(getForm().getSelectedAnnotationId())) {
               kmPlotParameters.getSelectedAnnotation().setId(
                       Long.valueOf(getForm().getSelectedAnnotationId()));
           }
       retrieveFormSelectedValues();
    }

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
            kmPlotParameters.setSelectedAnnotation(getQueryManagementService().
                    getRefreshedEntity(kmPlotParameters.getSelectedAnnotation()));
        }
    }

    private void refreshSelectedAnnotationValuesInstance() {
        if (!kmPlotParameters.getSelectedValues().isEmpty()) {
            Collection <PermissibleValue> newValues = new HashSet<PermissibleValue>();
            for (PermissibleValue value : kmPlotParameters.getSelectedValues()) {
                PermissibleValue newValue = getQueryManagementService().getRefreshedEntity(value);
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
    @Override
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
        if (!validateAnnotationGroup()) {
            return INPUT;
        }
        clearAnnotationBasedKmPlot();
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
            addActionError("struts.messages.error.must.select.annotation.group");
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
            if (kmPlotParameters.getSelectedAnnotation() == null) {
                addActionError(getText("struts.messages.error.select.valid.item", getArgs("annotation")));
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
              : kmPlotParameters.getSelectedAnnotation().getDefinition().getSortedPermissibleValueList()) {
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
            clearAnnotationBasedKmPlot();
            if (kmPlotParameters.validate()) {
                loadAnnotationDefinitions();
                loadPermissibleValues();
                KMPlot plot;
                try {
                    plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
                    SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, plot);
                } catch (Exception e) {
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
    public SortedMap<String, SortedMap<String, String>> getAllStringPValues() {
        if (SessionHelper.getAnnotationBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getAnnotationBasedKmPlot());
        }
        return new TreeMap<String, SortedMap<String, String>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        if (getForm().getSelectedAnnotationId() != null
            && !"-1".equals(getForm().getSelectedAnnotationId())
            && getForm().getAnnotationGroupSelection() != null
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
