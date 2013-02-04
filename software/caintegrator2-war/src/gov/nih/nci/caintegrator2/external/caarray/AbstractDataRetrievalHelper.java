/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.array.ArrayDesign;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.query.HybridizationSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.UnsupportedCategoryException;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for retrieving array data from caArray.
 */
abstract class AbstractDataRetrievalHelper {
    
    private final GenomicDataSourceConfiguration genomicSource;
    private final SearchService searchService;
    private final CaIntegrator2Dao dao;
    private Map<Hybridization, Sample> hybridizationToSampleMap;
    private Map<String, Biomaterial> nameToCaArraySampleMap;
    private Map<String, Hybridization> idToHybridizationMap;
    private PlatformHelper platformHelper;
    private ArrayDataValues arrayDataValues;
    
    AbstractDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            SearchService searchService, CaIntegrator2Dao dao) {
                this.genomicSource = genomicSource;
                this.searchService = searchService;
                this.dao = dao;
    }
    
    protected void init() throws DataRetrievalException {
        arrayDataValues = new ArrayDataValues(platformHelper.
                    getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET));
    }

    abstract ArrayDataValues retrieveData() 
    throws ConnectionException, DataRetrievalException, InconsistentDataSetsException, FileNotFoundException, 
    InvalidInputException;

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
        AbstractReporter reporter = platformHelper.getReporter(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, 
                probeSetName); 
        return reporter;
    }

    protected AbstractReporter getReporter(DesignElement designElement) {
        String probeSetName = designElement.getName();
        return getReporter(probeSetName);
    }

    protected ArrayData createArrayData(Hybridization hybridization) 
    throws InvalidInputException {
        Array array = new Array();
        array.setPlatform(platformHelper.getPlatform());
        array.setName(hybridization.getName());
        Sample sample = getAssociatedSample(hybridization);
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        if (!reporterLists.isEmpty()) {
            arrayData.getReporterLists().addAll(reporterLists);
            for (ReporterList reporterList : reporterLists) {
                reporterList.getArrayDatas().add(arrayData);
            }
        }
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.setStudy(getGenomicSource().getStudyConfiguration().getStudy());
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        getDao().save(array);
        return arrayData;
    }

    private Sample getAssociatedSample(Hybridization hybridization) 
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

    protected void setValue(ArrayData arrayData, AbstractReporter reporter, float value) {
        arrayDataValues.setFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL, value);
    }

    /**
     * @return the arrayDataValues
     */
    public ArrayDataValues getArrayDataValues() {
        return arrayDataValues;
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

}
