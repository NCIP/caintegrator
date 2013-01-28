/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator.application.analysis.SurvivalCalculator;
import gov.nih.nci.caintegrator.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalLengthUnitsEnum;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class SurvivalCalculatorTest {
    private static final String CENSORED_VALUE = "Alive";
    
    private StudySubjectAssignment subject1Uncensored;
    private StudySubjectAssignment subject2Censored;
    private SurvivalValueDefinition dateBasedSurvival;
    private SurvivalValueDefinition durationBasedSurvival;

    @Before
    public void setUp() throws Exception {
        
        subject1Uncensored = new StudySubjectAssignment();
        subject2Censored = new StudySubjectAssignment();
        
        AnnotationDefinition survivalStartDateAnnotation = new AnnotationDefinition();
        survivalStartDateAnnotation.setDisplayName("Survival Start Date");
        survivalStartDateAnnotation.setId(Long.valueOf(2));
        survivalStartDateAnnotation.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition deathDateAnnotation = new AnnotationDefinition();
        deathDateAnnotation.setDisplayName("Death Date");
        deathDateAnnotation.setId(Long.valueOf(3));
        deathDateAnnotation.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition lastFollowupDateAnnotation = new AnnotationDefinition();
        lastFollowupDateAnnotation.setDisplayName("Last Followup Date");
        lastFollowupDateAnnotation.setId(Long.valueOf(4));
        lastFollowupDateAnnotation.setDataType(AnnotationTypeEnum.DATE);
        
        AnnotationDefinition survivalLengthAnnotation = new AnnotationDefinition();
        survivalLengthAnnotation.setDataType(AnnotationTypeEnum.NUMERIC);
        survivalLengthAnnotation.setId(Long.valueOf(5));
        survivalLengthAnnotation.setDisplayName("Survival Length");
        
        AnnotationDefinition survivalStatusAnnotation = new AnnotationDefinition();
        survivalStatusAnnotation.setDataType(AnnotationTypeEnum.STRING);
        survivalStatusAnnotation.setId(Long.valueOf(6));
        survivalLengthAnnotation.setDisplayName("Survival Status");
        durationBasedSurvival = new SurvivalValueDefinition();
        durationBasedSurvival.setSurvivalValueType(SurvivalValueTypeEnum.LENGTH_OF_TIME);
        durationBasedSurvival.setSurvivalLength(survivalLengthAnnotation);
        durationBasedSurvival.setSurvivalStatus(survivalStatusAnnotation);
        durationBasedSurvival.setValueForCensored(CENSORED_VALUE);
        
        dateBasedSurvival = new SurvivalValueDefinition();
        dateBasedSurvival.setSurvivalValueType(SurvivalValueTypeEnum.DATE);
        dateBasedSurvival.setSurvivalStartDate(survivalStartDateAnnotation);
        dateBasedSurvival.setDeathDate(deathDateAnnotation);
        dateBasedSurvival.setLastFollowupDate(lastFollowupDateAnnotation);
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.DAYS);
        
        Calendar survivalStartDate = Calendar.getInstance();
        survivalStartDate.set(Calendar.YEAR, 2007);
        survivalStartDate.set(Calendar.MONTH, 1);
        survivalStartDate.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar survivalDeathDate = Calendar.getInstance();
        survivalDeathDate.set(Calendar.YEAR, 2008);
        survivalDeathDate.set(Calendar.MONTH, 3);
        survivalDeathDate.set(Calendar.DAY_OF_MONTH, 27);
        
        Calendar survivalLastFollowupDate = Calendar.getInstance();
        survivalLastFollowupDate.set(Calendar.YEAR, 2007);
        survivalLastFollowupDate.set(Calendar.MONTH, 6);
        survivalLastFollowupDate.set(Calendar.DAY_OF_MONTH, 15);
        
        subject1Uncensored.getSubjectAnnotationCollection().add(createDateSurvivalValue(survivalStartDateAnnotation, survivalStartDate));
        subject1Uncensored.getSubjectAnnotationCollection().add(createDateSurvivalValue(deathDateAnnotation, survivalDeathDate));
        subject1Uncensored.getSubjectAnnotationCollection().add(createDateSurvivalValue(lastFollowupDateAnnotation, survivalLastFollowupDate));
        subject1Uncensored.getSubjectAnnotationCollection().add(createNumericSurvivalValue(survivalLengthAnnotation, 100.5));
        subject1Uncensored.getSubjectAnnotationCollection().add(createStringSurvivalValue(survivalStatusAnnotation, null));
        
        subject2Censored.getSubjectAnnotationCollection().add(createDateSurvivalValue(survivalStartDateAnnotation, survivalStartDate));
        subject2Censored.getSubjectAnnotationCollection().add(createDateSurvivalValue(lastFollowupDateAnnotation, survivalLastFollowupDate));
        subject2Censored.getSubjectAnnotationCollection().add(createNumericSurvivalValue(survivalLengthAnnotation, 55.4));
        subject2Censored.getSubjectAnnotationCollection().add(createStringSurvivalValue(survivalStatusAnnotation, CENSORED_VALUE));
    }

    private SubjectAnnotation createDateSurvivalValue(AnnotationDefinition annotationDefinition, Calendar date) {
        DateAnnotationValue subject1SurvivalValue = new DateAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        if (date != null) {
            subject1SurvivalValue.setDateValue(date.getTime());
        }
        subject1SurvivalValue.setSubjectAnnotation(subjectAnnotation);
        subjectAnnotation.setAnnotationValue(subject1SurvivalValue);
        subject1SurvivalValue.setAnnotationDefinition(annotationDefinition);
        annotationDefinition.getAnnotationValueCollection().add(subject1SurvivalValue);
        return subjectAnnotation;
    }
    
    private SubjectAnnotation createNumericSurvivalValue(AnnotationDefinition annotationDefinition, Double survivalLength) {
        NumericAnnotationValue subjectSurvivalValue = new NumericAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        if (survivalLength != null) {
            subjectSurvivalValue.setNumericValue(survivalLength);
        }
        subjectSurvivalValue.setSubjectAnnotation(subjectAnnotation);
        subjectAnnotation.setAnnotationValue(subjectSurvivalValue);
        subjectSurvivalValue.setAnnotationDefinition(annotationDefinition);
        annotationDefinition.getAnnotationValueCollection().add(subjectSurvivalValue);
        return subjectAnnotation;
    }
    
    private SubjectAnnotation createStringSurvivalValue(AnnotationDefinition annotationDefinition, String censorStatus) {
        StringAnnotationValue subjectSurvivalValue = new StringAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        if (censorStatus != null) {
            subjectSurvivalValue.setStringValue(censorStatus);
        }
        subjectSurvivalValue.setSubjectAnnotation(subjectAnnotation);
        subjectAnnotation.setAnnotationValue(subjectSurvivalValue);
        subjectSurvivalValue.setAnnotationDefinition(annotationDefinition);
        annotationDefinition.getAnnotationValueCollection().add(subjectSurvivalValue);
        return subjectAnnotation;
    }

    @Test
    public void testCreateSubjectSurvivalDataDateBased() {
        SubjectSurvivalData survivalData1 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject1Uncensored);
        assertEquals(451, survivalData1.getSurvivalLength());
        assertFalse(survivalData1.isCensor());
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.MONTHS);
        survivalData1 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject1Uncensored);
        assertEquals(15, survivalData1.getSurvivalLength());
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.WEEKS);
        survivalData1 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject1Uncensored);
        assertEquals(64, survivalData1.getSurvivalLength());
        
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.DAYS);
        SubjectSurvivalData survivalData2 = null;
        try {
            survivalData2 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject2Censored);
            fail();
        } catch (IllegalArgumentException e) {
            // expected this exception, because there's no value for death date.
        }
        
        subject2Censored.getSubjectAnnotationCollection().add(createDateSurvivalValue(dateBasedSurvival.getDeathDate(), null));
        survivalData2 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject2Censored);
        assertEquals(164, survivalData2.getSurvivalLength());
        assertTrue(survivalData2.isCensor());
        
    }
    
    @Test
    public void testCreateSubjectSurvivalDataDurationBased() {
        SubjectSurvivalData survivalData1 = SurvivalCalculator.createSubjectSurvivalData(durationBasedSurvival, subject1Uncensored);
        assertEquals(101, survivalData1.getSurvivalLength());
        assertFalse(survivalData1.isCensor());
        
        SubjectSurvivalData survivalData2 = SurvivalCalculator.createSubjectSurvivalData(durationBasedSurvival, subject2Censored);
        assertEquals(55, survivalData2.getSurvivalLength());
        assertTrue(survivalData2.isCensor());
        
    }

}
