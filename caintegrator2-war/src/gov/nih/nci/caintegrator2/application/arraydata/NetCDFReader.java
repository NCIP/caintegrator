/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Provides functionality to read NetCDF files.
 */
class NetCDFReader extends AbstractNetCdfFileHandler {
    
    private static final Logger LOGGER = Logger.getLogger(NetCDFReader.class);

    private final DataRetrievalRequest request;
    private NetcdfFile reader;
    private List<List<AbstractReporter>> sequentialReporterLists;

    NetCDFReader(FileManager fileManager, DataRetrievalRequest request) {
        super(fileManager);
        this.request = request;
    }

    ArrayDataValues retrieveValues() {
        try {
            ArrayDataValues values = new ArrayDataValues(request.getReporters());
            if (ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER.equals(request.getReporterType())) {
                openNetCdfFile(request.getStudy(),
                        request.getArrayDatas().iterator().next().getReporterLists().iterator().next().getId(),
                        request.getReporterType());
            } else {
                openNetCdfFile(request.getStudy(), request.getPlatform().getId(), request.getReporterType());
            }
            for (ArrayDataValueType type : request.getTypes()) {
                loadValues(values, type);
            }
            closeNetCdfFile();
            return values;
        } catch (IOException e) {
            throw new ArrayDataStorageException("Couldn't read data file", e);
        } catch (InvalidRangeException e) {
            throw new ArrayDataStorageException("Couldn't read data file", e);
        }
    }

    private void openNetCdfFile(Study study, Long id, ReporterTypeEnum reporterType) throws IOException {
        reader = NetcdfFile.open(getFile(study, id, reporterType).getAbsolutePath());
    }

    private void closeNetCdfFile() throws IOException {
        reader.close();
    }

    private void loadValues(ArrayDataValues values, ArrayDataValueType type) throws IOException, InvalidRangeException {
        Variable variable = reader.findVariable(type.name());
        for (ArrayData arrayData : request.getArrayDatas()) {
            loadValues(values, variable, type, arrayData);
        }
    }

    private void loadValues(ArrayDataValues values, Variable variable, ArrayDataValueType type, ArrayData arrayData) 
    throws IOException, InvalidRangeException {
        if (Float.class.equals(type.getTypeClass())) {
            loadFloatValues(values, variable, type, arrayData);
        } else {
            throw new IllegalStateException("Unsupported data type " + type.getTypeClass().getName());
        }
    }

    private void loadFloatValues(ArrayDataValues values, Variable variable, ArrayDataValueType type, 
            ArrayData arrayData) 
    throws IOException, InvalidRangeException {
        for (List<AbstractReporter> reporters : getSequentialReporterLists()) {
            float[] floatValues = getFloatValues(variable, reporters, getArrayDataOffset(arrayData));
            values.setFloatValues(arrayData, reporters, type, floatValues);
        }
    }

    private Integer getArrayDataOffset(ArrayData arrayData) throws IOException {
        Integer offset = getArrayDataOffsets().get(arrayData.getId());
        if (offset == null) {
            String message = "NetCDF file "
                + getFile(request.getStudy(), request.getPlatform().getId(),
                        request.getReporterType()).getAbsolutePath()
                + " doesn't contain data for ArrayData with id " + arrayData.getId();
            LOGGER.error(message + ". ArrayData offsets: " + getArrayDataOffsets());
            throw new ArrayDataStorageException(message);
        }
        return offset;
    }

    private float[] getFloatValues(Variable variable, List<AbstractReporter> reporters, Integer arrayDataIndex) 
    throws IOException, InvalidRangeException {
        return (float[]) getValuesArray(variable, reporters, arrayDataIndex).get1DJavaArray(Float.class);
    }

    private Array getValuesArray(Variable variable, List<AbstractReporter> reporters, Integer arrayDataIndex) 
    throws IOException, InvalidRangeException {
        int[] origin = new int[2];
        origin[0] = arrayDataIndex;
        origin[1] = reporters.get(0).getDataStorageIndex();
        int[] size = new int[2];
        size[0] = 1;
        size[1] = reporters.size();
        return variable.read(origin, size);
    }

    private List<List<AbstractReporter>> getSequentialReporterLists() {
        List<AbstractReporter> allReporters = request.getReporters();
        if (sequentialReporterLists == null) {
            sequentialReporterLists = new ArrayList<List<AbstractReporter>>();
            int startIndex = 0;
            int endIndex = 0;
            int currentReporterIndex;
            int previousReporterIndex = request.getReporters().get(0).getDataStorageIndex();
            for (int i = 0; i < allReporters.size(); i++) {
                endIndex = i;
                currentReporterIndex = allReporters.get(i).getDataStorageIndex();
                if (currentReporterIndex - previousReporterIndex > 1) {
                    sequentialReporterLists.add(allReporters.subList(startIndex, endIndex));
                    startIndex = endIndex;
                }
                previousReporterIndex = currentReporterIndex;
            }
            sequentialReporterLists.add(allReporters.subList(startIndex, endIndex + 1));
        }
        return sequentialReporterLists;
    }

    @Override
    NetcdfFile getNetCdfFile() {
        return reader;
    }

}
