/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator.application.analysis.GenomicDataParameterValue;
import gov.nih.nci.caintegrator.application.analysis.IntegerParameterValue;
import gov.nih.nci.caintegrator.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator.application.analysis.StringParameterValue;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator.web.action.analysis.AbstractAnalysisFormParameter;
import gov.nih.nci.caintegrator.web.action.analysis.GenePatternAnalysisForm;
import gov.nih.nci.caintegrator.web.action.analysis.GenomicDataFormParameter;
import gov.nih.nci.caintegrator.web.action.analysis.SampleClassificationFormParameter;
import gov.nih.nci.caintegrator.web.action.analysis.SelectListFormParameter;
import gov.nih.nci.caintegrator.web.action.analysis.TextFieldFormParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AnalysisFormTest extends AbstractMockitoTest {

    private StringParameterValue parameterValue1;
    private IntegerParameterValue parameterValue2;
    private GenomicDataParameterValue parameterValue3;
    private SampleClassificationParameterValue parameterValue4;
    private TextFieldFormParameter formParameter1;
    private SelectListFormParameter formParameter2;
    private GenomicDataFormParameter formParameter3;
    private SampleClassificationFormParameter formParameter4;
    private StudySubscription studySubscription = new StudySubscription();
    private Query query = new Query();
    private GenePatternAnalysisForm form = new GenePatternAnalysisForm();

    @Before
    public void setUp() {
        AnalysisParameter parameter1 = new AnalysisParameter();
        parameter1.setName("parameter1");
        parameter1.setDescription("description1");
        parameter1.setRequired(false);
        parameter1.setType(AnalysisParameterType.STRING);

        AnalysisParameter parameter2 = new AnalysisParameter();
        parameter2.setName("parameter2");
        parameter2.setDescription("description2");
        parameter2.setRequired(false);
        parameter2.setType(AnalysisParameterType.INTEGER);

        parameter2.addChoice("1", "1");
        parameter2.addChoice("2", "2");

        AnalysisParameter parameter3 = new AnalysisParameter();
        parameter3.setType(AnalysisParameterType.GENOMIC_DATA);
        parameter3.setName("parameter3");

        AnalysisParameter parameter4 = new AnalysisParameter();
        parameter4.setType(AnalysisParameterType.SAMPLE_CLASSIFICATION);
        parameter4.setName("parameter4");

        AnalysisMethod method = new AnalysisMethod();
        method.setName("method");
        method.getParameters().add(parameter1);
        method.getParameters().add(parameter2);
        method.getParameters().add(parameter3);
        method.getParameters().add(parameter4);
        List<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
        methods.add(method);
        form.setAnalysisMethods(methods);
        form.setAnalysisMethodName("method");
        parameterValue1 = (StringParameterValue) form.getInvocation().getParameterValue(parameter1);
        parameterValue2 = (IntegerParameterValue) form.getInvocation().getParameterValue(parameter2);
        parameterValue3 = (GenomicDataParameterValue) form.getInvocation().getParameterValue(parameter3);
        parameterValue4 = (SampleClassificationParameterValue) form.getInvocation().getParameterValue(parameter4);

        parameterValue1.setValue("value1");

        formParameter1 = (TextFieldFormParameter) AbstractAnalysisFormParameter.create(form, parameterValue1);
        formParameter2 = (SelectListFormParameter) AbstractAnalysisFormParameter.create(form, parameterValue2);
        formParameter3 = (GenomicDataFormParameter) AbstractAnalysisFormParameter.create(form, parameterValue3);
        formParameter4 = (SampleClassificationFormParameter) AbstractAnalysisFormParameter.create(form, parameterValue4);
        form.getParameters().add(formParameter1);
        form.getParameters().add(formParameter2);
        form.getParameters().add(formParameter3);
        form.getParameters().add(formParameter4);

        List<Query> queries = new ArrayList<Query>();
        query.setId(1L);
        query.setName("Test");
        query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        queries.add(query);
        formParameter3.getForm().setGenomicQueries(queries);
        studySubscription.getQueryCollection().add(query);

        Collection<AnnotationFieldDescriptor> annotations = new ArrayList<AnnotationFieldDescriptor>();
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        AnnotationDefinition definition = new AnnotationDefinition();
        afd.setDefinition(definition);
        definition.setDisplayName("Test");
        annotations.add(afd);
        form.addClassificationAnnotations(annotations);
    }

    @Test
    public void testGetters() {
        assertEquals("description1", formParameter1.getDescription());
        assertEquals("textfield", formParameter1.getDisplayType());
        assertEquals("select", formParameter2.getDisplayType());
        assertEquals("parameter1", formParameter1.getName());
        assertEquals("value1", formParameter1.getValue());
        assertEquals(2, formParameter2.getChoices().size());
        assertTrue(formParameter2.getChoices().contains("2"));
        assertEquals(2, formParameter3.getChoices().size());
        assertTrue(formParameter3.getChoices().contains("All Genomic Data"));
        assertEquals("select", formParameter4.getDisplayType());
        assertEquals(2, formParameter4.getChoices().size());
    }

    @Test
    public void testSetValue() {
        formParameter1.setValue("new value");
        assertEquals("new value", formParameter1.getParameterValue().getValueAsString());
        formParameter2.setValue("2");
        assertEquals("2", formParameter2.getParameterValue().getValueAsString());
        assertEquals("2", form.getInvocation().getParameterValue(parameterValue2.getParameter()).getValueAsString());
        formParameter2.setValue("1");
        assertEquals("1", formParameter2.getParameterValue().getValueAsString());
        assertEquals("1", form.getInvocation().getParameterValue(parameterValue2.getParameter()).getValueAsString());
        assertEquals("1", formParameter2.getValue());
        assertEquals("1", formParameter2.getValue());
        assertEquals("", formParameter3.getValue());
        formParameter3.setValue("Test");
        assertEquals("Test", formParameter3.getValue());
        assertEquals(query, formParameter3.getSelectedQuery());

        formParameter3.setValue("All Genomic Data for Platform: SOME_PLATFORM");
        assertEquals("All Genomic Data for Platform: SOME_PLATFORM", formParameter3.getValue());
        assertEquals(null, formParameter3.getSelectedQuery());
        assertEquals("SOME_PLATFORM", formParameter3.getSelectedPlatform());

        assertEquals("", formParameter4.getValue());
        formParameter4.setValue("Test");
        assertEquals("Test", formParameter4.getValue());
        formParameter4.setValue("");
        assertEquals("", formParameter4.getValue());
    }

    @Test
    public void testConfigure() throws InvalidCriterionException {
        formParameter3.configureForInvocation(studySubscription, queryManagementService);
        verify(queryManagementService, times(1)).executeGenomicDataQuery(any(Query.class));
        assertNotNull(((GenomicDataParameterValue) formParameter3.getParameterValue()).getGenomicData());

        formParameter3.setValue("Test");
        formParameter3.configureForInvocation(studySubscription, queryManagementService);
        verify(queryManagementService, times(2)).executeGenomicDataQuery(any(Query.class));
        assertNotNull(((GenomicDataParameterValue) formParameter3.getParameterValue()).getGenomicData());

        parameterValue3.setGenomicData(new GenomicDataQueryResult());
        formParameter4.setValue("Test");
        formParameter4.configureForInvocation(studySubscription, queryManagementService);
        verify(queryManagementService, times(1)).execute(any(Query.class));
    }

    @Test
    public void testGetAnalysisMethodInformationUrl() {
        form.setUrl("http://genepattern.broadinstitute.org/gp/services/Analysis?");
        assertEquals("http://genepattern.broadinstitute.org/gp/getTaskDoc.jsp", form.getAnalysisMethodInformationUrl());
    }

    @Test
    public void testLogBasTwoParameter() {
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setType(AnalysisParameterType.STRING);
        parameter.setName("log.base.two");
        parameter.addChoice("no", "false");
        parameter.addChoice("yes", "true");
        parameter.setDefaultValue(parameter.getChoice("false"));

        StringParameterValue parameterValue = new StringParameterValue();
        parameterValue.setParameter(parameter);
        parameterValue.setValue("false");

        SelectListFormParameter formParameter = (SelectListFormParameter) AbstractAnalysisFormParameter.create(form, parameterValue);
        assertEquals("log.base.two", formParameter.getName());
        assertEquals("yes", formParameter.getValue());
    }

}
