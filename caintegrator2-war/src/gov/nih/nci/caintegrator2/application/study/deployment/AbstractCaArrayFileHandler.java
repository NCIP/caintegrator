/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Provides base handling to retrieve copy number data based on a copy number mapping file.
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
    
    File getDataFile(String dataFilename) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        try {
            byte[] fileBytes = getCaArrayFacade().retrieveFile(genomicSource, dataFilename);
            File tempFile = File.createTempFile("temp", "." + getFileType());
            Cai2Util.byteArrayToFile(fileBytes, tempFile);
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
