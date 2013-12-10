/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.TestArrayDesignFiles;
import gov.nih.nci.caintegrator.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.IPlatformDeploymentAjaxUpdater;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class ManagePlatformsActionTest extends AbstractSessionBasedTest {
    private ManagePlatformsAction action = new ManagePlatformsAction();
    private IPlatformDeploymentAjaxUpdater updater;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = mock(IPlatformDeploymentAjaxUpdater.class);

        DisplayableUserWorkspace displayableWorkspace = SessionHelper.getInstance().getDisplayableUserWorkspace();
        displayableWorkspace.refresh(workspaceService, true);

        action.setWorkspaceService(workspaceService);
        action.setArrayDataService(arrayDataService);
        action.setFileManager(fileManager);
        action.setAjaxUpdater(updater);
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
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        verify(updater, times(1)).runJob(any(PlatformConfiguration.class), anyString());

        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
        action.getPlatformForm().getAnnotationFiles().add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        action.getPlatformForm().getAnnotationFiles().add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        assertEquals(ActionSupport.ERROR, action.createPlatform());
        verify(updater, times(1)).runJob(any(PlatformConfiguration.class), anyString());

        action.setPlatformName("Mapping_50K");
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        verify(updater, times(2)).runJob(any(PlatformConfiguration.class), anyString());

        action.setPlatformType(PlatformTypeEnum.AGILENT_GENE_EXPRESSION);
        action.setPlatformFile(TestArrayDesignFiles.HUMAN_GENOME_CGH244A_ANNOTATION_FILE);
        action.setPlatformName("CGH244A");
        action.setPlatformFileFileName(TestArrayDesignFiles.HUMAN_GENOME_CGH244A_ANNOTATION_PATH);
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        verify(updater, times(3)).runJob(any(PlatformConfiguration.class), anyString());

        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER);
        action.setPlatformFile(TestArrayDesignFiles.AGILENT_HG_CGH_244A_TCGA_ADF_ANNOTATION_FILE);
        assertEquals(ActionSupport.SUCCESS, action.createPlatform());
        verify(updater, times(4)).runJob(any(PlatformConfiguration.class), anyString());
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

        // test for invalid file extensions
        action.setSelectedAction("addAnnotationFile");
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_CDF_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_CDF_PATH);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();

        action.clearErrorsAndMessages();
        action.setSelectedAction("addAnnotationFile");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_CDF_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_CDF_PATH);
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setSelectedAction("addAnnotationFile");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER);
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_CDF_FILE);
        action.setPlatformFileFileName("abc.cdf");
        action.validate();
        assertTrue(action.hasFieldErrors());

        // Test createPlatform
        action.setSelectedAction("createPlatform");
        action.setPlatformFile(null);
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
        action.setPlatformName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertTrue(action.hasActionErrors());

        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        action.setPlatformFile(TestArrayDesignFiles.EMPTY_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName(TestArrayDesignFiles.HG_U133A_ANNOTATION_PATH);
        action.validate();
        assertFalse(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        action.setPlatformName("");
        action.setPlatformFile(TestArrayDesignFiles.EMPTY_FILE);
        action.setPlatformFileFileName("empt.csv");
        action.validate();
        assertTrue(action.hasFieldErrors());


        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName("empt.csv");
        action.validate();
        assertFalse(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setPlatformFileFileName("file.invalidextension");
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setPlatformFile(TestArrayDesignFiles.HG_U133A_ANNOTATION_FILE);
        action.setPlatformFileFileName("abc.adf");
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
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
        action.setPlatformType(PlatformTypeEnum.AGILENT_GENE_EXPRESSION);
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
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER);
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
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        assertEquals(action.getPlatformNameDisplay(), "display: none;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: none;");
        assertEquals(action.getAddButtonDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
        assertEquals(action.getPlatformNameDisplay(), "display: block;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: none;");
        assertEquals(action.getAddButtonDisplay(), "display: block;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER);
        assertEquals(action.getPlatformNameDisplay(), "display: block;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: none;");
        assertEquals(action.getAddButtonDisplay(), "display: block;");
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER);
        assertEquals(action.getPlatformNameDisplay(), "display: block;");
        assertEquals(action.getPlatformChannelTypeDisplay(), "display: block;");
        assertEquals(action.getAddButtonDisplay(), "display: none;");

        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        assertEquals(action.getCsvlFileDisplay(), "display: block;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
        assertEquals(action.getCsvlFileDisplay(), "display: block;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER);
        assertEquals(action.getCsvlFileDisplay(), "display: block;");
        assertEquals(action.getAdfGemlFileDisplay(), "display: none;");
        action.setPlatformType(PlatformTypeEnum.AGILENT_COPY_NUMBER);
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
        action.setSelectedPlatformType(PlatformTypeEnum.AFFYMETRIX_SNP);
        assertEquals(ActionSupport.SUCCESS, action.updatePlatform());
    }
}

