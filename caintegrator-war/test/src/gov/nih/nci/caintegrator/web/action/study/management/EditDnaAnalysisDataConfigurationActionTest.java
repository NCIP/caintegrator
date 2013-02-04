/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.EditDnaAnalysisDataConfigurationAction;

import org.junit.Before;
import org.junit.Test;

public class EditDnaAnalysisDataConfigurationActionTest extends AbstractSessionBasedTest {

    private EditDnaAnalysisDataConfigurationAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new EditDnaAnalysisDataConfigurationAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
        action.setConfigurationHelper(configurationHelper);
    }

    @Test
    public void testEdit() {
        action.setUseGlad(true);
        action.getDnaAnalysisDataConfiguration().getSegmentationService().setUrl(ConfigurationParameter.GENE_PATTERN_URL.getDefaultValue());
        action.getGenomicSource().setDnaAnalysisDataConfiguration(null);
        action.prepare();
        action.edit();
        assertNotNull(action.getGenomicSource().getDnaAnalysisDataConfiguration());
        assertEquals(ConfigurationParameter.GENE_PATTERN_URL.getDefaultValue(), action.getDnaAnalysisDataConfiguration().getSegmentationService().getUrl());
    }

    @Test
    public void testSave() {
        action.prepare();
        action.setCaDnaCopyUrl("caDnaCopyUrl");
        action.setCaCghCallUrl("caCghCallUrl");
        action.getDnaAnalysisDataConfiguration().setUseCghCall(false);
        action.save();
        assertEquals("caDnaCopyUrl", action.getDnaAnalysisDataConfiguration().getSegmentationService().getUrl());
        action.getDnaAnalysisDataConfiguration().setUseCghCall(true);
        action.save();
        assertEquals("caCghCallUrl", action.getDnaAnalysisDataConfiguration().getSegmentationService().getUrl());
        assertTrue(studyManagementServiceStub.saveDnaAnalysisMappingFileCalled);
        assertTrue(studyManagementServiceStub.saveCalled);
    }

    @Test
    public void testValidate() {
        action.prepare();
        action.setFormAction(EditDnaAnalysisDataConfigurationAction.EDIT_ACTION);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setFormAction(EditDnaAnalysisDataConfigurationAction.SAVE_ACTION);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.setMappingFile(TestDataFiles.XBA_COPY_NUMBER_CHP_FILE);
        action.getDnaAnalysisDataConfiguration().getSegmentationService().setUrl("");
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.setUseGlad(false);
        action.getDnaAnalysisDataConfiguration().getSegmentationService().setUrl("url");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        action.setGenomicSource(genomicSource);
        action.setMappingFile(TestDataFiles.REMBRANDT_COPY_NUMBER_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
        genomicSource.setPlatformVendor(PlatformVendorEnum.AGILENT);
        action.setMappingFile(TestDataFiles.REMBRANDT_COPY_NUMBER_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.setMappingFile(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.getGenomicSource().setLoadingType(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setMappingFile(TestDataFiles.REMBRANDT_COPY_NUMBER_SINGLE_FILE);
    }

}
