/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

/**
 * Creates instances of dna analysis handlers.
 */
public interface DnaAnalysisHandlerFactory {

    /**
     * Creates a handler instance.
     * 
     * @param genomicSource the genomic data source
     * @param caArrayFacade the interface to caArray
     * @param arrayDataService the array data storage service
     * @param dao the data access interface
     * @return the handler.
     * @exception DataRetrievalException for invalid platform vendor.
     */
    AbstractUnparsedSupplementalMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource, 
            CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, 
            CaIntegrator2Dao dao) throws DataRetrievalException;

}
