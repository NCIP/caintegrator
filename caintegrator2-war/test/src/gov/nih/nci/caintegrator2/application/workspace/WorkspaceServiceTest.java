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
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableGenomicSource;
import gov.nih.nci.caintegrator2.web.DisplayableImageSource;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceServiceTest {

    private WorkspaceService workspaceService;
    private CaIntegrator2DaoStub daoStub;
    private AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();

    @Before
    public void setUp() {
        daoStub = new CaIntegrator2DaoStub();
        WorkspaceServiceImpl workspaceSvc = new WorkspaceServiceImpl();
        workspaceSvc.setDao(daoStub);
        authentication.setUsername("validUserName");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        workspaceService = workspaceSvc;
    }

    @Test
    public void testGetWorkspace() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertNotNull(workspace);
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        UserWorkspace workspaceAnonymous = workspaceService.getWorkspaceReadOnly();
        assertTrue(workspaceAnonymous.isAnonymousUser());

        WorkspaceServiceImpl workspaceService2 = (WorkspaceServiceImpl) workspaceService;
        workspaceService2.setDao(new DaoStubNoWorkspace());
        workspace = workspaceService2.getWorkspace();
        assertNotNull(workspace);
    }

    @Test
    public void testSubscribeAllStudies() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setId(1l);
        studySubscription.setStudy(new Study());
        workspace.getSubscriptionCollection().add(studySubscription);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribeAll(workspace);
        assertEquals(2, workspace.getSubscriptionCollection().size()); // daoStub has 1 public study.
        assertFalse(workspace.getSubscriptionCollection().contains(studySubscription));
    }

    @Test
    public void testSubscribeAllReadOnly() {
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        workspace.getSubscriptionCollection().add(studySubscription);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribeAllReadOnly(workspace);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        assertFalse(workspace.getSubscriptionCollection().contains(studySubscription));
    }

    @Test
    public void testSubscribe() {
        Study study = new Study();
        study.setId(1L);
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertEquals(0, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study, false);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study, false);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.unsubscribe(workspace, study);
        assertEquals(0, workspace.getSubscriptionCollection().size());
    }

    @Test
    public void testUnsubscribeAll() {
        workspaceService.unsubscribeAll(new Study());
        assertTrue(daoStub.retrieveAllSubscribedWorkspacesCalled);
    }

    @Test
    public void testCreateDisplayableStudySummary() {
        StudyHelper studyHelper = new StudyHelper();
        Study study = studyHelper.populateAndRetrieveStudyWithSourceConfigurations();
        DisplayableStudySummary summary = workspaceService.createDisplayableStudySummary(study);
        assertNotNull(summary);
        assertTrue(daoStub.retrieveNumberImagesCalled);
        assertTrue(daoStub.retrievePlatformsForGenomicSourceCalled);
        assertEquals(2, summary.getNumberSubjectAnnotationColumns());
        assertEquals(6, summary.getNumberSubjects());
        assertEquals(study.getShortTitleText(), summary.getStudyName());
        assertEquals(study.getLongTitleText(), summary.getStudyDescription());
        assertTrue(summary.isGenomicStudy());
        assertTrue(summary.isImagingStudy());
        List<DisplayableGenomicSource> genomicSources = summary.getGenomicDataSources();
        assertEquals(1, genomicSources.size());
        DisplayableGenomicSource genomicSource = genomicSources.get(0);
        assertEquals(2, genomicSource.getPlatforms().size());
        assertEquals(2, genomicSource.getNumberSamples());
        assertEquals(0, genomicSource.getNumberMappedSamples());
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

        GenomicDataSourceConfiguration gdsc = genomicSource.getGenomicDataSourceConfiguration();
        assertEquals("", gdsc.getControlSampleSetCommaSeparated());
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName("Sample1");
        gdsc.getControlSampleSetCollection().add(sampleSet);
        sampleSet = new SampleSet();
        sampleSet.setName("Sample2");
        gdsc.getControlSampleSetCollection().add(sampleSet);

        assertTrue(gdsc.getControlSampleSetCommaSeparated().contains("Sample1: 0"));
        assertTrue(gdsc.getControlSampleSetCommaSeparated().contains("Sample2: 0"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateDisplayableStudySummaryInvalid() {
        workspaceService.createDisplayableStudySummary(null);
    }

    @Test
    public void testSavePersistedAnalysisJob() {
        workspaceService.savePersistedAnalysisJob(new GenePatternAnalysisJob());
        assertTrue(daoStub.saveCalled);
    }


    private final class DaoStubNoWorkspace extends CaIntegrator2DaoStub {

        @Override
        public UserWorkspace getWorkspace(String username) {
            return null;
        }
    }

    @Test
    public void testGetEntity() {
        ExternalLinkList externalLinkList = new ExternalLinkList();
        externalLinkList.setId(1L);
        assertNotNull(workspaceService.getRefreshedEntity(externalLinkList));
        assertTrue(daoStub.getCalled);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityNoId() {
        ExternalLinkList externalLinkList = new ExternalLinkList();
        workspaceService.getRefreshedEntity(externalLinkList);
    }

    @Test
    public void testCreateList() throws ValidationException {
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        studySubscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        studySubscription.setUserWorkspace(workspace);
        workspace.getSubscriptionCollection().add(studySubscription);

        // Test Gene List
        GeneList geneList = new GeneList();
        geneList.setVisibility(Visibility.PRIVATE);
        geneList.setSubscription(studySubscription);
        Set<String> geneSymbols = new HashSet<String>();
        geneList.setName("Gene List 1");
        geneSymbols.add("egfr");
        geneSymbols.add("brca1");

        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(1, studySubscription.getGeneLists().size());
        assertNotNull(studySubscription.getGeneList("Gene List 1"));
        assertNull(studySubscription.getGeneList("Gene List 2"));

        geneList = new GeneList();
        geneList.setVisibility(Visibility.PRIVATE);
        geneList.setSubscription(studySubscription);
        geneList.setName("Gene List 2");
        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(2, studySubscription.getGeneLists().size());
        assertNotNull(studySubscription.getGeneList("Gene List 1"));
        assertNotNull(studySubscription.getGeneList("Gene List 2"));

        workspaceService.deleteAbstractList(studySubscription, geneList);
        assertEquals(1, studySubscription.getGeneLists().size());
        assertNotNull(studySubscription.getGeneList("Gene List 1"));
        assertNull(studySubscription.getGeneList("Gene List 2"));


        // Test Subject List
        SubjectList subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.PRIVATE);
        subjectList.setSubscription(studySubscription);
        Set<String> subjects = new HashSet<String>();
        subjectList.setName("Subject List 1");
        subjects.add("100");
        subjects.add("200");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(1, studySubscription.getSubjectLists().size());
        assertNotNull(studySubscription.getSubjectList("Subject List 1"));
        assertNull(studySubscription.getSubjectList("Subject List 2"));

        subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.PRIVATE);
        subjectList.setSubscription(studySubscription);
        subjectList.setName("Subject List 2");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(2, studySubscription.getSubjectLists().size());
        assertNotNull(studySubscription.getSubjectList("Subject List 1"));
        assertNotNull(studySubscription.getSubjectList("Subject List 2"));

        workspaceService.deleteAbstractList(studySubscription, subjectList);
        assertEquals(1, studySubscription.getSubjectLists().size());
        assertNotNull(studySubscription.getSubjectList("Subject List 1"));
        assertNull(studySubscription.getSubjectList("Subject List 2"));

        subjects.add("really long string to prove that identifiers can only be so long");
        try {
            workspaceService.createSubjectList(subjectList, subjects);
            fail("Shouldn't get here due to long identifier");
        } catch(ValidationException e) {
            // noop.
        }

        // Test Global Gene List
        geneList = new GeneList();
        geneList.setVisibility(Visibility.GLOBAL);
        geneList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        geneList.setSubscription(studySubscription);
        geneSymbols = new HashSet<String>();
        geneList.setName("Gene List 1");
        geneSymbols.add("egfr");
        geneSymbols.add("brca1");

        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getGeneLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 2"));

        geneList = new GeneList();
        geneList.setVisibility(Visibility.GLOBAL);
        geneList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        geneList.setSubscription(studySubscription);
        geneList.setName("Gene List 2");
        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(2, studySubscription.getStudy().getStudyConfiguration().getGeneLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 1"));
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 2"));

        workspaceService.deleteAbstractList(studySubscription, geneList);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getGeneLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 2"));


        // Test Global Subject List
        subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.GLOBAL);
        subjectList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        subjectList.setSubscription(studySubscription);
        subjects = new HashSet<String>();
        subjectList.setName("Subject List 1");
        subjects.add("100");
        subjects.add("200");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getSubjectLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 2"));

        subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.GLOBAL);
        subjectList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        subjectList.setSubscription(studySubscription);
        subjectList.setName("Subject List 2");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(2, studySubscription.getStudy().getStudyConfiguration().getSubjectLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 1"));
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 2"));

        workspaceService.deleteAbstractList(studySubscription, subjectList);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getSubjectLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 2"));
    }

}
