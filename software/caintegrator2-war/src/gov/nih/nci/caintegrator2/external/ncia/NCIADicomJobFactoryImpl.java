/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.file.FileManager;

/**
 * Creates an NCIADicomJobRunner given the FileManager and Job.
 */
public class NCIADicomJobFactoryImpl implements NCIADicomJobFactory {


    /**
     * {@inheritDoc}
     */
    public NCIADicomJobRunner createNCIADicomJobRunner(FileManager fileManager, NCIADicomJob job) {
        return new NCIADicomJobRunnerImpl(fileManager, job);
    }

}
