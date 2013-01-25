/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotConfiguration;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotConfigurationFactory;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.geneexpression.GenomicValueResultsTypeEnum;
import gov.nih.nci.caintegrator2.application.geneexpression.PlotSampleGroup;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GE Plot Handler for Annotation Based GE Plots.
 */
class ClinicalQueryBasedGEPlotHandler extends AbstractGEPlotHandler {

    private final GEPlotClinicalQueryBasedParameters parameters;
    private final Set<StudySubjectAssignment> usedSubjects = new HashSet<StudySubjectAssignment>();

        
    ClinicalQueryBasedGEPlotHandler(CaIntegrator2Dao dao, 
                                 QueryManagementService queryManagementService, 
                                 GEPlotClinicalQueryBasedParameters parameters, 
                                 GeneExpressionPlotService gePlotService) {
        super(dao, queryManagementService, gePlotService);
        this.parameters = parameters;
    }
    
    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup createPlots(StudySubscription subscription) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        List<GenomicDataQueryResult> genomicResults = new ArrayList<GenomicDataQueryResult>();
        for (Query query : parameters.getQueries()) {
            GenomicDataQueryResult queryResults = 
                retrieveGenomicResultsForQuery(subscription, query, SampleGroupType.DEFAULT);
            fillUsedSubjects(queryResults);
            genomicResults.add(queryResults);
        }
        addOptionalGroups(subscription, genomicResults);
        GeneExpressionPlotConfiguration configuration = GeneExpressionPlotConfigurationFactory.createPlotConfiguration(
                genomicResults, GenomicValueResultsTypeEnum.GENE_EXPRESSION);
        return createGeneExpressionPlot(parameters, configuration);
    }

    private void addOptionalGroups(StudySubscription subscription, List<GenomicDataQueryResult> genomicResults) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        if (parameters.isAddPatientsNotInQueriesGroup()) {
            GenomicDataQueryResult queryResults = addAllOthersGroup(subscription);
            if (!queryResults.getRowCollection().isEmpty()) {
                genomicResults.add(0, queryResults);
            }
        }
        if (parameters.isAddControlSamplesGroup() && !subscription.getStudy().
                getControlSampleSet(parameters.getControlSampleSetName()).getSamples().isEmpty()) {
            genomicResults.add(0, addControlSamplesGroup(subscription));
        }
    }

    private void fillUsedSubjects(GenomicDataQueryResult queryResults) {
        if (parameters.isAddPatientsNotInQueriesGroup() || parameters.isExclusiveGroups()) {
            for (GenomicDataResultRow row : queryResults.getRowCollection()) {
                for (GenomicDataResultValue value : row.getValues()) {
                    usedSubjects.add(value.getColumn().getSampleAcquisition().getAssignment());
                }
            }
        }
    }
    
    private GenomicDataQueryResult addControlSamplesGroup(StudySubscription subscription) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        Query query = new Query();
        query.setName(PlotSampleGroup.CONTROL_SAMPLE_GROUP_NAME);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        return retrieveGenomicResultsForQuery(subscription, query, SampleGroupType.CONTROL_GROUP);
    }

    private GenomicDataQueryResult addAllOthersGroup(StudySubscription subscription) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        Query query = new Query();
        query.setName(PlotSampleGroup.ALL_OTHERS_GROUP_NAME);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        return retrieveGenomicResultsForQuery(subscription, query, SampleGroupType.OTHERS_GROUP);
    }
    
    private GenomicDataQueryResult retrieveGenomicResultsForQuery(StudySubscription subscription, 
                                                                  Query query,
                                                                  SampleGroupType groupType) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        CompoundCriterion newCompoundCriterion = new CompoundCriterion();
        newCompoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        newCompoundCriterion.getCriterionCollection().add(query.getCompoundCriterion());
        newCompoundCriterion.getCriterionCollection().add(retrieveGeneNameCompoundCriterion(subscription, groupType));
        query.setCompoundCriterion(newCompoundCriterion);
        query.setReporterType(parameters.getReporterType());
        query.setSubscription(subscription);
        return getQueryManagementService().executeGenomicDataQuery(query);
    }

    private CompoundCriterion retrieveGeneNameCompoundCriterion(StudySubscription subscription, 
                                                                SampleGroupType groupType) 
    throws ControlSamplesNotMappedException {
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        geneNameCriterion.setGeneSymbol(parameters.getGeneSymbol());
        CompoundCriterion geneNameCompoundCriterion = new CompoundCriterion();
        geneNameCompoundCriterion.getCriterionCollection().add(geneNameCriterion);
        geneNameCompoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        if (SampleGroupType.CONTROL_GROUP.equals(groupType)) {
            geneNameCompoundCriterion.getCriterionCollection().add(
                    retrieveControlGroupCriterion(subscription, parameters.getControlSampleSetName()));
        } else if (parameters.isExclusiveGroups() || SampleGroupType.OTHERS_GROUP.equals(groupType)) {
            geneNameCompoundCriterion.getCriterionCollection().add(retrieveUsedSubjectsCriterion(usedSubjects));
        }
        return geneNameCompoundCriterion;
    }


}
