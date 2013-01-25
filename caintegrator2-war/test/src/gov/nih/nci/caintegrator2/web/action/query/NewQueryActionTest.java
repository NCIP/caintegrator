/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

/**
 *
 */
public class NewQueryActionTest extends AbstractSessionBasedTest {

     @Test
     public void testNewQueryAction() throws Exception {
         StudySubscription subscription = new StudySubscription();
         subscription.setId(Long.valueOf(1));
         Study study = new Study();
         StudyConfiguration studyConfiguration = new StudyConfiguration();
         studyConfiguration.setStatus(Status.DEPLOYED);
         study.setStudyConfiguration(studyConfiguration);
         subscription.setStudy(study);
         SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
         ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
         setStudySubscription(subscription);

         NewQueryAction action = new NewQueryAction();
         action.setWorkspaceService(workspaceService);

         action.prepare();
         action.validate();
         assertFalse(action.hasActionErrors());

         studyConfiguration.setStatus(Status.NOT_DEPLOYED);
         action.validate();
         assertTrue(action.hasActionErrors());
         action.clearErrorsAndMessages();


         ActionContext.getContext().getValueStack().setValue("studySubscription", null);
         action.validate();
         assertTrue(action.hasActionErrors());
     }

 }
