/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator2.application.analysis.GenomicDataParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.IntegerParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.StringParameterValue;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AnalysisFormTest {
    
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
        query.setResultType(ResultTypeEnum.GENOMIC);
        queries.add(query);
        formParameter3.getForm().setGenomicQueries(queries);
        studySubscription.setQueryCollection(new HashSet<Query>());
        studySubscription.getQueryCollection().add(query);
        
        Collection<AnnotationDefinition> annotations = new ArrayList<AnnotationDefinition>();
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setDisplayName("Test");
        annotations.add(definition);
        form.addClassificationAnnotations(annotations, EntityTypeEnum.SUBJECT);
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
        assertEquals("All Genomic Data", formParameter3.getValue());
        formParameter3.setValue("Test");
        assertEquals("Test", formParameter3.getValue());
        assertEquals(query, formParameter3.getSelectedQuery());
        assertEquals("", formParameter4.getValue());
        formParameter4.setValue("Test");
        assertEquals("Test", formParameter4.getValue());
        formParameter4.setValue("");
        assertEquals("", formParameter4.getValue());
    }
    
    @Test
    public void testConfigure() throws InvalidCriterionException {
        QueryManagementServiceStub queryManagementService = new QueryManagementServiceStub();
        formParameter3.configureForInvocation(studySubscription, queryManagementService);
        assertTrue(queryManagementService.executeGenomicDataQueryCalled);
        assertNotNull(((GenomicDataParameterValue) formParameter3.getParameterValue()).getGenomicData());
        queryManagementService.clear();
        formParameter3.setValue("Test");
        formParameter3.configureForInvocation(studySubscription, queryManagementService);
        assertTrue(queryManagementService.executeGenomicDataQueryCalled);
        assertNotNull(((GenomicDataParameterValue) formParameter3.getParameterValue()).getGenomicData());
 
        queryManagementService.clear();
        parameterValue3.setGenomicData(new GenomicDataQueryResult());
        formParameter4.setValue("Test");
        formParameter4.configureForInvocation(studySubscription, queryManagementService);
        assertTrue(queryManagementService.executeCalled);
    }
    
    @Test
    public void testGetAnalysisMethodInformationUrl() {
        form.setUrl("http://genepattern.broadinstitute.org/gp/services/Analysis?");
        assertEquals("http://genepattern.broadinstitute.org/gp/getTaskDoc.jsp", form.getAnalysisMethodInformationUrl());
    }

}
