/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.common;

import gov.nih.nci.caintegrator.external.ConnectionException;

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
 * Utility class used to retrieve data from caGrid objects.  This class cannot be tested using unit tests.
 */
public final class CaGridUtil {

    private static final Integer BUFFER_SIZE = 4096;
    
    private CaGridUtil() { }
    /**
     * Stores a file from an TransferServiceContextReference.
     * @param filename file to store.
     * @param tscr transfer reference to retrieve file from.
     * @return File that was created.
     * @throws RemoteException unable to connect.
     * @throws MalformedURIException malformed URI.
     * @throws ConnectionException unable to connect.
     */
    public static File retrieveFileFromTscr(String filename, TransferServiceContextReference tscr)
    throws ConnectionException, MalformedURIException, RemoteException {
        TransferServiceContextClient tclient = new TransferServiceContextClient(tscr.getEndpointReference());
        try {
            InputStream stream = (InputStream) TransferClientHelper.getData(tclient.getDataTransferDescriptor());
            return storeFileFromInputStream(stream, filename);
        } catch (Exception e) {
            throw new ConnectionException("Unable to download stream data from server.", e);
        } finally {
            tclient.destroy();
        }
    }

    private static File storeFileFromInputStream(InputStream istream, String filename) throws IOException {
        File file = new File(filename);
        OutputStream out = new FileOutputStream(file);
        byte [] buf = new byte[BUFFER_SIZE];
        int len;
        while ((len = istream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
        out.close();
        istream.close();
        return file;
    }
}
