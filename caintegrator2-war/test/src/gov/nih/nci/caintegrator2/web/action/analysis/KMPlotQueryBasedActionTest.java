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
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.CaIntegratorKMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KMPlotQueryBasedActionTest extends AbstractSessionBasedTest {
    
    private KMPlotQueryBasedAction action;
    private QueryManagementServiceStub queryManagementServiceStub = new QueryManagementServiceStub();
    private AnalysisServiceStub analysisServiceStub = new AnalysisServiceStub();
    private KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();
    private StudySubscription subscription;
    private SurvivalValueDefinition survivalValue;
    
    @Before
    public void setUp() {
        super.setUp();
        subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new KMPlotQueryBasedAction();
        action.setAnalysisService(analysisServiceStub);
        WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        
        action.setQueryManagementService(queryManagementServiceStub);
        queryManagementServiceStub.clear();
        analysisServiceStub.clear();
    }
    
    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
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
        SurvivalValueDefinition svd = new SurvivalValueDefinition();
        svd.setId(Long.valueOf(1));
        action.getKmPlotForm().setSurvivalValueDefinitionId("1");
        action.getKmPlotParameters().setSurvivalValueDefinition(svd);
        setupActionVariables();
        action.prepare();
        assertTrue(queryManagementServiceStub.getRefreshedEntityCalled);
        assertTrue(!action.getKmPlotForm().getSurvivalValueDefinitions().isEmpty());
    }
    
    @Test
    public void testValidate() {
        action.clearErrorsAndMessages();
        ActionContext.getContext().getValueStack().setValue("studySubscription", null);
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        action.getKmPlotForm().getSurvivalValueDefinitions().put("1", new SurvivalValueDefinition());
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        ActionContext.getContext().getValueStack().setValue("studySubscription", new StudySubscription());
        assertTrue(action.getActionErrors().isEmpty());
    }

    @Test
    public void testInput() {
        assertEquals(ActionSupport.SUCCESS, action.input());
    }

    @Test
    public void testCreatePlot() throws InterruptedException {
        setupActionVariables();
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        action.setCreatePlotSelected(true);
        assertEquals(ActionSupport.INPUT, action.createPlot());
        action.getKmPlotParameters().setSurvivalValueDefinition(new SurvivalValueDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setId(Long.valueOf(1));
        action.getKmPlotParameters().getSurvivalValueDefinition().setSurvivalStartDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setDeathDate(new AnnotationDefinition());
        action.getKmPlotParameters().getSurvivalValueDefinition().setLastFollowupDate(new AnnotationDefinition());
        assertEquals(ActionSupport.SUCCESS, action.createPlot());
        assertTrue(analysisServiceStub.createKMPlotCalled);
        
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
        SessionHelper.setKmPlot(PlotTypeEnum.QUERY_BASED, plot);
        assertEquals("1.10", action.getAllStringPValues().get("group").get("group"));
    }
    
    @Test
    public void testReset() {
        setupActionVariables();
        assertFalse(action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty());
        action.reset();
        assertFalse(action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty());
        action.setResetSelected(true);
        action.reset();
        assertTrue(action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty());
    }
    
    @Test
    public void testGetPlotUrl() {
        assertEquals("/caintegrator2/retrieveQueryKMPlot.action?", action.getPlotUrl());
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
        action.getKmPlotParameters().setExclusiveGroups(true);
        action.getKmPlotParameters().setAddPatientsNotInQueriesGroup(true);
        action.getKmPlotParameters().getQueries().add(new Query());
        action.getKmPlotParameters().getQueries().add(new Query());
        Query query1 = new Query();
        query1.setName("1");
        Query query2 = new Query();
        query2.setName("2");
        subscription.getQueryCollection().add(query1);
        subscription.getQueryCollection().add(query2);
        action.getKmPlotForm().getQueryBasedForm().setAddPatientsNotInQueriesGroup(true);
        action.getKmPlotForm().getQueryBasedForm().setExclusiveGroups(true);
        action.getKmPlotForm().getQueryBasedForm().getSelectedQueryNames().add(DisplayableQuery.getDisplayableQueryName(query1));
        action.getKmPlotForm().getQueryBasedForm().getUnselectedQueryNames().add(DisplayableQuery.getDisplayableQueryName(query1));
        action.getKmPlotForm().getQueryBasedForm().getSelectedQueries().put(DisplayableQuery.getDisplayableQueryName(query1), new DisplayableQuery(query1));
        action.getKmPlotForm().getQueryBasedForm().getUnselectedQueries().put(DisplayableQuery.getDisplayableQueryName(query2), new DisplayableQuery(query2));
    }
    

}