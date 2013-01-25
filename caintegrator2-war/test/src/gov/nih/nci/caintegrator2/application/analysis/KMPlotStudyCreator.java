/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class KMPlotStudyCreator {
    
    private AnnotationDefinition groupAnnotationField;
    private AnnotationFieldDescriptor groupAnnotationFieldDescriptor;
    private Collection <PermissibleValue> plotGroupValues;
    private SurvivalValueDefinition survivalValueDefinition;
    private Set<StudySubjectAssignment> usedSubjects = new HashSet<StudySubjectAssignment>();
    
    public Study createKMPlotStudy() {
        Study kmPlotStudy = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        kmPlotStudy.setStudyConfiguration(studyConfiguration);
        GenomicDataSourceConfiguration genomicDataSource = new GenomicDataSourceConfiguration();
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName("samples");
        genomicDataSource.getControlSampleSetCollection().add(sampleSet);
        studyConfiguration.getGenomicDataSources().add(genomicDataSource);
        StudySubjectAssignment male1 = new StudySubjectAssignment();
        male1.setId(Long.valueOf(1));
        StudySubjectAssignment male2 = new StudySubjectAssignment();
        male2.setId(Long.valueOf(2));
        StudySubjectAssignment female1 = new StudySubjectAssignment();
        female1.setId(Long.valueOf(3));
        kmPlotStudy.getAssignmentCollection().add(male1);
        kmPlotStudy.getAssignmentCollection().add(male2);
        kmPlotStudy.getAssignmentCollection().add(female1);
        
        SubjectAnnotation male1SubjectAnnotation = new SubjectAnnotation();
        SubjectAnnotation male2SubjectAnnotation = new SubjectAnnotation();
        SubjectAnnotation female1SubjectAnnotation = new SubjectAnnotation();
        SubjectAnnotation male1SurvivalStartDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation male1DeathDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation male1LastFollowupDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation male2SurvivalStartDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation male2DeathDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation male2LastFollowupDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation female1SurvivalStartDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation female1DeathDateAnnotation = new SubjectAnnotation();
        SubjectAnnotation female1LastFollowupDateAnnotation = new SubjectAnnotation();
        
        male1SubjectAnnotation.setStudySubjectAssignment(male1);
        male2SubjectAnnotation.setStudySubjectAssignment(male2);
        female1SubjectAnnotation.setStudySubjectAssignment(female1);
        
        male1SurvivalStartDateAnnotation.setStudySubjectAssignment(male1);
        male1DeathDateAnnotation.setStudySubjectAssignment(male1);
        male1LastFollowupDateAnnotation.setStudySubjectAssignment(male1);
        
        male2SurvivalStartDateAnnotation.setStudySubjectAssignment(male2);
        male2DeathDateAnnotation.setStudySubjectAssignment(male2);
        male2LastFollowupDateAnnotation.setStudySubjectAssignment(male2);
        
        female1SurvivalStartDateAnnotation.setStudySubjectAssignment(female1);
        female1DeathDateAnnotation.setStudySubjectAssignment(female1);
        female1LastFollowupDateAnnotation.setStudySubjectAssignment(female1);
        
        male1.getSubjectAnnotationCollection().add(male1SubjectAnnotation);
        male1.getSubjectAnnotationCollection().add(male1SurvivalStartDateAnnotation);
        male1.getSubjectAnnotationCollection().add(male1DeathDateAnnotation);
        male1.getSubjectAnnotationCollection().add(male1LastFollowupDateAnnotation);
        
        male2.getSubjectAnnotationCollection().add(male2SubjectAnnotation);
        male2.getSubjectAnnotationCollection().add(male2SurvivalStartDateAnnotation);
        male2.getSubjectAnnotationCollection().add(male2DeathDateAnnotation);
        male2.getSubjectAnnotationCollection().add(male2LastFollowupDateAnnotation);
        
        female1.getSubjectAnnotationCollection().add(female1SubjectAnnotation);
        female1.getSubjectAnnotationCollection().add(female1SurvivalStartDateAnnotation);
        female1.getSubjectAnnotationCollection().add(female1DeathDateAnnotation);
        female1.getSubjectAnnotationCollection().add(female1LastFollowupDateAnnotation);
        
        groupAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        groupAnnotationFieldDescriptor.setId(Long.valueOf(1));
        groupAnnotationFieldDescriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        // Add annotation values
        groupAnnotationField = new AnnotationDefinition();
        groupAnnotationFieldDescriptor.setDefinition(groupAnnotationField);
        groupAnnotationField.setDisplayName("Gender");
        groupAnnotationField.setId(Long.valueOf(1));
                
        StringAnnotationValue male1Value = new StringAnnotationValue();
        male1Value.setStringValue("M");
        male1Value.setSubjectAnnotation(male1SubjectAnnotation);
        male1SubjectAnnotation.setAnnotationValue(male1Value);
        male1Value.setAnnotationDefinition(groupAnnotationField);
        groupAnnotationField.getAnnotationValueCollection().add(male1Value);
        
        StringAnnotationValue male2Value = new StringAnnotationValue();
        male2Value.setStringValue("M");
        male2Value.setSubjectAnnotation(male2SubjectAnnotation);
        male2SubjectAnnotation.setAnnotationValue(male2Value);
        male2Value.setAnnotationDefinition(groupAnnotationField);
        groupAnnotationField.getAnnotationValueCollection().add(male2Value);
        
        StringAnnotationValue female1Value = new StringAnnotationValue();
        female1Value.setStringValue("F");
        female1Value.setSubjectAnnotation(female1SubjectAnnotation);
        female1SubjectAnnotation.setAnnotationValue(female1Value);
        female1Value.setAnnotationDefinition(groupAnnotationField);
        groupAnnotationField.getAnnotationValueCollection().add(female1Value);
        
        // Add the permissible values
        plotGroupValues = new HashSet<PermissibleValue>();
        PermissibleValue malePermissibleValue = new PermissibleValue();
        malePermissibleValue.setValue("M");
        PermissibleValue femalePermissibleValue = new PermissibleValue();
        femalePermissibleValue.setValue("F");
        plotGroupValues.add(malePermissibleValue);
        plotGroupValues.add(femalePermissibleValue);
        PermissibleValue invalidPermissibleValue1 = new PermissibleValue();
        invalidPermissibleValue1.setValue("INVALID1");
        PermissibleValue invalidPermissibleValue2 = new PermissibleValue();
        invalidPermissibleValue2.setValue("INVALID2");
        PermissibleValue invalidPermissibleValue3 = new PermissibleValue();
        invalidPermissibleValue3.setValue("INVALID3");
        PermissibleValue invalidPermissibleValue4 = new PermissibleValue();
        invalidPermissibleValue4.setValue("INVALID4");
        PermissibleValue invalidPermissibleValue5 = new PermissibleValue();
        invalidPermissibleValue5.setValue("INVALID5");
        PermissibleValue invalidPermissibleValue6 = new PermissibleValue();
        invalidPermissibleValue6.setValue("INVALID6");
        PermissibleValue invalidPermissibleValue7 = new PermissibleValue();
        invalidPermissibleValue7.setValue("INVALID7");
        PermissibleValue invalidPermissibleValue8 = new PermissibleValue();
        invalidPermissibleValue8.setValue("INVALID8");
        plotGroupValues.add(invalidPermissibleValue1);
        plotGroupValues.add(invalidPermissibleValue2);
        plotGroupValues.add(invalidPermissibleValue3);
        plotGroupValues.add(invalidPermissibleValue4);
        plotGroupValues.add(invalidPermissibleValue5);
        plotGroupValues.add(invalidPermissibleValue6);
        plotGroupValues.add(invalidPermissibleValue7);
        plotGroupValues.add(invalidPermissibleValue8);

        
        // Add survival items
        survivalValueDefinition = new SurvivalValueDefinition();
        survivalValueDefinition.setId(Long.valueOf(1));
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
        survivalValueDefinition.setSurvivalStartDate(survivalStartDateAnnotation);
        survivalValueDefinition.setDeathDate(deathDateAnnotation);
        survivalValueDefinition.setLastFollowupDate(lastFollowupDateAnnotation);
        
        Calendar survivalStartDates = Calendar.getInstance();
        survivalStartDates.set(Calendar.YEAR, 2007);
        survivalStartDates.set(Calendar.MONTH, 1);
        survivalStartDates.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar maleDeathDates = Calendar.getInstance();
        maleDeathDates.set(Calendar.YEAR, 2008);
        maleDeathDates.set(Calendar.MONTH, 1);
        maleDeathDates.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar male1LastFollowupDate = Calendar.getInstance();
        male1LastFollowupDate.set(Calendar.YEAR, 2007);
        male1LastFollowupDate.set(Calendar.MONTH, 6);
        male1LastFollowupDate.set(Calendar.DAY_OF_MONTH, 15);
        
        Calendar femaleDeathDate = Calendar.getInstance();
        femaleDeathDate.set(Calendar.YEAR, 2008);
        femaleDeathDate.set(Calendar.MONTH, 2);
        femaleDeathDate.set(Calendar.DAY_OF_MONTH, 1);
        
        DateAnnotationValue male1SurvivalStartDateValue = new DateAnnotationValue();
        male1SurvivalStartDateValue.setDateValue(survivalStartDates.getTime());
        male1SurvivalStartDateValue.setSubjectAnnotation(male1SurvivalStartDateAnnotation);
        male1SurvivalStartDateAnnotation.setAnnotationValue(male1SurvivalStartDateValue);
        male1SurvivalStartDateValue.setAnnotationDefinition(survivalStartDateAnnotation);
        survivalStartDateAnnotation.getAnnotationValueCollection().add(male1SurvivalStartDateValue);
        
        DateAnnotationValue male2SurvivalStartDateValue = new DateAnnotationValue();
        male2SurvivalStartDateValue.setDateValue(survivalStartDates.getTime());
        male2SurvivalStartDateValue.setSubjectAnnotation(male2SurvivalStartDateAnnotation);
        male2SurvivalStartDateAnnotation.setAnnotationValue(male2SurvivalStartDateValue);
        male2SurvivalStartDateValue.setAnnotationDefinition(survivalStartDateAnnotation);
        survivalStartDateAnnotation.getAnnotationValueCollection().add(male2SurvivalStartDateValue);
        
        DateAnnotationValue female1SurvivalStartDateValue = new DateAnnotationValue();
        female1SurvivalStartDateValue.setDateValue(survivalStartDates.getTime());
        female1SurvivalStartDateValue.setSubjectAnnotation(female1SurvivalStartDateAnnotation);
        female1SurvivalStartDateAnnotation.setAnnotationValue(female1SurvivalStartDateValue);
        female1SurvivalStartDateValue.setAnnotationDefinition(survivalStartDateAnnotation);
        survivalStartDateAnnotation.getAnnotationValueCollection().add(female1SurvivalStartDateValue);
        
        DateAnnotationValue male1DeathDateValue = new DateAnnotationValue();
        // Leave this blank to let it catch the lastFollowupDate instead.
//        male1DeathDateValue.setDateValue(maleDeathDates.getTime());
        male1DeathDateValue.setSubjectAnnotation(male1DeathDateAnnotation);
        male1DeathDateAnnotation.setAnnotationValue(male1DeathDateValue);
        male1DeathDateValue.setAnnotationDefinition(deathDateAnnotation);
        deathDateAnnotation.getAnnotationValueCollection().add(male1DeathDateValue);
        
        DateAnnotationValue male2DeathDateValue = new DateAnnotationValue();
        male2DeathDateValue.setDateValue(maleDeathDates.getTime());
        male2DeathDateValue.setSubjectAnnotation(male2DeathDateAnnotation);
        male2DeathDateAnnotation.setAnnotationValue(male2DeathDateValue);
        male2DeathDateValue.setAnnotationDefinition(deathDateAnnotation);
        deathDateAnnotation.getAnnotationValueCollection().add(male2DeathDateValue);
        
        DateAnnotationValue female1DeathDateValue = new DateAnnotationValue();
        female1DeathDateValue.setDateValue(femaleDeathDate.getTime());
        female1DeathDateValue.setSubjectAnnotation(female1DeathDateAnnotation);
        female1DeathDateAnnotation.setAnnotationValue(female1DeathDateValue);
        female1DeathDateValue.setAnnotationDefinition(deathDateAnnotation);
        deathDateAnnotation.getAnnotationValueCollection().add(female1DeathDateValue);
        
        DateAnnotationValue male1LastFollowupDateValue = new DateAnnotationValue();
        male1LastFollowupDateValue.setDateValue(male1LastFollowupDate.getTime());
        male1LastFollowupDateValue.setSubjectAnnotation(male1LastFollowupDateAnnotation);
        male1LastFollowupDateAnnotation.setAnnotationValue(male1LastFollowupDateValue);
        male1LastFollowupDateValue.setAnnotationDefinition(lastFollowupDateAnnotation);
        lastFollowupDateAnnotation.getAnnotationValueCollection().add(male1LastFollowupDateValue);
        
        
        DateAnnotationValue male2LastFollowupDateValue = new DateAnnotationValue();
        male2LastFollowupDateValue.setDateValue(maleDeathDates.getTime());
        male2LastFollowupDateValue.setSubjectAnnotation(male2LastFollowupDateAnnotation);
        male2LastFollowupDateAnnotation.setAnnotationValue(male2LastFollowupDateValue);
        male2LastFollowupDateValue.setAnnotationDefinition(lastFollowupDateAnnotation);
        lastFollowupDateAnnotation.getAnnotationValueCollection().add(male2LastFollowupDateValue);
        
        DateAnnotationValue female1LastFollowupDateValue = new DateAnnotationValue();
        female1LastFollowupDateValue.setDateValue(femaleDeathDate.getTime());
        female1LastFollowupDateValue.setSubjectAnnotation(female1LastFollowupDateAnnotation);
        female1LastFollowupDateAnnotation.setAnnotationValue(female1LastFollowupDateValue);
        female1LastFollowupDateValue.setAnnotationDefinition(lastFollowupDateAnnotation);
        lastFollowupDateAnnotation.getAnnotationValueCollection().add(female1LastFollowupDateValue);
        
        return kmPlotStudy;
    }
    
    public QueryResult retrieveQueryResultForAnnotationBased(Query query) {
        Study study = query.getSubscription().getStudy();
        QueryResult result = new QueryResult();
        result.setRowCollection(new HashSet<ResultRow>());
        Set<AnnotationDefinition> groupAnnotationFields = new HashSet<AnnotationDefinition>();
        for (ResultColumn column : query.getColumnCollection()) {
            groupAnnotationFields.add(column.getAnnotationDefinition());
        }
        for (StudySubjectAssignment assignment : study.getAssignmentCollection()) {
            ResultRow row = new ResultRow();
            row.setSubjectAssignment(assignment);
            row.setValueCollection(new HashSet<ResultValue>());
            result.getRowCollection().add(row);
            for(SubjectAnnotation annotation : assignment.getSubjectAnnotationCollection()) {
                AnnotationDefinition currentAnnotationDefinition = annotation.getAnnotationValue().getAnnotationDefinition();
                if (groupAnnotationFields.contains(currentAnnotationDefinition)) {
                    ResultValue value = new ResultValue();
                    value.setValue(annotation.getAnnotationValue());
                    ResultColumn column = new ResultColumn();
                    AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
                    annotationFieldDescriptor.setId(1l);
                    annotationFieldDescriptor.setDefinition(currentAnnotationDefinition);
                    annotationFieldDescriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
                    column.setAnnotationFieldDescriptor(annotationFieldDescriptor);
                    value.setColumn(column);
                    row.getValueCollection().add(value);
                }
            }
        }
        return result;
        
    }
    
    /**
     * Completely fake results by just giving it all subjects in the study.
     * @param query
     * @return
     */
    public QueryResult retrieveFakeQueryResults(Query query) {
        Study study = query.getSubscription().getStudy();
        QueryResult result = new QueryResult();
        result.setRowCollection(new HashSet<ResultRow>());
        for (StudySubjectAssignment assignment : study.getAssignmentCollection()) {
            if (!usedSubjects.contains(assignment)) {
                ResultRow row = new ResultRow();
                row.setSubjectAssignment(assignment);
                row.setValueCollection(new HashSet<ResultValue>());
                result.getRowCollection().add(row);
                usedSubjects.add(assignment);
                break;
            }
        }
        return result;
    }

    /**
     * @return the groupAnnotationField
     */
    public AnnotationDefinition getGroupAnnotationField() {
        return groupAnnotationField;
    }

    /**
     * @param groupAnnotationField the groupAnnotationField to set
     */
    public void setGroupAnnotationField(AnnotationDefinition groupAnnotationField) {
        this.groupAnnotationField = groupAnnotationField;
    }
    
    public AnnotationFieldDescriptor getGroupAnnotationFieldDescriptor() {
        return groupAnnotationFieldDescriptor;
    }

    /**
     * @return the plotGroupValues
     */
    public Collection<PermissibleValue> getPlotGroupValues() {
        return plotGroupValues;
    }

    /**
     * @param plotGroupValues the plotGroupValues to set
     */
    public void setPlotGroupValues(Collection<PermissibleValue> plotGroupValues) {
        this.plotGroupValues = plotGroupValues;
    }

    /**
     * @return the survivalValueDefinition
     */
    public SurvivalValueDefinition getSurvivalValueDefinition() {
        return survivalValueDefinition;
    }

    /**
     * @param survivalValueDefinition the survivalValueDefinition to set
     */
    public void setSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition) {
        this.survivalValueDefinition = survivalValueDefinition;
    }

}
