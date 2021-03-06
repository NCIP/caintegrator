/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import java.io.File;

import affymetrix.fusion.cdf.FusionCDFData;

/**
 * Provides common Affymetrix CDF file handling functionality.
 */
public final class AffymetrixCdfReader {

    private FusionCDFData cdfData;

    private AffymetrixCdfReader(File cdfFile) {
        if (cdfFile == null || !cdfFile.exists()) {
            throw new IllegalArgumentException("CDF file must exist.");
        }
        cdfData = new FusionCDFData();
        cdfData.setFileName(cdfFile.getAbsolutePath());
    }

    /**
     * Creates a new reader for the file given. Clients are expected to call
     * <code>close</code> to free resources when done.
     *
     * @param   cdfFile the CDF to open
     * @return  a reader for the CDF
     * @throws AffymetrixCdfReadException if the file couldn't be read
     */
    public static AffymetrixCdfReader create(File cdfFile) throws AffymetrixCdfReadException {
        AffymetrixCdfReader reader = new AffymetrixCdfReader(cdfFile);

        boolean opened = false;
        try {
            opened = reader.open();
        } catch (RuntimeException e) {
            // some CDF files are not supported by the AffxFusion jar and trying to read the files results in a
            // runtime exception being thrown - see GForge defect 11922 at
            // https://gforge.nci.nih.gov/tracker/?group_id=305&atid=1344&func=detail&aid=11922
            // we catch the exception to handle it more gracefully
            throw new AffymetrixCdfReadException("Unsupported file type", e);
        }
        if (!opened) {
            AffymetrixCdfReadException readException = new AffymetrixCdfReadException(reader.getCdfData().getError());
            reader.close();
            throw readException;
        }
        return reader;
    }

    private boolean open() {
        boolean success = cdfData.read();
        if (!success) {
            cdfData.clear();
            success = cdfData.read();
        }
        return success;
    }

    /**
     * Closes the reader.
     */
    public void close() {
        if (cdfData != null) {
            cdfData.clear();
            cdfData = null;
        }
    }

    /**
     * @return the cdfData
     */
    public FusionCDFData getCdfData() {
        return cdfData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

}
