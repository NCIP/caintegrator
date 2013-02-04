/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.file.FileManagerStub;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IPlatformDeploymentAjaxUpdater;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class ManagePlatformsActionTest extends AbstractSessionBasedTest {

    ManagePlatformsAction action = new ManagePlatformsAction();
    ArrayDataServiceStub arrayDataServiceStub = new ArrayDataServiceStub();
    FileManagerStub fileManagerStub = new FileManagerStub();
    PlatformDeploymentAjaxUpdaterStub ajaxUpdater = new PlatformDeploymentAjaxUpdaterStub();
    
    @Before
    public void setUp() {
        super.setUp();
        WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
        DisplayableUserWorkspace displayableWorkspace = 
            SessionHelper.getInstance().getDisplayableUserWorkspace();
        displayableWorkspace.refresh(workspaceService, true);
        
        ajaxUpdater = new PlatformDeploymentAjaxUpdaterStub();
        action.setWorkspaceService(workspaceService);
        action.setArrayDataService(arrayDataServiceStub);        
        action.setFileManager(fileManagerStub);
        action.setAjaxUpdater(ajaxUpdater);
    }
    
    @Test
    public void testPrepare() {
        SessionHelper.getInstance().setPlatformManager(false);
        action.prepare();
        assertFalse(SessionHelper.getInstance().isAuthorizedPage());
        
        SessionHelper.getInstance().setPlatformManager(true);
        action.prepare();
        assertTrue(SessionHelper.getInstance().isAuthorizedPage());
    }
    
    @Test
    public void testExecute() {
        assertEquals(ActionSupport.SUCCESS, action.execute());
    }
    
    @Test
    public void testSetPlatformFileFileName() {
        action.setPlatformFileFileName("abcdef.XML");
        assertTrue(action.getPlatformFileFileName().endsWith(".xml"));
        action.setPlatformFileFileName("abcdef.test.adf");
        assertTrue(action.getPlatformFileFileName().endsWith(".adf"));
        action.setPlatformFileFileName("abcdef");
        assertTrue(action.getPlatformFileFileName().endsWith("abcdef"));
    }
    
    @Test
    public void testCreatePlatform() {
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue());
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        assertTrue(ajaxUpdater.runJobCalled);
        ajaxUpdater.runJobCalled = false;
        
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP.getValue());
        action.getPlatformForm().getAnnotationFiles().add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        action.getPlatformForm().getAnnotationFiles().add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        assertEquals(ActionSupport.ERROR, action.createPlatform());
        assertFalse(ajaxUpdater.runJobCalled);
        
        action.setPlatformName("Mapping_50K");
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        assertTrue(ajaxUpdater.runJobCalled);
        ajaxUpdater.runJobCalled = false;
        
        action.setPlatformType(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue());
        action.setPlatformFile(TestArrayDesignFiles.HUMAN_GENOME_CGH244A_ANNOTATION_FILE);
        action.setPlatformName("CGH244A");
        action.setPlatformFileFileName(TestArrayDesignFiles.HUMAN_GENOME_CGH244A_ANNOTATION_PATH);
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        assertTrue(ajaxUpdater.runJobCalled);
        ajaxUpdater.runJobCalled = false;
        
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue());
        action.setPlatformFile(TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_FILE);
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        assertTrue(ajaxUpdater.runJobCalled);
        ajaxUpdater.runJobCalled = false;
    }
    
    @Test
    public void testAddAnnotationFile() {
        action.setSelectedAction("addAnnotationFile");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
    }

        
    @Test
    public void testValidation() {
        action.setSelectedAction("managePlatforms");
        action.validate();
        assertFalse(action.hasFieldErrors());

        // Test addAnnotationFile
        action.setSelectedAction("addAnnotationFile");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();

        action.setSelectedAction("addAnnotationFile");
        action.setPlatformFile(TestArrayDesignFiles.EMPTY_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();

        action.setSelectedAction("addAnnotationFile");
        action.setPlatformFile(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_PATH);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();

        // test invalid file extension
        action.setSelectedAction("addAnnotationFile");
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_CDF_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_CDF_PATH);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();        
        
        action.clearErrorsAndMessages();
        action.setSelectedAction("addAnnotationFile");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP.getValue());
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_CDF_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_CDF_PATH);
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        //action.clearErrorsAndMessages();
        //action.setSelectedAction("addAnnotationFile");
        //action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        //action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_ANNOTATION_PATH);
        //action.setPlatformType("notAValidPlatformType");
        //action.validate();
        
        // Test createPlatform
        action.setSelectedAction("createPlatform");
        action.setPlatformFile(null);
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP.getValue());
        action.setPlatformName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertTrue(action.hasActionErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue());
        action.setPlatformFile(TestArrayDesignFiles.EMPTY_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_ANNOTATION_PATH);
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue());
        action.setPlatformName("");
        action.setPlatformFile(TestArrayDesignFiles.EMPTY_FILE);
        action.setPlatformFileFileName("empt.csv");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName("abc.adf");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP.getValue());
        action.setPlatformName("");
        action.setPlatformFile(TestArrayDesignFiles.EMPTY_FILE);
        action.setPlatformFileFileName("empt.csv");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName("abc.adf");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.getPlatformForm().getAnnotationFiles().add(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue());
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName("abc.adf");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFileFileName("abc.csv");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformName("");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFileFileName("abc.xml");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue());
        action.setPlatformName("Agilent Copy Number Platform");
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName("abc.xml");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFileFileName("abc.csv");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformFileFileName("abc.adf");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.clearErrorsAndMessages();
        action.setPlatformName("");
        action.validate();
        assertTrue(action.hasFieldErrors());
    }

    @Test
    public void testGetDisabled() {
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue());
        assertEquals(action.getPlatformNameDisplay(), "display: none;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: none;");
        assertEquals(action.getAddButtonDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP.getValue());
        assertEquals(action.getPlatformNameDisplay(), "display: block;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: none;");
        assertEquals(action.getAddButtonDisplay(), "display: block;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue());
        assertEquals(action.getPlatformNameDisplay(), "display: block;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: none;");
        assertEquals(action.getAddButtonDisplay(), "display: block;");
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue());
        assertEquals(action.getPlatformNameDisplay(), "display: block;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: block;");
        assertEquals(action.getAddButtonDisplay(), "display: none;");

        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue());
        assertEquals(action.getCsvlFileDisplay(), "display: block;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP.getValue());
        assertEquals(action.getCsvlFileDisplay(), "display: block;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue());
        assertEquals(action.getCsvlFileDisplay(), "display: block;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue());
        assertEquals(action.getCsvlFileDisplay(), "display: none;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: block;");
    }

    @Test
    public void testUpdatePlatform() {
        action.setSelectedAction("delete");
        assertEquals(ActionSupport.ERROR, action.updatePlatform());
        action.setPlatformConfigurationId("1");
        assertEquals(ActionSupport.SUCCESS, action.updatePlatform());
        action.setSelectedAction("updatePlatformType");
        action.setSelectedPlatformType("Affymetrix SNP");
        assertEquals(ActionSupport.SUCCESS, action.updatePlatform());
    }

    private static class PlatformDeploymentAjaxUpdaterStub implements IPlatformDeploymentAjaxUpdater {

        public boolean runJobCalled = false;
        
        public void initializeJsp() {

        }

        public void runJob(PlatformConfiguration platformConfiguration, String username) {
            runJobCalled = true;
        }
        
    }
}

