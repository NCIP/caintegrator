/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the CaArrayFacade subsystem.
 */
public class CaArrayFacadeImpl implements CaArrayFacade {

    private static final String ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE =
        "Couldn't retrieve the requested array data due to the following error from caArray:  ";
    private CaArrayServiceFactory serviceFactory;
    private CaIntegrator2Dao dao;

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
    public ArrayDataValues retrieveData(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, DataRetrievalException {
        AbstractDataRetrievalHelper dataRetrievalHelper = getDataRetrievalHelper(genomicSource, null);
        try {
            dataRetrievalHelper.retrieveData();
            return ((ExpressionDataRetrievalHelper) dataRetrievalHelper).getArrayDataValues();
        } catch (InvalidInputException e) {
            throw new DataRetrievalException(ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE, e);
        } catch (InconsistentDataSetsException e) {
            throw new DataRetrievalException(ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE, e);
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Couldn't retrieve the array data file", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ArrayDataValues> retrieveDnaAnalysisData(GenomicDataSourceConfiguration genomicSource,
            ArrayDataService arrayDataService)
    throws ConnectionException, DataRetrievalException {
        AbstractDataRetrievalHelper dataRetrievalHelper = getDataRetrievalHelper(genomicSource, arrayDataService);
        try {
            dataRetrievalHelper.retrieveData();
            return ((DnaAnalysisDataRetrievalHelper) dataRetrievalHelper).getArrayDataValuesList();
        } catch (InvalidInputException e) {
            throw new DataRetrievalException(ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE, e);
        } catch (InconsistentDataSetsException e) {
            throw new DataRetrievalException(ARRAY_DATA_RETRIEVAL_ERROR_MESSAGE, e);
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Couldn't retrieve the array data file", e);
        }
    }

    private AbstractDataRetrievalHelper getDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            ArrayDataService arrayDataService)
    throws ConnectionException, DataRetrievalException {
        SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
        DataService dataService =
            getServiceFactory().createDataService(genomicSource.getServerProfile());
        boolean isSupportedExpressionData = (PlatformVendorEnum.AFFYMETRIX.equals(genomicSource.getPlatformVendor())
                || PlatformVendorEnum.AGILENT.equals(genomicSource.getPlatformVendor()))
                && genomicSource.isExpressionData();
        if (isSupportedExpressionData) {
            return new ExpressionDataRetrievalHelper(genomicSource, dataService, searchService, dao, arrayDataService);
        } else if (PlatformVendorEnum.AGILENT.equals(genomicSource.getPlatformVendor())
                && genomicSource.isCopyNumberData()) {
            return new DnaAnalysisDataRetrievalHelper(genomicSource, dataService, searchService, dao,
                    arrayDataService);
        }
        throw new DataRetrievalException("Unsupported platform vendor: " + genomicSource.getPlatformVendor()
                + " and type " + genomicSource.getDataTypeString());
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
    @Override
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
    @Override
    @SuppressWarnings("PMD.PreserveStackTrace")     // FileNotFoundException doesn't include a source Throwable
    public List<File> retrieveFilesForGenomicSource(GenomicDataSourceConfiguration genomicSource)
        throws ConnectionException, FileNotFoundException {
        try {
            SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
            Experiment experiment = CaArrayUtils.getExperiment(genomicSource.getExperimentIdentifier(), searchService);
            FileSearchCriteria criteria = new FileSearchCriteria();
            criteria.setExperiment(experiment.getReference());
            return CaArrayUtils.getFiles(searchService, criteria);
        } catch (InvalidInputException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (ExperimentNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGenomicSourceConnection(GenomicDataSourceConfiguration genomicSource)
        throws ConnectionException, ExperimentNotFoundException {
        SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
        CaArrayUtils.getExperiment(genomicSource.getExperimentIdentifier(), searchService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Date> checkForSampleUpdates(String experimentIdentifier, ServerConnectionProfile profile)
            throws ConnectionException, ExperimentNotFoundException {
        SearchService searchService = getServiceFactory().createSearchService(profile);
        Map<String, Date> sampleMap = new HashMap<String, Date>();
        for (Biomaterial experimentSample
                : getCaArraySamples(experimentIdentifier, searchService)) {
            sampleMap.put(experimentSample.getName(), experimentSample.getLastModifiedDataTime());
        }
        return sampleMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastDataModificationDate(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
        ExperimentNotFoundException {
        SearchService searchService = getServiceFactory().createSearchService(genomicSource.getServerProfile());
        Experiment experiment = CaArrayUtils.getExperiment(genomicSource.getExperimentIdentifier(), searchService);
        return experiment.getLastDataModificationDate();
    }
}
