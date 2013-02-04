/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */
public class CaBioFacadeStub implements CaBioFacade {

    public boolean retrieveGenesCalled;
    public boolean retrieveGenesFromGeneAliasCalled;
    public boolean isConnectionException;
    public boolean retrieveAllTaxonsCalled;
    public boolean isReturnResults;
    public boolean retrievePathwaysCalled;
    public boolean retrieveGenesFromPathwaysCalled;
    
    public void clear() {
        retrieveGenesCalled = false;
        retrieveGenesFromGeneAliasCalled = false;
        isConnectionException = false;
        retrieveAllTaxonsCalled = false;
        isReturnResults = false;
        retrievePathwaysCalled = false;
        retrieveGenesFromPathwaysCalled = false;
    }
    
    public List<CaBioDisplayableGene> retrieveGenes(CaBioSearchParameters params) throws ConnectionException {
        retrieveGenesCalled = true;
        if (isConnectionException) {
            throw new ConnectionException("");
        }
        List<CaBioDisplayableGene> genes = new ArrayList<CaBioDisplayableGene>();
        if (isReturnResults) {
            genes.add(new CaBioDisplayableGene());
        }
        return genes;
    }
    
    public List<CaBioDisplayableGene> retrieveGenesFromGeneAlias(CaBioSearchParameters params)
    throws ConnectionException {
        retrieveGenesFromGeneAliasCalled = true;
        if (isConnectionException) {
            throw new ConnectionException("");
        }
        List<CaBioDisplayableGene> genes = new ArrayList<CaBioDisplayableGene>();
        if (isReturnResults) {
            genes.add(new CaBioDisplayableGene());
        }
        return genes;
    }

    public List<String> retrieveAllTaxons() throws ConnectionException {
        retrieveAllTaxonsCalled = true;
        return Collections.emptyList();
    }
    
    public List<CaBioDisplayablePathway> retrievePathways(CaBioSearchParameters searchParams)
            throws ConnectionException {
        retrievePathwaysCalled = true;
        List<CaBioDisplayablePathway> pathways = new ArrayList<CaBioDisplayablePathway>();
        CaBioDisplayablePathway pathway = new CaBioDisplayablePathway();
        if (isReturnResults) {
            pathways.add(pathway);
        }
        return pathways;
    }

    public List<CaBioDisplayableGene> retrieveGenesFromPathways(CaBioSearchParameters searchParams)
            throws ConnectionException {
        retrieveGenesFromPathwaysCalled = true;
        List<CaBioDisplayableGene> genes = new ArrayList<CaBioDisplayableGene>();
        if (isReturnResults) {
            genes.add(new CaBioDisplayableGene());
        }
        return genes;
    }
    
}
