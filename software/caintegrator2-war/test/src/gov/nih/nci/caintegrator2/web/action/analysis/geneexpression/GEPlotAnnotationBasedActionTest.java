/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class GEPlotAnnotationBasedActionTest {
    private GEPlotAnnotationBasedAction action;
    private StudyManagementServiceStub studyManagementServiceStub = new StudyManagementServiceStub();
    private AnalysisServiceStub analysisServiceStub = new AnalysisServiceStub();
    
    private PermissibleValue val1 = new PermissibleValue();
    private PermissibleValue val2 = new PermissibleValue();
    
    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        StudySubscription subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        subscription.setQueryCollection(new HashSet<Query>());
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new GEPlotAnnotationBasedAction();
        action.setAnalysisService(analysisServiceStub);
        WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        
        action.setStudyManagementService(studyManagementServiceStub);
        studyManagementServiceStub.clear();
        analysisServiceStub.clear();
    }

    @SuppressWarnings("deprecation") // Use dummy AnnotationFile for testing
    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        AnnotationDefinition subjectDef1 = new AnnotationDefinition();
        subjectDef1.setId(Long.valueOf(1));
        val1.setId(Long.valueOf(1));
        val1.setValue("M");
        val2.setId(Long.valueOf(2));
        val2.setValue("F");
        subjectDef1.getPermissibleValueCollection().add(val1);
        subjectDef1.getPermissibleValueCollection().add(val2);
        subjectDef1.getCommonDataElement().getValueDomain().getPermissibleValueCollection().add(new PermissibleValue());
        AnnotationDefinition subjectDef2 = new AnnotationDefinition();
        subjectDef2.setId(Long.valueOf(2));
        study.getSubjectAnnotationCollection().add(subjectDef1);
        study.getSubjectAnnotationCollection().add(subjectDef2);
        
        DelimitedTextClinicalSourceConfiguration clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        studyConfiguration.getClinicalConfigurationCollection().add(clinicalConf);
        AnnotationFile annotationFile = new AnnotationFile();
        clinicalConf.setAnnotationFile(annotationFile);

        addColumn(annotationFile, subjectDef1);
        addColumn(annotationFile, subjectDef2);
        
        return study;
    }
    
    private void addColumn(AnnotationFile annotationFile, AnnotationDefinition subjectDef) {
        FileColumn column = new FileColumn();
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setShownInBrowse(true);
        fieldDescriptor.setDefinition(subjectDef);
        column.setFieldDescriptor(fieldDescriptor);
        annotationFile.getColumns().add(column);
    }
    
    @Test
    public void testPrepare() {
        setupActionVariables();
        action.getGePlotForm().getAnnotationBasedForm().setGeneSymbol("EGFR");
        action.prepare();
        assertEquals("EGFR", action.getPlotParameters().getGeneSymbol());
    }
    
    @Test
    public void testValidate() {
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
        action.getCurrentStudy().setStudyConfiguration(new StudyConfiguration());
        action.getCurrentStudy().getStudyConfiguration().setStatus(Status.DEPLOYED);
        GenomicDataSourceConfiguration gdsc =  new GenomicDataSourceConfiguration();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(gdsc);
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().isEmpty());
        gdsc.setDataType(GenomicDataSourceDataTypeEnum.COPY_NUMBER);
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
    }

    @Test
    public void testInput() {
        assertEquals(ActionSupport.SUCCESS, action.input());
    }
    
    @Test
    public void testUpdateAnnotationDefinitions() {
        // Invalid because thre's not an Annotation EntityType selected.
        assertEquals(ActionSupport.INPUT, action.updateAnnotationDefinitions());
        action.getGePlotForm().getAnnotationBasedForm().setAnnotationTypeSelection(EntityTypeEnum.SUBJECT.getValue());
        assertEquals(ActionSupport.SUCCESS, action.updateAnnotationDefinitions());
        assertEquals(1, action.getGePlotForm().getAnnotationBasedForm().getAnnotationDefinitions().size());
        assertTrue(action.getGePlotForm().getAnnotationBasedForm().getAnnotationDefinitions().containsKey("1"));
    }
    
    @Test
    public void testGetDisplayTab() {
        setupActionVariables();
        assertEquals("annotationTab", action.getDisplayTab());
        action.setDisplayTab("Testing");
        assertEquals("Testing", action.getDisplayTab());
    }
    
    @Test
    public void testGetAnnotationTypes() {
        assertEquals(1, action.getAnnotationTypes().size());
        action.getCurrentStudy().getImageSeriesAnnotationCollection().add(new AnnotationDefinition());
        assertEquals(2, action.getAnnotationTypes().size());
    }
    
    @Test
    public void testUpdatePermissibleValues() {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.updatePermissibleValues());
    }
    
    @Test
    public void testCreatePlot() throws InterruptedException {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        action.setCreatePlotSelected(true);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getPlotParameters().getSelectedValues().clear();
        action.getPlotParameters().getSelectedValues().add(val1);
        action.getPlotParameters().getSelectedValues().add(val2);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getPlotParameters().setGeneSymbol("EGFR");
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        assertTrue(analysisServiceStub.createGEPlotCalled);
        assertFalse(action.isCreatable());
        
        action.getGePlotForm().getAnnotationBasedForm().setSelectedAnnotationId("1");
        action.getGePlotForm().getAnnotationBasedForm().setAnnotationTypeSelection(EntityTypeEnum.SUBJECT.getValue());
        assertTrue(action.isCreatable());
    }
    
    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(action.getGePlotForm().getAnnotationBasedForm().getSelectedValuesIds().isEmpty());
        action.reset();
        assertTrue(action.getGePlotForm().getAnnotationBasedForm().getSelectedValuesIds().isEmpty());
    }
    
    @Test
    public void testGetPlotUrl() {
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.MEAN.getValue()).
                                        contains("retrieveAnnotationGEPlot_mean.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.MEDIAN.getValue()).
                                        contains("retrieveAnnotationGEPlot_median.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.LOG2_INTENSITY.getValue()).
                                        contains("retrieveAnnotationGEPlot_log2.action?"));
        assertTrue(action.retrieveGePlotUrl(PlotCalculationTypeEnum.BOX_WHISKER_LOG2_INTENSITY.getValue()).
                                        contains("retrieveAnnotationGEPlot_bw.action?"));
    }


    private void setupActionVariables() {
        action.getGePlotForm().getAnnotationBasedForm().setAnnotationTypeSelection(EntityTypeEnum.SUBJECT.getValue());
        action.getGePlotForm().getAnnotationBasedForm().getSelectedValuesIds().add("1");
        action.getGePlotForm().getAnnotationBasedForm().getSelectedValuesIds().add("2");
        AnnotationDefinition selectedAnnotation = new AnnotationDefinition();
        selectedAnnotation.getPermissibleValueCollection().add(val1);
        selectedAnnotation.getPermissibleValueCollection().add(val2);
        selectedAnnotation.setDataType(AnnotationTypeEnum.STRING);
        action.getPlotParameters().setSelectedAnnotation(selectedAnnotation);
        action.getPlotParameters().setAddPatientsNotInQueriesGroup(true);
    }
}
