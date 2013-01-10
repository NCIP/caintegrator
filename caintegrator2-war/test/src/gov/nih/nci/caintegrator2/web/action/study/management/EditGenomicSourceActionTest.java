/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IGenomicDataSourceAjaxUpdater;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class EditGenomicSourceActionTest extends AbstractSessionBasedTest {

    private EditGenomicSourceAction action;
    private StudyManagmentServiceStubForGenomicSource studyManagementServiceStub;
    private CaArrayFacade caArrayFacade;
    private IGenomicDataSourceAjaxUpdater updater;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = mock(IGenomicDataSourceAjaxUpdater.class);
        caArrayFacade = mock(CaArrayFacade.class);
        studyManagementServiceStub = new StudyManagmentServiceStubForGenomicSource();

        action = new EditGenomicSourceAction();
        action.setConfigurationHelper(configurationHelper);
        action.setWorkspaceService(workspaceService);
        action.setStudyManagementService(studyManagementServiceStub);
        action.setUpdater(updater);
        action.setCaArrayFacade(caArrayFacade);
        action.setArrayDataService(arrayDataService);
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
        verify(workspaceService, times(1)).clearSession();
        action.getGenomicSource().setPlatformName("Platform name");
        assertEquals(Action.SUCCESS, action.save());
        verify(updater, times(1)).runJob(anyLong());
        studyManagementServiceStub.clear();
        action.getGenomicSource().setId(1L);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(action.getGenomicSource());
        action.getGenomicSource().setStudyConfiguration(studyConfiguration);
        action.setStudyConfiguration(studyConfiguration);
        assertEquals(Action.SUCCESS, action.save());
        assertTrue(studyManagementServiceStub.deleteCalled);
        verify(updater, times(2)).runJob(anyLong());

        // Test platform validation.
        action.getGenomicSource().setPlatformVendor(PlatformVendorEnum.AGILENT);
        action.getGenomicSource().setPlatformName("");
        assertEquals(Action.INPUT, action.save());
        action.getGenomicSource().setPlatformName("Name");
        assertEquals(Action.SUCCESS, action.save());
    }

    /**
     * Tests the save action when a connection error occurs.
     */
    @Test
    public void saveConnectionError() throws Exception {
        doThrow(new ConnectionException("Connection Error")).when(caArrayFacade)
            .validateGenomicSourceConnection(any(GenomicDataSourceConfiguration.class));
        assertEquals(Action.INPUT, action.save());
    }

    /**
     * Tests the save action when an experiment not found error occurs.
     */
    @Test
    public void saveExperiementNotFoundError() throws Exception {
        doThrow(new ExperimentNotFoundException("Experiement Not Found")).when(caArrayFacade)
            .validateGenomicSourceConnection(any(GenomicDataSourceConfiguration.class));
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
        public <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.setStudyConfiguration(new StudyConfiguration());
            super.getRefreshedEntity(entity);
            return (T) genomicSource;
        }
    }
}
