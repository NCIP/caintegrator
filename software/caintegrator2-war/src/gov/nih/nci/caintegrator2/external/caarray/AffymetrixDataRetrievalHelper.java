/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DataType;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.FloatColumn;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.ExampleSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Responsible for retrieving array data from caArray.
 */
class AffymetrixDataRetrievalHelper extends AbstractDataRetrievalHelper {

    private static final String CHP_SIGNAL_TYPE_NAME = "CHPSignal";
    private static final Logger LOGGER = Logger.getLogger(AffymetrixDataRetrievalHelper.class);
    private final DataService dataService;

    
    AffymetrixDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource,
            DataService dataService,
            SearchService searchService, CaIntegrator2Dao dao) {
        super(genomicSource, searchService, dao);
        this.dataService = dataService;
    }

    protected ArrayDataValues retrieveData() 
    throws ConnectionException, DataRetrievalException, InconsistentDataSetsException, InvalidInputException {
    
        DataSet dataSet = dataService.getDataSet(createRequest());
        if (dataSet.getDatas().isEmpty()) {
            throw new DataRetrievalException("No Chip signal available for experiment: "
                    + getGenomicSource().getExperimentIdentifier());
        }
        Hybridization hybridization = dataSet.getDatas().get(0).getHybridization();
        setPlatformHelper(new PlatformHelper(getPlatform(hybridization)));
        init();
        convertToArrayDataValues(dataSet);
        return getArrayDataValues();
    }

    private DataSetRequest createRequest() throws InvalidInputException {
        DataSetRequest request = new DataSetRequest();
        for (Hybridization hybridization : getAllHybridizations()) {
            request.getHybridizations().add(hybridization.getReference());
        }
        request.getQuantitationTypes().add(getSignal().getReference());
        return request;
    }

    private QuantitationType getSignal() throws InvalidInputException {
        QuantitationType signal = new QuantitationType();
        signal.setName(CHP_SIGNAL_TYPE_NAME);
        signal.setDataType(DataType.FLOAT);
        ExampleSearchCriteria<QuantitationType> criteria = new ExampleSearchCriteria<QuantitationType>();
        criteria.setExample(signal);
        return getSearchService().searchByExample(criteria, null).getResults().get(0);
    }

    private void convertToArrayDataValues(DataSet dataSet) 
    throws DataRetrievalException, InvalidInputException {
        for (HybridizationData hybridizationData : dataSet.getDatas()) {
            loadArrayDataValues(hybridizationData, dataSet);
        }
    }

    private void loadArrayDataValues(HybridizationData hybridizationData, DataSet dataSet) 
    throws InvalidInputException {
        ArrayData arrayData = createArrayData(hybridizationData.getHybridization());
        List<DesignElement> probeSets = dataSet.getDesignElements();
        float[] values = ((FloatColumn) hybridizationData.getDataColumns().get(0)).getValues();
        for (int i = 0; i < probeSets.size(); i++) {
            AbstractReporter reporter = getReporter(probeSets.get(i));
            if (reporter == null) {
                String probeSetName = ((DesignElement) probeSets.get(i)).getName();
                LOGGER.warn("Reporter with name " + probeSetName + " was not found in platform " 
                        + getPlatformHelper().getPlatform().getName());
            } else {
                setValue(arrayData, reporter, values[i]);
            }
        }
    }

}
