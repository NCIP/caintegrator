/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacadeStub;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IGenomicDataSourceAjaxUpdater;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

@SuppressWarnings("PMD")
public class EditGenomicSourceActionTest extends AbstractSessionBasedTest {

    private EditGenomicSourceAction action;
    private StudyManagmentServiceStubForGenomicSource studyManagementServiceStub;
    private CaArrayFacadeStubForAction caArrayFacadeStub;
    private WorkspaceServiceStub workspaceServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditGenomicSourceActionTest.class); 
        action = (EditGenomicSourceAction) context.getBean("editGenomicSourceAction");
        studyManagementServiceStub = new StudyManagmentServiceStubForGenomicSource();
        studyManagementServiceStub.clear();
        workspaceServiceStub = (WorkspaceServiceStub) context.getBean("workspaceService");
        workspaceServiceStub.clear();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setUpdater(new GenomicDataSourceAjaxUpdaterStub());
        caArrayFacadeStub = new CaArrayFacadeStubForAction();
        action.setCaArrayFacade(caArrayFacadeStub);
    }
    
    @Test
    public void testAddNew() {
        action.addNew();
        assertEquals(PlatformVendorEnum.AFFYMETRIX.getValue(), action.getTempGenomicSource().getPlatformVendor().getValue());
        assertEquals(PlatformDataTypeEnum.EXPRESSION, action.getTempGenomicSource().getDataType());
    }

    @Test
    public void testGetAgilentPlatformNames() {
        action.getTempGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        action.getTempGenomicSource().setDataType(PlatformDataTypeEnum.EXPRESSION);
        assertEquals(1, action.getFilterPlatformNames().size());
        action.getTempGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        action.getTempGenomicSource().setDataType(PlatformDataTypeEnum.SNP);
        assertEquals(0, action.getFilterPlatformNames().size());
        action.getTempGenomicSource().setPlatformVendor(PlatformVendorEnum.AGILENT);
        action.getTempGenomicSource().setDataType(PlatformDataTypeEnum.EXPRESSION);
        assertEquals(0, action.getFilterPlatformNames().size());
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
        assertEquals(Action.SUCCESS, action.cancel());
        assertNotNull(action.getCaArrayFacade());
    }

    @Test
    public void testRefresh() {
        assertEquals(Action.SUCCESS, action.refresh());
        action.getTempGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        assertEquals(Action.SUCCESS, action.refresh());
    }

    @Test
    public void testGetDataType() {
        action.getTempGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        assertEquals(2, action.getDataTypes().size());
    }
    
    @Test
    public void testSave() {
        action.setTempGenomicSource(action.getGenomicSource());
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        assertEquals(Action.INPUT, action.save());
        assertTrue(workspaceServiceStub.clearSessionCalled);
        action.getGenomicSource().setPlatformName("Platform name");
        assertEquals(Action.SUCCESS, action.save());
        GenomicDataSourceAjaxUpdaterStub updaterStub = (GenomicDataSourceAjaxUpdaterStub) action.getUpdater();
        assertTrue(updaterStub.runJobCalled);
        updaterStub.runJobCalled = false;
        studyManagementServiceStub.clear();
        action.getGenomicSource().setId(1L);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(action.getGenomicSource());
        action.getGenomicSource().setStudyConfiguration(studyConfiguration);
        action.setStudyConfiguration(studyConfiguration);
        assertEquals(Action.SUCCESS, action.save());
        assertTrue(studyManagementServiceStub.deleteCalled);
        assertTrue(updaterStub.runJobCalled);
        
        // Test platform validation.
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AGILENT);
        action.getGenomicSource().setPlatformName("");
        assertEquals(Action.INPUT, action.save());
        action.getGenomicSource().setPlatformName("Name");
        assertEquals(Action.SUCCESS, action.save());
        
        // Test server validation.
        caArrayFacadeStub.throwConnectionException = true;
        assertEquals(Action.INPUT, action.save());
        
        caArrayFacadeStub.clear();
        caArrayFacadeStub.throwExperimentNotFoundException = true;
        assertEquals(Action.INPUT, action.save());
        
    }
    
    @Test
    public void testPrepare() {
        action.getGenomicSource().setId(1L);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }
    
    @Test
    public void testDelete() {
        assertEquals(Action.SUCCESS, action.delete());
        action.getStudyConfiguration().getGenomicDataSources().add(action.getGenomicSource());
        assertEquals(Action.SUCCESS, action.delete());
    }
    
    @Test
    public void testGetVarianceInputCssStyle() {
        assertEquals("display: block;", action.getVarianceInputCssStyle());
    }
    
    private static class StudyManagmentServiceStubForGenomicSource extends StudyManagementServiceStub {
        @SuppressWarnings("unchecked")
        @Override
        public <T> T getRefreshedEntity(T entity) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.setStudyConfiguration(new StudyConfiguration());
            super.getRefreshedEntity(entity);
            return (T) genomicSource;
        }
    }
    
    private static class GenomicDataSourceAjaxUpdaterStub implements IGenomicDataSourceAjaxUpdater {
        
        public boolean runJobCalled = false;
        
        public void initializeJsp() {
            
        }
        
        public void runJob(Long id) {
            runJobCalled = true;
        }
        
    }
    
    private static class CaArrayFacadeStubForAction extends CaArrayFacadeStub {
        public boolean throwConnectionException = false;
        public boolean throwExperimentNotFoundException = false;
        
        public void clear() {
            throwConnectionException = false;
            throwExperimentNotFoundException = false;
        }
        
        @Override
        public void validateGenomicSourceConnection(GenomicDataSourceConfiguration genomicSource)
                throws ConnectionException, ExperimentNotFoundException {
            if (throwConnectionException) {
                throw new ConnectionException("Exception Thrown");
            }
            if (throwExperimentNotFoundException) {
                throw new ExperimentNotFoundException("Exception Thrown");
            }
        }
    }
}
