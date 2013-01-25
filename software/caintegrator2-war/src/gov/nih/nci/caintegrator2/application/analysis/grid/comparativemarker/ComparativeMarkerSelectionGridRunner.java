/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker;

import gov.nih.nci.caintegrator2.common.CaGridUtil;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
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
import org.genepattern.cagrid.service.compmarker.mage.common.ComparativeMarkerSelMAGESvcI;
import org.genepattern.cagrid.service.compmarker.mage.context.client.ComparativeMarkerSelMAGESvcContextClient;
import org.genepattern.cagrid.service.compmarker.mage.context.stubs.types.AnalysisNotComplete;
import org.genepattern.cagrid.service.compmarker.mage.context.stubs.types.CannotLocateResource;

/**
 * Runs the GenePattern grid service Comparative Marker Selection.
 */
public class ComparativeMarkerSelectionGridRunner {
    private static final Logger LOGGER = Logger.getLogger(ComparativeMarkerSelectionGridRunner.class);
    private static final int DOWNLOAD_REFRESH_INTERVAL = 3000; // Every 3 seconds
    private static final int TIMEOUT_SECONDS = 300; // 900 seconds = 15 minutes
    private final ComparativeMarkerSelMAGESvcI client;
    private final FileManager fileManager;

    /**
     * Public Constructor.
     * @param client of grid service.
     * @param fileManager to store results zip file.
     */
    public ComparativeMarkerSelectionGridRunner(ComparativeMarkerSelMAGESvcI client, FileManager fileManager) {
        this.client = client;
        this.fileManager = fileManager;

    }


    /**
     * Runs comparative marker selection baseed on input parameters, and the gct and cls files.
     * @param subscription study to save downloaded file to.
     * @param parameters to run comparative marker selection.
     * @param gctFile gene pattern file containing genomic data.
     * @param clsFile gene pattern file containing sample classifications.
     * @return the results from Comparative Marker Selection.
     * @throws ConnectionException if unable to connect to grid service.
     * @throws InterruptedException if execution is interrupted.
     * @throws IOException if problem writing to filesystem.
     */
    public File execute(StudySubscription subscription, ComparativeMarkerSelectionParameters parameters,
            File gctFile, File clsFile) throws ConnectionException, InterruptedException, IOException {
        ComparativeMarkerSelMAGESvcContextClient analysis = client.createAnalysis();
        postUpload(analysis, parameters, gctFile, clsFile);
        return downloadResult(subscription, analysis);
    }

    private void postUpload(ComparativeMarkerSelMAGESvcContextClient analysisClient,
            ComparativeMarkerSelectionParameters parameters, File gctFile, File clsFile)
            throws IOException, ConnectionException {
        TransferServiceContextReference up = analysisClient.submitData(parameters.createParameterList());
        TransferServiceContextClient tClient = new TransferServiceContextClient(up.getEndpointReference());
        BufferedInputStream bis = null;
        Set<File> fileSet = new HashSet<File>();
        fileSet.add(clsFile);
        fileSet.add(gctFile);
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
            FileUtils.deleteQuietly(zipFile);
        }
        tClient.setStatus(Status.Staged);
    }

    private File downloadResult(StudySubscription studySubscription,
            ComparativeMarkerSelMAGESvcContextClient analysisClient)
            throws ConnectionException, InterruptedException, MalformedURIException, RemoteException {
        String filename = new File(fileManager.getUserDirectory(studySubscription) + File.separator + "CMS_RESULTS_"
                + System.currentTimeMillis() + ".zip").getAbsolutePath();
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
                LOGGER.info("CMS - Attempt # " + callCount + " to host: " + hostInfo
                        + "- Analysis not complete");
                checkTimeout(callCount);
            } catch (CannotLocateResource e) {
                LOGGER.info("CMS - Attempt # " + callCount + " to host: " + hostInfo
                        + "- Cannot locate resource");
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
            throw new ConnectionException("Timed out trying to download Comparative Marker Selection results");
        }
    }

}
