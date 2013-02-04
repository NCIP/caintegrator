/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
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

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditGenomicSourceActionTest.class); 
        action = (EditGenomicSourceAction) context.getBean("editGenomicSourceAction");
        studyManagementServiceStub = new StudyManagmentServiceStubForGenomicSource();
        studyManagementServiceStub.clear();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setUpdater(new GenomicDataSourceAjaxUpdaterStub());
        caArrayFacadeStub = new CaArrayFacadeStubForAction();
        action.setCaArrayFacade(caArrayFacadeStub);
    }
    
    @Test
    public void testGetPlatformNameDisable() {
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AGILENT.getValue());
        assertEquals("false", action.getPlatformNameDisable());
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX.getValue());
        assertEquals("true", action.getPlatformNameDisable());
    }

    @Test
    public void testGetAgilentPlatformNames() {
        assertEquals(2, action.getAgilentPlatformNames().size());
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }
    
    @Test
    public void testSave() {
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
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AGILENT.getValue());
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
    
    private static class StudyManagmentServiceStubForGenomicSource extends StudyManagementServiceStub {
        @SuppressWarnings("unchecked")
        @Override
        public <T> T getRefreshedStudyEntity(T entity) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.setStudyConfiguration(new StudyConfiguration());
            super.getRefreshedStudyEntity(entity);
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
