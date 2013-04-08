/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet;

import gov.nih.nci.caintegrator.external.biodbnet.search.GeneResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.PathwayResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.SearchParameters;

import java.util.Set;


/**
 * Interface interacting with bioDbNet various functionalities.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public interface BioDbNetService {

    /**
     * Retrieves a set the the gene ids that match a given gene symbols.
     * @param params the search parameters
     * @return the matching gene ids
     */
    Set<String> retrieveGeneIds(SearchParameters params);

    /**
     * Retrieves a set the the gene ids that match a given gene aliases.
     * @param params the search parameters
     * @return the matching gene ids
     */
    Set<String> retrieveGeneIdsByAlias(SearchParameters params);


    /**
     * Retrieves the set of genes matching the given ids and taxon.
     *
     * @param params the search parameters
     * @return the matching genes
     */
    Set<GeneResults> retrieveGenesById(SearchParameters params);

    /**
     * Retrieves the set of genes gene contained in the given pathways.
     * @param params the search parameters
     * @return the matching genes
     */
    Set<GeneResults> retrieveGenesByPathway(SearchParameters params);

    /**
     * Retrieves a set of pathways that contain the given gene symbols.
     * @param params the search parameters
     * @return the matching pathways
     */
    Set<PathwayResults> retrievePathwaysByGeneSymbols(SearchParameters params);

}
