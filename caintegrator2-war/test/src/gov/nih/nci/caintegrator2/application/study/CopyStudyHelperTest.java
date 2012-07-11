/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caintegrator2-war
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caintegrator2-war Software License (the License) is between NCI and You. You (or
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
 * its rights in the caintegrator2-war Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caintegrator2-war Software; (ii) distribute and
 * have distributed to and by third parties the caintegrator2-war Software and any
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
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
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
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalLengthUnitsEnum;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mshestopalov
 *
 */
public class CopyStudyHelperTest extends AbstractMockitoTest {

    private CopyStudyHelper helper;
    private StudyConfiguration copyFrom = null;
    private final String NAME = "name";
    private StudyManagementServiceImpl smSvc= null;

    @Before
    public void setUp() throws Exception {
        StudyHelper studyHelper = new StudyHelper();
        Study study = studyHelper.populateAndRetrieveStudyWithSourceConfigurations();
        copyFrom = study.getStudyConfiguration();
        copyFrom.setId(1L);
        copyFrom.setStudy(study);
        copyFrom.getStudy().setPubliclyAccessible(true);
        ExternalLinkList newExtList = new ExternalLinkList();
        newExtList.setDescription("description");
        newExtList.setName(NAME);
        newExtList.setFileName("filename");
        ExternalLink newExtLink = new ExternalLink();
        newExtLink.setCategory("category");
        newExtLink.setName(NAME);
        newExtLink.setUrl("url");
        newExtList.getExternalLinks().add(newExtLink);
        copyFrom.getExternalLinkLists().add(newExtList);
        SurvivalValueDefinition newSurv = new SurvivalValueDefinition();
        newSurv.setName("NAME");
        newSurv.setDeathDate(studyHelper.getSampleAnnotationDefinition());
        newSurv.setLastFollowupDate(studyHelper.getSampleAnnotationDefinition());
        newSurv.setSurvivalLength(studyHelper.getSampleAnnotationDefinition());
        newSurv.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.DAYS);
        newSurv.setSurvivalStartDate(studyHelper.getSampleAnnotationDefinition());
        newSurv.setSurvivalStatus(studyHelper.getSampleAnnotationDefinition());
        newSurv.setValueForCensored(NAME);
        newSurv.setSurvivalValueType(SurvivalValueTypeEnum.DATE);
        copyFrom.getStudy().getSurvivalValueDefinitionCollection().add(newSurv);
        StudyManagementServiceTest smsTest = new StudyManagementServiceTest();
        smsTest.setUp();
        smSvc = smsTest.getStudyManagementServiceImpl();
        smSvc.setWorkspaceService(workspaceService);
        helper = new CopyStudyHelper(smSvc);
    }


    @Test
    public void testExternalLinks() {
        StudyConfiguration copyTo = createNewStudy(3L);
        assertTrue(copyTo.getExternalLinkLists().isEmpty());
        helper.copyExternalLinks(copyFrom, copyTo);
        assertFalse(copyTo.getExternalLinkLists().isEmpty());
        assertEquals(1, copyTo.getExternalLinkLists().size());
    }

    @Test
    public void testSurvivalValueDefinitions() {
        StudyConfiguration copyTo = createNewStudy(4L);
        assertTrue(copyTo.getStudy().getSurvivalValueDefinitionCollection().isEmpty());
        helper.copySurvivalDefinitions(copyFrom, copyTo);
        assertFalse(copyTo.getStudy().getSurvivalValueDefinitionCollection().isEmpty());
        assertEquals(1, copyTo.getStudy().getSurvivalValueDefinitionCollection().size());
    }

    @Test
    public void testGenomicDataSources() {
        StudyConfiguration copyTo = createNewStudy(5L);
        assertTrue(copyTo.getGenomicDataSources().isEmpty());
        helper.copyStudyGenomicSource(copyFrom, copyTo);
        assertFalse(copyTo.getGenomicDataSources().isEmpty());
        assertEquals(1, copyTo.getGenomicDataSources().size());
    }

    @Test
    public void testImageDataSources() {
        StudyConfiguration copyTo = createNewStudy(6L);
        assertTrue(copyTo.getImageDataSources().isEmpty());
        helper.copyStudyImageSource(copyFrom, copyTo);
        assertFalse(copyTo.getImageDataSources().isEmpty());
        assertEquals(1, copyTo.getImageDataSources().size());
    }

    private StudyConfiguration createNewStudy(Long id) {
        StudyConfiguration copyTo = new StudyConfiguration();
        copyTo.setId(id);
        copyTo.setStudy(new Study());
        return copyTo;
    }

    @Test
    public void testAnnotationGroups() throws ValidationException, ConnectionException, IOException {
        StudyConfiguration copyTo = createNewStudy(7L);
        assertTrue(copyTo.getStudy().getAnnotationGroups().isEmpty());
        helper.copyAnnotationGroups(copyFrom, copyTo);
        assertFalse(copyTo.getStudy().getAnnotationGroups().isEmpty());
        assertEquals(1, copyTo.getStudy().getAnnotationGroups().size());
    }

    @Test
    public void testSubjectAnnotationGroups() throws ValidationException, IOException {
        StudyConfiguration copyTo = createNewStudy(8L);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        smSvc.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration =
            smSvc.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        assertEquals(1, studyConfiguration.getClinicalConfigurationCollection().size());
        assertTrue(copyTo.getClinicalConfigurationCollection().isEmpty());
        helper.copySubjectAnnotationGroups(studyConfiguration, copyTo);
        assertFalse(copyTo.getClinicalConfigurationCollection().isEmpty());
        assertEquals(1, copyTo.getClinicalConfigurationCollection().size());
    }

    @Test
    public void testAddStudyLogo() throws IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        smSvc.save(studyConfiguration);
        smSvc.addStudyLogo(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(), "image/jpeg");
        assertEquals(TestDataFiles.VALID_FILE.getName(), studyConfiguration.getStudyLogo().getFileName());
    }

}
