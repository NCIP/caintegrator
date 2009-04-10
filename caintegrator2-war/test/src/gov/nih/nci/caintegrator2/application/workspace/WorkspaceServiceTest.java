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
package gov.nih.nci.caintegrator2.application.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableGenomicSource;
import gov.nih.nci.caintegrator2.web.DisplayableImageSource;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WorkspaceServiceTest {
    
    private WorkspaceService workspaceService;
    private CaIntegrator2DaoStub daoStub;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("workspaceservice-test-config.xml", WorkspaceServiceTest.class); 
        workspaceService = (WorkspaceService) context.getBean("WorkspaceService"); 
        daoStub = (CaIntegrator2DaoStub) context.getBean("dao");
        daoStub.clear();
    }

    @Test
    public void testGetWorkspace() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertNotNull(workspace);
        
        WorkspaceServiceImpl workspaceService2 = (WorkspaceServiceImpl) workspaceService;
        workspaceService2.setDao(new DaoStubNoWorkspace());
        workspace = workspaceService2.getWorkspace();
        assertNotNull(workspace);
    }
    
    @Test
    public void testSubscribe() {
        Study study = new Study();
        study.setId(1L);
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertEquals(0, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.unsubscribe(workspace, study);
        assertEquals(0, workspace.getSubscriptionCollection().size());
    }
    
    @Test
    public void testCreateDisplayableStudySummary() {
        StudyHelper studyHelper = new StudyHelper();
        Study study = studyHelper.populateAndRetrieveStudyWithSourceConfigurations();
        DisplayableStudySummary summary = workspaceService.createDisplayableStudySummary(study);
        assertNotNull(summary);
        assertTrue(daoStub.retrieveNumberImagesForImagingSourceCalled);
        assertTrue(daoStub.retrieveNumberImageSeriesForImagingSourceCalled);
        assertTrue(daoStub.retrievePlatformsForGenomicSourceCalled);
        assertEquals(1, summary.getNumberSubjectAnnotationColumns());
        assertEquals(5, summary.getNumberSubjects());
        assertEquals(study.getShortTitleText(), summary.getStudyName());
        assertEquals(study.getLongTitleText(), summary.getStudyDescription());
        assertTrue(summary.isGenomicStudy());
        assertTrue(summary.isImagingStudy());
        List<DisplayableGenomicSource> genomicSources = summary.getGenomicDataSources();
        assertEquals(1, genomicSources.size());
        DisplayableGenomicSource genomicSource = genomicSources.get(0);
        assertEquals(2, genomicSource.getPlatforms().size());
        assertEquals(2, genomicSource.getNumberSamples());
        assertEquals(0, genomicSource.getNumberControlSamples());
        assertFalse(genomicSource.isControlSamplesSet());
        assertEquals("experimentIdentifier", genomicSource.getExperimentName());
        assertNotNull(genomicSource.getGenomicDataSourceConfiguration());
        //currently the genomic data source hostname is not populated in our test data
        assertNull(genomicSource.getHostName());
        
        List<DisplayableImageSource> imageSources = summary.getImageDataSources();
        assertEquals(1, imageSources.size());
        DisplayableImageSource imageSource = imageSources.get(0);
        assertEquals(2, imageSource.getNumberImages());
        assertEquals(1, imageSource.getNumberImageSeries());
        assertEquals(1, imageSource.getNumberImageStudies());
        assertEquals("collection", imageSource.getCollectionName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateDisplayableStudySummaryInvalid() {
        workspaceService.createDisplayableStudySummary(null);
    }
    
    @Test
    public void testSaveGenePatternAnalysisJob() {
        workspaceService.saveGenePatternAnalysisJob(new GenePatternAnalysisJob());
        assertTrue(daoStub.saveCalled);
    }
    
    @Test
    public void testSaveComparativeMarkerSelectionAnalysisJob() {
        workspaceService.saveComparativeMarkerSelectionAnalysisJob(new ComparativeMarkerSelectionAnalysisJob());
        assertTrue(daoStub.saveCalled);
    }
    
    private final class DaoStubNoWorkspace extends CaIntegrator2DaoStub {
        
        @Override
        public UserWorkspace getWorkspace(String username) {
            return null;
        }
    }

}
