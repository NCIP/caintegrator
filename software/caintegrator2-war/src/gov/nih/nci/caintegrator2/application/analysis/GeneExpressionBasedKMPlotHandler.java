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
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
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

import org.apache.commons.lang.StringUtils;

/**
 * KM Plot Handler for Gene Expression Based KM Plots.
 */
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
        SubjectGroup upRegulatedGroup = retrieveGroup(geneSymbol, geneSymbol + " >= " 
                                                                + kmParameters.getOverexpressedFoldChangeNumber() 
                                                                + "-fold Overexpressed",
                                                                RegulationTypeEnum.UP,
                                                                subscription);
        subjectGroupCollection.add(upRegulatedGroup);
        upRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Down Regulated
        SubjectGroup downRegulatedGroup = retrieveGroup(geneSymbol, geneSymbol + " >= " 
                                                        + kmParameters.getUnderexpressedFoldChangeNumber() 
                                                        + "-fold Underexpressed",
                                                        RegulationTypeEnum.DOWN,
                                                        subscription);
        subjectGroupCollection.add(downRegulatedGroup);
        downRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Intermediate
        SubjectGroup intermediateGroup = retrieveGroup(geneSymbol, geneSymbol + " intermediate",
                                                        RegulationTypeEnum.UNCHANGED,
                                                        subscription);
        subjectGroupCollection.add(intermediateGroup);
        intermediateGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
    }
    
    private SubjectGroup retrieveGroup(String geneSymbol, String groupName, RegulationTypeEnum regulationType, 
                                       StudySubscription subscription) throws InvalidCriterionException {
        SubjectGroup group = new SubjectGroup();
        group.setName(groupName);
        Collection<FoldChangeCriterion> criterionCollection = new HashSet<FoldChangeCriterion>();
        criterionCollection.add(retrieveFoldChangeCriterion(geneSymbol, regulationType));
        Collection<ResultRow> rows = retrieveFoldChangeRows(subscription, criterionCollection);
        assignRowsToGroup(group, rows);
        return group;
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
        criterion.setFoldsUp(kmParameters.getOverexpressedFoldChangeNumber().floatValue());
        criterion.setFoldsDown(kmParameters.getUnderexpressedFoldChangeNumber().floatValue());
        criterion.setControlSampleSetName(kmParameters.getControlSampleSetName());
        if (kmParameters.isMultiplePlatformsInStudy()) {
            criterion.setPlatformName(retrievePlatformName());
        }
        criterion.setGeneSymbol(geneSymbol);
        return criterion;
    }
    
    private String retrievePlatformName() {
        for (GenomicDataSourceConfiguration genomicSource 
                : getStudySubscription().getStudy().getStudyConfiguration().getGenomicDataSources()) {
            if (genomicSource.getControlSampleSet(kmParameters.getControlSampleSetName()) != null) {
                return genomicSource.getPlatformName();
            }
        }
        return null;
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
}
