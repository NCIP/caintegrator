/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;


/**
 * 
 */
public class ViewAllHeatmapActionTest extends AbstractSessionBasedTest {

    ViewAllHeatmapAction viewAllHeatmapAction = new ViewAllHeatmapAction();
    private StudySubscription subscription;
    private QueryManagementServiceStub queryManagementService = new QueryManagementServiceStub();
    private MyArrayDataServiceStub arrayDataService = new MyArrayDataServiceStub();

    @Before
    public void setUp() {
        super.setUp();
        subscription = new StudySubscription();
        subscription.setId(Long.valueOf(1));
        Study study = createFakeStudy();
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
        workspaceService.setSubscription(subscription);
        viewAllHeatmapAction.setWorkspaceService(workspaceService);
        viewAllHeatmapAction.setQueryManagementService(queryManagementService);
        viewAllHeatmapAction.setArrayDataService(arrayDataService);
    }
    
    private Study createFakeStudy() {
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        return study;
    }
    
    @Test
    public void testValidate() {
        
        // Test Prepare
        viewAllHeatmapAction.prepare();
        assertTrue(queryManagementService.retrieveCopyNumberPlatformsForStudyCalled);
        
        // Test validate
        viewAllHeatmapAction.validate();
        assertFalse(viewAllHeatmapAction.hasActionErrors());
        viewAllHeatmapAction.setSelectedAction("viewAll");
        viewAllHeatmapAction.validate();
        assertTrue(viewAllHeatmapAction.hasActionErrors());
        viewAllHeatmapAction.clearActionErrors();
        viewAllHeatmapAction.setCopyNumberPlatformName("hg17");
        viewAllHeatmapAction.validate();
        assertFalse(viewAllHeatmapAction.hasActionErrors());
    }
    
    @Test
    public void testExecute() {

        // Test Prepare
        viewAllHeatmapAction.prepare();
        assertTrue(queryManagementService.retrieveCopyNumberPlatformsForStudyCalled);
        
        // Test execute
        assertEquals("success", viewAllHeatmapAction.execute());
        viewAllHeatmapAction.setSelectedAction("cancel");
        assertEquals("homePage", viewAllHeatmapAction.execute());
        
        //Test get platform option
        assertEquals("-- None Available --", viewAllHeatmapAction.getCopyNumberPlatformOption());
        Set<String> platforms = new HashSet<String>();
        platforms.add("Platform 1");
        viewAllHeatmapAction.setCopyNumberPlatformsInStudy(platforms);
        assertEquals("", viewAllHeatmapAction.getCopyNumberPlatformOption());
        
    }
    
    class MyArrayDataServiceStub extends ArrayDataServiceStub {
        public Platform getPlatform(String platformName) {
            Platform platform = new Platform();
            platform.setName(platformName);
            ReporterList reporterList = new ReporterList("reporterName", ReporterTypeEnum.GENE_EXPRESSION_GENE);
            reporterList.setGenomeVersion(platformName);
            reporterList.setPlatform(platform);
            platform.addReporterList(reporterList);
            return platform;
        }
    }
}
