/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.geneexpression;

import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotConfiguration;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotConfigurationFactory;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator.application.geneexpression.GenomicValueResultsTypeEnum;
import gov.nih.nci.caintegrator.application.geneexpression.PlotSampleGroup;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GE Plot Handler for Annotation Based GE Plots.
 */
class AnnotationBasedGEPlotHandler extends AbstractGEPlotHandler {

    private final GEPlotAnnotationBasedParameters parameters;
    private final Set<StudySubjectAssignment> usedSubjects = new HashSet<StudySubjectAssignment>();
        
    AnnotationBasedGEPlotHandler(CaIntegrator2Dao dao, 
                                 QueryManagementService queryManagementService, 
                                 GEPlotAnnotationBasedParameters parameters, GeneExpressionPlotService gePlotService) {
        super(dao, queryManagementService, gePlotService);
        this.parameters = parameters;
    }
    
    /**
     * {@inheritDoc}
     * @throws ControlSamplesNotMappedException 
     * @throws InvalidCriterionException 
     */
    public GeneExpressionPlotGroup createPlots(StudySubscription subscription) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        boolean twoChannelType = false;
        List<GenomicDataQueryResult> genomicResults = new ArrayList<GenomicDataQueryResult>();
        for (PermissibleValue permissibleValue : parameters.getSelectedValues()) {
            GenomicDataQueryResult result = retrieveGenomicResults(permissibleValue, subscription);
            fillUsedSubjects(result);
            genomicResults.add(result);
            if (result.getQuery().isTwoChannelType()) {
                twoChannelType = true;
            }
        }
        addOptionalGroups(subscription, genomicResults, parameters.getControlSampleSetName());
        GeneExpressionPlotConfiguration configuration = GeneExpressionPlotConfigurationFactory.createPlotConfiguration(
                genomicResults, GenomicValueResultsTypeEnum.GENE_EXPRESSION);
        configuration.setTwoChannelType(twoChannelType);
        return createGeneExpressionPlot(parameters, configuration);
    }

    private void addOptionalGroups(StudySubscription subscription, List<GenomicDataQueryResult> genomicResults,
            String controlSampleSetName) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        if (parameters.isAddPatientsNotInQueriesGroup()) {
            GenomicDataQueryResult queryResults = addAllOthersGroup(subscription, controlSampleSetName);
            if (!queryResults.getRowCollection().isEmpty()) {
                genomicResults.add(0, queryResults);
            }
        }
        if (parameters.isAddControlSamplesGroup() && !subscription.getStudy().
                            getControlSampleSet(controlSampleSetName).getSamples().isEmpty()) {
            genomicResults.add(0, addControlSamplesGroup(subscription, controlSampleSetName));
        }
    }

    private void fillUsedSubjects(GenomicDataQueryResult queryResults) {
        if (parameters.isAddPatientsNotInQueriesGroup()) {
            for (GenomicDataResultRow row : queryResults.getRowCollection()) {
                for (GenomicDataResultValue value : row.getValues()) {
                    usedSubjects.add(value.getColumn().getSampleAcquisition().getAssignment());
                }
            }
        }
    }

    private GenomicDataQueryResult addAllOthersGroup(StudySubscription subscription, String controlSampleSetName) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        Query query = new Query();
        query.setName(PlotSampleGroup.ALL_OTHERS_GROUP_NAME);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        return retrieveOptionalGroupGenomicResults(subscription, query,
                SampleGroupType.OTHERS_GROUP, controlSampleSetName);
    }
    
    private GenomicDataQueryResult addControlSamplesGroup(StudySubscription subscription, String controlSampleSetName) 
    throws ControlSamplesNotMappedException, InvalidCriterionException {
        Query query = new Query();
        query.setName(PlotSampleGroup.CONTROL_SAMPLE_GROUP_NAME);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        return retrieveOptionalGroupGenomicResults(subscription, query,
                SampleGroupType.CONTROL_GROUP, controlSampleSetName);
    }

    private GenomicDataQueryResult retrieveGenomicResults(PermissibleValue permissibleValue,
            StudySubscription subscription) throws InvalidCriterionException {
        Query query = new Query();
        SelectedValueCriterion selectedValueCriterion = new SelectedValueCriterion();
        selectedValueCriterion.setAnnotationFieldDescriptor(parameters.getSelectedAnnotation());
        selectedValueCriterion.setValueCollection(new HashSet<PermissibleValue>());
        selectedValueCriterion.getValueCollection().add(permissibleValue);
        query.setName(permissibleValue.toString());
        
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.getCompoundCriterion().getCriterionCollection().add(retrieveGeneNameCriterion());
        query.getCompoundCriterion().getCriterionCollection().add(selectedValueCriterion);
        query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        query.setReporterType(parameters.getReporterType());
        query.setSubscription(subscription);
        return getQueryManagementService().executeGenomicDataQuery(query);
    }

    private GenomicDataQueryResult retrieveOptionalGroupGenomicResults(StudySubscription subscription, Query query,
            SampleGroupType groupType, String controlSampleSetName)
        throws ControlSamplesNotMappedException, InvalidCriterionException {
        CompoundCriterion newCompoundCriterion = new CompoundCriterion();
        newCompoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        newCompoundCriterion.getCriterionCollection().add(query.getCompoundCriterion());
        newCompoundCriterion.getCriterionCollection().add(
                    retrieveOptionalGroupCompoundCriterion(subscription, groupType, controlSampleSetName));
        query.setCompoundCriterion(newCompoundCriterion);
        query.setReporterType(parameters.getReporterType());
        query.setSubscription(subscription);
        return getQueryManagementService().executeGenomicDataQuery(query);
    }

    private CompoundCriterion retrieveOptionalGroupCompoundCriterion(StudySubscription subscription,
            SampleGroupType groupType, String controlSampleSetName) 
        throws ControlSamplesNotMappedException {
        CompoundCriterion geneNameCompoundCriterion = new CompoundCriterion();
        geneNameCompoundCriterion.getCriterionCollection().add(retrieveGeneNameCriterion());
        geneNameCompoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        if (SampleGroupType.OTHERS_GROUP.equals(groupType)) {
            geneNameCompoundCriterion.getCriterionCollection().add(retrieveUsedSubjectsCriterion(usedSubjects));
        } else if (SampleGroupType.CONTROL_GROUP.equals(groupType)) {
            geneNameCompoundCriterion.getCriterionCollection().add(
                    retrieveControlGroupCriterion(subscription, controlSampleSetName));
        }
        return geneNameCompoundCriterion;
    }

    private GeneNameCriterion retrieveGeneNameCriterion() {
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        geneNameCriterion.setPlatformName(parameters.getPlatformName());
        geneNameCriterion.setGeneSymbol(parameters.getGeneSymbol());
        return geneNameCriterion;
    }

}
