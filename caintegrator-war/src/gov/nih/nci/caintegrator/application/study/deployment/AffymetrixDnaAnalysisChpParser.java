/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import affymetrix.calvin.data.GenericData;
import affymetrix.calvin.data.ProbeSetMultiDataBase;
import affymetrix.calvin.data.ProbeSetMultiDataCopyNumberData;
import affymetrix.calvin.data.ProbeSetMultiDataGenotypeData;
import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import affymetrix.calvin.exception.UnsignedOutOfLimitsException;
import affymetrix.calvin.parameter.ParameterNameValue;
import affymetrix.calvin.parsers.GenericFileReader;
import affymetrix.calvin.parsers.InvalidFileTypeException;
import affymetrix.calvin.parsers.InvalidVersionException;
import affymetrix.fusion.chp.FusionCHPData;
import affymetrix.fusion.chp.FusionCHPDataReg;
import affymetrix.fusion.chp.FusionCHPMultiDataData;

import org.apache.log4j.Logger;

/**
 * Reads data in Affymetrix CHP (CNCHP) files.
 */
class AffymetrixDnaAnalysisChpParser {
    private static final Logger LOGGER = Logger.getLogger(AffymetrixDnaAnalysisChpParser.class);
    
    private static final String LOG2_RATIO = "Log2Ratio";
    private static final String PARAM_CHIP_TYPE = "affymetrix-algorithm-param-ChipType1";
    private static final String PARAM_ARRAY_TYPE = "affymetrix-array-type";
    private final File chpFile;
    private FusionCHPMultiDataData chpData;
    private FusionCHPData fusionChpData;
    private final CentralTendencyCalculator centralTendencyCalculator;

    private Map<String, AbstractReporter> reporterMap;

    static {
        FusionCHPMultiDataData.registerReader();
    }
    
    /**
     * Creates a new parser for the provided CHP file.
     * 
     * @param chpFile the CNCHP file.
     */
    AffymetrixDnaAnalysisChpParser(File chpFile, CentralTendencyCalculator centralTendencyCalculator) {
        this.chpFile = chpFile;
        this.centralTendencyCalculator = centralTendencyCalculator;
    }

    void parse(ArrayDataValues values, ArrayData arrayData, MultiDataType multiDataType) throws DataRetrievalException {
        loadReporterMap(values);
        try {
            int numProbeSets = getChpData().getEntryCount(multiDataType);
            for (int i = 0; i < numProbeSets; i++) {
                if (MultiDataType.CopyNumberMultiDataType.equals(multiDataType)) {
                    ProbeSetMultiDataCopyNumberData probeSetData = 
                        getChpData().getCopyNumberEntry(multiDataType, i);
                    loadData(probeSetData, values, arrayData);
                } else {
                    ProbeSetMultiDataGenotypeData probeSetData =
                        getChpData().getGenotypeEntry(multiDataType, i);
                    loadData(probeSetData, values, arrayData);
                    
                }
            }
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + chpFile.getAbsolutePath(), e);
        } catch (UnsignedOutOfLimitsException e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + chpFile.getAbsolutePath(), e);
        }
    }

    private void loadReporterMap(ArrayDataValues values) {
        reporterMap = new HashMap<String, AbstractReporter>();
        for (AbstractReporter reporter : values.getReporters()) {
            reporterMap.put(reporter.getName(), reporter);
        }
    }

    private void loadData(ProbeSetMultiDataCopyNumberData probeSetData, ArrayDataValues values, ArrayData arrayData) {
        AbstractReporter reporter = getReporter(probeSetData.getName());
        if (reporter != null) {
                        values.setFloatValue(arrayData,
                                reporter,
                                ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO,
                                getLog2Value(probeSetData), centralTendencyCalculator);
        } else {
             LOGGER.error("Platform library does not contain this reporter: " + probeSetData.getName());
        }
    }

    private void loadData(ProbeSetMultiDataGenotypeData probeSetData, ArrayDataValues values, ArrayData arrayData) {
        AbstractReporter reporter = getReporter(probeSetData.getName());
        if (reporter != null) {
                        values.setFloatValue(arrayData,
                                reporter, 
                                ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 
                                getLog2Value(probeSetData), centralTendencyCalculator);
        } else {
            LOGGER.error("Platform library does not contain this reporter: " + probeSetData.getName());
        }
    }

    private AbstractReporter getReporter(String name) {
        return reporterMap.get(name);
    }

    private List<Float> getLog2Value(ProbeSetMultiDataBase probeSetData) {
        List<Float> log2Values = new ArrayList<Float>();
        for (ParameterNameValue nameValue : probeSetData.getMetrics()) {
            if (LOG2_RATIO.equals(nameValue.getName())) {
                log2Values.add(nameValue.getValueFloat());
            }
        }
        return log2Values;
    }

    private FusionCHPMultiDataData getChpData() {
        if (chpData == null) {
            chpData = FusionCHPMultiDataData.fromBase(getFusionChpData());            
        }
        return chpData;
    }
    
    private FusionCHPData getFusionChpData() {
        if (fusionChpData == null) {
            fusionChpData = FusionCHPDataReg.read(chpFile.getAbsolutePath());
        }
        return fusionChpData;
    }
    
    String getArrayDesignName() throws DataRetrievalException {
        GenericFileReader reader = new GenericFileReader();
        reader.setFilename(chpFile.getAbsolutePath());
        GenericData data = new GenericData();
        String arrayName;
        String paramName = PARAM_ARRAY_TYPE;
        try {
            reader.readHeader(data, GenericFileReader.ReadHeaderOption.ReadNoDataGroupHeader);
            if (data
                    .getHeader().getGenericDataHdr().findNameValParam(PARAM_CHIP_TYPE) != null) {
                paramName = PARAM_CHIP_TYPE;
                arrayName =  data.getHeader().getGenericDataHdr().findNameValParam(paramName).getValueAscii();
            } else {
                arrayName =  data.getHeader().getGenericDataHdr().findNameValParam(paramName).getValueText();
            }
            reader.close();
            return arrayName;
        } catch (InvalidVersionException e) {
            throw new DataRetrievalException("Couldn't retrieve the array name from the array design file.  "
                    + "Invalid version from " + chpFile.getAbsolutePath(), e);
        } catch (InvalidFileTypeException e) {
            throw new DataRetrievalException("Couldn't retrieve the array name from the array design file.  "
                    + "Invalid file type from " + chpFile.getAbsolutePath(), e);
        } catch (Exception e) {
            throw new DataRetrievalException("Couldn't retrieve the array name from the array design file.  "
                    + "Looking for parameter named " + paramName + ". Local file path is "
                    + chpFile.getAbsolutePath(), e);
        }
    }
   
}
