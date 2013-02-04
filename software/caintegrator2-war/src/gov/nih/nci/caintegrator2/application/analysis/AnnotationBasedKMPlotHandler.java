/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * KM Plot Handler for Annotation Based KM Plots.
 */
class AnnotationBasedKMPlotHandler extends AbstractKMPlotHandler {

    private final KMAnnotationBasedParameters kmParameters;
    private final Map<SubjectGroup, PermissibleValue> subjectGroupPermissibleValue =
                                        new HashMap<SubjectGroup, PermissibleValue>();

    AnnotationBasedKMPlotHandler(StudySubscription studySubscription, CaIntegrator2Dao dao,
                                 SurvivalValueDefinition survivalValueDefinition,
                                 QueryManagementService queryManagementService,
                                 KMAnnotationBasedParameters kmParameters) {
        super(studySubscription, dao, survivalValueDefinition, queryManagementService);
        this.kmParameters = kmParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    KMPlot createPlot(KMPlotService kmPlotService) throws InvalidCriterionException,
    InvalidSurvivalValueDefinitionException {
        validateSurvivalValueDefinition();
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        AnnotationFieldDescriptor groupAnnotationField = kmParameters.getSelectedAnnotation();
        Collection<SubjectGroup> subjectGroupCollection = new HashSet<SubjectGroup>();
        retrieveSubjectGroups(kmParameters.getSelectedValues(), subjectGroupCollection);
        Collection<ResultRow> subjectRows =
                retrieveSubjectRowsFromDatabase(groupAnnotationField, getStudySubscription(), subjectGroupCollection);
        retrieveSubjectSurvivalData(groupAnnotationField, subjectRows, subjectGroupCollection);
        filterGroupsWithoutSurvivalData(configuration, subjectGroupCollection);
        configuration.setDurationLabel(getDurationLabel());
        return kmPlotService.generatePlot(configuration);
    }

    private void retrieveSubjectSurvivalData(AnnotationFieldDescriptor groupAnnotationField,
                                            Collection <ResultRow> rows,
                                            Collection<SubjectGroup> subjectGroupCollection) {
        for (ResultRow row : rows) {
            StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
            if (subjectSurvivalData != null) {
                AbstractAnnotationValue subjectPlotGroupValue = null;
                for (ResultValue value : row.getValueCollection()) {
                    if (value.getColumn().getAnnotationFieldDescriptor().equals(groupAnnotationField)) {
                        subjectPlotGroupValue = value.getValue();
                        break;
                    }
                }
                assignSubjectToGroup(subjectGroupCollection, subjectSurvivalData, subjectPlotGroupValue);
            }
        }
    }

    private void assignSubjectToGroup(Collection<SubjectGroup> subjectGroupCollection,
            SubjectSurvivalData subjectSurvivalData, AbstractAnnotationValue subjectPlotGroupValue) {
        for (SubjectGroup subjectGroup : subjectGroupCollection) {
            if (QueryUtil.annotationValueBelongToPermissibleValue(
                        subjectPlotGroupValue, subjectGroupPermissibleValue.get(subjectGroup))) {
                subjectGroup.getSurvivalData().add(subjectSurvivalData);
                break;
            }
        }
    }

    private void retrieveSubjectGroups(Collection<PermissibleValue> plotGroupValues,
            Collection<SubjectGroup> subjectGroupCollection) {
        Cai2Util.setColorPalette(plotGroupValues.size());
        for (PermissibleValue plotGroupValue : plotGroupValues) {
            SubjectGroup subjectGroup = new SubjectGroup();
            subjectGroup.setName(plotGroupValue.toString());
            subjectGroupPermissibleValue.put(subjectGroup, plotGroupValue);
            subjectGroupCollection.add(subjectGroup);
            subjectGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        }
    }


    private Collection<ResultRow> retrieveSubjectRowsFromDatabase(
            AnnotationFieldDescriptor groupAnnotationField, StudySubscription subscription,
            Collection<SubjectGroup> subjectGroups) throws InvalidCriterionException {
        ResultColumn column = new ResultColumn();
        column.setAnnotationFieldDescriptor(groupAnnotationField);
        column.setColumnIndex(0);
        Query query = new Query();
        query.getColumnCollection().add(column);
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.OR);
        query.setSubscription(subscription);

        AnnotationTypeEnum descriptorType = groupAnnotationField.getDefinition().getDataType();
        for (SubjectGroup value : subjectGroups) {
            if (descriptorType == AnnotationTypeEnum.NUMERIC) {
                query.getCompoundCriterion().getCriterionCollection()
                    .add(QueryUtil.createNumericComparisonCriterion(groupAnnotationField, value.getName()));
            } else if (descriptorType == AnnotationTypeEnum.DATE) {
                query.getCompoundCriterion().getCriterionCollection()
                    .add(QueryUtil.createDateComparisonCriterion(groupAnnotationField, value.getName()));
            } else {
                query.getCompoundCriterion().getCriterionCollection()
                    .add(QueryUtil.createStringComparisonCriterion(groupAnnotationField, value.getName()));
            }
        }
        QueryResult queryResult = getQueryManagementService().execute(query);
        return queryResult.getRowCollection();
    }
}
