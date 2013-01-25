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
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Calendar;
import java.util.Collection;

/**
 * Abstract class representing a Handler for KM Plot creation.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See calculateEndDate function
abstract class AbstractKMPlotHandler {
    
    private static final int MONTHS_IN_YEAR = 12;
    private final CaIntegrator2Dao dao;
    private final SurvivalValueDefinition survivalValueDefinition;
    private final QueryManagementService queryManagementService;
    
    protected AbstractKMPlotHandler(CaIntegrator2Dao dao, 
            SurvivalValueDefinition survivalValueDefinition, 
            QueryManagementService queryManagementService) {
        this.dao = dao;
        this.survivalValueDefinition = survivalValueDefinition;
        this.queryManagementService = queryManagementService;
    }
    
    public static AbstractKMPlotHandler createKMPlotHandler(CaIntegrator2Dao dao, 
                                                            SurvivalValueDefinition survivalValueDefinition, 
                                                            QueryManagementService queryManagementService,
                                                            AbstractKMParameters kmParameters) {
        if (kmParameters instanceof KMAnnotationBasedParameters) {
            return new AnnotationBasedKMPlotHandler(dao, 
                                                    survivalValueDefinition, 
                                                    queryManagementService, 
                                                    (KMAnnotationBasedParameters) kmParameters);
        } else if (kmParameters instanceof KMGeneExpressionBasedParameters) {
            return new GeneExpressionBasedKMPlotHandler(dao, 
                                                        survivalValueDefinition, 
                                                        queryManagementService, 
                                                        (KMGeneExpressionBasedParameters) kmParameters);
        } else if (kmParameters instanceof KMQueryBasedParameters) {
            return new QueryBasedKMPlotHandler(dao, 
                    survivalValueDefinition, 
                    queryManagementService, 
                    (KMQueryBasedParameters) kmParameters);
        }
        throw new IllegalArgumentException("Unknown Parameter Type");         
    }
    
    abstract KMPlot createPlot(KMPlotService kmPlotService, StudySubscription subscription) 
        throws InvalidCriterionException; 

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

    protected SubjectSurvivalData createSubjectSurvivalData(StudySubjectAssignment subjectAssignment) {
        Integer survivalLength = Integer.valueOf(0);
        DateAnnotationValue subjectSurvivalStartDate = null;
        DateAnnotationValue subjectDeathDate = null;
        DateAnnotationValue subjectLastFollowupDate = null;
        subjectSurvivalStartDate = subjectAssignment.getDateAnnotation(survivalValueDefinition.getSurvivalStartDate());
        subjectDeathDate = subjectAssignment.getDateAnnotation(survivalValueDefinition.getDeathDate());
        subjectLastFollowupDate = subjectAssignment.getDateAnnotation(survivalValueDefinition.getLastFollowupDate());
        Calendar calSubjectStartDate = Calendar.getInstance();
        Calendar calSubjectEndDate = Calendar.getInstance();
        if (subjectSurvivalStartDate != null && subjectSurvivalStartDate.getDateValue() != null) {
            calSubjectStartDate.setTime(subjectSurvivalStartDate.getDateValue());
        } else {
            return null;
        }
        Boolean censor = calculateEndDate(subjectDeathDate, subjectLastFollowupDate, calSubjectEndDate);
        if (censor == null) {
            return null;
        }
        survivalLength = monthsBetween(calSubjectStartDate, calSubjectEndDate);
        return new SubjectSurvivalData(survivalLength, censor);
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks are necessary
    protected Boolean calculateEndDate(DateAnnotationValue subjectDeathDate,
                                    DateAnnotationValue subjectLastFollowupDate, 
                                    Calendar calSubjectEndDate) {
        Boolean censor = false;
        if ((subjectDeathDate == null || subjectDeathDate.getDateValue() == null)
              && (subjectLastFollowupDate != null && subjectLastFollowupDate.getDateValue() != null)) {
            calSubjectEndDate.setTime(subjectLastFollowupDate.getDateValue());
            censor = true;
        } else if ((subjectDeathDate != null && subjectDeathDate.getDateValue() != null)) {
            calSubjectEndDate.setTime(subjectDeathDate.getDateValue());
            censor = false;
        } else {
            return null;
        }
        return censor;
    }
    
    protected Integer monthsBetween(Calendar startDate, Calendar endDate) {
        int yearsBetween = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
        int monthsBetween = endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
        return ((yearsBetween * MONTHS_IN_YEAR) + monthsBetween);
    }
    
    protected void validateSurvivalValueDefinition() {
        if (survivalValueDefinition == null) {
            throw new IllegalArgumentException("SurvivalValueDefinition cannot be null");
        }
        if (survivalValueDefinition.getSurvivalStartDate() == null
             || survivalValueDefinition.getDeathDate() == null 
             || survivalValueDefinition.getLastFollowupDate() == null) {
            throw new IllegalArgumentException("Must have a Start Date, Death Date, and Last Followup Date" 
                    + " defined for definition '" + survivalValueDefinition.getName() + "'.");
        }
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

}
