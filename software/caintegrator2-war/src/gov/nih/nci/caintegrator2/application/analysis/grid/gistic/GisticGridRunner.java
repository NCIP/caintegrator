/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.gistic;

import gov.nih.nci.caintegrator2.common.CaGridUtil;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.Status;
import org.genepattern.cabig.util.ZipUtils;
import org.genepattern.gistic.common.GisticI;
import org.genepattern.gistic.context.client.GisticContextClient;
import org.genepattern.gistic.context.stubs.types.AnalysisNotComplete;
import org.genepattern.gistic.context.stubs.types.CannotLocateResource;
import org.genepattern.gistic.stubs.types.InvalidParameterException;
import org.oasis.wsrf.faults.BaseFaultTypeDescription;

/**
 * Runs the GenePattern grid service Gistic.
 */
public class GisticGridRunner {
    
    private static final Logger LOGGER = Logger.getLogger(GisticGridRunner.class);
    private static final int DOWNLOAD_REFRESH_INTERVAL = 5000; // Every 5 seconds
    private static final int TIMEOUT_SECONDS = 7200; // For 3600 total Seconds (60 total minutes)
    private final GisticI client;
    private final FileManager fileManager;
    
    /**
     * Public Constructor.
     * @param client of grid service.
     * @param fileManager to store results zip file.
     */
    public GisticGridRunner(GisticI client, FileManager fileManager) {
        this.client = client;
        this.fileManager = fileManager;
    }

    
    /**
     * Runs GISTIC based on input parameters.
     * @param studySubscription study to save downloaded file to.
     * @param parameters from user input.
     * @param segmentFile for segmentation data.
     * @param markersFile for marker data.
     * @param cnvFile for cnv.
     * @return Gistic Results.
     * @throws ParameterException if have invalid parameter.
     * @throws ConnectionException if unable to connect.
     * @throws IOException if there's a problem saving files.
     * @throws InterruptedException if execution is interrupted.
     */
    public File execute(StudySubscription studySubscription, 
            GisticParameters parameters, File segmentFile, File markersFile, File cnvFile) 
    throws ParameterException, ConnectionException, IOException, InterruptedException {
        try {
            GisticContextClient analysisClient = client.createAnalysis(); 
            postUpload(analysisClient, parameters, segmentFile, markersFile, cnvFile);
            return downloadResult(studySubscription, analysisClient);
        } catch (InvalidParameterException e) {
            throw new ParameterException("The given parameters were invalid: " + getDescription(e), e);
        } catch (RemoteException e) {
            LOGGER.error("Couldn't complete GISTIC job", e);
            throw new ConnectionException("Unable to connect to server.", e);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } 
    }

    private void postUpload(GisticContextClient analysisClient, 
            GisticParameters parameters, File segmentFile, File markersFile, File cnvFile)
            throws IOException, ConnectionException {
        TransferServiceContextReference up = analysisClient.submitData(parameters.createParameterList(), 
                                                                       parameters.createGenomeBuild());
        TransferServiceContextClient tClient = new TransferServiceContextClient(up.getEndpointReference());
        BufferedInputStream bis = null;
        Set<File> fileSet = createGisticInputFileSet(segmentFile, markersFile, cnvFile);
        File zipFile = new File(ZipUtils.writeZipFile(fileSet));
        try {
            long size = zipFile.length();
            bis = new BufferedInputStream(new FileInputStream(zipFile));
            TransferClientHelper.putData(bis, size, tClient.getDataTransferDescriptor());
        } catch (Exception e) {
            // For some reason TransferClientHelper throws "Exception", going to rethrow a connection exception.
            throw new ConnectionException("Unable to transfer data to the server.", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            for (File file : fileSet) {
                FileUtils.deleteQuietly(file);
            }
            FileUtils.deleteQuietly(zipFile);
        }
        tClient.setStatus(Status.Staged);
    }

    private Set<File> createGisticInputFileSet(File segmentFile, File markersFile, File cnvFile)
            throws IOException {
        Set<File> fileSet = new HashSet<File>();
        fileSet.add(markersFile);
        fileSet.add(segmentFile);
        if (cnvFile != null) {
            fileSet.add(cnvFile);
        }
        return fileSet;
    }
    
    private File downloadResult(StudySubscription studySubscription, GisticContextClient analysisClient) 
        throws ConnectionException, InterruptedException, MalformedURIException, RemoteException {
        String filename = new File(fileManager.getUserDirectory(studySubscription) + File.separator
                + "GISTIC_RESULTS_" + System.currentTimeMillis() + ".zip").getAbsolutePath();
        TransferServiceContextReference tscr = null;
        int callCount = 0;
        String hostInfo = analysisClient.getEndpointReference().getAddress().getHost()
        + ":"
        + analysisClient.getEndpointReference().getAddress().getPort()
        + analysisClient.getEndpointReference().getAddress().getPath();
        while (tscr == null) {
            try {
                callCount++;
                tscr = analysisClient.getResult();
            } catch (AnalysisNotComplete e) {
                LOGGER.info("GISTIC - Attempt # " + callCount + " to host: " + hostInfo
                        + " - Analysis not complete");
                checkTimeout(callCount);
            } catch (CannotLocateResource e) {
                LOGGER.info("GISTIC - Attempt # " + callCount + " to host: " + hostInfo
                        + " - Cannot locate resource");
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
            throw new ConnectionException("Timed out trying to download GISTIC results");
        }
    }

    private String getDescription(InvalidParameterException e) {
        StringBuffer description = new StringBuffer();
        for (BaseFaultTypeDescription typeDescription : e.getDescription()) {
            if (description.length() > 0) {
                description.append(", ");
            }
            description.append(typeDescription.get_value());
        }
        return description.toString();
    }

}
