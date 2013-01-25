/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class DicomRetrievalAjaxUpdaterTest extends AbstractSessionBasedTest {

    private DicomRetrievalAjaxUpdater updater;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new DicomRetrievalAjaxUpdater();
        updater.setNciaFacade(nciaFacade);
        NCIADicomJob dicomJob = new NCIADicomJob();
        dicomJob.setCompleted(false);
        dicomJob.setCurrentlyRunning(true);
        dicomJob.getImageSeriesIDs().add("123");
        dicomJob.getImageStudyIDs().add("345");
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(dicomJob);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
    }

    @Test
    public void testRunDicomJob() throws Exception {
        updater.runDicomJob();
        // Testing asynchronously requires sleep for a second.
        while (updater.getDicomJob().isCurrentlyRunning()) {
            Thread.sleep(1000);
        }
        verify(nciaFacade, times(1)).retrieveDicomFiles(any(NCIADicomJob.class));
        assertFalse(updater.getDicomJob().isCurrentlyRunning());
        SessionHelper.getInstance().getDisplayableUserWorkspace().setDicomJob(null);
        updater.runDicomJob();
        verify(nciaFacade, times(1)).retrieveDicomFiles(any(NCIADicomJob.class));
    }
}
