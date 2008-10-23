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
package gov.nih.nci.caintegrator2.web.action.query;

//import gov.nih.nci.caintegrator2.data.StudyHelper;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;

import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;

import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class ManageQueryActionTest {

    private ManageQueryAction manageQueryAction;
    
    // Study objects
    private static final String selectedRowCriterion = "clinical";
    private final QueryManagementService queryManagementService = new QueryManagementServiceStub();

    // dummy string array for testing
    private final String [] selectedAnnotationsArray = {"annotation1", "annotation2"};
    private final String [] operatorsArray =  {"equals",">"};
    private final String [] selectedValuesArray = {"String1", "1.0"};
    private final Long [] holdLongArray = {Long.valueOf(12), Long.valueOf(4)};

    @Before
    @SuppressWarnings({"PMD"})
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("query-management-action-test-config.xml", ManageQueryActionTest.class); 
        manageQueryAction = (ManageQueryAction) context.getBean("manageQueryAction");
        manageQueryAction.setSelectedRowCriterion(selectedRowCriterion);
        manageQueryAction.setQueryManagementService(queryManagementService);
        manageQueryAction.setWorkspaceService(new WorkspaceServiceStub());
        setupSession();
        
        // the first time the parameter is null so
        // confirm that the getter method returns an empty array
        String[] emptyArray = {""};  // test the first time when the array is null
        Long[] emptyLongArray = null;
        assertArrayEquals(emptyArray,manageQueryAction.getSelectedAnnotations());
        manageQueryAction.setSelectedAnnotations(selectedAnnotationsArray);
        assertArrayEquals(selectedAnnotationsArray,manageQueryAction.getSelectedAnnotations());
        
        assertArrayEquals(emptyArray,manageQueryAction.getSelectedOperators());
        manageQueryAction.setSelectedOperators(operatorsArray);
        assertArrayEquals(operatorsArray,manageQueryAction.getSelectedOperators());
        
        assertArrayEquals(emptyArray,manageQueryAction.getSelectedValues());
        manageQueryAction.setSelectedValues(selectedValuesArray);
        assertArrayEquals(selectedValuesArray,manageQueryAction.getSelectedValues());
        
        assertArrayEquals(emptyLongArray,manageQueryAction.getSelectedClinicalAnnotations());
        manageQueryAction.setSelectedClinicalAnnotations(holdLongArray);
        assertArrayEquals(holdLongArray,manageQueryAction.getSelectedClinicalAnnotations());
        
        assertArrayEquals(emptyLongArray,manageQueryAction.getSelectedImageAnnotations());
        manageQueryAction.setSelectedImageAnnotations(holdLongArray);
        assertArrayEquals(holdLongArray,manageQueryAction.getSelectedImageAnnotations());
        
        
        manageQueryAction.setSelectedBasicOperator("or");
        assertEquals("or",manageQueryAction.getSelectedBasicOperator());
        
        manageQueryAction.setSelectedRowCriterion(selectedRowCriterion);
        
        manageQueryAction.setSelectedAction("");
        assertEquals("", manageQueryAction.getSelectedAction());    
        
    }

    @SuppressWarnings({"PMD", "unchecked"})
    private void setupSession() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        SessionHelper sessionHelper = SessionHelper.getInstance();
        manageQueryAction.prepare();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        studySubscription.setId(1L);
        StudySubscription studySubscription2 = new StudySubscription();
        studySubscription2.setStudy(new Study());
        studySubscription2.setId(2L);
        sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().clear();
        sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().add(studySubscription);
        sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().add(studySubscription2);
        sessionHelper.getDisplayableUserWorkspace().setCurrentStudySubscription(studySubscription);
        manageQueryAction.validate();
        manageQueryAction.getManageQueryHelper().setQueryCriteriaRowList(new ArrayList<QueryAnnotationCriteria>());
        QueryAnnotationCriteria queryAnnotationCriteria = new QueryAnnotationCriteria();
        AnnotationSelection annotationSelection = new AnnotationSelection();
        annotationSelection.setAnnotationDefinitions(new HashSet<AnnotationDefinition>());
        annotationSelection.getAnnotationDefinitions().add(new AnnotationDefinition());
        queryAnnotationCriteria.setAnnotationSelections(annotationSelection);
        queryAnnotationCriteria.setRowType(EntityTypeEnum.SUBJECT);
        manageQueryAction.getManageQueryHelper().getQueryCriteriaRowList().add(queryAnnotationCriteria);

        QueryAnnotationCriteria queryAnnotationCriteria2 = new QueryAnnotationCriteria();
        AnnotationSelection annotationSelection2 = new AnnotationSelection();
        annotationSelection2.setAnnotationDefinitions(new HashSet<AnnotationDefinition>());
        annotationSelection2.getAnnotationDefinitions().add(new AnnotationDefinition());
        queryAnnotationCriteria2.setAnnotationSelections(annotationSelection2);
        queryAnnotationCriteria2.setRowType(EntityTypeEnum.SUBJECT);
        manageQueryAction.getManageQueryHelper().getQueryCriteriaRowList().add(queryAnnotationCriteria2);
    }


    @Test
    @SuppressWarnings({"PMD"})
    public void testExecute() {      
        // test create new query
        manageQueryAction.setSelectedAction("createNewQuery");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        
        // test edit new query
        manageQueryAction.setSelectedAction("editQuery");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        
        // test execute query
        manageQueryAction.setSelectedAction("executeQuery");
        //manageQueryAction.getManageQueryHelper().setResultType(ResultTypeEnum.GENOMIC.getValue());
        //manageQueryAction.getManageQueryHelper().setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue());
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        
        // test save query
        // TODO fix this problem, shouldn't fail.
        manageQueryAction.setSelectedAction("saveQuery");
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        
        // test - addition of criteria rows

        // test - addition of row with non-clinical criterion
        manageQueryAction.setSelectedRowCriterion("notaclinicalrow");
        assertEquals("notaclinicalrow",manageQueryAction.getSelectedRowCriterion());
        manageQueryAction.setSelectedAction("addCriterionRow");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertEquals(Action.SUCCESS, manageQueryAction.execute());
        
        // test - addition of row with clinical criterion
        manageQueryAction.setSelectedRowCriterion(selectedRowCriterion);
        assertEquals(selectedRowCriterion,manageQueryAction.getSelectedRowCriterion());
        manageQueryAction.setSelectedAction("addCriterionRow");
        manageQueryAction.prepare();
        manageQueryAction.validate();
        assertEquals(Action.SUCCESS, manageQueryAction.execute());

        // test for invalid action
        manageQueryAction.setSelectedAction(null);
        assertEquals(Action.ERROR, manageQueryAction.execute());

        assertEquals(Action.SUCCESS, manageQueryAction.addCriterionRow());
        assertEquals(Action.SUCCESS, manageQueryAction.deleteCriterionRow());
        assertEquals(Action.SUCCESS, manageQueryAction.deleteCriterionRowAll());
        assertEquals(Action.SUCCESS, manageQueryAction.selectColumns());
        assertEquals(Action.SUCCESS, manageQueryAction.selectSorting());
//        assertEquals(Action.SUCCESS, manageQueryAction.saveQuery());
        assertNotNull(manageQueryAction.getQueryManagementService());
    }
}
