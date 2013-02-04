/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationFile;
import gov.nih.nci.caintegrator.application.study.AnnotationFileStub;
import gov.nih.nci.caintegrator.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.FileColumn;
import gov.nih.nci.caintegrator.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator.application.study.ImageAnnotationUploadType;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.aim.AIMFacade;
import gov.nih.nci.caintegrator.external.aim.ImageSeriesAnnotationsWrapper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.EditImagingSourceAnnotationsAction;
import gov.nih.nci.caintegrator.web.ajax.IImagingDataSourceAjaxUpdater;

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
