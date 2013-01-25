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
import affymetrix.calvin.data.ProbeSetMultiDataCopyNumberData;
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
 * Reads data in Affymetrix copy number CHP (CNCHP) files.
 */
class AffymetrixCopyNumberChpParser {
    
    private static final String LO2_RATIO = "Log2Ratio";

    private final File copyNumberChpFile;
    private FusionCHPMultiDataData chpData;
    private FusionCHPData fusionChpData;

    private Map<String, AbstractReporter> reporterMap;

    static {
        FusionCHPMultiDataData.registerReader();
    }
    
    /**
     * Creates a new parser for the provided CNCHP file.
     * 
     * @param copyNumberChpFile the CNCHP file.
     */
    AffymetrixCopyNumberChpParser(File copyNumberChpFile) {
        this.copyNumberChpFile = copyNumberChpFile;
    }

    void parse(ArrayDataValues values, ArrayData arrayData) throws DataRetrievalException {
        loadReporterMap(values);
        try {
            int numProbeSets = getChpData().getEntryCount(MultiDataType.CopyNumberMultiDataType);
            for (int i = 0; i < numProbeSets; i++) {
                ProbeSetMultiDataCopyNumberData probeSetData = 
                    getChpData().getCopyNumberEntry(MultiDataType.CopyNumberMultiDataType, i);
                loadData(probeSetData, values, arrayData);
            }
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + copyNumberChpFile.getAbsolutePath(), e);
        } catch (UnsignedOutOfLimitsException e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + copyNumberChpFile.getAbsolutePath(), e);
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
                ArrayDataValueType.COPY_NUMBER_LOG2_RATIO, 
                getLog2Value(probeSetData));
    }

    private AbstractReporter getReporter(String name) {
        return reporterMap.get(name);
    }

    private float getLog2Value(ProbeSetMultiDataCopyNumberData probeSetData) {
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
            fusionChpData = FusionCHPDataReg.read(copyNumberChpFile.getAbsolutePath());
        }
        return fusionChpData;
    }

    String getArrayDesignName() throws DataRetrievalException {
        GenericFileReader reader = new GenericFileReader();
        reader.setFilename(copyNumberChpFile.getAbsolutePath());
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
            throw new DataRetrievalException("Invalid version from " + copyNumberChpFile.getAbsolutePath(), e);
        } catch (InvalidFileTypeException e) {
            throw new DataRetrievalException("Invalid file type from " + copyNumberChpFile.getAbsolutePath(), e);
        } catch (Exception e) {
            throw new DataRetrievalException("Couldn't retrieve data from " + copyNumberChpFile.getAbsolutePath(), e);
        }
    }
   
}
