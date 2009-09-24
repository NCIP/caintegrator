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
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;


public class DefineImagingFileColumnActionTest extends AbstractSessionBasedTest {

    private DefineImagingFileColumnAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineFileColumnActionTest.class); 

        action = (DefineImagingFileColumnAction) context.getBean("defineImagingFileColumnAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testEditFileColumn() {
        assertEquals(Action.SUCCESS, action.editFileColumn());
    }
    
    @Test
    public void testPrepare() {
        action.getFileColumn().setId(1L);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }
    
    
    @Test
    public void testSelectDefintion() {
        action.getDefinitions().add(null);
        assertEquals(Action.SUCCESS, action.selectDefinition());
        assertTrue(studyManagementServiceStub.setDefinitionCalled);
        
        studyManagementServiceStub.clear();
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.selectDefinition());
        assertFalse(action.getActionErrors().isEmpty());
        
    }

    
    @Test
    public void testSelectDataElement() {
        action.getDataElements().add(new CommonDataElement());
        assertEquals(Action.SUCCESS, action.selectDataElement());
        assertTrue(studyManagementServiceStub.setDataElementCalled);
        
        studyManagementServiceStub.clear();
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.selectDataElement());
        assertFalse(action.getActionErrors().isEmpty());
        
        studyManagementServiceStub.clear();
        action.clearErrorsAndMessages();
        studyManagementServiceStub.throwConnectionException = true;
        assertEquals(Action.ERROR, action.selectDataElement());
        assertFalse(action.getActionErrors().isEmpty());
        
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testSetColumnType() {
        AnnotationFile annotationFile = new AnnotationFile();
        action.getFileColumn().setAnnotationFile(annotationFile);
        assertEquals("Annotation", action.getColumnType());
        action.setColumnType(null);
        annotationFile.setIdentifierColumn(action.getFileColumn());
        assertEquals(annotationFile.getIdentifierColumn(), action.getFileColumn());
        assertEquals("Identifier", action.getColumnType());
        action.setColumnType(null);
        annotationFile.setTimepointColumn(action.getFileColumn());
        assertEquals(annotationFile.getTimepointColumn(), action.getFileColumn());
        assertEquals("Timepoint", action.getColumnType());
        action.setColumnType("Annotation");
        assertEquals("Annotation", action.getColumnType());
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testSaveColumnType() {
        AnnotationFile annotationFile = new AnnotationFile();
        action.getFileColumn().setAnnotationFile(annotationFile);
        action.setColumnType("Timepoint");
        assertEquals(Action.SUCCESS, action.saveColumnType());
        action.setColumnType("Annotation");
        assertEquals(Action.SUCCESS, action.saveColumnType());
    }

    @Test
    public void testAnnotationDataTypeFunctions() {
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        action.getFileColumn().getFieldDescriptor().setDefinition(new AnnotationDefinition());
        // Assuming we will always have date, string, numeric, and possibly more later.
        assertTrue(action.getAnnotationDataTypes().length >= 3);
    }
    
    @Test
    public void testCreateNewDefinition() throws ValidationException, ParseException {
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.createNewDefinition());
        assertTrue(studyManagementServiceStub.createDefinitionCalled);
        assertFalse(action.isReadOnly());
    }
    
    @Test
    public void testIsColumnTypeAnnotation() {
        action.setColumnType("Annotation");
        assertTrue(action.isColumnTypeAnnotation());
        action.setColumnType("Identifier");
        assertFalse(action.isColumnTypeAnnotation());
    }
    
    @Test
    public void testIsPermissibleOn() {
        action.setColumnType("Identifier");
        assertFalse(action.isPermissibleOn());
        action.setColumnType("Annotation");
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        assertFalse(action.isPermissibleOn());
        action.getFileColumn().getFieldDescriptor().setDefinition(new AnnotationDefinition());
        assertTrue(action.isPermissibleOn());
    }
    
    @Test
    public void testUpdateAnnotationDefinition() throws ParseException {
        action.setColumnType("Annotation");
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        
        AnnotationDefinition definition = new AnnotationDefinition();
        action.getFileColumn().getFieldDescriptor().setDefinition(definition);
        definition.setType(AnnotationTypeEnum.DATE.getValue());
        
        Collection<AbstractPermissibleValue> permissibleValueCollection =  new HashSet<AbstractPermissibleValue>();
        definition.getPermissibleValueCollection().addAll(permissibleValueCollection);
        List<String> stringValues = new ArrayList<String>();
        action.setPermissibleUpdateList(stringValues);
        stringValues.add("10-05-2004");
        stringValues.add("01/02/1999");
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(definition.getPermissibleValueCollection().size(), 2);
        stringValues.add("11-10-2008");
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(definition.getPermissibleValueCollection().size(), 3);
        
        stringValues.add("XYZ");
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(definition.getPermissibleValueCollection().size(), 0);
    }

}
