/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAFacadeStub;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;


public class DicomRetrievalAjaxUpdaterTest {

    private DicomRetrievalAjaxUpdater updater;
    private NCIAFacadeStub nciaFacade;
    
    @Before
    public void setUp() throws Exception {
        updater = new DicomRetrievalAjaxUpdater();
        nciaFacade = new NCIAFacadeStub();
        updater.setNciaFacade(nciaFacade);
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        NCIADicomJob dicomJob = new NCIADicomJob();
        dicomJob.setCompleted(false);
        dicomJob.setCurrentlyRunning(true);
        dicomJob.getImageSeriesIDs().add("123");
        dicomJob.getImageStudyIDs().add("345");
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
        WebContextFactory.setWebContextBuilder(new WebContextBuilderStub());
    }

    @Test
    public void testRunDicomJob() throws InterruptedException {
        updater.runDicomJob();
        // Testing asynchronously requires sleep for a second.
        while (updater.getDicomJob().isCurrentlyRunning()) {
            Thread.sleep(1000);
        }
        assertTrue(nciaFacade.retrieveDicomFilesCalled);
        assertFalse(updater.getDicomJob().isCurrentlyRunning());
        nciaFacade.clear();
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(null);
        updater.runDicomJob();
        assertFalse(nciaFacade.retrieveDicomFilesCalled);
    }

}
