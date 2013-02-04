/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("PMD")
public class EditCopyNumberDataConfigurationActionTest extends AbstractSessionBasedTest {

    private EditCopyNumberDataConfigurationAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();

        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditCopyNumberDataConfigurationActionTest.class); 
        action = (EditCopyNumberDataConfigurationAction) context.getBean("editCopyNumberDataConfigurationAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }
    
    @Test
    public void testEdit() {
        action.setUseGlad(true);
        action.setGladUrl(ConfigurationParameter.GENE_PATTERN_URL.getDefaultValue());
        action.getGenomicSource().setCopyNumberDataConfiguration(null);
        action.prepare();
        action.edit();
        assertNotNull(action.getGenomicSource().getCopyNumberDataConfiguration());
        assertEquals(ConfigurationParameter.GENE_PATTERN_URL.getDefaultValue(), action.getGladUrl());
    }
    
    @Test
    public void testSave() {
        action.prepare();
        action.setUseGlad(true);
        action.setGladUrl("gladUrl");
        action.setCaDnaCopyUrl("caDnaCopyUrl");
        action.save();
        assertEquals("gladUrl", action.getCopyNumberDataConfiguration().getSegmentationService().getUrl());
        assertTrue(studyManagementServiceStub.saveCopyNumberMappingFileCalled);
        assertTrue(studyManagementServiceStub.saveCalled);
        action.setUseGlad(false);
        action.save();
        assertEquals("caDnaCopyUrl", action.getCopyNumberDataConfiguration().getSegmentationService().getUrl());
    }
    
    @Test
    public void testValidate() {
        action.prepare();
        action.setFormAction(EditCopyNumberDataConfigurationAction.EDIT_ACTION);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setFormAction(EditCopyNumberDataConfigurationAction.SAVE_ACTION);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.setCopyNumberMappingFile(TestDataFiles.XBA_COPY_NUMBER_CHP_FILE);
        action.getCopyNumberDataConfiguration().getSegmentationService().setUrl("");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.setUseGlad(false);
        action.setCaDnaCopyUrl("url");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.setCopyNumberMappingFile(TestDataFiles.REMBRANDT_COPY_NUMBER_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
    }
    
}
