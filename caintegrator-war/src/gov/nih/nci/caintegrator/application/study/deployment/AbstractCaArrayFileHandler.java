/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * Provides base handling to retrieve data files from caArray.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public abstract class AbstractCaArrayFileHandler {

    private final CaArrayFacade caArrayFacade;
    private final ArrayDataService arrayDataService;
    private final GenomicDataSourceConfiguration genomicSource;
    private final CentralTendencyCalculator centralTendencyCalculator;

    AbstractCaArrayFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService) {
        this.caArrayFacade = caArrayFacade;
        this.arrayDataService = arrayDataService;
        this.genomicSource = genomicSource;
        this.centralTendencyCalculator = new CentralTendencyCalculator(
                genomicSource.getTechnicalReplicatesCentralTendency(),
                genomicSource.isUseHighVarianceCalculation(),
                genomicSource.getHighVarianceThreshold(),
                genomicSource.getHighVarianceCalculationType());
    }

    /**
     * Clean up.
     * @param dataFile the data file to delete.
     */
    protected void doneWithFile(File dataFile) {
        dataFile.delete();
    }

    abstract String getFileType();

    File getDataFile(String dataFilename) throws ConnectionException, DataRetrievalException, ValidationException {
        try {
            byte[] fileBytes = getCaArrayFacade().retrieveFile(genomicSource, dataFilename);
            File tempFile = File.createTempFile("temp", "." + getFileType());
            IOUtils.write(fileBytes, new FileOutputStream(tempFile));
            return tempFile;
        } catch (FileNotFoundException e) {
            throw new ValidationException("Experiment " + genomicSource.getExperimentIdentifier()
                    + " doesn't contain a file named " + dataFilename, e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't write '" + getFileType() + "' file locally", e);
        }
    }

    CaArrayFacade getCaArrayFacade() {
        return caArrayFacade;
    }

    ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @return the centralTendencyCalculator
     */
    protected CentralTendencyCalculator getCentralTendencyCalculator() {
        return centralTendencyCalculator;
    }
}
