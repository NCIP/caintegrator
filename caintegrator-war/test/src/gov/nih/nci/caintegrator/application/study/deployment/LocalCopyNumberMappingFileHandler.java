/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.application.study.deployment.AffymetrixCopyNumberMappingFileHandler;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

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
