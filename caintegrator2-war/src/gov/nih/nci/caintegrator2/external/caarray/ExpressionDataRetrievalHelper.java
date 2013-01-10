/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DataType;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.ExampleSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformSignalNameEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import org.apache.log4j.Logger;

/**
 * Responsible for retrieving array data from caArray.
 */
class ExpressionDataRetrievalHelper extends AbstractDataRetrievalHelper {

    private static final ReporterTypeEnum REPORTER_TYPE = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET;
    private static final Logger LOGGER = Logger.getLogger(ExpressionDataRetrievalHelper.class);
    private ArrayDataValues arrayDataValues;
    
    ExpressionDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
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
            throw new DataRetrievalException("No hybridization values available for experiment: "
                    + getGenomicSource().getExperimentIdentifier());
        }
        Hybridization hybridization = dataSet.getDatas().get(0).getHybridization();
        setPlatformHelper(new PlatformHelper(getPlatform(hybridization)));

        arrayDataValues = new ArrayDataValues(getPlatformHelper().getAllReportersByType(REPORTER_TYPE));
        convertToArrayDataValues(dataSet);
    }

    private void convertToArrayDataValues(DataSet dataSet) 
    throws DataRetrievalException, InvalidInputException {
        fillSampleToHybridizationDataMap(dataSet);
        for (Sample sample : getSampleToHybridizationDataMap().keySet()) {
            loadArrayDataValues(sample, dataSet);
        }
    }
    

    private void loadArrayDataValues(Sample sample, DataSet dataSet) 
    throws InvalidInputException {
        List<HybridizationData> hybridizationDatas = getSampleToHybridizationDataMap().get(sample);
        ArrayData arrayData = createArrayData(sample, hybridizationDatas.get(0).getHybridization().getName());
        List<DesignElement> probeSets = dataSet.getDesignElements();
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
                setValue(arrayData, reporter, floatValues);
            }
        }
    }

    private ArrayData createArrayData(Sample sample, String arrayName) 
    throws InvalidInputException {
        Array array = new Array();
        array.setPlatform(getPlatformHelper().getPlatform());
        array.setName(arrayName);
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        Set<ReporterList> reporterLists = getPlatformHelper().getReporterLists(REPORTER_TYPE);
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

    @Override
    protected QuantitationType getSignal(DataSetRequest request) throws InvalidInputException {
        QuantitationType signal = new QuantitationType();
        signal.setName(getSignalTypeName());
        signal.setDataType(DataType.FLOAT);
        ExampleSearchCriteria<QuantitationType> criteria = new ExampleSearchCriteria<QuantitationType>();
        criteria.setExample(signal);
        return getSearchService().searchByExample(criteria, null).getResults().get(0);
    }

    protected void setValue(ArrayData arrayData, AbstractReporter reporter, List<Float> values) {
        arrayDataValues.setFloatValue(arrayData, 
                reporter, ArrayDataValueType.EXPRESSION_SIGNAL, values, getCentralTendencyCalculator());
        
    }

    /**
     * @return the arrayDataValues
     */
    public ArrayDataValues getArrayDataValues() {
        return arrayDataValues;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator2.external.caarray.AbstractDataRetrievalHelper#getSignalTypeName()
     */
    @Override
    public String getSignalTypeName() {
        if (getGenomicSource().getPlatformVendor().equals(PlatformVendorEnum.AGILENT)) {
            PlatformChannelTypeEnum holdPlatFormChannelType = getDao()
                                                .getPlatformConfiguration(getGenomicSource().getPlatformName())
                                                .getPlatformChannelType();
            if (holdPlatFormChannelType
                        .equals(PlatformChannelTypeEnum.ONE_COLOR)) {
                return PlatformSignalNameEnum.ONE_COLOR_SIGNAL_NAME.getValue();
            } else if (holdPlatFormChannelType
                      .equals(PlatformChannelTypeEnum.TWO_COLOR)) {
                return PlatformSignalNameEnum.TWO_COLOR_SIGNAL_NAME.getValue();
            }
        }
        return PlatformSignalNameEnum.AFFYMETRIX_SIGNAL_NAME.getValue();
    }

}
