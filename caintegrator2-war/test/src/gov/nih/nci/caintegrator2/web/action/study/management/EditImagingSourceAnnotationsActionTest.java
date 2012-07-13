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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationFileStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationUploadType;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.aim.AIMFacade;
import gov.nih.nci.caintegrator2.external.aim.ImageSeriesAnnotationsWrapper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IImagingDataSourceAjaxUpdater;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.opensymphony.xwork2.Action;

public class EditImagingSourceAnnotationsActionTest extends AbstractSessionBasedTest {

    private EditImagingSourceAnnotationsAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    private IImagingDataSourceAjaxUpdater updater;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = mock(IImagingDataSourceAjaxUpdater.class);

        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new EditImagingSourceAnnotationsAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);

        AIMFacade aimFacade = mock(AIMFacade.class);
        when(aimFacade.retrieveImageSeriesAnnotations(any(ServerConnectionProfile.class),
                anyCollectionOf(ImageSeries.class))).thenAnswer(new Answer<Map<ImageSeries, ImageSeriesAnnotationsWrapper>>() {
                    @Override
                    public Map<ImageSeries, ImageSeriesAnnotationsWrapper> answer(InvocationOnMock invocation) throws Throwable {
                        Collection<ImageSeries> images = (Collection<ImageSeries>) invocation.getArguments()[1];
                        Map<ImageSeries, ImageSeriesAnnotationsWrapper> results = new HashMap<ImageSeries, ImageSeriesAnnotationsWrapper>();
                        for (ImageSeries imageSeries : images) {
                            ImageSeriesAnnotationsWrapper annotations = new ImageSeriesAnnotationsWrapper();
                            annotations.addDefinitionValueToGroup("Group", "Definition", "Value");
                            results.put(imageSeries, annotations);
                        }
                        return results;
                    }
                });

        action.setUpdater(updater);
        action.setAimFacade(aimFacade);
        setupActionVariables();
    }

    @Test
    public void testValidate() {
        action.setAimReload(true);
        action.validate();
        assertEquals(null, action.getAimServerProfile().getUrl());
        action.setAimReload(false);
        action.validate();
        action.getAimServerProfile().setUrl("http://abc.com");
        action.validate();
        assertEquals("http://abc.com", action.getAimServerProfile().getUrl());
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        assertEquals(2, action.getDisplayableFields().size());
        assertEquals(2, action.getSelectableAnnotationGroups().size());

        action.getImageSourceConfiguration().getImageAnnotationConfiguration().setUploadType(ImageAnnotationUploadType.AIM);
        action.setUploadType(ImageAnnotationUploadType.AIM.getValue());
        assertEquals("display: block;", action.getAimInputCssStyle());
        assertEquals("display: none;", action.getFileInputCssStyle());
        action.prepare();
        assertTrue(action.isAimReload());
    }

    @Test
    public void testAddImageAnnotations() {
        action.clearErrorsAndMessages();
        action.setUploadType(ImageAnnotationUploadType.FILE.getValue());
        assertEquals("display: block;", action.getFileInputCssStyle());
        assertEquals("display: none;", action.getAimInputCssStyle());
        action.setImageAnnotationFile(TestDataFiles.VALID_FILE);
        action.setImageAnnotationFileFileName(TestDataFiles.VALID_FILE.getName());
        assertEquals(Action.SUCCESS, action.addImageAnnotations());

        action.setImageAnnotationFile(TestDataFiles.INVALID_FILE_MISSING_VALUE);
        assertEquals(Action.INPUT, action.addImageAnnotations());
        action.clearErrorsAndMessages();
        action.setImageAnnotationFile(TestDataFiles.INVALID_FILE_DOESNT_EXIST);
        assertEquals(Action.ERROR, action.addImageAnnotations());
        action.clearErrorsAndMessages();
        action.setImageAnnotationFile(null);
        assertEquals(Action.INPUT, action.addImageAnnotations());
    }

    @Test
    public void testAddAimImageAnnotations() {
        action.clearErrorsAndMessages();
        action.setUploadType(ImageAnnotationUploadType.AIM.getValue());
        assertEquals(Action.INPUT, action.addImageAnnotations());
        action.getAimServerProfile().setUrl("http://abc.com");
        action.clearErrorsAndMessages();
        assertEquals("loadingAimAnnotation", action.addImageAnnotations());
    }

    @Test
    public void testMisc() {
        action.setUploadType(ImageAnnotationUploadType.AIM.getValue());
        assertEquals(ImageAnnotationUploadType.AIM.getValue(), action.getUploadType());
    }

    @Test
    public void testSave() {
        action.prepare();

        action.getDisplayableFields().get(0).setAnnotationGroupName("imageSeriesGroup");
        assertEquals(Action.SUCCESS, action.save());
        assertEquals("imageSeriesGroup", action.getDisplayableFields().get(0).getFieldDescriptor().getAnnotationGroup().getName());
    }

    private void setupActionVariables() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setName("subjectGroup");
        AnnotationGroup annotationGroup2 = new AnnotationGroup();
        annotationGroup2.setName("imageSeriesGroup");
        studyConfiguration.getStudy().getAnnotationGroups().add(annotationGroup);
        studyConfiguration.getStudy().getAnnotationGroups().add(annotationGroup2);
        action.setStudyConfiguration(studyConfiguration);

        action.getImageSourceConfiguration().setId(1L);
        ImageAnnotationConfiguration imageAnnotations = new ImageAnnotationConfiguration();
        imageAnnotations.setAnnotationFile(createAnnotationFile());
        action.getImageSourceConfiguration().setImageAnnotationConfiguration(imageAnnotations);
    }

    private AnnotationFile createAnnotationFile() {
        AnnotationDefinition subjectDef1 = new AnnotationDefinition();
        subjectDef1.setId(Long.valueOf(1));
        AnnotationDefinition subjectDef2 = new AnnotationDefinition();
        subjectDef2.setId(Long.valueOf(2));

        DelimitedTextClinicalSourceConfiguration clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        AnnotationFileStub annotationFile = new AnnotationFileStub();
        clinicalConf.setAnnotationFile(annotationFile);

        addColumn(annotationFile, subjectDef1);
        addColumn(annotationFile, subjectDef2);

        return annotationFile;
    }

    private void addColumn(AnnotationFile annotationFile, AnnotationDefinition subjectDef) {
        FileColumn column = new FileColumn();
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setShownInBrowse(true);
        fieldDescriptor.setDefinition(subjectDef);
        column.setFieldDescriptor(fieldDescriptor);
        column.setAnnotationFile(annotationFile);
        annotationFile.getColumns().add(column);
    }
}
