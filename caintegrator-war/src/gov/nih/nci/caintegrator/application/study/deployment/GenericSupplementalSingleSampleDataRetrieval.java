/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.GenericSingleSamplePerFileParser;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Reads data in supplemental raw data file.
 */
public final class GenericSupplementalSingleSampleDataRetrieval {

    private final SupplementalDataFile supplementalDataFile;
    private final ReporterTypeEnum reporterType;
    private final ArrayDataValueType dataType;

    /**
     * @param supplementalDataFile the supplemental file
     * @param reporterType the reporter type
     * @param dataType the data type
     */
    public GenericSupplementalSingleSampleDataRetrieval(SupplementalDataFile supplementalDataFile,
            ReporterTypeEnum reporterType, ArrayDataValueType dataType) {
        this.supplementalDataFile = supplementalDataFile;
        this.reporterType = reporterType;
        this.dataType = dataType;
    }

    private static final Logger LOGGER = Logger.getLogger(GenericSupplementalSingleSampleDataRetrieval.class);

    /**
     * Parsing the level 2 data file.
     * @param values ArrayDataValues to be populated.
     * @param arrayData ArrayData mapping.
     * @param platformHelper the platformHelper.
     * @param centralTendencyCalculator to calculate central tendency when there is more than one value
     *                                      per sample/reporter.
     * @throws DataRetrievalException when unable to parse.
     */
    public void parseDataFile(ArrayDataValues values, ArrayData arrayData,
            PlatformHelper platformHelper, CentralTendencyCalculator centralTendencyCalculator)
    throws DataRetrievalException {
        Map<String, float[]> dataMap = GenericSingleSamplePerFileParser.INSTANCE.extractData(
                supplementalDataFile, platformHelper.getPlatform().getVendor());
        loadArrayDataValues(dataMap, values, arrayData, platformHelper, centralTendencyCalculator);
    }

    private void loadArrayDataValues(Map<String, float[]> dataMap, ArrayDataValues values,
            ArrayData arrayData, PlatformHelper platformHelper, CentralTendencyCalculator centralTendencyCalculator) {
        for (String probeName : dataMap.keySet()) {
            AbstractReporter reporter = platformHelper.getReporter(reporterType, probeName);
            if (reporter == null) {
                LOGGER.warn("Reporter with name " + probeName + " was not found in platform "
                        + platformHelper.getPlatform().getName());
            } else {
                values.setFloatValue(arrayData, reporter, dataType, dataMap.get(probeName), centralTendencyCalculator);
            }
        }
    }
}
