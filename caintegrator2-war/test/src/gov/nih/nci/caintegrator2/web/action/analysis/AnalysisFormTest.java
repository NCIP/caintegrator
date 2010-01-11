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
