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
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * KM Plot Handler for Gene Expression Based KM Plots.
 */
class GeneExpressionBasedKMPlotHandler extends AbstractKMPlotHandler {

    private final KMGeneExpressionBasedParameters kmParameters;
    private final Set<StudySubjectAssignment> usedSubjects = new HashSet<StudySubjectAssignment>();

    
    GeneExpressionBasedKMPlotHandler(CaIntegrator2Dao dao, 
                                 SurvivalValueDefinition survivalValueDefinition, 
                                 QueryManagementService queryManagementService, 
                                 KMGeneExpressionBasedParameters kmParameters) {
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
        // Up Regulated
        SubjectGroup upRegulatedGroup = retrieveGroup(kmParameters.getGeneSymbol() + " >= " 
                                                                + kmParameters.getOverexpressedFoldChangeNumber() 
                                                                + "-fold Overexpressed",
                                                                RegulationTypeEnum.UP,
                                                                subscription);
        subjectGroupCollection.add(upRegulatedGroup);
        upRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Down Regulated
        SubjectGroup downRegulatedGroup = retrieveGroup(kmParameters.getGeneSymbol() + " >= " 
                                                        + kmParameters.getUnderexpressedFoldChangeNumber() 
                                                        + "-fold Underexpressed",
                                                        RegulationTypeEnum.DOWN,
                                                        subscription);
        subjectGroupCollection.add(downRegulatedGroup);
        downRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Intermediate
        SubjectGroup intermediateGroup = retrieveGroup("Intermediate",
                                                        RegulationTypeEnum.UNCHANGED,
                                                        subscription);
        subjectGroupCollection.add(intermediateGroup);
        intermediateGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
    }
    
    private SubjectGroup retrieveGroup(String groupName, RegulationTypeEnum regulationType, 
                                       StudySubscription subscription) throws InvalidCriterionException {
        SubjectGroup group = new SubjectGroup();
        group.setName(groupName);
        Collection<FoldChangeCriterion> criterionCollection = new HashSet<FoldChangeCriterion>();
        criterionCollection.add(retrieveFoldChangeCriterion(regulationType));
        Collection<ResultRow> rows = retrieveFoldChangeRows(subscription, criterionCollection);
        assignRowsToGroup(group, rows);
        return group;
    }

    private void assignRowsToGroup(SubjectGroup group, Collection<ResultRow> rows) {
        for (ResultRow row : rows) {
            StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            if (!usedSubjects.contains(subjectAssignment)) {
                SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
                if (subjectSurvivalData != null) {
                    group.getSurvivalData().add(subjectSurvivalData);
                    usedSubjects.add(subjectAssignment);
                }
            }
        }
    }
    
    private FoldChangeCriterion retrieveFoldChangeCriterion(RegulationTypeEnum regulationType) {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(regulationType);
        criterion.setFoldsUp(kmParameters.getOverexpressedFoldChangeNumber().floatValue());
        if (RegulationTypeEnum.UNCHANGED.equals(regulationType)) {
            criterion.setFoldsDown(1 / kmParameters.getUnderexpressedFoldChangeNumber().floatValue());
        } else {
            criterion.setFoldsDown(kmParameters.getUnderexpressedFoldChangeNumber().floatValue());
        }
        criterion.setControlSampleSetName(kmParameters.getControlSampleSetName());
        criterion.setGeneSymbol(kmParameters.getGeneSymbol());
        return criterion;
    }
    
    private Collection<ResultRow> retrieveFoldChangeRows(StudySubscription subscription, 
                                  Collection<FoldChangeCriterion> foldChangeCriterionCollection) 
                                  throws InvalidCriterionException {
        Query query = new Query();
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.getCompoundCriterion().getCriterionCollection().addAll(foldChangeCriterionCollection);
        query.setSubscription(subscription);
        QueryResult queryResult = getQueryManagementService().execute(query);
        return queryResult.getRowCollection();
    }
}
