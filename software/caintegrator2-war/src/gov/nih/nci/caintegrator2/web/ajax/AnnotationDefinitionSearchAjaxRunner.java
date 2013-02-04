/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

import java.util.List;

/**
 * This is what calls the NCIAFacade to retrieve the DICOM files through the grid, it is 
 * thread based, so it is asynchronous and started via the DicomRetrievalAjaxUpdater.
 */
public class AnnotationDefinitionSearchAjaxRunner implements Runnable {

    private final DataElementSearchAjaxUpdater updater;
    
    AnnotationDefinitionSearchAjaxRunner(DataElementSearchAjaxUpdater updater) {
        this.updater = updater;
     }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        boolean timedOut = false;
        try {
            updater.increaseRunningThreadCount();
            updater.setAnnotationDefinitionInProgress();
            List<AnnotationDefinition> definitions = updater.getStudyManagementService().
                                            getMatchingDefinitions(updater.getKeywordsList());
            Thread.sleep(1); // See if it's been interrupted (from a timeout).
            updater.updateAnnotationDefinitionTable(definitions);
        } catch (InterruptedException e) {
            timedOut = true;
        } finally {
            if (!timedOut) {
                updater.decreaseRunningThreadCount();
            }
        }
    }

}
