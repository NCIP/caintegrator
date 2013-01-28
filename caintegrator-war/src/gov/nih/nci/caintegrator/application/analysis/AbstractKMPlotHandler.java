/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalLengthUnitsEnum;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.Collection;

/**
 * Abstract class representing a Handler for KM Plot creation.
 */
abstract class AbstractKMPlotHandler {

    private final CaIntegrator2Dao dao;
    private final SurvivalValueDefinition survivalValueDefinition;
    private final QueryManagementService queryManagementService;
    private final StudySubscription studySubscription;

    protected AbstractKMPlotHandler(StudySubscription studySubscription, CaIntegrator2Dao dao,
            SurvivalValueDefinition survivalValueDefinition,
            QueryManagementService queryManagementService) {
        this.studySubscription = studySubscription;
        this.dao = dao;
        this.survivalValueDefinition = survivalValueDefinition;
        this.queryManagementService = queryManagementService;
    }

    public static AbstractKMPlotHandler createKMPlotHandler(StudySubscription studySubscription,
                                                            CaIntegrator2Dao dao,
                                                            SurvivalValueDefinition survivalValueDefinition,
                                                            QueryManagementService queryManagementService,
                                                            AbstractKMParameters kmParameters) {
        if (kmParameters instanceof KMAnnotationBasedParameters) {
            return new AnnotationBasedKMPlotHandler(studySubscription, dao,
                                                    survivalValueDefinition,
                                                    queryManagementService,
                                                    (KMAnnotationBasedParameters) kmParameters);
        } else if (kmParameters instanceof KMGeneExpressionBasedParameters) {
            return new GeneExpressionBasedKMPlotHandler(studySubscription, dao,
                                                        survivalValueDefinition,
                                                        queryManagementService,
                                                        (KMGeneExpressionBasedParameters) kmParameters);
        } else if (kmParameters instanceof KMQueryBasedParameters) {
            return new QueryBasedKMPlotHandler(studySubscription, dao,
                    survivalValueDefinition,
                    queryManagementService,
                    (KMQueryBasedParameters) kmParameters);
        }
        throw new IllegalArgumentException("Unknown Parameter Type");
    }

    abstract KMPlot createPlot(KMPlotService kmPlotService) throws InvalidCriterionException,
            InvalidSurvivalValueDefinitionException;

    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // Default implementation is null
    void setupAndValidateParameters(AnalysisService analysisService) throws GenesNotFoundInStudyException,
            InvalidCriterionException {
        // no-op : default implementation is no-op, override if necessary.
    }

    protected void filterGroupsWithoutSurvivalData(KMPlotConfiguration configuration,
            Collection<SubjectGroup> subjectGroupCollection) {
        for (SubjectGroup group : subjectGroupCollection) {
            if (group.getSurvivalData().isEmpty()) {
                configuration.getFilteredGroups().add(group);
            } else {
                configuration.getGroups().add(group);
            }
        }
    }

    protected String getDurationLabel() {
        if (SurvivalLengthUnitsEnum.DAYS.equals(survivalValueDefinition.getSurvivalLengthUnits())) {
            return KMPlotConfiguration.DAYS_DURATION_LABEL;
        } else if (SurvivalLengthUnitsEnum.WEEKS.equals(survivalValueDefinition.getSurvivalLengthUnits())) {
            return KMPlotConfiguration.WEEKS_DURATION_LABEL;
        }
        return KMPlotConfiguration.MONTHS_DURATION_LABEL;
    }

    protected SubjectSurvivalData createSubjectSurvivalData(StudySubjectAssignment subjectAssignment) {
        return SurvivalCalculator.createSubjectSurvivalData(survivalValueDefinition, subjectAssignment);
    }

    protected void validateSurvivalValueDefinition() throws InvalidSurvivalValueDefinitionException {
        if (survivalValueDefinition == null) {
            throw new InvalidSurvivalValueDefinitionException("SurvivalValueDefinition cannot be null");
        }
        Cai2Util.validateSurvivalValueDefinition(survivalValueDefinition);
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @return the survivalValueDefinition
     */
    public SurvivalValueDefinition getSurvivalValueDefinition() {
        return survivalValueDefinition;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @return the studySubscription
     */
    public StudySubscription getStudySubscription() {
        return studySubscription;
    }

}
