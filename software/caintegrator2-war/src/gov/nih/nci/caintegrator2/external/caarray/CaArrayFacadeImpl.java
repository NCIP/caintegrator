/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caarray.external.v1_0.experiment.Experiment;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CaArrayFacade subsystem.
 */
public class CaArrayFacadeImpl implements CaArrayFacade {

    private static final String ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE = "Couldn't retrieve the requested array data";
    private CaArrayServiceFactory serviceFactory;
    private CaIntegrator2Dao dao;

    /**
     * {@inheritDoc}
     */
    public List<Sample> getSamples(String experimentIdentifier, ServerConnectionProfile profile) 
    throws ConnectionException, ExperimentNotFoundException {
        SearchService searchService = getServiceFactory().createSearchService(profile);
        return getSamples(searchService, experimentIdentifier);
    }

    private List<Sample> getSamples(SearchService searchService, String experimentIdentifier) 
    throws ExperimentNotFoundException {
        List<Sample> samples = new ArrayList<Sample>();
        for (Biomaterial experimentSample 
                : getCaArraySamples(experimentIdentifier, searchService)) {
            samples.add(translateSample(experimentSample));
        }
        return samples;
    }

    private List<Biomaterial> getCaArraySamples(String experimentIdentifier, 
            SearchService searchService) throws ExperimentNotFoundException {
        return CaArrayUtils.getSamples(experimentIdentifier, searchService);
    }

    private Sample translateSample(Biomaterial loadedSample) {
        Sample sample = new Sample();
        sample.setName(loadedSample.getName());
        return sample;
    }

    /**
     * @return the serviceFactory
     */
    public CaArrayServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    /**
     * @param serviceFactory the serviceFactory to set
     */
    public void setServiceFactory(CaArrayServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }
    
    /**
     * {@inheritDoc}
     */
    public ArrayDataValues retrieveData(GenomicDataSourceConfiguration genomicSource) 
    throws ConnectionException, DataRetrievalException {
        AbstractDataRetrievalHelper dataRetrievalHelper = getDataRetrievalHelper(genomicSource);
        try {
            return dataRetrievalHelper.retrieveData();
        } catch (InvalidInputException e) {
            throw new DataRetrievalException(ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE, e);
        } catch (InconsistentDataSetsException e) {
            throw new DataRetrievalException(ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE, e);
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Couldn't retrieve the array data file", e);
        }
    }

    private AbstractDataRetrievalHelper getDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource) 
    throws ConnectionException, DataRetrievalException {
        SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
        DataService dataService = 
            getServiceFactory().createDataService(genomicSource.getServerProfile());
        switch (PlatformVendorEnum.getByValue(genomicSource.getPlatformVendor())) {
        case AFFYMETRIX:
            return new AffymetrixDataRetrievalHelper(genomicSource, dataService,
                    searchService, dao);
        case AGILENT:
            return new AgilentDataRetrievalHelper(genomicSource, dataService,
                 searchService, dao);
        default:
            throw new DataRetrievalException("Unknown platform vendor.");
        }
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] retrieveFile(GenomicDataSourceConfiguration genomicSource, String filename) 
    throws FileNotFoundException, ConnectionException {
        File dataFile = getFile(genomicSource, filename);
        CaArrayEntityReference fileRef = dataFile.getReference();
        DataService dataService = getServiceFactory().createDataService(genomicSource.getServerProfile());
        return CaArrayUtils.retrieveFile(dataService, fileRef);
    }

    private File getFile(GenomicDataSourceConfiguration genomicSource, String filename) 
    throws ConnectionException, FileNotFoundException {
        List<File> results = retrieveFilesForGenomicSource(genomicSource);
        for (File file : results) {
            if (filename.equals(file.getMetadata().getName())) {
                return file;
            }
        }
        throw new FileNotFoundException("The experiment did not contain a file named " + filename);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.PreserveStackTrace")     // FileNotFoundException doesn't include a source Throwable
    public List<File> retrieveFilesForGenomicSource(GenomicDataSourceConfiguration genomicSource)
        throws ConnectionException, FileNotFoundException {
        try {
            SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
            Experiment experiment = CaArrayUtils.getExperiment(genomicSource.getExperimentIdentifier(), searchService);
            FileSearchCriteria criteria = new FileSearchCriteria();
            criteria.setExperiment(experiment.getReference());
            return searchService.searchForFiles(criteria, null).getResults();
        } catch (InvalidInputException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (ExperimentNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void validateGenomicSourceConnection(GenomicDataSourceConfiguration genomicSource) 
        throws ConnectionException, ExperimentNotFoundException {
        SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
        CaArrayUtils.getExperiment(genomicSource.getExperimentIdentifier(), searchService);
    }

}
