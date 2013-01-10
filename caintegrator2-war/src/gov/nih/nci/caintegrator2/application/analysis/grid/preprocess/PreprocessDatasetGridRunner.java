/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.preprocess;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.caintegrator2.common.CaGridUtil;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.Status;
import org.genepattern.cagrid.service.preprocessdataset.mage.common.PreprocessDatasetMAGEServiceI;
import org.genepattern.cagrid.service.preprocessdataset.mage.context.client.PreprocessDatasetMAGEServiceContextClient;
import org.genepattern.cagrid.service.preprocessdataset.mage.context.stubs.types.AnalysisNotComplete;
import org.genepattern.cagrid.service.preprocessdataset.mage.context.stubs.types.CannotLocateResource;


/**
 * Runs the GenePattern grid service PreprocessDataset (MAGE).
 */
public class PreprocessDatasetGridRunner {
    private static final Logger LOGGER = Logger.getLogger(PreprocessDatasetGridRunner.class);
    private static final int DOWNLOAD_REFRESH_INTERVAL = 1000;
    private static final int TIMEOUT_SECONDS = 300;
    private final PreprocessDatasetMAGEServiceI client;
    
    /**
     * Public Constructor.
     * @param client of grid service.
     */
    public PreprocessDatasetGridRunner(PreprocessDatasetMAGEServiceI client) {
        this.client = client;
    }
    
    /**
     * Executes the grid service PreprocessDataset.
     * @param studySubscription for current study.
     * @param parameters for preprocess dataset.
     * @param gctFile the unprocessed gct file to run preprocess on.
     * @throws ConnectionException if unable to connect to grid service.
     * @throws InterruptedException if thread is interrupted while waiting for file download.
     */
    public void execute(StudySubscription studySubscription, PreprocessDatasetParameters parameters, 
            File gctFile) 
        throws ConnectionException, InterruptedException {
        // TODO figure out the "if" condition here, probably number of lines needs to be greater than something.
//        if (dataset.getValues().length > 0) {
            
//        }
        runPreprocessDataset(parameters, gctFile);
    }

    
    private File runPreprocessDataset(PreprocessDatasetParameters parameters, File unprocessedGctFile) 
    throws ConnectionException, InterruptedException {
        try {
            PreprocessDatasetMAGEServiceContextClient analysis = client.createAnalysis();
            postUpload(analysis, parameters, unprocessedGctFile);
            return downloadResult(unprocessedGctFile, analysis);
        } catch (RemoteException e) {
            throw new ConnectionException("Remote Connection Failed.", e);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read GCT file.");
        }
    }

    private void postUpload(PreprocessDatasetMAGEServiceContextClient analysis, PreprocessDatasetParameters parameters,
            File unprocessedGctFile) throws IOException, ConnectionException {
        TransferServiceContextReference up = analysis.submitData(parameters.createParameterList());
        TransferServiceContextClient tClient = new TransferServiceContextClient(up.getEndpointReference());
        BufferedInputStream bis = null;
        try {
            long size = unprocessedGctFile.length();
            bis = new BufferedInputStream(new FileInputStream(unprocessedGctFile));
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
    
    private File downloadResult(File gctFile, PreprocessDatasetMAGEServiceContextClient analysisClient) 
    throws ConnectionException, InterruptedException, IOException {
        TransferServiceContextReference tscr = null;
        String hostInfo = "N/A";
        int callCount = 0;
        hostInfo = analysisClient.getEndpointReference().getAddress().getHost()
             + ":"
             + analysisClient.getEndpointReference().getAddress().getPort()
             + analysisClient.getEndpointReference().getAddress().getPath();
        while (tscr == null) {
            try {
                callCount++;
                tscr = analysisClient.getResult();
            } catch (AnalysisNotComplete e) {
                LOGGER.info("Preprocess - Attempt # " + callCount + " to host:"
                        + hostInfo + " - Analysis not complete");
                checkTimeout(callCount);
            } catch (CannotLocateResource e) {
                LOGGER.info("Preprocess - Attempt # " + callCount + " to host:"
                        + hostInfo + " - Cannot locate resource");
                checkTimeout(callCount);
            } catch (RemoteException e) {
                throw new ConnectionException("Unable to connect to server to download result.", e);
            }
            Thread.sleep(DOWNLOAD_REFRESH_INTERVAL);
        }
        File zipFile = CaGridUtil.retrieveFileFromTscr(gctFile.getAbsolutePath() + ".zip", tscr); 
        return replaceGctFileWithPreprocessed(gctFile, zipFile);
    }

    private File replaceGctFileWithPreprocessed(File gctFile, File zipFile) throws IOException {
        File zipFileDirectory = new File(zipFile.getParent().concat("/tempPreprocessedZipDir"));
        FileUtils.deleteDirectory(zipFileDirectory);
        FileUtils.forceMkdir(zipFileDirectory);
        FileUtils.waitFor(zipFileDirectory, TIMEOUT_SECONDS);        
        Cai2Util.isValidZipFile(zipFile);
        ZipUtilities.unzip(zipFile, zipFileDirectory);
        FileUtils.waitFor(zipFileDirectory, TIMEOUT_SECONDS);
        Cai2Util.printDirContents(zipFileDirectory);
        if (zipFileDirectory.list() != null) {
            if (zipFileDirectory.list().length != 1) {
                int dirListlength = zipFileDirectory.list().length;
                FileUtils.deleteDirectory(zipFileDirectory);
                throw new IllegalStateException("The zip file returned from PreprocessDataset"
                      + " should have exactly 1 file instead of " + dirListlength);
            }
        } else {
            String zipFileDirectoryPath = zipFileDirectory.getAbsolutePath();
            FileUtils.deleteDirectory(zipFileDirectory);
            throw new IllegalStateException("The zip file directory list at path: "
                    + zipFileDirectoryPath + "is null.");               
        }
        String[] files = zipFileDirectory.list();
        File preprocessedFile = new File(zipFileDirectory, files[0]);
        FileUtils.deleteQuietly(gctFile); // Remove the non-preprocessed file
        FileUtils.moveFile(preprocessedFile, gctFile); // move to gctFile
        FileUtils.deleteQuietly(zipFile);
        FileUtils.deleteDirectory(zipFileDirectory);
        return gctFile;
    }
        
    private void checkTimeout(int callCount) throws ConnectionException {
        if (callCount >= TIMEOUT_SECONDS) {
            throw new ConnectionException("Timed out trying to download preprocess dataset results");
        }
    }
    
}
