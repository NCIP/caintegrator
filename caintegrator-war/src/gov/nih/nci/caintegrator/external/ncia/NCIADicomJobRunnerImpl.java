/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.file.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;

/**
 * Class to deal with retrieving and temporarily storing DICOM files from NCIA through the grid.
 */
public class NCIADicomJobRunnerImpl implements NCIADicomJobRunner {
    
    private static final Integer BUFFER_SIZE = 8192;
    private static final String REMOTE_CONNECTION_FAILED = "Remote Connection Failed.";
    private static final String DICOM_ZIP_FILE_NAME = "nciaDicomFiles.zip";
    private static final String DICOM_JOB_STRING = "DICOM_JOB_";
    private static int dicomJobCounter = 0;
    private final File temporaryStorageDirectory;
    private final NCIADicomJob job;
    
    /**
     * Public Constructor.
     * @param fileManager determines where to place the temporary storage directory.
     * @param job task that needs to run.
     */
    public NCIADicomJobRunnerImpl(FileManager fileManager, NCIADicomJob job) {
        job.setJobId(getNextJobId());
        temporaryStorageDirectory = fileManager.getNewTemporaryDirectory(job.getJobId());
        this.job = job;
    }
    
    /**
     * {@inheritDoc}
     */
    public File retrieveDicomFiles() throws ConnectionException {
        TransferServiceContextReference tscr = null;
        try {
            NCIACoreServiceClient client = new NCIACoreServiceClient(job.getServerConnection().getUrl());
            switch (job.getImageAggregationType()) {
            case IMAGESERIES:
                tscr = client.retrieveDicomDataBySeriesUIDs(job.getImageSeriesIDs().
                                                      toArray(new String[job.getImageSeriesIDs().size()]));
                break;
            case IMAGESTUDY:
                tscr = client.retrieveDicomDataByStudyUIDs(job.getImageStudyIDs().
                                                      toArray(new String[job.getImageStudyIDs().size()]));
                break;
            default:
                return null;
            }
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } catch (RemoteException e) {
            throw new ConnectionException(REMOTE_CONNECTION_FAILED, e);
        } 
        File file = gridTransferDicomData(tscr);
        job.setCompleted(true);
        return file;
    }
    
    private String getNextJobId() {
        return DICOM_JOB_STRING + ++dicomJobCounter;
    }

    private File gridTransferDicomData(TransferServiceContextReference tscr) 
        throws ConnectionException {
        File file = null;
        try {
            TransferServiceContextClient tclient = new TransferServiceContextClient(tscr.getEndpointReference());
            InputStream istream = TransferClientHelper.getData(tclient.getDataTransferDescriptor());
            file = storeDicomFiles(istream);
            tclient.destroy();
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } catch (RemoteException e) {
            throw new ConnectionException(REMOTE_CONNECTION_FAILED, e);
        } catch (Exception e) {
            throw new ConnectionException("Unable to get dicom data from Transfer Client.", e);
        }
        return file;
    }
    
    private File storeDicomFiles(InputStream istream) throws IOException {
        File dicomFile = new File(temporaryStorageDirectory, DICOM_ZIP_FILE_NAME);
        OutputStream out = new FileOutputStream(dicomFile);
        byte [] buf = new byte[BUFFER_SIZE];
        int len;
        while ((len = istream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
        out.close();
        istream.close();
        return dicomFile;
    }
}
