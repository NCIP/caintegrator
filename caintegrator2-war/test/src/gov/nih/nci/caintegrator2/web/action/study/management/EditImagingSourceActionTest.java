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
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.ncia.NCIAFacadeStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IImagingDataSourceAjaxUpdater;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

/**
 * 
 */
public class EditImagingSourceActionTest extends AbstractSessionBasedTest {

    private EditImagingSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    private ImagingDataSourceAjaxUpdaterStub updaterStub;
    private NCIAFacadeStubForAction nciaFacadeStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditClinicalSourceActionTest.class); 
        action = (EditImagingSourceAction) context.getBean("editImagingSourceAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        nciaFacadeStub = new NCIAFacadeStubForAction();
        nciaFacadeStub.clear();
        action.clearErrorsAndMessages();
        updaterStub = new ImagingDataSourceAjaxUpdaterStub();
        updaterStub.clear();
        action.setUpdater(updaterStub);
        action.setNciaFacade(nciaFacadeStub);
    }
    
    @Test
    public void testValidate() {
        action.getImageSourceConfiguration().getServerProfile().setUrl("http://test, http://test2");
        action.setCancelAction(true);
        action.validate();
        assertEquals("http://test, http://test2", action.getImageSourceConfiguration().getServerProfile().getUrl());
        action.setCancelAction(false);
        action.validate();
        assertEquals("http://test", action.getImageSourceConfiguration().getServerProfile().getUrl());
        
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testPrepare() {
        action.getImageSourceConfiguration().setImageAnnotationConfiguration(new ImageAnnotationConfiguration());
        action.getImageSourceConfiguration().setId(1L);
        action.getImageSourceConfiguration().getImageAnnotationConfiguration().setId(1L);
        action.prepare();
        assertTrue(action.isFileUpload());
        assertFalse(action.hasActionErrors());
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }
    
    private void validateAddSource() {
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());
        
        action.getImageSourceConfiguration().getServerProfile().setUrl("Fake URL");
        action.setMappingType(ImageDataSourceMappingTypeEnum.AUTO.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.getImageSourceConfiguration().getServerProfile().setWebUrl("http://someurl.nci.nih.gov/");
        action.setMappingType(ImageDataSourceMappingTypeEnum.AUTO.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.getImageSourceConfiguration().getServerProfile().setWebUrl("http://someurl.nci.nih.gov/ncia");
        action.setMappingType(ImageDataSourceMappingTypeEnum.AUTO.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());         

        // test with INvalid input files
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());         
        
        action.setImageClinicalMappingFile(null);
        action.setMappingType(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());         
        

        // test with valid input files
        action.getImageSourceConfiguration().setCollectionName("Fake Collection Name");
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertFalse(action.hasFieldErrors());
    }


    @Test
    public void testSaveImagingSource() {
        validateAddSource();
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        assertTrue(updaterStub.runJobCalled);
        updaterStub.clear();
        action.clearErrorsAndMessages();
        
        nciaFacadeStub.throwConnectionException = true;
        assertEquals(Action.INPUT, action.saveImagingSource());
        nciaFacadeStub.clear();
        updaterStub.clear();
        action.clearErrorsAndMessages();
        nciaFacadeStub.throwInvalidImagingCollectionException = true;
        assertEquals(Action.INPUT, action.saveImagingSource());
        nciaFacadeStub.clear();
        updaterStub.clear();
        action.clearErrorsAndMessages();
        
        action.getImageSourceConfiguration().setId(1l);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        assertTrue(studyManagementServiceStub.deleteCalled);
        assertTrue(updaterStub.runJobCalled);
        
        
    }
    
    @Test
    public void testLoadImageAnnotations() {
        assertEquals(Action.SUCCESS, action.loadImageAnnotations());
        assertTrue(studyManagementServiceStub.loadImageAnnotationCalled);
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.loadImageAnnotations());   
    }
    
    @Test
    public void testDelete() {
        assertEquals(Action.SUCCESS, action.delete());
        assertTrue(studyManagementServiceStub.deleteCalled);
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.delete());
    }
    
    @Test
    public void testMapImagingSource() {
        action.setImageClinicalMappingFile(null);
        action.setMappingType(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue());
        assertEquals(Action.INPUT, action.mapImagingSource());
        
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        action.setImageClinicalMappingFileFileName("valid");
        action.clearErrorsAndMessages();
        studyManagementServiceStub.clear();
        assertEquals(Action.SUCCESS, action.mapImagingSource());
        assertTrue(updaterStub.runJobCalled);
    }
    
    @Test
    public void testGetSetMappingType() {
        action.setMappingType(null);
        assertEquals("", action.getMappingType());
        action.setMappingType("invalid");
        assertEquals("", action.getMappingType());
        action.setMappingType(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue());
        assertEquals(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue(), action.getMappingType());
    }
    
    private static class ImagingDataSourceAjaxUpdaterStub implements IImagingDataSourceAjaxUpdater {
        
        public boolean runJobCalled = false;
        
        public void clear() {
            runJobCalled = false;
        }
        
        public void initializeJsp() {
            
        }

        public void runJob(Long imagingSourceId, File imageClinicalMappingFile,
                ImageDataSourceMappingTypeEnum mappingType, boolean mapOnly,
                boolean loadAimAnnotation) {
            runJobCalled = true;
        }

        public void runJob(Long imagingSourceId) {
            runJobCalled = true;
        }
        
    }
    
    private static class NCIAFacadeStubForAction extends NCIAFacadeStub {
        public boolean throwConnectionException = false;
        public boolean throwInvalidImagingCollectionException = false;
        
        public void clear() {
            throwConnectionException = false;
            throwInvalidImagingCollectionException = false;
        }
        
        @Override
        public void validateImagingSourceConnection(ServerConnectionProfile profile, String collectionNameProject) 
        throws ConnectionException ,InvalidImagingCollectionException {
            if (throwConnectionException) {
                throw new ConnectionException("Exception Thrown");
            }
            if (throwInvalidImagingCollectionException) {
                throw new InvalidImagingCollectionException("Exception Thrown");
            }
        }
    }
}
