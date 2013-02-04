/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.geneexpression;

import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotConfiguration;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * Abstract class representing a handler for Gene Expression plot creation.
 */
public abstract class AbstractGEPlotHandler {

    private final CaIntegrator2Dao dao;
    private final QueryManagementService queryManagementService;
    private final GeneExpressionPlotService gePlotService;

    /**
     * Constructor.
     * @param dao to call database.
     * @param queryManagementService for query execution.
     * @param gePlotService used to create the ge plots.
     */
    protected AbstractGEPlotHandler(CaIntegrator2Dao dao, QueryManagementService queryManagementService,
                                    GeneExpressionPlotService gePlotService) {
        this.dao = dao;
        this.queryManagementService = queryManagementService;
        this.gePlotService = gePlotService;
    }

    /**
     * Creates the GeneExpressionPlotHandler based on Parameters.
     * @param dao to call database.
     * @param queryManagementService for query creation.
     * @param parameters used to determine type of handler to create.
     * @param gePlotService used to create the ge plots.
     * @return handler.
     */
    public static AbstractGEPlotHandler createGeneExpressionPlotHandler(CaIntegrator2Dao dao,
                                                                        QueryManagementService queryManagementService,
                                                                        AbstractGEPlotParameters parameters,
                                                                        GeneExpressionPlotService gePlotService) {
        if (parameters instanceof GEPlotAnnotationBasedParameters) {
            return new AnnotationBasedGEPlotHandler(dao, queryManagementService,
                                                   (GEPlotAnnotationBasedParameters) parameters,
                                                   gePlotService);
        } else if (parameters instanceof GEPlotGenomicQueryBasedParameters) {
            return new GenomicQueryBasedGEPlotHandler(dao, queryManagementService,
                                                    (GEPlotGenomicQueryBasedParameters) parameters,
                                                    gePlotService);
        } else if (parameters instanceof GEPlotClinicalQueryBasedParameters) {
            return new ClinicalQueryBasedGEPlotHandler(dao, queryManagementService,
                    (GEPlotClinicalQueryBasedParameters) parameters, gePlotService);
}
        throw new IllegalArgumentException("Unknown Parameter Type");
    }

    /**
     * Creates the GeneExpressionPlotGroup for the parameters.
     * @param subscription that user is currently using.
     * @return plot group.
     * @throws ControlSamplesNotMappedException when a control sample is not mapped.
     * @throws InvalidCriterionException if the criterion is not valid for the query.
     */
    public abstract GeneExpressionPlotGroup createPlots(StudySubscription subscription)
        throws ControlSamplesNotMappedException, InvalidCriterionException;

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * Creates the plot.
     * @param parameters input for graph.
     * @param configuration to add genes not found.
     * @return the gene expression plot group.
     */
    protected GeneExpressionPlotGroup createGeneExpressionPlot(AbstractGEPlotParameters parameters,
                                                               GeneExpressionPlotConfiguration configuration) {
        configuration.getGenesNotFound().addAll(parameters.getGenesNotFound());
        return gePlotService.generatePlots(configuration);
    }

    /**
     * Retrieves the control group criterion for the study subscription.
     * @param subscription to get control samples for.
     * @param controlSampleSetName to retrieve for.
     * @return criterion on the ID's of the control samples.
     * @throws ControlSamplesNotMappedException when a control sample is not mapped.
     */
    protected AbstractCriterion retrieveControlGroupCriterion(StudySubscription subscription,
            String controlSampleSetName)
    throws ControlSamplesNotMappedException {
        CompoundCriterion idCriteria = new CompoundCriterion();
        idCriteria.setBooleanOperator(BooleanOperatorEnum.OR);
        idCriteria.setCriterionCollection(new HashSet<AbstractCriterion>());
        for (Sample sample : subscription.getStudy().getControlSampleSet(controlSampleSetName).getSamples()) {
            if (CollectionUtils.isEmpty(sample.getSampleAcquisitions())) {
                throw new ControlSamplesNotMappedException("Sample '"  + sample.getName()
                        + "' is not mapped to a patient.");
            }
            IdentifierCriterion idCriterion = new IdentifierCriterion();
            idCriterion.setStringValue(sample.getSampleAcquisitions()
                    .iterator().next().getAssignment().getIdentifier());
            idCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
            idCriterion.setEntityType(EntityTypeEnum.SUBJECT);
            idCriteria.getCriterionCollection().add(idCriterion);
        }
        return idCriteria;
    }

    /**
     * Retrieves the used subjects criterion.
     * @param usedSubjects set of used subjects.
     * @return compound criterion of used subjects.
     */
    protected CompoundCriterion retrieveUsedSubjectsCriterion(Set<StudySubjectAssignment> usedSubjects) {
        CompoundCriterion idCriteria = new CompoundCriterion();
        idCriteria.setBooleanOperator(BooleanOperatorEnum.AND);
        idCriteria.setCriterionCollection(new HashSet<AbstractCriterion>());
        for (StudySubjectAssignment assignment : usedSubjects) {
            IdentifierCriterion idCriterion = new IdentifierCriterion();
            idCriterion.setStringValue(assignment.getIdentifier());
            idCriterion.setWildCardType(WildCardTypeEnum.NOT_EQUAL_TO);
            idCriterion.setEntityType(EntityTypeEnum.SUBJECT);
            idCriteria.getCriterionCollection().add(idCriterion);
        }
        return idCriteria;
    }

    /**
     * Sample Group Type enumeration.
     */
    protected enum SampleGroupType {
        /**
         * Default.
         */
        DEFAULT,
        /**
         * Others Subjects.
         */
        OTHERS_GROUP,
        /**
         * Control samples group.
         */
        CONTROL_GROUP;
    }

}
