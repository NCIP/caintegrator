/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
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
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.RangeTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * KM Plot Handler for Gene Expression Based KM Plots.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See getGroupName()
class GeneExpressionBasedKMPlotHandler extends AbstractKMPlotHandler {

    private final KMGeneExpressionBasedParameters kmParameters;
        
    GeneExpressionBasedKMPlotHandler(StudySubscription studySubscription, CaIntegrator2Dao dao, 
                                 SurvivalValueDefinition survivalValueDefinition, 
                                 QueryManagementService queryManagementService, 
                                 KMGeneExpressionBasedParameters kmParameters) {
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
        Collection <SubjectGroup> subjectGroupCollection = new HashSet<SubjectGroup>();
        for (String geneSymbol : kmParameters.getGenesFoundInStudy()) {
            retrieveSubjectGroups(getStudySubscription(), subjectGroupCollection, geneSymbol);    
        }
        filterGroupsWithoutSurvivalData(configuration, subjectGroupCollection);
        configuration.getGenesNotFound().addAll(kmParameters.getGenesNotFound());
        configuration.setDurationLabel(getDurationLabel());
        return kmPlotService.generatePlot(configuration);
    }


    private void retrieveSubjectGroups(StudySubscription subscription, 
                                       Collection<SubjectGroup> subjectGroupCollection,
                                       String geneSymbol) 
        throws InvalidCriterionException {
        Cai2Util.setColorPalette(3 * kmParameters.getGenesFoundInStudy().size());
        // Up Regulated
        SubjectGroup upRegulatedGroup = retrieveGroup(geneSymbol, GeneExpressionGroupType.OVER_VALUE,
                                                                subscription);
        subjectGroupCollection.add(upRegulatedGroup);
        upRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Down Regulated
        SubjectGroup downRegulatedGroup = retrieveGroup(geneSymbol, GeneExpressionGroupType.UNDER_VALUE,
                                                        subscription);
        subjectGroupCollection.add(downRegulatedGroup);
        downRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Intermediate
        SubjectGroup intermediateGroup = retrieveGroup(geneSymbol, GeneExpressionGroupType.BETWEEN_VALUES,
                                                        subscription);
        subjectGroupCollection.add(intermediateGroup);
        intermediateGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
    }
    
    private SubjectGroup retrieveGroup(String geneSymbol, GeneExpressionGroupType groupType, 
                                       StudySubscription subscription) throws InvalidCriterionException {
        SubjectGroup group = new SubjectGroup();
        group.setName(getGroupName(geneSymbol, groupType));
        Collection<AbstractCriterion> criterionCollection = new HashSet<AbstractCriterion>();
        if (isFoldChangeType()) {
            criterionCollection.add(retrieveFoldChangeCriterion(geneSymbol, groupType.getFoldChangeType()));
        } else if (isExpressionLevelType()) {
            criterionCollection.add(retrieveExpressionLevelCriterion(geneSymbol, groupType.getExpressionLevelType()));
        } else {
            throw new InvalidCriterionException("Unknown gene expression type for KM Plot by Gene Expression.");
        }
        Collection<ResultRow> rows = retrieveRows(subscription, criterionCollection);
        assignRowsToGroup(group, rows);
        return group;
    }
    
    @SuppressWarnings("PMD.CyclomaticComplexity") // complex case statement for the different group names.
    private String getGroupName(String geneSymbol, GeneExpressionGroupType groupType) {
        StringBuffer groupName = new StringBuffer(geneSymbol);
        String comparator = ">= ";
        String value = "";
        String suffix = "";
        switch (groupType) {
        case OVER_VALUE:
            value = String.valueOf(kmParameters.getOverValue());
            if (isFoldChangeType()) {
                suffix = "-fold Overexpressed";
            }
            break;
        case BETWEEN_VALUES:
            comparator = "";
            suffix = " intermediate";
            break;
        case UNDER_VALUE:
            value = String.valueOf(kmParameters.getUnderValue());
            comparator = "<= ";
            if (isFoldChangeType()) {
                comparator = ">= ";
                suffix = "-fold Underexpressed";
            }
            break;
        default: 
            break;
        } 
        groupName.append(comparator).append(value).append(suffix);
        return groupName.toString();
    }

