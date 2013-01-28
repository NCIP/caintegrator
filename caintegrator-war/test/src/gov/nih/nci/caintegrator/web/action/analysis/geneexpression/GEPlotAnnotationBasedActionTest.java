/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.geneexpression.GEPlotAnnotationBasedAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class GEPlotAnnotationBasedActionTest extends AbstractSessionBasedTest {
    private GEPlotAnnotationBasedAction action;
    private final StudyManagementServiceStub studyManagementService = new StudyManagementServiceStub();

    private PermissibleValue val1 = new PermissibleValue();
    private PermissibleValue val2 = new PermissibleValue();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        StudySubscription subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new GEPlotAnnotationBasedAction();
        action.setStudyManagementService(studyManagementService);
        action.setAnalysisService(analysisService);
        action.setWorkspaceService(workspaceService);
        action.setQueryManagementService(queryManagementService);
        setStudySubscription(subscription);
        SessionHelper.getInstance().getDisplayableUserWorkspace().refresh(workspaceService, true);
    }

    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        AnnotationGroup group = new AnnotationGroup();
        group.setName("subjectAnnotations");
        study.getAnnotationGroups().add(group);
        AnnotationFieldDescriptor afd1 = new AnnotationFieldDescriptor();
        afd1.setId(1l);
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setId(2l);
        group.getAnnotationFieldDescriptors().add(afd1);
        group.getAnnotationFieldDescriptors().add(afd2);
        AnnotationDefinition subjectDef1 = new AnnotationDefinition();
        subjectDef1.setId(Long.valueOf(1));
        val1.setId(Long.valueOf(1));
        val1.setValue("M");
        val2.setId(Long.valueOf(2));
        val2.setValue("F");
        subjectDef1.getPermissibleValueCollection().add(val1);
        subjectDef1.getPermissibleValueCollection().add(val2);
        subjectDef1.getCommonDataElement().getValueDomain().getPermissibleValueCollection().add(new PermissibleValue());
        afd1.setDefinition(subjectDef1);
        AnnotationDefinition subjectDef2 = new AnnotationDefinition();
        subjectDef2.setId(Long.valueOf(2));
        afd2.setDefinition(subjectDef2);
        return study;
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
        gdsc.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
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
        action.getGePlotForm().getAnnotationBasedForm().setAnnotationGroupSelection("subjectAnnotations");
        assertEquals(ActionSupport.SUCCESS, action.updateAnnotationDefinitions());
        assertEquals(1, action.getGePlotForm().getAnnotationBasedForm().getAnnotationFieldDescriptors().size());
        assertTrue(action.getGePlotForm().getAnnotationBasedForm().getAnnotationFieldDescriptors().containsKey("1"));
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
        AnnotationGroup imagingGroup = new AnnotationGroup();
        imagingGroup.setName("imaging");
        action.getCurrentStudy().getAnnotationGroups().add(imagingGroup);
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        imagingGroup.getAnnotationFieldDescriptors().add(afd);
        afd.setDefinition(new AnnotationDefinition());
        assertEquals(2, action.getAnnotationTypes().size());
    }

    @Test
    public void testUpdatePermissibleValues() {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.updatePermissibleValues());
    }

    @Test
    public void testCreatePlot() throws Exception {
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
        verify(analysisService, atLeastOnce()).createGeneExpressionPlot(any(StudySubscription.class), any(AbstractGEPlotParameters.class));
        assertFalse(action.isCreatable());

        action.getGePlotForm().getAnnotationBasedForm().setSelectedAnnotationId("1");
        action.getGePlotForm().getAnnotationBasedForm().setAnnotationGroupSelection("subjectAnnotations");
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
        action.getGePlotForm().getAnnotationBasedForm().getSelectedValuesIds().add("1");
        action.getGePlotForm().getAnnotationBasedForm().getSelectedValuesIds().add("2");
        AnnotationFieldDescriptor selectedAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        AnnotationDefinition selectedAnnotation = new AnnotationDefinition();
        selectedAnnotation.getPermissibleValueCollection().add(val1);
        selectedAnnotation.getPermissibleValueCollection().add(val2);
        selectedAnnotation.setDataType(AnnotationTypeEnum.STRING);
        selectedAnnotationFieldDescriptor.setDefinition(selectedAnnotation);
        selectedAnnotationFieldDescriptor.setId(1l);
        action.getPlotParameters().setSelectedAnnotation(selectedAnnotationFieldDescriptor);
        action.getPlotParameters().setAddPatientsNotInQueriesGroup(true);
    }
}
