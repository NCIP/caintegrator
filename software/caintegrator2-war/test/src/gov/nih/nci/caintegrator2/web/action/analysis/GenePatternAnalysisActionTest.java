/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.analysis.GenomicDataParameterValue;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.common.ConfigurationHelperStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.PersistedAnalysisJobAjaxUpdater;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GenePatternAnalysisActionTest extends AbstractSessionBasedTest {
    
    private GenePatternAnalysisAction action;
    private QueryManagementServiceStub queryManagementService;

    @Before
    public void setUp() {
        super.setUp();
        StudySubscription subscription = new StudySubscription();
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(Status.DEPLOYED);
        study.setStudyConfiguration(studyConfiguration);
        subscription.setStudy(study);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action = new GenePatternAnalysisAction();
        action.setAnalysisService(new AnalysisServiceStub());
        queryManagementService = new QueryManagementServiceStub();
        action.setQueryManagementService(queryManagementService);
        action.setWorkspaceService(new WorkspaceServiceStub());
        action.setAjaxUpdater(new PersistedAnalysisJobAjaxUpdater());
        action.setConfigurationHelper(new ConfigurationHelperStub());
    }
    
    @Test
    public void testPrepare() {
        queryManagementService.platformsForStudy.add("platform1");
        action.prepare();
        assertFalse(action.getGenePatternAnalysisForm().isMultiplePlatformsInStudy());
        queryManagementService.platformsForStudy.add("platform2");
        action.prepare();
        assertTrue(action.getGenePatternAnalysisForm().isMultiplePlatformsInStudy());
    }
    
    @Test
    public void testOpen() {
        action.setSelectedAction(GenePatternAnalysisAction.OPEN_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());
    }
    
    @Test
    public void testConnect() {
        action.setSelectedAction(GenePatternAnalysisAction.CONNECT_ACTION);
        assertEquals(ActionSupport.SUCCESS, action.execute());
        assertTrue(action.getGenePatternAnalysisForm().getAnalysisMethods().isEmpty());
        action.getGenePatternAnalysisForm().setUrl("url");
        action.execute();
        assertNotNull(action.getGenePatternAnalysisForm().getAnalysisMethods());
        assertEquals(1, action.getGenePatternAnalysisForm().getAnalysisMethods().size());
    }

    @Test
    public void testSetAnalysisMethodName() {
        action.setSelectedAction(GenePatternAnalysisAction.CONNECT_ACTION);
        assertNull(action.getGenePatternAnalysisForm().getInvocation());
        action.getGenePatternAnalysisForm().setUrl("url");
        assertEquals("url", action.getGenePatternAnalysisForm().getUrl());
        action.execute();
        action.setSelectedAction(GenePatternAnalysisAction.CHANGE_METHOD_ACTION);
        assertEquals(1, action.getGenePatternAnalysisForm().getAnalysisMethodNames().size());
        action.setAnalysisMethodName("method");
        action.execute();
        assertNotNull(action.getGenePatternAnalysisForm().getInvocation());
        assertEquals("method", action.getGenePatternAnalysisForm().getAnalysisMethodName());
        assertEquals(1, action.getGenePatternAnalysisForm().getParameters().size());
        AbstractAnalysisFormParameter parameter = action.getGenePatternAnalysisForm().getParameters().get(0);
        assertEquals("parameter", parameter.getName());
        assertTrue(parameter.isRequired());
        assertTrue(action.getGenePatternAnalysisForm().isExecutable());
        action.setAnalysisMethodName("");
        action.execute();
        assertNull(action.getGenePatternAnalysisForm().getInvocation());
        assertFalse(action.getGenePatternAnalysisForm().isExecutable());
    }
    
    @Test
    public void testExecute() {
        action.setSelectedAction(GenePatternAnalysisAction.CONNECT_ACTION);
        action.getGenePatternAnalysisForm().setUrl("url");
        action.execute();
        action.setSelectedAction(GenePatternAnalysisAction.CHANGE_METHOD_ACTION);
        action.setAnalysisMethodName("method");
        action.execute();
        GenomicDataParameterValue genomicParameterValue = new GenomicDataParameterValue();
        AnalysisParameter genomicParameter = new AnalysisParameter();
        genomicParameter.setType(AnalysisParameterType.GENOMIC_DATA);
        genomicParameterValue.setParameter(genomicParameter);
        action.getGenePatternAnalysisForm().getInvocation().setParameterValue(genomicParameter, genomicParameterValue);
        action.setSelectedAction(GenePatternAnalysisAction.EXECUTE_ACTION);
        assertEquals("status", action.execute());
        
        action.setSelectedAction("INVALID");
        assertEquals(Action.INPUT, action.execute());
        
    }
    
    @Test
    public void testValidate() {
        action.setSelectedAction(GenePatternAnalysisAction.CONNECT_ACTION);
        action.getGenePatternAnalysisForm().setUrl("");
        action.getGenePatternAnalysisForm().setUsername("username");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getGenePatternAnalysisForm().setUrl("bad url");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getGenePatternAnalysisForm().setUrl("http://localhost/directory");
        action.getCurrentGenePatternAnalysisJob().setName("name");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        action.validate();
        assertFalse(action.hasErrors());
        action.clearErrorsAndMessages();
        action.execute();
        action.setSelectedAction(GenePatternAnalysisAction.CHANGE_METHOD_ACTION);
        action.setAnalysisMethodName("method");
        action.execute();
        action.setSelectedAction(GenePatternAnalysisAction.EXECUTE_ACTION);
        action.getGenePatternAnalysisForm().getParameters().get(0).setValue("");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getGenePatternAnalysisForm().getParameters().get(0).setValue("value");
        action.validate();
        assertFalse(action.hasErrors());
        action.getGenePatternAnalysisForm().getParameters().get(0).getParameterValue().getParameter().setType(AnalysisParameterType.INTEGER);
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getGenePatternAnalysisForm().getParameters().get(0).getParameterValue().getParameter().setType(AnalysisParameterType.FLOAT);
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.getCurrentStudy().getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
        action.clearErrorsAndMessages();
        ActionContext.getContext().getValueStack().setValue("studySubscription", null);
        action.validate();
        assertTrue(action.getActionErrors().size() == 1);
    }
    
    @Test
    public void testDeleteAnalysisJob() {
        assertEquals(ActionSupport.SUCCESS, action.deleteAnalysisJob());
    }
    
    @Test
    public void testGetAnalysisTypes() {
        assertEquals(2, action.getAnalysisTypes().size());
        GenomicDataSourceConfiguration gdsc = new GenomicDataSourceConfiguration();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(gdsc);
        gdsc.setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
        assertEquals(3, action.getAnalysisTypes().size());
        gdsc.setDataType(GenomicDataSourceDataTypeEnum.COPY_NUMBER);
        gdsc.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        assertEquals(3, action.getAnalysisTypes().size());gdsc = new GenomicDataSourceConfiguration();
        action.getCurrentStudy().getStudyConfiguration().getGenomicDataSources().add(gdsc);
        gdsc.setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
        assertEquals(4, action.getAnalysisTypes().size());
    }

}