    private void assignRowsToGroup(SubjectGroup group, Collection<ResultRow> rows) {
        for (ResultRow row : rows) {
        StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
            if (subjectSurvivalData != null) {
                group.getSurvivalData().add(subjectSurvivalData);
            }
        }
    }
    
    private FoldChangeCriterion retrieveFoldChangeCriterion(String geneSymbol, RegulationTypeEnum regulationType) {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(regulationType);
        criterion.setFoldsUp(kmParameters.getOverValue().floatValue());
        criterion.setFoldsDown(kmParameters.getUnderValue().floatValue());
        criterion.setControlSampleSetName(kmParameters.getControlSampleSetName());
        if (kmParameters.isMultiplePlatformsInStudy()) {
            criterion.setPlatformName(kmParameters.getPlatformName());
        }
        criterion.setGeneSymbol(geneSymbol);
        return criterion;
    }
    
    private ExpressionLevelCriterion retrieveExpressionLevelCriterion(String geneSymbol, RangeTypeEnum rangeType) {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(rangeType);
        criterion.setLowerLimit(RangeTypeEnum.INSIDE_RANGE.equals(rangeType) 
                ? kmParameters.getUnderValue().floatValue() : kmParameters.getOverValue().floatValue());
        criterion.setUpperLimit(RangeTypeEnum.INSIDE_RANGE.equals(rangeType) 
                ? kmParameters.getOverValue().floatValue() : kmParameters.getUnderValue().floatValue());
        if (kmParameters.isMultiplePlatformsInStudy()) {
            criterion.setPlatformName(kmParameters.getPlatformName());
        }
        criterion.setGeneSymbol(geneSymbol);
        return criterion;
    }
    
    private Collection<ResultRow> retrieveRows(StudySubscription subscription, 
                                  Collection<AbstractCriterion> foldChangeCriterionCollection) 
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    void setupAndValidateParameters(AnalysisService analysisService) throws GenesNotFoundInStudyException {
        if (StringUtils.isNotBlank(kmParameters.getGeneSymbol())) {
            kmParameters.getGenesFoundInStudy().clear();
            kmParameters.getGenesFoundInStudy().addAll(Cai2Util.
                    createListFromCommaDelimitedString(kmParameters.getGeneSymbol()));
            kmParameters.getGenesNotFound().clear();
            kmParameters.getGenesNotFound().addAll(analysisService.validateGeneSymbols(getStudySubscription(), 
                    kmParameters.getGenesFoundInStudy()));
            kmParameters.getGenesFoundInStudy().removeAll(kmParameters.getGenesNotFound());
        }
    }
    
    private boolean isFoldChangeType() {
        return ExpressionTypeEnum.FOLD_CHANGE.
                equals(kmParameters.getExpressionType());
    }
    
    private boolean isExpressionLevelType() {
        return ExpressionTypeEnum.EXPRESSION_LEVEL.
                equals(kmParameters.getExpressionType());
    }
    
    /**
     * Enum for the expression group types.
     */
    private static enum GeneExpressionGroupType {
        OVER_VALUE(RegulationTypeEnum.UP, RangeTypeEnum.GREATER_OR_EQUAL),
        
        BETWEEN_VALUES(RegulationTypeEnum.UNCHANGED, RangeTypeEnum.INSIDE_RANGE),
        
        UNDER_VALUE(RegulationTypeEnum.DOWN, RangeTypeEnum.LESS_OR_EQUAL);
        
        private RegulationTypeEnum foldChangeType;
        private RangeTypeEnum expressionLevelType;
        
        private GeneExpressionGroupType(RegulationTypeEnum foldChangeType, RangeTypeEnum expressionLevelType) {
            this.foldChangeType = foldChangeType;
            this.expressionLevelType = expressionLevelType;
        }

        /**
         * @return the foldChangeType
         */
        public RegulationTypeEnum getFoldChangeType() {
            return foldChangeType;
        }

        /**
         * @return the expressionLevelType
         */
        public RangeTypeEnum getExpressionLevelType() {
            return expressionLevelType;
        }
        
        
    }
}
