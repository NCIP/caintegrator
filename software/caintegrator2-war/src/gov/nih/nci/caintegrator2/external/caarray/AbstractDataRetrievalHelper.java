/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.array.ArrayDesign;
import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.FloatColumn;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.HybridizationSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.UnsupportedCategoryException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Responsible for retrieving array data from caArray.
 */
abstract class AbstractDataRetrievalHelper {
    
    private final GenomicDataSourceConfiguration genomicSource;
    private final DataService dataService;
    private final SearchService searchService;
    private final CaIntegrator2Dao dao;
    private final CentralTendencyCalculator centralTendencyCalculator;
    private final ArrayDataService arrayDataService;
    private Map<Hybridization, Sample> hybridizationToSampleMap;
    private Map<String, Biomaterial> nameToCaArraySampleMap;
    private Map<String, Hybridization> idToHybridizationMap;
    private PlatformHelper platformHelper;
    private final Map<Sample, List<HybridizationData>> sampleToHybridizationDataMap =
        new HashMap<Sample, List<HybridizationData>>();
    private String signalTypeName;
    
    AbstractDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            DataService dataService, SearchService searchService, CaIntegrator2Dao dao, 
            ArrayDataService arrayDataService) {
                this.genomicSource = genomicSource;
                this.dataService = dataService;
                this.searchService = searchService;
                this.dao = dao;
                this.arrayDataService = arrayDataService;
                this.centralTendencyCalculator = new CentralTendencyCalculator(
                        genomicSource.getTechnicalReplicatesCentralTendency(), 
                        genomicSource.isUseHighVarianceCalculation(), 
                        genomicSource.getHighVarianceThreshold(), 
                        genomicSource.getHighVarianceCalculationType());
    }
    
    abstract Logger getLogger();

    abstract ReporterTypeEnum getReporterType();

    protected DataSetRequest createRequest() throws InvalidInputException {
        DataSetRequest request = new DataSetRequest();
        for (Hybridization hybridization : getAllHybridizations()) {
            if (getGenomicSource().getPlatformName().equals(hybridization.getArrayDesign().getName())) {
                request.getHybridizations().add(hybridization.getReference());
            }
        }
        if (request.getHybridizations().isEmpty()) {
            throw new InvalidInputException("No caArray data found with Array Design: "
                    + getGenomicSource().getPlatformName());
        }
        request.getQuantitationTypes().add(getSignal(request).getReference());
        return request;
    }
    
    abstract QuantitationType getSignal(DataSetRequest request) throws InvalidInputException;

    abstract void retrieveData() 
    throws ConnectionException, DataRetrievalException, InconsistentDataSetsException, FileNotFoundException, 
        InvalidInputException;
    
    /**
     * @param dataSet
     * @throws InvalidInputException
     */
    protected void fillSampleToHybridizationDataMap(DataSet dataSet) throws InvalidInputException {
        getSampleToHybridizationDataMap().clear();
        for (HybridizationData hybridizationData : dataSet.getDatas()) {
            Sample sample = getAssociatedSample(hybridizationData.getHybridization());
            if (getSampleToHybridizationDataMap().get(sample) == null) {
                getSampleToHybridizationDataMap().put(sample, new ArrayList<HybridizationData>());
            }
            getSampleToHybridizationDataMap().get(sample).add(hybridizationData);
        }
    }

    /**
     * @param hybridizationDatas
     * @return
     */
    protected List<float[]> retrieveAllHybridizationValues(List<HybridizationData> hybridizationDatas) {
        List<float[]> allHybridizationsValues = new ArrayList<float[]>();
        for (HybridizationData hybridizationData : hybridizationDatas) {
            float[] values = ((FloatColumn) hybridizationData.getDataColumns().get(0)).getValues();
            allHybridizationsValues.add(values);
        }
        return allHybridizationsValues;
    }

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @return the searchService
     */
    public SearchService getSearchService() {
        return searchService;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    Map<Hybridization, Sample> getHybridizationToSampleMap() 
    throws InvalidInputException {
        if (hybridizationToSampleMap == null) {
            loadHybridizationToSampleMap();
        }
        return hybridizationToSampleMap;
    }

    protected Biomaterial getCaArraySample(Sample sample) 
    throws InvalidReferenceException, UnsupportedCategoryException {
        return getNameToCaArraySampleMap().get(sample.getName());
    }

    private Map<String, Biomaterial> getNameToCaArraySampleMap() {
        if (nameToCaArraySampleMap == null) {
            loadSampleToCaArraySampleMap();
        }
        return nameToCaArraySampleMap;
    }

    private void loadSampleToCaArraySampleMap() {
        nameToCaArraySampleMap = new HashMap<String, Biomaterial>();
        try {
            List<Biomaterial> samples = 
                CaArrayUtils.getSamples(getGenomicSource().getExperimentIdentifier(), searchService);
            for (Biomaterial biomaterial : samples) {
                nameToCaArraySampleMap.put(biomaterial.getName(), biomaterial);
            }
        } catch (ExperimentNotFoundException e) {
            throw new IllegalStateException("Couldn't retrieve valid experiment");
        }
    }

    protected Platform getPlatform(Hybridization hybridization) throws DataRetrievalException {
        ArrayDesign arrayDesign = hybridization.getArrayDesign();
        if (arrayDesign == null) {
            throw new DataRetrievalException(
                    "There is no array design associated with the array for the hybridization "
                    + hybridization.getName() + ", unable to load array data");
        }
        Platform platform = getDao().getPlatform(arrayDesign.getName());
        if (platform == null) {
            throw new DataRetrievalException("The platform named " + arrayDesign.getName() 
                    + " hasn't been loaded into the system");
        }
        return platform;
    }

    protected Set<Hybridization> getAllHybridizations() throws InvalidInputException {
        return getHybridizationToSampleMap().keySet();
    }

    private void loadHybridizationToSampleMap() throws InvalidInputException {
        hybridizationToSampleMap = new HashMap<Hybridization, Sample>();
        for (Sample sample : getGenomicSource().getSamples()) {
            Biomaterial caArraySample = getCaArraySample(sample);
            List<Hybridization> hybridizations = getHybridizations(caArraySample);
            addToSampleMap(sample, hybridizations);
        }
    }

    protected List<Hybridization> getHybridizations(Biomaterial caArraySample) throws InvalidInputException {
        HybridizationSearchCriteria criteria = new HybridizationSearchCriteria();
        Set<CaArrayEntityReference> sourceSet = new HashSet<CaArrayEntityReference>();
        sourceSet.add(caArraySample.getReference());
        criteria.setBiomaterials(sourceSet);
        return getSearchService().searchForHybridizations(criteria, null).getResults();
    }

    protected void addToSampleMap(Sample sample, List<Hybridization> hybridizations) {
        for (Hybridization hybridization : hybridizations) {
            hybridizationToSampleMap.put(hybridization, sample);
        }
    }

    protected AbstractReporter getReporter(String probeSetName) {
        AbstractReporter reporter = platformHelper.getReporter(getReporterType(), 
                probeSetName); 
        return reporter;
    }

    protected AbstractReporter getReporter(DesignElement designElement) {
        String probeSetName = designElement.getName();
        return getReporter(probeSetName);
    }
    
    protected Set<ReporterList> getReporterList(ReporterTypeEnum reporterType) {
        return getPlatformHelper().getReporterLists(reporterType);
    }

    protected Sample getAssociatedSample(Hybridization hybridization) 
    throws InvalidInputException {
        return getHybridizationToSampleMap().get(getIdToHybridizationMap().get(hybridization.getId()));
    }

    private Map<String, Hybridization> getIdToHybridizationMap() 
    throws InvalidInputException {
        if (idToHybridizationMap == null) {
            idToHybridizationMap = new HashMap<String, Hybridization>();
            for (Hybridization hybridization : getAllHybridizations()) {
                idToHybridizationMap.put(hybridization.getId(), hybridization);
            }
        }
        return idToHybridizationMap;
    }

    /**
     * @return the centralTendencyCalculator
     */
    protected CentralTendencyCalculator getCentralTendencyCalculator() {
        return centralTendencyCalculator;
    }

    /**
     * @return the platformHelper
     */
    public PlatformHelper getPlatformHelper() {
        return platformHelper;
    }

    /**
     * @param platformHelper the platformHelper to set
     */
    public void setPlatformHelper(PlatformHelper platformHelper) {
        this.platformHelper = platformHelper;
    }

    /**
     * @return the dataService
     */
    protected DataService getDataService() {
        return dataService;
    }

    /**
     * @return the sampleToHybridizationDataMap
     */
    protected Map<Sample, List<HybridizationData>> getSampleToHybridizationDataMap() {
        return sampleToHybridizationDataMap;
    }

    /**
     * @return the signalTypeName.  This is the name of column in the array
     * data that is retrieved from the genomic data source (caArray)
     * and is interpreted as the Signal Type.
     */
    public String getSignalTypeName() {
        return signalTypeName;
    }

    /**
     * @param signalTypeName the signalTypeName to set
     */
    public void setSignalTypeName(String signalTypeName) {
        this.signalTypeName = signalTypeName;
    }

}
