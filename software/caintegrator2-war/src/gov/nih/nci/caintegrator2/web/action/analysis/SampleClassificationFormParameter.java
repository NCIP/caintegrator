/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.GenomicDataParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * A parameter that is used to divide samples into two or more classes.
 */
public class SampleClassificationFormParameter extends AbstractAnalysisFormParameter {

    private AnnotationFieldDescriptor classificationAnnotation;

    SampleClassificationFormParameter(GenePatternAnalysisForm form, AbstractParameterValue parameterValue) {
        super(form, parameterValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayType() {
        return "select";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if (classificationAnnotation == null) {
            return "";
        } else {
            return classificationAnnotation.getDefinition().getDisplayName();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) {
        if (StringUtils.isBlank(value)) {
            classificationAnnotation = null;
        } else {
            classificationAnnotation = getForm().getClassificationAnnotation(value);
        }
    }

    /**
     * @return the available choices
     */
    public Collection<String> getChoices() {
        List<String> choices = new ArrayList<String>();
        if (!isRequired()) {
            choices.add("");
        }
        choices.addAll(getForm().getClassificationAnnotationNames());
        return choices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureForInvocation(StudySubscription studySubscription,
                                       QueryManagementService queryManagementService) throws InvalidCriterionException {
        SampleClassificationParameterValue parameterValue =
            (SampleClassificationParameterValue) getParameterValue();
        parameterValue.clear();
        if (classificationAnnotation != null) {
            Query query = createClassificationQuery(studySubscription);
            QueryResult result = queryManagementService.execute(query);
            Map<Sample, String> sampleToClassificationNameMap = getSampleToClassificationNameMap(result);
            List<Sample> samples = getSamplesToClassify();
            for (Sample sample : samples) {
                parameterValue.classify(sample, sampleToClassificationNameMap.get(sample));
            }
        }
    }

    private List<Sample> getSamplesToClassify() {
        GenomicDataFormParameter genomicFormParameter = getAssociatedGenomicDataFormParameter();
        GenomicDataParameterValue genomicParamaterValue =
            (GenomicDataParameterValue) genomicFormParameter.getParameterValue();
        List<Sample> samples = new ArrayList<Sample>();
        for (GenomicDataResultColumn column : genomicParamaterValue.getGenomicData().getColumnCollection()) {
            samples.add(column.getSampleAcquisition().getSample());
        }
        return samples;
    }

    private GenomicDataFormParameter getAssociatedGenomicDataFormParameter() {
        // Finds most closely preceding GenomicDataFormParameter
        int parameterIndex = getForm().getParameters().indexOf(this);
        for (int i = parameterIndex - 1; i >= 0; i--) {
            if (getForm().getParameters().get(i) instanceof GenomicDataFormParameter) {
                return (GenomicDataFormParameter) getForm().getParameters().get(i);
            }
        }
        throw new IllegalStateException("No GenomicDataFormParameter found for SampleClassificationFormParameter");
    }

    private Map<Sample, String> getSampleToClassificationNameMap(QueryResult result) {
        Map<Sample, String> classificationMap = new HashMap<Sample, String>();
        for (ResultRow row : result.getRowCollection()) {
            ResultValue resultValue = row.getValueCollection().iterator().next();
            String classificationName = resultValue.getValue().toString();
            if (StringUtils.isBlank(classificationName)) {
                classificationName = "none";
            }
            if (row.getSampleAcquisition() != null) {
                classificationMap.put(row.getSampleAcquisition().getSample(), classificationName);
            }
        }
        return classificationMap;
    }

    private Query createClassificationQuery(StudySubscription studySubscription) {
        Query query = new Query();
        query.setResultType(ResultTypeEnum.CLINICAL);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setColumnCollection(new HashSet<ResultColumn>());
        ResultColumn column = new ResultColumn();
        column.setAnnotationFieldDescriptor(classificationAnnotation);
        column.setColumnIndex(0);
        query.getColumnCollection().add(column);
        ResultColumn sampleColumn = new ResultColumn();
        sampleColumn.setEntityType(EntityTypeEnum.SAMPLE);
        sampleColumn.setColumnIndex(1);
        query.getColumnCollection().add(sampleColumn);
        query.setSubscription(studySubscription);
        return query;
    }

}
