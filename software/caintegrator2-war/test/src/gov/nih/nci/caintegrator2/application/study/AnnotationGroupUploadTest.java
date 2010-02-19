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
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;

import org.junit.Before;
import org.junit.Test;

public class AnnotationGroupUploadTest {

    AnnotationGroupUploadContent uploadContent;
    private MyDaoStub daoStub = new MyDaoStub();

    
    @Before
    public void setUp() throws Exception {
        uploadContent = new AnnotationGroupUploadContent();
    } 
    @Test
    public void testSetGoodValues() {
        uploadContent.setCdeId("");
        assertNull(uploadContent.getCdeId());
        uploadContent.setVersion("");
        assertNull(uploadContent.getVersion());
        uploadContent.setAnnotationType("annotation");
        uploadContent.setCdeId("12345");
        assertEquals(Long.valueOf("12345"), uploadContent.getCdeId());
        uploadContent.setDataType("string");
        uploadContent.setVersion("1.2");
        assertEquals(Float.valueOf("1.2"), uploadContent.getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnnotationType() {
        uploadContent.setAnnotationType("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCdeId() {
        uploadContent.setCdeId("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataType() {
        uploadContent.setDataType("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVersion() {
        uploadContent.setVersion("Unknown");
    }
    
    @Test
    public void testCreateAnnotationFieldDescriptor() throws ValidationException {
        StudyConfiguration studyConfiguration = createStudyConfiguration();
        AnnotationFieldDescriptor afd;
        AnnotationDefinition ad;
        afd = uploadContent.createAnnotationFieldDescriptor(studyConfiguration, daoStub);
        assertNull(afd.getId());
        
        // Test reuse existing Subject 
        uploadContent.setColumnName("Subject");
        uploadContent.setAnnotationType("identifier");
        uploadContent.setDataType("string");
        afd = uploadContent.createAnnotationFieldDescriptor(studyConfiguration, daoStub);
        assertEquals(1L, afd.getId().longValue());
        
        // Test existing Subject not matching data type 
        uploadContent.setColumnName("Subject");
        uploadContent.setAnnotationType("identifier");
        uploadContent.setDataType("numeric");
        boolean gotException = false;
        try {
            afd = uploadContent.createAnnotationFieldDescriptor(studyConfiguration, daoStub);
        } catch (ValidationException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // Test new Gender
        uploadContent.setColumnName("Gender");
        uploadContent.setDataType("string");
        uploadContent.setAnnotationType("annotation");
        ad = uploadContent.createAnnotationDefinition(daoStub);
        assertNull(ad.getId());
        
        // Test existing annotation definition gender
        uploadContent.setDefinitionName("Gender");
        ad = uploadContent.createAnnotationDefinition(daoStub);
        assertEquals(1L, ad.getId().longValue());
        
        // Test existing annotation definition gender not matching data type
        uploadContent.setDefinitionName("Gender");
        uploadContent.setDataType("numeric");
        gotException = false;
        try {
            ad = uploadContent.createAnnotationDefinition(daoStub);
        } catch (ValidationException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
    }
    
    private StudyConfiguration createStudyConfiguration() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        
        return studyConfiguration;
    }
    
    class MyDaoStub extends CaIntegrator2DaoStub {

        @Override
        public AnnotationDefinition getAnnotationDefinition(Long cdeId, Float version) {
            // TODO Auto-generated method stub
            return super.getAnnotationDefinition(cdeId, version);
        }

        @Override
        public AnnotationDefinition getAnnotationDefinition(String name) {
            if ("Gender".equals(name)) {
                AnnotationDefinition ad = new AnnotationDefinition();
                ad.setKeywords(name);
                ad.setId(1L);
                CommonDataElement cde = new CommonDataElement();
                ValueDomain vd = new ValueDomain();
                vd.setDataTypeString("string");
                cde.setValueDomain(vd);
                cde.setLongName(name);
                ad.setCommonDataElement(cde);
                return ad;
            }

            return super.getAnnotationDefinition(name);
        }

        @Override
        public AnnotationFieldDescriptor getExistingFieldDescriptorInStudy(String name,
                StudyConfiguration studyConfiguration) {
            if ("Subject".equals(name)) {
                AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
                afd.setId(1L);
                afd.setName("Subject");
                afd.setType(AnnotationFieldType.IDENTIFIER);
                AnnotationGroup ag = new AnnotationGroup();
                ag.getAnnotationFieldDescriptors().add(afd);
                afd.setAnnotationGroup(ag);
                AnnotationDefinition ad = new AnnotationDefinition();
                CommonDataElement cde = new CommonDataElement();
                ValueDomain vd = new ValueDomain();
                vd.setDataTypeString("string");
                cde.setValueDomain(vd);
                ad.setCommonDataElement(cde);
                afd.setDefinition(ad);
                return afd;
            }
            return super.getExistingFieldDescriptorInStudy(name, studyConfiguration);
        }
        
    }

}