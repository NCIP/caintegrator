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
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * KM Plot Handler for Query Based KM Plots.
 */
class QueryBasedKMPlotHandler extends AbstractKMPlotHandler {

    private final KMQueryBasedParameters kmParameters;
    private final Set<StudySubjectAssignment> usedSubjects = new HashSet<StudySubjectAssignment>();
    
    QueryBasedKMPlotHandler(CaIntegrator2Dao dao, 
                                 SurvivalValueDefinition survivalValueDefinition, 
                                 QueryManagementService queryManagementService, 
                                 KMQueryBasedParameters kmParameters) {
        super(dao, survivalValueDefinition, queryManagementService);
        this.kmParameters = kmParameters;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    KMPlot createPlot(KMPlotService kmPlotService, StudySubscription subscription) throws InvalidCriterionException {
        validateSurvivalValueDefinition();
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        Collection <SubjectGroup> subjectGroupCollection = new HashSet<SubjectGroup>();
        retrieveSubjectGroups(subscription, subjectGroupCollection);
        filterGroupsWithoutSurvivalData(configuration, subjectGroupCollection);
        return kmPlotService.generatePlot(configuration);
    }

    private void retrieveSubjectGroups(StudySubscription subscription, 
                                       Collection<SubjectGroup> subjectGroupCollection) 
    throws InvalidCriterionException {
        
        for (Query query : kmParameters.getQueries()) {
            query.setResultType(ResultTypeEnum.CLINICAL);
            SubjectGroup group = retrieveGroup(query);
            subjectGroupCollection.add(group);
            group.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        }
        if (kmParameters.isAddPatientsNotInQueriesGroup()) {
            SubjectGroup otherSubjectsGroup = retrieveOtherSubjectGroup(subscription);
            subjectGroupCollection.add(otherSubjectsGroup);
            otherSubjectsGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        }
    }
    
    private SubjectGroup retrieveGroup(Query query) throws InvalidCriterionException {
        SubjectGroup group = new SubjectGroup();
        group.setName(query.getName());
        Collection<ResultRow> rows = getQueryManagementService().execute(query).getRowCollection();
        assignRowsToGroup(group, rows);
        return group;
    }

    private void assignRowsToGroup(SubjectGroup group, Collection<ResultRow> rows) {
        for (ResultRow row : rows) {
            StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            if (!kmParameters.isExclusiveGroups() || !usedSubjects.contains(subjectAssignment)) {
                SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
                if (subjectSurvivalData != null) {
                    group.getSurvivalData().add(subjectSurvivalData);
                }
                usedSubjects.add(subjectAssignment);
            }
        }
    }

    private SubjectGroup retrieveOtherSubjectGroup(StudySubscription subscription) {
        SubjectGroup otherSubjectsGroup = new SubjectGroup();
        otherSubjectsGroup.setName("Others");
        for (StudySubjectAssignment assignment : subscription.getStudy().getAssignmentCollection()) {
            if (!usedSubjects.contains(assignment)) {
                SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(assignment);
                if (subjectSurvivalData != null) {
                    otherSubjectsGroup.getSurvivalData().add(subjectSurvivalData);
                }
                usedSubjects.add(assignment);
            }
        }
        return otherSubjectsGroup;
    }
}
