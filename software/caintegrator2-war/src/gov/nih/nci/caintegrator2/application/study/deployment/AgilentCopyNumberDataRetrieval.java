/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.AgilentLevelTwoDataMultiFileParser;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Reads data in Agilent raw data file.
 */
public final class AgilentCopyNumberDataRetrieval {

    /**
     * The INSTANCE of the AgilentRawDataFileParser.
     */
    public static final AgilentCopyNumberDataRetrieval INSTANCE = new AgilentCopyNumberDataRetrieval();
    
    private static final Logger LOGGER = Logger.getLogger(AgilentCopyNumberDataRetrieval.class);
    
    /**
     * Parsing the level 2 data file.
     * @param dataFile the level 2 file.
     * @param values ArrayDataValues to be populated.
     * @param arrayData ArrayData mapping.
     * @param platformHelper the platformHelper.
     * @throws DataRetrievalException when unable to parse.
     */
    public void parseDataFile(File dataFile, ArrayDataValues values, ArrayData arrayData,
            PlatformHelper platformHelper) throws DataRetrievalException {
        Map<String, Float> agilentDataMap = AgilentLevelTwoDataMultiFileParser.INSTANCE.extractData(dataFile);
        loadArrayDataValues(agilentDataMap, values, arrayData, platformHelper);
    }
    
    private void loadArrayDataValues(Map<String, Float> agilentDataMap, ArrayDataValues values,
            ArrayData arrayData, PlatformHelper platformHelper) {
        for (String probeName : agilentDataMap.keySet()) {
            AbstractReporter reporter = getReporter(platformHelper, probeName);
            if (reporter == null) {
                LOGGER.warn("Reporter with name " + probeName + " was not found in platform " 
                        + platformHelper.getPlatform().getName());
            } else {
                values.setFloatValue(arrayData, reporter, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO,
                        agilentDataMap.get(probeName).floatValue());
            }
        }
    }

    private AbstractReporter getReporter(PlatformHelper platformHelper, String probeSetName) {
        AbstractReporter reporter = platformHelper.getReporter(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, 
                probeSetName); 
        return reporter;
    }
}
