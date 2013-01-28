/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

/**
 * Creates instances of copy number handlers.
 */
public interface ExpressionHandlerFactory {

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
    AbstractExpressionMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource, 
            CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, 
            CaIntegrator2Dao dao) throws DataRetrievalException;

}
