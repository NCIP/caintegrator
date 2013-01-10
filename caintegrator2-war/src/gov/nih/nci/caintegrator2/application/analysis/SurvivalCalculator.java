/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a static utility class used for calculating survival values for a subject.
 */
public final class SurvivalCalculator {

    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_WEEK = 7;
    private static final int MONTHS_IN_YEAR = 12;
    private static final double DAYS_IN_YEAR = 365.25;
    private static final double DAYS_IN_MONTH = DAYS_IN_YEAR / MONTHS_IN_YEAR;
    private static final long ONE_HOUR = 60 * 60 * 1000L;


    private SurvivalCalculator() {
    }

    /**
     * Creates survival data based on the definition and subject.
     * @param survivalValueDefinition to calculate survival data for.
     * @param subjectAssignment subject to calculate survival data.
     * @return survival data.
     */
    public static SubjectSurvivalData createSubjectSurvivalData(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {
        if (SurvivalValueTypeEnum.DATE.equals(survivalValueDefinition.getSurvivalValueType())) {
            return SurvivalCalculator.createDateBasedSurvivalData(survivalValueDefinition, subjectAssignment);
        }
        if (SurvivalValueTypeEnum.LENGTH_OF_TIME.equals(survivalValueDefinition.getSurvivalValueType())) {
            return SurvivalCalculator.createDurationBasedSurvivalData(survivalValueDefinition, subjectAssignment);
        }
        throw new IllegalStateException("Unknown survival value type.");
    }

    private static SubjectSurvivalData createDateBasedSurvivalData(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {

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
        Integer survivalLength = calculateSurvivalLength(survivalValueDefinition, calSubjectStartDate,
                calSubjectEndDate);
        return new SubjectSurvivalData(survivalLength, censor);
    }

    private static Integer calculateSurvivalLength(SurvivalValueDefinition survivalValueDefinition,
            Calendar calSubjectStartDate, Calendar calSubjectEndDate) {
        Integer survivalLength = Integer.valueOf(0);
        Integer daysBetween = daysBetween(calSubjectStartDate, calSubjectEndDate);
        switch (survivalValueDefinition.getSurvivalLengthUnits()) {
        case DAYS:
            survivalLength = daysBetween;
            break;
        case WEEKS:
            survivalLength = Math.round(daysBetween / DAYS_IN_WEEK);
            break;
        case MONTHS:
            survivalLength = (int) Math.round(daysBetween / DAYS_IN_MONTH);
            break;
        default:
            throw new IllegalStateException("Unknown survival length type.");
        }
        return survivalLength;
    }

    private static SubjectSurvivalData createDurationBasedSurvivalData(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {
        NumericAnnotationValue survivalLength = subjectAssignment.getNumericAnnotation(
                survivalValueDefinition.getSurvivalLength());
        if (survivalLength == null || survivalLength.getNumericValue() == null) {
            return null;
        }
        return new SubjectSurvivalData((int) Math.round(survivalLength.getNumericValue()),
                getCensorStatusForDurationBasedSurvival(survivalValueDefinition, subjectAssignment));
    }

    private static Boolean getCensorStatusForDurationBasedSurvival(SurvivalValueDefinition survivalValueDefinition,
            StudySubjectAssignment subjectAssignment) {
        Boolean censor = false;
        if (survivalValueDefinition.getSurvivalStatus() != null
            && !StringUtils.isBlank(survivalValueDefinition.getValueForCensored())) {
            String survivalStatus = subjectAssignment.getAnnotationValueAsString(
                    survivalValueDefinition.getSurvivalStatus());
            try {
                censor = Double.valueOf(survivalValueDefinition.getValueForCensored()).
                    equals(Double.valueOf(survivalStatus));
            } catch (Exception e) {
                censor = survivalValueDefinition.getValueForCensored().equals(survivalStatus);
            }
        }
        return censor;
    }

    private static Boolean calculateEndDate(DateAnnotationValue subjectDeathDate,
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


    private static Integer daysBetween(Calendar startDate, Calendar endDate) {
        return Math.round(((endDate.getTimeInMillis() - startDate.getTimeInMillis() + ONE_HOUR))
                                / (ONE_HOUR * HOURS_IN_DAY));
    }

}
