/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;

/**
 * 
 */
public class NCIADicomJobFactoryStub implements NCIADicomJobFactory {

    public NCIADicomJobRunnerStub nciaDicomJobRunnerStub; 
    
    public NCIADicomJobRunner createNCIADicomJobRunner(FileManager fileManager, NCIADicomJob job) {
        nciaDicomJobRunnerStub = new NCIADicomJobRunnerStub();
        return nciaDicomJobRunnerStub;
    }
    
    public static class NCIADicomJobRunnerStub implements NCIADicomJobRunner {
        public boolean retrieveDicomFilesCalled = false;
        
        public File retrieveDicomFiles() throws ConnectionException {
            retrieveDicomFilesCalled = true;
            return new File(".");
        }
        
    }

}
