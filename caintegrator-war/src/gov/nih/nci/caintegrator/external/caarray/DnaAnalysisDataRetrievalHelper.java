/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.QuantitationTypeSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Responsible for retrieving array data from caArray.
 */
class DnaAnalysisDataRetrievalHelper extends AbstractDataRetrievalHelper {

    private static final Logger LOGGER = Logger.getLogger(DnaAnalysisDataRetrievalHelper.class);
    private static final ReporterTypeEnum REPORTER_TYPE = ReporterTypeEnum.DNA_ANALYSIS_REPORTER;
    private List<ArrayDataValues> arrayDataValuesList;
    
    DnaAnalysisDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            DataService dataService, SearchService searchService, CaIntegrator2Dao dao, 
            ArrayDataService arrayDataService) {
        super(genomicSource, dataService, searchService, dao, arrayDataService);
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    @Override
    ReporterTypeEnum getReporterType() {
        return REPORTER_TYPE;
    }
    
    protected void retrieveData() 
    throws ConnectionException, DataRetrievalException, InconsistentDataSetsException, InvalidInputException {
    
        DataSet dataSet = getDataService().getDataSet(createRequest());
        if (dataSet.getDatas().isEmpty()) {
            throw new DataRetrievalException("No log2 ratio available for experiment: "
                    + getGenomicSource().getExperimentIdentifier());
        }
        Hybridization hybridization = dataSet.getDatas().get(0).getHybridization();
        setPlatformHelper(new PlatformHelper(getPlatform(hybridization)));
        arrayDataValuesList = new ArrayList<ArrayDataValues>();
        convertToArrayDataValuesList(dataSet);
    }
    

    @Override
    protected QuantitationType getSignal(DataSetRequest request) throws InvalidInputException {
        // Select the quantitation types (columns) of Log2Ratio.
        QuantitationTypeSearchCriteria qtCrit = new QuantitationTypeSearchCriteria();
        qtCrit.setHybridization(request.getHybridizations().iterator().next());
        List<QuantitationType> qtypes = getSearchService().searchForQuantitationTypes(qtCrit);
        for (QuantitationType qt : qtypes) {
            if ("DataMatrixCopyNumber.Log2Ratio".equalsIgnoreCase(qt.getName())) {
                return qt;
            }
        }
        return null;
    }
    
    private void convertToArrayDataValuesList(DataSet dataSet) 
    throws DataRetrievalException, InvalidInputException {
        fillSampleToHybridizationDataMap(dataSet);
        for (Sample sample : getSampleToHybridizationDataMap().keySet()) {
            ArrayData arrayData = createArrayData(sample);
            getDao().save(arrayData);
            ArrayDataValues arrayDataValues = new ArrayDataValues(
                    getPlatformHelper().getAllReportersByType(REPORTER_TYPE));
            arrayDataValuesList.add(arrayDataValues);
            
            storeArrayDataValues(dataSet, sample, arrayData, arrayDataValues);
            getArrayDataService().save(arrayDataValues);
            arrayDataValues.clearMaps();
        }
    }

    private void storeArrayDataValues(DataSet dataSet, Sample sample, ArrayData arrayData,
            ArrayDataValues arrayDataValues) {
        List<DesignElement> probeSets = dataSet.getDesignElements();
        List<HybridizationData> hybridizationDatas = getSampleToHybridizationDataMap().get(sample);
        List<float[]> allHybridizationsValues = retrieveAllHybridizationValues(hybridizationDatas);
        for (int i = 0; i < probeSets.size(); i++) {
            AbstractReporter reporter = getReporter(probeSets.get(i));
            if (reporter == null) {
                String probeSetName = ((DesignElement) probeSets.get(i)).getName();
                getLogger().warn("Reporter with name " + probeSetName + " was not found in platform " 
                        + getPlatformHelper().getPlatform().getName());
            } else {
                List<Float> floatValues = new ArrayList<Float>();
                for (float[] values : allHybridizationsValues) {
                    floatValues.add(values[i]);
                }
                arrayDataValues.setFloatValue(arrayData, reporter, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO,
                        floatValues, getCentralTendencyCalculator());
            }
        }
    }
    
    private ArrayData createArrayData(Sample sample) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.COPY_NUMBER);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        arrayData.setStudy(getGenomicSource().getStudyConfiguration().getStudy());
        Array array = new Array();
        array.getArrayDataCollection().add(arrayData);
        arrayData.setArray(array);
        array.getSampleCollection().add(sample);
        sample.getArrayCollection().add(array);
        if (!getReporterList(REPORTER_TYPE).isEmpty()) {
            arrayData.getReporterLists().addAll(getReporterList(REPORTER_TYPE));
            for (ReporterList reporterList : getReporterList(REPORTER_TYPE)) {
                reporterList.getArrayDatas().add(arrayData);    
            }
            array.setPlatform(getReporterList(REPORTER_TYPE).iterator().next().getPlatform());
        }
        return arrayData;
    }

    /**
     * @return the arrayDataValuesList
     */
    protected List<ArrayDataValues> getArrayDataValuesList() {
        return arrayDataValuesList;
    }
    
}
