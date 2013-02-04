/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;

/**
 * Loads copy number data from locally-available CNCHP files (as opposed to retrieval from caArray) 
 * for quicker testing.
 */
class LocalCopyNumberMappingFileHandler extends AffymetrixCopyNumberMappingFileHandler {

    LocalCopyNumberMappingFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    protected void doneWithFile(File cnchpFile) {
        // no-op: don't delete
    }

    @Override
    File getDataFile(String copyNumberFilename) throws ConnectionException, DataRetrievalException,
            ValidationException {
        return TestDataFiles.getAffymetrixDataFile(copyNumberFilename);
    }
    
}
