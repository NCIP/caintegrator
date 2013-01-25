/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EditDnaAnalysisDataConfigurationActionTest extends AbstractSessionBasedTest {

    private EditDnaAnalysisDataConfigurationAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditDnaAnalysisDataConfigurationActionTest.class);
        action = (EditDnaAnalysisDataConfigurationAction) context.getBean("editDnaAnalysisDataConfigurationAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
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
