/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
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
    
    AnnotationBasedKMPlotHandler(CaIntegrator2Dao dao, 
                                 SurvivalValueDefinition survivalValueDefinition, 
                                 QueryManagementService queryManagementService, 
                                 KMAnnotationBasedParameters kmParameters) {
        super(dao, survivalValueDefinition, queryManagementService);
        this.kmParameters = kmParameters;
    }
    
    /**
     * {@inheritDoc}
     */
    KMPlot createPlot(KMPlotService kmPlotService, StudySubscription subscription) throws InvalidCriterionException {
        validateSurvivalValueDefinition();
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        AnnotationDefinition groupAnnotationField = kmParameters.getSelectedAnnotation(); 
        Collection <SubjectGroup> subjectGroupCollection = new HashSet<SubjectGroup>();
        retrieveSubjectGroups(kmParameters.getSelectedValues(), subjectGroupCollection);
        Collection <ResultRow> subjectRows = 
                retrieveSubjectRowsFromDatabase(kmParameters.getEntityType(), groupAnnotationField, subscription);
        retrieveSubjectSurvivalData(groupAnnotationField, subjectRows, subjectGroupCollection);
        filterGroupsWithoutSurvivalData(configuration, subjectGroupCollection);
        return kmPlotService.generatePlot(configuration);
    }

    private void retrieveSubjectSurvivalData(AnnotationDefinition groupAnnotationField,
                                            Collection <ResultRow> rows, 
                                            Collection<SubjectGroup> subjectGroupCollection) {
        for (ResultRow row : rows) {
            StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
            if (subjectSurvivalData != null) {
                AbstractAnnotationValue subjectPlotGroupValue = null;
                for (ResultValue value : row.getValueCollection()) {
                    if (value.getColumn().getAnnotationDefinition().equals(groupAnnotationField)) {
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
            if (Cai2Util.annotationValueBelongToPermissibleValue(
                        subjectPlotGroupValue, subjectGroupPermissibleValue.get(subjectGroup))) {
                subjectGroup.getSurvivalData().add(subjectSurvivalData);
                break;
            }
        }
    }

    private void retrieveSubjectGroups(Collection<PermissibleValue> plotGroupValues,
            Collection<SubjectGroup> subjectGroupCollection) {
        for (PermissibleValue plotGroupValue : plotGroupValues) {
            SubjectGroup subjectGroup = new SubjectGroup();
            subjectGroup.setName(plotGroupValue.toString());
            subjectGroupPermissibleValue.put(subjectGroup, plotGroupValue);
            subjectGroupCollection.add(subjectGroup);
            subjectGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        }
    }

    
    private Collection<ResultRow> retrieveSubjectRowsFromDatabase(EntityTypeEnum groupFieldType, 
                                                 AnnotationDefinition groupAnnotationField,
                                                 StudySubscription subscription) throws InvalidCriterionException {
        Query query = new Query();
        ResultColumn column = new ResultColumn();
        column.setAnnotationDefinition(groupAnnotationField);
        column.setEntityType(groupFieldType);
        column.setColumnIndex(0);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.getColumnCollection().add(column);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setSubscription(subscription);
        QueryResult queryResult = getQueryManagementService().execute(query);
        return queryResult.getRowCollection();
    }
    
    
    
}
