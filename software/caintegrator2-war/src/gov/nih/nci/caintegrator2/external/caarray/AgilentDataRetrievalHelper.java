/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidInputException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Responsible for retrieving array data from caArray.
 */
class AgilentDataRetrievalHelper extends AbstractDataRetrievalHelper {

    private static final Logger LOGGER = Logger.getLogger(AgilentDataRetrievalHelper.class);
    private final DataService dataService;

    /**
     * @param genomicSource
     * @param dataService
     * @param searchService
     * @param dao
     */
    AgilentDataRetrievalHelper(GenomicDataSourceConfiguration genomicSource, DataService dataService,
            SearchService searchService, CaIntegrator2Dao dao) {
        super(genomicSource, searchService, dao);
        this.dataService = dataService;
    }

    protected ArrayDataValues retrieveData() 
    throws DataRetrievalException, FileNotFoundException, ConnectionException, InvalidInputException {
        Set<Hybridization> hybridizationSet = getAllHybridizations();
        if (hybridizationSet.isEmpty()) {
            return new ArrayDataValues(new ArrayList<AbstractReporter>());
        }
        setPlatformHelper(new PlatformHelper(getDao().getPlatform(getGenomicSource().getPlatformName())));
        init();
        populateArrayDataValues(hybridizationSet);
        return getArrayDataValues();
    }
    
    private void populateArrayDataValues(Set<Hybridization> hybridizationSet) 
    throws DataRetrievalException, FileNotFoundException, 
    ConnectionException, InvalidInputException {
        for (Hybridization hybridization : hybridizationSet) {
            File dataFile = getDataFile(hybridization);
            byte[] byteArray = CaArrayUtils.retrieveFile(dataService, dataFile.getReference());
            ArrayData arrayData = createArrayData(hybridization);
            Map<String, Float> agilentDataMap = AgilentLevelTwoDataMultiFileParser.INSTANCE.extractData(
                    new InputStreamReader(new ByteArrayInputStream(byteArray)));
            loadArrayDataValues(agilentDataMap, arrayData);
        }
    }

    private File getDataFile(Hybridization hybridization) throws DataRetrievalException, InvalidInputException {
        FileSearchCriteria criteria = new FileSearchCriteria();
        Set<CaArrayEntityReference> nodes = new HashSet<CaArrayEntityReference>();
        criteria.setExperimentGraphNodes(nodes);
        nodes.add(hybridization.getReference());
        List<File> files =  getSearchService().searchForFiles(criteria, null).getResults();
        if (files.isEmpty()) {
            throw new DataRetrievalException("No matching file for hybridization.");
        }
        return files.get(0);
    }
    
    private void loadArrayDataValues(Map<String, Float> agilentDataMap, ArrayData arrayData) {
        for (String probeName : agilentDataMap.keySet()) {
            AbstractReporter reporter = getReporter(probeName);
            if (reporter == null) {
                LOGGER.warn("Reporter with name " + probeName + " was not found in platform " 
                        + getPlatformHelper().getPlatform().getName());
            } else {
                setValue(arrayData, reporter, agilentDataMap.get(probeName).floatValue());
            }
        }
    }

}
