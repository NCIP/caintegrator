/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.cadsr.freestylesearch.util.SearchException;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;

import java.util.List;

/**
 * This is called to asynchronously run a caDSR search and update the results.
 */
public class CaDsrSearchAjaxRunner implements Runnable {

    private final DataElementSearchAjaxUpdater updater;
    
    CaDsrSearchAjaxRunner(DataElementSearchAjaxUpdater updater) {
        this.updater = updater;
     }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        boolean timedOut = false;
        try {
            updater.setCaDsrInProgress();
            updater.increaseRunningThreadCount();
            List<CommonDataElement> dataElements = updater.getStudyManagementService().
                                        getMatchingDataElements(updater.getKeywordsList());
            Thread.sleep(1); // See if it's been interrupted (from a timeout).
            updater.updateCaDsrTable(dataElements);
            
        } catch (SearchException e) {
            updater.setCaDsrError("caDSR provider currently unavailable!");
        } catch (InterruptedException e) {
            timedOut = true;
        } finally {
            if (!timedOut) {
                updater.decreaseRunningThreadCount();
            }
        }
    }
}
