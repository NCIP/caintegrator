/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.io.File;

/**
 * Interface for running NCIA Dicom Jobs.
 */
public interface NCIADicomJobRunner {
    
    /**
     * Retrieves dicom files for the job.
     * 
     * NOTE: right now it assumes the job contains only 1 Series Instance UID, later
     * we can expand it to have lists of the 3 different types of UID's.
     * @return Zipped file.
     * @throws ConnectionException if we are unable to connect.
     */
    File retrieveDicomFiles() throws ConnectionException;

}
