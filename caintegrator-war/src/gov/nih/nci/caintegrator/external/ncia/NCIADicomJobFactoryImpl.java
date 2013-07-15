/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import gov.nih.nci.caintegrator.file.FileManager;

import org.springframework.stereotype.Component;

/**
 * Creates an NCIADicomJobRunner given the FileManager and Job.
 */
@Component
public class NCIADicomJobFactoryImpl implements NCIADicomJobFactory {

    /**
     * {@inheritDoc}
     */
    public NCIADicomJobRunner createNCIADicomJobRunner(FileManager fileManager, NCIADicomJob job) {
        return new NCIADicomJobRunnerImpl(fileManager, job);
    }

}
