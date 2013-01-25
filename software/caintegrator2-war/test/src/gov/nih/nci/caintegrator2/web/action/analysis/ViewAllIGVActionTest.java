/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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
public class ViewAllIGVActionTest extends AbstractSessionBasedTest {

    ViewAllIGVAction viewAllIGVAction = new ViewAllIGVAction();
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
        viewAllIGVAction.setWorkspaceService(workspaceService);
        viewAllIGVAction.setQueryManagementService(queryManagementService);
        viewAllIGVAction.setArrayDataService(arrayDataService);
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
        viewAllIGVAction.prepare();
        assertTrue(queryManagementService.retrieveCopyNumberPlatformsForStudyCalled);
        assertTrue(queryManagementService.retrieveGeneExpressionPlatformsForStudyCalled);
        
        // Test validate
        viewAllIGVAction.validate();
        assertFalse(viewAllIGVAction.hasActionErrors());
        viewAllIGVAction.setSelectedAction("viewAll");
        viewAllIGVAction.setExpressionPlatformName("hg18");
        viewAllIGVAction.validate();
        assertFalse(viewAllIGVAction.hasActionErrors());
        viewAllIGVAction.setCopyNumberPlatformName("hg17");
        viewAllIGVAction.validate();
        assertTrue(viewAllIGVAction.hasActionErrors());
    }
    
    @Test
    public void testExecute() {

        // Test Prepare
        viewAllIGVAction.prepare();
        assertTrue(queryManagementService.retrieveCopyNumberPlatformsForStudyCalled);
        assertTrue(queryManagementService.retrieveGeneExpressionPlatformsForStudyCalled);
        
        // Test execute
        assertEquals("success", viewAllIGVAction.execute());
        viewAllIGVAction.setSelectedAction("cancel");
        assertEquals("homePage", viewAllIGVAction.execute());
        
        //Test get platform option
        assertEquals("-- None Available --", viewAllIGVAction.getExpressionPlatformOption());
        assertEquals("-- None Available --", viewAllIGVAction.getCopyNumberPlatformOption());
        Set<String> platforms = new HashSet<String>();
        platforms.add("Platform 1");
        viewAllIGVAction.setExpressionPlatformsInStudy(platforms);
        viewAllIGVAction.setCopyNumberPlatformsInStudy(platforms);
        assertEquals("", viewAllIGVAction.getExpressionPlatformOption());
        assertEquals("", viewAllIGVAction.getCopyNumberPlatformOption());
        
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
