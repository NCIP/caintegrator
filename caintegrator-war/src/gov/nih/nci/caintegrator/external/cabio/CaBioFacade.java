/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.cabio;

import gov.nih.nci.caintegrator.external.ConnectionException;

import java.util.List;

/**
 * Facade to retrieve data from CaBio.
 */
public interface CaBioFacade {
    
    /**
     * Retrieves genes from caBio given input parameters.
     * @param searchParams to search genes on.
     * @return list of displayable genes.
     * @throws ConnectionException if unable to connect to caBio.
     */
    List<CaBioDisplayableGene> retrieveGenes(CaBioSearchParameters searchParams) throws ConnectionException;
    
    /**
     * Retrieves genes from caBio given pathways.
     * @param searchParams to search genes on.
     * @return list of displayable genes.
     * @throws ConnectionException if unable to connect to caBio.
     */
    List<CaBioDisplayableGene> retrieveGenesFromPathways(CaBioSearchParameters searchParams) 
    throws ConnectionException;
    
    /**
     * Retrievs pathways from caBio given input parameters.
     * @param searchParams to search pathways on.
     * @return list of displayable pathways.
     * @throws ConnectionException if unable to connect to caBio.
     */
    List<CaBioDisplayablePathway> retrievePathways(CaBioSearchParameters searchParams) throws ConnectionException;

    /**
     * Retrieves all taxons from caBio.
     * @return list of taxons.
     * @throws ConnectionException if unable to connect to caBio.
     */
    List<String> retrieveAllTaxons() throws ConnectionException;
}
