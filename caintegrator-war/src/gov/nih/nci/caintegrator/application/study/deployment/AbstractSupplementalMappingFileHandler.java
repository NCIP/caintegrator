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
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Provides base handling to retrieve copy number data based on a copy number mapping file.
 */
public abstract class AbstractSupplementalMappingFileHandler extends AbstractCaArrayFileHandler {

    private final CaIntegrator2Dao dao;
    
    AbstractSupplementalMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService);
                this.dao = dao;
    }
    
    void loadMappingFile() throws DataRetrievalException, ValidationException {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(getMappingFile()));
            String[] fields;
            while ((fields = Cai2Util.readDataLine(reader)) != null) {
                processMappingData(fields);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Sample mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read sample mapping file: ", e);
        } catch (ConnectionException e) {
            throw new DataRetrievalException("Couldn't connect to caArray: ", e);
        }
        
    }

    private void processMappingData(String[] fields)
    throws FileNotFoundException, ValidationException, ConnectionException, DataRetrievalException {
        if (fields.length > 1 && !fields[0].startsWith("#")) {
            String subjectId = fields[0].trim();
            String sampleName = fields[1].trim();
            SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
            supplementalDataFile.setFileName(fields[2].trim());
            supplementalDataFile.setProbeNameHeader(fields[3].trim());
            supplementalDataFile.setValueHeader(fields[4].trim());
            supplementalDataFile.setSampleHeader(fields[5].trim());
            mappingSample(subjectId, sampleName, supplementalDataFile);
        }
    }
    
    abstract File getMappingFile() throws FileNotFoundException;
    
    abstract void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile) 
    throws FileNotFoundException, ValidationException, ConnectionException, DataRetrievalException;

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

}
