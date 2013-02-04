/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NCIAFacadeStub implements NCIAFacade {
    
    public boolean retrieveDicomFilesCalled;
    
    public void clear() {
        retrieveDicomFilesCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAllCollectionNameProjects(ServerConnectionProfile profile) throws ConnectionException {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    public List<ImageSeriesAcquisition> getImageSeriesAcquisitions(String collectionNameProject,
            ServerConnectionProfile profile) throws ConnectionException {
        List<ImageSeriesAcquisition> acquisitions = new ArrayList<ImageSeriesAcquisition>();
        acquisitions.add(new ImageSeriesAcquisition());
        return acquisitions;
    }

    public File retrieveDicomFiles(NCIADicomJob job) throws ConnectionException {
        NCIADicomJob dicomJob = new NCIADicomJob();
        dicomJob.setCompleted(true);
        dicomJob.setJobId("fakeJob");
        retrieveDicomFilesCalled = true;
        return TestDataFiles.VALID_FILE;
    }


}
