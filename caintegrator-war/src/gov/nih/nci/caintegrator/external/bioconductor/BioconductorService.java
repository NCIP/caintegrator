/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.bioconductor;

import gov.nih.nci.caintegrator.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;


/**
 * Facade for access to Bioconductor grid services.
 */
public interface BioconductorService {
    
    /**
     * Retrieves segmentation data from Bioconductor's caDNAcopy or caCGHcall service
     * and associates it (via <code>SegmentData</code> objects) to the array datas passed in.
     * 
     * @param dnaAnalysisData retrieve segmentation data 
     * @param configuration include service to connect to.
     * @throws ConnectionException if the service couldn't be reached.
     * @throws DataRetrievalException if the job didn't complete.
     */
    void addSegmentationData(DnaAnalysisData dnaAnalysisData,
            DnaAnalysisDataConfiguration configuration)
    throws ConnectionException, DataRetrievalException;    
    

}
