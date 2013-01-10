/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.file.FileManager;

/**
 * Interface for the factory that creates NCIADicomJobRunner objects.
 */
public interface NCIADicomJobFactory {
    
    /**
     * Creates a <code>NCIADicomJobRunner</code> instance based on the given job.
     * @param fileManager to store dicom files temporarily.
     * @param job task to run.
     * @return created runner object.
     */
    NCIADicomJobRunner createNCIADicomJobRunner(FileManager fileManager, NCIADicomJob job);


}
