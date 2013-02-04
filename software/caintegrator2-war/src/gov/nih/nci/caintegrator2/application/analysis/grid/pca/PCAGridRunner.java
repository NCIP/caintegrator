/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.pca;

import gov.nih.nci.caintegrator2.common.CaGridUtil;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.Status;
import org.genepattern.pca.common.PCAI;
import org.genepattern.pca.context.client.PCAContextClient;
import org.genepattern.pca.context.stubs.types.AnalysisNotComplete;
import org.genepattern.pca.context.stubs.types.CannotLocateResource;

/**
 * Runs the GenePattern grid service PCA.
 */
public class PCAGridRunner {

    private static final Logger LOGGER = Logger.getLogger(PCAGridRunner.class);
    private static final int DOWNLOAD_REFRESH_INTERVAL = 1000;
    private static final int TIMEOUT_SECONDS = 300;
    private final PCAI client;
    private final FileManager fileManager;
    
    /**
     * Constructor.
     * @param client of the grid service.
     * @param fileManager to store results zip file.
     */
    public PCAGridRunner(PCAI client, FileManager fileManager) {
        this.client = client;
        this.fileManager = fileManager;
    }

    
    /**
     * Runs PCA baseed on input parameters, and the gct file.
     * @param studySubscription for current study.
     * @param parameters to run PCA.
     * @param gctFile gene pattern file containing genomic data.
     * @return the zipped file results from PCA (should contain 3 .odf files).
     * @throws ConnectionException if unable to connect to grid service.
     * @throws InterruptedException if thread is interrupted while waiting for file download.
     */
    public File execute(StudySubscription studySubscription, PCAParameters parameters, File gctFile) 
        throws ConnectionException, InterruptedException {
        try {
            PCAContextClient analysisClient = client.createAnalysis();
            postUpload(analysisClient, parameters, gctFile);
            return downloadResult(studySubscription, analysisClient);
        } catch (RemoteException e) {
            throw new ConnectionException("Remote Connection Failed.", e);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't read gct file at the path " + gctFile.getAbsolutePath(), e);
        }
    }
    
    private void postUpload(PCAContextClient analysis, PCAParameters parameters, File gctFile) 
        throws ConnectionException, IOException {
        TransferServiceContextReference up = analysis.submitData(parameters.createParameterList());
        TransferServiceContextClient tClient = new TransferServiceContextClient(up.getEndpointReference());
        BufferedInputStream bis = null;
        try {
            long size = gctFile.length();
            bis = new BufferedInputStream(new FileInputStream(gctFile));
            TransferClientHelper.putData(bis, size, tClient.getDataTransferDescriptor());
        } catch (Exception e) {
            // For some reason TransferClientHelper throws "Exception", going to rethrow a connection exception.
            throw new ConnectionException("Unable to transfer gct data to the server.", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
        tClient.setStatus(Status.Staged);
    }
    
    private File downloadResult(StudySubscription studySubscription, PCAContextClient analysisClient) 
        throws ConnectionException, MalformedURIException, RemoteException, InterruptedException {
        String filename = fileManager.createNewStudySubscriptionFile(studySubscription,
                "PCA_RESULTS_" + System.currentTimeMillis() + ".zip").getAbsolutePath(); 
        TransferServiceContextReference tscr = null;
        int callCount = 0;
        while (tscr == null) {
            try {
                callCount++;
                tscr = analysisClient.getResult();
            } catch (AnalysisNotComplete e) {
                LOGGER.info("PCA - " + callCount + " - Analysis not complete");
                checkTimeout(callCount);
            } catch (CannotLocateResource e) {
                LOGGER.info("PCA - " + callCount + " - Cannot locate resource");
                checkTimeout(callCount);
            } catch (RemoteException e) {
                throw new ConnectionException("Unable to connect to server to download result.", e);
            }
            Thread.sleep(DOWNLOAD_REFRESH_INTERVAL);
        }
        return CaGridUtil.retrieveFileFromTscr(filename, tscr);
    }

    private void checkTimeout(int callCount) throws ConnectionException {
        if (callCount >= TIMEOUT_SECONDS) {
            throw new ConnectionException("Timed out trying to download PCA results");
        }
    }
    
}
