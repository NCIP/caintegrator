/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.application.analysis.AbstractKMParameters;
import gov.nih.nci.caintegrator2.application.kmplot.CaIntegratorKMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KMPlotAnnotationBasedActionTest extends AbstractSessionBasedTest  {

    private KMPlotAnnotationBasedAction action;
    private final QueryManagementServiceStub queryManagementServiceStub = new QueryManagementServiceStub();
    private final StudyManagementServiceStub studyManagementService = new StudyManagementServiceStub();
    private final KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();

    private PermissibleValue val1 = new PermissibleValue();
    private PermissibleValue val2 = new PermissibleValue();
    private SurvivalValueDefinition survivalValue;

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
        action = new KMPlotAnnotationBasedAction();
        action.setStudyManagementService(studyManagementService);
        action.setAnalysisService(analysisService);
        action.setWorkspaceService(workspaceService);
        action.setQueryManagementService(queryManagementServiceStub);
        queryManagementServiceStub.clear();

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
        subjectDef1.getPermissibleValueCollection().add(new PermissibleValue());
        afd1.setDefinition(subjectDef1);
        AnnotationDefinition subjectDef2 = new AnnotationDefinition();
        subjectDef2.setId(Long.valueOf(2));
        afd2.setDefinition(subjectDef2);
        AnnotationDefinition survivalStartDate = new AnnotationDefinition();
        survivalStartDate.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition deathDate = new AnnotationDefinition();
        deathDate.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition lastFollowupDate = new AnnotationDefinition();
        lastFollowupDate.setDataType(AnnotationTypeEnum.DATE);

        survivalValue = new SurvivalValueDefinition();
        survivalValue.setId(Long.valueOf(1));
        survivalValue.setSurvivalStartDate(survivalStartDate);
        survivalValue.setDeathDate(deathDate);
        survivalValue.setLastFollowupDate(lastFollowupDate);
        study.getSurvivalValueDefinitionCollection().add(survivalValue);

        return study;
    }

    @Test
    public void testPrepare() {
        action.getKmPlotParameters().setSurvivalValueDefinition(survivalValue);
        setupActionVariables();
        action.prepare();
        assertTrue(queryManagementServiceStub.getRefreshedEntityCalled);
        assertEquals(1, action.getKmPlotForm().getSurvivalValueDefinitions().size());
    }

    @Test
    public void testValidate() {
        action.clearErrorsAndMessages();
        action.validate();
        assertTrue(action.getActionErrors().size() > 0);
        action.clearErrorsAndMessages();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getKmPlotForm().getSurvivalValueDefinitions().put("1", new SurvivalValueDefinition());
        action.validate();
        assertTrue(action.getActionErrors().isEmpty());
    }

    @Test
    public void testInput() {
        assertEquals(ActionSupport.SUCCESS, action.input());
    }

    @Test
    public void testUpdateAnnotationDefinitions() {
        // Invalid because thre's not an Annotation EntityType selected.
        assertEquals(ActionSupport.INPUT, action.updateAnnotationDefinitions());
        KMPlotAnnotationBasedActionForm form = new KMPlotAnnotationBasedActionForm();
        form.setAnnotationGroupSelection("subjectAnnotations");
        action.getKmPlotForm().setAnnotationBasedForm(form);
        assertEquals(ActionSupport.SUCCESS, action.updateAnnotationDefinitions());
        assertEquals(1, action.getKmPlotForm().getAnnotationBasedForm().getAnnotationFieldDescriptors().size());
        assertTrue(action.getKmPlotForm().getAnnotationBasedForm().getAnnotationFieldDescriptors().containsKey("1"));
    }

    @Test
    public void testGetDisplayTab() {
        setupActionVariables();
        assertEquals("annotationTab", action.getDisplayTab());
        action.setDisplayTab("Testing");
        assertEquals("Testing", action.getDisplayTab());
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
        action.getKmPlotParameters().getSelectedValues().clear();
        action.getKmPlotParameters().getSelectedValues().add(val1);
        action.getKmPlotParameters().getSelectedValues().add(val2);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getKmPlotParameters().setSurvivalValueDefinition(new SurvivalValueDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setId(Long.valueOf(1));
        action.getKmPlotParameters().getSurvivalValueDefinition().setSurvivalStartDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getSurvivalStartDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setDeathDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getDeathDate().setDataType(AnnotationTypeEnum.DATE);
        action.getKmPlotParameters().getSurvivalValueDefinition().setLastFollowupDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().getLastFollowupDate().setDataType(AnnotationTypeEnum.DATE);
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        verify(analysisService, atLeastOnce()).createKMPlot(any(StudySubscription.class), any(AbstractKMParameters.class));
        assertFalse(action.isCreatable());

        action.getKmPlotForm().getAnnotationBasedForm().setSelectedAnnotationId("1");
        action.getKmPlotForm().getAnnotationBasedForm().setAnnotationGroupSelection("subjectAnnotations");
        action.getKmPlotForm().setSurvivalValueDefinitionId("1");
        assertTrue(action.isCreatable());
    }

    @Test
    public void testGetAllStringPValues() {
        plotService.setCaIntegratorPlotService(new CaIntegratorKMPlotServiceStub());
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        configuration.setTitle("title");
        configuration.setDurationLabel("duration");
        configuration.setProbabilityLabel("probability");
        SubjectGroup group1 = createGroup();
        configuration.getGroups().add(group1);
        SubjectGroup group2 = createGroup();
        configuration.getGroups().add(group2);
        KMPlot plot = plotService.generatePlot(configuration);
        SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, plot);
        assertEquals("1.10", action.getAllStringPValues().get("group").get("group"));
    }

    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(action.getKmPlotForm().getAnnotationBasedForm().getSelectedValuesIds().isEmpty());
        action.reset();
        assertTrue(action.getKmPlotForm().getAnnotationBasedForm().getSelectedValuesIds().isEmpty());
    }

    @Test
    public void testGetPlotUrl() {
        assertEquals("/caintegrator/retrieveAnnotationKMPlot.action?", action.getPlotUrl());
    }

    private SubjectGroup createGroup() {
        SubjectGroup group = new SubjectGroup();
        group.setColor(Color.BLACK);
        group.setName("group");
        SubjectSurvivalData survivalData = new SubjectSurvivalData(1, false);
        group.getSurvivalData().add(survivalData);
        return group;
    }

    private void setupActionVariables() {
        KMPlotAnnotationBasedActionForm form = new KMPlotAnnotationBasedActionForm();
        form.setAnnotationGroupSelection("subjectAnnotations");
        form.getSelectedValuesIds().add("1");
        form.getSelectedValuesIds().add("2");
        action.getKmPlotForm().setAnnotationBasedForm(form);
        AnnotationFieldDescriptor selectedAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        AnnotationDefinition selectedAnnotation = new AnnotationDefinition();
        selectedAnnotation.getPermissibleValueCollection().add(val1);
        selectedAnnotation.getPermissibleValueCollection().add(val2);
        selectedAnnotation.setDataType(AnnotationTypeEnum.STRING);
        selectedAnnotation.setId(Long.valueOf(1));
        selectedAnnotationFieldDescriptor.setDefinition(selectedAnnotation);
        selectedAnnotationFieldDescriptor.setId(1l);
        action.getKmPlotParameters().setSelectedAnnotation(selectedAnnotationFieldDescriptor);
    }


}
