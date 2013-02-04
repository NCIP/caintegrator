/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caarray.external.v1_0.experiment.Experiment;
import gov.nih.nci.caarray.external.v1_0.query.BiomaterialSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.ExperimentSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.SearchResult;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.BiomaterialType;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.DataTransferException;
import gov.nih.nci.caarray.services.external.v1_0.data.JavaDataApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.search.JavaSearchApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator.external.ConnectionException;

//import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains utility methods for working with the caArray API.
 */
final class CaArrayUtils {
    
    private CaArrayUtils() {
        super();
    }
    
    @SuppressWarnings("PMD.PreserveStackTrace")     // FileNotFoundException doesn't include a source Throwable
    static byte[] retrieveFile(DataService dataService, CaArrayEntityReference fileRef) 
    throws FileNotFoundException, ConnectionException {
        JavaDataApiUtils dataServiceHelper = new JavaDataApiUtils(dataService);
        try {
            return dataServiceHelper.getFileContents(fileRef, false);
        } catch (InvalidReferenceException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (DataTransferException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    static List<Biomaterial> getSamples(String experimentIdentifier, SearchService searchService) 
    throws ExperimentNotFoundException {
        return getSamples(getExperiment(experimentIdentifier, searchService), searchService);
    }


    static List<Biomaterial> getSamples(Experiment experiment, SearchService searchService) {
        BiomaterialSearchCriteria criteria = new BiomaterialSearchCriteria();
        criteria.setExperiment(experiment.getReference());
        Set<BiomaterialType> types = new HashSet<BiomaterialType>();
        types.add(BiomaterialType.SAMPLE);
        criteria.setTypes(types);
        try {
            return getSamples(searchService, criteria);
        } catch (InvalidInputException e) {
            throw new IllegalStateException("Couldn't load Biomaterials for valid experiment", e);
        }
    }
    
    private static List<Biomaterial> getSamples(SearchService searchService, BiomaterialSearchCriteria criteria)
            throws InvalidInputException {
        return new JavaSearchApiUtils(searchService).biomaterialsByCriteria(criteria).list();
    }

    static Experiment getExperiment(String experimentIdentifier, SearchService searchService) 
    throws ExperimentNotFoundException {
        ExperimentSearchCriteria criteria = new ExperimentSearchCriteria();
        criteria.setPublicIdentifier(experimentIdentifier);
        SearchResult<Experiment> experiments;
        try {
            experiments = searchService.searchForExperiments(criteria, null);
        } catch (InvalidInputException e) {
            throw new ExperimentNotFoundException(getExperimentNotFoundMessage(experimentIdentifier), e);
        }
        if (experiments.getResults().isEmpty()) {
            throw new ExperimentNotFoundException(getExperimentNotFoundMessage(experimentIdentifier));
        } else {
            return experiments.getResults().get(0);
        }
    }

    private static String getExperimentNotFoundMessage(String experimentIdentifier) {
        return "Experiment '" + experimentIdentifier + "' could not be found";
    }
    
    static List<File> getFiles(SearchService searchService, FileSearchCriteria criteria)
        throws InvalidInputException {
        return new JavaSearchApiUtils(searchService).filesByCriteria(criteria).list();
    }    

}
