/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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

/**
 * Reads data in Affymetrix CHP (CNCHP) files.
 */
class AffymetrixDnaAnalysisChpParser {
    
    private static final String LO2_RATIO = "Log2Ratio";

    private final File chpFile;
    private FusionCHPMultiDataData chpData;
    private FusionCHPData fusionChpData;

    private Map<String, AbstractReporter> reporterMap;

    static {
        FusionCHPMultiDataData.registerReader();
    }
    
    /**
     * Creates a new parser for the provided CHP file.
     * 
     * @param chpFile the CNCHP file.
     */
    AffymetrixDnaAnalysisChpParser(File chpFile) {
        this.chpFile = chpFile;
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
        values.setFloatValue(arrayData, 
                getReporter(probeSetData.getName()), 
                ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 
                getLog2Value(probeSetData));
    }

    private void loadData(ProbeSetMultiDataGenotypeData probeSetData, ArrayDataValues values, ArrayData arrayData) {
        values.setFloatValue(arrayData, 
                getReporter(probeSetData.getName()), 
                ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 
                getLog2Value(probeSetData));
    }

    private AbstractReporter getReporter(String name) {
        return reporterMap.get(name);
    }

    private float getLog2Value(ProbeSetMultiDataBase probeSetData) {
        for (ParameterNameValue nameValue : probeSetData.getMetrics()) {
            if (LO2_RATIO.equals(nameValue.getName())) {
                return nameValue.getValueFloat();
            }
        }
        return 0.0f;
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
        try {
            reader.readHeader(data, GenericFileReader.ReadHeaderOption.ReadNoDataGroupHeader);
            String name =  data
                .getHeader()
                .getGenericDataHdr()
                .findNameValParam("affymetrix-algorithm-param-ChipType1")
                .getValueAscii();
            reader.close();
            return name;
        } catch (InvalidVersionException e) {
            throw new DataRetrievalException("Invalid version from " + chpFile.getAbsolutePath(), e);
        } catch (InvalidFileTypeException e) {
            throw new DataRetrievalException("Invalid file type from " + chpFile.getAbsolutePath(), e);
        } catch (Exception e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + chpFile.getAbsolutePath(), e);
        }
    }
   
}
