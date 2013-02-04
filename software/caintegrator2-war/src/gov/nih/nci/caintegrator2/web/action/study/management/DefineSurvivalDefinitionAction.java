/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.InvalidSurvivalValueDefinitionException;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalLengthUnitsEnum;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Action used to modify/create/delete SurvivalValueDefinitions for a Study.
 */
public class DefineSurvivalDefinitionAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private SurvivalValueDefinition survivalValueDefinition = new SurvivalValueDefinition();

    private Map<String, AnnotationDefinition> dateAnnotationDefinitions =
                                                new HashMap<String, AnnotationDefinition>();
    private Map<String, AnnotationDefinition> numericAnnotationDefinitions =
                                                new HashMap<String, AnnotationDefinition>();
    private Map<String, AnnotationDefinition> survivalStatusAnnotationDefinitions =
                                                new HashMap<String, AnnotationDefinition>();
    private Map<String, SurvivalValueDefinition> survivalValueDefinitions =
                                                new HashMap<String, SurvivalValueDefinition>();
    private List<String> survivalStatusValues = new ArrayList<String>();
    private DefineSurvivalDefinitionActionForm survivalDefinitionFormValues = new DefineSurvivalDefinitionActionForm();
    private String actionType = "";
    private boolean newDefinition = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if ("modify".equals(actionType) && survivalValueDefinition.getId() == null) {
            addActionError(getText("struts.messages.error.select.survival.definition.first"));
        }
        actionType = "";
        validateStudyHasValidAnnotations();
    }

    private void validateStudyHasValidAnnotations() {
        if (getStudy().getAllVisibleAnnotationFieldDescriptors(
                EntityTypeEnum.SUBJECT, AnnotationTypeEnum.DATE).size() < 3
                && getStudy().getAllVisibleAnnotationFieldDescriptors(EntityTypeEnum.SUBJECT,
                        AnnotationTypeEnum.NUMERIC).isEmpty()) {
            addActionError(getText("struts.messages.error.survival.definition.invalid"));
        }
    }

    /**
     * Refreshes the current clinical source configuration.
     */
    @Override
    public void prepare() {
        super.prepare();
        populateSurvivalValueDefinitions();
        populateDateAnnotationDefinitions();
        populateNumericAnnotationDefinitions();
        populateSurvivalStatusAnnotationDefinitions();
        refreshExistingSurvivalDefinition();
        retrieveFormValues();
        refreshObjectInstances();
        updateSurvivalStatus();
        newDefinition = false;
    }

    private void refreshExistingSurvivalDefinition() {
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalValueDefinitionId())) {
            survivalValueDefinition.setId(Long.valueOf(survivalDefinitionFormValues.getSurvivalValueDefinitionId()));
        }
        if (survivalValueDefinition.getId() != null) {
            survivalValueDefinition = getStudyManagementService().getRefreshedEntity(survivalValueDefinition);
        }
    }

    private void populateSurvivalValueDefinitions() {
        if (getStudy() != null
            && getStudy().getSurvivalValueDefinitionCollection() != null
            && survivalValueDefinitions.size()
                != getStudy().getSurvivalValueDefinitionCollection().size()) {
            survivalValueDefinitions = new HashMap<String, SurvivalValueDefinition>();
            for (SurvivalValueDefinition def
                    : getStudy().getSurvivalValueDefinitionCollection()) {
                survivalValueDefinitions.put(def.getId().toString(), def);
            }
        }
    }

    private void populateDateAnnotationDefinitions() {
        if (dateAnnotationDefinitions.isEmpty()) {
            dateAnnotationDefinitions = new HashMap<String, AnnotationDefinition>();
            for (AnnotationFieldDescriptor descriptor : getStudy().getAllVisibleAnnotationFieldDescriptors(
                    EntityTypeEnum.SUBJECT, AnnotationTypeEnum.DATE)) {
                dateAnnotationDefinitions
                        .put(descriptor.getDefinition().getId().toString(), descriptor.getDefinition());
            }
        }
    }

    private void populateNumericAnnotationDefinitions() {
        if (numericAnnotationDefinitions.isEmpty()) {
            numericAnnotationDefinitions = new HashMap<String, AnnotationDefinition>();
            for (AnnotationFieldDescriptor descriptor : getStudy().getAllVisibleAnnotationFieldDescriptors(
                    EntityTypeEnum.SUBJECT, AnnotationTypeEnum.NUMERIC)) {
                numericAnnotationDefinitions
                        .put(descriptor.getDefinition().getId().toString(), descriptor.getDefinition());
            }
        }
    }

    private void populateSurvivalStatusAnnotationDefinitions() {
        if (survivalStatusAnnotationDefinitions.isEmpty()) {
            survivalStatusAnnotationDefinitions = new HashMap<String, AnnotationDefinition>();
            for (AnnotationFieldDescriptor descriptor : getStudy().getAllVisibleAnnotationFieldDescriptors(
                    EntityTypeEnum.SUBJECT, null)) {
                if (!descriptor.getDefinition().getPermissibleValueCollection().isEmpty()) {
                    survivalStatusAnnotationDefinitions
                        .put(descriptor.getDefinition().getId().toString(), descriptor.getDefinition());
                }
            }
        }
    }

    private void refreshObjectInstances() {
        if (survivalValueDefinition.getSurvivalStartDate() != null
            && survivalValueDefinition.getSurvivalStartDate().getId() != null) {
            survivalValueDefinition.setSurvivalStartDate(getStudyManagementService().
                    getRefreshedEntity(survivalValueDefinition.getSurvivalStartDate()));
        }

        if (survivalValueDefinition.getDeathDate() != null
            && survivalValueDefinition.getDeathDate().getId() != null) {
            survivalValueDefinition.setDeathDate(getStudyManagementService().
                    getRefreshedEntity(survivalValueDefinition.getDeathDate()));
        }

        if (survivalValueDefinition.getLastFollowupDate() != null
            && survivalValueDefinition.getLastFollowupDate().getId() != null) {
            survivalValueDefinition.setLastFollowupDate(getStudyManagementService().
                    getRefreshedEntity(survivalValueDefinition.getLastFollowupDate()));
        }
        if (survivalValueDefinition.getSurvivalLength() != null
            && survivalValueDefinition.getSurvivalLength().getId() != null) {
            survivalValueDefinition.setSurvivalLength(getStudyManagementService().
                    getRefreshedEntity(survivalValueDefinition.getSurvivalLength()));
        }
        if (survivalValueDefinition.getSurvivalStatus() != null
            && survivalValueDefinition.getSurvivalStatus().getId() != null) {
            survivalValueDefinition.setSurvivalStatus(getStudyManagementService().
                    getRefreshedEntity(survivalValueDefinition.getSurvivalStatus()));
        }
    }

    /**
     * Updates values to show user for survival status.
     * @return struts result.
     */
    public String updateSurvivalStatusValues() {
        checkNullFormValues();
        updateSurvivalStatus();
        if (survivalValueDefinition.getId() == null) {
            newDefinition = true;
        }
        return SUCCESS;
    }

    private void checkNullFormValues() {
        if (StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalStatusId())) {
            survivalValueDefinition.setSurvivalStatus(null);
        }
        if (StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalLengthId())) {
            survivalValueDefinition.setSurvivalLength(null);
        }
    }

    private void updateSurvivalStatus() {
        survivalStatusValues.clear();
        if (survivalValueDefinition.getSurvivalStatus() != null) {
            for (PermissibleValue pv : survivalValueDefinition.getSurvivalStatus().getPermissibleValueCollection()) {
                survivalStatusValues.add(pv.getValue());
            }
            Collections.sort(survivalStatusValues);
        }
    }

    private void retrieveFormValues() {
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalStartDateId())) {
            survivalValueDefinition.setSurvivalStartDate(new AnnotationDefinition());
            survivalValueDefinition.getSurvivalStartDate().setId(
                    Long.valueOf(survivalDefinitionFormValues.getSurvivalStartDateId()));
        }
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalDeathDateId())) {
            survivalValueDefinition.setDeathDate(new AnnotationDefinition());
            survivalValueDefinition.getDeathDate().setId(
                    Long.valueOf(survivalDefinitionFormValues.getSurvivalDeathDateId()));
        }
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getLastFollowupDateId())) {
            survivalValueDefinition.setLastFollowupDate(new AnnotationDefinition());
            survivalValueDefinition.getLastFollowupDate().setId(
                    Long.valueOf(survivalDefinitionFormValues.getLastFollowupDateId()));
        }
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalLengthId())) {
            survivalValueDefinition.setSurvivalLength(new AnnotationDefinition());
            survivalValueDefinition.getSurvivalLength().setId(
                    Long.valueOf(survivalDefinitionFormValues.getSurvivalLengthId()));
        }
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalStatusId())) {
            survivalValueDefinition.setSurvivalStatus(new AnnotationDefinition());
            survivalValueDefinition.getSurvivalStatus().setId(
                    Long.valueOf(survivalDefinitionFormValues.getSurvivalStatusId()));
        }
        if (!StringUtils.isBlank(survivalDefinitionFormValues.getSurvivalLengthUnits())) {
            survivalValueDefinition.setSurvivalLengthUnits(SurvivalLengthUnitsEnum
                    .getByValue(survivalDefinitionFormValues.getSurvivalLengthUnits()));
        }
    }


    private void clear() {
        survivalDefinitionFormValues.clear();
        this.survivalValueDefinition = new SurvivalValueDefinition();
    }

    /**
     * Cancel action.
     * @return struts return string.
     */
    public String cancel() {
        return SUCCESS;
    }

    /**
     * Edits the survival value definitions.
     * @return Struts result.
     */
    public String editSurvivalValueDefinitions() {
        clear();
        return SUCCESS;
    }

    /**
     * Edits the survival value definition that is chosen.
     * @return the Struts result.
     */
    public String editSurvivalValueDefinition() {
        survivalDefinitionFormValues.load(survivalValueDefinition);
        updateSurvivalStatus();
        return SUCCESS;
    }

    /**
     * Creates a new survival value definition.
     *
     * @return the Struts result.
     */
    public String newSurvivalValueDefinition() {
        newDefinition = true;
        survivalValueDefinition = new SurvivalValueDefinition();
        survivalDefinitionFormValues.clear();
        populateSurvivalValueDefinitions();
        return SUCCESS;
    }

    /**
     * Deletes a survival value definition.
     *
     * @return the Struts result.
     */
    public String deleteSurvivalValueDefinition() {
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogDelete(survivalValueDefinition));
        getStudyManagementService().
            removeSurvivalValueDefinition(getStudy(), getSurvivalValueDefinition());
        this.clear();
        populateSurvivalValueDefinitions();
        return SUCCESS;
    }

    /**
     * Saves a survival value definition.
     *
     * @return the Struts result.
     */
    public String saveSurvivalValueDefinition() {
        setSurvivalValueDefinitionValues();

        try {
            validateSurvivalValueDefinition();
        } catch (InvalidSurvivalValueDefinitionException e) {
            if (survivalValueDefinition.getId() == null) {
                newDefinition = true;
            }
            addActionError("Unable to save Survival Value Definition: " + e.getMessage());
            return INPUT;
        }
        if (!getStudy().getSurvivalValueDefinitionCollection().contains(survivalValueDefinition)) {
            getStudy().getSurvivalValueDefinitionCollection().add(survivalValueDefinition);
        }
        getStudyManagementService().save(getStudyConfiguration());
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogSave(survivalValueDefinition));
        survivalDefinitionFormValues.clear();
        return SUCCESS;
    }

    /**
     *
     */
    private void setSurvivalValueDefinitionValues() {
        survivalValueDefinition.setName(survivalDefinitionFormValues.getSurvivalValueDefinitionName());
        survivalValueDefinition.setSurvivalValueType(SurvivalValueTypeEnum.getByValue(survivalDefinitionFormValues
                .getSurvivalValueType()));
        survivalValueDefinition.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.getByValue(
                survivalDefinitionFormValues.getSurvivalLengthUnits()));
        if (SurvivalValueTypeEnum.DATE.equals(survivalValueDefinition.getSurvivalValueType())) {
            survivalValueDefinition.setSurvivalLength(null);
            survivalValueDefinition.setSurvivalStatus(null);
            survivalValueDefinition.setValueForCensored(null);
        }
        if (SurvivalValueTypeEnum.LENGTH_OF_TIME.equals(survivalValueDefinition.getSurvivalValueType())) {
            survivalValueDefinition.setSurvivalStartDate(null);
            survivalValueDefinition.setDeathDate(null);
            survivalValueDefinition.setLastFollowupDate(null);
            survivalValueDefinition.setValueForCensored(survivalDefinitionFormValues.getValueForCensored());
            checkNullFormValues();
        }

    }

    /**
     * @throws InvalidSurvivalValueDefinitionException
     */
    private void validateSurvivalValueDefinition() throws InvalidSurvivalValueDefinitionException {
        if (StringUtils.isBlank(survivalValueDefinition.getName())) {
            throw new InvalidSurvivalValueDefinitionException("Must enter a name for Survival Value Definition.");
        }
        for (SurvivalValueDefinition definition : getStudy().getSurvivalValueDefinitionCollection()) {
            if (definition != survivalValueDefinition
                && definition.getName().equalsIgnoreCase(survivalValueDefinition.getName())) {
                throw new InvalidSurvivalValueDefinitionException("That name already exists, must enter a unique "
                        + "name for Survival Value Definition.");
            }
        }
        Cai2Util.validateSurvivalValueDefinition(survivalValueDefinition);
    }

    /**
     * @return the survivalValueDefinition
     */
    public SurvivalValueDefinition getSurvivalValueDefinition() {
        return survivalValueDefinition;
    }

    /**
     * @param survivalValueDefinition the survivalValueDefinition to set
     */
    public void setSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition) {
        this.survivalValueDefinition = survivalValueDefinition;
    }

    /**
     * @return the survivalValueDefinitions
     */
    public Map<String, SurvivalValueDefinition> getSurvivalValueDefinitions() {
        return survivalValueDefinitions;
    }

    /**
     * @param survivalValueDefinitions the survivalValueDefinitions to set
     */
    public void setSurvivalValueDefinitions(Map<String, SurvivalValueDefinition> survivalValueDefinitions) {
        this.survivalValueDefinitions = survivalValueDefinitions;
    }


    /**
     * @return the survivalDefinitionFormValues
     */
    public DefineSurvivalDefinitionActionForm getSurvivalDefinitionFormValues() {
        return survivalDefinitionFormValues;
    }


    /**
     * @param survivalDefinitionFormValues the survivalDefinitionFormValues to set
     */
    public void setSurvivalDefinitionFormValues(DefineSurvivalDefinitionActionForm survivalDefinitionFormValues) {
        this.survivalDefinitionFormValues = survivalDefinitionFormValues;
    }


    /**
     * @return the dateAnnotationDefinitions
     */
    public Map<String, AnnotationDefinition> getDateAnnotationDefinitions() {
        return dateAnnotationDefinitions;
    }


    /**
     * @param dateAnnotationDefinitions the dateAnnotationDefinitions to set
     */
    public void setDateAnnotationDefinitions(Map<String, AnnotationDefinition> dateAnnotationDefinitions) {
        this.dateAnnotationDefinitions = dateAnnotationDefinitions;
    }

    /**
     * @return the actionType
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the numericAnnotationDefinitions
     */
    public Map<String, AnnotationDefinition> getNumericAnnotationDefinitions() {
        return numericAnnotationDefinitions;
    }

    /**
     * @param numericAnnotationDefinitions the numericAnnotationDefinitions to set
     */
    public void setNumericAnnotationDefinitions(Map<String, AnnotationDefinition> numericAnnotationDefinitions) {
        this.numericAnnotationDefinitions = numericAnnotationDefinitions;
    }

    /**
     * @return the survivalStatusAnnotationDefinitions
     */
    public Map<String, AnnotationDefinition> getSurvivalStatusAnnotationDefinitions() {
        return survivalStatusAnnotationDefinitions;
    }

    /**
     * @param survivalStatusAnnotationDefinitions the survivalStatusAnnotationDefinitions to set
     */
    public void setSurvivalStatusAnnotationDefinitions(
            Map<String, AnnotationDefinition> survivalStatusAnnotationDefinitions) {
        this.survivalStatusAnnotationDefinitions = survivalStatusAnnotationDefinitions;
    }

    /**
     * @return the survivalStatusValues
     */
    public List<String> getSurvivalStatusValues() {
        return survivalStatusValues;
    }

    /**
     * @param survivalStatusValues the survivalStatusValues to set
     */
    public void setSurvivalStatusValues(List<String> survivalStatusValues) {
        this.survivalStatusValues = survivalStatusValues;
    }

    /**
     * @return the newSurvivalValueDefinition
     */
    public boolean isNewDefinition() {
        return newDefinition;
    }

    /**
     *
     * @return css style value.
     */
    public String getDateInputCssStyle() {
        return SurvivalValueTypeEnum.DATE.getValue().equals(survivalDefinitionFormValues.getSurvivalValueType())
            ? "display: block;" : "display: none;";
    }

    /**
     *
     * @return css style value.
     */
    public String getLengthOfTimeInputCssStyle() {
        return SurvivalValueTypeEnum.LENGTH_OF_TIME.getValue().equals(
                survivalDefinitionFormValues.getSurvivalValueType()) ? "display: block;" : "display: none;";
    }

}
